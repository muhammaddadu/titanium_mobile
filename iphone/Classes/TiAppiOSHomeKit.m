/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2014 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

#import "TiAppiOSHomeKit.h"
#import "TiUtils.h"
#import "TiAppiOSHomeProxy.h"

@implementation TiAppiOSHomeKit

// Initialise Homekit
- (id)init
{
    self = [super init];
    if (self) {
        self.homeManager = [[HMHomeManager alloc] init]; // has common database
        self.homeManager.delegate = self;
    }
    return self;
}

-(NSString*)apiName
{
    return @"Ti.App.iOS.HomeKitProxy";
}

// Function to add home
-(void)addHome:(id)args{
    NSString *homeName = [TiUtils stringValue:[args objectAtIndex:0]];
    NSLog(@"Adding home with name %@",homeName);
    
    [self.homeManager addHomeWithName:homeName completionHandler:^(HMHome *home, NSError *error){
        if (error == nil) {
            [self.homeManager updatePrimaryHome:home completionHandler:^(NSError *error){
                if (error) {
                    NSLog(@"Error updating primary home: %@", error);
                } else {
                    NSLog(@"Primary home updated.");
                }
            }];
        } else {
            NSLog(@"Error adding new home: %@", error);
        }
    }];
}

-(TiProxy *)getPrimaryHome:(id)args
{
    return [self primaryHome];
}

-(TiProxy *)primaryHome
{
    return [self wrapHome:self.homeManager.primaryHome];
}

-(NSArray *)getHomes:(id)args
{
    return [self homes];
}

-(NSArray *)homes
{    return [self wrapHomes:self.homeManager.homes];
}

- (void)homeManagerDidUpdateHomes:(HMHomeManager *)manager
{
    if (manager.primaryHome) {
        self.homeManager.primaryHome.delegate = self;
        
        for (HMAccessory *accessory in self.homeManager.primaryHome.accessories) {
            accessory.delegate = self;
        }
    }
    [self fireEvent:@"homeManagerDidUpdateHomes" withObject:@{@"homes":self.homes,@"primaryHome":self.primaryHome}];
}

- (NSMutableArray*) wrapHomes:(NSArray*)homes
{
    NSMutableArray *returnArray = [NSMutableArray array];
    for(HMHome *home in homes)
    {
        [returnArray addObject:[self wrapHome:home]];
    }
    return returnArray;
}

- (TiProxy*) wrapHome:(HMHome*)home
{
    return [[[TiAppiOSHomeProxy alloc] initWithHome:home andPageContext:[self pageContext]] autorelease];
}

#pragma mark - HMHomeDelegate
- (void)home:(HMHome *)home didAddAccessory:(HMAccessory *)accessory
{
    
    [self fireEvent:@"didAddAccessory" withObject:nil];
}

- (void)home:(HMHome *)home didRemoveAccessory:(HMAccessory *)accessory
{
    
    [self fireEvent:@"didRemoveAccessory" withObject:nil];
}

#pragma mark - HMAccessoryDelegate
- (void)accessoryDidUpdateReachability:(HMAccessory *)accessory
{
    
    [self fireEvent:@"accessoryDidUpdateReachability" withObject:nil];
}

@end

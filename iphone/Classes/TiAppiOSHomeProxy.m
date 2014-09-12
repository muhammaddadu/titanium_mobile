/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2014 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

#import "TiAppiOSHomeProxy.h"
#import "TiUtils.h"

@implementation TiAppiOSHomeProxy

-(id)initWithHome:(HMHome*)home andPageContext:(id<TiEvaluator>)context
{
    if(self = [super _initWithPageContext:context])
    {
        _home = home;
    }
    return self;
}

-(void)dealloc
{
    //RELEASE_TO_NIL(_home);
    [super dealloc];
}

-(NSString*)name
{
    return [_home name];
}

-(NSString*)getName:(id)args
{
    return [_home name];
}

-(void)setName:(id)args
{
    ENSURE_SINGLE_ARG(args, NSString);
    NSString *homeName = [TiUtils stringValue:args];
    [_home updateName:homeName completionHandler:^(NSError *error){
        if (error == nil) {
            NSLog(@"Home name updated");
        } else {
            NSLog(@"Error updating home name: %@", error);
        }
    }];
}

-(BOOL)isPrimary
{
    return [_home isPrimary];
}

//Accesories

-(NSArray*)getAccesories:(id)args
{
    return [_home accessories];
}

-(void)addAccesory:(id)args
{
    
}

-(void)assignAccesory:(id)args
{
    
}

-(void)removeAccessory:(id)args
{
    
}

-(void)unblockAccessory:(id)args
{
    
}

//Rooms
// To Do: create proxy
-(NSArray*)getRooms:(id)args
{
    //return [_home rooms];
}


-(void)getRoomForEntireHome:(id)args
{
    //return [_home roomForEntireHome];
}

-(void)addRoomWithName:(id)args
{
    ENSURE_SINGLE_ARG(args, NSString);
    NSString *roomName = [TiUtils stringValue:args];
    [_home addRoomWithName:(NSString *)roomName completionHandler:^(HMRoom *room, NSError *error){
        if(error==nil){ //success
            NSLog(@"Room added");
        }else{
            NSLog(@"Error adding room");
        }
    }];
}

-(void)removeRoom:(id)args
{
    
}

//Managing Zones
// To Do: create proxy
-(NSArray*)getZones:(id)args
{
    //[_home zones];
}

-(void)addZoneWithName:(id)args
{
    ENSURE_SINGLE_ARG(args, NSString);
    NSString *zoneName = [TiUtils stringValue:args];
    [_home addZoneWithName:zoneName completionHandler:^(HMZone *zone, NSError *error) {
        
    }];
}

-(void)removeZone:(id)args
{
    
}

//Managing Services
// To Do: create proxy
-(void)servicesWithTypes:(id)args
{
    
}

-(void)serviceGroups:(id)args
{
    
}

-(void)addServiceGroupWithName:(id)args
{
    ENSURE_SINGLE_ARG(args, NSString);
    NSString *serviceGroupName = [TiUtils stringValue:args];
    [_home addServiceGroupWithName:serviceGroupName completionHandler:^(HMServiceGroup *group, NSError *error) {
        
    }];
}

-(void)removeServiceGroup:(id)args
{
    
}


//Managing Actions
// To Do: create proxy
-(NSArray*)getActionSets:(id)args
{
//    return [_home actionSets];
}

-(void)addActionSetWithName:(id)args
{
    ENSURE_SINGLE_ARG(args, NSString);
    NSString *actionSetName = [TiUtils stringValue:args];
    [_home addActionSetWithName:actionSetName completionHandler:^(HMActionSet *actionSet, NSError *error) {
        
    }];
    
}

-(void)removeActionSet:(id)args
{
    
}

-(void)executeActionSet:(id)args
{
    
}

-(void)trigger:(id)args
{
    
}

-(void)addTrigger:(id)args
{
    
}

-(void)removeTrigger:(id)args
{
    
}


//Managing Users
// To Do: create proxy
-(void)addUserWithCompletionHandler:(id)args
{
    ENSURE_SINGLE_ARG(args, NSDictionary);
    NSString *userName = [TiUtils stringValue:args['name']];
    //[_home addUser:userName privilege:<#(HMHomeUserPrivilege)#> completionHandler:<#^(NSError *error)completion#>]
}

-(void)removeUser:(id)args
{
    
}

-(NSArray*)getUsers:(id)args
{
    //return [_home users];
}


@end

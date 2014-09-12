/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2014 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */
#import "TiProxy.h"
#import <HomeKit/HomeKit.h>

@interface TiAppiOSHomeKit : TiProxy<HMHomeManagerDelegate, HMHomeDelegate, HMAccessoryDelegate> {

}

@property (nonatomic, strong) HMHomeManager *homeManager;

-(void)addHome:(id)args;
-(NSString *)getPrimaryHome:(id)args;

@end

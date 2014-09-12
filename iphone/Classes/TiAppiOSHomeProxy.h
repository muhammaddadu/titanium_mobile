/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2014 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */
#import "TiProxy.h"
#import <HomeKit/HomeKit.h>

@interface TiAppiOSHomeProxy : TiProxy {
    HMHome *_home;
}

-(id)initWithHome:(HMHome*)home andPageContext:(id<TiEvaluator>)context;

@end

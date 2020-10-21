#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>

#import "UIScreen+DarkModeTraitChangeListener.h"

@interface DarkMode : RCTEventEmitter <RCTBridgeModule>

- (void)onModeChange:(NSString *)newMode;

@end

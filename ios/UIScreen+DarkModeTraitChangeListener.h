#import <UIKit/UIKit.h>

#import "DarkMode.h"

typedef NS_ENUM(NSInteger, DarkModeMode) {
    DarkModeModeLight,
    DarkModeModeDark,
};

@class DarkMode;

@interface UIScreen (DarkModeTraitChangeListener)

+ (NSString *)getCurrentMode;
+ (void)setCurrentManager:(DarkMode *)manager;

@end

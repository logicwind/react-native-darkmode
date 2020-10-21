#import <objc/runtime.h>

#import "UIScreen+DarkModeTraitChangeListener.h"

@implementation UIScreen (DarkModeTraitChangeListener)

static DarkModeMode currentMode = DarkModeModeLight;
static DarkMode *currentManager = NULL;

+ (void)load
{
    if (@available(iOS 13.0, *)) {
        static dispatch_once_t token;
        dispatch_once(&token, ^{
            SEL originalMethodSelector = @selector(traitCollectionDidChange:);
            SEL newMethodSelector = @selector(newTraitCollectionDidChange:);
            Method originalMethod = class_getInstanceMethod(self, originalMethodSelector);
            Method newMethod = class_getInstanceMethod(self, newMethodSelector);
            
            BOOL methodAdded = class_addMethod([self class], originalMethodSelector, method_getImplementation(newMethod), method_getTypeEncoding(newMethod));
            
            if (methodAdded) {
                class_replaceMethod([self class], newMethodSelector, method_getImplementation(originalMethod), method_getTypeEncoding(originalMethod));
            } else {
                method_exchangeImplementations(originalMethod, newMethod);
            }
        });
    }
}

- (void)newTraitCollectionDidChange:(UITraitCollection *)previousTraitCollection
{
    [self newTraitCollectionDidChange:previousTraitCollection];

    [UIScreen parseCurrentTraitCollection:self.traitCollection];
}

+ (void)parseCurrentTraitCollection:(UITraitCollection *)traitCollection
{
    if (@available(iOS 13.0, *)) {
        UIUserInterfaceStyle traitStyle = traitCollection.userInterfaceStyle;
        UIUserInterfaceStyle currentStyle = currentMode == DarkModeModeDark ? UIUserInterfaceStyleDark : UIUserInterfaceStyleLight;
        if (traitStyle != currentStyle) {
            DarkModeMode newMode = traitStyle == UIUserInterfaceStyleDark ? DarkModeModeDark : DarkModeModeLight;
            [UIScreen updateCurrentStyle:newMode];
        }
    }
}

+ (NSString *)getCurrentMode
{
    if (@available(iOS 13.0, *)) { 
        static BOOL hasRun = NO;
        if (!hasRun) {
            [UIScreen parseCurrentTraitCollection:UITraitCollection.currentTraitCollection];
            hasRun = YES;
        }
    }

    return [UIScreen parseCurrentMode];
}

+ (NSString *)parseCurrentMode
{
    if (currentMode == DarkModeModeDark) {
        return @"DARK";
    }

    return @"LIGHT";
}

+ (void)updateCurrentStyle:(DarkModeMode)newMode
{
    currentMode = newMode;
    if (currentManager) {
        [currentManager onModeChange:[UIScreen parseCurrentMode]];
    }
}

+ (void)setCurrentManager:(DarkMode *)manager
{
    currentManager = manager;
}

@end

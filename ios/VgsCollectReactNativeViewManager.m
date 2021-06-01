#import "React/RCTViewManager.h"

@interface RCT_EXTERN_MODULE(VgsCollectReactNativeViewManager, RCTViewManager)
 
RCT_EXPORT_VIEW_PROPERTY(config, NSDictionary)

RCT_EXPORT_VIEW_PROPERTY(isSecureTextEntry, BOOL)
RCT_EXPORT_VIEW_PROPERTY(placeholder, NSString)
RCT_EXPORT_VIEW_PROPERTY(fontFamily, NSString)
RCT_EXPORT_VIEW_PROPERTY(textColor, NSString)
RCT_EXPORT_VIEW_PROPERTY(fontSize, CGFloat)

@end

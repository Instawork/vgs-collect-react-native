#import "React/RCTViewManager.h"

@interface RCT_EXTERN_MODULE(CollectorManager, NSObject)

RCT_EXTERN_METHOD(createNamedCollector:(NSString *)name vaultId:(NSString *)vaultId environment:(NSString *)environment)

RCT_EXTERN_METHOD(submit:(NSString *)name
                  path:(NSString *)path
                  method:(NSString *)method
                  headers:(NSDictionary *)headers
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject
                  )
                  
RCT_EXTERN_METHOD(pinConfirm:(NSString *)name
                  path:(NSString *)path
                  method:(NSString *)method
                  headers:(NSDictionary *)headers
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject
                  )


@end

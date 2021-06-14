import VGSCollectSDK
import UIKit

@objc(CollectorManager)
class CollectorManager: NSObject {
 static var map: [String: VGSCollect] = [:];

 @objc(createNamedCollector:vaultId:environment:)
 func createNamedCollector(_ name: String, vaultId: String, environment: String) -> Void {
    
    if let existingInstance = CollectorManager.map[name] {
        existingInstance.unsubscribeAllTextFields();
        CollectorManager.map.removeValue(forKey: name);
    }
        
    CollectorManager.map[name] = VGSCollect(id: vaultId, environment: environment);
 }

    @objc(submit:path:method:headers:resolver:rejecter:)
    func submit(_ name: String, path: String, method: String, headers: Dictionary<String, String>, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) -> Void {
    print("Sbmit", name, headers);
        let collector = CollectorManager.map[name]!
        
        if (collector == nil) {
            reject("error", "invalid name", NSError(domain: "MissingCollectorByNameError", code: 401))
            return;
        }
        collector.customHeaders = headers;
    
        collector.sendData(
            path: path,
            method: method == "POST" ? .post : .get
        ) { [weak self](response) in
            switch response {
              case .success(let code, let data, let response):
                let jsonData = data != nil ? try? JSONSerialization.jsonObject(with: data!, options: []) as? [String: Any] : nil;
                print("VGS Collect done! code= \(code), data: \(jsonData), response: \(response)");
                resolve(["code": code, "data": jsonData, "response": response ]);
                
                return
              case .failure(let code, let data, let response, let error):
                print("VGS Collect failed! Details:\n code = \(code) data = \(data) response = \(response)) error = \(error)")
                reject("error", error?.localizedDescription, error ?? NSError(domain: "VGSCollectError", code: code));
                
              return
            }
        }
    }
}

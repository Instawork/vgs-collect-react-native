import UIKit
import VGSCollectSDK

@objc(VgsCollectReactNativeViewManager)
class VgsCollectReactNativeViewManager: RCTViewManager {

  override func view() -> (VgsCollectReactNativeView) {
    return VgsCollectReactNativeView()
  }
} 

class VgsCollectReactNativeView : UIView {
    let textField = VGSTextField()
    
    override init(frame: CGRect) {
         super.init(frame: frame)
         createVgsCollect();
     }
     
     required init?(coder aDecoder: NSCoder) {
         super.init(coder: aDecoder)
     }
    
    private func createVgsCollect() {
        let stackView = UIStackView.init(arrangedSubviews: [textField])
         stackView.axis = .vertical
         
         stackView.distribution = .fill
         stackView.translatesAutoresizingMaskIntoConstraints = false
         self.addSubview(stackView)
         
         NSLayoutConstraint.activate([
             stackView.leftAnchor.constraint(equalTo: self.leftAnchor),
             stackView.rightAnchor.constraint(equalTo: self.rightAnchor),
             stackView.topAnchor.constraint(equalTo: self.topAnchor),
             stackView.heightAnchor.constraint(equalTo: self.heightAnchor)
         ])
        
        textField.borderColor = .clear;
    }

    @objc var config: NSDictionary = NSDictionary() {
        didSet {
            let collectorName = config["collectorName"] as! String;
            let collector = CollectorManager.map[collectorName];
            
            if (collector != nil) {
                let fieldName = config["fieldName"] as! String;
                let fieldType = config["fieldType"] as! String;
                let configuration = VGSConfiguration(collector: collector!, fieldName: fieldName);

                let formatPattern = config["formatPattern"] as? String;
                if (formatPattern != nil) {
                    configuration.formatPattern = formatPattern;
                }
                
                let divider = config["divider"] as? String;
                if (divider != nil) {
                    configuration.divider = divider;
                }
                
                
                let keyboardType = config["keyboardType"] as? String;
                if (keyboardType != nil && keyboardType == "numberPad") {
                    configuration.keyboardType = .numberPad
                }

                if (fieldType == "cardHolderName") {
                  configuration.type = .cardHolderName
                } else if (fieldType == "expDate") {
                  configuration.type = .expDate
                } else if (fieldType == "cvc") {
                  configuration.type = .cvc
                } else if (fieldType == "cardNumber") {
                  configuration.type = .cardNumber
                } else {
                  configuration.type = .none
                }
                
                let rawValidations = config["validations"] as? NSArray;
                var rules = [VGSValidationRuleProtocol]()
                
                if (rawValidations != nil && rawValidations!.count > 0) {
                    for rawRule: NSDictionary in rawValidations as! [NSDictionary] {
                        let min = rawRule["min"] as? Int;
                        let max = rawRule["max"] as? Int;
                        let pattern = rawRule["pattern"] as? String;
                        
                        if (min != nil && max != nil) {
                            rules.append(VGSValidationRuleLength(min: min!, max: max!, error: "Invalid length"))
                        } else if (pattern != nil) {
                            rules.append(VGSValidationRulePattern(pattern: pattern!, error: "Invalid pattern"))
                        }
                    }
                }
                                
                if (rules.count > 0) {
                    configuration.validationRules = VGSValidationRuleSet(rules: rules);
                    configuration.isRequiredValidOnly = true;
                }

                textField.configuration = configuration;
            }
        }
    }

    @objc var fontFamily: String = "" {
        didSet {
            let font = UIFont.init(name: fontFamily, size: fontSize);
            textField.font = font;
        }
    }
    
    @objc var textColor: String = "" {
         didSet {
            textField.textColor = hexStringToUIColor(hex: textColor)
         }
    }
    
    @objc var placeholder: String = "" {
        didSet {
            textField.placeholder = placeholder;
        }
    }
    
    @objc var isSecureTextEntry: Bool = false {
        didSet {
            textField.isSecureTextEntry = isSecureTextEntry;
        }
    }
    
    @objc var fontSize: CGFloat = 16 {
        didSet {
            var font: UIFont?;
            
            if (fontFamily == "") {
                font = UIFont.systemFont(ofSize: fontSize);
            } else {
                font = UIFont.init(name: fontFamily, size: fontSize);
            }
            
            textField.font = font;
        }
    }

    func hexStringToUIColor (hex:String) -> UIColor {
        var cString:String = hex.trimmingCharacters(in: .whitespacesAndNewlines).uppercased()
        
        if (cString.hasPrefix("#")) {
            cString.remove(at: cString.startIndex)
        }
        
        if (cString.hasPrefix("TRANSPARENT")) {
            return UIColor.clear;
        }
        
        if ((cString.count) != 6) {
            return UIColor.gray
        }
        
        var rgbValue:UInt64 = 0
        Scanner(string: cString).scanHexInt64(&rgbValue)
        
        return UIColor(
            red: CGFloat((rgbValue & 0xFF0000) >> 16) / 255.0,
            green: CGFloat((rgbValue & 0x00FF00) >> 8) / 255.0,
            blue: CGFloat(rgbValue & 0x0000FF) / 255.0,
            alpha: CGFloat(1.0)
        )
    }
}

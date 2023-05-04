import UIKit
import VGSCollectSDK

@objc(VgsCollectReactNativeViewManager)
class VgsCollectReactNativeViewManager: RCTViewManager {

  override func view() -> (VgsCollectReactNativeView) {
    return VgsCollectReactNativeView()
  }

  override class func requiresMainQueueSetup() -> Bool {
    return true
  }
} 

class VgsCollectReactNativeView : UIView {
    var textField: VGSTextField?;
    
    override init(frame: CGRect) {
         super.init(frame: frame)
     }
     
     required init?(coder aDecoder: NSCoder) {
         super.init(coder: aDecoder)
     }
    
    private func createVgsCollect() {
      if (textField != nil) {
        let stackView = UIStackView.init(arrangedSubviews: [textField!])
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
        
        textField!.borderColor = .clear;
      }
    }

    @objc var config: NSDictionary = NSDictionary() {
        didSet {
            let collectorName = config["collectorName"] as! String;
            let collector = CollectorManager.map[collectorName];
            
            if (collector != nil) {
                let fieldName = config["fieldName"] as! String;
                let fieldType = config["fieldType"] as! String;
                var configuration: VGSConfiguration;
                
                if (fieldType == "expDate") {
                  textField = VGSExpDateTextField()
                  configuration = VGSExpDateConfiguration(collector: collector!, fieldName: fieldName);
                  configuration.type = .expDate
                
                    let inputDateFormat = config["inputDateFormat"] as! String;
                    
                    if (inputDateFormat == "longYear") {
                        (configuration as! VGSExpDateConfiguration).inputDateFormat = .longYear;
                    } else if (inputDateFormat == "shortYear") {
                        (configuration as! VGSExpDateConfiguration).inputDateFormat = .shortYear;
                    } else if (inputDateFormat == "shortYearThenMonth") {
                        (configuration as! VGSExpDateConfiguration).inputDateFormat = .shortYearThenMonth;
                    } else if (inputDateFormat == "longYearThenMonth") {
                       (configuration as! VGSExpDateConfiguration).inputDateFormat = .longYearThenMonth;
                    }
                    
                    let outputDateFormat = config["outputDateFormat"] as! String;
                    
                    if (outputDateFormat == "longYear") {
                        (configuration as! VGSExpDateConfiguration).outputDateFormat = .longYear;
                    } else if (outputDateFormat == "shortYear") {
                        (configuration as! VGSExpDateConfiguration).outputDateFormat = .shortYear;
                    } else if (outputDateFormat == "shortYearThenMonth") {
                        (configuration as! VGSExpDateConfiguration).outputDateFormat = .shortYearThenMonth;
                    } else if (outputDateFormat == "longYearThenMonth") {
                       (configuration as! VGSExpDateConfiguration).outputDateFormat = .longYearThenMonth;
                    }
                } else {
                    configuration = VGSConfiguration(collector: collector!, fieldName: fieldName);
                    
                    if (fieldType == "cardHolderName") {
                        textField = VGSTextField()
                        configuration.type = .cardHolderName;
                    } else if (fieldType == "cvc") {
                        textField = VGSCVCTextField()
                        configuration.type = .cvc;
                    } else if (fieldType == "cardNumber") {
                        textField = VGSCardTextField()
                        configuration.type = .cardNumber
                    } else {
                        textField = VGSTextField()
                        configuration.type = .none
                    }
                }
            

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

                if (fieldType == "pin" || fieldType == "pinConfirm"){
                    rules.append(VGSValidationRuleLength(min: 4, max: 4, error: "Invalid length"))
                }
                                
                if (rules.count > 0) {
                    configuration.validationRules = VGSValidationRuleSet(rules: rules);
                    configuration.isRequiredValidOnly = true;
                }

                textField!.configuration = configuration;
                self.fontFamily = { self.fontFamily }()
                self.textColor = { self.textColor }()
                self.placeholder = { self.placeholder }()
                self.isSecureTextEntry = { self.isSecureTextEntry }()
                self.fontSize = { self.fontSize }();
                createVgsCollect()
            }
        }
    }

    @objc var fontFamily: String = "" {
        didSet {
            if (textField != nil) {
                let font = UIFont.init(name: fontFamily, size: fontSize);
                textField!.font = font;
            }
        }
    }
    
    @objc var textColor: String = "" {
         didSet {
            if (textField != nil) {
                textField!.textColor = hexStringToUIColor(hex: textColor);
            }
         }
    }
    
    @objc var placeholder: String = "" {
        didSet {
            if (textField != nil) {
                textField!.placeholder = placeholder;
            }
        }
    }
    
    @objc var isSecureTextEntry: Bool = false {
        didSet {
            if (textField != nil) {
                textField!.isSecureTextEntry = isSecureTextEntry;
            }
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
            
            if (textField != nil) {
                textField!.font = font;
            }
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

package com.vgscollectreactnative

import android.graphics.Color
import android.graphics.Typeface
import android.text.InputType
import android.util.TypedValue
import android.view.View
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp
import com.facebook.react.views.text.ReactFontManager
import com.verygoodsecurity.vgscollect.view.card.validation.rules.VGSInfoRule
import com.verygoodsecurity.vgscollect.view.card.validation.rules.ValidationRule
import com.verygoodsecurity.vgscollect.widget.VGSEditText


import com.verygoodsecurity.vgscollect.widget.VGSCardNumberEditText
import com.verygoodsecurity.vgscollect.widget.ExpirationDateEditText
import com.verygoodsecurity.vgscollect.view.InputFieldView


class VgsCollectReactNativeViewManager : SimpleViewManager<View>() {
  override fun getName() = "VgsCollectReactNativeView"
  private lateinit var reactContext: ThemedReactContext;

  override fun createViewInstance(reactContext: ThemedReactContext): View {
    this.reactContext = reactContext;

    return VgsCollectFieldInstance(reactContext)
  }

  @ReactProp(name = "config")
  fun config(view: View, initParams: ReadableMap) {
    val collectorName = initParams.getString("collectorName");
    val instance = (view as VgsCollectFieldInstance);

    collectorName?.let { it ->
      initParams.getString("fieldType")?.let { fieldType ->
        if (fieldType == "expDate") {
          System.out.println("=====================================expDate");
          instance.setExpField();
          initParams.getString("inputDateFormat")?.let { inputDateFormat ->
            if (inputDateFormat == "longYear") {
              (instance.vgsField as ExpirationDateEditText).setDateRegex("MM/yyyy");
            } else if (inputDateFormat == "shortYear") {

              (instance.vgsField as ExpirationDateEditText).setDateRegex("MM/yy");
            } else if (inputDateFormat == "shortYearThenMonth") {
              (instance.vgsField as ExpirationDateEditText).setDateRegex("yyyy/MM");
            } else if (inputDateFormat == "longYearThenMonth") {
              (instance.vgsField as ExpirationDateEditText).setDateRegex("yy/MM");
            }
          }

          initParams.getString("outputDateFormat")?.let { outputDateFormat ->
            if (outputDateFormat == "longYear") {
              (instance.vgsField as ExpirationDateEditText).setDateRegex("MM/yyyy");
            } else if (outputDateFormat == "shortYear") {
              (instance.vgsField as ExpirationDateEditText).setDateRegex("MM/yy");
            } else if (outputDateFormat == "shortYearThenMonth") {
              (instance.vgsField as ExpirationDateEditText).setDateRegex("yyyy/MM");
            } else if (outputDateFormat == "longYearThenMonth") {
              (instance.vgsField as ExpirationDateEditText).setDateRegex("yy/MM");
            }
          }
        }
        else if (fieldType == "cvc") {
          System.out.println("=====================================cvc");
          instance.setCvvField();
        }
        else if (fieldType == "cardNumber") {
          System.out.println("=====================================cardNumber");
          instance.setCardNumberField();
          initParams.getString("divider")?.let { divider ->
            (instance.vgsField as VGSCardNumberEditText).setDivider(divider.single());
          }
        }
        else {
          System.out.println("=====================================text");
          instance.setText()
        }
      }

      val field = instance.vgsField;

      val collector = CollectorManager.map[it];
      collector?.bindView(field);

      initParams.getString("fieldName")?.let { fieldName ->
        field.setFieldName(fieldName);
      }

      initParams.getString("keyboardType")?.let { keyboardType ->
        if (keyboardType === "numberPad") {
          if (isSecureTextEntry) {
            field.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD)
          } else {
            field.setInputType(InputType.TYPE_CLASS_NUMBER)
          }
        }
      }

      initParams.getArray("validations")?.let { rawValidationsArray ->
        try {
          val rule = VGSInfoRule.ValidationBuilder();

          for (i in rawValidationsArray.toArrayList().indices) {

          System.out.println("===================================== $i");
            rawValidationsArray.getMap(i)?.let { rawValidation ->
              val max = rawValidation.getInt("max");
              val min = rawValidation.getInt("min");
              val pattern = rawValidation.getString("pattern");


          System.out.println("===================================== $min $max $pattern");

              pattern?.let {patternStr ->
                rule.setRegex(patternStr);
              }

              if (min != null && max != null && max > min && max > 0) {
                  rule.setAllowableMinLength(min).setAllowableMaxLength(max);
              }
            }
          }
          rule.build()

          var tempName = instance.getType();
          System.out.println("===================================== $tempName");


          var method = field.javaClass.getMethod("addRule", VGSInfoRule.ValidationBuilder().javaClass);
          if (method != null) {
            method.invoke(field, rule);
          }
        } catch (e: Error) {
          System.out.println("VGSCollect failed to set validations, error=$e");
        }
      }

      field.setIsRequired(true);
      field.setSingleLine(true);

      // initParams.getString("formatPattern")?.let { formatPattern ->
      //   try {
      //     System.out.println("VGSCollect formatPattern->setMaxLength to ${formatPattern.length}");

      //     var method = field.javaClass.getMethod("setMaxLength");

      //     System.out.println("================================================== $method");
      //     if (method != null) {
      //       method.invoke(field, formatPattern.length);
      //     }
      //   } catch (e: Error) {
      //     System.out.println("VGSCollect failed to set formatPattern, error=$e");
      //   }
      // }
    }
  }

  @ReactProp(name = "fontSize")
  fun setFontSize(view: View, value: Float = 16.toFloat()) {
    (view as VgsCollectFieldInstance).vgsField.setTextSize(TypedValue.COMPLEX_UNIT_PX, value)
  }

  private var isSecureTextEntry: Boolean = false;

  @ReactProp(name = "isSecureTextEntry")
  fun setIsSecureTextEntry(view: View, value: Boolean) {

    System.out.println("=====================================secure $value");
    this.isSecureTextEntry = value;
    (view as VgsCollectFieldInstance).vgsField.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);
  }

  @ReactProp(name = "fontFamily")
  fun setFontFamily(view: View, value: String) {
    if (value != "") {
      ReactFontManager.getInstance().getTypeface(value, Typeface.NORMAL, this.reactContext.assets)?.let {
        (view as VgsCollectFieldInstance).vgsField.setTypeface(
          it
        )
      };
    }
  }

  @ReactProp(name = "placeholder")
  fun setPlaceholder(view: View, placeholder: String) {
    (view as VgsCollectFieldInstance).vgsField.setHint(placeholder);
  }

  @ReactProp(name = "textColor", customType = "Color")
  fun setTextColor(view: View, value: Int) {
    (view as VgsCollectFieldInstance).vgsField.setTextColor(value);
    view.vgsField.setHintTextColor(value);
  }
}

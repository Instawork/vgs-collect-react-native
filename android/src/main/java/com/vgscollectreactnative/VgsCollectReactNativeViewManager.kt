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

class VgsCollectReactNativeViewManager : SimpleViewManager<View>() {
  override fun getName() = "VgsCollectReactNativeView"
  private lateinit var reactContext: ThemedReactContext;

  override fun createViewInstance(reactContext: ThemedReactContext): View {
    this.reactContext = reactContext;

    return VgsCollectFieldInstance(reactContext)
  }

  @ReactProp(name = "fontSize")
  fun setFontSize(view: View, value: Float) {
    (view as VgsCollectFieldInstance).vgsField.setTextSize(TypedValue.COMPLEX_UNIT_PX, value)
  }

  @ReactProp(name = "isSecureTextEntry")
  fun setIsSecureTextEntry(view: View, value: Boolean) {
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

  @ReactProp(name = "config")
  fun config(view: View, initParams: ReadableMap) {
    val collectorName = initParams.getString("collectorName");
    val instance = (view as VgsCollectFieldInstance);

    collectorName?.let { it ->
      initParams.getString("fieldType")?.let { fieldType ->
        if (fieldType == "expDate") {
          instance.setExpField();
        } else if (fieldType == "cvc") {
          instance.setCvvField();
        } else if (fieldType == "cardNumber") {
          instance.setCardNumberField();
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
          field.setInputType(InputType.TYPE_CLASS_NUMBER)
        }
      }

      initParams.getString("keyboardType")?.let { keyboardType ->
        if (keyboardType === "numberPad") {
          field.setMaxLines(InputType.TYPE_CLASS_NUMBER)
        }
      }

      initParams.getString("formatPattern")?.let { formatPattern ->
        try {
          (field as VGSEditText).setMaxLength(formatPattern.length)
        } catch (e: Error) {

        }
      }

      initParams.getArray("validations")?.let { rawValidationsArray ->
        if (!field.isValidationEnabled()) {
          if (rawValidationsArray.size() > 0) {
            field.enableValidation(true);
          }

          for (i in 0..rawValidationsArray.size()) {
            rawValidationsArray.getMap(i)?.let { rawValidation ->
              System.out.println("VGSCollect rawValidation=$rawValidation");
              val max = rawValidation.getInt("max");
              val min = rawValidation.getInt("min");
              val pattern = rawValidation.getString("pattern");

              System.out.println("VGSCollect rawValidation, max=$max, min=$min, pattern=$pattern");

              if (pattern != null) {
                val rule = VGSInfoRule.ValidationBuilder().setRegex(pattern).build()

                try {
                  (field as VGSEditText).addRule(rule)
                } catch (e: Error) {}
              } else if (min != null && max != null) {
                val rule = VGSInfoRule.ValidationBuilder().setAllowableMinLength(min).setAllowableMaxLength(max).build()

                try {
                  (field as VGSEditText).addRule(rule)
                } catch (e: Error) {}
              }
            }
          }
        }
      }
    }
  }
}

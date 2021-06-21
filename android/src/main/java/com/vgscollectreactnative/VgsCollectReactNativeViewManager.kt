package com.vgscollectreactnative

import android.text.InputType
import android.view.View
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp
import com.verygoodsecurity.vgscollect.view.card.validation.rules.VGSInfoRule
import com.verygoodsecurity.vgscollect.widget.VGSEditText


import com.verygoodsecurity.vgscollect.widget.VGSCardNumberEditText
import com.verygoodsecurity.vgscollect.widget.ExpirationDateEditText


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
        when (fieldType) {
            "expDate" -> {
              instance.initExpField();
              initParams.getString("inputDateFormat")?.let { inputDateFormat ->
                when (inputDateFormat) {
                  "longYear" -> {
                    (instance.vgsField as ExpirationDateEditText).setDateRegex("MM/yyyy");
                  }
                  "shortYear" -> {
                    (instance.vgsField as ExpirationDateEditText).setDateRegex("MM/yy");
                  }
                  "shortYearThenMonth" -> {
                    (instance.vgsField as ExpirationDateEditText).setDateRegex("yyyy/MM");
                  }
                  "longYearThenMonth" -> {
                    (instance.vgsField as ExpirationDateEditText).setDateRegex("yy/MM");
                  }
                }
              }

              initParams.getString("outputDateFormat")?.let { outputDateFormat ->
                when (outputDateFormat) {
                  "longYear" -> {
                    (instance.vgsField as ExpirationDateEditText).setDateRegex("MM/yyyy");
                  }
                  "shortYear" -> {
                    (instance.vgsField as ExpirationDateEditText).setDateRegex("MM/yy");
                  }
                  "shortYearThenMonth" -> {
                    (instance.vgsField as ExpirationDateEditText).setDateRegex("yyyy/MM");
                  }
                  "longYearThenMonth" -> {
                    (instance.vgsField as ExpirationDateEditText).setDateRegex("yy/MM");
                  }
                }
              }
            }
            "cvc" -> {
              instance.initCvvField();
            }
            "cardNumber" -> {
              instance.initCardNumberField();
              initParams.getString("divider")?.let { divider ->
                (instance.vgsField as VGSCardNumberEditText).setDivider(divider.single());
              }
            }
            else -> {
              instance.initText()
            }
        }
      }

      if (instance.vgsField == null) {
        instance.initText();
      }

      instance.vgsField?.let { field ->
        val collector = CollectorManager.map[it];
        collector?.bindView(field);

        initParams.getString("fieldName")?.let { fieldName ->
          field.setFieldName(fieldName);
        }

        initParams.getString("keyboardType")?.let { keyboardType ->
          if (keyboardType === "numberPad") {
            field.setInputType(InputType.TYPE_CLASS_NUMBER)

            instance.isSecureTextEntry?.let {
              if (it) {
                field.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD)
              }
            }
          }
        }

        initParams.getArray("validations")?.let { rawValidationsArray ->
          try {
            val rule = VGSInfoRule.ValidationBuilder();

            for (i in rawValidationsArray.toArrayList().indices) {
              rawValidationsArray.getMap(i)?.let { rawValidation ->
                val max = rawValidation.getInt("max");
                val min = rawValidation.getInt("min");
                val pattern = rawValidation.getString("pattern");

                pattern?.let {patternStr ->
                  rule.setRegex(patternStr);
                }

                if (min != null && max != null && max > min && max > 0) {
                  rule.setAllowableMinLength(min).setAllowableMaxLength(max);
                }
              }
            }

            if (field is VGSEditText) {
              field.addRule(rule.build())
            }
          } catch (e: Error) {
            System.out.println("VGSCollect failed to set validations, error=$e");
          }
        }

        initParams.getString("formatPattern")?.let { formatPattern ->
          try {
            if (field is VGSEditText) {
              field.setMaxLength( formatPattern.length);
            }
          } catch (e: Error) {
            System.out.println("VGSCollect failed to set formatPattern, error=$e");
          }
        }
      }
    }
  }

  @ReactProp(name = "fontSize")
  fun setFontSize(view: View, value: Float = 16.toFloat()) {
    (view as VgsCollectFieldInstance).fontSize = value;
    view.setViewProps();
  }

  @ReactProp(name = "isSecureTextEntry")
  fun setIsSecureTextEntry(view: View, value: Boolean) {
    (view as VgsCollectFieldInstance).isSecureTextEntry = value;
    view.setViewProps();
  }

  @ReactProp(name = "fontFamily")
  fun setFontFamily(view: View, value: String) {
    (view as VgsCollectFieldInstance).fontFamily = value;
    view.setViewProps();
  }

  @ReactProp(name = "placeholder")
  fun setPlaceholder(view: View, placeholder: String) {
    (view as VgsCollectFieldInstance).placeholder = placeholder;
    view.setViewProps();
  }

  @ReactProp(name = "textColor", customType = "Color")
  fun setTextColor(view: View, value: Int) {
    (view as VgsCollectFieldInstance).textColor = value;
    view.setViewProps();
  }
}

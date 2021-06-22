package com.vgscollectreactnative

import android.graphics.Typeface
import android.text.InputType
import android.util.TypedValue
import android.widget.LinearLayout
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.views.text.ReactFontManager
import com.verygoodsecurity.vgscollect.view.InputFieldView
import com.verygoodsecurity.vgscollect.view.date.DatePickerMode
import com.verygoodsecurity.vgscollect.widget.CardVerificationCodeEditText
import com.verygoodsecurity.vgscollect.widget.ExpirationDateEditText
import com.verygoodsecurity.vgscollect.widget.VGSCardNumberEditText
import com.verygoodsecurity.vgscollect.widget.VGSEditText
import com.verygoodsecurity.vgscollect.widget.VGSTextInputLayout
 
import com.verygoodsecurity.vgscollect.view.card.validation.rules.VGSInfoRule

class VgsCollectFieldInstance(context: ThemedReactContext) : LinearLayout(context) {
  private var reactContext: ThemedReactContext = context
  var vgsField: InputFieldView? = null;

  var placeholder: String? = null;
  var fontFamily: String? = null;
  var isSecureTextEntry: Boolean? = null;
  var textColor: Int? = null;
  var fontSize: Float? = null;
  var keyboardType: String? = null;
  var fieldName: String? = null;

  init {
  }

  fun setViewProps() {
    vgsField?.let { field ->
      placeholder?.let {
        field.setHint(it);
      }

      fontFamily?.let {
        if (it != "") {
          ReactFontManager.getInstance().getTypeface(it, Typeface.NORMAL, this.reactContext.assets)?.let {fontInstance ->
            field.setTypeface(fontInstance)
          };
        }
      }

      textColor?.let {
        field.setTextColor(it);
      }

      fieldName?.let {
        field.setFieldName(it);
      }

      isSecureTextEntry?.let {
        if (it) {
          field.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        }
      }

      keyboardType?.let {
        if (it === "numberPad") {
          isSecureTextEntry?.let {
            if (it) {
              field.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            } else {
              field.setInputType(InputType.TYPE_CLASS_NUMBER);
            }
          }
        }
      }

      fontSize?.let {
        field.setTextSize(TypedValue.COMPLEX_UNIT_PX, it);
      }

      field.setIsRequired(true);
      field.setSingleLine(true);
    }
  }

  private fun addInputToLayout() {
    vgsField?.let {
      it.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
      setViewProps();
      this.addView(it)
    }
  }

  fun initExpField() {
    vgsField = ExpirationDateEditText(context)
    (vgsField as ExpirationDateEditText).setDatePickerMode(DatePickerMode.SPINNER)
    this.addInputToLayout()
  }

  fun initCvvField() {
    vgsField = CardVerificationCodeEditText(context)
    this.addInputToLayout()
  }

  fun initCardNumberField() {
    vgsField = VGSCardNumberEditText(context)
    this.addInputToLayout()
  }

  fun initPinField() {
    vgsField = VGSEditText(context)
    val rule : VGSInfoRule = VGSInfoRule.ValidationBuilder()
    .setRegex("^[a-zA-Z0-9 ,'.-]+$")
    .setAllowableMinLength(4)
    .setAllowableMaxLength(6)
    .build()

    (vgsField as VGSEditText).addRule(rule)
    this.addInputToLayout()
  }

  fun initText() {
    vgsField = VGSEditText(context)
    this.addInputToLayout()
  }
}

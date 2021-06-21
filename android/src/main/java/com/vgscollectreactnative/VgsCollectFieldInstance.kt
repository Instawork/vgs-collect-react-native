package com.vgscollectreactnative

import android.widget.LinearLayout
import com.facebook.react.uimanager.ThemedReactContext
import com.verygoodsecurity.vgscollect.view.InputFieldView
import com.verygoodsecurity.vgscollect.view.date.DatePickerMode
import com.verygoodsecurity.vgscollect.widget.CardVerificationCodeEditText
import com.verygoodsecurity.vgscollect.widget.ExpirationDateEditText
import com.verygoodsecurity.vgscollect.widget.VGSCardNumberEditText
import com.verygoodsecurity.vgscollect.widget.VGSEditText
import com.verygoodsecurity.vgscollect.widget.VGSTextInputLayout

class VgsCollectFieldInstance(context: ThemedReactContext) : LinearLayout(context) {
  private var INSTANCE_TYPE: String = "text"
  private var reactContext: ThemedReactContext = context
  var vgsField: InputFieldView = VGSEditText(context)
  private lateinit var vgsTextInputLayout: VGSTextInputLayout

  init {
    this.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    this.createVGSTextInputLayout(reactContext)
    this.addView(vgsTextInputLayout)
  }

  private fun createVGSTextInputLayout(reactContext: ThemedReactContext) {
    vgsTextInputLayout = VGSTextInputLayout(reactContext)
    vgsTextInputLayout.layoutParams =
        LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
  }

  private fun addInputToLayout() {
    vgsField.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    vgsTextInputLayout.addView(vgsField)
  }

  fun setExpField() {
    vgsField = ExpirationDateEditText(context)
    (vgsField as ExpirationDateEditText).setDatePickerMode(DatePickerMode.SPINNER)
    vgsTextInputLayout.setHint("Exp Date")
    INSTANCE_TYPE = "expDate"
    this.addInputToLayout()
  }

  fun setCvvField() {
    vgsField = CardVerificationCodeEditText(context)
    INSTANCE_TYPE = "cvc"
    this.addInputToLayout()
  }

  fun setCardNumberField() {
    vgsField = VGSCardNumberEditText(context)
    INSTANCE_TYPE = "cardNumber"
    this.addInputToLayout()
  }

  fun setText() {
    this.addInputToLayout()
  }

  fun getType(): String {
    return INSTANCE_TYPE
  }
}

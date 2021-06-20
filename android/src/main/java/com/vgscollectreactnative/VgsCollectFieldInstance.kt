package com.vgscollectreactnative

import android.util.TypedValue
import android.widget.LinearLayout
import com.facebook.react.uimanager.ThemedReactContext
import com.verygoodsecurity.vgscollect.view.InputFieldView
import com.verygoodsecurity.vgscollect.widget.CardVerificationCodeEditText
import com.verygoodsecurity.vgscollect.widget.ExpirationDateEditText
import com.verygoodsecurity.vgscollect.widget.VGSCardNumberEditText
import com.verygoodsecurity.vgscollect.widget.VGSEditText
import com.verygoodsecurity.vgscollect.widget.VGSTextInputLayout;

import com.verygoodsecurity.vgscollect.view.date.DatePickerMode;

class VgsCollectFieldInstance(context: ThemedReactContext) : LinearLayout(context) {
  private var reactContext: ThemedReactContext = context;
  var vgsField: InputFieldView = VGSEditText(context);
  private lateinit var vgsTextInputLayout: VGSTextInputLayout;

  init {
    this.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    this.createVGSTextInputLayout(reactContext);
    this.addView(vgsTextInputLayout);
  }

  private fun createVGSTextInputLayout(reactContext: ThemedReactContext) {
    vgsTextInputLayout = VGSTextInputLayout(reactContext);
    vgsTextInputLayout.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
  }

  private fun addInputToLayout() {
    vgsField.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    vgsTextInputLayout.addView(vgsField);
  }

  fun setExpField() {
    System.out.println("========================================1got exp input");
    vgsField = ExpirationDateEditText(context);
    (vgsField as ExpirationDateEditText).setDatePickerMode(DatePickerMode.SPINNER);
    vgsTextInputLayout.setHint("Exp Date");
    this.addInputToLayout();
  }

  fun setCvvField() {
    System.out.println("========================================1got cvc input");
    vgsField = CardVerificationCodeEditText(context);
  }

  fun setCardNumberField() {
    System.out.println("========================================1got card# input");
    vgsField = VGSCardNumberEditText(context);
  }

}

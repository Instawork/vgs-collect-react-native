package com.vgscollectreactnative

import android.widget.LinearLayout
import com.facebook.react.bridge.ReactContext
import com.verygoodsecurity.vgscollect.view.InputFieldView
import com.verygoodsecurity.vgscollect.widget.CardVerificationCodeEditText
import com.verygoodsecurity.vgscollect.widget.ExpirationDateEditText
import com.verygoodsecurity.vgscollect.widget.VGSCardNumberEditText
import com.verygoodsecurity.vgscollect.widget.VGSEditText

class VgsCollectFieldInstance(context: ReactContext) : LinearLayout(context) {
  private var reactContext: ReactContext = context;
  var vgsField: InputFieldView = VGSEditText(context);

  init {
    this.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    vgsField.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    this.addView(vgsField);
  }

  fun setExpField() {
    vgsField = ExpirationDateEditText(context);
  }

  fun setCvvField() {
    vgsField = CardVerificationCodeEditText(context);
  }

  fun setCardNumberField() {
    vgsField = VGSCardNumberEditText(context);
  }
}

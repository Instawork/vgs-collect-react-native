package com.vgscollectreactnative

import android.app.Activity
import com.facebook.react.bridge.*
import com.verygoodsecurity.vgscollect.VGSCollectLogger
import com.verygoodsecurity.vgscollect.core.Environment
import com.verygoodsecurity.vgscollect.core.HTTPMethod
import com.verygoodsecurity.vgscollect.core.VGSCollect
import com.verygoodsecurity.vgscollect.core.VgsCollectResponseListener
import com.verygoodsecurity.vgscollect.core.model.network.VGSRequest
import com.verygoodsecurity.vgscollect.core.model.network.VGSResponse
import com.verygoodsecurity.vgscollect.widget.VGSEditText


class CollectorManager internal constructor(context: ReactApplicationContext?) : ReactContextBaseJavaModule(context) {

  companion object {
    val map: MutableMap<String, VGSCollect> = mutableMapOf<String, VGSCollect>();
  }

  @ReactMethod
  fun createNamedCollector(name: String, vaultId: String, environment: String) {
    System.out.println("VGSCollect creating with: name=$name, vaultId=$vaultId, env=$environment");

    reactApplicationContext.currentActivity?.let {
      map[name] = VGSCollect(it, vaultId, if (environment == "sandbox") Environment.SANDBOX else Environment.LIVE);
      System.out.println("VGSCollect created ACTIVITY with: name=$name, vaultId=$vaultId, env=$environment");
    }
  }

  @ReactMethod
  fun pinConfirm(name: String, callback: Callback) {
    VGSCollectLogger.logLevel = VGSCollectLogger.Level.WARN;
    System.out.println("VGSCollect wrapper triggered confirmPin, name=$name");

    val activity: Activity? = currentActivity
    var isEqual = true
    if (activity != null) {
      val pin = activity.findViewById<VGSEditText>(R.id.vgsPin)
      if (pin != null){
        val pinConfirm = activity.findViewById<VGSEditText>(R.id.vgsPinConfirm)
        isEqual = pin.isContentEquals(pinConfirm)
      }
    }
    val map = Arguments.createMap();
    map.putInt("code", 200);
    map.putBoolean("data", isEqual);
    callback.invoke(map);
  }

  @ReactMethod
  fun submit(name: String, path: String, method: String, headers: ReadableMap, callback: Callback) {
    VGSCollectLogger.logLevel = VGSCollectLogger.Level.WARN;
    System.out.println("VGSCollect wrapper triggered submit, name=$name, path=$path, method=$method");

    map[name]?.let {
      val requestBuilder = VGSRequest.VGSRequestBuilder();
      val methodObj = if (method == "GET") HTTPMethod.GET else HTTPMethod.POST;
      requestBuilder.setMethod(methodObj);
      requestBuilder.setPath(path);

      headers.let { headersRaw ->
        val iterator = headersRaw.keySetIterator();
        val map = mutableMapOf<String, String>();

        while (iterator.hasNextKey()) {
          val key = iterator.nextKey();
          val value = headersRaw.getString(key);

          if (value != null) {
            map[key] = value
          };
        }

        requestBuilder.setCustomHeader(map)
      }

      it.clearResponseListeners();
      it.addOnResponseListeners(object : VgsCollectResponseListener {
        override fun onResponse(response: VGSResponse?) {
          System.out.println("VGSCollect response=$response");

          when(response) {
            is VGSResponse.SuccessResponse -> {
              System.out.println("VGSCollect success response=$response");
              val map = Arguments.createMap();
              map.putInt("code", response.successCode);
              map.putString("data", response.body);
              callback.invoke(map);
            }
            is VGSResponse.ErrorResponse -> {
              System.out.println("VGSCollect error response=$response");
              val map = Arguments.createMap();
              map.putInt("code", response.errorCode);
              map.putString("error", response.localizeMessage);
              callback.invoke(map);
            }
            else -> {}
          }
        }
      });

      it.asyncSubmit(requestBuilder.build());
    }
  }


  override fun getName(): String {
    return "CollectorManager";
  }
}

package com.vgscollectreactnative

import com.facebook.react.bridge.*
import com.verygoodsecurity.vgscollect.core.HTTPMethod
import com.verygoodsecurity.vgscollect.core.VGSCollect
import com.verygoodsecurity.vgscollect.core.VgsCollectResponseListener
import com.verygoodsecurity.vgscollect.core.model.network.VGSResponse


class CollectorManager internal constructor(context: ReactApplicationContext?) : ReactContextBaseJavaModule(context) {

  companion object {
    val map: MutableMap<String, VGSCollect> = mutableMapOf<String, VGSCollect>();
  }

  @ReactMethod
  fun createNamedCollector(name: String, vaultId: String, environment: String) {
    reactApplicationContext.currentActivity?.let {
      map[name] = VGSCollect(it, vaultId, environment);
    }
  }

  @ReactMethod
  fun submit(name: String, path: String, method: String, headers: ReadableMap, callback: Callback) {
    System.out.println("VGSCollect wrapper triggered submit, name=$name, path=$path, method=$method, headers=$headers");

    map[name]?.let {
      val methodObj = if (method == "GET") HTTPMethod.GET else HTTPMethod.POST;

      when(val response = it.submit(path, methodObj)) {
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
      }
    }
  }


  override fun getName(): String {
    return "CollectorManager";
  }
}

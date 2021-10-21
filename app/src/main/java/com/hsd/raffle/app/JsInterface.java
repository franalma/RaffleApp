package com.hsd.raffle.app;

import android.webkit.JavascriptInterface;


interface JsInterface {
   @JavascriptInterface
    void onEvent(String data);
}

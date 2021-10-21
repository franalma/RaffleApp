package com.hsd.raffle.app;

import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;

public class Utils {

    public static WebView setupWebview(Activity context, int id, JsInterface jsInterface){
        WebView.setWebContentsDebuggingEnabled(true);
        WebView webView = context.findViewById(id);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setVerticalScrollBarEnabled(false);
        // disable scroll on touch
        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });


        webView.addJavascriptInterface(jsInterface, "delegate");

        return webView;
    }

    public static WebView loadWebPage(WebView webView, String webPath){
        webView.loadUrl("file:///android_asset/"+webPath);
        return webView;
    }

}

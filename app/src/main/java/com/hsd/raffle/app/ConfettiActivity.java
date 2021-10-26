package com.hsd.raffle.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class ConfettiActivity extends AppCompatActivity {

    WebView webView;
    String data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confetti);
        this.loadWebview();

    }

    private void loadWebview() {
        webView = Utils.setupWebview(this, R.id.id_webview_result, new JsInterface() {
            @Override
            public void onEvent(String data) {

            }
        });
        this.data = getIntent().getStringExtra("data");
        try{
            Prize prize = PrizeController.getInstance(null).getPrizeForPosition(getIntent().getIntExtra("gift_info",-1));
            webView.loadUrl("file:///android_asset/web/result.html?data="+data+"&prize="+prize.getImg()+"&x="+prize.getPosX()+"&y="+prize.getPosY());
        }catch(Exception e){
            e.printStackTrace();
        }

    }




}
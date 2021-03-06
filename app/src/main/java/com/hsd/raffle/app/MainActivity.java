package com.hsd.raffle.app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.ml.scan.HmsScan;
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_SCAN_ONE = 10000;
    private final static int CAMERA_REQ_CODE = 2000;
    private String readValues[];
    private ArrayList<Integer> selectedNumbers = new ArrayList();
    private final static int NUMBER_OF_GIFS = 3;
    private WebView webView;
    private static  int currentGift = NUMBER_OF_GIFS-1;
    private JSONArray jarray = new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.askForPermissions();
        loadWebview();
    }

    private void askForPermissions() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_REQ_CODE);
        }
    }

    public void onScanCode(View view) {
        currentGift = NUMBER_OF_GIFS-1;
        this.scanCodeInternal();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (data != null && requestCode == REQUEST_CODE_SCAN_ONE) {
                HmsScan obj = data.getParcelableExtra(ScanUtil.RESULT);
                this.processData(obj.getOriginalValue());
                this.updateDataToWebView();
            } else {
                Toast.makeText(this, "Not possible to read QR", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    private void processData(String data) {
        String [] readValues = data.split(",");
        try{
            jarray = new JSONArray();
            for (int i = 0; i < readValues.length; i++){
                JSONObject joc = new JSONObject();
                joc.put("label", readValues[i]);
                joc.put("value", readValues[i]);
                jarray.put(joc);
            }

        }catch(Exception e){
            e.printStackTrace();
        }


//       readValues = data.split(",");
//        int cont = 1;
//        for (int i = 0; i<readValues.length; i++){
//            readValues[i] = cont++ +"."+readValues[i];
//        }
//
//        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, readValues);
//        ListView listView = findViewById(R.id.id_listview);
//        listView.setAdapter(adapter);
//
//        for (int i = 0; i<NUMBER_OF_GIFS; i++){
//            getRandomValue();
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void scanCodeInternal() {
        HmsScanAnalyzerOptions options = new HmsScanAnalyzerOptions.Creator()
                .setHmsScanTypes(HmsScan.QRCODE_SCAN_TYPE).create();
        ScanUtil.startScan(this, REQUEST_CODE_SCAN_ONE, options);
    }

    public void getRandomValue() {
        int result;
        do {
            Random r = new Random();
            int low = 0;
            int high = readValues.length;
            result = r.nextInt(high - low) + low;
        } while (selectedNumbers.contains(result));
        selectedNumbers.add(result);
        System.out.println("--result: " + result);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.show_prices:
                Intent intent = new Intent(this, PrizeResult.class);
                intent.putExtra("winners", this.selectedNumbers);
                intent.putExtra("participants", this.readValues);
                startActivity(intent);
                return true;
        }
        return false;
    }
    final JsInterface jsInterface =   new com.hsd.raffle.app.JsInterface() {
        @JavascriptInterface
        @Override
        public void onEvent(String data) {
            Intent intent = new Intent(MainActivity.this,ConfettiActivity.class);
            intent.putExtra("data", data);
            intent.putExtra("gift_info", currentGift--);
            removeItem(data);
            startActivity(intent);
        }
    };

    private void loadWebview() {
        webView = Utils.setupWebview(this, R.id.id_webview,jsInterface);
        WebView webView = findViewById(R.id.id_webview);
        webView.loadUrl("file:///android_asset/web/index.html");

    }

    private void updateDataToWebView(){
        try{
            WebView webView = findViewById(R.id.id_webview);
            webView.loadUrl("javascript:addData("+jarray.toString()+")");
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    private void removeItem(String data){
        for (int i = 0; i<jarray.length(); i++){
            try{
                JSONObject item = jarray.getJSONObject(i);
                if (item.getString("value").equals(data)){
                    jarray.remove(i);
                    break;
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl("javascript:addData("+jarray.toString()+")");
            }
        });

    }
}


package com.hsd.raffle.app;

import android.content.Context;
import android.os.Environment;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class PrizeController {
    private final static String FILE_NAME = "prizes_hsd.json";
    private static PrizeController instance;

    private ArrayList<Prize> prizes = new ArrayList<>();
    private String eventName = "";

    private PrizeController(Context context) {

        try {
//            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
//            File file = new File(dir.getAbsolutePath() + File.separator + FILE_NAME);
//            BufferedReader br = new BufferedReader(new FileReader(file));
            InputStream inputStream = context.getAssets().open("prizes_default.json");
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            String content = "";

            while ((line = br.readLine()) != null) {
                content += line;
            }
            JSONObject values = new JSONObject(content);
            fillInternalData(values);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fillInternalData(JSONObject values) {
        try {
            this.eventName = values.getString("contest_name");
            JSONArray jarray = values.getJSONArray("prizes");
            for (int i = 0; i< jarray.length(); i++){
                JSONObject joc = jarray.getJSONObject(i);
                this.prizes.add(new Prize(joc.getString("name"), joc.getString("img")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Prize getPrizeForPosition(int value){
        return this.prizes.get(value);
    }

    public static PrizeController getInstance(Context context) {
        if (instance == null) {
            instance = new PrizeController(context);
        }
        return instance;
    }

}

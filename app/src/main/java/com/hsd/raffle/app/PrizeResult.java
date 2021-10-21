package com.hsd.raffle.app;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class PrizeResult extends AppCompatActivity {
    ArrayList<Integer> winners;
    String[] participants;
    int currentWinner = 0;

    int[] imgGifts = new int[]{R.drawable.img_huawei_band, R.drawable.img_huawei_band, R.drawable.img_huawei_band, R.drawable.img_watch_gt_pro2};


    private void getParams() {
        try {
            winners = getIntent().getExtras().getIntegerArrayList("winners");
            participants = getIntent().getExtras().getStringArray("participants");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prize_result);
        getParams();
    }

    private void showNextWinner() {
        if (currentWinner < winners.size()) {
            TextView textView = findViewById(R.id.id_winner_name);
            ImageView imageView = findViewById(R.id.id_prize_img);
            imageView.setImageResource(imgGifts[currentWinner]);
            String name = participants[winners.get(currentWinner++)];
            textView.setText(name);
        } else {
            Toast.makeText(this, "No more winners", Toast.LENGTH_LONG).show();
        }
    }

    public void onClear(View view) {
        TextView textView = findViewById(R.id.id_winner_name);
        ImageView imageView = findViewById(R.id.id_prize_img);
        imageView.setImageResource(R.drawable.img_unknown_gift);
        textView.setText("********************");
    }

    public void onShowNextWinner(View view) {
        showNextWinner();
    }
}
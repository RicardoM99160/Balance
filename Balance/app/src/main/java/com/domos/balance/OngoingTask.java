package com.domos.balance;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class OngoingTask extends AppCompatActivity {

    long mMilliseconds = 60000;
    SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("HH:mm:ss");
    TextView txtTimer;

    CountDownTimer mCountDownTimer = new CountDownTimer(mMilliseconds, 1000) {
        @Override
        public void onFinish() {
            txtTimer.setText(mSimpleDateFormat.format(0));
        }

        public void onTick(long millisUntilFinished) {
            txtTimer.setText(mSimpleDateFormat.format(millisUntilFinished));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ongoing_task);

        mSimpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        txtTimer = (TextView) findViewById(R.id.otTxtTimer);

        mCountDownTimer.start();
    }
}
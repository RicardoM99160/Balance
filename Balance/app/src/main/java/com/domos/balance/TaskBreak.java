package com.domos.balance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.domos.balance.data.Task;

import java.text.SimpleDateFormat;

public class TaskBreak extends AppCompatActivity {

    TextView txtTimer, txtTitleSuggestion;
    ImageView imgEmoji;
    ImageButton btnReturn, btnSettings;

    CountDownTimer mCountDownBreak;
    SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("mm:ss");

    long breakDuration;
    int currentPomodoro;
    Task ongoingTask;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_break);

        ongoingTask = new Task();

        Bundle extras = getIntent().getExtras();

        ongoingTask = (Task) getIntent().getSerializableExtra("ongoingTask");
        breakDuration = Long.parseLong(extras.getString("break"));
        currentPomodoro = Integer.parseInt(extras.getString("currentPomodoro"));
        initialize();
        setupTimer();

        mCountDownBreak.start();
    }

    public void  initialize(){
        txtTimer = (TextView) findViewById(R.id.tbTxtTimer);
        txtTitleSuggestion = (TextView) findViewById(R.id.tbTxtTitleSuggestion);
        imgEmoji = (ImageView) findViewById(R.id.tbImgEmoji);
        btnReturn = (ImageButton) findViewById(R.id.tbBtnReturn);
        btnSettings = (ImageButton) findViewById(R.id.tbBtnSettings);

        imgEmoji.setImageResource(ongoingTask.getEmoji());

    }

    protected void setupTimer(){
        mCountDownBreak = new CountDownTimer(breakDuration, 1000) {
            @Override
            public void onFinish() {
                mCountDownBreak.cancel();
                currentPomodoro++;
                Intent intent = new Intent(TaskBreak.this, OngoingTask.class);
                intent.putExtra("newOngoingTask", ongoingTask);
                intent.putExtra("currentPomodoro", ""+currentPomodoro);
                startActivity(intent);
            }

            public void onTick(long millisUntilFinished) {
                txtTimer.setText(mSimpleDateFormat.format(millisUntilFinished));
            }
        };
    }
}
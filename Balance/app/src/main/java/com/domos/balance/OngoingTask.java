package com.domos.balance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ImageView;
import android.widget.TextView;

import com.domos.balance.data.Task;

import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class OngoingTask extends AppCompatActivity {

    long mMilliseconds = 60000;

    long pomodoroDuration;
    long pomodoroRest;

    int numberOfPomodoros;
    int currentPomodoro;

    CountDownTimer mCountDownTimer;

    SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("mm:ss");

    Task ongoingTask ;

    ImageView ivEmoji;
    TextView txtTaskname, txtDuracion, txtCount,  txtTimer, txtCurrentPomodoro;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ongoing_task);

        //Recuperando task
        Intent intent = getIntent();

        ongoingTask = new Task();
        ongoingTask = (Task) intent.getSerializableExtra("newOngoingTask");


        mSimpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        inicialize();
        setPomodoroTimer(ongoingTask.getDuration());
        numberOfPomodoros = ongoingTask.getCount();
        currentPomodoro = 1;

        setupTimer();
        mCountDownTimer.start();
    }

    protected void setupTimer(){
       mCountDownTimer = new CountDownTimer(pomodoroDuration, 1000) {
            @Override
            public void onFinish() {
                //AqUI SE MUEVE A LA OTRA VISTA DE DESCANSO
                // DEBE COMPROBAR QUE NO SE HAYA TERMINADO TO-DO :(
                //txtTimer.setText(mSimpleDateFormat.format(0));
                if(currentPomodoro < numberOfPomodoros){
                    currentPomodoro++;
                    txtCurrentPomodoro.setText("POMODORO "+currentPomodoro);
                    this.start();
                }



            }

            public void onTick(long millisUntilFinished) {
                txtTimer.setText(mSimpleDateFormat.format(millisUntilFinished));
            }
        };
    }



    protected void inicialize(){
        txtTimer = (TextView) findViewById(R.id.otTxtTimer);

        ivEmoji = (ImageView) findViewById(R.id.otBtnEmoji);
        txtTaskname = (TextView) findViewById(R.id.otTxtTaskname);
        txtDuracion = (TextView) findViewById(R.id.otTxtDuration);
        txtCount = (TextView) findViewById(R.id.otTxtCount);
        txtCurrentPomodoro = (TextView) findViewById(R.id.otTxtPomodoroTitle);

        ivEmoji.setImageResource(ongoingTask.getEmoji());
        txtTaskname.setText(ongoingTask.getName());
        txtDuracion.setText(ongoingTask.getDuration());
        txtCount.setText(ongoingTask.getCount()+ " Pomodoros");

    }


    private void setPomodoroTimer(String selectedInterval){


        switch (selectedInterval){
            case "25 min - 5 min":
                pomodoroDuration = TimeUnit.MINUTES.toMillis(25);
                pomodoroRest =TimeUnit.MINUTES.toMillis(5);

                break;
            case "45 min - 15 min":
                pomodoroDuration = TimeUnit.MINUTES.toMillis(45);
                pomodoroRest =TimeUnit.MINUTES.toMillis(15);
                break;

            case "50 min - 10 min":
                pomodoroDuration = TimeUnit.MINUTES.toMillis(50);
                pomodoroRest =TimeUnit.MINUTES.toMillis(10);
                break;

            case "30 sec - 5 sec":
                pomodoroDuration = TimeUnit.SECONDS.toMillis(30);
                pomodoroRest = TimeUnit.SECONDS.toMillis(5);
                break;
        }


    }
}
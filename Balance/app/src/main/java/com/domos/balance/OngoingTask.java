package com.domos.balance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
    Button btnFinish, btnInterrupt;
    ImageButton btnReturn;



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

        ivEmoji = (ImageView) findViewById(R.id.otImgEmoji);
        txtTaskname = (TextView) findViewById(R.id.otTxtTaskname);
        txtDuracion = (TextView) findViewById(R.id.otTxtDuration);
        txtCount = (TextView) findViewById(R.id.otTxtCount);
        txtCurrentPomodoro = (TextView) findViewById(R.id.otTxtPomodoroTitle);

        ivEmoji.setImageResource(ongoingTask.getEmoji());
        txtTaskname.setText(ongoingTask.getName());
        txtDuracion.setText(ongoingTask.getDuration());
        txtCount.setText(ongoingTask.getCount()+ " Pomodoros");

        btnFinish = (Button) findViewById(R.id.otBtnFinish);
        btnInterrupt = (Button) findViewById(R.id.otBtnInterrupt);
        btnReturn = (ImageButton) findViewById(R.id.otBtnReturn);

        buttonSetUp();

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

            case "10 sec - 5 sec":
                pomodoroDuration = TimeUnit.SECONDS.toMillis(10);
                pomodoroRest = TimeUnit.SECONDS.toMillis(5);
                break;
        }

    }

    public void buttonSetUp(){

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Obteniendo los strings que seran asignados a los elementos del Dialog
                int title = R.string.cdOngoingTaskFinishTitle;
                int subtitle = R.string.cdOngoingTaskFinishSubtitle;
                int confirm = R.string.cdOngoingTaskConfirmar;
                int cancel = R.string.cdOngoingTaskCancelar;
                showDialog(title, subtitle, confirm, cancel);
            }
        });

        btnInterrupt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Obteniendo los strings que seran asignados a los elementos del Dialog
                int title = R.string.cdOngoingTaskReturnTitle;
                int subtitle = R.string.cdOngoingTaskReturnSubtitle;
                int confirm = R.string.cdOngoingTaskConfirmar;
                int cancel = R.string.cdOngoingTaskCancelar;
                showDialog(title, subtitle, confirm, cancel);
            }
        });

    }

    public void showDialog(int title, int subtitle, int confirm, int cancel){

        //Instanciando dialogo de yes/no
        CustomYesNoDialog confirmation = new CustomYesNoDialog(OngoingTask.this);

        confirmation.show();

        confirmation.setContentView(R.layout.custom_yes_no_dialog);

        TextView txtTitle = (TextView) confirmation.findViewById(R.id.cdTxtTitle);
        TextView txtSubtitle = (TextView) confirmation.findViewById(R.id.cdTxtSubtitle);
        Button yes_btn = (Button) confirmation.findViewById(R.id.cdBtnYes);
        Button no_btn = (Button) confirmation.findViewById(R.id.cdBtnNo);

        txtTitle.setText(title);
        txtSubtitle.setText(subtitle);
        yes_btn.setText(confirm);
        no_btn.setText(cancel);


        confirmation.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //Agregando onClickListener a los botones del diálogo
        yes_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v2) {

                //Detengo primero el counter
                mCountDownTimer.cancel();

                //Redirijo al MainActivity
                Intent intent = new Intent(OngoingTask.this, MainActivity.class);
                startActivity(intent);
                confirmation.dismiss();
            }
        });

        no_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                confirmation.dismiss();
            }
        });
    }
}
package com.domos.balance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.domos.balance.data.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class OngoingTask extends AppCompatActivity {

    long mMilliseconds = 60000;
    long pomodoroDuration;
    long pomodoroRest;
    CountDownTimer mCountDownTimer, breakTimer;
    SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("mm:ss");
    Task ongoingTask ;
    ImageView ivEmoji, ivBreakEmoji;
    TextView txtTaskname, txtDuracion, txtCount,  txtTimer, txtCurrentPomodoro, txtBreakTimer, txtBreakTitleSuggestion;
    Button btnFinish, btnInterrupt;
    ImageButton btnReturn, btnEndBreak;
    ConstraintLayout constraintLayoutBreak, constraintLayoutOngoingTask;
    MediaPlayer mediaPlayer;

    boolean taskIsFinished;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ongoing_task);

        taskIsFinished = false;

        //Recuperando tarea en proceso
        Intent intent = getIntent();
        ongoingTask = new Task();
        ongoingTask = (Task) intent.getSerializableExtra("newOngoingTask");

        //Inicializando la interfaz
        inicialize();
        updateUI("Work");

        //Inicializando el Timer
        setPomodoroTimer(ongoingTask.getDuration());
        setupTimer();
        setupBreak();

        //Se inicia el timer si todavía hay pomodoros pendientes
        //Sino se termina
        /*if (ongoingTask.getCurrentPomodoro() <= ongoingTask.getCount()){
            mCountDownTimer.start();
        }else {
            mCountDownTimer.onFinish();
        }*/
        mediaPlayer = MediaPlayer.create(OngoingTask.this, R.raw.bell);
        mediaPlayer.start();
        mCountDownTimer.start();
    }

    protected void setupTimer(){
       mCountDownTimer = new CountDownTimer(pomodoroDuration, 1000) {
            @Override
            public void onFinish() {

                //Cancelo el timer
                this.cancel();

                //AqUI SE MUEVE A LA OTRA VISTA DE DESCANSO
                // DEBE COMPROBAR QUE NO SE HAYA TERMINADO TO-DO :(
                if(ongoingTask.getCurrentPomodoro() <= ongoingTask.getCount()){

                    //Se incrementa el pomodoro actual
                    ongoingTask.setCurrentPomodoro(ongoingTask.getCurrentPomodoro()+1);

                    //Actualizo la interfaz
                    updateUI("Break");

                    //Inicio el timer del break
                    //mediaPlayer = MediaPlayer.create(OngoingTask.this, R.raw.bell);
                    mediaPlayer.start();
                    breakTimer.start();

                }else {

                    //TODO: Si se llega a este punto significa que la tarea fue terminada con exito

                    taskIsFinished = true;

                    //Significa que la tarea fue terminada con exito
                    ongoingTask.setSuccessful(true);

                    //Almaceno los resultados en la base de datos
                    saveData();

                    //Le muestro el mensaje de exito al usuario
                    txtCurrentPomodoro.setText("Tarea finalizada");
                    txtTimer.setTextSize(55);
                    txtTimer.setText("Felicidades");
                    btnFinish.setText(R.string.otBtnSalir);
                }

            }

            public void onTick(long millisUntilFinished) {
                txtTimer.setText(mSimpleDateFormat.format(millisUntilFinished));
            }
        };
    }

    protected void setupBreak(){
        breakTimer = new CountDownTimer(pomodoroRest, 1000) {
            @Override
            public void onFinish() {
                this.cancel();
                updateUI("Work");
                if(ongoingTask.getCurrentPomodoro() <= ongoingTask.getCount()){
                    //mediaPlayer = MediaPlayer.create(OngoingTask.this, R.raw.bell);
                    mediaPlayer.start();
                    mCountDownTimer.start();
                }else {
                    mCountDownTimer.onFinish();
                }

            }

            public void onTick(long millisUntilFinished) {
                txtBreakTimer.setText(mSimpleDateFormat.format(millisUntilFinished));
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
        constraintLayoutBreak = (ConstraintLayout) findViewById(R.id.otConstraintLayoutBreak);
        constraintLayoutOngoingTask = (ConstraintLayout) findViewById(R.id.otConstraintLayoutOngoingTask);

        txtBreakTimer =  (TextView) findViewById(R.id.tbTxtTimer);
        txtBreakTitleSuggestion = (TextView) findViewById(R.id.tbTxtTitleSuggestion);
        ivBreakEmoji = (ImageView) findViewById(R.id.tbImgEmoji);
        ivBreakEmoji.setImageResource(ongoingTask.getEmoji());

        ivEmoji.setImageResource(ongoingTask.getEmoji());
        txtTaskname.setText(ongoingTask.getName());
        txtDuracion.setText(ongoingTask.getDuration());
        String cantidadPomodoros = ongoingTask.getCount() == 1 ? " pomodoro" : " pomodoros";
        txtCount.setText(ongoingTask.getCount()+ cantidadPomodoros);
        txtCurrentPomodoro.setText("POMODORO "+ongoingTask.getCurrentPomodoro());

        btnFinish = (Button) findViewById(R.id.otBtnFinish);
        btnInterrupt = (Button) findViewById(R.id.otBtnInterrupt);
        btnReturn = (ImageButton) findViewById(R.id.otBtnReturn);
        btnEndBreak = (ImageButton) findViewById(R.id.tbBtnEndBreak);

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

                if(ongoingTask.isSuccessful()){
                    Intent intent = new Intent(OngoingTask.this, MainActivity.class);
                    startActivity(intent);
                }else{
                    //Obteniendo los strings que seran asignados a los elementos del Dialog
                    int title = R.string.cdOngoingTaskFinishTitle;
                    int subtitle = R.string.cdOngoingTaskFinishSubtitle;
                    int confirm = R.string.cdOngoingTaskConfirmar;
                    int cancel = R.string.cdOngoingTaskCancelar;
                    showDialog(title, subtitle, confirm, cancel);
                }
            }
        });

        btnInterrupt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(taskIsFinished){
                    Toast.makeText(OngoingTask.this, "No puede agregar nuevas interrupciones", Toast.LENGTH_SHORT).show();
                }else{
                    showInterruptionDialog();
                }

            }
        });

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ongoingTask.isSuccessful()){
                    Intent intent = new Intent(OngoingTask.this, MainActivity.class);
                    startActivity(intent);
                }else{
                    //Obteniendo los strings que seran asignados a los elementos del Dialog
                    int title = R.string.cdOngoingTaskReturnTitle;
                    int subtitle = R.string.cdOngoingTaskReturnSubtitle;
                    int confirm = R.string.cdOngoingTaskConfirmar;
                    int cancel = R.string.cdOngoingTaskCancelar;
                    showDialog(title, subtitle, confirm, cancel);
                }

            }
        });

        btnEndBreak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                breakTimer.onFinish();
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

        EditText cdEdtInterrupcion = (EditText) confirmation.findViewById(R.id.cdEdtInterrupcion);
        cdEdtInterrupcion.setVisibility(View.GONE);

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

                //Significa que la tarea no fue terminada exitosamente
                ongoingTask.setSuccessful(false);

                //Almaceno los resultados en la base de datos
                saveData();

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

    public void showInterruptionDialog(){
        //Instanciando dialogo de yes/no
        CustomYesNoDialog confirmation = new CustomYesNoDialog(OngoingTask.this);

        confirmation.show();

        confirmation.setContentView(R.layout.custom_yes_no_dialog);

        TextView txtTitle = (TextView) confirmation.findViewById(R.id.cdTxtTitle);
        TextView txtSubtitle = (TextView) confirmation.findViewById(R.id.cdTxtSubtitle);
        Button yes_btn = (Button) confirmation.findViewById(R.id.cdBtnYes);
        Button no_btn = (Button) confirmation.findViewById(R.id.cdBtnNo);
        EditText edtInterrupcion = (EditText) confirmation.findViewById(R.id.cdEdtInterrupcion);

        txtSubtitle.setVisibility(View.GONE);
        no_btn.setVisibility(View.GONE);

        txtTitle.setText(R.string.cdOngoingTaskInterruptionTitle);
        edtInterrupcion.setHint(R.string.cdOngoingTaskInterruptionHint);
        yes_btn.setText(R.string.cdOngoingTaskInterruptionRegistrar);


        confirmation.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //Agregando onClickListener a los botones del diálogo
        yes_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v2) {
                if(!TextUtils.isEmpty(edtInterrupcion.getText().toString()) && !edtInterrupcion.getText().toString().trim().isEmpty()){
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm a");
                    String currentMoment = sdf.format(new Date());
                    ArrayList<String> notes = new ArrayList<>();
                    if(ongoingTask.getNotes() == null){
                        notes.add(edtInterrupcion.getText().toString()+"/"+currentMoment);
                        ongoingTask.setNotes(notes);
                    }else {
                        notes = ongoingTask.getNotes();
                        notes.add(edtInterrupcion.getText().toString()+"/"+currentMoment);
                        ongoingTask.setNotes(notes);
                    }
                    confirmation.dismiss();
                }else{
                    Toast.makeText(getApplicationContext(), "Coloque un nombre a la tarea", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });

    }

    public void saveData(){
        if(ongoingTask.getCurrentPomodoro() > ongoingTask.getCount()){
            ongoingTask.setCurrentPomodoro(ongoingTask.getCurrentPomodoro()-1);
        }

        MainActivity.databaseReference.child(MainActivity.userUID).child("TareasPendientes").child(ongoingTask.getId()).setValue(ongoingTask);

        statistics();
    }

    public void updateUI(String ui){
        switch (ui){
            case "Break":
                constraintLayoutBreak.setVisibility(View.VISIBLE);
                constraintLayoutOngoingTask.setVisibility(View.GONE);
                break;
            case "Work":
                constraintLayoutBreak.setVisibility(View.GONE);
                constraintLayoutOngoingTask.setVisibility(View.VISIBLE);
                break;
        }
        txtCurrentPomodoro.setText("POMODORO "+ongoingTask.getCurrentPomodoro());

    }

    public void statistics(){
        MainActivity.databaseReference.child(MainActivity.userUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Long tareasRealizadas = (Long) snapshot.child("tareasRealizadas").getValue();
                Long tareasExitosas = (Long) snapshot.child("tareasExitosas").getValue();
                Long tareasFallidas = (Long) snapshot.child("tareasFallidas").getValue();
                Long tareasPendientes = (Long) snapshot.child("misTareasPendientes").getValue();

                //Esto significa que ahora hay una tarea pendiente menos, porque ya se realizó
                MainActivity.databaseReference.child(MainActivity.userUID).child("misTareasPendientes").setValue(tareasPendientes - 1);

                if(tareasRealizadas == null){
                    MainActivity.databaseReference.child(MainActivity.userUID).child("tareasRealizadas").setValue(1);
                }else{
                    MainActivity.databaseReference.child(MainActivity.userUID).child("tareasRealizadas").setValue(tareasRealizadas + 1);
                }


                if(ongoingTask.isSuccessful()){
                    if(tareasExitosas == null){
                        MainActivity.databaseReference.child(MainActivity.userUID).child("tareasExitosas").setValue(1);
                    }else{
                        MainActivity.databaseReference.child(MainActivity.userUID).child("tareasExitosas").setValue(tareasExitosas + 1);
                    }
                }else{
                    if(tareasFallidas == null){
                        MainActivity.databaseReference.child(MainActivity.userUID).child("tareasFallidas").setValue(1);
                    }else{
                        MainActivity.databaseReference.child(MainActivity.userUID).child("tareasFallidas").setValue(tareasFallidas + 1);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
package com.domos.balance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class UserProfile extends AppCompatActivity {

    ImageButton btnReturn;
    TextView txtUserName, txtUserAntiquity, txtCompletedTasks, txtFailedTasks, txtPendingTasks;
    ImageView imgEmojiFav;

    Long userAntiquity, tareasRealizadas, tareasExitosas, tareasFallidas, tareasPendientes;

    //private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        initialize();
    }

    public void initialize(){

        txtUserName = (TextView) findViewById(R.id.upTxtUserName);
        txtUserAntiquity = (TextView) findViewById(R.id.upTxtUserAntiquity);
        txtCompletedTasks = (TextView) findViewById(R.id.upTxtCompletedTasksCount);
        txtFailedTasks = (TextView) findViewById(R.id.upTxtFailedTasksCount);
        txtPendingTasks = (TextView) findViewById(R.id.upTxtPendingTasksCount);
        imgEmojiFav = (ImageView) findViewById(R.id.upImgEmojiFav);
        btnReturn = (ImageButton) findViewById(R.id.upBtnReturn);

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfile.this, MainActivity.class);
                startActivity(intent);
            }
        });

        loadData();
        showData();
    }

    public void loadData(){
        Date creationDate = new Date(MainActivity.user.getMetadata().getCreationTimestamp());
        Date currentDate = new Date();

        long difference_in_time = currentDate.getTime() - creationDate.getTime();
        long difference_In_Days= (difference_in_time / (1000 * 60 * 60 * 24)) % 365;
        userAntiquity = difference_In_Days;

        /*mAuth = FirebaseAuth.getInstance();

        Long timestamp =  mAuth.getInstance().
                getAccessToken(false).getResult().getIssuedAtTimestamp();
        userDaysSinceCreation = TimeUnit.MILLISECONDS.toDays(timestamp);*/

        MainActivity.databaseReference.child(MainActivity.userUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tareasRealizadas = (Long) snapshot.child("tareasRealizadas").getValue();
                tareasExitosas = (Long) snapshot.child("tareasExitosas").getValue();
                tareasFallidas = (Long) snapshot.child("tareasFallidas").getValue();
                tareasPendientes = (Long) snapshot.child("misTareasPendientes").getValue();

                txtCompletedTasks.setText(""+tareasExitosas);
                txtFailedTasks.setText(""+tareasFallidas);
                txtPendingTasks.setText(""+tareasPendientes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void showData(){

        String antiquityText = "";
        if(userAntiquity == 0){
             antiquityText = "Usuario desde ahora";
        }else if(userAntiquity == 1){
            antiquityText = "Usuario desde 1 día";
        }else{
            antiquityText = "Usuario desde " + userAntiquity + " días";
        }
        txtUserAntiquity.setText(antiquityText);

        txtUserName.setText(MainActivity.userName);



    }
}
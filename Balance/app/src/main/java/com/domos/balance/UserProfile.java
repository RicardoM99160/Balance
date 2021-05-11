package com.domos.balance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

public class UserProfile extends AppCompatActivity {
    private FirebaseAuth mAuth;
    ImageButton btnReturn;
    TextView txtUserName, txtUserAntiquity, txtCompletedTasks, txtFailedTasks, txtPendingTasks;
    ImageView imgEmojiFav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        initialize();
        getUserStatistics();


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

        txtUserName.setText(MainActivity.nombreUsuario);
    }


    private void getUserStatistics(){
        mAuth = FirebaseAuth.getInstance();
        String something;

        Long timestamp =  mAuth.getInstance().
                getAccessToken(false).getResult().getIssuedAtTimestamp();
        Long userDaysSinceCreation = TimeUnit.MILLISECONDS.toDays(timestamp);

        txtUserAntiquity.setText("Usuario por "+userDaysSinceCreation+" dias");
    }


}
package com.domos.balance;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class CreateTask extends AppCompatActivity {

    Task newTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        newTask = new Task();

    }


}
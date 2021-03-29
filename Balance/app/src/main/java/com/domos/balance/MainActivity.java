package com.domos.balance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    HashMap<String,Task> Tasks = new HashMap<String, Task>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //Creo que este seria ejecutado cuando termine de crear la nueva tarea
    //En el onResume se recibe la nueva tarea y se almacena en la lista de tareas
    //Si no funciona así tendría que crear algun evento o algo así
    @Override
    protected void onResume() {
        super.onResume();

        //Obtengo la tarea de la actividad CreateTask
        //Estoy utilizando serialization porque tengo un problema con los booleanos en Parcelable
        Task newTask = (Task) getIntent().getSerializableExtra("newTask");
        Tasks.put(newTask.getId(),newTask);

    }



    public void crearTarea(View view){
        Intent intent = new Intent(this,CreateTask.class);
        startActivity(intent);
    }

}
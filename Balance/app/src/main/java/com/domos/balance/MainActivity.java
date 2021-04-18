package com.domos.balance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.domos.balance.adapters.TaskAdapter;
import com.domos.balance.data.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ImageView btnUsericon;
    RecyclerView recyclerViewTasks;

    ArrayList<Task> tasksList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tasksList = new ArrayList<>();
        recyclerViewTasks = (RecyclerView) findViewById(R.id.hpRecyclerVTasks);
        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(this));
        llenarTareas();
        TaskAdapter adapter = new TaskAdapter(tasksList);
        recyclerViewTasks.setAdapter(adapter);

        btnUsericon = (ImageView) findViewById(R.id.hpBtnUsericon);
        btnUsericon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Esto no es un boton", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void llenarTareas(){
        tasksList.add(new Task("1","Tarea 1","tu madre",1,R.drawable.nervous,true,true,null));
        tasksList.add(new Task("2","Tarea 2","tu madre 2",1,R.drawable.sick,true,true,null));
        tasksList.add(new Task("3","Tarea 3","tu madre 3",1,R.drawable.superhero,true,true,null));
        tasksList.add(new Task("1","Tarea 1","tu madre",1,R.drawable.nervous,true,true,null));
        tasksList.add(new Task("2","Tarea 2","tu madre 2",1,R.drawable.sick,true,true,null));
        tasksList.add(new Task("3","Tarea 3","tu madre 3",1,R.drawable.superhero,true,true,null));
    }

    //Creo que este seria ejecutado cuando termine de crear la nueva tarea
    //En el onResume se recibe la nueva tarea y se almacena en la lista de tareas
    //Si no funciona así tendría que crear algun evento o algo así

    /*
    @Override
    protected void onResume() {
        super.onResume();

        //Obtengo la tarea de la actividad CreateTask
        //Estoy utilizando serialization porque tengo un problema con los booleanos en Parcelable
        Task newTask = (Task) getIntent().getSerializableExtra("newTask");
        Tasks.put(newTask.getId(),newTask);

    }*/



    public void crearTarea(View view){
        Intent intent = new Intent(this,CreateTask.class);
        startActivity(intent);
    }

}
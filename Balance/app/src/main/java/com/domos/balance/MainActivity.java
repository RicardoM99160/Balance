package com.domos.balance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.domos.balance.adapters.TaskAdapter;
import com.domos.balance.data.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ImageView btnUsericon;
    Button btnNewTask;
    RecyclerView recyclerViewTasks;

    ArrayList<Task> tasksList;

    public static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static DatabaseReference databaseReference = database.getReference("usuarios");
    public static SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    public static String currentDate;
    Query actualUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentDate = sdf.format(new Date());
        actualUser = databaseReference.child("UIDUser").child(currentDate);

        initialize();
    }


    public void initialize(){
        recyclerViewTasks = (RecyclerView) findViewById(R.id.hpRecyclerVTasks);
        btnUsericon = (ImageView) findViewById(R.id.hpBtnUsericon);
        btnNewTask = (Button) findViewById(R.id.hpBtnNewtask);

        selectTaskSetUp();
        buttonSetUp();
    }

    //Configuración del recyclerView que muestra las tareas
    public void selectTaskSetUp(){

        tasksList = new ArrayList<>();
        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(this));

        
        actualUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tasksList.removeAll(tasksList);
                for(DataSnapshot dato : snapshot.getChildren()){
                    Task task = dato.getValue(Task.class);
                    task.setId(dato.getKey());
                    tasksList.add(task);
                }

                TaskAdapter adapter = new TaskAdapter(tasksList);
                recyclerViewTasks.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //llenarTareas();

    }

    //Metodo para asignar OnClickListener a todos los botones
    public void buttonSetUp(){
        btnUsericon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this,"Esto no es un boton", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,OngoingTask.class);
                startActivity(intent);
            }
        });

        btnNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,CreateTask.class);
                startActivity(intent);
            }
        });
    }

    private void llenarTareas(){
        tasksList.add(new Task("1","Tarea 1","tu madre",1,R.drawable.nervous));
        tasksList.add(new Task("2","Tarea 2","tu madre 2",1,R.drawable.sick));
        tasksList.add(new Task("3","Tarea 3","tu madre 3",1,R.drawable.superhero));
        tasksList.add(new Task("1","Tarea 1","tu madre",1,R.drawable.nervous));
        tasksList.add(new Task("2","Tarea 2","tu madre 2",1,R.drawable.sick));
        tasksList.add(new Task("3","Tarea 3","tu madre 3",1,R.drawable.superhero));
    }




}
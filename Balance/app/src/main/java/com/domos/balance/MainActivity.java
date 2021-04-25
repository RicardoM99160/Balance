package com.domos.balance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class MainActivity extends AppCompatActivity {

    ImageView btnUsericon;
    Button btnNewTask;
    RecyclerView recyclerViewTasks;
    LinearLayout linearLayoutWithoutTasks;
    EditText searchBar;

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

        //Inicializo el recyclerView antes, para tratar que las tareas se carguen rápido
        //TODO: Buscar la manera de cargar los datos antes que se cargue la vista (si se puede sino así quedará)
        recyclerViewTasks = (RecyclerView) findViewById(R.id.hpRecyclerVTasks);
        selectTaskSetUp();

        initialize();
    }


    public void initialize(){

        linearLayoutWithoutTasks = (LinearLayout) findViewById(R.id.hpLinearLayoutWithoutTasks);
        btnUsericon = (ImageView) findViewById(R.id.hpBtnUsericon);
        btnNewTask = (Button) findViewById(R.id.hpBtnNewtask);
        searchBar = (EditText) findViewById(R.id.hpSearchbar);

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

                if(tasksList.isEmpty()){
                    linearLayoutWithoutTasks.setVisibility(View.VISIBLE);
                    recyclerViewTasks.setVisibility(View.GONE);
                    searchBar.setVisibility(View.GONE);
                }else{
                    linearLayoutWithoutTasks.setVisibility(View.GONE);
                    recyclerViewTasks.setVisibility(View.VISIBLE);
                    searchBar.setVisibility(View.VISIBLE);
                    TaskAdapter adapter = new TaskAdapter(tasksList);

                    //Evento de click al seleccionar una task

                    adapter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showDialog(v);
                        }
                    });
                    recyclerViewTasks.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    public void showDialog(View v){
        //Instanciando dialogo de yes/no
        CustomYesNoDialog confirmation = new CustomYesNoDialog(MainActivity.this);

        confirmation.show();

        confirmation.setContentView(R.layout.custom_yes_no_dialog);

        TextView txtTitle = (TextView) confirmation.findViewById(R.id.cdTxtTitle);
        TextView txtSubtitle = (TextView) confirmation.findViewById(R.id.cdTxtSubtitle);
        Button yes_btn = (Button) confirmation.findViewById(R.id.cdBtnYes);
        Button no_btn = (Button) confirmation.findViewById(R.id.cdBtnNo);

        txtTitle.setText(R.string.cdHomepageTitle);
        txtSubtitle.setVisibility(View.GONE);
        yes_btn.setText(R.string.cdHomepageConfirmar);
        no_btn.setText(R.string.cdHomepageCancelar);


        confirmation.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //Agregando onClickListener a los botones del diálogo
        yes_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v2) {

                //Paso 1 del ciclo de vida de la tarea
                tasksList.get(recyclerViewTasks.getChildAdapterPosition(v)).setStarted(true);

                Intent i = new Intent(MainActivity.this,OngoingTask.class);

                i.putExtra("newOngoingTask", tasksList.get(recyclerViewTasks.getChildAdapterPosition(v)));
                startActivity(i);
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

    //Metodo para asignar OnClickListener a todos los botones
    public void buttonSetUp(){
        btnUsericon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this,"Esto no es un boton", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,Wellcome.class);
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





}
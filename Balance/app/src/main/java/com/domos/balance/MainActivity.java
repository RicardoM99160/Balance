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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    private FirebaseAuth mAuth;

    ImageView btnUsericon;
    TextView tvNombreUsuario;
    Button btnNewTask;
    RecyclerView recyclerViewTasks;
    LinearLayout linearLayoutWithoutTasks;
    EditText searchBar;

    String strProvider;

    ArrayList<Task> tasksList;

    public static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static DatabaseReference databaseReference = database.getReference("usuarios");
    public static SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    public static String currentDate;
    public static String userUID;
    Query userTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();
        userUID = mAuth.getUid();
        currentDate = sdf.format(new Date());
        userTasks = databaseReference.child(userUID).child("TareasPendientes");



        //Inicializo el recyclerView antes, para tratar que las tareas se carguen rápido
        //TODO: Buscar la manera de cargar los datos antes que se cargue la vista (si se puede sino así quedará)
        recyclerViewTasks = (RecyclerView) findViewById(R.id.hpRecyclerVTasks);
        selectTaskSetUp();

        initialize();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
        }
    }

    public void initialize(){

        linearLayoutWithoutTasks = (LinearLayout) findViewById(R.id.hpLinearLayoutWithoutTasks);
        btnUsericon = (ImageView) findViewById(R.id.hpBtnUsericon);
        btnNewTask = (Button) findViewById(R.id.hpBtnNewtask);
        searchBar = (EditText) findViewById(R.id.hpSearchbar);
        tvNombreUsuario = (TextView) findViewById(R.id.maTvNombre);
        buttonSetUp();
        getUserData();

    }

    //Configuración del recyclerView que muestra las tareas

    public void selectTaskSetUp(){

        tasksList = new ArrayList<>();
        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(this));

        
        userTasks.addValueEventListener(new ValueEventListener() {
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
    //TODO: Esto deberia de ser un listener, y no va aqui
    public void logout(View v){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainActivity.this, Login.class);
        startActivity(intent);

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

    public void getUserData() {

        //logged with google
        FirebaseUser user = mAuth.getCurrentUser();

        if (user == null)
            return;

        String nombreUsuario;

        strProvider = mAuth.getInstance().
                getAccessToken(false).getResult().getSignInProvider();
        
        switch (strProvider) {
            case "google.com":
                nombreUsuario = user.getDisplayName();
                tvNombreUsuario.setText(nombreUsuario);
                break;

            case "password":
                //Connects to Firebase and gets the user name.
                String uid = mAuth.getUid();
                DatabaseReference uidRef = databaseReference.child(uid);
                ValueEventListener eventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String nombreUsuario = dataSnapshot.child("Nombre").getValue(String.class);
                        tvNombreUsuario.setText(nombreUsuario);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                };
                uidRef.addListenerForSingleValueEvent(eventListener);

                break;
        }


    }



}
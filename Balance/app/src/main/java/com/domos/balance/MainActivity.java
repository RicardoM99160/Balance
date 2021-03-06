package com.domos.balance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

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
    TaskAdapter adapter;
    Task tareaSeleccionada;

    public static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static DatabaseReference databaseReference = database.getReference("usuarios");
    public static SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    public static String currentDate;
    public static String userUID;
    Query userTasks;

    public static FirebaseUser user;
    public static String userName;
    public static int pendingTasks;
    public static int startedTasks;
    public static int successfulTasks;
    public static int failedTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pendingTasks = 0;
        startedTasks = 0;
        successfulTasks = 0;
        failedTasks = 0;

        mAuth = FirebaseAuth.getInstance();
        userUID = mAuth.getUid();
        currentDate = sdf.format(new Date());
        userTasks = databaseReference.child(userUID).child("TareasPendientes");



        //Inicializo el recyclerView antes, para tratar que las tareas se carguen rápido
        //TODO: Buscar la manera de cargar los datos antes que se cargue la vista (si se puede sino así quedará)
        recyclerViewTasks = (RecyclerView) findViewById(R.id.hpRecyclerVTasks);
        selectTaskSetUp();

        try {
            initialize();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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

    public void initialize() throws InterruptedException {

        linearLayoutWithoutTasks = (LinearLayout) findViewById(R.id.hpLinearLayoutWithoutTasks);
        btnUsericon = (ImageView) findViewById(R.id.hpBtnUsericon);
        btnNewTask = (Button) findViewById(R.id.hpBtnNewtask);
        searchBar = (EditText) findViewById(R.id.hpSearchbar);
        tvNombreUsuario = (TextView) findViewById(R.id.maTvNombre);

        searchbarSetUp();
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

                    if(task.isStarted()){
                        startedTasks++;
                        if(task.isSuccessful()){
                            successfulTasks++;
                        }else{
                            failedTasks++;
                        }
                    }else{
                        pendingTasks++;
                    }
                }

                if(tasksList.isEmpty()){
                    linearLayoutWithoutTasks.setVisibility(View.VISIBLE);
                    recyclerViewTasks.setVisibility(View.GONE);
                    searchBar.setVisibility(View.GONE);
                }else{
                    linearLayoutWithoutTasks.setVisibility(View.GONE);
                    recyclerViewTasks.setVisibility(View.VISIBLE);
                    searchBar.setVisibility(View.VISIBLE);
                    adapter = new TaskAdapter(tasksList);

                    //Evento de click al seleccionar una task

                    adapter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            tareaSeleccionada = tasksList.get(recyclerViewTasks.getChildAdapterPosition(v));
                            //Si la tarea no ha sido iniciada (isStartetd = false) entonces se muestra el modal para iniciar la tarea
                            if(!tareaSeleccionada.isStarted()){
                                showDialog(v, "iniciar");
                            }else{
                                Intent intent = new Intent(MainActivity.this, TaskDetails.class);
                                intent.putExtra("selectedTask", tareaSeleccionada);
                                startActivity(intent);
                            }
                        }
                    });

                    adapter.onLongClick(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            tareaSeleccionada = tasksList.get(recyclerViewTasks.getChildAdapterPosition(v));
                            showDialog(v, "archivar");
                            return false;
                        }
                    });
                    recyclerViewTasks.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    public void showDialog(View v, String action){

        //Instanciando dialogo de yes/no
        CustomYesNoDialog confirmation = new CustomYesNoDialog(MainActivity.this);

        confirmation.show();

        confirmation.setContentView(R.layout.custom_yes_no_dialog);

        TextView txtTitle = (TextView) confirmation.findViewById(R.id.cdTxtTitle);
        TextView txtSubtitle = (TextView) confirmation.findViewById(R.id.cdTxtSubtitle);
        Button yes_btn = (Button) confirmation.findViewById(R.id.cdBtnYes);
        Button no_btn = (Button) confirmation.findViewById(R.id.cdBtnNo);

        EditText cdEdtInterrupcion = (EditText) confirmation.findViewById(R.id.cdEdtInterrupcion);
        cdEdtInterrupcion.setVisibility(View.GONE);

        switch(action){
            case "iniciar":
                txtTitle.setText(R.string.cdHomepageStartTaskTitle);
                txtSubtitle.setVisibility(View.GONE);
                break;
            case "archivar":
                txtTitle.setText(R.string.cdHomepageRemoveTaskTitle);
                txtSubtitle.setVisibility(View.VISIBLE);
                txtSubtitle.setText(R.string.cdHomepageRemoveTaskSubtitle);
        }
        yes_btn.setText(R.string.cdHomepageConfirmar);
        no_btn.setText(R.string.cdHomepageCancelar);


        confirmation.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //tareaSeleccionada = tasksList.get(recyclerViewTasks.getChildAdapterPosition(v));

        //Agregando onClickListener a los botones del diálogo
        yes_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v2) {

                switch(action){
                    case "iniciar":
                        //Paso 1 del ciclo de vida de la tarea
                        tareaSeleccionada.setStarted(true);
                        Intent i = new Intent(MainActivity.this,OngoingTask.class);
                        i.putExtra("newOngoingTask", tareaSeleccionada);
                        startActivity(i);
                        break;
                    case "archivar":
                        databaseReference.child(MainActivity.userUID).child("TareasArchivadas").child(currentDate).push().setValue(tareaSeleccionada);
                        databaseReference.child(MainActivity.userUID).child("TareasPendientes").child(tareaSeleccionada.getId()).setValue(null);
                }


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


    public void searchbarSetUp(){
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
    }

    private void filter(String text){
        ArrayList<Task> filterdList = new ArrayList<>();
        for (Task task: tasksList){
            if(task.getName().toLowerCase().contains(text.toLowerCase())){
                filterdList.add(task);
            }
        }
        adapter.filterList(filterdList);
    }

    //Metodo para asignar OnClickListener a todos los botones

    public void buttonSetUp(){


        btnUsericon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v);
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

    //
    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {

                    case R.id.menuitemMiPerfil:
                        //TODO: Programar activiy Mi Perfil
                        intent = new Intent(MainActivity.this, UserProfile.class);
                        startActivity(intent);
                        return true;

                    //el segundo cierra sesion
                    case R.id.menuitemLogout:

                        FirebaseAuth.getInstance().signOut();
                        intent = new Intent(MainActivity.this, Welcome.class);
                        startActivity(intent);

                        return true;
                    default:
                        return false;
                }

            }
        });

        inflater.inflate(R.menu.menu_principal, popup.getMenu());
        popup.show();
    }

    //
    public void getUserData() throws InterruptedException {

        //logged with google
        user = mAuth.getCurrentUser();

        if (user == null)
            return;

        Thread.sleep(1000);
        strProvider = mAuth.getInstance().
                getAccessToken(false).getResult().getSignInProvider();
        
        switch (strProvider) {
            case "google.com":
                userName = user.getDisplayName();
                tvNombreUsuario.setText(userName);
                break;

            case "password":
                //Connects to Firebase and gets the user name.
                String uid = mAuth.getUid();
                DatabaseReference uidRef = databaseReference.child(uid);
                ValueEventListener eventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        userName = dataSnapshot.child("Nombre").getValue(String.class);
                        tvNombreUsuario.setText(userName);
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
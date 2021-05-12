package com.domos.balance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.domos.balance.adapters.NoteAdapter;
import com.domos.balance.data.Task;

import java.util.ArrayList;

public class TaskDetails extends AppCompatActivity {

    ImageView ivEmoji;
    TextView txtTitle, txtTaskname, txtDuracion, txtCount, txtSinInterrupciones;
    ImageButton btnReturn;
    Button btnEliminar;

    Task selectedTask;

    RecyclerView recyclerViewNotes;
    ArrayList<String> notes;
    NoteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        //Recuperando tarea en proceso
        Intent intent = getIntent();
        selectedTask = new Task();
        selectedTask = (Task) intent.getSerializableExtra("selectedTask");

        recyclerViewNotes = (RecyclerView) findViewById(R.id.tdRecyclerVNotes);

        initialize();
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        notes = new ArrayList<>();
        recyclerViewNotes.setLayoutManager(new LinearLayoutManager(this));

        if(selectedTask.getNotes() == null){
            //Mostrar un texto que no hay notas
            recyclerViewNotes.setVisibility(View.GONE);
            btnEliminar.setVisibility(View.GONE);
            txtSinInterrupciones.setVisibility(View.VISIBLE);
        }else {
            notes = selectedTask.getNotes();
            adapter = new NoteAdapter(notes);
            recyclerViewNotes.setAdapter(adapter);

            recyclerViewNotes.setVisibility(View.VISIBLE);
            btnEliminar.setVisibility(View.VISIBLE);
            txtSinInterrupciones.setVisibility(View.GONE);
        }
    }

    public void initialize(){
        txtTitle = (TextView) findViewById(R.id.tdTxtTitulo);
        txtTaskname = (TextView) findViewById(R.id.tdTxtTaskname);
        txtDuracion = (TextView) findViewById(R.id.tdTxtDuration);
        txtCount = (TextView) findViewById(R.id.tdTxtCount);
        txtSinInterrupciones = (TextView) findViewById(R.id.tdTxtSinInterrupciones);
        ivEmoji = (ImageView) findViewById(R.id.tdImgEmoji);
        btnReturn = (ImageButton) findViewById(R.id.tdBtnReturn);
        btnEliminar = (Button) findViewById(R.id.tdBtnEliminar);

        String title = selectedTask.isSuccessful() ? "Tarea completada" : "Tarea fallida";
        txtTitle.setText(title);
        txtTaskname.setText(selectedTask.getName());
        txtDuracion.setText(selectedTask.getDuration());
        String cantidadPomodoros = selectedTask.getCount() == 1 ? " pomodoro" : " pomodoros";
        txtCount.setText(selectedTask.getCount() + cantidadPomodoros);
        ivEmoji.setImageResource(selectedTask.getEmoji());


        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaskDetails.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void loadData(){

    }
}
package com.domos.balance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.domos.balance.data.Task;

public class TaskDetails extends AppCompatActivity {

    ImageView ivEmoji;
    TextView txtTitle, txtTaskname, txtDuracion, txtCount;
    ImageButton btnReturn;
    Button btnEliminar;

    Task selectedTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        //Recuperando tarea en proceso
        Intent intent = getIntent();
        selectedTask = new Task();
        selectedTask = (Task) intent.getSerializableExtra("selectedTask");

        initialize();
    }

    public void initialize(){
        txtTitle = (TextView) findViewById(R.id.tdTxtTitulo);
        txtTaskname = (TextView) findViewById(R.id.tdTxtTaskname);
        txtDuracion = (TextView) findViewById(R.id.tdTxtDuration);
        txtCount = (TextView) findViewById(R.id.tdTxtCount);
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
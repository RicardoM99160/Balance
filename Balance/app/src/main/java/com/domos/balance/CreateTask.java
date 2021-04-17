package com.domos.balance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

public class CreateTask extends AppCompatActivity {

    Task newTask;
    NumberPicker numCount;
    NumberPicker numDuration;
    TextView txtTimeMethod;
    String[] durations = new String[]{"25 min - 5 min","45 min - 15 min","50 min - 10 min", "30 sec - 5 sec"};
    Boolean timeIsSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        timeIsSelected = false;

        //Configuración del TimePicker para seleccionar la duración en tiempo de la tarea
        numCount = (NumberPicker) findViewById(R.id.ctNumCount);
        numCount.setMinValue(1);
        numCount.setMaxValue(4);
        numCount.setVisibility(View.GONE);

        //Configuración del NumberPicker utilizado para seleccionar un método
        numDuration = (NumberPicker) findViewById(R.id.ctNumDuration);
        numDuration.setMinValue(0);
        numDuration.setMaxValue(durations.length-1);
        numDuration.setDisplayedValues(durations);
        numDuration.setVisibility(View.VISIBLE);

        //Configuración del TextView que muestra el título "Asignar Método" o "Asignar Tiempo"
        txtTimeMethod = (TextView) findViewById(R.id.ctTxtDurationCount);
        txtTimeMethod.setText(R.string.ctTxtAsignarMetodo);

        newTask = new Task();

    }

    //Este método se asigna a los botones desde el XML
    public void selectTimeMethod(View view){
        if(timeIsSelected){
            numDuration.setVisibility(View.VISIBLE);
            numCount.setVisibility(View.GONE);
            txtTimeMethod.setText(R.string.ctTxtAsignarMetodo);
            timeIsSelected = false;
        }else{
            //Este se ejecuta primero cuando presiono el botón, porque en el onCreate defino que timeIsSelected = false
            numDuration.setVisibility(View.GONE);
            numCount.setVisibility(View.VISIBLE);
            txtTimeMethod.setText(R.string.ctTxtAsignarTiempo);
            timeIsSelected = true;
        }
    }

    public void goToHomepage(View view){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

}
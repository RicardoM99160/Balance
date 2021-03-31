package com.domos.balance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import org.w3c.dom.Text;

public class CreateTask extends AppCompatActivity {

    Task newTask;
    TimePicker time;
    NumberPicker method;
    TextView txtTimeMethod;
    String[] methods = new String[]{"GTD","Pomodoro","DO"};
    Boolean timeIsSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        timeIsSelected = false;

        //Configuración del TimePicker para seleccionar la duración en tiempo de la tarea
        time = (TimePicker) findViewById(R.id.tmpTime);
        time.setIs24HourView(true);
        time.setVisibility(View.GONE);

        //Configuración del NumberPicker utilizado para seleccionar un método
        method = (NumberPicker) findViewById(R.id.numMethod);
        method.setMinValue(0);
        method.setMaxValue(methods.length-1);
        method.setDisplayedValues(methods);
        method.setValue(1);
        method.setVisibility(View.VISIBLE);

        //Configuración del TextView que muestra el título "Asignar Método" o "Asignar Tiempo"
        txtTimeMethod = (TextView) findViewById(R.id.txtTimeMethod);
        txtTimeMethod.setText(R.string.ctTxtAsignarMetodo);

        newTask = new Task();

    }

    public void selectTimeMethod(View view){
        if(timeIsSelected){
            method.setVisibility(View.VISIBLE);
            time.setVisibility(View.GONE);
            txtTimeMethod.setText(R.string.ctTxtAsignarMetodo);
            timeIsSelected = false;
        }else{
            //Este se ejecuta primero cuando presiono el botón, porque en el onCreate defino que timeIsSelected = false
            method.setVisibility(View.GONE);
            time.setVisibility(View.VISIBLE);
            txtTimeMethod.setText(R.string.ctTxtAsignarTiempo);
            timeIsSelected = true;
        }
    }

    public void goToHomepage(View view){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

}
package com.domos.balance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.domos.balance.adapters.EmojiAdapter;

import java.util.ArrayList;

public class CreateTask extends AppCompatActivity {

    //Inicializando elementos de la vista
    NumberPicker numDuration, numCount;
    TextView txtDurationCountEmoji, txtDuration, txtCount;
    GridView gridViewEmojis;
    Button btnAccept, btnCancel;
    ImageView btnEmoji;
    ImageButton btnReturn, btnSettings;


    //Inicializando variables restantes
    String[] durations = new String[]{"25 min - 5 min","45 min - 15 min","50 min - 10 min", "30 sec - 5 sec"}; //utilizado por NumberPicker numDuration
    Boolean timeIsSelected;
    int step = 0, stepEmoji = 3;
    EmojiAdapter adapterEmojis;

    //Todavía no lo estoy implementando

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        step = 0;
        initialize();

        timeIsSelected = false;

    }

    public void initialize(){
        numCount = (NumberPicker) findViewById(R.id.ctNumCount);
        numDuration = (NumberPicker) findViewById(R.id.ctNumDuration);
        btnAccept = (Button) findViewById(R.id.ctBtnAccept);
        btnCancel = (Button) findViewById(R.id.ctBtnCancel);
        btnEmoji = (ImageView) findViewById(R.id.ctBtnEmoji);
        btnReturn = (ImageButton) findViewById(R.id.ctBtnReturn);
        btnSettings = (ImageButton) findViewById(R.id.ctBtnSettings);

        //Configuración del TextView que muestra el título "Asignar Método" o "Asignar Tiempo"
        txtDurationCountEmoji = (TextView) findViewById(R.id.ctTxtDurationCount);
        txtDurationCountEmoji.setText(R.string.ctTxtAsignarDuracion);

        txtDuration = (TextView) findViewById(R.id.ctTxtDuration);
        txtCount = (TextView) findViewById(R.id.ctTxtCount);

        btnCancel.setEnabled(false);

        selectCountSetup();
        selectDurationSetUp();
        selectEmojiSetUp();
        buttonSetUp();
    }

    //Configuración del NumberPicker para seleccionar la cantidad de pomodoros
    public void selectCountSetup(){
        numCount.setMinValue(1);
        numCount.setMaxValue(4);
        numCount.setVisibility(View.GONE);

        numCount.setValue(1);
        txtCount.setText(numCount.getValue()+" pomodoro");

        numCount.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if(newVal == 1){
                    txtCount.setText(newVal+" pomodoro");
                }else{
                    txtCount.setText(newVal+" pomodoros");
                }
            }
        });


    }

    //Configuración del NumberPicker utilizado para seleccionar la duración del pomodoro
    public void selectDurationSetUp(){
        numDuration.setMinValue(0);
        numDuration.setMaxValue(durations.length-1);
        numDuration.setDisplayedValues(durations);
        numDuration.setVisibility(View.VISIBLE);

        numDuration.setValue(0);
        txtDuration.setText(durations[numDuration.getValue()]);

        numDuration.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                txtDuration.setText(durations[newVal]);
            }
        });


    }

    //Configuración del gridview que muestra los emojis
    public void selectEmojiSetUp(){
        ArrayList<Integer> emojis = new ArrayList<>();
        emojis.add(R.drawable.nervous);
        emojis.add(R.drawable.sick);
        emojis.add(R.drawable.superhero);
        gridViewEmojis = (GridView) findViewById(R.id.ctGridvEmojis);
        adapterEmojis = new EmojiAdapter(this,emojis);
        gridViewEmojis.setAdapter(adapterEmojis);
        gridViewEmojis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                btnEmoji.setImageResource(emojis.get(position));

                //Despues de seleccionar un emoji ocultar
                gridViewEmojis.setVisibility(View.GONE);
                selectDurationCount(step);
            }
        });
    }

    //Metodo para asignar OnClickListener a todos los botones
    public void buttonSetUp(){

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(step < 1) {
                    step++;
                    selectDurationCount(step);
                }else if(step == 1){

                    //TODO: aquí debo guardar la nueva tarea en el arreglo

                    //TODO: Se podría mostrar un cuado de dialogo para confirmar si se desea crear la tarea
                    //Si se selecciona cancelar hacer step = 1;

                    Intent intent = new Intent(CreateTask.this, MainActivity.class);
                    startActivity(intent);
                }

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(step > 0){

                }
                step--;
                selectDurationCount(step);
            }
        });

        btnEmoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDurationCount(stepEmoji);
            }
        });

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateTask.this,MainActivity.class);
                startActivity(intent);
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CreateTask.this, "No existen los ajustes jaja", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void selectDurationCount(int nextStep){
        switch(nextStep){
            case 0:
                txtDurationCountEmoji.setText(R.string.ctTxtAsignarDuracion);
                numDuration.setVisibility(View.VISIBLE);
                numCount.setVisibility(View.GONE);
                gridViewEmojis.setVisibility(View.GONE);
                btnCancel.setEnabled(false);
                break;
            case 1:
                txtDurationCountEmoji.setText(R.string.ctTxtAsignarCantidad);
                numCount.setVisibility(View.VISIBLE);
                numDuration.setVisibility(View.GONE);
                gridViewEmojis.setVisibility(View.GONE);
                btnCancel.setEnabled(true);
                break;
            case 3:
                txtDurationCountEmoji.setText(R.string.ctTxtAsignarEmoji);
                gridViewEmojis.setVisibility(View.VISIBLE);
                numCount.setVisibility(View.GONE);
                numDuration.setVisibility(View.GONE);
                break;
        }
    }


    //Este método se asigna a los botones desde el XML
    public void selectTimeMethod(View view){
        if(timeIsSelected){
            numDuration.setVisibility(View.VISIBLE);
            numCount.setVisibility(View.GONE);
            txtDurationCountEmoji.setText(R.string.ctTxtAsignarDuracion);
            timeIsSelected = false;
        }else{
            //Este se ejecuta primero cuando presiono el botón, porque en el onCreate defino que timeIsSelected = false
            numDuration.setVisibility(View.GONE);
            numCount.setVisibility(View.VISIBLE);
            txtDurationCountEmoji.setText(R.string.ctTxtAsignarCantidad);
            timeIsSelected = true;
        }
    }

    public void goToHomepage(View view){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

}
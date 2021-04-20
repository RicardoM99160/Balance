package com.domos.balance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Wellcome extends AppCompatActivity {

    Button btnInicioSesion, btnRegistrarse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wellcome);

        btnInicioSesion = (Button) findViewById(R.id.wBtnLogin);
        btnRegistrarse = (Button) findViewById(R.id.wBtnSignUp);

        btnInicioSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Wellcome.this, Login.class);
                startActivity(intent);
            }
        });
    }
}
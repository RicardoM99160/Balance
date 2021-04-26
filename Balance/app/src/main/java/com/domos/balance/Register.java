package com.domos.balance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    TextInputEditText edtRegistrarCorreo, edtRegistrarContra, edtRegistrarNombre;
    Button btnRegistrarUsuario;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        inicializar();


    }


    public void inicializar() {

        edtRegistrarCorreo = (TextInputEditText) findViewById(R.id.rgEdtxtCorreo);
        edtRegistrarContra = (TextInputEditText) findViewById(R.id.rgEdtxtPassword);
        edtRegistrarNombre = (TextInputEditText) findViewById(R.id.rgEdtxtNombre);

        btnRegistrarUsuario = (Button) findViewById(R.id.rgBtnLogIn);

        btnRegistrarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Programar las funciones de firebase que creen la nueva cuenta
                String correo, contra, nombre;
                correo = edtRegistrarCorreo.getText().toString();
                contra = edtRegistrarContra.getText().toString();
                nombre = edtRegistrarNombre.getText().toString();

                //TODO:Trasladar texto a archivo de strings

                if (TextUtils.isEmpty(correo)) {
                    Toast.makeText(getApplicationContext(), "Por favor ingrese un correo", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(contra)) {
                    Toast.makeText(getApplicationContext(), "Por favor ingrese una contraseña", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(nombre)) {
                    Toast.makeText(getApplicationContext(), "Por favor ingrese un nombre", Toast.LENGTH_LONG).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(correo, contra)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Registro de cuenta exitosa", Toast.LENGTH_LONG).show();
                                    //TODO:Esto es bien sucio, sucio bichos sucio
                                    HashMap<String, String> nuevoUsuario = new HashMap<>();
                                    nuevoUsuario.put("Nombre", nombre);
                                    MainActivity.databaseReference.child(task.getResult().getUser().getUid()).setValue(nuevoUsuario);
                                    Intent intent = new Intent(Register.this, Login.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Registro de cuenta fallido", Toast.LENGTH_LONG).show();
                                }
                            }
                        });


            }
        });

    }

}
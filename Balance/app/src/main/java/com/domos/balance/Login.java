package com.domos.balance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.GoogleAuthProvider;


public class Login extends AppCompatActivity {

    TextInputEditText edtCorreo, edtContra;
    TextView tvGoToRegister;
    Button btnIniciarSesion, btnLoginGoogle;
    String correo, contra;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    public static final int RC_SIGN_IN=  1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        inicializar();



    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void inicializar() {

        edtCorreo = (TextInputEditText) findViewById(R.id.lgEdtxtEmail);
        edtContra = (TextInputEditText) findViewById(R.id.lgEdtxtPassword);
        btnIniciarSesion = (Button) findViewById(R.id.lgBtnLogIn);
        btnLoginGoogle = (Button) findViewById(R.id.lgBtnLoginGoogle);
        tvGoToRegister = (TextView) findViewById(R.id.lgGoToRegister);


        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Programar las funciones de firebase que revisan si el usuario existe

                correo = edtCorreo.getText().toString();
                contra = edtContra.getText().toString();

                if (TextUtils.isEmpty(correo)) {
                    Toast.makeText(getApplicationContext(), "Por favor ingrese un correo", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(contra)) {
                    Toast.makeText(getApplicationContext(), "Por favor ingrese una contraseña", Toast.LENGTH_LONG).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(correo, contra)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Inicio de sesión exitoso", Toast.LENGTH_LONG).show();

                                    //Esto solo debe suceder cuando el inicio de sesión es exitoso
                                    Intent intent = new Intent(Login.this, MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Inicio de sesión fallido", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

        btnLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                authenticateWithGoogle(account);

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                // ...
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void authenticateWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "Sorry authentication failed.", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }



    public void navigateToRegister(View v){

        Intent intent = new Intent(Login.this,Register.class);
        startActivity(intent);
    }

}
package com.example.sesion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;




public class Registrarse extends AppCompatActivity {

    private FirebaseAuth mAuth;   // <------

    private EditText correo;
    private EditText password1;
    private EditText password2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);
        mAuth = FirebaseAuth.getInstance();  // <------
        correo = findViewById(R.id.correo);
        password1 = findViewById(R.id.password);
        password2 = findViewById(R.id.verifica_password);


    }
    @Override
    public void onStart() {  // <------
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
       // if(currentUser != null){
          //  reload();    // <------
        //}

        if(currentUser != null){
            Intent i = new Intent(getApplicationContext(),usuario.class);
            startActivity(i);

        }
    }

    public void registrar_usuario(View view) {

        if ( password1.getText().toString().equals(password2.getText().toString())) {
            createAccount(correo.getText().toString(), password1.getText().toString());
        } else
               { Toast.makeText(this, "Las contraseñas no coinciden",
                Toast.LENGTH_SHORT).show(); }


    }

    public void reload() {
      //  Intent i = new Intent(getApplicationContext(),Registrarse.class);
      //  startActivity(i);
    }

    public void createAccount( String email, String password){

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                           // Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(getApplicationContext(), "Usuario creado",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                            //updateUI(); // <------
                            Intent i = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(i);
                        } else {
                            // If sign in fails, display a message to the user.
                           // Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            //Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                               //     Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), "Usuario no creado",
                                    Toast.LENGTH_SHORT).show();
                           // updateUI(null);
                        }
                    }
                });
    }

    public void updateUI () {  // <------
                          //por el momento no le paso usuario pero se puede sobrecargar
                          // el método
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);   //AQUI CAMBIAR HACIA LA INTERFAZ DE USUARIO
                            // como no la tengo voy al MainActivity
    }



}
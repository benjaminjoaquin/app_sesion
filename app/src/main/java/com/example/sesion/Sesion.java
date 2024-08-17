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

public class Sesion extends AppCompatActivity {

    private FirebaseAuth mAuth;   // <------

    private EditText correo;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sesion);
        mAuth = FirebaseAuth.getInstance();  // <------
        correo = findViewById(R.id.sesion_email);
        password = findViewById(R.id.sesion_password);
    }

    @Override
    public void onStart() {  // <------
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent i = new Intent(getApplicationContext(),usuario.class);
            startActivity(i);

        }
        //if(currentUser != null){
         //   reload();    // <------
       // }
    }

public void iniciar_sesion (View view) {

    signIn (correo.getText().toString(), password.getText().toString());
}

    public void reload() {
     //   Intent i = new Intent(getApplicationContext(),Sesion.class);
      //  startActivity(i);
    }

   public void signIn ( String email, String password ) {
       mAuth.signInWithEmailAndPassword(email, password)
               .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       if (task.isSuccessful()) {
                           // Sign in success, update UI with the signed-in user's information
                           //Log.d(TAG, "signInWithEmail:success");
                           Toast.makeText(getApplicationContext(), "Inicio de sesión exitoso",
                                   Toast.LENGTH_SHORT).show();
                           FirebaseUser user = mAuth.getCurrentUser();
                           //updateUI(user);
                          // updateUI();
                           Intent i = new Intent(getApplicationContext(),usuario.class);
                           startActivity(i);
                       } else {
                           // If sign in fails, display a message to the user.
                           //Log.w(TAG, "signInWithEmail:failure", task.getException());
                           Toast.makeText(getApplicationContext(), "Authentication failed.",
                                   Toast.LENGTH_SHORT).show();
                           //updateUI(null);

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
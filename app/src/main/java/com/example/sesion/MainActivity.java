package com.example.sesion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;   // <------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();  // <------
        setContentView(R.layout.activity_main);
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

    }

    public void registrarse (View view){
        Intent i = new Intent (this, Registrarse.class);
        startActivity(i);

    }

    public void iniciar_sesion (View view){
        Intent i = new Intent (this, Sesion.class);
        startActivity(i);

    }

}
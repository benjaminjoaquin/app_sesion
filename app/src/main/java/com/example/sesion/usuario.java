package com.example.sesion;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;


public class usuario extends AppCompatActivity {
    private static final int FILE_SELECT_CODE = 0; // <--------
    private final String TAG = "java.StorageActivity";

    private FirebaseAuth mAuth;   // <------
    private FirebaseStorage storage;

    private TextView email;
    private TextView id;
    private TextView verificado;
    private TextView ruta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);
        mAuth = FirebaseAuth.getInstance();  // <------
        email = findViewById(R.id.email_user);
        id = findViewById(R.id.id_user);
        verificado = findViewById(R.id.verificado_user);
        ruta = findViewById(R.id.ruta_archivo);
        obtener_datos();
        storage = FirebaseStorage.getInstance();
    }

    @Override
    public void onStart() {  // <------
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);

        }

    }


    public void showFileChooser(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {

            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "no existe un file chooser",
                    Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
// Get the Uri of the selected file
                    Uri uri = data.getData();
                    // Log.d(TAG, "File Uri: " + uri.toString());
// Get the path
                    String path = "";
                    try {
                        path = getPath(usuario.this, uri); //--------
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    ruta.setText(path + "");
                    //Log.d(TAG, "File Path: " + path);
// Get the file instance
// File file = new File(path);
// Initiate the upload
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public static String getPath(usuario context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) { // Eat it
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }


    public void obtener_datos() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            String e_mail = user.getEmail();
            String uid = user.getUid();
            boolean emailVerified = user.isEmailVerified();

            email.setText(e_mail + "");
            id.setText(uid + "");
            verificado.setText(emailVerified + "");


        }

    }

    public void cerrar_sesion(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);

    }

    public void ver_archivos(View view) {
        if (mAuth.getCurrentUser() != null) {
            Intent i = new Intent(this, MaterialLista.class);
            startActivity(i);
        } else {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(usuario.this);
            builder1.setMessage("Ha ocurrido un Error");
            builder1.setCancelable(true);
            builder1.setPositiveButton("ok", (dialog, id) -> {
                dialog.cancel();
            });
            AlertDialog alert1 = builder1.create();
            alert1.show();
        }

    }


    public void subir_archivo(View view) throws FileNotFoundException {
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();  // <---
        Uri file = Uri.fromFile(new File(ruta.getText()+""));
        StorageReference riversRef = storageRef.child("files/"+file.getLastPathSegment());
        UploadTask uploadTask = riversRef.putFile(file);


// Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                AlertDialog.Builder builder1 = new AlertDialog.Builder(usuario.this);
                builder1.setMessage("Ha ocurrido un Error");
                builder1.setCancelable(true);
                builder1.setPositiveButton("ok", (dialog, id) -> {
                    dialog.cancel();
                });
                AlertDialog alert1 = builder1.create();
                alert1.show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                AlertDialog.Builder builder1 = new AlertDialog.Builder(usuario.this);
                builder1.setMessage("Subida completada");
                builder1.setCancelable(true);
                builder1.setPositiveButton("ok", (dialog, id) -> {
                    dialog.cancel();
                });
                AlertDialog alert1 = builder1.create();
                alert1.show();
                ruta.setText("");

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
               Log.d(TAG, "Upload is " + progress + "% done");


            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "Upload is paused");
            }
        });

    }

}
package com.example.sesion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MaterialLista extends AppCompatActivity {
    private ListView listView;
    private ArrayList<String> documentos;

    //private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_lista);
        listView = findViewById(R.id.listDescargas);
        documentos = new ArrayList<>();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        // [START storage_list_all]
        StorageReference listRef = storage.getReference().child("files");


       // storageReference = FirebaseStorage.getInstance().getReference();
       // StorageReference listRef = storageReference.child("files");

        listRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference item: listResult.getItems()) {
                            documentos.add(item.getName()+"");
                        }

                        ArrayAdapter <String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                               android.R.layout.simple_list_item_1,documentos);
                        listView.setAdapter(adapter);
                    }
                })
                .addOnFailureListener( e -> {
AlertDialog.Builder builder1 = new AlertDialog.Builder (MaterialLista.this);
builder1.setMessage("Ha ocurrido un Error");
                    builder1.setCancelable(true);
                    builder1.setPositiveButton("ok", (dialog, id) -> {dialog.cancel();});
                AlertDialog alert1 = builder1.create();
                alert1.show();
                });


    }  //oncreate


    public void volver (View view ){
        Intent i = new Intent(this, usuario.class);
        startActivity(i);
    }

}
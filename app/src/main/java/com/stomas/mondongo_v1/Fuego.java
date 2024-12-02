package com.stomas.mondongo_v1;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Fuego extends AppCompatActivity {

    //Declaro variables
    private EditText txtCodigo, txtNombre, txtDueño, txtDireccion;

    private ListView lista;

    private Spinner spCalidad;

    String[] Tipos_Calidad = {"Exoticas", "Domesticas", "Acuaticos"};
    //Definimos un variable para la conexion de Firebase

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase);
        //Invocamos un metodo para cargar la lista
        CargarListaFireStore();
        //Inicializo la conexion de BD
        db = FirebaseFirestore.getInstance();
        //Uno las variables del formulario XML
        txtCodigo = findViewById(R.id.txtCodigo);
        txtNombre = findViewById(R.id.txtNombre);
        txtDueño = findViewById(R.id.txtDueño);
        txtDireccion = findViewById(R.id.txtDireccion);
        spCalidad = findViewById(R.id.spCalidad);
        lista = findViewById(R.id.lista);
        //Poblamos el Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, Tipos_Calidad);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCalidad.setAdapter(adapter);

    }

    //Metodo para enviar los datos a firebase
    public void enviarDatosFirestore(View view) {
        //Obrenemos los campos ingresados en el formulario
        String codigo = txtCodigo.getText().toString();
        String nombre = txtNombre.getText().toString();
        String dueño = txtDueño.getText().toString();
        String direccion = txtDireccion.getText().toString();
        String tipoMascota = spCalidad.getSelectedItem().toString();

        //mapeamos los datos ingresados para enviarlos a Firebase
        Map<String, Object> meme = new HashMap<>();
        meme.put("codigo", codigo);
        meme.put("nombre", nombre);
        meme.put("dueño", dueño);
        meme.put("direccion", direccion);
        meme.put("tipoMascota", tipoMascota);
        //Envio los datos a Firebase
        db.collection("mascotas")
                .document(codigo)
                .set(meme)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Datos Enviados",Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Datos no enviados", Toast.LENGTH_SHORT).show();
                });


    }

    public void CargarLista(View view) {
        //invoco el metodo que carga lista de firebase
        CargarListaFireStore();

    }

    public void CargarListaFireStore(){
        //obtenemos la instancia de firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //hacemos una consulta a la coleccion llamada mascotas
        db.collection("mascotas")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        //Si la consulta a la coleccion fue exitosa
                        if(task.isSuccessful()) {
                            //Creamos un Arraylist para almacenar los elementos
                            List<String> listaMeme = new ArrayList<>();
                            //Para mostrar los elementos de la lista utilizo un for
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String linea = "||" + document.getString("codigo") +
                                        "||" + document.getString("nombre") +
                                        "||" + document.getString("dueño") +
                                        "||" + document.getString("direccion");
                                listaMeme.add(linea);

                            }
                            //creamos un ArrayAdapter para mostrar las mascotas en la lista
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                    Fuego.this, android.R.layout.simple_list_item_1, listaMeme);
                            lista.setAdapter(adapter);
                        }else{
                            //Imprimo en la consola si ahi errores al traer los datos
                            Log.e("TAG", "Error al obtener los datos", task.getException());
                        }
                    }
                });
    }

}

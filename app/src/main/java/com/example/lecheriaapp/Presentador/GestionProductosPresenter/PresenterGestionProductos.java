package com.example.lecheriaapp.Presentador.GestionProductosPresenter;

import android.app.Dialog;
import android.content.Context;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.lecheriaapp.R;
import com.example.lecheriaapp.Vista.GestionProductosView.AgregarProductoFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class PresenterGestionProductos implements View.OnClickListener {

    private Context mContext;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private Dialog dialog;
    private EditText mNombre, mCaloria, mPrecio, mDisponibilidad, mIngredientes, mEstado;

    public PresenterGestionProductos(Context mContext, DatabaseReference mDatabase, FirebaseAuth mAuth) {
        this.mContext = mContext;
        this.mDatabase = mDatabase;
        this.mAuth = mAuth;
    }

    public void agregarProducto() {
        //Se crea un diálogo para agregar un nuevo producto
        Dialog dialog = new Dialog(mContext);
        //Se establece la vista del diálogo como el layout "agregarproducto"
        dialog.setContentView(R.layout.agregarproducto);
        //Se asignan las referencias de los campos del layout a las variables correspondientes
        mNombre = dialog.findViewById(R.id.textNombreProducto);
        mCaloria = dialog.findViewById(R.id.textCalorias);
        mPrecio = dialog.findViewById(R.id.textPrecio);
        mDisponibilidad = dialog.findViewById(R.id.textDisponibilidad);
        mIngredientes = dialog.findViewById(R.id.textIngredientes);
        mEstado = dialog.findViewById(R.id.textEstado);
        //Se asigna el botón "Agregar" del layout a la variable "mAddButton" y se establece su acción al hacer clic en el botón "Agregar"
        Button mAddButton = dialog.findViewById(R.id.btnAgregar);
        mAddButton.setOnClickListener(this);
        //Se muestra el diálogo en la pantalla
        dialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //Si se hace clic en el botón "Agregar"
            case R.id.btnAgregar:
                //Se obtienen los valores de los campos del layout
                String estado = mEstado.getText().toString();
                String nombre = mNombre.getText().toString();
                String caloria = mCaloria.getText().toString();
                float precio = Float.parseFloat(mPrecio.getText().toString());
                String disponibilidad = mDisponibilidad.getText().toString();
                String ingredientes = mIngredientes.getText().toString();
                //Se llama al método "cargaProductoFirebase" para agregar el producto a Firebase
                cargaProductoFirebase(estado,nombre,caloria,precio,disponibilidad,ingredientes);
                break;
        }
    }

    private void cargaProductoFirebase(String estado,String nombre, String caloria,
                                       float precio, String disponibilidad, String ingredientes) {
        //Se obtiene la instancia de FirebaseAuth y DatabaseReference
        mAuth= FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference();
        //Se crea un mapa de valores para el nuevo producto
        Map<String,Object> producto = new HashMap<>();
        producto.put("estado",estado);
        producto.put("nombre",nombre);
        producto.put("caloria",caloria);
        producto.put("precio",precio);
        producto.put("disponibilidad",disponibilidad);
        producto.put("ingredientes",ingredientes);
        //Se actualiza la referencia del producto en la base de datos de Firebase con los valores del nuevo producto
        mDatabase.child("Usuarios").child(mAuth.getCurrentUser().getUid()).child("producto").updateChildren(producto).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //Si se completa la actualización del producto, se muestra un mensaje de confirmación
                Toast.makeText(mContext, "Producto agregado", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Si ocurre un error en la actualización del producto, se muestra un mensaje de error
                Toast.makeText(mContext, "Error al agregar producto", Toast.LENGTH_SHORT).show();
            }

        }) ;
    }


}
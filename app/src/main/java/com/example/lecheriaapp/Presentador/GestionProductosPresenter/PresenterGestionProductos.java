package com.example.lecheriaapp.Presentador.GestionProductosPresenter;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.lecheriaapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

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

        Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.agregarproducto);
        mNombre = dialog.findViewById(R.id.textNombreProducto);
         mCaloria = dialog.findViewById(R.id.textCalorias);
         mPrecio = dialog.findViewById(R.id.textPrecio);
         mDisponibilidad = dialog.findViewById(R.id.spinnerSede);
        mIngredientes = dialog.findViewById(R.id.spinnerIngredientes);
        mEstado = dialog.findViewById(R.id.spinnerEstado);
        Button mAgregar = dialog.findViewById(R.id.btnAgregar);
        mAgregar.setOnClickListener(this);
        dialog.show();
    }

    @Override
    public void onClick(View view) {
    switch (view.getId()){
        case R.id.btnAgregar:
            String nombre = mNombre.getText().toString();
            String caloria = mCaloria.getText().toString();
            float precio = Float.parseFloat(mPrecio.getText().toString());
            String disponibilidad = mDisponibilidad.getText().toString();
            String ingredientes = mIngredientes.getText().toString();
            String estado = mEstado.getText().toString();

            cargaProductoFirebase(nombre,caloria,precio,disponibilidad,ingredientes,estado);
            break;
    }
    }

    private void cargaProductoFirebase(String nombre, String caloria, float precio, String disponibilidad, String ingredientes,String estado) {
        Map<String,Object> producto = new HashMap<>();
        producto.put("nombre",nombre);
        producto.put("caloria",caloria);
        producto.put("precio",precio);
        producto.put("disponibilidad",disponibilidad);
        producto.put("ingredientes",ingredientes);
        producto.put("estado",estado);


        mDatabase.child("Usuarios").child(mAuth.getCurrentUser().getUid()).child("Productos").updateChildren(producto).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                dialog.dismiss();
            }
        }) ;
    }


}

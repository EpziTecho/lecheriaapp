package com.example.lecheriaapp.Presentador.GestionProductosPresenter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lecheriaapp.Adaptadores.RecyclerProductoGestionAdapter;
import com.example.lecheriaapp.Modelo.ProductoModel;
import com.example.lecheriaapp.R;
import com.example.lecheriaapp.Vista.GestionProductosView.GestionProductosFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PresenterGestionProductos implements View.OnClickListener {

    private static int PICK_IMAGE_REQUEST = 1;
    private Context mContext;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private RecyclerProductoGestionAdapter adapter;

    public PresenterGestionProductos(Context mContext, DatabaseReference mDatabase, FirebaseAuth mAuth) {
        this.mContext = mContext;
        this.mDatabase = mDatabase;
        this.mAuth = mAuth;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

        }
    }
    public void eliminarProductoFirebase(int posicion) {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Obtenemos la referencia del producto a actualizar
        DatabaseReference productoRef = mDatabase.child("Usuarios")
                .child(mAuth.getCurrentUser().getUid())
                .child("productos")
                .child("producto" + getFormattedNumber(posicion));

        // Creamos un mapa de valores para actualizar el estado del producto a "ELIMINADO"
        Map<String, Object> producto = new HashMap<>();
        producto.put("estado", "ELIMINADO");

        // Actualizamos la referencia del producto en la base de datos de Firebase con el nuevo estado
        productoRef.updateChildren(producto)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Si se completa la actualización del producto, se muestra un mensaje de confirmación
                        Toast.makeText(mContext, "Producto eliminado", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Si ocurre un error en la actualización del producto, se muestra un mensaje de error
                        Toast.makeText(mContext, "Error al eliminar producto", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String getFormattedNumber(int number) {
        DecimalFormat decimalFormat = new DecimalFormat("00");
        return decimalFormat.format(number);
    }

    public void cargarRecyclerViewGestion(RecyclerView recyclerView ){
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            mDatabase = FirebaseDatabase.getInstance().getReference();

            mDatabase.child("Usuarios").child(user.getUid()).child("productos").addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) { //Se ejecuta cada vez que se cambia algo en la base de datos
                    ArrayList<ProductoModel> arrayListProductos = new ArrayList<>();
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        ProductoModel productoModel = new ProductoModel();
                        productoModel.setNombre(snapshot.child("nombre").getValue(String.class));
                        productoModel.setEstado(snapshot.child("estado").getValue(String.class));
                        productoModel.setPrecio(snapshot.child("precio").getValue(String.class));
                        productoModel.setCaloria(snapshot.child("caloria").getValue(String.class));
                        productoModel.setDisponibilidad(snapshot.child("disponibilidad").getValue(String.class));
                        productoModel.setIngredientes(snapshot.child("ingredientes").getValue(String.class));
                        productoModel.setCategoria(snapshot.child("categoria").getValue(String.class));
                        arrayListProductos.add(productoModel);
                    }
                    adapter = new RecyclerProductoGestionAdapter(mContext, R.layout.producto_row_gestion, arrayListProductos);
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}

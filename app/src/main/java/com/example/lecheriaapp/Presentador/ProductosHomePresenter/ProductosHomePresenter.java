package com.example.lecheriaapp.Presentador.ProductosHomePresenter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lecheriaapp.Adaptadores.RecyclerProductoAdapter;
import com.example.lecheriaapp.Modelo.ProductoModel;
import com.example.lecheriaapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductosHomePresenter {
    private Context mContext;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private RecyclerProductoAdapter adapter;


    public ProductosHomePresenter(Context mContext, FirebaseAuth mAuth, DatabaseReference mDatabase) {
        this.mContext = mContext;
        this.mAuth = mAuth;
        this.mDatabase = mDatabase;

    }

    public void cargarRecyclerView(RecyclerView recyclerView){
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
                        productoModel.setPrecio(String.valueOf(snapshot.child("precio").getValue(Float.class)));
                        productoModel.setCalorias(snapshot.child("calorias").getValue(String.class));
                        productoModel.setIngredientes(snapshot.child("ingredientes").getValue(String.class));
                        arrayListProductos.add(productoModel);
                    }

                    adapter = new RecyclerProductoAdapter(mContext, R.layout.producto_row, arrayListProductos);
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }



}

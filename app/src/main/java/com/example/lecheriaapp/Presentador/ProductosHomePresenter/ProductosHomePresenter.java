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
import com.google.firebase.database.Query;
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

    public void cargarRecyclerView(RecyclerView recyclerView) {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            mDatabase = FirebaseDatabase.getInstance().getReference();

            mDatabase.child("Usuarios").child(user.getUid()).child("productos").addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ArrayList<ProductoModel> arrayListProductos = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String estado = snapshot.child("estado").getValue(String.class);
                        if (!estado.equalsIgnoreCase("ELIMINADO") && !estado.equalsIgnoreCase("eliminado")) {
                            ProductoModel productoModel = new ProductoModel();
                            productoModel.setNombre(snapshot.child("nombre").getValue(String.class));
                            productoModel.setEstado(estado);
                            productoModel.setPrecio(String.valueOf(snapshot.child("precio").getValue(Float.class)));
                            productoModel.setIngredientes(snapshot.child("ingredientes").getValue(String.class));
                            productoModel.setDisponibilidad(snapshot.child("disponibilidad").getValue(String.class));
                            productoModel.setCaloria(snapshot.child("caloria").getValue(String.class));
                            arrayListProductos.add(productoModel);
                        }
                    }

                    adapter = new RecyclerProductoAdapter(mContext, R.layout.producto_row, arrayListProductos);
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            // No se ha iniciado sesión, cargar productos de usuarios con el rol "admin" y "adminSMP"
            DatabaseReference usuariosRef = FirebaseDatabase.getInstance().getReference().child("Usuarios");
            Query query = usuariosRef.orderByChild("rol").startAt("admin").endAt("adminSMP");

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<ProductoModel> arrayListProductos = new ArrayList<>();
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String uid = userSnapshot.getKey();
                        DatabaseReference productosRef = usuariosRef.child(uid).child("productos");
                        productosRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    String estado = snapshot.child("estado").getValue(String.class);
                                    if (!estado.equalsIgnoreCase("ELIMINADO") && !estado.equalsIgnoreCase("eliminado")) {
                                        ProductoModel productoModel = new ProductoModel();
                                        productoModel.setNombre(snapshot.child("nombre").getValue(String.class));
                                        productoModel.setEstado(estado);
                                        productoModel.setPrecio(String.valueOf(snapshot.child("precio").getValue(Float.class)));
                                        productoModel.setIngredientes(snapshot.child("ingredientes").getValue(String.class));
                                        productoModel.setDisponibilidad(snapshot.child("disponibilidad").getValue(String.class));
                                        productoModel.setCaloria(snapshot.child("caloria").getValue(String.class));
                                        arrayListProductos.add(productoModel);
                                    }
                                }
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Manejar error de lectura de la base de datos
                            }
                        });
                    }
                    adapter = new RecyclerProductoAdapter(mContext, R.layout.producto_row, arrayListProductos);
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Manejar error de lectura de la base de datos
                }
            });
        }
    }
}

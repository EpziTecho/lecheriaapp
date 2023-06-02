package com.example.lecheriaapp.Presentador.GestionProductosPresenter;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lecheriaapp.Adaptadores.RecyclerProductoGestionAdapter;
import com.example.lecheriaapp.Modelo.ProductoModel;
import com.example.lecheriaapp.R;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PresenterGestionProductos implements View.OnClickListener {

    private Context mContext;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private Dialog dialog;
    private RecyclerView recyclerView;
    private RecyclerProductoGestionAdapter adapter;
    private EditText mNombre, mCaloria, mPrecio, mDisponibilidad, mIngredientes, mEstado;

    public PresenterGestionProductos(Context mContext, DatabaseReference mDatabase, FirebaseAuth mAuth) {
        this.mContext = mContext;
        this.mDatabase = mDatabase;
        this.mAuth = mAuth;
    }

    public void agregarProducto() {
        //Se crea un diálogo para agregar un nuevo producto
        dialog = new Dialog(mContext);
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
        Button mCancelButton = dialog.findViewById(R.id.btnCancelar);
        mCancelButton.setOnClickListener(this);
        // Obtener el objeto de ventana del diálogo y ajustar su ancho y alto
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        //Se muestra el diálogo en la pantalla
        dialog.show();
    }

    //Cuando le demos click en cancelar, se cierra el dialogo
    public void cancelar() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
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
            case R.id.btnCancelar:
                cancelar();
                break;
        }
    }

    private void cargaProductoFirebase(String estado, String nombre, String caloria,
                                       float precio, String disponibilidad, String ingredientes) {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Primero obtenemos el número del último producto creado
        mDatabase.child("Usuarios").child(mAuth.getCurrentUser().getUid()).child("ultimoProducto").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();
                    int ultimoProducto = 0;
                    if (snapshot.exists()) {
                        ultimoProducto = Integer.parseInt(snapshot.getValue().toString());
                    }

                    // Creamos un mapa de valores para el nuevo producto
                    Map<String, Object> producto = new HashMap<>();
                    producto.put("estado", estado);
                    producto.put("nombre", nombre);
                    producto.put("caloria", caloria);
                    producto.put("precio", precio);
                    producto.put("disponibilidad", disponibilidad);
                    producto.put("ingredientes", ingredientes);

                    // Actualizamos la referencia del producto en la base de datos de Firebase con los valores del nuevo producto
                    mDatabase.child("Usuarios").child(mAuth.getCurrentUser().getUid()).child("productos").child("producto" + (ultimoProducto + 1)).setValue(producto).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // Si se completa la actualización del producto, se muestra un mensaje de confirmación
                            Toast.makeText(mContext, "Producto agregado", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Si ocurre un error en la actualización del producto, se muestra un mensaje de error
                            Toast.makeText(mContext, "Error al agregar producto", Toast.LENGTH_SHORT).show();
                        }
                    });

                    // Actualizamos el número del último producto creado
                    mDatabase.child("Usuarios").child(mAuth.getCurrentUser().getUid()).child("ultimoProducto").setValue(ultimoProducto + 1);
                } else {
                    Toast.makeText(mContext, "Error al obtener el último producto", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
                        productoModel.setPrecio(String.valueOf(snapshot.child("precio").getValue(Float.class)));
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

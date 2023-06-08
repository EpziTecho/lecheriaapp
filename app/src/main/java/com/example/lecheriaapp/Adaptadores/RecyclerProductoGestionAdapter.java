package com.example.lecheriaapp.Adaptadores;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lecheriaapp.Modelo.ProductoModel;
import com.example.lecheriaapp.Presentador.GestionProductosPresenter.PresenterGestionProductos;
import com.example.lecheriaapp.R;
import com.example.lecheriaapp.Vista.ProductoView.DetallesProductoFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class RecyclerProductoGestionAdapter extends RecyclerView.Adapter<RecyclerProductoGestionAdapter.ProductoViewGestionHolder> {
    private Context mcontext;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private int layoutResource;
    private ArrayList<ProductoModel> arrayListProductos;
    private PresenterGestionProductos presenterGestionProductos;

    public RecyclerProductoGestionAdapter(Context mcontext, int layoutResource, ArrayList<ProductoModel> arrayListProductos) {
        this.mcontext = mcontext;
        this.layoutResource = layoutResource;
        this.arrayListProductos = arrayListProductos;
        // Inicializa el presentador aquí
        presenterGestionProductos = new PresenterGestionProductos(mcontext, databaseReference, firebaseAuth);
    }


    @NonNull
    @Override
    public RecyclerProductoGestionAdapter.ProductoViewGestionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(layoutResource,parent,false);

        return new RecyclerProductoGestionAdapter.ProductoViewGestionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerProductoGestionAdapter.ProductoViewGestionHolder holder, @SuppressLint("RecyclerView") int position) {
        ProductoModel productoModel = arrayListProductos.get(position);
        holder.mNombreProducto.setText(productoModel.getNombre());
        holder.mPrecioProducto.setText("S/. "+productoModel.getPrecio());
        holder.mEstadoProducto.setText(productoModel.getEstado());
        holder.mImagenProducto.setImageResource(R.drawable.ic_launcher_background);

        // Establecer clic del botón "Editar"
        holder.btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Acción al hacer clic en el botón "Editar"
                // Puedes llamar a un método en tu presentador para manejar la acción
                // por ejemplo: presenterGestionProductos.editarProducto(productoModel);
                presenterGestionProductos.editarProducto(productoModel.getNombre(), productoModel.getCalorias(), Float.parseFloat(productoModel.getPrecio()), productoModel.getDisponibilidad(), productoModel.getIngredientes(), productoModel.getEstado(), position);
            }
        });

        // Establecer clic del botón "Eliminar"
        holder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Acción al hacer clic en el botón "Eliminar"
                // Puedes llamar a un método en tu presentador para manejar la acción
                // por ejemplo: presenterGestionProductos.eliminarProducto(productoModel);
                //Toast.makeText(mcontext, "Eliminar", Toast.LENGTH_SHORT).show();
                presenterGestionProductos.eliminarProductoFirebase(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(arrayListProductos != null && arrayListProductos.size() > 0) {
            return arrayListProductos.size();
        } else {
            return 0;
        }
    }

    public class ProductoViewGestionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mNombreProducto, mPrecioProducto, mEstadoProducto, mDescripcionProducto;
        ImageView mImagenProducto;
        Button btnEditar, btnEliminar;

        public ProductoViewGestionHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mNombreProducto = itemView.findViewById(R.id.nombreProductoRow);
            mPrecioProducto = itemView.findViewById(R.id.precioProductoRow);
            mEstadoProducto = itemView.findViewById(R.id.estadoProductoRow);
            mImagenProducto = itemView.findViewById(R.id.imagenProductoRow);
            btnEditar = itemView.findViewById(R.id.btnEditarProducto);
            btnEliminar = itemView.findViewById(R.id.btnEliminarProducto);
        }

        @Override
        public void onClick(View view) {
            // Acción al hacer clic en el elemento de la lista
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                ProductoModel productoModel = arrayListProductos.get(position);
                // Aquí puedes abrir un fragmento o actividad para mostrar los detalles del producto
                // por ejemplo: mostrarDetallesProducto(productoModel);
            }
        }
    }
}

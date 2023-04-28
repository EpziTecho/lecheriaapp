package com.example.lecheriaapp.Productos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lecheriaapp.R;

import java.util.List;

public class ProductosAdapter extends RecyclerView.Adapter<ProductosAdapter.ProductosViewHolder> {

    private Context context;
    private List<Productos> listaProductos;

    public ProductosAdapter(Context context, List<Productos> listaProductos) {
        this.context = context;
        this.listaProductos = listaProductos;
    }

    @NonNull
    @Override
    public ProductosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_producto, parent, false);
        return new ProductosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductosViewHolder holder, int position) {
        Productos producto = listaProductos.get(position);

        holder.nombre.setText(producto.getNombre());
        holder.descripcion.setText(producto.getDescripcion());
        holder.precio.setText(producto.getPrecio());

        // Cargar imagen con Glide, Picasso u otra librería similar
        Glide.with(context).load(producto.getImagen()).into(holder.imagen);
    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    public static class ProductosViewHolder extends RecyclerView.ViewHolder {

        public ImageView imagen;
        public TextView nombre;
        public TextView descripcion;
        public TextView precio;

        public ProductosViewHolder(@NonNull View itemView) {
            super(itemView);

            imagen = itemView.findViewById(R.id.producto_imagen);
            nombre = itemView.findViewById(R.id.producto_nombre);
            descripcion = itemView.findViewById(R.id.producto_descripcion);
            precio = itemView.findViewById(R.id.producto_precio);
        }
    }
}


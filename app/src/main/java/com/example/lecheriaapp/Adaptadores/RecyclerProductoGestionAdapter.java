package com.example.lecheriaapp.Adaptadores;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lecheriaapp.Modelo.ProductoModel;
import com.example.lecheriaapp.R;
import com.example.lecheriaapp.Vista.ProductoView.DetallesProductoFragment;

import java.util.ArrayList;

public class RecyclerProductoGestionAdapter extends RecyclerView.Adapter<RecyclerProductoGestionAdapter.ProductoViewGestionHolder> {
    private Context mcontext;
    private int layoutResource;
    private ArrayList<ProductoModel> arrayListProductos;

    public RecyclerProductoGestionAdapter(Context mcontext, int layoutResource, ArrayList<ProductoModel> arrayListProductos) {
        this.mcontext = mcontext;
        this.layoutResource = layoutResource;
        this.arrayListProductos = arrayListProductos;
    }


    @NonNull
    @Override
    public RecyclerProductoGestionAdapter.ProductoViewGestionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(layoutResource,parent,false);

        return new RecyclerProductoGestionAdapter.ProductoViewGestionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerProductoGestionAdapter.ProductoViewGestionHolder holder, int position) {
        ProductoModel productoModel = arrayListProductos.get(position);
        holder.mNombreProducto.setText(productoModel.getNombre());
        holder.mPrecioProducto.setText("S/. "+productoModel.getPrecio());
        holder.mEstadoProducto.setText(productoModel.getEstado());
        //holder.mDescripcionProducto.setText(productoModel.getCalorias());
        /*holder.mImagenProducto.setImageResource(productoModel.getImagen());*/
        holder.mImagenProducto.setImageResource(R.drawable.ic_launcher_background);
    }

    @Override
    public int getItemCount() {
        if(arrayListProductos != null && arrayListProductos.size() > 0) {
            return arrayListProductos.size();
        } else {
            return 0;
        }
    }

    public class ProductoViewGestionHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        TextView mNombreProducto, mPrecioProducto, mEstadoProducto, mDescripcionProducto;
        ImageView mImagenProducto;

        public ProductoViewGestionHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mNombreProducto=itemView.findViewById(R.id.nombreProductoRow);
            mPrecioProducto=itemView.findViewById(R.id.precioProductoRow);
            mEstadoProducto=itemView.findViewById(R.id.estadoProductoRow);
            mImagenProducto=itemView.findViewById(R.id.imagenProductoRow);

        }

        @Override
        public void onClick(View view) {

        }
    }

}



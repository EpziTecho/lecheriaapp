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

public class RecyclerProductoAdapter extends RecyclerView.Adapter<RecyclerProductoAdapter.ProductoViewHolder>{
    private Context mcontext;
    private int layoutResource;
    private ArrayList<ProductoModel> arrayListProductos;

    public RecyclerProductoAdapter(Context mcontext, int layoutResource, ArrayList<ProductoModel> arrayListProductos) {
        this.mcontext = mcontext;
        this.layoutResource = layoutResource;
        this.arrayListProductos = arrayListProductos;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mcontext).inflate(layoutResource,parent,false);

        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) { //Aqui se asignan los valores a los elementos del recycler view

            ProductoModel productoModel = arrayListProductos.get(position);
            holder.mNombreProducto.setText(productoModel.getNombre());
            holder.mPrecioProducto.setText("S/. "+productoModel.getPrecio());
            holder.mEstadoProducto.setText(productoModel.getEstado());
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

    public class ProductoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mNombreProducto,mPrecioProducto,mEstadoProducto;
        ImageView mImagenProducto;

        public ProductoViewHolder(@NonNull View itemView) { //Aqui se enlazan los elementos del recycler view con los del layout
            super(itemView);
            itemView.setOnClickListener(this);
            mNombreProducto=itemView.findViewById(R.id.nombreProductoRow);
            mPrecioProducto=itemView.findViewById(R.id.precioProductoRow);
            mEstadoProducto=itemView.findViewById(R.id.estadoProductoRow);
            mImagenProducto=itemView.findViewById(R.id.imagenProductoRow);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            ProductoModel productoModel = arrayListProductos.get(position);
            Bundle args = new Bundle();
            args.putString("nombre", productoModel.getNombre());
            args.putString("precio", productoModel.getPrecio());
            args.putString("estado", productoModel.getEstado());
            args.putString("caloria", productoModel.getCaloria());
            args.putString("ingredientes", productoModel.getIngredientes());
            args.putString("disponibilidad", productoModel.getDisponibilidad());
            args.putString("imagen", productoModel.getImagen());
            DetallesProductoFragment detallesProductoFragment = new DetallesProductoFragment();
            detallesProductoFragment.setArguments(args);
            FragmentManager fragmentManager = ((FragmentActivity) mcontext).getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, detallesProductoFragment)
                    .addToBackStack(null)
                    .commit();
        }
        }

    }


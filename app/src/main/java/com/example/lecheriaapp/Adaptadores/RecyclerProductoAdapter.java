package com.example.lecheriaapp.Adaptadores;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {

            ProductoModel productoModel = arrayListProductos.get(position);
            holder.mNombreProducto.setText(productoModel.getNombre());
            holder.mPrecioProducto.setText(productoModel.getPrecio());
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

    public class ProductoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mNombreProducto,mPrecioProducto,mDescripcionProducto;
        ImageView mImagenProducto;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            mNombreProducto=itemView.findViewById(R.id.nombreProductoRow);
            mPrecioProducto=itemView.findViewById(R.id.precioProductoRow);
            //mDescripcionProducto=itemView.findViewById(R.id.descripcionProductoRow);
            mImagenProducto=itemView.findViewById(R.id.imagenProductoRow);

        }

        @Override
        public void onClick(View view) {

            Intent intent = new Intent(mcontext, DetallesProductoFragment.class);
            mcontext.startActivity(intent);

        }
    }
}

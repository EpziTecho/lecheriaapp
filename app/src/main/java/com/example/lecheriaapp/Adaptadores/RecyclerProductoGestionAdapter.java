package com.example.lecheriaapp.Adaptadores;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lecheriaapp.Modelo.ProductoModel;
import com.example.lecheriaapp.Presentador.GestionProductosPresenter.PresentadorEditarProductos;
import com.example.lecheriaapp.Presentador.GestionProductosPresenter.PresenterGestionProductos;
import com.example.lecheriaapp.R;
import com.example.lecheriaapp.Vista.GestionProductosView.EditarProductosFragment;
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
    private PresentadorEditarProductos presentadorEditarProductos;

    public RecyclerProductoGestionAdapter(Context mcontext, int layoutResource, ArrayList<ProductoModel> arrayListProductos) {
        this.mcontext = mcontext;
        this.layoutResource = layoutResource;
        this.arrayListProductos = arrayListProductos;
        // Inicializa el presentador aquí
        presenterGestionProductos = new PresenterGestionProductos(mcontext, databaseReference, firebaseAuth);
        presentadorEditarProductos = new PresentadorEditarProductos(mcontext);
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
        holder.mPrecioProducto.setText(productoModel.getPrecio());
        holder.mEstadoProducto.setText(productoModel.getEstado());
        // Cargar la imagen utilizando Glide
        Glide.with(mcontext)
                .load(productoModel.getImageUrl())  // Aquí deberías proporcionar la URL o el archivo local de la imagen
                .placeholder(R.drawable.baseline_table_restaurant_24)  // Imagen de carga por defecto
                .error(R.drawable.baseline_book_24)  // Imagen de error si no se puede cargar la imagen
                .into(holder.mImagenProducto);

        // Establecer clic del botón "Editar"
        holder.btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Abrir el fragmento EditarProductosFragment con los datos del producto
                String nombre = productoModel.getNombre();
                String calorias = productoModel.getCaloria();
                String precio = productoModel.getPrecio();
                String disponibilidad = productoModel.getDisponibilidad();
                String categoria = productoModel.getCategoria();
                String ingredientes = productoModel.getIngredientes();
                String estado = productoModel.getEstado();
                String imagen = productoModel.getImageUrl();

                Fragment editarFragment = EditarProductosFragment.newInstance(nombre, calorias, precio, disponibilidad, categoria, ingredientes, estado, imagen,position);
                FragmentManager fragmentManager = ((AppCompatActivity) mcontext).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, editarFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
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

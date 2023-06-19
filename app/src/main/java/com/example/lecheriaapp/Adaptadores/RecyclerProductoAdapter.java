package com.example.lecheriaapp.Adaptadores;

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
import com.example.lecheriaapp.R;
import com.example.lecheriaapp.Vista.LoginView.LoginFragment;
import com.example.lecheriaapp.Vista.ProductoView.DetallesProductoFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecyclerProductoAdapter extends RecyclerView.Adapter<RecyclerProductoAdapter.ProductoViewHolder> {
    private Context mcontext;
    private int layoutResource;
    private ArrayList<ProductoModel> arrayListProductos;
    private DatabaseReference mDatabase;
    private FavoritosUpdateListener favoritosUpdateListener;

    public RecyclerProductoAdapter(Context mcontext, int layoutResource, ArrayList<ProductoModel> arrayListProductos) {
        this.mcontext = mcontext;
        this.layoutResource = layoutResource;
        this.arrayListProductos = arrayListProductos;
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void setFavoritosUpdateListener(FavoritosUpdateListener listener) {
        favoritosUpdateListener = listener;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(layoutResource, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        ProductoModel productoModel = arrayListProductos.get(position);
        holder.mNombreProducto.setText(productoModel.getNombre());
        holder.mPrecioProducto.setText("S/. " + productoModel.getPrecio());
        holder.mEstadoProducto.setText(productoModel.getEstado());
        holder.mImagenProducto.setImageResource(R.drawable.ic_launcher_background);

        // Establecer clic en el botón de favoritos
        holder.mBotonFavoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Agregar producto a favoritos
                agregarAFavoritos(productoModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayListProductos.size();
    }

    public class ProductoViewHolder extends RecyclerView.ViewHolder {
        TextView mNombreProducto, mPrecioProducto, mEstadoProducto;
        ImageView mImagenProducto;
        Button mBotonFavoritos;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            mNombreProducto = itemView.findViewById(R.id.nombreProductoRow);
            mPrecioProducto = itemView.findViewById(R.id.precioProductoRow);
            mEstadoProducto = itemView.findViewById(R.id.estadoProductoRow);
            mImagenProducto = itemView.findViewById(R.id.imagenProductoRow);
            mBotonFavoritos = itemView.findViewById(R.id.btn_favorito);

            // Establecer clic en todo el elemento
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    ProductoModel productoModel = arrayListProductos.get(position);

                    // Abrir fragmento de detalles del producto
                    Bundle args = new Bundle();
                    args.putString("nombre", productoModel.getNombre());
                    args.putString("precio", productoModel.getPrecio());
                    args.putString("estado", productoModel.getEstado());
                    args.putString("caloria", productoModel.getCaloria());
                    args.putString("ingredientes", productoModel.getIngredientes());
                    args.putString("disponibilidad", productoModel.getDisponibilidad());
                    args.putString("categoria", productoModel.getCategoria());
                    args.putString("imagen", productoModel.getImagen());
                    DetallesProductoFragment detallesProductoFragment = new DetallesProductoFragment();
                    detallesProductoFragment.setArguments(args);
                    FragmentManager fragmentManager = ((FragmentActivity) mcontext).getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, detallesProductoFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });
        }
    }

    private void agregarAFavoritos(ProductoModel producto) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Usuario con sesión iniciada, agregar o eliminar el producto de favoritos
            String clienteId = user.getUid();
            DatabaseReference usuarioRef = mDatabase.child("Usuarios").child(clienteId);
            DatabaseReference favoritosRef = usuarioRef.child("Favoritos");
            favoritosRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    boolean productoExiste = false;
                    for (DataSnapshot productoSnapshot : dataSnapshot.getChildren()) {
                        ProductoModel productoFavorito = productoSnapshot.getValue(ProductoModel.class);
                        if (productoFavorito != null && productoFavorito.getNombre().equals(producto.getNombre())) {
                            productoExiste = true;
                            productoSnapshot.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(mcontext, "Producto eliminado de favoritos", Toast.LENGTH_SHORT).show();
                                        if (favoritosUpdateListener != null) {
                                            favoritosUpdateListener.onFavoritosUpdated();
                                        }
                                    } else {
                                        Toast.makeText(mcontext, "Error al eliminar el producto de favoritos", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            break;
                        }
                    }

                    if (!productoExiste) {
                        favoritosRef.child(producto.getNombre()).setValue(producto).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(mcontext, "Producto agregado a favoritos", Toast.LENGTH_SHORT).show();
                                    if (favoritosUpdateListener != null) {
                                        favoritosUpdateListener.onFavoritosUpdated();
                                    }
                                } else {
                                    Toast.makeText(mcontext, "Error al agregar el producto a favoritos", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Manejar error de lectura/escritura de la base de datos
                }
            });
        } else {
            // No hay sesión iniciada, redirigir al fragmento de inicio de sesión
            Toast.makeText(mcontext, "Inicia sesión para agregar productos a favoritos", Toast.LENGTH_SHORT).show();
            FragmentManager fragmentManager = ((FragmentActivity) mcontext).getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, new LoginFragment())
                    .addToBackStack(null)
                    .commit();
        }
    }


    public interface FavoritosUpdateListener {
        void onFavoritosUpdated();
    }
}

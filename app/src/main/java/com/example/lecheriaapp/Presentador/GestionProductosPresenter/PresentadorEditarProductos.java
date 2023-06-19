package com.example.lecheriaapp.Presentador.GestionProductosPresenter;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.lecheriaapp.R;
import com.example.lecheriaapp.Vista.GestionProductosView.GestionProductosFragment;
import com.example.lecheriaapp.Vista.GestionProductosView.EditarProductosFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.Map;

public class PresentadorEditarProductos {
    private Context mContext;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    public PresentadorEditarProductos(Context context) {
        mContext = context;
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    private void actualizarProductoFirebase(Map<String, Object> producto, int position, Fragment fragment) {
        String userId = mAuth.getCurrentUser().getUid();

        // Actualizamos los valores del producto en la base de datos de Firebase
        mDatabase.child("Usuarios").child(userId).child("productos").child("producto" + getFormattedNumber(position)).updateChildren(producto)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Si se completa la actualización del producto, se muestra un mensaje de confirmación
                        Toast.makeText(mContext, "Producto actualizado", Toast.LENGTH_SHORT).show();
                        FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
                        Fragment gestionProductosFragment = new GestionProductosFragment();
                        fragmentManager.beginTransaction().replace(R.id.fragment_container, gestionProductosFragment).commit();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Si ocurre un error en la actualización del producto, se muestra un mensaje de error
                        Toast.makeText(mContext, "Error al actualizar producto", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void editarProducto(Map<String, Object> producto, int position, Fragment fragment) {
        // Llamar al método de actualización del producto
        actualizarProductoFirebase(producto, position, fragment);
    }

    private String getFormattedNumber(int number) {
        DecimalFormat decimalFormat = new DecimalFormat("00");
        return decimalFormat.format(number);
    }
}

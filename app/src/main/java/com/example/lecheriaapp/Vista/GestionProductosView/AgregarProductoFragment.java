package com.example.lecheriaapp.Vista.GestionProductosView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.lecheriaapp.Presentador.GestionProductosPresenter.PresentadorAgregarProductos;
import com.example.lecheriaapp.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class AgregarProductoFragment extends Fragment implements View.OnClickListener {

    private EditText mTextNombreProducto, mTextCalorias, mTextPrecio, mTextIngredientes;
    private Button mBtnAgregar, mBtnCancelar, mBtnSeleccionarImagen;
    private ImageView mSelectedImageView;
    private Spinner mSpinnerEstado, mSpinnerDisponibilidad, mSpinnerCategoria;
    private ArrayAdapter<String> mEstadoAdapter, mDisponibilidadAdapter, mCategoriaAdapter;
    private static final int GALLERY_REQUEST_CODE = 123;
    private Uri imageUri;
    private PresentadorAgregarProductos mPresentador;
    private StorageReference mStorageRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.agregarproducto, container, false);

        mTextNombreProducto = view.findViewById(R.id.textNombreProducto);
        mTextCalorias = view.findViewById(R.id.textCalorias);
        mTextPrecio = view.findViewById(R.id.textPrecio);
        mTextIngredientes = view.findViewById(R.id.textIngredientes);
        mBtnAgregar = view.findViewById(R.id.btnAgregar);
        mBtnCancelar = view.findViewById(R.id.btnCancelar);
        mBtnSeleccionarImagen = view.findViewById(R.id.selectImageButton);
        mSelectedImageView = view.findViewById(R.id.selectedImageView);
        mSpinnerEstado = view.findViewById(R.id.spinnerEstado);
        mSpinnerDisponibilidad = view.findViewById(R.id.spinnerDisponibilidad);
        mSpinnerCategoria = view.findViewById(R.id.spinnerCategoria);

        // Configurar adaptador para el spinner de estado
        Resources res = getResources();
        String[] estados = res.getStringArray(R.array.left_spinner_estado);
        mEstadoAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, estados);
        mEstadoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerEstado.setAdapter(mEstadoAdapter);

        // Configurar adaptador para el spinner de disponibilidad
        String[] disponibilidad = res.getStringArray(R.array.left_spinner_sedes);
        mDisponibilidadAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, disponibilidad);
        mDisponibilidadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerDisponibilidad.setAdapter(mDisponibilidadAdapter);

        // Configurar adaptador para el spinner de categoría
        String[] categorias = res.getStringArray(R.array.right_spinner_items);
        mCategoriaAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categorias);
        mCategoriaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerCategoria.setAdapter(mCategoriaAdapter);

        mBtnAgregar.setOnClickListener(this);
        mBtnSeleccionarImagen.setOnClickListener(this);
        mBtnCancelar.setOnClickListener(this);
        mPresentador = new PresentadorAgregarProductos(requireContext());
        mStorageRef = FirebaseStorage.getInstance().getReference(); // Obtener referencia a Firebase Storage

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnAgregar) {
            // Crear un diálogo de progreso para mostrar mientras se realiza el registro del usuario


            String nombreProducto = mTextNombreProducto.getText().toString().trim();
            String calorias = mTextCalorias.getText().toString().trim();
            String precio = mTextPrecio.getText().toString().trim();
            String ingredientes = mTextIngredientes.getText().toString().trim();
            String estado = mSpinnerEstado.getSelectedItem().toString();
            String disponibilidad = mSpinnerDisponibilidad.getSelectedItem().toString();
            String categoria = mSpinnerCategoria.getSelectedItem().toString();

            if (!nombreProducto.isEmpty() && !calorias.isEmpty() && !ingredientes.isEmpty() && !estado.isEmpty() && !disponibilidad.isEmpty() && !categoria.isEmpty() && imageUri != null) {
                // Subir la imagen a Firebase Storage
                final ProgressDialog dialog = new ProgressDialog(requireContext());
                dialog.setMessage("Agregando producto...");
                dialog.setCancelable(false);
                dialog.show();
                StorageReference imageRef = mStorageRef.child("productos").child(imageUri.getLastPathSegment());
                UploadTask uploadTask = imageRef.putFile(imageUri);
                uploadTask.addOnSuccessListener(taskSnapshot -> {
                    // Obtener la URL de descarga de la imagen subida
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();

                        // Crear el mapa de datos para el producto
                        Map<String, Object> producto = new HashMap<>();
                        producto.put("caloria", calorias);
                        producto.put("categoria", imageUrl);
                        producto.put("disponibilidad", disponibilidad);
                        producto.put("estado", estado);
                        producto.put("ingredientes", ingredientes);
                        producto.put("nombre", nombreProducto);
                        producto.put("precio", precio);

                        // Llamar al presentador para agregar el producto
                        mPresentador.agregarProducto(
                                estado,
                                nombreProducto,
                                calorias,
                                precio,
                                disponibilidad,
                                categoria,
                                ingredientes,
                                imageUrl
                        );

                        // Mostrar mensaje de éxito
                        dialog.dismiss();
                       // Toast.makeText(getActivity(), "Producto agregado", Toast.LENGTH_SHORT).show();
                        // Ir al fragmento de gestión de productos
                        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.fragment_container, new GestionProductosFragment()).commit();
                    });
                }).addOnFailureListener(e -> {
                    // Mostrar mensaje de error en caso de falla en la carga de la imagen
                    Toast.makeText(getActivity(), "Error al subir la imagen", Toast.LENGTH_SHORT).show();
                });
            } else {
                Toast.makeText(getActivity(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            }
        } else if (view.getId() == R.id.selectImageButton) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, GALLERY_REQUEST_CODE);
        } else if (view.getId() == R.id.btnCancelar) {
            Toast.makeText(getActivity(), "Cancelado", Toast.LENGTH_SHORT).show();
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, new GestionProductosFragment()).commit();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == getActivity().RESULT_OK && data != null) {
            imageUri = data.getData();
            mSelectedImageView.setImageURI(imageUri);
        }
    }
}

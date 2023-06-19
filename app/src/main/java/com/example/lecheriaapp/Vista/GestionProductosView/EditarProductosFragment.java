package com.example.lecheriaapp.Vista.GestionProductosView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.lecheriaapp.Presentador.GestionProductosPresenter.PresentadorEditarProductos;
import com.example.lecheriaapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EditarProductosFragment extends Fragment implements View.OnClickListener {

    private EditText mTextNombreProducto, mTextCalorias, mTextPrecio, mTextIngredientes;
    private Button mBtnGuardar, mBtnCancelar, mBtnSeleccionarImagen;
    private PresentadorEditarProductos mPresentador;
    private int mPosicion;
    private Spinner mSpinnerEstado, mSpinnerDisponibilidad, mSpinnerCategoria;
    private DatabaseReference databaseReference;
    private ImageView mImageView;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    private StorageReference mStorageRef;

    private static final int REQUEST_PERMISSIONS = 1;

    public static EditarProductosFragment newInstance(String nombre, String calorias, String precio, String disponibilidad, String categoria, String ingredientes, String estado, int posicion) {
        EditarProductosFragment fragment = new EditarProductosFragment();
        Bundle args = new Bundle();
        args.putString("nombre", nombre);
        args.putString("caloria", calorias);
        args.putString("precio", precio);
        args.putString("disponibilidad", disponibilidad);
        args.putString("categoria", categoria);
        args.putString("ingredientes", ingredientes);
        args.putString("estado", estado);
        args.putInt("posicion", posicion);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.editarproducto, container, false);

        mTextNombreProducto = view.findViewById(R.id.textNombreProducto1);
        mTextCalorias = view.findViewById(R.id.textCalorias1);
        mTextPrecio = view.findViewById(R.id.textPrecio1);
        mTextIngredientes = view.findViewById(R.id.textIngredientes1);
        mBtnGuardar = view.findViewById(R.id.btnLayoutEditarProducto1);
        mBtnCancelar = view.findViewById(R.id.btnCancelar1);
        mSpinnerEstado = view.findViewById(R.id.spinnerEstado1);
        mSpinnerDisponibilidad = view.findViewById(R.id.spinnerDisponibilidad1);
        mSpinnerCategoria = view.findViewById(R.id.spinnerCategoria1);
        mImageView = view.findViewById(R.id.selectedImageView1);
        mBtnSeleccionarImagen = view.findViewById(R.id.selectImageButton1);

        mBtnGuardar.setOnClickListener(this);
        mBtnCancelar.setOnClickListener(this);
        mBtnSeleccionarImagen.setOnClickListener(this);

        mPresentador = new PresentadorEditarProductos(requireContext());

        // Obtener los datos del producto para mostrar en los campos de texto
        Bundle args = getArguments();
        if (args != null) {
            mTextNombreProducto.setText(args.getString("nombre"));
            mTextCalorias.setText(args.getString("caloria"));
            mTextPrecio.setText(String.valueOf(args.getString("precio")));
            mTextIngredientes.setText(args.getString("ingredientes"));
            mPosicion = args.getInt("posicion");

            // Establecer los valores de los spinners
            ArrayAdapter<CharSequence> estadoAdapter = ArrayAdapter.createFromResource(requireContext(),
                    R.array.left_spinner_estado, android.R.layout.simple_spinner_item);
            estadoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpinnerEstado.setAdapter(estadoAdapter);

            ArrayAdapter<CharSequence> disponibilidadAdapter = ArrayAdapter.createFromResource(requireContext(),
                    R.array.left_spinner_sedes, android.R.layout.simple_spinner_item);
            disponibilidadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpinnerDisponibilidad.setAdapter(disponibilidadAdapter);

            ArrayAdapter<CharSequence> categoriaAdapter = ArrayAdapter.createFromResource(requireContext(),
                    R.array.right_spinner_items, android.R.layout.simple_spinner_item);
            categoriaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpinnerCategoria.setAdapter(categoriaAdapter);

            // Establecer los valores seleccionados de los spinners
            String estado = args.getString("estado");
            String disponibilidad = args.getString("disponibilidad");
            String categoria = args.getString("categoria");
            if (estado != null) {
                int estadoPosition = estadoAdapter.getPosition(estado);
                mSpinnerEstado.setSelection(estadoPosition);
            }
            if (disponibilidad != null) {
                int disponibilidadPosition = disponibilidadAdapter.getPosition(disponibilidad);
                mSpinnerDisponibilidad.setSelection(disponibilidadPosition);
            }
            if (categoria != null) {
                int categoriaPosition = categoriaAdapter.getPosition(categoria);
                mSpinnerCategoria.setSelection(categoriaPosition);
            }
        }

        // Obtener la referencia al almacenamiento Firebase
        mStorageRef = FirebaseStorage.getInstance().getReference();

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnLayoutEditarProducto1) {
            // Obtener los valores de los campos de texto
            String nombreProducto = mTextNombreProducto.getText().toString().trim();
            String calorias = mTextCalorias.getText().toString().trim();
            String precio = mTextPrecio.getText().toString().trim();
            String ingredientes = mTextIngredientes.getText().toString().trim();
            String estado = mSpinnerEstado.getSelectedItem().toString();
            String disponibilidad = mSpinnerDisponibilidad.getSelectedItem().toString();
            String categoria = mSpinnerCategoria.getSelectedItem().toString();

            if (!nombreProducto.isEmpty() && !calorias.isEmpty() && !precio.isEmpty() && !ingredientes.isEmpty()) {
                // Crear un mapa para almacenar los datos del producto
                Map<String, Object> productoData = new HashMap<>();
                productoData.put("nombre", nombreProducto);
                productoData.put("caloria", calorias);
                productoData.put("precio", Float.parseFloat(precio));
                productoData.put("disponibilidad", disponibilidad);
                productoData.put("categoria", categoria);
                productoData.put("ingredientes", ingredientes);
                productoData.put("estado", estado);

                // Llamar al método del presentador para editar el producto
                mPresentador.editarProducto(productoData, mPosicion, this);

                // Mostrar un mensaje de éxito
                Toast.makeText(getActivity(), "Producto editado correctamente", Toast.LENGTH_SHORT).show();

                // Navegar de vuelta a la lista de productos
                requireActivity().onBackPressed();
            } else {
                Toast.makeText(getActivity(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            }
        } else if (view.getId() == R.id.btnCancelar1) {
            requireActivity().onBackPressed();
        } else if (view.getId() == R.id.selectImageButton1) {
            // Solicitar permisos de almacenamiento si no están concedidos
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSIONS);
            } else {
                // Abrir la galería para seleccionar una imagen
                openGallery();
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleccionar imagen"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), mImageUri);
                mImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, abrir la galería
                openGallery();
            } else {
                // Permiso denegado, mostrar un mensaje de error
                Toast.makeText(getActivity(), "Permiso denegado. No se puede abrir la galería", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

package com.example.lecheriaapp.Vista.GestionProductosView;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.lecheriaapp.Presentador.GestionProductosPresenter.PresenterGestionProductos;
import com.example.lecheriaapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;


public class AgregarProductoFragment extends Fragment implements  View.OnClickListener{

    private PresenterGestionProductos presenterGestionProductos;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private Button mAddButton;
    private Button mCancelButton;

    private Spinner leftSpinnerEstado;
    private Spinner leftSpinnerSede;
    private Spinner leftSpinnerCategoria;
    public AgregarProductoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.agregarproducto, container, false);

        presenterGestionProductos = new PresenterGestionProductos(getActivity(), mDatabase, mAuth);
        mAddButton = view.findViewById(R.id.btnAgregar);
        mAddButton.setOnClickListener(this);
        mCancelButton = view.findViewById(R.id.btnCancelar);
        mCancelButton.setOnClickListener(this);

        // Obtener la referencia a los Spinners del layout
        leftSpinnerEstado = view.findViewById(R.id.textEstado);
        leftSpinnerSede = view.findViewById(R.id.textDisponibilidad);
        leftSpinnerCategoria = view.findViewById(R.id.textCategoria);
        // Crear un ArrayAdapter para cada Spinner con los datos deseados
        ArrayAdapter<CharSequence> estadoAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.left_spinner_estado, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> sedeAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.left_spinner_items, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> categoriaAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.right_spinner_items, android.R.layout.simple_spinner_item);
        // Especificar el diseño del menú desplegable de los Spinners
        estadoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sedeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoriaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Establecer los ArrayAdapter como adaptador de cada Spinner
        leftSpinnerEstado.setAdapter(estadoAdapter);
        leftSpinnerSede.setAdapter(sedeAdapter);
        leftSpinnerCategoria.setAdapter(categoriaAdapter);



        return view;


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnAgregar:
                presenterGestionProductos.agregarProducto();
                break;

        }
    }
}
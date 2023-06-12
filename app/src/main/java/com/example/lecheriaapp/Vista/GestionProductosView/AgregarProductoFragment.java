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
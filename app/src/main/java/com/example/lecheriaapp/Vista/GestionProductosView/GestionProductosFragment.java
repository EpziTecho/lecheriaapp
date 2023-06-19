package com.example.lecheriaapp.Vista.GestionProductosView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lecheriaapp.Presentador.GestionProductosPresenter.PresenterGestionProductos;
import com.example.lecheriaapp.Presentador.ProductosHomePresenter.ProductosHomePresenter;
import com.example.lecheriaapp.R;
import com.example.lecheriaapp.Vista.RegistroView.RegistroFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;


public class GestionProductosFragment extends Fragment implements  View.OnClickListener{

    private Button mBtnAgregarProducto;
    private PresenterGestionProductos presenterGestionProductos;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private RecyclerView recyclerView;
    private ProductosHomePresenter productosHomePresenter;


    public GestionProductosFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gestionproductos, container, false);
        presenterGestionProductos = new PresenterGestionProductos(getActivity(), mDatabase, mAuth);
        mBtnAgregarProducto = view.findViewById(R.id.add_button);
        mBtnAgregarProducto.setOnClickListener(this);
        productosHomePresenter= new ProductosHomePresenter(getActivity(), mAuth, mDatabase);

        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecycler();
    }
    private void initRecycler() {
        RecyclerView recyclerView = getView().findViewById(R.id.product_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        presenterGestionProductos.cargarRecyclerViewGestion(recyclerView);
    }

    @Override
    public void onClick(View view) {

        /*Toast.makeText(getActivity(), "Agregando producto", Toast.LENGTH_SHORT).show();*/
        switch (view .getId()){
            case R.id.add_button:
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new AgregarProductoFragment()).commit();
                break;


        }

    }
}
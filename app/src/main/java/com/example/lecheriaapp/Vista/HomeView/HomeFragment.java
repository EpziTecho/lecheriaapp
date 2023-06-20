package com.example.lecheriaapp.Vista.HomeView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lecheriaapp.Modelo.ProductoModel;
import com.example.lecheriaapp.Presentador.ProductosHomePresenter.ProductosHomePresenter;
import com.example.lecheriaapp.Adaptadores.ProductosAdapter;
import com.example.lecheriaapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;


public class HomeFragment extends Fragment implements View.OnClickListener {

    private RecyclerView recyclerView;
    private ProductosAdapter adapter;
    private ArrayList<ProductoModel> productoModelList;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ProductosHomePresenter productosHomePresenter;

    private Spinner spinnerSedes;
    private Spinner rightSpinner;

    public HomeFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewHome);
        // Obtener la referencia a los Spinners del layout
        spinnerSedes = view.findViewById(R.id.leftSpinner);
        rightSpinner = view.findViewById(R.id.rightSpinner);
        // Crear un ArrayAdapter para cada Spinner con los datos deseados
        ArrayAdapter<CharSequence> leftAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.left_spinner_items, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> rightAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.right_spinner_items, android.R.layout.simple_spinner_item);
        // Especificar el diseño del menú desplegable de los Spinners
        leftAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Establecer los ArrayAdapter como adaptador de cada Spinner
        spinnerSedes.setAdapter(leftAdapter);
        rightSpinner.setAdapter(rightAdapter);
        productosHomePresenter = new ProductosHomePresenter(getActivity(), mAuth, mDatabase);

        // Agregar listener al spinner de Sedes
        spinnerSedes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String sede = parent.getItemAtPosition(position).toString();
                if (sede.equals("Todas las sedes")) {
                    initRecyclerTodos();
                } else if (sede.equals("Centro")) {
                    initRecyclerCentro();
                } else if (sede.equals("SMP")) {
                    initRecyclerSMP();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No se seleccionó ningún item
            }
        });

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       initRecyclerTodos();
    }

    private void initRecyclerTodos() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        productoModelList = new ArrayList<>();  // Inicializar productoModelList aquí

        adapter = new ProductosAdapter(getContext(), productoModelList);  // Inicializar el adaptador
        recyclerView.setAdapter(adapter);

        productosHomePresenter.cargarRecyclerView(recyclerView);
    }

    private void initRecyclerCentro() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        productoModelList = new ArrayList<>();  // Inicializar productoModelList aquí

        adapter = new ProductosAdapter(getContext(), productoModelList);  // Inicializar el adaptador
        recyclerView.setAdapter(adapter);

        productosHomePresenter.mostrarProductosAdminCentro(recyclerView);
    }

    private void initRecyclerSMP() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        productoModelList = new ArrayList<>();  // Inicializar productoModelList aquí

        adapter = new ProductosAdapter(getContext(), productoModelList);  // Inicializar el adaptador
        recyclerView.setAdapter(adapter);

        productosHomePresenter.mostrarProductosAdminSMP(recyclerView);
    }

    @Override
    public void onClick(View view) {

    }
}

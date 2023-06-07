package com.example.lecheriaapp.Vista.HomeView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class HomeFragment extends Fragment implements View.OnClickListener{

    private RecyclerView recyclerView;
    private ProductosAdapter adapter;
    private ArrayList<ProductoModel> productoModelList;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ProductosHomePresenter productosHomePresenter;

    private Spinner leftSpinner;
    private Spinner rightSpinner;

    public HomeFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewHome);
        // Obtener la referencia a los Spinners del layout
        leftSpinner = view.findViewById(R.id.leftSpinner);
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
        leftSpinner.setAdapter(leftAdapter);
        rightSpinner.setAdapter(rightAdapter);
        productosHomePresenter= new ProductosHomePresenter(getActivity(), mAuth, mDatabase);

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecycler();

    }
        private void initRecycler() {
        RecyclerView recyclerView = getView().findViewById(R.id.recyclerViewHome);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
            productoModelList = new ArrayList<>();  // Inicializar productoModelList aquí

            recyclerView.setAdapter(adapter);

            productosHomePresenter = new ProductosHomePresenter(getActivity(), mAuth, mDatabase);
            productosHomePresenter.cargarRecyclerView(recyclerView);

            // Verificar si el usuario ha iniciado sesión y agregar un producto si no lo ha hecho
            mAuth = FirebaseAuth.getInstance();
            if (mAuth.getCurrentUser() == null) {
                ProductoModel producto = new ProductoModel("producto1", "calorias1", "precio1", "estado1", "disponibilidad1","Ingrediente1", "https://loremflickr.com/320/240/desserts");
                ProductoModel producto2 = new ProductoModel("producto2", "calorias2", "precio2", "estado2", "disponibilidad2","Ingrediente2", "https://loremflickr.com/320/240/desserts");
                ProductoModel producto3 = new ProductoModel("producto3", "calorias3", "precio3", "estado3", "disponibilidad3","Ingrediente3", "https://loremflickr.com/320/240/desserts");
                ProductoModel producto4 = new ProductoModel("producto4", "calorias4", "precio4", "estado4", "disponibilidad4","Ingrediente4", "https://loremflickr.com/320/240/desserts");
                ProductoModel producto5 = new ProductoModel("producto5", "calorias5", "precio5", "estado5", "disponibilidad5","Ingrediente5", "https://loremflickr.com/320/240/desserts");
                ProductoModel producto6 = new ProductoModel("producto6", "calorias6", "precio6", "estado6", "disponibilidad6","Ingrediente6", "https://loremflickr.com/320/240/desserts");
                productoModelList.add(producto);
                productoModelList.add(producto2);
                productoModelList.add(producto3);
                productoModelList.add(producto4);
                productoModelList.add(producto5);
                productoModelList.add(producto6);
                adapter = new ProductosAdapter(getActivity(), productoModelList);
                recyclerView.setAdapter(adapter);

                adapter.notifyDataSetChanged();
            }
    }

    @Override
    public void onClick(View view) {



    }
}


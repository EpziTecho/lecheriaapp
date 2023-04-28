package com.example.lecheriaapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lecheriaapp.Productos.Productos;
import com.example.lecheriaapp.Productos.ProductosAdapter;

import java.util.ArrayList;


public class PromocionesFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductosAdapter adapter;
    private ArrayList<Productos> productosList;

    private Spinner leftSpinner;
    private Spinner rightSpinner;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_promociones, container, false);

        // Obtener la referencia al RecyclerView del layout
        recyclerView = root.findViewById(R.id.recyclerView);

        // Crear un LayoutManager para mostrar los elementos en dos columnas
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        // Crear y asignar un ProductosAdapter al RecyclerView
        productosList = new ArrayList<>();
        adapter = new ProductosAdapter(getContext(), productosList);
        recyclerView.setAdapter(adapter);

        // Agregar productos de ejemplo
        productosList.add(new Productos("Producto 1", "Descripción del producto 1", "10.00", "https://loremflickr.com/320/240/desserts"));
        productosList.add(new Productos("Producto 2", "Descripción del producto 2", "20.00", "https://loremflickr.com/320/240/desserts"));
        productosList.add(new Productos("Producto 3", "Descripción del producto 3", "30.00", "https://loremflickr.com/320/240/desserts"));
        productosList.add(new Productos("Producto 4", "Descripción del producto 4", "40.00", "https://loremflickr.com/320/240/desserts"));

        // Notificar al adaptador que se han agregado elementos
        adapter.notifyDataSetChanged();
/*// Obtener la referencia a los Spinners del layout
        leftSpinner = root.findViewById(R.id.leftSpinner);
        rightSpinner = root.findViewById(R.id.rightSpinner);

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
        rightSpinner.setAdapter(rightAdapter);*/

        return root;
    }
}


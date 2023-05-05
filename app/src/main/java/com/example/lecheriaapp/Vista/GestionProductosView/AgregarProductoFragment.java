package com.example.lecheriaapp.Vista.GestionProductosView;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lecheriaapp.R;

public class AgregarProductoFragment extends Fragment {

    public AgregarProductoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.agregarproducto, container, false);
    }
}
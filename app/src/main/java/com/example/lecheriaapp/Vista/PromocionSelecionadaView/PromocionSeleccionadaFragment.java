package com.example.lecheriaapp.Vista.PromocionSelecionadaView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.lecheriaapp.R;

public class PromocionSeleccionadaFragment extends Fragment {
    private ListView mListaPasos;
    private ArrayAdapter<String> mPasosAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_promocion_seleccionada, container, false);

        // Crear el adaptador de la lista
        String[] pasos = {"1: Acercate a cualquiera de nuestras sedes", "2: Muestra el qr en caja", "3: Disfruta tu promo!"};
        mPasosAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, R.id.paso_texto, pasos);

        // Configurar la lista con el adaptador
        mListaPasos = (ListView) rootView.findViewById(R.id.lista_pasos);
        mListaPasos.setAdapter(mPasosAdapter);

        return rootView;
    }
}


package com.example.lecheriaapp.Vista.PromoPerfilView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.lecheriaapp.R;
import com.example.lecheriaapp.Vista.PromocionSelecionadaView.PromocionSeleccionadaFragment;

public class PromoperfilFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_promoperfil, container, false);

        // Obtener el botón de "Quiero esta promo"
        Button quieroPromoButton = rootView.findViewById(R.id.btn_quiero_promo);

        // Establecer un OnClickListener para el botón
        quieroPromoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear una instancia del fragmento PromocionSeleccionadaFragment
                PromocionSeleccionadaFragment nuevoFragmento = new PromocionSeleccionadaFragment();

                // Reemplazar el actual fragmento por el nuevo fragmento
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, nuevoFragmento);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return rootView;
    }
}

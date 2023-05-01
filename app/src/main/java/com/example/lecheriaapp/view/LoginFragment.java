package com.example.lecheriaapp.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.lecheriaapp.R;
import com.example.lecheriaapp.model.Usuario;
import com.example.lecheriaapp.view.fragmentos.AdminFragment;
import com.example.lecheriaapp.view.fragmentos.UsuarioFragment;

import java.util.List;

public class LoginFragment extends Fragment {
    private EditText correoEditText;
    private EditText passwordEditText;
    private Button iniciarSesionButton;

    public LoginFragment() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflar el layout del fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // Obtener referencias a los elementos del layout
        correoEditText = view.findViewById(R.id.username_input);
        passwordEditText = view.findViewById(R.id.password_input);
        iniciarSesionButton = view.findViewById(R.id.login_button);

        // Configurar el botón de iniciar sesión para validar las credenciales
        iniciarSesionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo = correoEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                List<Usuario> usuarios = Usuario.getUsuarios();

                for (Usuario usuario : usuarios) {
                    if (usuario.getCorreo().equals(correo) && usuario.getPassword().equals(password)) {
                        if (usuario.getRol().equals("Administrador")) {
                            // Redireccionar al fragmento de administrador
                            Fragment adminFragment = new AdminFragment();
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragment_container, adminFragment);
                            transaction.commit();
                        } else if (usuario.getRol().equals("Cliente")) {
                            // Redireccionar al fragmento de cliente
                            Fragment userFragment = new UsuarioFragment();
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragment_container, userFragment);
                            transaction.commit();
                        }
                        Toast.makeText(getActivity(), "Bienvenido " + usuario.getNombre(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                }

                // Si las credenciales no son válidas, mostrar un mensaje de error
                Toast.makeText(getActivity(), "Credenciales inválidas", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}

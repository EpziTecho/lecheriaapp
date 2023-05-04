package com.example.lecheriaapp.Vista.RegistroView;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lecheriaapp.Presentador.RegistroPresenter.PresentadorRegistro;
import com.example.lecheriaapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistroFragment extends Fragment implements View.OnClickListener {

    // Declaración de variables
    private EditText mEtxtNombre, mEtxtUsuario, mEtxtCorreo, mEtxtPassword, mEtxtConfirmarPassword;
    private PresentadorRegistro presentadorRegistro;

    public RegistroFragment() {

    }

    // Método que se ejecuta cuando se crea la vista del fragmento
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registro, container, false);

        // Obtener instancias de Firebase Auth y Database
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        // Crear instancia del presentador del registro
        presentadorRegistro = new PresentadorRegistro(getActivity(), mAuth, mDatabase);

        // Obtener referencias de los elementos del layout
        mEtxtNombre = view.findViewById(R.id.editTextNombre);
        mEtxtUsuario = view.findViewById(R.id.editTextUsername);
        mEtxtCorreo = view.findViewById(R.id.editTextEmail);
        mEtxtPassword = view.findViewById(R.id.editTextPassword);
        mEtxtConfirmarPassword = view.findViewById(R.id.editTextConfirmarPassword);
        Button mBtnRegistro = view.findViewById(R.id.buttonRegistro);
        mBtnRegistro.setOnClickListener(this);

        // Devolver vista
        return view;
    }

    // Método que se ejecuta cuando se hace clic en un botón
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonRegistro:
                // Obtener valores de los campos de texto
                String nombre = mEtxtNombre.getText().toString().trim();
                String usuario = mEtxtUsuario.getText().toString().trim();
                String email = mEtxtCorreo.getText().toString().trim();
                String password = mEtxtPassword.getText().toString().trim();
                String confirmarPassword = mEtxtConfirmarPassword.getText().toString().trim();

                // Verificar si las contraseñas coinciden
                if (password.equals(confirmarPassword)) {
                    // Enviar información al presentador para crear un nuevo usuario
                    presentadorRegistro.singUpUser(email, password, nombre, usuario);
                } else {
                    // Mostrar mensaje de error si las contraseñas no coinciden
                    Toast.makeText(getActivity(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
}

package com.example.lecheriaapp.Vista.PrincipalView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.lecheriaapp.CartaCompletaFragment;
import com.example.lecheriaapp.HomeFragment;
import com.example.lecheriaapp.Presentador.PrincipalPresenter.PresenterPrincipal;
import com.example.lecheriaapp.PromocionesFragment;
import com.example.lecheriaapp.PromoperfilFragment;
import com.example.lecheriaapp.R;
import com.example.lecheriaapp.Vista.GestionProductosView.GestionProductosFragment;
import com.example.lecheriaapp.UbicanosFragment;
import com.example.lecheriaapp.databinding.ActivityMainBinding;
import com.example.lecheriaapp.Vista.LoginView.LoginFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DrawerLayout drawerLayout;
    private ActivityMainBinding binding;

    private PresenterPrincipal presenterPrincipal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflar el layout de la actividad usando la clase generada por data binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        // Establecer el layout como el contenido principal de la actividad
        setContentView(binding.getRoot());

        // Configurar la barra de herramientas (toolbar) y establecerla como la barra de acciones (action bar)
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);

        // Obtener el layout del cajón de navegación y establecer su escucha de eventos
        drawerLayout = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        navigationView.setNavigationItemSelectedListener(this);
        //comentario de prueba
        // Crear el botón de navegación y configurarlo con el cajón de navegación y la barra de herramientas
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Configurar el fondo del menú de navegación inferior y establecer su escucha de eventos
        binding.bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            // Cambiar el fragmento mostrado en función del elemento seleccionado en el menú de navegación inferior
            switch (item.getItemId()) {
                case R.id.home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.Ubicanos:
                    replaceFragment(new UbicanosFragment());
                    break;
                case R.id.Promos:
                    replaceFragment(new PromocionesFragment());
                    break;

            }

            return true;
        });

        // Si no se ha guardado ningún estado anterior, crear y mostrar el fragmento de inicio y marcar el elemento "Inicio" como seleccionado
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.home);
        }

        presenterPrincipal = new PresenterPrincipal(this,mDatabase,mAuth);
        presenterPrincipal.welcomeMessage();
       /* // Obtener una instancia de FirebaseAuth y DatabaseReference para acceder a la información del usuario actual
        mAuth= FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference();
        FirebaseUser usuario = mAuth.getCurrentUser();

        // Si hay un usuario logueado, mostrar un mensaje de bienvenida con su nombre obtenido desde la base de datos
        if (usuario!= null) {
            mDatabase.child("Usuarios").child(usuario.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Toast.makeText(MainActivity.this, "Bienvenido " + snapshot.child("nombre").getValue(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Manejo de errores
                }
            });
        } else {
            // Si no hay ningún usuario logueado, mostrar un mensaje informativo
            Toast.makeText(MainActivity.this, "Probandooo xD-- No hay usuario logueado", Toast.LENGTH_SHORT).show();
        }*/
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_IniciarSesion:
                  replaceFragment(new LoginFragment());
                 break;
            case R.id.nav_cartacompleta:
                replaceFragment(new CartaCompletaFragment());
                break;

            case R.id.nav_promosperfil:
                replaceFragment(new PromoperfilFragment());
                break;

            case R.id.nav_gestionProductos:
                replaceFragment(new GestionProductosFragment());
                break;

            case R.id.nav_logout:
                //Toast.makeText(this, "Sesion Finalizada ", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Vuelve pronto", Toast.LENGTH_SHORT).show();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
    //Boton de Notificaciones
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.notificaciones) {
            // Aquí manejas el clic en el boton de notificaciones
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
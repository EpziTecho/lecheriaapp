package com.example.lecheriaapp.Vista.PrincipalView;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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

import com.bumptech.glide.Glide;
import com.example.lecheriaapp.CartaCompletaFragment;
import com.example.lecheriaapp.HomeFragment;
import com.example.lecheriaapp.Modelo.UserModel;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


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
        //Prueva merge
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

        // Verificar si hay una sesión iniciada y ocultar el elemento de menú "Iniciar Sesión" si es así, o el elemento de menú "Cerrar Sesión" en caso contrario
        Menu navMenu = navigationView.getMenu(); //Obtener el menu del navigationView
        MenuItem loginItem = navMenu.findItem(R.id.nav_IniciarSesion);//Obtener el item del menu del navigationView
        MenuItem logoutItem = navMenu.findItem(R.id.nav_logout);//Obtener el item del menu del navigationView
        mAuth = FirebaseAuth.getInstance();//Obtener la instancia de la base de datos para verificar si hay una sesion iniciada o no
        if (mAuth.getCurrentUser() != null) { //Si el usuario no es nulo
            loginItem.setVisible(false);//Ocultar el item de iniciar sesion
            logoutItem.setVisible(true);//Mostrar el item de cerrar sesion
        } else {//Si el usuario es nulo
            loginItem.setVisible(true);//Mostrar el item de iniciar sesion
            logoutItem.setVisible(false);//Ocultar el item de cerrar sesion
        }
        //Hacer que Al iniciar Sesion , del usuario logeado se muestre el nombre del usuario  y el correo en el header
        //Obtener referencia del header en el navigationView
        View headerView = navigationView.getHeaderView(0);
        mAuth= FirebaseAuth.getInstance(); //Obtener la instancia de la base de datos
        mDatabase= FirebaseDatabase.getInstance().getReference(); //Obtener la referencia de la base de datos
        FirebaseUser currentUser = mAuth.getCurrentUser();//Obtener el usuario actual
        TextView txtUserName = headerView.findViewById(R.id.tvNombre); //Obtener el nombre del usuario y mostrarlo en el header(TextView)
        TextView txtUserEmail = headerView.findViewById(R.id.tvCorreo);//Obtener el correo del usuario y mostrarlo en el header(TextView)
        //Imagenes aun en proceso
        /*ImageView imgUserProfile = headerView.findViewById(R.id.imgUserProfile);*/
        if (currentUser != null) { //Si el usuario no es nulo
            mDatabase.child("Usuarios").child(currentUser.getUid()).get().addOnCompleteListener(task -> { //Obtener los datos del usuario actual
                if (task.isSuccessful()) {
                    UserModel userModel = task.getResult().getValue(UserModel.class); //
                    if (userModel != null) {
                        txtUserName.setText(userModel.getNombre());
                        txtUserEmail.setText(userModel.getEmail());
    /* if (currentUser.getPhotoUrl() != null) {
        Glide.with(this).load(currentUser.getPhotoUrl()).into(imgUserProfile);
    }*/
                    } else {
                        Toast.makeText(this, "Error al obtener los datos del usuario", Toast.LENGTH_SHORT).show();
                    }
                } else {//Si no se obtienen los datos del usuario actual
                    Toast.makeText(this, "Error al obtener los datos del usuario", Toast.LENGTH_SHORT).show();
                }
            });



        }

        // Si no se ha guardado ningún estado anterior, crear y mostrar el fragmento de inicio y marcar el elemento "Inicio" como seleccionado
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.home);
        }

        presenterPrincipal = new PresenterPrincipal(this,mDatabase,mAuth);
        presenterPrincipal.welcomeMessage();

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
                // Cerrar la sesión actual y redirigir a la actividad de inicio de sesión con un mensaje de despedida,
                // se refresca la actividad para evitar que el usuario pueda volver a ella usando el botón "Atrás"
                Toast.makeText(this, "Vuelve pronto", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut(); // Cerrar la sesión actual
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent); // Redirigir a la actividad de inicio de sesión
                finish(); // Finalizar la actividad actual para evitar que el usuario pueda volver a ella usando el botón "Atrás"

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
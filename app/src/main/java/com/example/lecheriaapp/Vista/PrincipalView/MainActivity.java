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
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.lecheriaapp.Vista.FavoritosUsuarioView.FavoritosUsuarioFragment;
import com.example.lecheriaapp.Vista.HomeView.HomeFragment;
import com.example.lecheriaapp.Modelo.UserModel;
import com.example.lecheriaapp.Presentador.PrincipalPresenter.PresenterPrincipal;
import com.example.lecheriaapp.Vista.PromocionesView.PromocionesFragment;
import com.example.lecheriaapp.Vista.PromoPerfilView.PromoperfilFragment;
import com.example.lecheriaapp.R;
import com.example.lecheriaapp.Vista.GestionProductosView.GestionProductosFragment;
import com.example.lecheriaapp.Vista.UbicanosView.UbicanosFragment;
import com.example.lecheriaapp.databinding.ActivityMainBinding;
import com.example.lecheriaapp.Vista.LoginView.LoginFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DrawerLayout drawerLayout;
    private ActivityMainBinding binding;

    private PresenterPrincipal presenterPrincipal;
    private ImageView mUserImageView;

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
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Menu navMenu = navigationView.getMenu();
        MenuItem loginItem = navMenu.findItem(R.id.nav_IniciarSesion);
        MenuItem logoutItem = navMenu.findItem(R.id.nav_logout);
        MenuItem gestionProductosItem = navMenu.findItem(R.id.nav_gestionProductos);
        MenuItem gestionProductosItemOculto1 = navMenu.findItem(R.id.nav_favoritos);
        MenuItem gestionProductosItemOculto2 = navMenu.findItem(R.id.nav_promosperfil);
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            loginItem.setVisible(false);
            logoutItem.setVisible(true);

            String currentUserId = mAuth.getCurrentUser().getUid();
            mDatabase.child("Usuarios").child(currentUserId).child("rol").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    String rol = task.getResult().getValue(String.class);
                    if (rol != null) {
                        if (rol.equals("AdminCentro") || rol.equals("AdminSMP") || rol.equals("AdminCallao") || rol.equals("AdminAte")) {
                            gestionProductosItem.setVisible(true);
                            gestionProductosItemOculto1.setVisible(false);
                            gestionProductosItemOculto2.setVisible(false);
                        } else if (rol.equals("cliente")) {
                            gestionProductosItem.setVisible(false);
                            gestionProductosItemOculto1.setVisible(true);
                            gestionProductosItemOculto2.setVisible(true);
                        } else {
                            gestionProductosItem.setVisible(false);
                            gestionProductosItemOculto1.setVisible(false);
                            gestionProductosItemOculto2.setVisible(false);
                        }
                    }
                } else {
                    Toast.makeText(this, "Error al obtener el rol del usuario", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            loginItem.setVisible(true);
            logoutItem.setVisible(false);
            gestionProductosItem.setVisible(false);
            gestionProductosItemOculto1.setVisible(false);
            gestionProductosItemOculto2.setVisible(false);
        }

        // Hacer que al iniciar sesión, del usuario logeado se muestre el nombre del usuario y el correo en el header
        // Obtener referencia del header en el navigationView
        View headerView = navigationView.getHeaderView(0);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        TextView txtUserName = headerView.findViewById(R.id.tvNombre);
        TextView txtUserEmail = headerView.findViewById(R.id.tvCorreo);
        ImageView imgUserPhoto = headerView.findViewById(R.id.ivFoto);

        if (currentUser != null) {
            String uid = currentUser.getUid();
            DatabaseReference userRef = mDatabase.child("Usuarios").child(uid);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        UserModel userModel = dataSnapshot.getValue(UserModel.class);
                        if (userModel != null) {
                            String userName = userModel.getNombre();
                            String userEmail = userModel.getEmail();
                            String userPhoto = userModel.getImagen();
                            txtUserName.setText(userName);
                            txtUserEmail.setText(userEmail);
                            // Carga de imagen con Glide
                            if (userPhoto != null) {
                                Glide.with(getApplicationContext())
                                        .load(userPhoto)
                                        .apply(RequestOptions.circleCropTransform())
                                        .transition(DrawableTransitionOptions.withCrossFade())
                                        .into(imgUserPhoto);
                            } else {
                                // Si no hay URL de imagen, puedes mostrar una imagen predeterminada aquí
                                imgUserPhoto.setImageResource(R.drawable.logolecheria);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Manejar el error, si es necesario
                }
            });
        }

        // Si no se ha guardado ningún estado anterior, crear y mostrar el fragmento de inicio y marcar el elemento "Inicio" como seleccionado
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.home);
        }

        presenterPrincipal = new PresenterPrincipal(this, mDatabase, mAuth);
        presenterPrincipal.welcomeMessage();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_IniciarSesion:
                replaceFragment(new LoginFragment());
                break;
            case R.id.nav_favoritos:
                replaceFragment(new FavoritosUsuarioFragment());
                break;

            case R.id.nav_promosperfil:
                replaceFragment(new PromocionesFragment());
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
            // Aquí manejas el clic en el botón de notificaciones
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

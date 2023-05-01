package com.example.lecheriaapp.model;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private String nombre;
    private String apellido;
    private String correo;
    private String password;
    private String rol;

    public Usuario(String nombre, String apellido, String correo, String password, String rol) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.password = password;
        this.rol = rol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }


    // Define una lista estática de usuarios
    public static List<Usuario> getUsuarios() {
        List<Usuario> usuario = new ArrayList<>();
        usuario.add(new Usuario("Juan", "Perez", "juan@gmail.com", "123456", "Administrador"));
        usuario.add(new Usuario("Maria", "Lopez", "maria@gmail.com", "123456", "Cliente"));
        return usuario;
    }
}

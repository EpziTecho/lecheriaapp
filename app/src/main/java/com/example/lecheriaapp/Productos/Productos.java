package com.example.lecheriaapp.Productos;

public class Productos {
private String nombre;
    private String descripcion;
    private String precio;
    private String imagen;

    public Productos(String nombre, String descripcion, String precio, String imagen) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.imagen = imagen;
    }

    public Productos() {
        this.nombre = "";
        this.descripcion = "";
        this.precio = "";
        this.imagen = "";
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getPrecio() {
        return precio;
    }

    public String getImagen() {
        return imagen;
    }

}

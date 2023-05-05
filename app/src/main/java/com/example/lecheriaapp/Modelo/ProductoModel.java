package com.example.lecheriaapp.Modelo;

public class ProductoModel {
private String nombre;
    private String descripcion;
    private String precio;
    private String imagen;

    public ProductoModel(String nombre, String descripcion, String precio, String imagen) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.imagen = imagen;
    }

    public ProductoModel() {
        this.nombre = "";
        this.descripcion = "";
        this.precio = "";
        this.imagen = "";
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}

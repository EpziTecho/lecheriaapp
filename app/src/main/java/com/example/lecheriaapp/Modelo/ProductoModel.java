package com.example.lecheriaapp.Modelo;

public class ProductoModel {
    private String nombre;
    private String calorias;
    private String precio;
    private String estado;
    private String disponibilidad;
    private String Ingredientes;
    private String imagen;

    public ProductoModel(String nombre, String calorias, String precio, String estado, String disponibilidad,String Ingredientes, String imagen) {
        this.nombre = nombre;
        this.calorias = calorias;
        this.precio = precio;
        this.estado = estado;
        this.disponibilidad = disponibilidad;
        this.Ingredientes = Ingredientes;
        this.imagen = imagen;
    }

    public ProductoModel() {

    }

    public String getCalorias() {
        return calorias;
    }

    public void setCalorias(String calorias) {
        this.calorias = calorias;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }



    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(String disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public String getIngredientes() {
        return Ingredientes;
    }

    public void setIngredientes(String ingredientes) {
        Ingredientes = ingredientes;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}


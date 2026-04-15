package modelo;

public class Producto {

    private int codP;
    private float precio;
    private String descripcion;
    private TipoProducto tipo; // Usamos el enum TipoProducto directamente
    private int idCategoria;

    // Constructor vacío
    public Producto() {
    }

    // Constructor con todos los campos
    public Producto(int codP, float precio, String descripcion, TipoProducto tipo, int idCategoria) {
        this.codP = codP;
        this.precio = precio;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.idCategoria = idCategoria;
    }

    // Getters y setters
    public int getCodP() {
        return codP;
    }

    public void setCodP(int codP) {
        this.codP = codP;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TipoProducto getTipo() {
        return tipo;
    }

    public void setTipo(TipoProducto tipo) {
        this.tipo = tipo;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

}
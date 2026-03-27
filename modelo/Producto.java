package modelo;

public class Producto {
	
	    private int codP;
	    private float precio;
	    private String descripcion;
	    private Tipo tipo;
	    private int idCategoria;

	    public Producto() {
	    }

	    public Producto(int codP, float precio, String descripcion, Tipo tipo, int idCategoria) {
	        this.codP = codP;
	        this.precio = precio;
	        this.descripcion = descripcion;
	        this.tipo = tipo;
	        this.idCategoria = idCategoria;
	    }

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

	    public Tipo getTipo() {
	        return tipo;
	    }

	    public void setTipo(Tipo tipo) {
	        this.tipo = tipo;
	    }

	    public int getIdCategoria() {
	        return idCategoria;
	    }

	    public void setIdCategoria(int idCategoria) {
	        this.idCategoria = idCategoria;
	    }

}

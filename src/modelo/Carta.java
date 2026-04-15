package modelo;

public class Carta {
	private String id_C;
	private String nombre;
	private String tipo;
	private double precio;
	private boolean foil;
	private int psa;
	
	// constructor
	public Carta() {
		
	}
	
	public Carta(String id_C, String nombre, String tipo, double precio) {
		this.id_C = id_C;
		this.nombre = nombre;
		this.tipo = tipo;
		this.precio = precio;
	}
	
	// getters and setters
	public boolean isFoil() {
	    return foil;
	}
	public void setFoil(boolean foil) {
	    this.foil = foil;
	}
	public String getId_C() {
		return id_C;
	}
	public void setId_C(String id_C) {
		this.id_C = id_C;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public double getPrecio() {
		return precio;
	}
	public void setPrecio(double precio) {
		this.precio = precio;
	}
	public int getPsa() {
	    return psa;
	}
	public void setPsa(int psa) {
	    this.psa = psa;
	}
	
	@Override
	public String toString() {
	    String tipoFoil = foil ? "✨ FOIL" : "";
	    return nombre + " (" + tipo + ") " + tipoFoil + " - " + precio + "€";
	}
}

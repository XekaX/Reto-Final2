package modelo;

/**
 * 
 * AUTOR: Ander
 * 
 * Clase que representa una carta del sistema.
 * 
 * Contiene la información básica de una carta como su identificador,
 * nombre, tipo, precio, rareza (foil) y grado de evaluación (PSA).
 */
public class Carta {

	/** Identificador único de la carta */
	private String id_C;

	/** Nombre de la carta */
	private String nombre;

	/** Tipo de carta (Pokemon, Magic, One Piece, etc.) */
	private String tipo;

	/** Precio de la carta */
	private double precio;

	/** Indica si la carta es de tipo foil (especial) */
	private boolean foil;

	/** Nivel de gradación PSA de la carta */
	private int psa;
	
	/**
	 * Constructor vacío de la clase Carta.
	 */
	public Carta() {
		
	}
	
	/**
	 * Constructor con parámetros básicos.
	 * 
	 * @param id_C identificador de la carta
	 * @param nombre nombre de la carta
	 * @param tipo tipo de carta
	 * @param precio precio de la carta
	 */
	public Carta(String id_C, String nombre, String tipo, double precio) {
		this.id_C = id_C;
		this.nombre = nombre;
		this.tipo = tipo;
		this.precio = precio;
	}
	
	/**
	 * Devuelve si la carta es foil.
	 * 
	 * @return true si es foil, false en caso contrario
	 */
	public boolean isFoil() {
	    return foil;
	}

	/**
	 * Establece si la carta es foil.
	 * 
	 * @param foil valor booleano
	 */
	public void setFoil(boolean foil) {
	    this.foil = foil;
	}

	/**
	 * Obtiene el identificador de la carta.
	 * 
	 * @return id de la carta
	 */
	public String getId_C() {
		return id_C;
	}

	/**
	 * Establece el identificador de la carta.
	 * 
	 * @param id_C identificador
	 */
	public void setId_C(String id_C) {
		this.id_C = id_C;
	}

	/**
	 * Obtiene el nombre de la carta.
	 * 
	 * @return nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Establece el nombre de la carta.
	 * 
	 * @param nombre nombre
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Obtiene el tipo de la carta.
	 * 
	 * @return tipo
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * Establece el tipo de la carta.
	 * 
	 * @param tipo tipo
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	/**
	 * Obtiene el precio de la carta.
	 * 
	 * @return precio
	 */
	public double getPrecio() {
		return precio;
	}

	/**
	 * Establece el precio de la carta.
	 * 
	 * @param precio precio
	 */
	public void setPrecio(double precio) {
		this.precio = precio;
	}

	/**
	 * Obtiene el valor PSA de la carta.
	 * 
	 * @return psa
	 */
	public int getPsa() {
	    return psa;
	}

	/**
	 * Establece el valor PSA de la carta.
	 * 
	 * @param psa valor de gradación
	 */
	public void setPsa(int psa) {
	    this.psa = psa;
	}
	
	/**
	 * Representación en texto de la carta.
	 * 
	 * Muestra nombre, tipo, si es foil y su precio.
	 * 
	 * @return cadena descriptiva de la carta
	 */
	@Override
	public String toString() {
	    String tipoFoil = foil ? "✨ FOIL" : "";
	    return nombre + " (" + tipo + ") " + tipoFoil + " - " + precio + "€";
	}
}
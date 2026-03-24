package modelo;

import java.time.LocalDate;

public class Compra {
	
	private int cod_P;
	private String dni;
	private LocalDate fecha;
	private int cantidad;
	
	public Compra(int cod_P, String dni, LocalDate fecha, int cantidad) {
		super();
		this.cod_P = cod_P;
		this.dni = dni;
		this.fecha = fecha;
		this.cantidad = cantidad;
	}

	public int getCod_P() {
		return cod_P;
	}

	public void setCod_P(int cod_P) {
		this.cod_P = cod_P;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	@Override
	public String toString() {
		return "Compra [cod_P=" + cod_P + ", dni=" + dni + ", fecha=" + fecha + ", cantidad=" + cantidad + "]";
	}
	
    

	
}

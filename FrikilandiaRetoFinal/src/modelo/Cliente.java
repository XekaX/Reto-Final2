package modelo;

/**
 * Representa un cliente del sistema.
 * Hereda de Usuario e incluye datos personales como nombre y teléfono.
 *
 * @author Ekain
 */

public class Cliente extends Usuario{
	
	
	private String nombre;
	private String telefono;
	
	public Cliente() {
	    super("", "");
	}
	
	public Cliente(String identificacion, String contrasenia) {
		super(identificacion, contrasenia);
	}
	
	public Cliente(String identificacion, String contrasenia, String nombre, String telefono) {
		super(identificacion, contrasenia);
		this.nombre = nombre;
		this.telefono = telefono;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	@Override
	public String toString() {
	    return super.toString() + " Cliente [nombre=" + nombre + ", telefono=" + telefono + "]";
	}

}
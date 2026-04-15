package modelo;

public abstract class Usuario {
	
	protected String identificacion;
	protected String contrasenia;
	
	
	public Usuario(String identificacion, String contrasenia) {
		super();
		this.identificacion = identificacion;
		this.contrasenia = contrasenia;
	}
	public String getIdentificacion() {
		return identificacion;
	}
	public void setIdentificacion(String identificacion) {
		this.identificacion = identificacion;
	}
	public String getContrasenia() {
		return contrasenia;
	}
	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
	}
	@Override
	public String toString() {
		return "Usuario [identificacion=" + identificacion + ", contrasenia=" + contrasenia + "]";
	}

}
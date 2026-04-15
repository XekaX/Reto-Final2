package modelo;

public class Trabajador extends Usuario{
	
	
    private int cod_T;
	private String nombre;
	private Tipo tipo;
	
	public Trabajador() {
	    super("", "");
	}
	
	public Trabajador(String identificacion, String contrasenia) {
		super(identificacion, contrasenia);
	}
	
	public Trabajador(String identificacion, String contrasenia,int cod_T, String nombre, Tipo tipo) {
		super(identificacion, contrasenia);
		this.nombre = nombre;
		this.tipo = tipo;
	}
	
	
	public int getCod_T() {
		return cod_T;
	}


	public void setCod_T(int cod_T) {
		this.cod_T = cod_T;
	}


	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	public String getContrasenia() {
		return contrasenia;
	}

	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
	}


	@Override
	public String toString() {
		return super.toString() +  "Trabajador [cod_P=" + cod_T+ ", nombre=" + nombre + ", tipo=" + tipo + "]";
	}
	
}
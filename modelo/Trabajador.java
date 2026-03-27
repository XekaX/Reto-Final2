package modelo;

public class Trabajador extends Usuario{
	
	
    private int cod_P;
	private String nombre;
	private Tipo tipo;
	
	public Trabajador() {
	    super("", "");
	}
	
	public Trabajador(String identificacion, String contrasenia) {
		super(identificacion, contrasenia);
	}
	
	public Trabajador(String identificacion, String contrasenia,int cod_P, String nombre, Tipo tipo) {
		super(identificacion, contrasenia);
		this.nombre = nombre;
		this.tipo = tipo;
	}
	
	
	public int getCod_P() {
		return cod_P;
	}


	public void setCod_P(int cod_P) {
		this.cod_P = cod_P;
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
		return super.toString() +  "Trabajador [cod_P=" + cod_P + ", nombre=" + nombre + ", tipo=" + tipo + "]";
	}
	
}
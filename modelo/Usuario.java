package modelo;

/**
 * Clase abstracta que representa un usuario genérico del sistema.
 * Sirve como clase base para distintos tipos de usuarios como Cliente o Trabajador.
 *
 * @author Ekain
 */
public abstract class Usuario {

    /** Identificación del usuario */
    protected String identificacion;

    /** Contraseña del usuario */
    protected String contrasenia;

    /**
     * Constructor de Usuario.
     *
     * @param identificacion identificador del usuario
     * @param contrasenia contraseña del usuario
     */
    public Usuario(String identificacion, String contrasenia) {
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
        return "Usuario [identificacion=" + identificacion +
               ", contrasenia=" + contrasenia + "]";
    }
}
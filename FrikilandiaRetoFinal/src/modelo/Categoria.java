package modelo;

/**
 * Representa una categoría de productos dentro del sistema.
 * Cada categoría tiene un identificador único y un nombre.
 * @author Ekain
 */
public class Categoria {

    /** Identificador de la categoría */
    private int id_categoria;

    /** Nombre de la categoría */
    private String nombre;

    public Categoria() {
        // Constructor vacío
    }

    public int getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(int id_categoria) {
        this.id_categoria = id_categoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Devuelve el nombre de la categoría.
     */
    @Override
    public String toString() {
        return nombre;
    }
}
package modelo;

/**
 * 
 * AUTOR: Ander
 * 
 * Clase que representa una línea dentro del carrito de compra.
 * 
 * Cada línea contiene un producto y la cantidad seleccionada
 * de dicho producto en el carrito.
 */
public class LineaCarrito {

    /** Producto asociado a la línea del carrito */
    private Producto producto;

    /** Cantidad del producto */
    private int cantidad;

    /**
     * Constructor de la clase LineaCarrito.
     * 
     * @param producto producto a añadir
     * @param cantidad cantidad del producto
     */
    public LineaCarrito(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    /**
     * Obtiene el producto de la línea.
     * 
     * @return producto
     */
    public Producto getProducto() { 
        return producto; 
    }

    /**
     * Obtiene la cantidad del producto.
     * 
     * @return cantidad
     */
    public int getCantidad() { 
        return cantidad; 
    }

    /**
     * Establece la cantidad del producto.
     * 
     * @param cantidad nueva cantidad
     */
    public void setCantidad(int cantidad) { 
        this.cantidad = cantidad; 
    }
}
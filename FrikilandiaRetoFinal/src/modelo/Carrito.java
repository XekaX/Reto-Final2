package modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * AUTOR: Ander
 * 
 * Clase que representa el carrito de compra.
 * 
 * Gestiona una colección de productos seleccionados por el usuario,
 * permitiendo añadir productos, calcular el total y obtener información
 * sobre el contenido del carrito.
 */
public class Carrito {

    /** Lista de líneas del carrito (producto + cantidad) */
    private List<LineaCarrito> lineas = new ArrayList<>();

    /**
     * Añade un producto al carrito.
     * 
     * Si el producto ya existe en el carrito, incrementa su cantidad.
     * En caso contrario, crea una nueva línea.
     * 
     * @param p producto a añadir
     * @param cantidad cantidad del producto
     */
    public void agregarProducto(Producto p, int cantidad) {
        for (LineaCarrito l : lineas) {
            if (l.getProducto().getCodP() == p.getCodP()) {
                l.setCantidad(l.getCantidad() + cantidad);
                return;
            }
        }
        lineas.add(new LineaCarrito(p, cantidad));
    }

    /**
     * Calcula el precio total del carrito.
     * 
     * @return total de la compra
     */
    public double calcularTotal() {
        double total = 0;
        for (LineaCarrito l : lineas) {
            total += l.getProducto().getPrecio() * l.getCantidad();
        }
        return total;
    }

    /**
     * Devuelve la lista de líneas del carrito.
     * 
     * @return lista de productos con sus cantidades
     */
    public List<LineaCarrito> getLineas() {
        return lineas;
    }
    
    /**
     * Calcula el número total de productos en el carrito.
     * 
     * @return cantidad total de productos
     */
    public int getTotalProductos() {
        int total = 0;
        for (LineaCarrito l : lineas) {
            total += l.getCantidad();
        }
        return total;
    }
}
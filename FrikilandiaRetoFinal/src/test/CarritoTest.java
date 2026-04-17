package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import modelo.*;

public class CarritoTest {

    @Test
    public void testCalcularTotal() {
        Carrito carrito = new Carrito();

        Producto p1 = new Producto(1, 10.0, "Producto1", TipoProducto.CARTAS, 1);
        Producto p2 = new Producto(2, 5.0, "Producto2", TipoProducto.COMICS, 2);

        carrito.getLineas().add(new LineaCarrito(p1, 2));
        carrito.getLineas().add(new LineaCarrito(p2, 3));

        double total = carrito.calcularTotal();

        assertEquals(35.0, total);
    }
    
    @Test
    public void testEliminarLinea() {
        Carrito carrito = new Carrito();

        Producto p = new Producto(1, 10.0, "Producto", TipoProducto.CARTAS, 1);

        carrito.getLineas().add(new LineaCarrito(p, 2));
        carrito.getLineas().add(new LineaCarrito(p, 1));

        carrito.getLineas().remove(0);

        assertEquals(1, carrito.getLineas().size());
    }
    
    @Test
    public void testAgregarProducto() {
        Carrito carrito = new Carrito();

        Producto p = new Producto(1, 10.0, "Producto", TipoProducto.CARTAS, 1);

        carrito.getLineas().add(new LineaCarrito(p, 2));

        assertEquals(1, carrito.getLineas().size());
    }
    
    @Test
    public void testProductoEsSobre() {
        Producto p = new Producto(1, 10.0, "Sobre Pokemon", TipoProducto.CARTAS, 1);

        assertEquals(TipoProducto.CARTAS, p.getTipo());
    }
}
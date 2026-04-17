package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import modelo.*;

public class ProductoTest {

    @Test
    public void testConstructorCompleto() {
        Producto p = new Producto(1, 9.99, "Carta Pikachu", TipoProducto.CARTAS, 10);

        assertEquals(1, p.getCodP());
        assertEquals(9.99, p.getPrecio());
        assertEquals("Carta Pikachu", p.getDescripcion());
        assertEquals(TipoProducto.CARTAS, p.getTipo());
        assertEquals(10, p.getIdCategoria());
    }
    
    @Test
    public void testSettersGetters() {
        Producto p = new Producto();

        p.setCodP(2);
        p.setPrecio(15.5);
        p.setDescripcion("Comic Batman");
        p.setTipo(TipoProducto.COMICS);
        p.setIdCategoria(20);

        assertEquals(2, p.getCodP());
        assertEquals(15.5, p.getPrecio());
        assertEquals("Comic Batman", p.getDescripcion());
        assertEquals(TipoProducto.COMICS, p.getTipo());
        assertEquals(20, p.getIdCategoria());
    }
    
    @Test
    public void testCambioTipoProducto() {
        Producto p = new Producto(3, 5.0, "Producto", TipoProducto.CARTAS, 1);

        p.setTipo(TipoProducto.COMICS);

        assertEquals(TipoProducto.COMICS, p.getTipo());
    }
    
    @Test
    public void testPrecioPositivo() {
        Producto p = new Producto(4, 20.0, "Producto caro", TipoProducto.CARTAS, 1);

        assertTrue(p.getPrecio() > 0);
    }
}
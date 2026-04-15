package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import modelo.RegistroVentas;

public class TestRegistroVentas {

    private RegistroVentas r1;
    private RegistroVentas r2;

    @BeforeEach
    public void setUp() {
        r1 = new RegistroVentas();
        r2 = new RegistroVentas();

        r1.registrarVenta("One Piece", 1, 3.5);
        r1.registrarVenta("Dragon Ball", 2, 4.0);

        r2.registrarVenta("Pikachu", 2, 5.0);
        r2.registrarVenta("Naruto", 3, 4.0);
    }

    @Test
    public void testAssertTrue() {
        assertTrue(r1 != null);
        assertTrue(r2.getProductos() != null, "Array no null");
        assertTrue(r2.existeProducto("Pikachu"));
    }

    @Test
    public void testAssertFalse() {
        assertFalse(r1 == r2);
        assertFalse(r2.existeProducto("Charizard"), "No debería existir");
    }

    @Test
    public void testAssertEquals() {
        assertEquals(2, r1.getContador());
        assertEquals(2, r2.getContador());
        assertEquals(2L, (long) r2.getContador(), "Error contador");

        assertEquals(5, r2.totalUnidadesVendidas());
        assertEquals(22.0, r2.ingresosTotales(), 0.01);
    }

    @Test
    public void testAssertNull() {
        assertNull(r1.getProductos()[2]);
        assertNull(r1.getProductos()[2], "Posición vacía");
        assertNotNull(r2);
        assertNotNull(r2.getProductos(), "Array no null");
    }

    @Test
    public void testAssertSame() {
        RegistroVentas r3 = r1;

        assertSame(r1, r3);
        assertSame(r1, r3, "Mismo objeto");
        assertNotSame(r1, r2);
        assertNotSame(r2, r3, "Diferentes objetos");
    }

    @Test
    public void testFail() {
        if (!r2.existeProducto("Pikachu")) {
            fail("Debería existir Pikachu");
        }
        if (r2.totalUnidadesVendidas() < 0) {
            fail("No puede ser negativo");
        }
        if (r1.getContador() < 0) {
            fail("Contador inválido");
        }
    }
}
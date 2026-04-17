package modelo;

/**
 * Gestiona un registro simple de ventas en memoria.
 * Permite añadir ventas y calcular estadísticas.
 *
 * @author Ekain
 */
public class RegistroVentas {

    private String[] productos;
    private int[] cantidades;
    private double[] precios;
    private int contador;

    public RegistroVentas() {
        productos = new String[5];
        cantidades = new int[5];
        precios = new double[5];
        contador = 0;
    }

    public boolean registrarVenta(String producto, int cantidad, double precio) {
        if (contador < productos.length && cantidad > 0 && precio > 0) {
            productos[contador] = producto;
            cantidades[contador] = cantidad;
            precios[contador] = precio;
            contador++;
            return true;
        }
        return false;
    }

    public boolean existeProducto(String producto) {
        for (int i = 0; i < contador; i++) {
            if (productos[i].equals(producto)) return true;
        }
        return false;
    }

    public int totalUnidadesVendidas() {
        int total = 0;
        for (int i = 0; i < contador; i++) {
            total += cantidades[i];
        }
        return total;
    }

    public double ingresosTotales() {
        double total = 0;
        for (int i = 0; i < contador; i++) {
            total += cantidades[i] * precios[i];
        }
        return total;
    }
}
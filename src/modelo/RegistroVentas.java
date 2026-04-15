package modelo;

public class RegistroVentas {

    private String[] productos;
    private int[] cantidades;
    private double[] precios;
    private int contador;

    // ✅ Este SÍ es un constructor (sin void)
    public RegistroVentas() {
        this.productos = new String[5];
        this.cantidades = new int[5];
        this.precios = new double[5];
        this.contador = 0;
    }

    public int getContador() {
        return contador;
    }

    public String[] getProductos() {
        return productos;
    }

    // =========================
    // Añadir venta
    // =========================
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

    // =========================
    // Buscar venta
    // =========================
    public boolean existeProducto(String producto) {
        for (int i = 0; i < contador; i++) {
            if (productos[i].equals(producto)) {
                return true;
            }
        }
        return false;
    }

    // =========================
    // Total vendido (cantidad)
    // =========================
    public int totalUnidadesVendidas() {
        int total = 0;
        for (int i = 0; i < contador; i++) {
            total += cantidades[i];
        }
        return total;
    }

    // =========================
    // Total ingresos
    // =========================
    public double ingresosTotales() {
        double total = 0;
        for (int i = 0; i < contador; i++) {
            total += cantidades[i] * precios[i];
        }
        return total;
    }
}
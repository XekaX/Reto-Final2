package modelo;

import java.util.ArrayList;
import java.util.List;

public class Carrito {
    private List<LineaCarrito> lineas = new ArrayList<>();

    public void agregarProducto(Producto p, int cantidad) {
        for (LineaCarrito l : lineas) {
            if (l.getProducto().getCodP() == p.getCodP()) {
                l.setCantidad(l.getCantidad() + cantidad);
                return;
            }
        }
        lineas.add(new LineaCarrito(p, cantidad));
    }

    public double calcularTotal() {
        double total = 0;
        for (LineaCarrito l : lineas) {
            total += l.getProducto().getPrecio() * l.getCantidad();
        }
        return total;
    }

    public List<LineaCarrito> getLineas() {
        return lineas;
    }
    
    public int getTotalProductos() {
        int total = 0;
        for (LineaCarrito l : lineas) {
            total += l.getCantidad();
        }
        return total;
    }
}
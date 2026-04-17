package utilidades;

import java.util.List;
import modelo.Producto;

public interface InterfazExportadorProductos {
    void exportarProductos(List<Producto> listaProductos, String ruta);
}
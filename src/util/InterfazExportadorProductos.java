package util;

import java.util.List;
import modelo.Producto;

public interface InterfazExportadorProductos {
    void exportarProductos(List<Producto> listaProductos, String ruta);
}
package vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import modelo.Producto;
import principal.Principal;

public class VentanaVisualizar extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private JTable tablaProductos;
    private JScrollPane jscroll;

    public static void main(String[] args) {
        try {
            VentanaVisualizar dialog = new VentanaVisualizar();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public VentanaVisualizar() {
        setBounds(100, 100, 553, 300);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setLayout(new FlowLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        
        presentarTablaProductos();
    }
    
    private JTable cargarTablaProductos() {
        String[] columnasNombre = { "Cod_P", "Precio", "Descripcion", "Tipo" };
        Map<Integer, Producto> productosMap = Principal.listarProductos();

        // Crear el modelo de la tabla
        DefaultTableModel model = new DefaultTableModel(null, columnasNombre);

        // Llenar el modelo con los datos de los productos
        for (Entry<Integer, Producto> entry : productosMap.entrySet()) {
            Producto produc = entry.getValue();
            if (produc != null) { // Verificar que el producto no sea nulo
                String[] fila = new String[4];
                fila[0] = String.valueOf(produc.getCodP()); // cod
                fila[1] = String.valueOf(produc.getPrecio()); // precio
                fila[2] = produc.getDescripcion(); // descripcion
                fila[3] = produc.getTipo().name(); // tipo
                model.addRow(fila);
            } else {
                System.out.println("Producto nulo encontrado en la entrada: " + entry.getKey());
            }
        }
        
        // Crear la tabla con el modelo
        return new JTable(model);
    }
    
    private void presentarTablaProductos() {
        contentPanel.setLayout(new BorderLayout());
        jscroll = new JScrollPane();
        jscroll.setBounds(0, 0, 585, 213);

        tablaProductos = this.cargarTablaProductos();
        jscroll.setViewportView(tablaProductos);
        contentPanel.add(jscroll, BorderLayout.CENTER); // Agregar JScrollPane al panel

        // Si quieres mostrar la columna "Tipo", quita estas líneas
        // tablaProductos.getColumnModel().getColumn(3).setMinWidth(0);
        // tablaProductos.getColumnModel().getColumn(3).setMaxWidth(0);
        // tablaProductos.getColumnModel().getColumn(3).setWidth(0);
    }
}
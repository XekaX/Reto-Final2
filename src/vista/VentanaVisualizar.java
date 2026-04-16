package vista;

import java.awt.BorderLayout;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import modelo.Producto;
import principal.Principal;
import util.ExportadorProductosXML;
import modelo.ResultadoMedia;

import java.awt.event.*;

/**
 * Ventana que muestra los productos en una tabla. Permite exportar a XML y ver
 * estadísticas.
 * 
 * Funcionalidades: - Visualizar todos los productos - Exportar productos a XML
 * - Mostrar estadísticas (media y producto más caro)
 * 
 * @author Ekain
 */
public class VentanaVisualizar extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();// Panel principal donde se añaden los componentes
	private JTable tablaProductos;// Tabla para mostrar productos
	private JButton btnXML, btnMedia;// Botones de acciones

	/**
	 * Constructor que inicializa la ventana.
	 */
	public VentanaVisualizar() {
		setBounds(100, 100, 551, 339);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		presentarTablaProductos();// Llamada para construir la interfaz
	}

	/**
	 * Carga los productos en una tabla.
	 * 
	 * @return JTable con productos
	 */
	private JTable cargarTablaProductos() {
		String[] columnas = { "Cod_P", "Precio", "Descripcion", "Tipo" };// Nombres de las columnas
		DefaultTableModel model = new DefaultTableModel(null, columnas);// Modelo de la tabla

		for (Producto p : Principal.listarProductos().values()) {
			model.addRow(new Object[] { p.getCodP(), // Código del producto
					p.getPrecio(), // Precio
					p.getDescripcion(), // Descripción
					p.getTipo() // Tipo (ENUM)
			});
		}
		return new JTable(model);
	}

	/**
	 * Método que construye la interfaz gráfica: - Añade la tabla dentro de un
	 * JScrollPane - Añade los botones de acciones
	 */
	private void presentarTablaProductos() {
		contentPanel.setLayout(null);

		tablaProductos = cargarTablaProductos();
		JScrollPane scroll = new JScrollPane(tablaProductos);
		scroll.setBounds(0, 0, 539, 215);
		contentPanel.add(scroll);

		btnXML = new JButton("Crear XML");
		btnXML.setBounds(201, 231, 105, 39);
		btnXML.addActionListener(this);
		contentPanel.add(btnXML);

		btnMedia = new JButton("Ver Media");
		btnMedia.setBounds(10, 231, 105, 39);
		btnMedia.addActionListener(this);
		contentPanel.add(btnMedia);
	}

	/**
	 * Método que gestiona los eventos de los botones.
	 * 
	 * @param e Evento generado por los botones
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// BOTÓN CREAR XML
		if (e.getSource().equals(btnXML)) {

			String ruta = "src/xml/catalogo.xml";// Ruta donde se guardará el archivo
			new java.io.File("src/xml").mkdirs();// Crear carpeta si no existe

			List<Producto> lista = new ArrayList<>(Principal.listarProductos().values());// Obtener lista de productos
			new ExportadorProductosXML().exportarProductos(lista, ruta);// Crear exportador XML y exporta el XML

			JOptionPane.showMessageDialog(this, "XML guardado en: " + ruta);// Mensaje de confirmación

		} else if (e.getSource().equals(btnMedia)) {

			ResultadoMedia m = Principal.mediaYMasCaro();

			JOptionPane.showMessageDialog(this, "Media: " + m.getMedia() + "\nMás caro: " + m.getProductoMasCaro()
					+ "\nPrecio máximo: " + m.getPrecioMaximo() + "\nClasificación: " + m.getClasificacion());
		}
	}
}
package vistas;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import main.Main;

import java.util.List;

import modelo.Producto;
import utilidades.ExportadorProductosXML;
import modelo.ResultadoMedia;

import java.awt.event.*;

/**
 * Ventana de visualización de productos. Permite mostrar los productos en una
 * tabla y ejecutar operaciones adicionales como exportación a XML y obtención
 * de estadísticas.
 *
 * Funcionalidades principales: - Listado de productos en formato tabla -
 * Exportación de productos a XML - Visualización de estadísticas (media de
 * precios y producto más caro)
 *
 * @author Ekain
 */
public class VentanaVisualizar extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	// Panel principal donde se colocan todos los componentes
	private final JPanel contentPanel = new JPanel();

	// Tabla que muestra los productos
	private JTable tablaProductos;

	// Botones de acciones principales
	private JButton btnXML, btnMedia;

	/**
	 * Constructor de la ventana. Inicializa la interfaz gráfica.
	 */
	public VentanaVisualizar() {
		setTitle("Visualizacion Productos");
		setBounds(100, 100, 551, 339);
		setLocationRelativeTo(null);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		presentarTablaProductos();
	}

	/**
	 * Carga los productos en una tabla Swing.
	 */
	private JTable cargarTablaProductos() {

		String[] columnas = { "Cod_P", "Precio", "Descripcion", "Tipo" };

		DefaultTableModel model = new DefaultTableModel(null, columnas);

		for (Producto p : Main.listarProductos().values()) {

			model.addRow(new Object[] { p.getCodP(), p.getPrecio(), p.getDescripcion(), p.getTipo() });
		}

		return new JTable(model);
	}

	/**
	 * Construye la interfaz gráfica de la ventana.
	 */
	private void presentarTablaProductos() {

		contentPanel.setLayout(null);

		// ⚪ FONDO BLANCO
		contentPanel.setBackground(Color.WHITE);

		// TABLA
		tablaProductos = cargarTablaProductos();
		JScrollPane scroll = new JScrollPane(tablaProductos);

		scroll.setBounds(0, 0, 539, 215);
		contentPanel.add(scroll);

		// ================= BOTÓN EXPORTAR XML =================
		btnXML = new JButton("Crear XML");
		btnXML.setBounds(330, 231, 140, 35);
		btnXML.addActionListener(this);
		estiloBoton(btnXML);
		contentPanel.add(btnXML);

		// ================= BOTÓN ESTADÍSTICAS =================
		btnMedia = new JButton("Ver Media");
		btnMedia.setBounds(30, 231, 140, 35);
		btnMedia.addActionListener(this);
		estiloBoton(btnMedia);
		contentPanel.add(btnMedia);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		// ================= EXPORTAR XML =================
		if (e.getSource().equals(btnXML)) {

			String ruta = "src/xml/catalogo.xml";
			new java.io.File("src/xml").mkdirs();

			List<Producto> lista = new ArrayList<>(Main.listarProductos().values());

			new ExportadorProductosXML().exportarProductos(lista, ruta);

			JOptionPane.showMessageDialog(this, "XML guardado en: " + ruta);

			// ================= ESTADÍSTICAS =================
		} else if (e.getSource().equals(btnMedia)) {

			ResultadoMedia m = Main.mediaYMasCaro();

			JOptionPane.showMessageDialog(this, "Media: " + m.getMedia() + "\nMás caro: " + m.getProductoMasCaro()
					+ "\nPrecio máximo: " + m.getPrecioMaximo() + "\nClasificación: " + m.getClasificacion());
		}
	}

	/**
	 * Estilo uniforme para botones
	 */
	private void estiloBoton(JButton btn) {
		btn.setBackground(new Color(30, 144, 255)); // azul
		btn.setForeground(Color.WHITE);
		btn.setFocusPainted(false);
		btn.setBorderPainted(false);
		btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
	}
}
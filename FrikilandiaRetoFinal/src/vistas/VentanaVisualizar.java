package vistas;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import modelo.Producto;
import main.Main;
import utilidades.ExportadorProductosXML;
import modelo.ResultadoMedia;

import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaVisualizar extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTable tablaProductos;
	private JScrollPane jscroll;
	private JButton btnXML;
	private JButton btnMedia;
	
	public VentanaVisualizar() {
		setBounds(100, 100, 551, 339);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		presentarTablaProductos();
	}

	private JTable cargarTablaProductos() {
		String[] columnasNombre = { "Cod_P", "Precio", "Descripcion", "Tipo" };
		Map<Integer, Producto> productosMap = Main.listarProductos();

		DefaultTableModel model = new DefaultTableModel(null, columnasNombre);

		for (Entry<Integer, Producto> entry : productosMap.entrySet()) {
			Producto produc = entry.getValue();
			if (produc != null) {
				String[] fila = new String[4];
				fila[0] = String.valueOf(produc.getCodP());
				fila[1] = String.format("%.2f", produc.getPrecio());
				fila[2] = produc.getDescripcion();
				fila[3] = produc.getTipo().name();
				model.addRow(fila);
			}
		}

		return new JTable(model);
	}

	private void presentarTablaProductos() {
		contentPanel.setLayout(null);

		tablaProductos = this.cargarTablaProductos();
		jscroll = new JScrollPane(tablaProductos);
		jscroll.setBounds(0, 0, 539, 215);

		contentPanel.add(jscroll);

		btnXML = new JButton("Crear XML");
		btnXML.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnXML.setBounds(201, 231, 105, 39);
		btnXML.addActionListener(this);
		contentPanel.add(btnXML);

		// En el constructor, junto al btnXML:
		btnMedia = new JButton("Ver Media");
		btnMedia.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnMedia.setBounds(10, 231, 105, 39);
		btnMedia.addActionListener(this);
		contentPanel.add(btnMedia);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(btnXML)) {

			String ruta = "src/xml/catalogo.xml";

			// Crear la carpeta si no existe
			new java.io.File("src/xml").mkdirs();

			// Generar XML
			List<Producto> listaProductos = new ArrayList<>(Main.listarProductos().values());
			ExportadorProductosXML exportador = new ExportadorProductosXML();
			exportador.exportarProductos(listaProductos, ruta);

			JOptionPane.showMessageDialog(this, "XML guardado en: " + ruta, "Éxito", JOptionPane.INFORMATION_MESSAGE);

		} else if (e.getSource().equals(btnMedia)) {
			ResultadoMedia media = Main.mediaYMasCaro();
		    JOptionPane.showMessageDialog(this,
		        "Precio medio: " + media.getMedia() + "€\n" +
		        "Producto más caro: " + media.getProductoMasCaro() + "\n" +
		        "Precio máximo: " + media.getPrecioMaximo() + "€\n" +
		        "Clasificación: " + media.getClasificacion(),
		        "Estadísticas productos",
		        JOptionPane.INFORMATION_MESSAGE);
		}

	}
}
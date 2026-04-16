package vista;

import java.awt.event.*;
import java.util.Map;
import javax.swing.*;

import controlador.Dao;
import controlador.DaoImplementacionMySql;
import excepciones.CodigoException;
import modelo.Categoria;
import modelo.Producto;
import modelo.TipoProducto;
import principal.Principal;

/**
 * Ventana principal para la gestión de productos. Permite añadir, modificar,
 * eliminar y visualizar productos.
 * 
 * Se conecta con la capa DAO para obtener categorías y con la clase Principal
 * para realizar operaciones CRUD sobre los productos.
 * 
 * @author Ekain
 */
public class VentanaTrabajador extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private Map<Integer, Categoria> map;// Map de categorías obtenidas de la base de datos
	private JTextField textDescripcion;// Campos de texto para introducir datos
	private JTextField textPrecio;
	private JTextField textCod;
	private JComboBox<Categoria> cmbCategoria;// ComboBox para seleccionar la categoría
	private JButton btnAñadir;
	private JButton btnModificar;
	private JButton btnEliminar;
	private JButton btnVisualizar;

	/**
	 * Constructor que inicializa la interfaz gráfica y carga las categorías.
	 */
	public VentanaTrabajador() {
		setTitle("Gestión de Productos");
		setSize(299, 324);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);

		JLabel lblCod = new JLabel("Código:");
		lblCod.setBounds(26, 29, 70, 24);
		getContentPane().add(lblCod);

		JLabel lblPrecio = new JLabel("Precio:");
		lblPrecio.setBounds(26, 80, 64, 13);
		getContentPane().add(lblPrecio);

		JLabel lblDescripcion = new JLabel("Descripción:");
		lblDescripcion.setBounds(26, 121, 97, 13);
		getContentPane().add(lblDescripcion);

		JLabel lblCategoria = new JLabel("Categoría:");
		lblCategoria.setBounds(26, 164, 70, 13);
		getContentPane().add(lblCategoria);

		textCod = new JTextField();
		textCod.setBounds(120, 34, 97, 19);
		getContentPane().add(textCod);

		textPrecio = new JTextField();
		textPrecio.setBounds(120, 79, 97, 19);
		getContentPane().add(textPrecio);

		textDescripcion = new JTextField();
		textDescripcion.setBounds(120, 120, 96, 19);
		getContentPane().add(textDescripcion);

		cmbCategoria = new JComboBox<>();
		cmbCategoria.setBounds(120, 162, 97, 21);
		getContentPane().add(cmbCategoria);

		// Crear DAO y obtener categorías de la base de datos
		Dao dao = new DaoImplementacionMySql();
		map = dao.listarCategoria();

		// Añado las categorias al comboBox
		for (Categoria c : map.values()) {
			cmbCategoria.addItem(c);
		}

		btnAñadir = new JButton("Añadir");
		btnAñadir.setBounds(10, 212, 120, 30);
		btnAñadir.addActionListener(this);
		getContentPane().add(btnAñadir);

		btnModificar = new JButton("Modificar");
		btnModificar.setBounds(135, 212, 120, 30);
		btnModificar.addActionListener(this);
		getContentPane().add(btnModificar);

		btnEliminar = new JButton("Eliminar");
		btnEliminar.setBounds(10, 252, 120, 30);
		btnEliminar.addActionListener(this);
		getContentPane().add(btnEliminar);

		btnVisualizar = new JButton("Visualizar");
		btnVisualizar.setBounds(135, 252, 120, 30);
		btnVisualizar.addActionListener(this);
		getContentPane().add(btnVisualizar);
	}

	/**
	 * Gestiona los eventos de los botones.
	 * 
	 * @param e Evento de acción
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(btnAñadir)) {
			alta();
		} else if (e.getSource().equals(btnEliminar)) {
			eliminar();
		} else if (e.getSource().equals(btnModificar)) {
			new VentanaModificar().setVisible(true);
		} else if (e.getSource().equals(btnVisualizar)) {
			new VentanaVisualizar().setVisible(true);
		}
	}

	/**
	 * Da de alta un nuevo producto.
	 * 
	 * Valida que el código sea numérico y el precio correcto. Muestra mensajes de
	 * error o éxito.
	 */
	private void alta() {
		Producto p = new Producto();
		try {
			String textoCod = textCod.getText();
			if (!textoCod.matches("\\d+")) {
				throw new CodigoException("Debes introducir un código numérico");
			}
			// Asignar valores al producto
			p.setCodP(Integer.parseInt(textoCod));
			p.setPrecio(Float.parseFloat(textPrecio.getText()));
			p.setDescripcion(textDescripcion.getText());

			// Obtener categoría seleccionada
			Categoria cat = (Categoria) cmbCategoria.getSelectedItem();
			p.setIdCategoria(cat.getId_categoria());

			if (cat.getNombre().equalsIgnoreCase("Cartas"))
				p.setTipo(TipoProducto.CARTAS);
			else if (cat.getNombre().equalsIgnoreCase("Comics"))
				p.setTipo(TipoProducto.COMICS);

			// Llamar a la lógica para guardar product	o
			boolean ok = Principal.altaProducto(p);

			// Mostrar resultado
			if (ok)
				JOptionPane.showMessageDialog(this, "Producto añadido: " + p.getDescripcion());
			else
				JOptionPane.showMessageDialog(this, "Error al añadir el producto.");

		} catch (CodigoException ex) {
			ex.visualizarMensaje();
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "Precio inválido");
		}
	}

	/**
	 * Elimina un producto por su código.
	 */
	private void eliminar() {
		int cod = Integer.parseInt(textCod.getText());

		if (!Principal.existeProducto(cod)) {
			JOptionPane.showMessageDialog(this, "No existe ese producto");
			return;
		}

		Producto p = new Producto();
		p.setCodP(cod);

		boolean ok = Principal.eliminarProducto(p);
		if (ok) {
			JOptionPane.showMessageDialog(this, "Producto eliminado");
			dispose(); // Cerrar ventana
		} else {
			JOptionPane.showMessageDialog(this, "Error al eliminar");
		}
	}
}
package vistas;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.*;
import java.util.Map;
import javax.swing.*;

import main.Dao;
import main.DaoImplementacionMySql;
import excepciones.CodigoException;
import main.Main;
import modelo.Categoria;
import modelo.Producto;
import modelo.TipoProducto;

/**
 * Ventana principal para la gestión de productos. Permite añadir, modificar,
 * eliminar y visualizar productos.
 * 
 * Se conecta con la capa DAO para obtener categorías y con la clase Principal
 * para realizar operaciones CRUD sobre los productos.
 * 
 * @author Ekain
 */
public class VMenuTrabajador extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private Map<Integer, Categoria> map; // Map de categorías obtenidas de la base de datos

	// Campos de texto para introducir datos
	private JTextField textDescripcion;
	private JTextField textPrecio;
	private JTextField textCod;

	// ComboBox para seleccionar la categoría
	private JComboBox<Categoria> cmbCategoria;

	private JButton btnAñadir;
	private JButton btnModificar;
	private JButton btnEliminar;
	private JButton btnVisualizar;
	private JButton btnCarta;

	/**
	 * Constructor que inicializa la interfaz gráfica y carga las categorías.
	 */
	public VMenuTrabajador() {
		setTitle("Gestión de Productos");
		setSize(361, 324);
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
		btnAñadir.setBounds(37, 212, 120, 30);
		btnAñadir.setIcon(escalarIcono("/resources/añadirProducto.png"));
		btnAñadir.addActionListener(this);
		estiloBoton(btnAñadir);
		getContentPane().add(btnAñadir);

		btnModificar = new JButton("Modificar");
		btnModificar.setBounds(197, 213, 120, 30);
		btnModificar.setIcon(escalarIcono("/resources/modificar.jpg"));
		btnModificar.addActionListener(this);
		estiloBoton(btnModificar);
		getContentPane().add(btnModificar);

		btnEliminar = new JButton("Eliminar");
		btnEliminar.setBounds(37, 253, 120, 30);
		btnEliminar.setIcon(escalarIcono("/resources/eliminar.png"));
		btnEliminar.addActionListener(this);
		estiloBoton(btnEliminar);
		getContentPane().add(btnEliminar);

		btnVisualizar = new JButton("Visualizar");
		btnVisualizar.setBounds(197, 254, 120, 30);
		btnVisualizar.setIcon(escalarIcono("/resources/visualizar.png"));
		btnVisualizar.addActionListener(this);
		estiloBoton(btnVisualizar);
		getContentPane().add(btnVisualizar);
		
		btnCarta = new JButton("Carta");
		btnCarta.setBounds(237, 31, 84, 20);
		btnCarta.addActionListener(this);
		estiloBoton(btnCarta);
		getContentPane().add(btnCarta);
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
		} else if (e.getSource().equals(btnCarta)) {
			VCartas cartas = new VCartas(null, rootPaneCheckingEnabled);
			cartas.setVisible(true);
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

			// Llamar a la lógica para guardar producto
			boolean ok = Main.añadirProducto(p);

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

		if (!Main.existeProducto(cod)) {
			JOptionPane.showMessageDialog(this, "No existe ese producto");
			return;
		}

		Producto p = new Producto();
		p.setCodP(cod);

		boolean ok = Main.eliminarProductos(p);
		if (ok) {
			JOptionPane.showMessageDialog(this, "Producto eliminado");
			dispose(); // Cerrar ventana
		} else {
			JOptionPane.showMessageDialog(this, "Error al eliminar");
		}
	}

	/**
	 * Estilo uniforme para los botones de la ventana.
	 */
	private void estiloBoton(JButton btn) {
		btn.setBackground(new Color(30, 144, 255));
		btn.setForeground(Color.WHITE);
		btn.setFocusPainted(false);
		btn.setBorderPainted(false);
		btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

		btn.setHorizontalTextPosition(SwingConstants.RIGHT);
		btn.setVerticalTextPosition(SwingConstants.CENTER);
		btn.setIconTextGap(8);
	}

	/**
	 * Escala un icono para que no ocupe todo el botón.
	 * 
	 * @param ruta ruta del recurso dentro del proyecto
	 * @return ImageIcon escalado
	 */
	private ImageIcon escalarIcono(String ruta) {
		ImageIcon icono = new ImageIcon(getClass().getResource(ruta));
		Image imagen = icono.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
		return new ImageIcon(imagen);
	}
}

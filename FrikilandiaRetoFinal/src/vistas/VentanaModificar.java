package vistas;

// Importaciones de interfaz gráfica y eventos
import java.awt.*;
import java.awt.event.*;

// Colecciones
import java.util.Map;

// Componentes Swing
import javax.swing.*;

// Clases de tu proyecto
import main.Dao;
import main.DaoImplementacionMySql;
import main.Main;
import modelo.Categoria;
import modelo.Producto;
/**
 * Ventana que permite modificar un producto existente.
 * 
 * <p>Funcionalidad principal:
 * <ul>
 *   <li>Introducir el código de un producto</li>
 *   <li>Modificar su precio</li>
 *   <li>Validar los datos introducidos</li>
 * </ul>
 * 
 * <p>Esta clase forma parte de la capa de presentación (vista)
 * dentro de una arquitectura basada en MVC. Se comunica con la
 * lógica de negocio a través de la clase {@code Principal}.
 * 
 * @author Ekain
 */
public class VentanaModificar extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	// Campos de texto para introducir datos del producto
	private JTextField textCod, textPrecio, textDescripcion;

	// ComboBox de categorías (no editable)
	private JComboBox<Categoria> cmbCategoria;

	// Mapa de categorías obtenidas desde la base de datos
	private Map<Integer, Categoria> map;

	// Botones de acciones
	private JButton btnGuardar, btnCancelar;

	/**
	 * Constructor que inicializa la interfaz gráfica
	 * y carga las categorías desde la base de datos.
	 */
	public VentanaModificar() {

		// Configuración básica de la ventana
		setTitle("Modificar Producto");
		setSize(400, 300);
		setLocationRelativeTo(null);

		getContentPane().setLayout(null);

		// ⚪ FONDO BLANCO
		getContentPane().setBackground(Color.WHITE);

		// Etiqueta Código
		JLabel lblCod = new JLabel("Código:");
		lblCod.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblCod.setForeground(Color.BLACK);
		lblCod.setBounds(26, 29, 70, 24);
		getContentPane().add(lblCod);

		// Campo Código
		textCod = new JTextField();
		textCod.setBounds(140, 34, 150, 19);
		textCod.setColumns(10);
		getContentPane().add(textCod);

		// Etiqueta Precio
		JLabel lblPrecio = new JLabel("Precio:");
		lblPrecio.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblPrecio.setForeground(Color.BLACK);
		lblPrecio.setBounds(26, 80, 64, 13);
		getContentPane().add(lblPrecio);

		// Campo Precio
		textPrecio = new JTextField();
		textPrecio.setBounds(140, 79, 150, 19);
		textPrecio.setColumns(10);
		getContentPane().add(textPrecio);

		// Etiqueta Descripción
		JLabel lblDesc = new JLabel("Descripción:");
		lblDesc.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblDesc.setForeground(Color.BLACK);
		lblDesc.setBounds(26, 121, 97, 13);
		getContentPane().add(lblDesc);

		// Campo Descripción (no editable porque no se modifica aquí)
		textDescripcion = new JTextField();
		textDescripcion.setBounds(140, 120, 150, 19);
		textDescripcion.setColumns(10);
		textDescripcion.setEditable(false);
		getContentPane().add(textDescripcion);

		// Etiqueta Categoría
		JLabel lblCat = new JLabel("Categoría:");
		lblCat.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblCat.setForeground(Color.BLACK);
		lblCat.setBounds(26, 164, 70, 13);
		getContentPane().add(lblCat);

		// ComboBox de categorías (deshabilitado)
		cmbCategoria = new JComboBox<>();
		cmbCategoria.setFont(new Font("Tahoma", Font.PLAIN, 15));
		cmbCategoria.setBounds(140, 162, 150, 21);
		cmbCategoria.setEnabled(false);
		getContentPane().add(cmbCategoria);

		// Obtener categorías desde la base de datos mediante DAO
		Dao dao = new DaoImplementacionMySql();
		map = dao.listarCategoria();

		// Añadir categorías al combo
		for (Categoria c : map.values())
			cmbCategoria.addItem(c);

		// BOTÓN GUARDAR
		btnGuardar = new JButton("Guardar");
		btnGuardar.setBounds(90, 220, 100, 30);
		btnGuardar.addActionListener(this);
		estiloBoton(btnGuardar);
		getContentPane().add(btnGuardar);

		// BOTÓN CANCELAR
		btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(210, 220, 100, 30);
		btnCancelar.addActionListener(this);
		estiloBoton(btnCancelar);
		getContentPane().add(btnCancelar);
	}

	/**
	 * Gestiona los eventos de los botones.
	 * 
	 * @param e Evento de acción generado por el usuario
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		// Si pulsa guardar → modificar producto
		if (e.getSource().equals(btnGuardar))
			modificar();

		// Si pulsa cancelar → cerrar ventana
		else if (e.getSource().equals(btnCancelar))
			dispose();
	}

	/**
	 * Estilo uniforme para los botones de la ventana.
	 */
	private void estiloBoton(JButton btn) {
		btn.setBackground(new Color(30, 144, 255)); // azul
		btn.setForeground(Color.WHITE);
		btn.setFocusPainted(false);
		btn.setBorderPainted(false);
		btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
	}

	/**
	 * Realiza la modificación de un producto.
	 * 
	 * <Incluye las siguientes validaciones:
	 * 
	 *   Campos obligatorios no vacíos
	 *   Código numérico
	 *   Precio con formato válido
	 *   Existencia del producto
	 * 
	 * Si todas las validaciones son correctas, se actualiza el producto
	 * mediante la capa de negocio.
	 */
	private void modificar() {

		// Obtener valores introducidos
		String codStr = textCod.getText().trim();
		String precioStr = textPrecio.getText().trim();

		// 1. Validar campos vacíos
		if (codStr.isEmpty() || precioStr.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Debes rellenar todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// 2. Validar que el código sea numérico
		if (!codStr.matches("\\d+")) {
			JOptionPane.showMessageDialog(this, "El código debe ser numérico", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// 3. Validar que el precio sea decimal válido
		if (!precioStr.matches("\\d+(\\.\\d+)?")) {
			JOptionPane.showMessageDialog(this, "El precio debe ser un número válido", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Convertir código a entero
		int cod = Integer.parseInt(codStr);

		// 4. Comprobar si el producto existe en la lógica de negocio
		if (!Main.existeProducto(cod)) {
			JOptionPane.showMessageDialog(this, "No existe un producto con ese código.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Crear objeto producto con los nuevos datos
		Producto p = new Producto();
		p.setCodP(cod);
		p.setDescripcion(textDescripcion.getText());
		p.setPrecio(Float.parseFloat(textPrecio.getText()));

		// Llamar a la capa de negocio para modificar el producto
		boolean ok = Main.modificarProducto(p);

		// Mostrar resultado de la operación
		if (ok)
			JOptionPane.showMessageDialog(this, "Producto modificado: " + p.getDescripcion(), "Éxito",
					JOptionPane.INFORMATION_MESSAGE);
		else
			JOptionPane.showMessageDialog(this, "Error al modificar.", "Error", JOptionPane.ERROR_MESSAGE);
	}
}
package vistas;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import main.DaoImplementacionMySql;
import main.Dao;
import modelo.Carrito;
import modelo.LineaCarrito;
import modelo.Sesion;
import utilidades.FondoPanel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * 
 * AUTOR: Ander
 * 
 * Ventana del carrito de compra.
 * 
 * Permite visualizar los productos añadidos al carrito, eliminar productos
 * y realizar la compra. También gestiona la actualización del inventario
 * del usuario en un fichero CSV tras completar la compra.
 */
public class VCarrito extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	/** Tabla donde se muestran los productos del carrito */
	private JTable tabla;

	/** Objeto carrito que contiene los productos seleccionados */
	private Carrito carrito;

	/** Campo que muestra el total de la compra */
	private JTextField textTotal;

	/** Campo que muestra el usuario actual */
	private JTextField textUsuario;

	/** Botón para confirmar la compra */
	private JButton btnComprar;

	/** Botón para eliminar un producto del carrito */
	private JButton btnEliminar;

	/**
	 * Constructor que inicializa la ventana del carrito.
	 * 
	 * Configura la interfaz gráfica, muestra los productos del carrito
	 * y prepara los eventos de interacción.
	 * 
	 * @param parent ventana padre
	 * @param modal indica si la ventana es modal
	 * @param carrito carrito con los productos seleccionados
	 */
	public VCarrito(JFrame parent, boolean modal, Carrito carrito) {
		super(parent, modal);
		this.carrito = carrito;

		setTitle("Carrito");
		setSize(454, 322);
		FondoPanel fondoPanel = new FondoPanel("/resources/fondo.jpg");
		setContentPane(fondoPanel);
		getContentPane().setLayout(null);

		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/icono.jpg")));

		// Tabla de productos
		tabla = new JTable();
		JScrollPane scroll = new JScrollPane(tabla);
		scroll.setBounds(10, 10, 360, 200);
		fondoPanel.add(scroll);

		// Botón eliminar producto
		btnEliminar = new JButton("Eliminar");
		btnEliminar.setBounds(10, 256, 84, 20);
		fondoPanel.add(btnEliminar);
		btnEliminar.addActionListener(this);
		
		// Botón realizar compra
		btnComprar = new JButton("Comprar");
		btnComprar.setBounds(10, 226, 84, 20);
		fondoPanel.add(btnComprar);
		btnComprar.addActionListener(this);

		// Campo total
		textTotal = new JTextField();
		textTotal.setEditable(false);
		textTotal.setBounds(334, 227, 96, 18);
		fondoPanel.add(textTotal);
		textTotal.setColumns(10);

		JLabel total = new JLabel("total:");
		total.setBounds(280, 230, 44, 12);
		total.setOpaque(true);
		total.setBackground(Color.white);
		fondoPanel.add(total);

		// Campo usuario
		textUsuario = new JTextField();
		textUsuario.setEditable(false);
		textUsuario.setColumns(10);
		textUsuario.setBounds(174, 227, 96, 18);
		fondoPanel.add(textUsuario);

		setLocationRelativeTo(parent);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		textUsuario.setText(Sesion.getUsuario().getIdentificacion());

		// Carga inicial de datos
		cargarDatos();
	}

	/**
	 * Carga los datos del carrito en la tabla y actualiza el total.
	 */
	private void cargarDatos() {
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn("Producto");
		model.addColumn("Cantidad");
		model.addColumn("Precio");

		for (LineaCarrito l : carrito.getLineas()) {
			model.addRow(new Object[] {
				l.getProducto().getDescripcion(),
				l.getCantidad(),
				String.format("%.2f", l.getProducto().getPrecio() * l.getCantidad())
			});
		}

		tabla.setModel(model);

		textTotal.setText(String.format("%.2f", carrito.calcularTotal()));
	}

	/**
	 * Gestiona los eventos de los botones del carrito.
	 * 
	 * Permite realizar la compra o eliminar productos seleccionados.
	 * 
	 * @param e evento de acción
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

	    // Acción de compra
	    if (e.getSource().equals(btnComprar)) {
	        try {
	            String dni = textUsuario.getText();

	            // Registro de la compra en base de datos
	            Dao dao = new DaoImplementacionMySql();
	            dao.realizarCompra(carrito, dni);

	            JOptionPane.showMessageDialog(this, "Compra realizada correctamente");

	            // Actualización del inventario en CSV
	            File fichero = new File(dni + ".csv");
	            Map<String, Integer> productos = new HashMap<>();

	            if (fichero.exists()) {
	                BufferedReader reader = new BufferedReader(new FileReader(fichero));
	                String linea;

	                while ((linea = reader.readLine()) != null) {
	                    String[] partes = linea.split(",");
	                    if (partes.length == 2) {
	                        String nombre = partes[0];
	                        int cantidad = Integer.parseInt(partes[1]);
	                        productos.put(nombre, cantidad);
	                    }
	                }
	                reader.close();
	            }

	            // Actualiza cantidades con los productos comprados
	            for (LineaCarrito l : carrito.getLineas()) {
	                String nombre = l.getProducto().getDescripcion();
	                int cantidad = l.getCantidad();

	                productos.put(nombre, productos.getOrDefault(nombre, 0) + cantidad);
	            }

	            BufferedWriter writer = new BufferedWriter(new FileWriter(fichero, false));

	            for (String nombre : productos.keySet()) {
	                writer.write(nombre + "," + productos.get(nombre));
	                writer.newLine();
	            }

	            writer.close();

	            // Limpia el carrito
	            carrito.getLineas().clear();
	            cargarDatos();

	        } catch (Exception ex) {
	            JOptionPane.showMessageDialog(this, ex.getMessage());
	        }

	    // Acción de eliminar producto
	    } else if (e.getSource().equals(btnEliminar)) {

	        int fila = tabla.getSelectedRow();

	        if (fila == -1) {
	            JOptionPane.showMessageDialog(this, "Selecciona un producto");
	            return;
	        }

	        carrito.getLineas().remove(fila);

	        cargarDatos();
	    }
	}
}
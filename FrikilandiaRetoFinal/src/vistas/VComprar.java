package vistas;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import main.Dao;
import main.DaoImplementacionMySql;
import main.Main;
import modelo.Carrito;
import modelo.Producto;
import modelo.Sesion;
import utilidades.FondoPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

/**
 * 
 * AUTOR: Ander
 * 
 * Ventana de compra de productos.
 * 
 * Permite al cliente visualizar los productos disponibles, añadirlos a un carrito
 * y acceder a la vista del carrito para gestionar la compra.
 * 
 * También muestra el dinero disponible del usuario y lo actualiza automáticamente.
 */
public class VComprar extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	/** Campo que muestra el usuario actual */
	private JTextField textUsuario;

	/** Botón para añadir productos al carrito */
	private JButton btnAñadir;

	/** Tabla donde se muestran los productos disponibles */
	private JTable tabla;

	/** Lista de productos obtenidos de la base de datos */
	private List<Producto> productos;

	/** Carrito donde se almacenan los productos seleccionados */
	private Carrito carrito;

	/** Botón para abrir la ventana del carrito */
	private JButton btnCarrito;

	/** Etiqueta que muestra el número de productos en el carrito */
	private JLabel lblContador;

	/** Campo que muestra el dinero disponible del cliente */
	private JTextField dineroField;

	/**
	 * Constructor que inicializa la ventana de compra.
	 * 
	 * Configura la interfaz gráfica, carga los productos desde la base de datos,
	 * crea la tabla y asigna los eventos necesarios.
	 * 
	 * También inicializa el carrito y un temporizador que actualiza el dinero del usuario.
	 * 
	 * @param parent ventana padre
	 * @param modal indica si la ventana es modal
	 */
	public VComprar(JFrame parent, boolean modal) {
		super(parent, modal);

		setTitle("Comprar");
		setSize(454, 293);
		FondoPanel fondoPanel = new FondoPanel("/resources/fondo.jpg");
		setContentPane(fondoPanel);
		getContentPane().setLayout(null);

		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/icono.jpg")));

		// Etiqueta contador de productos en carrito
		lblContador = new JLabel("Productos: 0");
		lblContador.setBounds(250, 210, 150, 25);
		lblContador.setOpaque(true);
		lblContador.setBackground(Color.white);
		fondoPanel.add(lblContador);

		// Botón añadir producto
		btnAñadir = new JButton("Añadir");
		btnAñadir.setBounds(10, 210, 100, 25);
		fondoPanel.add(btnAñadir);
		btnAñadir.addActionListener(this);

		// Botón ver carrito
		btnCarrito = new JButton("Ver carrito");
		btnCarrito.setBounds(125, 210, 100, 25);
		fondoPanel.add(btnCarrito);
		btnCarrito.addActionListener(this);

		// Campo usuario
		textUsuario = new JTextField();
		textUsuario.setEditable(false);
		textUsuario.setColumns(10);
		textUsuario.setBounds(10, 10, 96, 18);
		fondoPanel.add(textUsuario);

		setLocationRelativeTo(parent);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		textUsuario.setText(Sesion.getUsuario().getIdentificacion());

		// Campo dinero
		dineroField = new JTextField();
		dineroField.setColumns(10);
		dineroField.setBounds(116, 10, 96, 18);
		fondoPanel.add(dineroField);

		// Tabla de productos
		tabla = new JTable();
		JScrollPane scroll = new JScrollPane(tabla);
		scroll.setBounds(10, 50, 400, 150);
		fondoPanel.add(scroll);

		// Obtención de productos desde el DAO
		Dao dao = new DaoImplementacionMySql();
		try {
			productos = dao.obtenerProductos();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Configuración del modelo de la tabla
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn("Código");
		model.addColumn("Descripción");
		model.addColumn("Precio");

		for (Producto p : productos) {
		    model.addRow(new Object[]{
		        p.getCodP(),
		        p.getDescripcion(),
		        p.getPrecio()
		    });
		}

		tabla.setModel(model);

		// Inicialización del carrito
		carrito = new Carrito();

		// Carga inicial del dinero
		actualizarDinero();

		// Temporizador que actualiza el dinero cada 2 segundos
		Timer timer = new Timer(2000, new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        actualizarDinero();
		    }
		});
		timer.start();
	}

	/**
	 * Gestiona los eventos de los botones de la ventana.
	 * 
	 * Permite añadir productos al carrito o abrir la ventana del carrito.
	 * 
	 * @param e evento de acción
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(btnAñadir)) {
		    int fila = tabla.getSelectedRow();

		    if (fila != -1) {
		        int cod = (int) tabla.getValueAt(fila, 0);

		        // Busca el producto seleccionado y lo añade al carrito
		        for (Producto p : productos) {
		            if (p.getCodP() == cod) {
		                carrito.agregarProducto(p, 1);
		                break;
		            }
		        }

		        // Actualiza el contador de productos
		        lblContador.setText("Productos: " + carrito.getTotalProductos());
		    }
		} else if (e.getSource().equals(btnCarrito)) {

			// Abre la ventana del carrito
			VCarrito v = new VCarrito((JFrame) getParent(), true, carrito);
		    v.setVisible(true);
		}
	}

	/**
	 * Actualiza el dinero del cliente consultándolo desde la lógica principal.
	 */
	private void actualizarDinero() {
	    try {
	        String dni = Sesion.getUsuario().getIdentificacion();
	        double dinero = Main.obtenerDineroCliente(dni);
	        dineroField.setText(String.valueOf(dinero));
	    } catch (Exception e) {
	        dineroField.setText("Error");
	    }
	}
}
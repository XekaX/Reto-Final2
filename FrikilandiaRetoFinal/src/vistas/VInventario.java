package vistas;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import main.Dao;
import main.DaoImplementacionMySql;
import main.Main;
import modelo.Carta;
import modelo.Sesion;
import utilidades.FondoPanel;

/**
 * 
 * AUTOR: Ander
 * 
 * Ventana de inventario del cliente.
 * 
 * Permite visualizar los productos que posee el usuario, así como abrir sobres
 * para obtener cartas. También gestiona la lectura y escritura del inventario
 * en archivos CSV y muestra el dinero disponible del cliente.
 */
public class VInventario extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	/** Campo que muestra el usuario actual */
	private JTextField textUsuario;

	/** Botón para abrir sobres */
	private JButton btnAbrir;

	/** Tabla donde se muestra el inventario */
	private JTable tabla;

	/** Modelo de datos de la tabla */
	private DefaultTableModel modelo;

	/** Campo que muestra el dinero del cliente */
	private JTextField dineroField;

	/**
	 * Constructor que inicializa la ventana de inventario.
	 * 
	 * Configura la interfaz gráfica, carga los datos del inventario desde un archivo,
	 * crea la tabla y asigna los eventos necesarios.
	 * 
	 * También inicia un temporizador que actualiza el dinero del cliente periódicamente.
	 * 
	 * @param parent ventana padre
	 * @param modal indica si la ventana es modal
	 */
	public VInventario(JFrame parent, boolean modal) {
		super(parent, modal);

		setTitle("Inventario");
		setSize(454, 293);
		FondoPanel fondoPanel = new FondoPanel("/resources/fondo.jpg");
		setContentPane(fondoPanel);
		getContentPane().setLayout(null);

		setLocationRelativeTo(parent);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/icono.jpg")));

		// Obtiene el usuario actual y carga su inventario
		String usuario = Sesion.getUsuario().getIdentificacion();
		List<String[]> productos = leerInventario(usuario);

		// Configuración de la tabla
		String[] columnas = { "Producto", "Cantidad" };
		modelo = new DefaultTableModel(columnas, 0);

		for (String[] fila : productos) {
			modelo.addRow(fila);
		}

		tabla = new JTable(modelo);
		JScrollPane scroll = new JScrollPane(tabla);
		scroll.setBounds(10, 40, 420, 200);
		fondoPanel.add(scroll);

		// Campo usuario
		textUsuario = new JTextField();
		textUsuario.setEditable(false);
		textUsuario.setBounds(10, 10, 96, 18);
		fondoPanel.add(textUsuario);
		textUsuario.setText(usuario);

		// Botón abrir sobre
		btnAbrir = new JButton("Abrir sobre");
		btnAbrir.setBounds(320, 10, 110, 20);
		fondoPanel.add(btnAbrir);

		// Campo dinero
		dineroField = new JTextField();
		dineroField.setColumns(10);
		dineroField.setBounds(116, 10, 96, 18);
		fondoPanel.add(dineroField);

		btnAbrir.addActionListener(this);

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
	 * Gestiona el evento del botón "Abrir sobre".
	 * 
	 * Comprueba que haya un producto seleccionado y que haya cantidad disponible.
	 * Si es un sobre válido, lo abre, obtiene cartas desde la base de datos
	 * y actualiza tanto la interfaz como los archivos CSV.
	 * 
	 * @param e evento de acción
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(btnAbrir)) {
			int fila = tabla.getSelectedRow();

			if (fila == -1) {
				JOptionPane.showMessageDialog(null, "Selecciona un producto");
				return;
			}

			try {
				int cantidad = Integer.parseInt((String) modelo.getValueAt(fila, 1));

				if (cantidad <= 0) {
					JOptionPane.showMessageDialog(null, "No tienes sobres disponibles");
					return;
				}

				String producto = (String) modelo.getValueAt(fila, 0);
				int codProducto = obtenerCodigoProducto(producto);

				if (codProducto == -1) {
				    JOptionPane.showMessageDialog(null, "Este producto no es un sobre");
				    return;
				}

				// Llamada al DAO para abrir el sobre
				Dao dao = new DaoImplementacionMySql();
				List<Carta> cartas = dao.abrirSobre(codProducto, Sesion.getUsuario().getIdentificacion());

				// Muestra las cartas obtenidas
				for (Carta c : cartas) {
				    String mensaje = "🎴 Carta obtenida:\n\n" +
				            "Nombre: " + c.getNombre() + "\n" +
				            "Tipo: " + c.getTipo() + "\n" +
				            "Rareza: " + (c.isFoil() ? "FOIL ✨" : "NORMAL") + "\n" +
				            "Precio: " + c.getPrecio();

				    JOptionPane.showMessageDialog(this, mensaje);
				}

				// Guarda las cartas en CSV
				guardarCartasCSV(Sesion.getUsuario().getIdentificacion(), cartas);

				// Actualiza la cantidad en la tabla
				modelo.setValueAt(String.valueOf(cantidad - 1), fila, 1);

				// Actualiza el archivo de inventario
				actualizarInventarioCSV();

				JOptionPane.showMessageDialog(null, "¡Sobre abierto!");

			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage());
			}
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

	/**
	 * Guarda el inventario actual en un archivo CSV.
	 */
	private void actualizarInventarioCSV() {

		String ruta = "C:\\Users\\ander\\Desktop\\ClaseTrabajos\\PGR\\eclipse-workspace\\2EVA\\FrikilandiaRetoFinal\\79227927B.csv";

		try (PrintWriter pw = new PrintWriter(new FileWriter(ruta))) {

			for (int i = 0; i < modelo.getRowCount(); i++) {
				pw.println(modelo.getValueAt(i, 0) + "," + modelo.getValueAt(i, 1));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Lee el inventario del usuario desde un archivo CSV.
	 * 
	 * @param usuario identificador del usuario
	 * @return lista de productos con su cantidad
	 */
	private List<String[]> leerInventario(String usuario) {
		List<String[]> datos = new ArrayList<>();

		String ruta = "C:\\Users\\ander\\Desktop\\ClaseTrabajos\\PGR\\eclipse-workspace\\2EVA\\FrikilandiaRetoFinal\\79227927B.csv";

		try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
			String linea;

			while ((linea = br.readLine()) != null) {

				String[] partes = linea.split(",");

				if (partes.length == 2) {
					datos.add(new String[] { partes[0].trim(), partes[1].trim() });
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return datos;
	}

	/**
	 * Guarda las cartas obtenidas en un archivo CSV del usuario.
	 * 
	 * @param usuario identificador del usuario
	 * @param cartas lista de cartas obtenidas
	 */
	private void guardarCartasCSV(String usuario, List<Carta> cartas) {

		String ruta = "C:\\Users\\ander\\Desktop\\ClaseTrabajos\\PGR\\eclipse-workspace\\2EVA\\FrikilandiaRetoFinal\\"
				+ usuario + "cartas.csv";

		try (PrintWriter pw = new PrintWriter(new FileWriter(ruta, true))) {

			for (Carta c : cartas) {
				pw.println(c.getId_C() + "," + c.getNombre() + "," + c.getTipo() + ","
						+ (c.isFoil() ? "FOIL" : "NORMAL") + "," + c.getPrecio());
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Obtiene el código de producto asociado a un nombre de sobre.
	 * 
	 * @param nombre nombre del producto
	 * @return código del producto o -1 si no es válido
	 */
	private int obtenerCodigoProducto(String nombre) {

		switch (nombre) {
		case "Sobre Pokemon":
			return 101;
		case "Sobre Magic":
			return 102;
		case "Sobre One Piece":
			return 103;
		}

		return -1;
	}
}
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

public class VInventario extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JTextField textUsuario;
	private JButton btnAbrir;

	private JTable tabla;
	private DefaultTableModel modelo;
	private JTextField dineroField;

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

		String usuario = Sesion.getUsuario().getIdentificacion();
		List<String[]> productos = leerInventario(usuario);

		String[] columnas = { "Producto", "Cantidad" };
		modelo = new DefaultTableModel(columnas, 0);

		for (String[] fila : productos) {
			modelo.addRow(fila);
		}

		tabla = new JTable(modelo);
		JScrollPane scroll = new JScrollPane(tabla);
		scroll.setBounds(10, 40, 420, 200);
		fondoPanel.add(scroll);

		textUsuario = new JTextField();
		textUsuario.setEditable(false);
		textUsuario.setBounds(10, 10, 96, 18);
		fondoPanel.add(textUsuario);
		textUsuario.setText(usuario);

		btnAbrir = new JButton("Abrir sobre");
		btnAbrir.setBounds(320, 10, 110, 20);
		fondoPanel.add(btnAbrir);
		
		dineroField = new JTextField();
		dineroField.setColumns(10);
		dineroField.setBounds(116, 10, 96, 18);
		fondoPanel.add(dineroField);

		btnAbrir.addActionListener(this);
		
		actualizarDinero();
		
		Timer timer = new Timer(2000, new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        actualizarDinero();
		    }
		});
		timer.start();
	}

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

				Dao dao = new DaoImplementacionMySql();

				List<Carta> cartas = dao.abrirSobre(codProducto, Sesion.getUsuario().getIdentificacion());

				for (Carta c : cartas) {
				    String mensaje = "🎴 Carta obtenida:\n\n" +
				            "Nombre: " + c.getNombre() + "\n" +
				            "Tipo: " + c.getTipo() + "\n" +
				            "Rareza: " + (c.isFoil() ? "FOIL ✨" : "NORMAL") + "\n" +
				            "Precio: " + c.getPrecio();

				    JOptionPane.showMessageDialog(this, mensaje);
				}

				guardarCartasCSV(Sesion.getUsuario().getIdentificacion(), cartas);

				modelo.setValueAt(String.valueOf(cantidad - 1), fila, 1);

				actualizarInventarioCSV();

				JOptionPane.showMessageDialog(null, "¡Sobre abierto!");

			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage());
			}
		}
	}
	
	private void actualizarDinero() {
	    try {
	        String dni = Sesion.getUsuario().getIdentificacion();
	        double dinero = Main.obtenerDineroCliente(dni);
	        dineroField.setText(String.valueOf(dinero));
	    } catch (Exception e) {
	        dineroField.setText("Error");
	    }
	}

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
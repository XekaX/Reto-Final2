package vistas;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import utilidades.FondoPanel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.Timer;

import main.Main;
import modelo.Sesion;

/**
 * 
 * AUTOR: Ander
 * 
 * Ventana principal del cliente.
 * 
 * Permite al usuario acceder a las distintas funcionalidades del sistema,
 * como ver su inventario, comprar productos, consultar su colección,
 * visualizar el dinero disponible y revisar su historial de compras.
 * 
 * También actualiza automáticamente el dinero del cliente cada cierto tiempo.
 */
public class VMenuCliente extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 1L;

	/** Botón para abrir el inventario del cliente */
	private JButton btnInventario;

	/** Botón para acceder a la compra de productos */
	private JButton btnComprar;

	/** Campo que muestra el usuario actual (DNI) */
	private JTextField textUsuario;

	/** Campo que muestra el dinero disponible del cliente */
	private JTextField dineroField;

	/** Botón para ver la colección del cliente */
	private JButton btnColeccion;

	/** Botón para mostrar el total gastado */
	private JButton btnVerGastado;

	/** Botón para mostrar el historial de compras */
	private JButton btnHistorial;

	/**
	 * Constructor que inicializa la ventana principal del cliente.
	 * 
	 * Configura la interfaz gráfica, crea los botones y campos,
	 * y asigna los eventos necesarios para cada acción.
	 * 
	 * También inicia un temporizador que actualiza el dinero del cliente
	 * automáticamente cada 2 segundos.
	 */
	public VMenuCliente() {
		setTitle("Cliente");
		setSize(300, 200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/icono.jpg")));
		
		FondoPanel fondoPanel = new FondoPanel("/resources/fondo.jpg");
		setContentPane(fondoPanel);
		getContentPane().setLayout(null);
		
		btnVerGastado = new JButton("Ver gastado");
		btnVerGastado.setBounds(10, 70, 150, 20);
		fondoPanel.add(btnVerGastado);
		btnVerGastado.addActionListener(this);

		btnHistorial = new JButton("Historial");
		btnHistorial.setBounds(10, 100, 150, 20);
		fondoPanel.add(btnHistorial);
		btnHistorial.addActionListener(this);
		
		btnInventario = new JButton("Inventario");
		btnInventario.setBounds(180, 103, 96, 20);
		fondoPanel.add(btnInventario);
		btnInventario.addActionListener(this);
		
		btnComprar = new JButton("Comprar");
		btnComprar.setBounds(180, 133, 96, 20);
		fondoPanel.add(btnComprar);
		
		textUsuario = new JTextField();
		textUsuario.setEditable(false);
		textUsuario.setBounds(10, 10, 96, 18);
		fondoPanel.add(textUsuario);
		textUsuario.setColumns(10);

		btnComprar.addActionListener(this);

		// Muestra el DNI del usuario actual
		textUsuario.setText(Sesion.getUsuario().getIdentificacion());
		
		btnColeccion = new JButton("Coleccion");
		btnColeccion.setBounds(180, 73, 96, 20);
		fondoPanel.add(btnColeccion);
		btnColeccion.addActionListener(this);
		
		dineroField = new JTextField();
		dineroField.setBounds(116, 10, 96, 18);
		fondoPanel.add(dineroField);
		dineroField.setColumns(10);
		dineroField.setEditable(false);
		
		// Carga inicial del dinero del cliente
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
	 * Dependiendo del botón pulsado, abre nuevas ventanas
	 * o ejecuta acciones como mostrar el dinero gastado o el historial.
	 * 
	 * @param e evento de acción
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(btnInventario)) {
			VInventario invent = new VInventario(this, true);
			invent.setVisible(true);
		} else if (e.getSource().equals(btnComprar)) {
			VComprar comprar = new VComprar(this, true);
			comprar.setVisible(true);
		} else if (e.getSource().equals(btnColeccion)) {
			VColeccion coleccion = new VColeccion(this, true);
			coleccion.setVisible(true);
		} else if (e.getSource().equals(btnVerGastado)) {
			mostrarGastado();
		} else if (e.getSource().equals(btnHistorial)) {
			HistorialGastado();
		}
	}
	
	/**
	 * Actualiza el dinero del cliente consultándolo desde la lógica principal.
	 * 
	 * Si ocurre algún error, se muestra "Error" en el campo.
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
	 * Muestra en una ventana emergente el total de dinero gastado por el cliente.
	 */
	private void mostrarGastado() {
		String dni = Sesion.getUsuario().getIdentificacion();
	    double gastado = Main.obtenerTotalGastadoCliente(dni);

	    JOptionPane.showMessageDialog(this,
	        "Total gastado: " + gastado + "€",
	        "Información",
	        JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * Muestra el historial de compras del cliente.
	 * 
	 * Si no existen compras registradas, se informa al usuario.
	 */
	private void HistorialGastado() {
		String dni = Sesion.getUsuario().getIdentificacion();
	    List<String> historial = Main.obtenerHistorialCliente(dni);

	    if (historial.isEmpty()) {
	        JOptionPane.showMessageDialog(this,
	            "No hay compras registradas",
	            "Historial",
	            JOptionPane.INFORMATION_MESSAGE);
	        return;
	    }

	    StringBuilder sb = new StringBuilder();

	    for (String linea : historial) {
	        sb.append(linea).append("\n");
	    }

	    JOptionPane.showMessageDialog(this,
	        sb.toString(),
	        "Historial de compras",
	        JOptionPane.INFORMATION_MESSAGE);
	}
}

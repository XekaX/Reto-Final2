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

public class VMenuCliente extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	private JButton btnInventario;
	private JButton btnComprar;
	private JTextField textUsuario;
	private JTextField dineroField;
	private JButton btnColeccion;
	private JButton btnVerGastado;
	private JButton btnHistorial;
	
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
	
	private void actualizarDinero() {
	    try {
	        String dni = Sesion.getUsuario().getIdentificacion();
	        double dinero = Main.obtenerDineroCliente(dni);
	        dineroField.setText(String.valueOf(dinero));
	    } catch (Exception e) {
	        dineroField.setText("Error");
	    }
	}
	
	private void mostrarGastado() {
		String dni = Sesion.getUsuario().getIdentificacion();
	    double gastado = Main.obtenerTotalGastadoCliente(dni);

	    JOptionPane.showMessageDialog(this,
	        "Total gastado: " + gastado + "€",
	        "Información",
	        JOptionPane.INFORMATION_MESSAGE);
	}
	
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

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

public class VComprar extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JTextField textUsuario;
	private JButton btnAñadir;
	private JTable tabla;
	private List<Producto> productos;
	private Carrito carrito;
	private JButton btnCarrito;
	private JLabel lblContador;
	private JTextField dineroField;

	public VComprar(JFrame parent, boolean modal) {
		super(parent, modal);

		setTitle("Comprar");
		setSize(454, 293);
		FondoPanel fondoPanel = new FondoPanel("/resources/fondo.jpg");
		setContentPane(fondoPanel);
		getContentPane().setLayout(null);

		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/icono.jpg")));
		
		lblContador = new JLabel("Productos: 0");
		lblContador.setBounds(250, 210, 150, 25);
		lblContador.setOpaque(true);
		lblContador.setBackground(Color.white);
		fondoPanel.add(lblContador);
		
		btnAñadir = new JButton("Añadir");
		btnAñadir.setBounds(10, 210, 100, 25);
		fondoPanel.add(btnAñadir);
		btnAñadir.addActionListener(this);
		
		btnCarrito = new JButton("Ver carrito");
		btnCarrito.setBounds(125, 210, 100, 25);
		fondoPanel.add(btnCarrito);
		btnCarrito.addActionListener(this);
		
		textUsuario = new JTextField();
		textUsuario.setEditable(false);
		textUsuario.setColumns(10);
		textUsuario.setBounds(10, 10, 96, 18);
		fondoPanel.add(textUsuario);
		setLocationRelativeTo(parent);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		textUsuario.setText(Sesion.getUsuario().getIdentificacion());
		
		dineroField = new JTextField();
		dineroField.setColumns(10);
		dineroField.setBounds(116, 10, 96, 18);
		fondoPanel.add(dineroField);
		
		tabla = new JTable();
		JScrollPane scroll = new JScrollPane(tabla);
		scroll.setBounds(10, 50, 400, 150);
		fondoPanel.add(scroll);
		
		Dao dao = new DaoImplementacionMySql();
		try {
			productos = dao.obtenerProductos();
		} catch (Exception e) {
			e.printStackTrace();
		}

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
		
		carrito = new Carrito();
	
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
		if (e.getSource().equals(btnAñadir)) {
		    int fila = tabla.getSelectedRow();

		    if (fila != -1) {
		        int cod = (int) tabla.getValueAt(fila, 0);

		        for (Producto p : productos) {
		            if (p.getCodP() == cod) {
		                carrito.agregarProducto(p, 1);
		                break;
		            }
		        }

		        lblContador.setText("Productos: " + carrito.getTotalProductos());
		    }
		} else if (e.getSource().equals(btnCarrito)) {
			VCarrito v = new VCarrito((JFrame) getParent(), true, carrito);
		    v.setVisible(true);
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
}
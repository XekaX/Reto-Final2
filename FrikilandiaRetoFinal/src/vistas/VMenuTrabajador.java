package vistas;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import excepciones.CodigoException;
import main.Dao;
import main.DaoImplementacionMySql;
import main.Main;
import modelo.Categoria;
import modelo.Producto;
import modelo.TipoProducto;
import utilidades.FondoPanel;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;

public class VMenuTrabajador extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JButton btnCarta;
	
	private Map<Integer, Categoria> map;
	private JTextField textDescripcion;
	private JTextField textPrecio;
	private JTextField textCod;
	private JComboBox<Categoria> cmbCategoria;
	private JButton btnAñadir;
	private JButton btnModificar;
	private JButton btnEliminar;
	private JButton btnVisualizar;

	public VMenuTrabajador() {
		setTitle("Trabajador");
		setSize(345, 332);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/icono.jpg")));

		FondoPanel fondoPanel = new FondoPanel("/resources/fondo.jpg");
		setContentPane(fondoPanel);
		getContentPane().setLayout(null);
		
		JLabel lblCod = new JLabel("Código:");
		lblCod.setBounds(26, 34, 64, 13);
		lblCod.setOpaque(true);
		lblCod.setBackground(Color.white);
		getContentPane().add(lblCod);

		JLabel lblPrecio = new JLabel("Precio:");
		lblPrecio.setBounds(26, 80, 64, 13);
		lblPrecio.setOpaque(true);
		lblPrecio.setBackground(Color.white);
		getContentPane().add(lblPrecio);

		JLabel lblDescripcion = new JLabel("Descripción:");
		lblDescripcion.setBounds(26, 121, 64, 13);
		lblDescripcion.setOpaque(true);
		lblDescripcion.setBackground(Color.white);
		getContentPane().add(lblDescripcion);

		JLabel lblCategoria = new JLabel("Categoría:");
		lblCategoria.setBounds(26, 164, 64, 13);
		lblCategoria.setOpaque(true);
		lblCategoria.setBackground(Color.white);
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

		Dao dao = new DaoImplementacionMySql();
		map = dao.listarCategoria();

		for (Categoria c : map.values()) {
			cmbCategoria.addItem(c);
		}
		
		ImageIcon icono = null;
		URL url = getClass().getResource("/resources/añadirProducto.png");

		Image img = new ImageIcon(url).getImage();
		Image imgEscalada = img.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
		icono = new ImageIcon(imgEscalada);

		btnAñadir = new JButton("Añadir", icono);
		btnAñadir.setBounds(10, 212, 120, 30);
		btnAñadir.addActionListener(this);
		btnAñadir.setFocusPainted(false);
		getContentPane().add(btnAñadir);
		
		ImageIcon iconoModificar = null;
		URL urlModificar = getClass().getResource("/resources/modificar.jpg");
		
		Image imgM = new ImageIcon(urlModificar).getImage();
	    Image imgMEscalada = imgM.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
	    iconoModificar = new ImageIcon(imgMEscalada);

		btnModificar = new JButton("Modificar", iconoModificar);
		btnModificar.setBounds(135, 212, 120, 30);
		btnModificar.addActionListener(this);
		getContentPane().add(btnModificar);
		
		ImageIcon iconoEliminar = null;
		URL urlEliminar = getClass().getResource("/resources/eliminar.png");
		
		Image imgE = new ImageIcon(urlEliminar).getImage();
	    Image imgEEscalada = imgE.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
	    iconoEliminar = new ImageIcon(imgEEscalada);

		btnEliminar = new JButton("Eliminar", iconoEliminar);
		btnEliminar.setBounds(10, 252, 120, 30);
		btnEliminar.addActionListener(this);
		getContentPane().add(btnEliminar);
		
		ImageIcon iconoVisualizar = null;
		URL urlVisualizar = getClass().getResource("/resources/visualizar.png");
		
		Image imgV = new ImageIcon(urlVisualizar).getImage();
	    Image imgVEscalada = imgV.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
	    iconoVisualizar = new ImageIcon(imgVEscalada);

		btnVisualizar = new JButton("Visualizar", iconoVisualizar);
		btnVisualizar.setBounds(135, 252, 120, 30);
		btnVisualizar.addActionListener(this);
		getContentPane().add(btnVisualizar);
		
		btnCarta = new JButton("Carta");
		btnCarta.setBounds(237, 31, 84, 20);
		fondoPanel.add(btnCarta);
		btnCarta.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(btnCarta)) {
			VCartas carta = new VCartas(null, true);
			carta.setVisible(true);
		} else if (e.getSource().equals(btnAñadir)) {
			alta();
		} else if (e.getSource().equals(btnEliminar)) {
			eliminar();
		} else if (e.getSource().equals(btnModificar)) {
			VentanaModificar m = new VentanaModificar();
			m.setVisible(true);
		} else if (e.getSource().equals(btnVisualizar)) {
			VentanaVisualizar v = new VentanaVisualizar();
			v.setVisible(true);
		}
	}
	
	private void alta() {
		Producto p = new Producto();
		try {
			String textoCod = textCod.getText();
			if (!textoCod.matches("\\d+")) {
				throw new CodigoException("Debes introducir un código numérico");
			}

			p.setCodP(Integer.parseInt(textoCod));
			p.setPrecio(Float.parseFloat(textPrecio.getText()));
			p.setDescripcion(textDescripcion.getText());

			Categoria cat = (Categoria) cmbCategoria.getSelectedItem();
			p.setIdCategoria(cat.getId_categoria());

			if (cat.getNombre().equalsIgnoreCase("Cartas"))
				p.setTipo(TipoProducto.CARTAS);
			else if (cat.getNombre().equalsIgnoreCase("Comics"))
				p.setTipo(TipoProducto.COMICS);

			boolean ok = Main.añadirProducto(p);

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
			dispose();
		} else {
			JOptionPane.showMessageDialog(this, "Error al eliminar");
		}
	}
}

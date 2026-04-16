package vistas;

import java.awt.*;
import java.awt.event.*;
import java.util.Map;
import javax.swing.*;
import main.Dao;
import main.DaoImplementacionMySql;
import modelo.Categoria;
import modelo.Producto;
import main.Main;

public class VentanaModificar extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JTextField textCod, textPrecio, textDescripcion;
	private JComboBox<Categoria> cmbCategoria;
	private Map<Integer, Categoria> map;
	private JButton btnGuardar, btnCancelar;

	public VentanaModificar() {
		setTitle("Modificar Producto");
		setSize(400, 300);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);

		JLabel lblCod = new JLabel("Código:");
		lblCod.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblCod.setBounds(26, 29, 70, 24);
		getContentPane().add(lblCod);

		textCod = new JTextField();
		textCod.setBounds(120, 34, 97, 19);
		textCod.setColumns(10);
		getContentPane().add(textCod);

		JLabel lblPrecio = new JLabel("Precio:");
		lblPrecio.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblPrecio.setBounds(26, 80, 64, 13);
		getContentPane().add(lblPrecio);

		textPrecio = new JTextField();
		textPrecio.setBounds(120, 79, 97, 19);
		textPrecio.setColumns(10);
		getContentPane().add(textPrecio);

		JLabel lblDesc = new JLabel("Descripción:");
		lblDesc.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblDesc.setBounds(26, 121, 97, 13);
		getContentPane().add(lblDesc);

		textDescripcion = new JTextField();
		textDescripcion.setBounds(120, 120, 96, 19);
		textDescripcion.setColumns(10);
		textDescripcion.setEditable(false);
		getContentPane().add(textDescripcion);

		JLabel lblCat = new JLabel("Categoría:");
		lblCat.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblCat.setBounds(26, 164, 70, 13);
		getContentPane().add(lblCat);

		cmbCategoria = new JComboBox<>();
		cmbCategoria.setFont(new Font("Tahoma", Font.PLAIN, 15));
		cmbCategoria.setBounds(120, 162, 97, 21);
		cmbCategoria.setEnabled(false);
		getContentPane().add(cmbCategoria);

		Dao dao = new DaoImplementacionMySql();
		map = dao.listarCategoria();
		for (Categoria c : map.values())
			cmbCategoria.addItem(c);

		btnGuardar = new JButton("Guardar");
		btnGuardar.setBounds(105, 222, 85, 21);
		btnGuardar.addActionListener(this);
		getContentPane().add(btnGuardar);

		btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(200, 222, 85, 21);
		btnCancelar.addActionListener(this);
		getContentPane().add(btnCancelar);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(btnGuardar))
			modificar();
		else if (e.getSource().equals(btnCancelar))
			dispose();
	}

	private void modificar() {
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

		int cod = Integer.parseInt(codStr);

		// 4. Comprobar existencia
		if (!Main.existeProducto(cod)) {
			JOptionPane.showMessageDialog(this, "No existe un producto con ese código.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		Producto p = new Producto();
		p.setCodP(cod);
		p.setDescripcion(textDescripcion.getText());
		p.setPrecio(Float.parseFloat(textPrecio.getText()));
		boolean ok = Main.modificarProducto(p);
		if (ok)
			JOptionPane.showMessageDialog(this, "Producto modificado: " + p.getDescripcion(), "Éxito",
					JOptionPane.INFORMATION_MESSAGE);
		else
			JOptionPane.showMessageDialog(this, "Error al modificar.", "Error", JOptionPane.ERROR_MESSAGE);
	}
}
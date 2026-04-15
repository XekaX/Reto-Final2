package vistas;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import utilidades.FondoPanel;

import javax.swing.JTextField;
import javax.swing.WindowConstants;

import modelo.Cliente;
import main.Dao;
import main.DaoImplementacionMySql;

import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class VRegistro extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JTextField nombreField;
	private JPasswordField contrasenaField;
	private JButton registrarButton;
	private JTextField dniField;
	private JTextField telefonoField;
	private JPasswordField cntraseniaConfField;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_2;
	private JLabel lblNewLabel_3;
	private JLabel lblNewLabel_4;

	public VRegistro(JFrame parent, boolean modal) {
		super(parent, modal);

		setTitle("Registro");
		setSize(454, 293);
		FondoPanel fondoPanel = new FondoPanel("/resources/fondo.jpg");
		setContentPane(fondoPanel);
		getContentPane().setLayout(null);
		setLocationRelativeTo(parent);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/icono.jpg")));

		nombreField = new JTextField();
		nombreField.setBounds(280, 10, 150, 20);
		getContentPane().add(nombreField);

		contrasenaField = new JPasswordField();
		contrasenaField.setBounds(280, 100, 150, 20);
		getContentPane().add(contrasenaField);

		registrarButton = new JButton("Registrar");
		registrarButton.setBounds(280, 226, 150, 20);
		getContentPane().add(registrarButton);

		registrarButton.addActionListener(this);

		getRootPane().setDefaultButton(registrarButton);

		dniField = new JTextField();
		dniField.setBounds(280, 40, 150, 20);
		fondoPanel.add(dniField);
		dniField.setColumns(10);

		telefonoField = new JTextField();
		telefonoField.setBounds(280, 70, 150, 20);
		fondoPanel.add(telefonoField);
		telefonoField.setColumns(10);

		cntraseniaConfField = new JPasswordField();
		cntraseniaConfField.setBounds(280, 130, 150, 20);
		fondoPanel.add(cntraseniaConfField);

		lblNewLabel = new JLabel("nombre");
		lblNewLabel.setBounds(143, 13, 127, 12);
		lblNewLabel.setOpaque(true);
		lblNewLabel.setBackground(Color.white);
		fondoPanel.add(lblNewLabel);

		lblNewLabel_1 = new JLabel("dni");
		lblNewLabel_1.setBounds(143, 43, 127, 12);
		lblNewLabel_1.setOpaque(true);
		lblNewLabel_1.setBackground(Color.white);
		fondoPanel.add(lblNewLabel_1);

		lblNewLabel_2 = new JLabel("telefono");
		lblNewLabel_2.setBounds(143, 73, 127, 12);
		lblNewLabel_2.setOpaque(true);
		lblNewLabel_2.setBackground(Color.white);
		fondoPanel.add(lblNewLabel_2);

		lblNewLabel_3 = new JLabel("contraseña");
		lblNewLabel_3.setBounds(143, 103, 127, 12);
		lblNewLabel_3.setOpaque(true);
		lblNewLabel_3.setBackground(Color.white);
		fondoPanel.add(lblNewLabel_3);

		lblNewLabel_4 = new JLabel("confirmar contraseña");
		lblNewLabel_4.setBounds(143, 133, 127, 12);
		lblNewLabel_4.setOpaque(true);
		lblNewLabel_4.setBackground(Color.white);
		fondoPanel.add(lblNewLabel_4);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == registrarButton) {

			String nombre = nombreField.getText();
			String dni = dniField.getText();
			String telefono = telefonoField.getText();
			String password = new String(contrasenaField.getPassword());
			String confirmPassword = new String(cntraseniaConfField.getPassword());

			if (nombre.isEmpty() || dni.isEmpty() || telefono.isEmpty() || password.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios");
				return;
			}

			if (!telefono.matches("^\\+?\\d{9,15}$")) {
				JOptionPane.showMessageDialog(this, "Teléfono inválido");
				return;
			}

			if (!password.equals(confirmPassword)) {
				JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden");
				return;
			}

			try {
				Cliente cliente = new Cliente();
				cliente.setNombre(nombre);
				cliente.setIdentificacion(dni);
				cliente.setTelefono(telefono);
				cliente.setContrasenia(password);

				Dao dao = new DaoImplementacionMySql();
				dao.registrarCliente(cliente);

				JOptionPane.showMessageDialog(this, "Cliente registrado correctamente");

				dispose();

			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
			}
		}
	}
}

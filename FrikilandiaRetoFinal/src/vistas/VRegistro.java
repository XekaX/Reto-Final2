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

/**
 * 
 * AUTOR: Ander
 * 
 * Ventana de registro de clientes.
 * 
 * Se encarga de mostrar un formulario donde el usuario introduce sus datos
 * (nombre, DNI, teléfono y contraseña) y gestiona el proceso de registro.
 * 
 * Incluye la validación de los datos introducidos y la comunicación con la capa
 * de acceso a datos (DAO) para guardar el cliente en la base de datos.
 */
public class VRegistro extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	/** Campo donde el usuario introduce su nombre */
	private JTextField nombreField;

	/** Campo donde el usuario introduce la contraseña */
	private JPasswordField contrasenaField;

	/** Botón que lanza el proceso de registro */
	private JButton registrarButton;

	/** Campo donde el usuario introduce su DNI */
	private JTextField dniField;

	/** Campo donde el usuario introduce su teléfono */
	private JTextField telefonoField;

	/** Campo donde el usuario confirma la contraseña */
	private JPasswordField cntraseniaConfField;

	/** Etiquetas que describen cada campo del formulario */
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_2;
	private JLabel lblNewLabel_3;
	private JLabel lblNewLabel_4;

	/**
	 * Constructor que inicializa la ventana de registro.
	 * 
	 * Configura la interfaz gráfica, crea los campos de entrada, posiciona los
	 * componentes y asigna los eventos necesarios.
	 * 
	 * @param parent ventana padre
	 * @param modal  indica si la ventana es modal
	 */
	public VRegistro(JFrame parent, boolean modal) {
		super(parent, modal);

		// Configuración básica de la ventana
		setTitle("Registro");
		setSize(454, 293);
		FondoPanel fondoPanel = new FondoPanel("/resources/fondo.jpg");
		setContentPane(fondoPanel);
		getContentPane().setLayout(null);
		setLocationRelativeTo(parent);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		// Icono de la ventana
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/icono.jpg")));

		// Creación de campos de entrada
		nombreField = new JTextField();
		nombreField.setBounds(280, 10, 150, 20);
		getContentPane().add(nombreField);

		contrasenaField = new JPasswordField();
		contrasenaField.setBounds(280, 100, 150, 20);
		getContentPane().add(contrasenaField);

		// Botón de registro y asignación de evento
		registrarButton = new JButton("Registrar");
		registrarButton.setBounds(280, 226, 150, 20);
		getContentPane().add(registrarButton);
		registrarButton.addActionListener(this);

		// Permite pulsar Enter para activar el botón
		getRootPane().setDefaultButton(registrarButton);

		// Resto de campos del formulario
		dniField = new JTextField();
		dniField.setBounds(280, 40, 150, 20);
		fondoPanel.add(dniField);

		telefonoField = new JTextField();
		telefonoField.setBounds(280, 70, 150, 20);
		fondoPanel.add(telefonoField);

		cntraseniaConfField = new JPasswordField();
		cntraseniaConfField.setBounds(280, 130, 150, 20);
		fondoPanel.add(cntraseniaConfField);

		// Etiquetas descriptivas
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

	/**
	 * Método que gestiona el evento del botón de registro.
	 * 
	 * Recoge los datos introducidos, realiza validaciones básicas y, si todo es
	 * correcto, crea un cliente y lo guarda en la base de datos. También muestra
	 * mensajes al usuario según el resultado.
	 * 
	 * @param e evento de acción
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == registrarButton) {

			// Obtención de datos del formulario
			String nombre = nombreField.getText();
			String dni = dniField.getText();
			String telefono = telefonoField.getText();
			String password = new String(contrasenaField.getPassword());
			String confirmPassword = new String(cntraseniaConfField.getPassword());

			// Validación de campos vacíos
			if (nombre.isEmpty() || dni.isEmpty() || telefono.isEmpty() || password.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios");
				return;
			}

			// Validación del formato del teléfono
			if (!telefono.matches("^\\+?\\d{9,15}$")) {
				JOptionPane.showMessageDialog(this, "Teléfono inválido");
				return;
			}

			// Validación de coincidencia de contraseñas
			if (!password.equals(confirmPassword)) {
				JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden");
				return;
			}

			try {
				// Creación del objeto cliente con los datos introducidos
				Cliente cliente = new Cliente();
				cliente.setNombre(nombre);
				cliente.setIdentificacion(dni);
				cliente.setTelefono(telefono);
				cliente.setContrasenia(password);

				// Llamada al DAO para guardar el cliente
				Dao dao = new DaoImplementacionMySql();
				dao.registrarCliente(cliente);

				// Mensaje de éxito
				JOptionPane.showMessageDialog(this, "Cliente registrado correctamente");

				// Cierre de la ventana
				dispose();

			} catch (Exception ex) {
				// Manejo de errores
				JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
			}
		}
	}
}
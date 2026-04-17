package vistas;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import utilidades.FondoPanel;

import javax.swing.JTextField;

import excepciones.LoginException;
import main.Main;
import modelo.Cliente;
import modelo.Sesion;
import modelo.Tipo;
import modelo.Trabajador;
import modelo.Usuario;

import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.Color;

/**
 * 
 * AUTOR: Ander
 * 
 * Ventana de inicio de sesión.
 * 
 * Permite al usuario introducir sus credenciales para acceder al sistema.
 * 
 * Según el tipo de usuario autenticado (cliente o trabajador),
 * redirige a la ventana correspondiente.
 * 
 * También permite acceder a la ventana de registro.
 */
public class Login extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	/** Campo donde el usuario introduce su identificador */
	private JTextField UsuarioTextField;

	/** Campo donde el usuario introduce la contraseña */
	private JPasswordField ContraseñaField;

	/** Botón para iniciar sesión */
	private JButton btnIniciarSesion;

	/** Botón para abrir la ventana de registro */
	private JButton btnRegistrarse;

	/**
	 * Constructor que inicializa la ventana de login.
	 * 
	 * Configura la interfaz gráfica, crea los campos de entrada
	 * y asigna los eventos a los botones.
	 */
	public Login() {
		setTitle("Login");
		setSize(300, 200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/icono.jpg")));

		setContentPane(new FondoPanel("/resources/fondo.jpg"));
		getContentPane().setLayout(null);

		// Campo usuario
		UsuarioTextField = new JTextField();
		UsuarioTextField.setBounds(130, 30, 156, 18);
		getContentPane().add(UsuarioTextField);

		// Campo contraseña
		ContraseñaField = new JPasswordField();
		ContraseñaField.setBounds(130, 58, 156, 18);
		getContentPane().add(ContraseñaField);

		// Etiquetas
		JLabel lblNewLabel = new JLabel("Usuario:");
		lblNewLabel.setBounds(28, 33, 92, 12);
		lblNewLabel.setOpaque(true);
		lblNewLabel.setBackground(Color.white);
		getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Contraseña:");
		lblNewLabel_1.setBounds(28, 61, 92, 12);
		lblNewLabel_1.setOpaque(true);
		lblNewLabel_1.setBackground(Color.white);
		getContentPane().add(lblNewLabel_1);

		// Botón iniciar sesión
		btnIniciarSesion = new JButton("Iniciar sesion");
		btnIniciarSesion.setBounds(136, 84, 150, 25);
		getContentPane().add(btnIniciarSesion);
		btnIniciarSesion.addActionListener(this);

		// Botón registrarse
		btnRegistrarse = new JButton("Registrarse");
		btnRegistrarse.setBounds(136, 119, 150, 25);
		getContentPane().add(btnRegistrarse);
		btnRegistrarse.addActionListener(this);

		// Permite usar Enter para iniciar sesión
		getRootPane().setDefaultButton(btnIniciarSesion);
	}

	/**
	 * Gestiona los eventos de los botones de la ventana.
	 * 
	 * Permite iniciar sesión o abrir la ventana de registro.
	 * 
	 * @param e evento de acción
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		// Acción de iniciar sesión
		if (e.getSource().equals(btnIniciarSesion)) {

			String usuario = UsuarioTextField.getText();
			String contrasenia = new String(ContraseñaField.getPassword());

			// Creación del objeto usuario con las credenciales
			Usuario us = new Cliente();
			us.setIdentificacion(usuario);
			us.setContrasenia(contrasenia);

			try {
				// Llamada a la lógica de login
				Usuario usuarioLogueado = Main.login(us);

				// Guarda la sesión
				Sesion.setUsuario(usuarioLogueado);

				JOptionPane.showMessageDialog(this,
						"Login correcto: " + usuarioLogueado.getIdentificacion());

				// Redirección según tipo de usuario
				if (usuarioLogueado instanceof Cliente) {

				    VMenuCliente menuC = new VMenuCliente();
				    menuC.setVisible(true);

				} else if (usuarioLogueado instanceof Trabajador) {

				    Trabajador t = (Trabajador) usuarioLogueado;

				    if (t.getTipo() == Tipo.ADMIN) {
				        VMenuAdmin menuA = new VMenuAdmin();
				        menuA.setVisible(true);
				    } else {
				        VMenuTrabajador menuT = new VMenuTrabajador();
				        menuT.setVisible(true);
				    }
				}

				// Cierra la ventana de login
				this.dispose();

			} catch (LoginException ex) {
				// Error en las credenciales
				JOptionPane.showMessageDialog(this,
						ex.getMessage(),
						"Error de Login",
						JOptionPane.ERROR_MESSAGE);
			}
		}

		// Acción de abrir registro
		else if (e.getSource().equals(btnRegistrarse)) {
			VRegistro registro = new VRegistro(this, true);
			registro.setVisible(true);
		}
	}
}
package vista;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import util.FondoPanel;

import javax.swing.JTextField;

import excepciones.LoginException;
import principal.Principal;
import modelo.Cliente;
import modelo.Usuario;

import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;

public class Login extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JTextField UsuarioTextField;
	private JPasswordField ContraseñaField;
	private JButton btnIniciarSesion;
	private JButton btnRegistrarse;

	/**
	 * Create the frame.
	 */
	public Login() {
		setTitle("Login");
		setSize(300, 200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/berserk.jpeg")));

		setContentPane(new FondoPanel("/resources/loki.jpeg"));
		setLayout(null);

		UsuarioTextField = new JTextField();
		UsuarioTextField.setBounds(130, 30, 156, 18);
		add(UsuarioTextField);

		ContraseñaField = new JPasswordField();
		ContraseñaField.setBounds(130, 58, 156, 18);
		add(ContraseñaField);

		JLabel lblNewLabel = new JLabel("Usuario:");
		lblNewLabel.setBounds(28, 33, 92, 12);
		add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Contraseña:");
		lblNewLabel_1.setBounds(28, 61, 92, 12);
		add(lblNewLabel_1);

		btnIniciarSesion = new JButton("Iniciar sesion");
		btnIniciarSesion.setBounds(136, 84, 150, 25);
		add(btnIniciarSesion);
		btnIniciarSesion.addActionListener(this);

		btnRegistrarse = new JButton("Registrarse");
		btnRegistrarse.setBounds(136, 119, 150, 25);
		add(btnRegistrarse);
		btnRegistrarse.addActionListener(this);

		getRootPane().setDefaultButton(btnIniciarSesion);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource().equals(btnIniciarSesion)) {

			String usuario = UsuarioTextField.getText();
			String contrasenia = new String(ContraseñaField.getPassword());

			Usuario us = new Cliente();
			us.setIdentificacion(usuario);
			us.setContrasenia(contrasenia);

			try {

				Usuario usuarioLogueado = Principal.login(us);

				JOptionPane.showMessageDialog(this, "Login correcto: " + usuarioLogueado.getIdentificacion());

				if (usuarioLogueado instanceof Cliente) {

					VentanaCliente menuC = new VentanaCliente();
					menuC.setVisible(true);

				} else {

					VentanaTrabajador menuT = new VentanaTrabajador();
					menuT.setVisible(true);

				}
				this.dispose();

			} catch (LoginException ex) {

				JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de Login", JOptionPane.ERROR_MESSAGE);

			}
		}
	}
}
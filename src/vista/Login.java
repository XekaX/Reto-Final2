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
import java.awt.Font;
import java.awt.Color;

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
		setSize(445, 267);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/berserk.jpeg")));

		setContentPane(new FondoPanel("/resources/guts.png"));
		getContentPane().setLayout(null);

		UsuarioTextField = new JTextField();
		UsuarioTextField.setBounds(153, 30, 156, 18);
		getContentPane().add(UsuarioTextField);

		ContraseñaField = new JPasswordField();
		ContraseñaField.setBounds(153, 60, 156, 18);
		getContentPane().add(ContraseñaField);

		JLabel lblNewLabel = new JLabel("Usuario:");
		lblNewLabel.setForeground(new Color(255, 255, 255));
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNewLabel.setBounds(52, 31, 92, 12);
		getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Contraseña:");
		lblNewLabel_1.setForeground(new Color(255, 255, 255));
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNewLabel_1.setBounds(52, 61, 92, 12);
		getContentPane().add(lblNewLabel_1);

		btnIniciarSesion = new JButton("Iniciar sesion");
		btnIniciarSesion.setBounds(153, 84, 150, 25);
		getContentPane().add(btnIniciarSesion);
		btnIniciarSesion.addActionListener(this);

		btnRegistrarse = new JButton("Registrarse");
		btnRegistrarse.setBounds(153, 117, 150, 25);
		getContentPane().add(btnRegistrarse);
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
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

public class Login extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JTextField UsuarioTextField;
	private JPasswordField ContraseñaField;
	private JButton btnIniciarSesion;
	private JButton btnRegistrarse;

	public Login() {
		setTitle("Login");
		setSize(300, 200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/icono.jpg")));

		setContentPane(new FondoPanel("/resources/fondo.jpg"));
		getContentPane().setLayout(null);

		UsuarioTextField = new JTextField();
		UsuarioTextField.setBounds(130, 30, 156, 18);
		getContentPane().add(UsuarioTextField);

		ContraseñaField = new JPasswordField();
		ContraseñaField.setBounds(130, 58, 156, 18);
		getContentPane().add(ContraseñaField);

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

		btnIniciarSesion = new JButton("Iniciar sesion");
		btnIniciarSesion.setBounds(136, 84, 150, 25);
		getContentPane().add(btnIniciarSesion);
		btnIniciarSesion.addActionListener(this);

		btnRegistrarse = new JButton("Registrarse");
		btnRegistrarse.setBounds(136, 119, 150, 25);
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
				Usuario usuarioLogueado = Main.login(us);
				Sesion.setUsuario(usuarioLogueado);
				JOptionPane.showMessageDialog(this, "Login correcto: " + usuarioLogueado.getIdentificacion());

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
				
				this.dispose();
			} catch (LoginException ex) {
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de Login", JOptionPane.ERROR_MESSAGE);
			}
		}
		
		else if (e.getSource().equals(btnRegistrarse)) {
			VRegistro registro = new VRegistro(this, true);
			registro.setVisible(true);
		}
	}
}
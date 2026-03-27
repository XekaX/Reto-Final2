package vistas;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import controlador.Main;
import excepciones.LoginException;
import modelo.Cliente;
import modelo.Tipo;
import modelo.Trabajador;
import modelo.Usuario;
import utilidades.FondoPanel;

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

        setIconImage(Toolkit.getDefaultToolkit().getImage(
                getClass().getResource("/resources/berserk.jpeg")));

        setContentPane(new FondoPanel("/resources/fondo.jpg"));
        setLayout(null);

        UsuarioTextField = new JTextField();
        UsuarioTextField.setBounds(130, 30, 156, 18);
        add(UsuarioTextField);

        ContraseñaField = new JPasswordField();
        ContraseñaField.setBounds(130, 58, 156, 18);
        add(ContraseñaField);

        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setBounds(28, 33, 92, 12);
        add(lblUsuario);

        JLabel lblPass = new JLabel("Contraseña:");
        lblPass.setBounds(28, 61, 92, 12);
        add(lblPass);

        btnIniciarSesion = new JButton("Iniciar sesión");
        btnIniciarSesion.setBounds(136, 84, 150, 25);
        add(btnIniciarSesion);

        btnRegistrarse = new JButton("Registrarse");
        btnRegistrarse.setBounds(136, 119, 150, 25);
        add(btnRegistrarse);

        btnIniciarSesion.addActionListener(this);
        btnRegistrarse.addActionListener(this);

        getRootPane().setDefaultButton(btnIniciarSesion);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // 🔹 LOGIN
        if (e.getSource() == btnIniciarSesion) {

            String usuario = UsuarioTextField.getText();
            String contrasenia = new String(ContraseñaField.getPassword());

            // 👇 Usuario genérico (IMPORTANTE)
            Usuario us = new Usuario(usuario, contrasenia) {};

            try {

                Usuario usuarioLogueado = Main.login(us);

                JOptionPane.showMessageDialog(this,
                        "Login correcto: " + usuarioLogueado.getIdentificacion());

                // 🔥 CONTROL DE ROLES
                if (usuarioLogueado instanceof Cliente) {

                    new VMenuCliente().setVisible(true);

                } else {

                    Trabajador t = (Trabajador) usuarioLogueado;

                    if (t.getTipo() == Tipo.ADMIN) {
                        new VMenuAdmin().setVisible(true);
                    } else {
                        new VMenuTrabajador().setVisible(true);
                    }
                }

                this.dispose();

            } catch (LoginException ex) {

                JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Error de Login",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        // 🔹 REGISTRO (de momento vacío)
        if (e.getSource() == btnRegistrarse) {

            JOptionPane.showMessageDialog(this,
                    "Funcionalidad no implementada todavía");
        }
    }
}
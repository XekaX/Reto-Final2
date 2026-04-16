package vistas;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import main.Main;
import modelo.Tipo;
import modelo.Trabajador;
import utilidades.FondoPanel;

public class VMenuAdmin extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    private JButton btnCrear, btnVer, btnEliminar, btnModificar;

    public VMenuAdmin() {

        setTitle("ADMIN");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        setIconImage(Toolkit.getDefaultToolkit().getImage(
                getClass().getResource("/resources/icono.jpg")));

        setContentPane(new FondoPanel("/resources/fondo.jpg"));
        setLayout(null);
        
        setLocationRelativeTo(null);

        btnCrear = new JButton("Crear trabajador");
        btnCrear.setBounds(100, 40, 200, 30);
        add(btnCrear);

        btnVer = new JButton("Ver trabajadores");
        btnVer.setBounds(100, 90, 200, 30);
        add(btnVer);

        btnEliminar = new JButton("Eliminar trabajador");
        btnEliminar.setBounds(100, 140, 200, 30);
        add(btnEliminar);

        btnModificar = new JButton("Modificar trabajador");
        btnModificar.setBounds(100, 190, 200, 30);
        add(btnModificar);

        btnCrear.addActionListener(this);
        btnVer.addActionListener(this);
        btnEliminar.addActionListener(this);
        btnModificar.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btnCrear) crearTrabajador();
        if (e.getSource() == btnVer) verTrabajadores();
        if (e.getSource() == btnEliminar) eliminarTrabajador();
        if (e.getSource() == btnModificar) modificarTrabajador();
    }

    private void crearTrabajador() {

        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("ID trabajador:"));
            String nombre = JOptionPane.showInputDialog("Nombre:");
            String pass = JOptionPane.showInputDialog("Contraseña:");

            String[] opciones = {"ADMIN", "TRABAJADOR"};
            String tipoStr = (String) JOptionPane.showInputDialog(
                    null, "Tipo:", "Selecciona tipo",
                    JOptionPane.QUESTION_MESSAGE, null,
                    opciones, opciones[0]);

            Tipo tipo = Tipo.valueOf(tipoStr);

            Trabajador t = new Trabajador();
            t.setCod_T(id);
            t.setNombre(nombre);
            t.setTipo(tipo);
            t.setContrasenia(pass);

            Main.insertarTrabajador(t);

            JOptionPane.showMessageDialog(this, "Trabajador creado");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error");
        }
    }

    private void verTrabajadores() {

        ArrayList<Trabajador> lista = Main.obtenerTrabajadores();

        String texto = "";

        for (Trabajador t : lista) {
            texto += "ID: " + t.getCod_T()
                    + " | Nombre: " + t.getNombre()
                    + " | Tipo: " + t.getTipo() + "\n";
        }

        JOptionPane.showMessageDialog(this, texto);
    }

    private void eliminarTrabajador() {

        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("ID a eliminar"));

            Main.eliminarTrabajador(id);

            JOptionPane.showMessageDialog(this, "Eliminado");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error");
        }
    }

    private void modificarTrabajador() {

        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("ID a modificar"));

            String nombre = JOptionPane.showInputDialog("Nuevo nombre:");
            String pass = JOptionPane.showInputDialog("Nueva contraseña:");

            String[] opciones = {"ADMIN", "TRABAJADOR"};
            String tipoStr = (String) JOptionPane.showInputDialog(
                    null, "Tipo:", "Selecciona tipo",
                    JOptionPane.QUESTION_MESSAGE, null,
                    opciones, opciones[0]);

            Tipo tipo = Tipo.valueOf(tipoStr);

            Trabajador t = new Trabajador();
            t.setCod_T(id);
            t.setNombre(nombre);
            t.setTipo(tipo);
            t.setContrasenia(pass);

            Main.actualizarTrabajador(t);

            JOptionPane.showMessageDialog(this, "Trabajador actualizado");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al modificar");
        }
    }
}
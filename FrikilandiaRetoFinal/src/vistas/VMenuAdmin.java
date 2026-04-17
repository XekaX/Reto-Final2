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

/**
 * Clase que representa el menú del administrador en la aplicación Frikilandia.
 * 
 * Permite realizar operaciones CRUD sobre los trabajadores:
 * crear, ver, eliminar y modificar.
 * 
 * Esta clase forma parte de la capa de vista (MVC) y se comunica con el
 * controlador Main para acceder a la base de datos.
 */
public class VMenuAdmin extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    // Botones del panel de administración
    private JButton btnCrear, btnVer, btnEliminar, btnModificar;

    /**
     * Constructor de la ventana del administrador.
     * Inicializa la interfaz gráfica y los botones.
     */
    public VMenuAdmin() {

        setTitle("ADMIN");

        // Tamaño de la ventana
        setSize(400, 350);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Icono de la ventana
        setIconImage(Toolkit.getDefaultToolkit().getImage(
                getClass().getResource("/resources/berserk.jpeg")));

        // Fondo personalizado
        setContentPane(new FondoPanel("/resources/tcgsimulator.jpg"));
        setLayout(null);

        setLocationRelativeTo(null);

        // Botón crear trabajador
        btnCrear = new JButton("Crear trabajador");
        btnCrear.setBounds(100, 40, 200, 30);
        add(btnCrear);

        // Botón ver trabajadores
        btnVer = new JButton("Ver trabajadores");
        btnVer.setBounds(100, 90, 200, 30);
        add(btnVer);

        // Botón eliminar trabajador
        btnEliminar = new JButton("Eliminar trabajador");
        btnEliminar.setBounds(100, 140, 200, 30);
        add(btnEliminar);

        // Botón modificar trabajador
        btnModificar = new JButton("Modificar trabajador");
        btnModificar.setBounds(100, 190, 200, 30);
        add(btnModificar);

        // Listener de eventos
        btnCrear.addActionListener(this);
        btnVer.addActionListener(this);
        btnEliminar.addActionListener(this);
        btnModificar.addActionListener(this);
    }

    /**
     * Gestiona los eventos de los botones del menú.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btnCrear) crearTrabajador();

        if (e.getSource() == btnVer) verTrabajadores();

        if (e.getSource() == btnEliminar) eliminarTrabajador();

        if (e.getSource() == btnModificar) modificarTrabajador();
    }

    /**
     * Crea un nuevo trabajador introducido por el usuario.
     * Inserta el trabajador en la base de datos.
     */
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

            // Crear objeto trabajador
            Trabajador t = new Trabajador();

            // Asignar datos
            t.setCod_T(id);
            t.setNombre(nombre);
            t.setTipo(tipo);
            t.setContrasenia(pass);

            // Insertar en BD
            Main.insertarTrabajador(t);

            JOptionPane.showMessageDialog(this, "Trabajador creado");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al crear trabajador");
        }
    }

    /**
     * Muestra todos los trabajadores del sistema.
     */
    private void verTrabajadores() {

        ArrayList<Trabajador> lista = Main.obtenerTrabajadores();

        String texto = "";

        // Recorrer lista de trabajadores
        for (Trabajador t : lista) {
            texto += "ID: " + t.getCod_T()
                    + " | Nombre: " + t.getNombre()
                    + " | Tipo: " + t.getTipo() + "\n";
        }

        JOptionPane.showMessageDialog(this, texto);
    }

    /**
     * Elimina un trabajador según su ID.
     */
    private void eliminarTrabajador() {

        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("ID a eliminar"));

            Main.eliminarTrabajador(id);

            JOptionPane.showMessageDialog(this, "Trabajador eliminado");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar trabajador");
        }
    }

    /**
     * Modifica los datos de un trabajador existente.
     */
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

            // Crear trabajador actualizado
            Trabajador t = new Trabajador();
            t.setCod_T(id);
            t.setNombre(nombre);
            t.setTipo(tipo);
            t.setContrasenia(pass);

            // Actualizar en BD
            Main.actualizarTrabajador(t);

            JOptionPane.showMessageDialog(this, "Trabajador actualizado");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al modificar trabajador");
        }
    }
}
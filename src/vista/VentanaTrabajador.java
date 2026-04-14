package vista;

import java.awt.event.*;
import java.util.Map;
import javax.swing.*;

import controlador.Dao;
import controlador.DaoImplementacionMySql;
import excepciones.CodigoException;
import modelo.Categoria;
import modelo.Producto;
import modelo.TipoProducto;
import principal.Principal;

public class VentanaTrabajador extends JDialog implements ActionListener {

    private static final long serialVersionUID = 1L;

    private Map<Integer, Categoria> map;
    private JTextField textDescripcion;
    private JTextField textPrecio;
    private JTextField textCod;
    private JComboBox<Categoria> cmbCategoria;
    private JButton btnAñadir;
    private JButton btnModificar;
    private JButton btnEliminar;
    private JButton btnVisualizar;

    public VentanaTrabajador() {
        setTitle("Gestión de Productos");
        setSize(400, 280);
        setLocationRelativeTo(null);
        getContentPane().setLayout(null);

        JLabel lblCod = new JLabel("Código:");
        lblCod.setBounds(26, 29, 70, 24);
        getContentPane().add(lblCod);

        JLabel lblPrecio = new JLabel("Precio:");
        lblPrecio.setBounds(26, 80, 64, 13);
        getContentPane().add(lblPrecio);

        JLabel lblDescripcion = new JLabel("Descripción:");
        lblDescripcion.setBounds(26, 121, 97, 13);
        getContentPane().add(lblDescripcion);

        JLabel lblCategoria = new JLabel("Categoría:");
        lblCategoria.setBounds(26, 164, 70, 13);
        getContentPane().add(lblCategoria);

        textCod = new JTextField();
        textCod.setBounds(120, 34, 97, 19);
        getContentPane().add(textCod);

        textPrecio = new JTextField();
        textPrecio.setBounds(120, 79, 97, 19);
        getContentPane().add(textPrecio);

        textDescripcion = new JTextField();
        textDescripcion.setBounds(120, 120, 96, 19);
        getContentPane().add(textDescripcion);

        cmbCategoria = new JComboBox<>();
        cmbCategoria.setBounds(120, 162, 97, 21);
        getContentPane().add(cmbCategoria);

        Dao dao = new DaoImplementacionMySql();
        map = dao.listarCategoria();

        for (Categoria c : map.values()) {
            cmbCategoria.addItem(c);
        }

        btnAñadir = new JButton("Añadir");
        btnAñadir.setBounds(10, 212, 85, 21);
        btnAñadir.addActionListener(this);
        getContentPane().add(btnAñadir);

        btnModificar = new JButton("Modificar");
        btnModificar.setBounds(105, 212, 85, 21);
        btnModificar.addActionListener(this);
        getContentPane().add(btnModificar);

        btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(194, 212, 85, 21);
        btnEliminar.addActionListener(this);
        getContentPane().add(btnEliminar);

        btnVisualizar = new JButton("Visualizar");
        btnVisualizar.setBounds(289, 212, 85, 21);
        btnVisualizar.addActionListener(this);
        getContentPane().add(btnVisualizar);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btnAñadir)) {
            alta();
        } else if (e.getSource().equals(btnEliminar)) {
            eliminar();
        } else if (e.getSource().equals(btnModificar)) {
            VentanaModificar m = new VentanaModificar();
            m.setVisible(true);
        } else if (e.getSource().equals(btnVisualizar)) {
            VentanaVisualizar v = new VentanaVisualizar();
            v.setVisible(true);
        }
    }

    private void alta() {
        Producto p = new Producto();
        try {
            String textoCod = textCod.getText();
            if (!textoCod.matches("\\d+")) {
                throw new CodigoException("Debes introducir un código numérico");
            }

            p.setCodP(Integer.parseInt(textoCod));
            p.setPrecio(Float.parseFloat(textPrecio.getText()));
            p.setDescripcion(textDescripcion.getText());

            Categoria cat = (Categoria) cmbCategoria.getSelectedItem();
            p.setIdCategoria(cat.getId_categoria());

            if (cat.getNombre().equalsIgnoreCase("Cartas"))
                p.setTipo(TipoProducto.CARTAS);
            else if (cat.getNombre().equalsIgnoreCase("Comics"))
                p.setTipo(TipoProducto.COMICS);

            boolean ok = Principal.altaProducto(p);

            if (ok)
                JOptionPane.showMessageDialog(this, "Producto añadido: " + p.getDescripcion());
            else
                JOptionPane.showMessageDialog(this, "Error al añadir el producto.");

        } catch (CodigoException ex) {
            ex.visualizarMensaje();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Precio inválido");
        }
    }

    private void eliminar() {
        int cod = Integer.parseInt(textCod.getText());

        if (!Principal.existeProducto(cod)) {
            JOptionPane.showMessageDialog(this, "No existe ese producto");
            return;
        }

        Producto p = new Producto();
        p.setCodP(cod);

        boolean ok = Principal.eliminarProducto(p);

        if (ok) {
            JOptionPane.showMessageDialog(this, "Producto eliminado");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error al eliminar");
        }
    }
}
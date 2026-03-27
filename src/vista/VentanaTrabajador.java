package vista;

import java.awt.*;
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
import util.FondoPanel;

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

        // Labels (IGUAL)
        JLabel lblCod = new JLabel("Código:");
        lblCod.setBackground(new Color(0, 0, 0));
        lblCod.setForeground(new Color(0, 0, 0));
        lblCod.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblCod.setBounds(26, 29, 70, 24);
        getContentPane().add(lblCod);

        JLabel lblPrecio = new JLabel("Precio:");
        lblPrecio.setBackground(new Color(0, 0, 0));
        lblPrecio.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblPrecio.setForeground(new Color(0, 0, 0));
        lblPrecio.setBounds(26, 80, 64, 13);
        getContentPane().add(lblPrecio);

        JLabel lblDescripcion = new JLabel("Descripción:");
        lblDescripcion.setBackground(new Color(0, 0, 0));
        lblDescripcion.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblDescripcion.setForeground(new Color(0, 0, 0));
        lblDescripcion.setBounds(26, 121, 97, 13);
        getContentPane().add(lblDescripcion);

        JLabel lblCategoria = new JLabel("Categoría:");
        lblCategoria.setBackground(new Color(0, 0, 0));	
        lblCategoria.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblCategoria.setForeground(new Color(0, 0, 0));
        lblCategoria.setBounds(26, 164, 70, 13);
        getContentPane().add(lblCategoria);

        // Campos
        textCod = new JTextField();
        textCod.setBounds(120, 34, 97, 19);
        estiloCampo(textCod);
        getContentPane().add(textCod);

        textPrecio = new JTextField();
        textPrecio.setBounds(120, 79, 97, 19);
        estiloCampo(textPrecio);
        getContentPane().add(textPrecio);

        textDescripcion = new JTextField();
        textDescripcion.setBounds(120, 120, 96, 19);
        estiloCampo(textDescripcion);
        getContentPane().add(textDescripcion);

        // Combo
        cmbCategoria = new JComboBox<>();
        cmbCategoria.setBounds(120, 162, 97, 21);
        cmbCategoria.setBackground(new Color(45,45,45));
        cmbCategoria.setForeground(Color.WHITE);
        getContentPane().add(cmbCategoria);

        Dao dao = new DaoImplementacionMySql();
        map = dao.listarCategoria();
        for (Categoria c : map.values()) {
            cmbCategoria.addItem(c);
        }

        // Botones
        btnAñadir = new JButton("Añadir");
        btnAñadir.setBounds(10, 212, 85, 21);
        estiloBoton(btnAñadir, new Color(0,150,136));
        btnAñadir.addActionListener(this);
        getContentPane().add(btnAñadir);

        btnModificar = new JButton("Modificar");
        btnModificar.setBackground(new Color(0, 0, 0));
        btnModificar.setBounds(105, 212, 85, 21);
        estiloBoton(btnModificar, new Color(33,150,243));
        btnModificar.addActionListener(this);
        getContentPane().add(btnModificar);

        btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(194, 212, 85, 21);
        estiloBoton(btnEliminar, new Color(244,67,54));
        btnEliminar.addActionListener(this);
        getContentPane().add(btnEliminar);

        btnVisualizar = new JButton("Visualizar");
        btnVisualizar.setBounds(289, 212, 85, 21);
        estiloBoton(btnVisualizar, new Color(156,39,176));
        btnVisualizar.addActionListener(this);
        getContentPane().add(btnVisualizar);
    }

    private void estiloCampo(JTextField txt) {
        txt.setBackground(new Color(255, 255, 255)); // fondo blanco
        txt.setForeground(new Color(0, 0, 0));       // texto negro
        txt.setCaretColor(new Color(0, 0, 0));      // cursor negro
        txt.setBorder(BorderFactory.createLineBorder(new Color(90,90,90)));
    }
    private void estiloBoton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(new Color(0, 0, 0));
        btn.setFocusPainted(false);
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
                JOptionPane.showMessageDialog(this, "Producto añadido: " + p.getDescripcion(), "Alta exitosa",
                        JOptionPane.INFORMATION_MESSAGE);
            else
                JOptionPane.showMessageDialog(this, "Error al añadir el producto.", "Error",
                        JOptionPane.ERROR_MESSAGE);

        } catch (CodigoException ex) {
            ex.visualizarMensaje();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Introduce un número válido para precio", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminar() {
        int cod = Integer.parseInt(textCod.getText());
        if (!Principal.existeProducto(cod)) {
            JOptionPane.showMessageDialog(this, "No existe un producto con ese código.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        Producto p = new Producto();
        p.setCodP(cod);
        boolean ok = Principal.eliminarProducto(p);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Producto eliminado.", "Eliminado", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else
            JOptionPane.showMessageDialog(this, "Error al eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}
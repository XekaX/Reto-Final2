package vista;

import java.awt.*;
import java.awt.event.*;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.*;
import controlador.Dao;
import controlador.DaoImplementacionMySql;
import modelo.Categoria;
import modelo.Producto;
import principal.Principal;

public class VentanaModificar extends JDialog implements ActionListener {

    private static final long serialVersionUID = 1L;

    private static final Color BG_DARK    = new Color(14, 14, 30);
    private static final Color BG_CARD    = new Color(20, 20, 42);
    private static final Color BG_INPUT   = new Color(30, 30, 56);
    private static final Color GOLD       = new Color(232, 197, 71);
    private static final Color RED_SOFT   = new Color(232, 92, 92);
    private static final Color TEXT_MAIN  = new Color(244, 244, 255);
    private static final Color TEXT_DIM   = new Color(136, 136, 170);
    private static final Color BORDER_COL = new Color(46, 46, 74);

    private JTextField textCod, textPrecio, textDescripcion;
    private JComboBox<Categoria> cmbCategoria;
    private Map<Integer, Categoria> map;
    private JButton btnGuardar, btnCancelar;

    public static void main(String[] args) {
        try {
            VentanaModificar d = new VentanaModificar();
            d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            d.setVisible(true);
        } catch (Exception e) { e.printStackTrace(); }
    }

    public VentanaModificar() {
        setTitle("Modificar Producto");
        setSize(520, 420);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_DARK);
        getContentPane().setLayout(new BorderLayout());

        JPanel main = new JPanel(new GridBagLayout());
        main.setBackground(BG_CARD);
        main.setBorder(new CompoundBorder(
            new LineBorder(GOLD, 2, true),
            new EmptyBorder(24, 28, 24, 28)
        ));

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(6, 6, 6, 6);

        JLabel title = new JLabel("Modificar producto");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(TEXT_MAIN);
        g.gridx = 0; g.gridy = 0; g.gridwidth = 4;
        main.add(title, (GridBagConstraints) g.clone());

        JLabel sub = new JLabel("EDITA CÓDIGO Y PRECIO · RESTO DE SOLO LECTURA");
        sub.setFont(new Font("Monospaced", Font.PLAIN, 10));
        sub.setForeground(TEXT_DIM);
        g.gridy = 1;
        main.add(sub, (GridBagConstraints) g.clone());

        JSeparator sep = new JSeparator();
        sep.setForeground(BORDER_COL);
        g.gridy = 2; g.insets = new Insets(8, 6, 12, 6);
        main.add(sep, (GridBagConstraints) g.clone());

        // Código
        g.insets = new Insets(6, 6, 6, 6);
        g.gridwidth = 2; g.gridy = 3; g.gridx = 0;
        main.add(label("CÓDIGO"), (GridBagConstraints) g.clone());
        g.gridy = 4;
        textCod = styledField("P-0042", false);
        main.add(textCod, (GridBagConstraints) g.clone());

        // Precio
        g.gridx = 2; g.gridy = 3;
        main.add(label("PRECIO"), (GridBagConstraints) g.clone());
        g.gridy = 4;
        textPrecio = styledField("12.99", false);
        main.add(textPrecio, (GridBagConstraints) g.clone());

        // Descripción (solo lectura)
        g.gridx = 0; g.gridy = 5; g.gridwidth = 4;
        main.add(label("DESCRIPCIÓN — SOLO LECTURA"), (GridBagConstraints) g.clone());
        g.gridy = 6;
        textDescripcion = styledField("No editable", true);
        main.add(textDescripcion, (GridBagConstraints) g.clone());

        // Categoría (solo lectura)
        g.gridy = 7;
        main.add(label("CATEGORÍA — SOLO LECTURA"), (GridBagConstraints) g.clone());
        g.gridy = 8;
        cmbCategoria = new JComboBox<>();
        styleComboDisabled(cmbCategoria);
        Dao dao = new DaoImplementacionMySql();
        map = dao.listarCategoria();
        for (Categoria c : map.values()) cmbCategoria.addItem(c);
        cmbCategoria.setEnabled(false);
        main.add(cmbCategoria, (GridBagConstraints) g.clone());

        // Aviso
        g.gridy = 9; g.insets = new Insets(10, 6, 6, 6);
        JLabel notice = new JLabel("La descripción y categoría no son modificables una vez registradas.");
        notice.setFont(new Font("Monospaced", Font.PLAIN, 10));
        notice.setForeground(TEXT_DIM);
        notice.setBorder(new CompoundBorder(
            new MatteBorder(0, 3, 0, 0, GOLD),
            new EmptyBorder(4, 10, 4, 6)
        ));
        main.add(notice, (GridBagConstraints) g.clone());

        JSeparator sep2 = new JSeparator();
        sep2.setForeground(BORDER_COL);
        g.gridy = 10; g.insets = new Insets(10, 6, 10, 6);
        main.add(sep2, (GridBagConstraints) g.clone());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        btnPanel.setBackground(BG_CARD);
        btnGuardar  = styledBtn("Guardar cambios", GOLD,     BG_CARD);
        btnCancelar = styledBtn("Cancelar",        RED_SOFT, BG_CARD);
        btnGuardar.addActionListener(this);
        btnCancelar.addActionListener(this);
        btnPanel.add(btnGuardar);
        btnPanel.add(btnCancelar);
        g.gridy = 11; g.insets = new Insets(4, 6, 4, 6);
        main.add(btnPanel, (GridBagConstraints) g.clone());

        JPanel wrap = new JPanel(new GridBagLayout());
        wrap.setBackground(BG_DARK);
        wrap.setBorder(new EmptyBorder(20, 20, 20, 20));
        wrap.add(main, new GridBagConstraints());
        getContentPane().add(wrap, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btnGuardar))       modificar();
        else if (e.getSource().equals(btnCancelar)) dispose();
    }

    private void modificar() {
        int cod = Integer.parseInt(textCod.getText());
        if (!Principal.existeProducto(cod)) {
            JOptionPane.showMessageDialog(this, "No existe un producto con ese código.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Producto p = new Producto();
        p.setCodP(cod);
        p.setDescripcion(textDescripcion.getText());
        p.setPrecio(Float.parseFloat(textPrecio.getText()));
        boolean ok = Principal.modificarProducto(p);
        if (ok) JOptionPane.showMessageDialog(this, "Producto modificado: " + p.getDescripcion(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
        else    JOptionPane.showMessageDialog(this, "Error al modificar.", "Error", JOptionPane.ERROR_MESSAGE);
    }

    private JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Monospaced", Font.BOLD, 10));
        l.setForeground(TEXT_DIM);
        return l;
    }

    private JTextField styledField(String placeholder, boolean disabled) {
        JTextField f = new JTextField(18);
        f.setBackground(disabled ? new Color(24, 24, 48) : BG_INPUT);
        f.setForeground(disabled ? TEXT_DIM : TEXT_MAIN);
        f.setCaretColor(GOLD);
        f.setFont(new Font("SansSerif", Font.PLAIN, 13));
        f.setEditable(!disabled);
        f.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COL, 1, true),
            new EmptyBorder(4, 10, 4, 10)
        ));
        if (!disabled) {
            f.setText(placeholder);
            f.setForeground(TEXT_DIM);
            f.addFocusListener(new FocusAdapter() {
                public void focusGained(FocusEvent e) {
                    if (f.getText().equals(placeholder)) { f.setText(""); f.setForeground(TEXT_MAIN); }
                }
                public void focusLost(FocusEvent e) {
                    if (f.getText().isEmpty()) { f.setText(placeholder); f.setForeground(TEXT_DIM); }
                }
            });
        } else {
            f.setText(placeholder);
        }
        return f;
    }

    private void styleComboDisabled(JComboBox<Categoria> c) {
        c.setBackground(new Color(24, 24, 48));
        c.setForeground(TEXT_DIM);
        c.setFont(new Font("SansSerif", Font.PLAIN, 13));
        c.setBorder(new LineBorder(BORDER_COL, 1, true));
    }

    private JButton styledBtn(String text, Color accent, Color bg) {
        JButton b = new JButton(text);
        b.setFont(new Font("Monospaced", Font.BOLD, 11));
        b.setForeground(accent);
        b.setBackground(bg);
        b.setBorder(new CompoundBorder(
            new LineBorder(accent, 1, true),
            new EmptyBorder(7, 18, 7, 18)
        ));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { b.setBackground(accent.darker().darker()); }
            public void mouseExited(MouseEvent e)  { b.setBackground(bg); }
        });
        return b;
    }
}
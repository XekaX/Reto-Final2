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
import modelo.TipoProducto;
import principal.Principal;

public class VentanaTrabajador extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private static final Color BG_DARK = new Color(14, 14, 30);
	private static final Color BG_CARD = new Color(20, 20, 42);
	private static final Color BG_INPUT = new Color(30, 30, 56);
	private static final Color GOLD = new Color(232, 197, 71);
	private static final Color TEAL = new Color(71, 197, 180);
	private static final Color ORANGE = new Color(232, 160, 71);
	private static final Color RED_SOFT = new Color(232, 92, 92);
	private static final Color TEXT_MAIN = new Color(244, 244, 255);
	private static final Color TEXT_DIM = new Color(136, 136, 170);
	private static final Color BORDER_COL = new Color(46, 46, 74);

	private JTextField textCod, textPrecio, textDescripcion;
	private JComboBox<Categoria> cmbCategoria;
	private Map<Integer, Categoria> map;
	private JButton btnAñadir, btnModificar, btnEliminar, btnVisualizar;

	public static void main(String[] args) {
		try {
			VentanaTrabajador d = new VentanaTrabajador();
			d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			d.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public VentanaTrabajador() {
		setTitle("Gestión de Productos");
		setSize(520, 420);
		setLocationRelativeTo(null);
		getContentPane().setBackground(BG_DARK);
		getContentPane().setLayout(new BorderLayout());

		JPanel main = new JPanel();
		main.setBackground(BG_CARD);
		main.setLayout(new GridBagLayout());
		main.setBorder(new CompoundBorder(new LineBorder(GOLD, 2, true), new EmptyBorder(24, 28, 24, 28)));

		GridBagConstraints g = new GridBagConstraints();
		g.fill = GridBagConstraints.HORIZONTAL;
		g.insets = new Insets(6, 6, 6, 6);

		// Título
		JLabel title = new JLabel("Gestión de productos");
		title.setFont(new Font("SansSerif", Font.BOLD, 22));
		title.setForeground(TEXT_MAIN);
		g.gridx = 0;
		g.gridy = 0;
		g.gridwidth = 4;
		main.add(title, (GridBagConstraints) g.clone());

		JLabel sub = new JLabel("ALTA · BAJA · MODIFICACIÓN");
		sub.setFont(new Font("Monospaced", Font.PLAIN, 10));
		sub.setForeground(TEXT_DIM);
		g.gridy = 1;
		main.add(sub, (GridBagConstraints) g.clone());

		// Separador
		JSeparator sep = new JSeparator();
		sep.setForeground(BORDER_COL);
		sep.setBackground(BORDER_COL);
		g.gridy = 2;
		g.insets = new Insets(8, 6, 12, 6);
		main.add(sep, (GridBagConstraints) g.clone());

		// Código (mitad izquierda)
		g.insets = new Insets(6, 6, 6, 6);
		g.gridwidth = 2;
		g.gridy = 3;
		g.gridx = 0;
		main.add(label("CÓDIGO"), (GridBagConstraints) g.clone());
		g.gridy = 4;
		textCod = styledField("");
		main.add(textCod, (GridBagConstraints) g.clone());

		// Precio (mitad derecha)
		g.gridx = 2;
		g.gridy = 3;
		main.add(label("PRECIO"), (GridBagConstraints) g.clone());
		g.gridy = 4;
		textPrecio = styledField("");
		main.add(textPrecio, (GridBagConstraints) g.clone());

		// Descripción (ancho completo)
		g.gridx = 0;
		g.gridy = 5;
		g.gridwidth = 4;
		main.add(label("DESCRIPCIÓN"), (GridBagConstraints) g.clone());
		g.gridy = 6;
		textDescripcion = styledField("Nombre del producto");
		main.add(textDescripcion, (GridBagConstraints) g.clone());

		// Categoría
		g.gridy = 7;
		main.add(label("CATEGORÍA"), (GridBagConstraints) g.clone());
		g.gridy = 8;
		cmbCategoria = new JComboBox<>();
		styleCombo(cmbCategoria);
		Dao dao = new DaoImplementacionMySql();
		map = dao.listarCategoria();
		for (Categoria c : map.values())
			cmbCategoria.addItem(c);
		main.add(cmbCategoria, (GridBagConstraints) g.clone());

		// Separador botones
		JSeparator sep2 = new JSeparator();
		sep2.setForeground(BORDER_COL);
		g.gridy = 9;
		g.insets = new Insets(14, 6, 10, 6);
		main.add(sep2, (GridBagConstraints) g.clone());

		// Botones
		g.insets = new Insets(4, 6, 4, 6);
		JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
		btnPanel.setBackground(BG_CARD);

		btnAñadir = styledBtn("Añadir", GOLD, BG_CARD);
		btnModificar = styledBtn("Modificar", ORANGE, BG_CARD);
		btnVisualizar = styledBtn("Catálogo", TEAL, BG_CARD);
		btnEliminar = styledBtn("Eliminar", RED_SOFT, BG_CARD);

		btnAñadir.addActionListener(this);
		btnModificar.addActionListener(this);
		btnVisualizar.addActionListener(this);
		btnEliminar.addActionListener(this);

		btnPanel.add(btnAñadir);
		btnPanel.add(btnModificar);
		btnPanel.add(btnVisualizar);
		btnPanel.add(btnEliminar);

		g.gridy = 10;
		main.add(btnPanel, (GridBagConstraints) g.clone());

		JPanel wrap = new JPanel(new GridBagLayout());
		wrap.setBackground(BG_DARK);
		wrap.setBorder(new EmptyBorder(20, 20, 20, 20));
		wrap.add(main, new GridBagConstraints());
		getContentPane().add(wrap, BorderLayout.CENTER);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(btnAñadir))
			alta();
		else if (e.getSource().equals(btnEliminar))
			eliminar();
		else if (e.getSource().equals(btnModificar)) {
			VentanaModificar m = new VentanaModificar();
			m.setVisible(true);
		} else if (e.getSource().equals(btnVisualizar)) {
			VentanaVisualizar v = new VentanaVisualizar();
			v.setVisible(true);
		}
	}

	private void alta() {
		try {
			Producto p = new Producto();
			p.setCodP(Integer.parseInt(textCod.getText()));
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
				JOptionPane.showMessageDialog(this, "Error al añadir el producto.", "Error", JOptionPane.ERROR_MESSAGE);
			dispose();
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "Datos incorrectos: " + ex.getMessage(), "Error",
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

	// ── Helpers de estilo ──────────────────────────────────────────
	private JLabel label(String text) {
		JLabel l = new JLabel(text);
		l.setFont(new Font("Monospaced", Font.BOLD, 10));
		l.setForeground(TEXT_DIM);
		return l;
	}

	private JTextField styledField(String placeholder) {
		JTextField f = new JTextField(18);
		f.setBackground(BG_INPUT);
		f.setForeground(TEXT_DIM);
		f.setCaretColor(GOLD);
		f.setFont(new Font("SansSerif", Font.PLAIN, 13));
		f.setBorder(new CompoundBorder(new LineBorder(BORDER_COL, 1, true), new EmptyBorder(4, 10, 4, 10)));
		f.setText(placeholder);
		f.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				if (f.getText().equals(placeholder)) {
					f.setText("");
					f.setForeground(TEXT_MAIN);
				}
			}

			public void focusLost(FocusEvent e) {
				if (f.getText().isEmpty()) {
					f.setText(placeholder);
					f.setForeground(TEXT_DIM);
				}
			}
		});
		return f;
	}

	private void styleCombo(JComboBox<Categoria> c) {
		c.setBackground(BG_INPUT);
		c.setForeground(TEXT_MAIN);
		c.setFont(new Font("SansSerif", Font.PLAIN, 13));
		c.setBorder(new LineBorder(BORDER_COL, 1, true));
		((JLabel) c.getRenderer()).setOpaque(true);
	}

	private JButton styledBtn(String text, Color accent, Color bg) {
		JButton b = new JButton(text);
		b.setFont(new Font("Monospaced", Font.BOLD, 11));
		b.setForeground(accent);
		b.setBackground(bg);
		b.setBorder(new CompoundBorder(new LineBorder(accent, 1, true), new EmptyBorder(7, 18, 7, 18)));
		b.setFocusPainted(false);
		b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		b.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				b.setBackground(accent.darker().darker());
			}

			public void mouseExited(MouseEvent e) {
				b.setBackground(bg);
			}
		});
		return b;
	}
}
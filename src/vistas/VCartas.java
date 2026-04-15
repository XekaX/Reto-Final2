package vistas;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

import main.Main;
import modelo.Carta;
import utilidades.FondoPanel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.JComboBox;
import javax.swing.JSpinner;

public class VCartas extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JButton btnAniadir;
	private JTextField nombreField;
	private JComboBox<String> tipoBox;
	JSpinner precioSpinner;

	public VCartas(JFrame parent, boolean modal) {
		super(parent, modal);

		setTitle("Cartas");
		setSize(454, 293);
		FondoPanel fondoPanel = new FondoPanel("/resources/fondo.jpg");
		setContentPane(fondoPanel);
		getContentPane().setLayout(null);

		setLocationRelativeTo(parent);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/icono.jpg")));

		btnAniadir = new JButton("Añadir");
		btnAniadir.setBounds(330, 209, 100, 37);
		fondoPanel.add(btnAniadir);
		btnAniadir.addActionListener(this);

		nombreField = new JTextField();
		nombreField.setBounds(330, 10, 100, 18);
		fondoPanel.add(nombreField);
		nombreField.setColumns(10);

		String[] tipos = { "Magic", "One Piece", "Pokemon" };
		tipoBox = new JComboBox<>(tipos);
		tipoBox.setBounds(330, 36, 100, 20);
		fondoPanel.add(tipoBox);

		precioSpinner = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 1000.0, 0.5));
		JSpinner.NumberEditor editor = new JSpinner.NumberEditor(precioSpinner, "#0.00");
		precioSpinner.setEditor(editor);

		precioSpinner.setBounds(330, 66, 100, 20);
		fondoPanel.add(precioSpinner);
		
		getRootPane().setDefaultButton(btnAniadir);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(btnAniadir)) {
			try {
				String nombre = nombreField.getText();
				String tipo = (String) tipoBox.getSelectedItem();
				double precio = (double) precioSpinner.getValue();

				if (nombre.isEmpty()) {
					JOptionPane.showMessageDialog(this, "El nombre es obligatorio");
					return;
				}

				String id = Main.generarIdCarta(tipo);
				Carta carta = new Carta(id, nombre, tipo, precio);
				Main.registrarCarta(carta);

				JOptionPane.showMessageDialog(this, "Carta añadida: " + id);

				nombreField.setText("");
				precioSpinner.setValue(0.0);

			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Error al añadir carta");
				ex.printStackTrace();
			}
		}
	}
}

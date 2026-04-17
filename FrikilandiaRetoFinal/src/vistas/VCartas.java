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

/**
 * 
 * AUTOR: Ander
 * 
 * Ventana de gestión de cartas.
 * 
 * Permite añadir nuevas cartas al sistema introduciendo su nombre,
 * tipo y precio. Genera automáticamente un identificador y registra
 * la carta en la lógica principal.
 */
public class VCartas extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	/** Botón para añadir una nueva carta */
	private JButton btnAniadir;

	/** Campo donde se introduce el nombre de la carta */
	private JTextField nombreField;

	/** Selector del tipo de carta */
	private JComboBox<String> tipoBox;

	/** Selector del precio de la carta */
	JSpinner precioSpinner;

	/**
	 * Constructor que inicializa la ventana de cartas.
	 * 
	 * Configura la interfaz gráfica, crea los campos de entrada
	 * y asigna los eventos necesarios.
	 * 
	 * @param parent ventana padre
	 * @param modal indica si la ventana es modal
	 */
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

		// Botón añadir carta
		btnAniadir = new JButton("Añadir");
		btnAniadir.setBounds(330, 209, 100, 37);
		fondoPanel.add(btnAniadir);
		btnAniadir.addActionListener(this);

		// Campo nombre
		nombreField = new JTextField();
		nombreField.setBounds(330, 10, 100, 18);
		fondoPanel.add(nombreField);
		nombreField.setColumns(10);

		// Selector de tipo
		String[] tipos = { "Magic", "One Piece", "Pokemon" };
		tipoBox = new JComboBox<>(tipos);
		tipoBox.setBounds(330, 36, 100, 20);
		fondoPanel.add(tipoBox);

		// Selector de precio
		precioSpinner = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 1000.0, 0.5));
		JSpinner.NumberEditor editor = new JSpinner.NumberEditor(precioSpinner, "#0.00");
		precioSpinner.setEditor(editor);
		precioSpinner.setBounds(330, 66, 100, 20);
		fondoPanel.add(precioSpinner);

		// Permite usar Enter para añadir
		getRootPane().setDefaultButton(btnAniadir);
	}

	/**
	 * Gestiona el evento del botón de añadir carta.
	 * 
	 * Recoge los datos introducidos, valida el nombre,
	 * genera un identificador único y registra la carta en el sistema.
	 * 
	 * @param e evento de acción
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(btnAniadir)) {
			try {
				String nombre = nombreField.getText();
				String tipo = (String) tipoBox.getSelectedItem();
				double precio = (double) precioSpinner.getValue();

				// Validación del nombre
				if (nombre.isEmpty()) {
					JOptionPane.showMessageDialog(this, "El nombre es obligatorio");
					return;
				}

				// Creación y registro de la carta
				String id = Main.generarIdCarta(tipo);
				Carta carta = new Carta(id, nombre, tipo, precio);
				Main.registrarCarta(carta);

				JOptionPane.showMessageDialog(this, "Carta añadida: " + id);

				// Limpieza de campos
				nombreField.setText("");
				precioSpinner.setValue(0.0);

			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Error al añadir carta");
				ex.printStackTrace();
			}
		}
	}
}
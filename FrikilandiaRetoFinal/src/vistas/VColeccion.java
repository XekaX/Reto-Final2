package vistas;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.WindowConstants;

import main.Main;
import modelo.Carta;
import modelo.Sesion;
import utilidades.FondoPanel;

/**
 * 
 * AUTOR: Ander
 * 
 * Ventana de colección de cartas del cliente.
 * 
 * Permite visualizar las cartas obtenidas, filtrarlas por tipo y precio,
 * vender cartas y aplicarles un sistema de "gradeo" (PSA) que modifica su valor.
 * 
 * También gestiona la lectura y escritura de las cartas en archivos CSV
 * y muestra el dinero disponible del usuario.
 */
public class VColeccion extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	/** Campo que muestra el usuario actual */
	private JTextField textUsuario;

	/** Campo que muestra el dinero disponible del cliente */
	private JTextField dineroField;

	/** Botón para vender cartas */
	private JButton btnVender;

	/** Lista visual de cartas */
	private JList<Carta> listaCartas;

	/** Modelo de datos de la lista */
	private DefaultListModel<Carta> modeloLista;

	/** Filtro por tipo de carta */
	private JComboBox<String> filtroTipo;

	/** Filtro de orden por precio */
	private JComboBox<String> filtroPrecio;

	/** Botón para gradear cartas */
	private JButton btnGradear;

	/** Lista interna de cartas del usuario */
	private List<Carta> cartas = new ArrayList<>();

	/**
	 * Constructor que inicializa la ventana de colección.
	 * 
	 * Configura la interfaz gráfica, carga las cartas desde archivo,
	 * aplica los filtros iniciales y asigna los eventos necesarios.
	 * 
	 * También inicia un temporizador que actualiza el dinero del cliente.
	 * 
	 * @param parent ventana padre
	 * @param modal indica si la ventana es modal
	 */
	public VColeccion(JFrame parent, boolean modal) {
		super(parent, modal);

		setTitle("Colección");
		setSize(454, 293);
		FondoPanel fondoPanel = new FondoPanel("/resources/fondo.jpg");
		setContentPane(fondoPanel);
		getContentPane().setLayout(null);

		setLocationRelativeTo(parent);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/icono.jpg")));

		// Campo usuario
		textUsuario = new JTextField();
		textUsuario.setEditable(false);
		textUsuario.setColumns(10);
		textUsuario.setBounds(10, 10, 96, 18);
		fondoPanel.add(textUsuario);

		textUsuario.setText(Sesion.getUsuario().getIdentificacion());

		// Campo dinero
		dineroField = new JTextField();
		dineroField.setColumns(10);
		dineroField.setBounds(116, 10, 96, 18);
		fondoPanel.add(dineroField);

		// Lista de cartas
		modeloLista = new DefaultListModel<>();
		listaCartas = new JList<>(modeloLista);

		JScrollPane scroll = new JScrollPane(listaCartas);
		scroll.setBounds(10, 50, 300, 180);
		fondoPanel.add(scroll);

		// Filtros
		filtroTipo = new JComboBox<>(new String[] { "Todos", "One Piece", "Pokemon", "Magic" });
		filtroTipo.setBounds(320, 50, 120, 25);
		fondoPanel.add(filtroTipo);

		filtroPrecio = new JComboBox<>(new String[] { "Más caro a barato", "Más barato a caro" });
		filtroPrecio.setBounds(320, 90, 120, 25);
		fondoPanel.add(filtroPrecio);

		// Botón vender
		btnVender = new JButton("Vender");
		btnVender.setBounds(320, 130, 120, 25);
		fondoPanel.add(btnVender);

		// Botón gradear
		btnGradear = new JButton("Gradear");
		btnGradear.setBounds(320, 170, 120, 25);
		fondoPanel.add(btnGradear);

		// Eventos
		btnVender.addActionListener(this);
		filtroTipo.addActionListener(this);
		filtroPrecio.addActionListener(this);
		btnGradear.addActionListener(this);

		// Carga inicial del dinero
		actualizarDinero();

		// Temporizador que actualiza el dinero cada 2 segundos
		Timer timer = new Timer(2000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				actualizarDinero();
			}
		});
		timer.start();

		// Carga y muestra inicial de cartas
		cargarCartas();
		aplicarFiltros();
	}

	/**
	 * Gestiona los eventos de la ventana.
	 * 
	 * Permite vender cartas, aplicar filtros o gradear cartas.
	 * 
	 * @param e evento de acción
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(btnVender)) {
			venderCarta();
		} else if (e.getSource().equals(filtroTipo)) {
			aplicarFiltros();
		} else if (e.getSource().equals(filtroPrecio)) {
			aplicarFiltros();
		} else if (e.getSource().equals(btnGradear)) {
			gradearCarta();
		}
	}

	/**
	 * Actualiza el dinero del cliente.
	 */
	private void actualizarDinero() {
		try {
			String dni = Sesion.getUsuario().getIdentificacion();
			double dinero = Main.obtenerDineroCliente(dni);
			dineroField.setText(String.format("%.2f", dinero));
		} catch (Exception e) {
			dineroField.setText("Error");
		}
	}

	/**
	 * Carga las cartas del usuario desde un archivo CSV.
	 */
	private void cargarCartas() {
		cartas.clear();

		String dni = textUsuario.getText();
		String nombreFichero = dni + "cartas.csv";

		try (BufferedReader br = new BufferedReader(new FileReader(nombreFichero))) {
			String linea;

			while ((linea = br.readLine()) != null) {
				String[] partes = linea.split(",");

				Carta c = new Carta();

				c.setId_C(partes[0].trim());
				c.setNombre(partes[1].trim());
				c.setTipo(partes[2].trim());
				c.setFoil(partes[3].trim().equalsIgnoreCase("FOIL"));

				double precioBase = Double.parseDouble(partes[4].trim());

				if (partes.length >= 6) {
					c.setPsa(Integer.parseInt(partes[5]));
					c.setPrecio(ajustarPrecio(precioBase, c.getPsa()));
				} else {
					c.setPsa(0);
					c.setPrecio(precioBase);
				}

				cartas.add(c);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Aplica los filtros seleccionados y actualiza la lista de cartas.
	 */
	private void aplicarFiltros() {
		modeloLista.clear();

		String tipoSeleccionado = (String) filtroTipo.getSelectedItem();
		String ordenPrecio = (String) filtroPrecio.getSelectedItem();

		List<Carta> filtradas = new ArrayList<>();

		for (Carta c : cartas) {
			if (tipoSeleccionado.equals("Todos") || c.getTipo().equalsIgnoreCase(tipoSeleccionado)) {
				filtradas.add(c);
			}
		}

		// Ordenación por precio
		filtradas.sort((c1, c2) -> {
			if (ordenPrecio.equals("Más caro a barato")) {
				return Double.compare(c2.getPrecio(), c1.getPrecio());
			} else {
				return Double.compare(c1.getPrecio(), c2.getPrecio());
			}
		});

		for (Carta c : filtradas) {
			modeloLista.addElement(c);
		}
	}

	/**
	 * Genera un valor PSA aleatorio según probabilidades.
	 * 
	 * @return valor PSA entre 1 y 10
	 */
	private int generarPSA() {
		double r = Math.random();

		if (r < 0.05)
			return 10;
		else if (r < 0.15)
			return 9;
		else if (r < 0.30)
			return 8;
		else if (r < 0.50)
			return 7;
		else if (r < 0.70)
			return 6;
		else if (r < 0.85)
			return 5;
		else if (r < 0.93)
			return 4;
		else if (r < 0.97)
			return 3;
		else if (r < 0.99)
			return 2;
		else
			return 1;
	}

	/**
	 * Ajusta el precio de una carta en función de su PSA.
	 * 
	 * @param precioBase precio original
	 * @param psa puntuación PSA
	 * @return nuevo precio ajustado
	 */
	private double ajustarPrecio(double precioBase, int psa) {

		if (psa == 10) {
			return precioBase * 10;
		} else if (psa == 9) {
			return precioBase * 2;
		} else if (psa == 8) {
			return precioBase * 1.3;
		} else if (psa == 7) {
			return precioBase * 1.1;
		} else if (psa >= 5) {
			return precioBase;
		} else if (psa >= 3) {
			return precioBase * 0.8;
		} else {
			return precioBase * 0.9;
		}
	}

	/**
	 * Vende la carta seleccionada y actualiza los datos del usuario.
	 */
	private void venderCarta() {
		Carta seleccionada = listaCartas.getSelectedValue();

		if (seleccionada == null) {
			JOptionPane.showMessageDialog(this, "Selecciona una carta");
			return;
		}

		double precioVenta = seleccionada.getPrecio() * 0.7;

		try {
			String dni = textUsuario.getText();

			Main.sumarDineroCliente(dni, precioVenta);

			eliminarCartaCSV(seleccionada);

			cargarCartas();
			aplicarFiltros();

			actualizarDinero();

			JOptionPane.showMessageDialog(this,
					"Carta vendida por " + String.format("%.2f", precioVenta) + "€");

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error al vender carta");
		}
	}

	/**
	 * Elimina una carta concreta del archivo CSV.
	 * 
	 * @param carta carta a eliminar
	 */
	private void eliminarCartaCSV(Carta carta) {
		String dni = textUsuario.getText();
		String fichero = dni + "cartas.csv";

		List<String> lineas = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(new FileReader(fichero))) {
			String linea;
			boolean eliminada = false;

			while ((linea = br.readLine()) != null) {

				String[] partes = linea.split(",");

				if (!eliminada && partes[0].trim().equals(carta.getId_C())
						&& partes[1].trim().equals(carta.getNombre())) {
					eliminada = true;
					continue;
				}

				lineas.add(linea);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try (PrintWriter pw = new PrintWriter(fichero)) {
			for (String l : lineas) {
				pw.println(l);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Aplica un PSA a la carta seleccionada y actualiza su precio.
	 */
	private void gradearCarta() {
		Carta seleccionada = listaCartas.getSelectedValue();

		if (seleccionada == null) {
			JOptionPane.showMessageDialog(this, "Selecciona una carta");
			return;
		}

		if (seleccionada.getPsa() > 0) {
			JOptionPane.showMessageDialog(this, "Esta carta ya está gradeada");
			return;
		}

		int psa = generarPSA();
		seleccionada.setPsa(psa);

		double nuevoPrecio = ajustarPrecio(seleccionada.getPrecio(), psa);
		seleccionada.setPrecio(nuevoPrecio);

		guardarCartasCSV();
		aplicarFiltros();

		JOptionPane.showMessageDialog(this, "Carta gradeada con PSA " + psa);
	}

	/**
	 * Guarda todas las cartas actualizadas en el archivo CSV.
	 */
	private void guardarCartasCSV() {
		String dni = textUsuario.getText();
		String fichero = dni + "cartas.csv";

		try (PrintWriter pw = new PrintWriter(fichero)) {

			for (Carta c : cartas) {
				String linea = c.getId_C() + "," + c.getNombre() + "," + c.getTipo() + ","
						+ (c.isFoil() ? "FOIL" : "NORMAL") + "," + String.format("%.2f", c.getPrecio()) + ","
						+ c.getPsa();

				pw.println(linea);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
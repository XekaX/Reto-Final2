package vistas;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import utilidades.FondoPanel;
import javax.swing.JButton;

public class VMenuTrabajador extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private JButton btnProducto;
	private JButton btnCarta;

	public VMenuTrabajador() {
		setTitle("Trabajador");
		setSize(300, 200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/icono.jpg")));

		FondoPanel fondoPanel = new FondoPanel("/resources/fondo.jpg");
		setContentPane(fondoPanel);
		getContentPane().setLayout(null);
		
		btnProducto = new JButton("Producto");
		btnProducto.setBounds(192, 133, 84, 20);
		fondoPanel.add(btnProducto);
		btnProducto.addActionListener(this);
		
		btnCarta = new JButton("Carta");
		btnCarta.setBounds(192, 103, 84, 20);
		fondoPanel.add(btnCarta);
		btnCarta.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(btnProducto)) {
			VProducto produc = new VProducto(null, true);
			produc.setVisible(true);
		} else if (e.getSource().equals(btnCarta)) {
			VCartas carta = new VCartas(null, true);
			carta.setVisible(true);
		}
	}
}

package vistas;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import utilidades.FondoPanel;
import javax.swing.JButton;

public class VProducto extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private JButton btnaniadirP;

	public VProducto (JFrame parent, boolean modal) {
		super(parent, modal);

			setTitle("Producto");
			setSize(454, 293);
			FondoPanel fondoPanel = new FondoPanel("/resources/fondo.jpg");
			setContentPane(fondoPanel);
			getContentPane().setLayout(null);
			
			setLocationRelativeTo(parent);
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

			setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/icono.jpg")));
			
			btnaniadirP = new JButton("Añadir");
			btnaniadirP.setBounds(144, 217, 130, 29);
			fondoPanel.add(btnaniadirP);
			btnaniadirP.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}

}

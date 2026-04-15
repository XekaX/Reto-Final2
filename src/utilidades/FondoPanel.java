package utilidades;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class FondoPanel extends JPanel {

    /**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private Image imagen;

    public FondoPanel(String rutaImagen) {
        imagen = new ImageIcon(
            getClass().getResource(rutaImagen)
        ).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
    }
}
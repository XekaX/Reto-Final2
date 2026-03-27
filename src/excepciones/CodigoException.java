package excepciones;

import javax.swing.JOptionPane;

public class CodigoException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CodigoException(String mensaje) {
        super(mensaje);
    }

    public void visualizarMensaje() {
        JOptionPane.showMessageDialog(null, getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
    }
}
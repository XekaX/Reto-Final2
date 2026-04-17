package modelo;

/**
 * Almacena el resultado de estadísticas de productos.
 * Incluye media, producto más caro y clasificación.
 *
 * @author Ekain
 */

public class ResultadoMedia {
	private float media;
	private String productoMasCaro;
	private float precioMaximo;
	private String clasificacion;

	public float getMedia() {
		return media;
	}
	public void setMedia(float media) {
		this.media = media;
	}
	public String getProductoMasCaro() {
		return productoMasCaro;
	}
	public void setProductoMasCaro(String p) {
		this.productoMasCaro = p;
	}
	public float getPrecioMaximo() {
		return precioMaximo;
	}
	public void setPrecioMaximo(float p) {
		this.precioMaximo = p;
	}
	public String getClasificacion() {
		return clasificacion;
	}
	public void setClasificacion(String c) {
		this.clasificacion = c;
	}
}
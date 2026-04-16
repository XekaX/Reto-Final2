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

    public void setProductoMasCaro(String productoMasCaro) {
        this.productoMasCaro = productoMasCaro;
    }

    public float getPrecioMaximo() {
        return precioMaximo;
    }

    public void setPrecioMaximo(float precioMaximo) {
        this.precioMaximo = precioMaximo;
    }

    public String getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
    }
}
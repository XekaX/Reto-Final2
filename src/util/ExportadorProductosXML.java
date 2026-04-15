package util;

import java.io.File;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import modelo.Producto;

public class ExportadorProductosXML implements InterfazExportadorProductos {

    @Override
    public void exportarProductos(List<Producto> listaProductos, String ruta) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document doc = builder.newDocument();

            Element root = doc.createElement("catalogo");
            doc.appendChild(root);

            for (Producto p : listaProductos) {
                Element prodNode = doc.createElement("producto");
                prodNode.setAttribute("id", String.valueOf(p.getCodP()));
                root.appendChild(prodNode);

                Element nombre = doc.createElement("nombre");
                nombre.setTextContent(p.getDescripcion());
                prodNode.appendChild(nombre);

                Element tipo = doc.createElement("tipo");
                tipo.setTextContent(p.getTipo().name()); // Enum a String
                prodNode.appendChild(tipo);

                Element precio = doc.createElement("precio");
                precio.setTextContent(String.valueOf(p.getPrecio()));
                prodNode.appendChild(precio);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(ruta));
            transformer.transform(source, result);

            System.out.println("Archivo XML generado con éxito en: " + ruta);

        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }
}
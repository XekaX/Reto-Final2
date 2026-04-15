package principal;

import excepciones.CodigoException;
import excepciones.LoginException;
import modelo.Categoria;
import modelo.Producto;
import modelo.ResultadoMedia;
import modelo.Trabajador;
import modelo.Usuario;
import vista.Login;

import java.util.Map;

import controlador.Dao;
import controlador.DaoImplementacionMySql;

public class Principal {

	private static Dao dao = new DaoImplementacionMySql();

	public static void main(String[] args) {

		Login login = new Login();
		login.setVisible(true);
	}

	public static Usuario login(Usuario trabajador) throws LoginException {
		return dao.login(trabajador);
	}
	
	public static boolean altaProducto(Producto product) throws CodigoException{
		return dao.añadirProducto(product);
	}
	
	public static Map<Integer, Categoria> listarCategorias() {
	    return dao.listarCategoria();
	}
	
	public static boolean eliminarProducto(Producto product) {
		return dao.eliminarProductos(product);
	}
	
	public static boolean existeProducto(int Cod_P) {
		return dao.existeProducto(Cod_P);
	}
	
	public static boolean modificarProducto(Producto product) {
		return dao.modificarProducto(product);
	}
	
    public static Map<Integer, Producto> listarProductos() {
		return dao.listarProductos();   
	}
    
    public static ResultadoMedia mediaYMasCaro() {
        return dao.mediaYMasCaro();
    }
}
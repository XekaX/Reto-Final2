package controlador;



import java.util.Map;

import excepciones.CodigoException;
import excepciones.LoginException;
import modelo.Categoria;
import modelo.Producto;
import modelo.ResultadoMedia;
import modelo.Usuario;

public interface Dao {

	public Usuario login(Usuario usuario) throws LoginException;
	
	public boolean añadirProducto (Producto produ) throws CodigoException;

	public Map<Integer, Categoria> listarCategoria();

	public boolean eliminarProductos(Producto produc);
		
	public boolean existeProducto(int Cod_P);
	
	public boolean modificarProducto(Producto produc);
	
	public Map<Integer, Producto> listarProductos();

	public ResultadoMedia mediaYMasCaro();


}

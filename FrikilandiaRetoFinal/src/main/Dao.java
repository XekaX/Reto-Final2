package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import excepciones.CodigoException;
import excepciones.LoginException;
import modelo.Carrito;
import modelo.Carta;
import modelo.Categoria;
import modelo.Cliente;
import modelo.Producto;
import modelo.ResultadoMedia;
import modelo.Trabajador;
import modelo.Usuario;

public interface Dao {

	public Usuario login(Usuario usuario) throws LoginException;

	void registrarCliente(Cliente cliente) throws Exception;

	void registrarCarta(Carta carta) throws Exception;
	
	String generarIdCarta(String tipo) throws Exception;

	List<Producto> obtenerProductos() throws Exception;

	void realizarCompra(Carrito carrito, String dniCliente) throws Exception;

	List<Carta> abrirSobre(int codProducto, String dniCliente) throws Exception;
	
	double obtenerDineroCliente(String dni) throws Exception;
	
	void sumarDineroCliente(String dni, double cantidad) throws Exception;

	void actualizarTrabajador(Trabajador t);

	void eliminarTrabajador(int codT);

	ArrayList<Trabajador> obtenerTrabajadores();

	void insertarTrabajador(Trabajador t);

	boolean añadirProducto(Producto produ) throws CodigoException;

	Map<Integer, Categoria> listarCategoria();

	boolean eliminarProductos(Producto produc);

	boolean existeProducto(int Cod_P);

	boolean modificarProducto(Producto produc);

	Map<Integer, Producto> listarProductos();

	ResultadoMedia mediaYMasCaro();

	double obtenerTotalGastadoCliente(String dni);

	List<String> obtenerHistorialCliente(String dni);
}
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
import vistas.Login;

public class Main {
	
	private static Dao dao = new DaoImplementacionMySql();
	
	public static void main(String[] args) {
		
		Login login = new Login();
		login.setVisible(true);
	}
	
	public static Usuario login(Usuario trabajador) throws LoginException {
		return dao.login(trabajador);
	}
	
	public static void registrarCliente(Cliente cliente) throws Exception {
		dao.registrarCliente(cliente);
	}
	
	public static void registrarCarta(Carta carta) throws Exception {
		dao.registrarCarta(carta);
	}
	
	public static String generarIdCarta(String tipo) throws Exception {
		return dao.generarIdCarta(tipo);
	}
	
	public static List<Producto> obtenerProductos() throws Exception {
		return dao.obtenerProductos();
	}
	
	public static void realizarCompra(Carrito carrito, String dniCliente) throws Exception {
		dao.realizarCompra(carrito, dniCliente);
	}
	
	public static List<Carta> abrirSobre(int codProducto, String dniCliente) throws Exception {
		return dao.abrirSobre(codProducto, dniCliente);
	}
	
	public static double obtenerDineroCliente(String dni) throws Exception {
		return dao.obtenerDineroCliente(dni);
	}
	
	public static void sumarDineroCliente(String dni, double cantidad) throws Exception {
		dao.sumarDineroCliente(dni, cantidad);
	}
	
	public static void actualizarTrabajador(Trabajador t) {
		dao.actualizarTrabajador(t);
	}
	
	public static void eliminarTrabajador(int codT) {
		dao.eliminarTrabajador(codT);
	}
	
	public static ArrayList<Trabajador> obtenerTrabajadores() {
		return dao.obtenerTrabajadores();
	}
	
	public static void insertarTrabajador(Trabajador t) {
		dao.insertarTrabajador(t);
	}
	
	public static boolean añadirProducto(Producto produ) throws CodigoException {
		return dao.añadirProducto(produ);
	}
	
	public static Map<Integer, Categoria> listarCategoria() {
		return dao.listarCategoria();
	}
	
	public static boolean eliminarProductos(Producto produc) {
		return dao.eliminarProductos(produc);
	}
	
	public static boolean existeProducto(int Cod_P) {
		return dao.existeProducto(Cod_P);
	}
	
	public static boolean modificarProducto(Producto produc) {
		return dao.modificarProducto(produc);
	}
	
	public static Map<Integer, Producto> listarProductos() {
		return dao.listarProductos();
	}
	
	public static ResultadoMedia mediaYMasCaro() {
		return dao.mediaYMasCaro();
	}
	
	public static double obtenerTotalGastadoCliente(String dni) {
		return dao.obtenerTotalGastadoCliente(dni);
	}
	
	public static List<String> obtenerHistorialCliente(String dni) {
		return dao.obtenerHistorialCliente(dni);
	}
}

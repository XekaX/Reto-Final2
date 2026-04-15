package main;

import java.util.ArrayList;
import java.util.List;

import excepciones.LoginException;
import modelo.Carrito;
import modelo.Carta;
import modelo.Cliente;
import modelo.Producto;
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
}
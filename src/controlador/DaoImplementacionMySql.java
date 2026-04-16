package controlador;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import excepciones.CodigoException;
import excepciones.LoginException;
import modelo.Categoria;
import modelo.Cliente;
import modelo.Producto;
import modelo.ResultadoMedia;
import modelo.Trabajador;
import modelo.Usuario;
import modelo.Tipo;
import modelo.TipoProducto;

public class DaoImplementacionMySql implements Dao {
	// variables
	private Connection con;
	private PreparedStatement stmt;

	// sentencias Sql
	// Trabajador
	final String LOGIN_TRABAJADOR = "SELECT * FROM Trabajador WHERE Cod_T = ? AND contrasenia = ?";
	// Cliente
	final String LOGIN_CLIENTE = "SELECT * FROM Cliente WHERE DNI = ? AND contrasenia = ?";
	// Producto
	final String AÑADIR_PRODUCTO = "INSERT INTO Producto (cod_P, precio, descripcion, tipo, id_categoria) VALUES (?,?,?,?,?)";
	final String LEER_CATEGORIA = "SELECT * FROM Categoria";
	final String ELIMINAR_PRODUCTO = "DELETE from Producto where Cod_P=?";
	final String EXISTE_PRODUCTO = "SELECT 1 FROM Producto WHERE Cod_P = ?";
	final String MODIFICAR_PRODUCTO = "UPDATE PRODUCTO SET precio=?, descripcion=? WHERE Cod_P=?";
	final String LISTAR_PRODUCTO = "SELECT * FROM Producto";
	final String MEDIA_Y_MAS_CARO = "CALL MEDIA_Y_MAS_CARO()";

	private void openConnection() {
		try {
			con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/frikilandia?serverTimezone=Europe/Madrid&useSSL=false", "root", "abcd*1234");
		}catch (SQLException e) {
			System.out.println("Error al intentar abrir la BD");
		}
	}

	private void closeConnection() throws SQLException {
		if (stmt != null) {
			stmt.close();
		}
		if (con != null) {
			con.close();
		}
	}

	@Override
	public Usuario login(Usuario usuario) throws LoginException {

		ResultSet rs = null;
		Usuario usu = null;

		openConnection();

		try {
			// Buscar trabajador
			stmt = con.prepareStatement(LOGIN_TRABAJADOR);
			stmt.setString(1, usuario.getIdentificacion());
			stmt.setString(2, usuario.getContrasenia());
			rs = stmt.executeQuery();

			if (rs.next()) {

				usu = new Trabajador();

				String tipo = rs.getString("tipo");
				Tipo tipoEnum = Tipo.valueOf(tipo.toUpperCase());

				((Trabajador) usu).setTipo(tipoEnum);
				usu.setIdentificacion(rs.getString("Cod_T"));

			} else {
				// Buscar cliente
				stmt = con.prepareStatement(LOGIN_CLIENTE);
				stmt.setString(1, usuario.getIdentificacion());
				stmt.setString(2, usuario.getContrasenia());
				rs = stmt.executeQuery();

				if (rs.next()) {
					usu = new Cliente();
					usu.setIdentificacion(rs.getString("DNI"));
				} else {
					throw new LoginException("Usuario o contraseña incorrecta");
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				closeConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return usu;
	}

	@Override
	public boolean añadirProducto(Producto produ) throws CodigoException {
		openConnection();
		boolean insertado = false;

		try {
			stmt = con.prepareStatement(AÑADIR_PRODUCTO);
			stmt.setInt(1, produ.getCodP());
			stmt.setFloat(2, produ.getPrecio());
			stmt.setString(3, produ.getDescripcion());
			stmt.setString(4, produ.getTipo().name()); // asumiendo que Tipo es un enum en Producto
			stmt.setInt(5, produ.getIdCategoria());

			int filas = stmt.executeUpdate();
			insertado = filas > 0;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				closeConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return insertado;
	}

	@Override
	public Map<Integer, Categoria> listarCategoria() {
		Map<Integer, Categoria> map = new HashMap<>();
		ResultSet rs = null;
		openConnection();

		try {
			stmt = con.prepareStatement(LEER_CATEGORIA);
			rs = stmt.executeQuery();

			while (rs.next()) {
				Categoria categoria = new Categoria();
				categoria.setId_categoria(rs.getInt("id_categoria"));
				categoria.setNombre(rs.getString("nom_c"));

				map.put(categoria.getId_categoria(), categoria);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				closeConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return map;
	}

	@Override
	public boolean eliminarProductos(Producto produc) {
		openConnection();
		boolean eliminado = false;
		try {
			stmt = con.prepareStatement(ELIMINAR_PRODUCTO);
			stmt.setInt(1, produc.getCodP());

			int filasAfectadas = stmt.executeUpdate();
			if (filasAfectadas > 0) {
				eliminado = true; // Si se eliminó correctamente
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				closeConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return eliminado;
	}

	@Override
	public boolean existeProducto(int Cod_P) {
		openConnection();
		boolean existe = false;
		try {
			stmt = con.prepareStatement(EXISTE_PRODUCTO);
			stmt.setInt(1, Cod_P); // CORREGIDO
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				existe = true; // Si hay un resultado, el producto existe
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				closeConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return existe;
	}

	@Override
	public boolean modificarProducto(Producto produc) {
		openConnection();
		boolean modificado = false;
		try {
			stmt = con.prepareStatement(MODIFICAR_PRODUCTO);
			stmt.setFloat(1, produc.getPrecio());
			stmt.setString(2, produc.getDescripcion());
			stmt.setInt(3, produc.getCodP());

			int filasAfectadas = stmt.executeUpdate();
			if (filasAfectadas > 0) {
				modificado = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				closeConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return modificado;
	}

	@Override
	public Map<Integer, Producto> listarProductos() {
		Map<Integer, Producto> map = new HashMap<>();
		ResultSet rs = null;
		openConnection();

		try {
			stmt = con.prepareStatement(LISTAR_PRODUCTO);
			rs = stmt.executeQuery();

			while (rs.next()) {
				Producto p = new Producto();
				p.setCodP(rs.getInt("cod_P"));
				p.setPrecio(rs.getFloat("precio"));
				p.setDescripcion(rs.getString("descripcion"));
				p.setIdCategoria(rs.getInt("id_categoria"));
				p.setTipo(TipoProducto.valueOf(rs.getString("tipo").toUpperCase()));

				// Agregar el producto al mapa usando su código como clave
				map.put(p.getCodP(), p);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				closeConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return map;
	}

	@Override
	public ResultadoMedia mediaYMasCaro() {

		openConnection();
		ResultadoMedia resultado = new ResultadoMedia();
		ResultSet rs = null;

		try {
			stmt = con.prepareStatement("CALL MEDIA_Y_MAS_CARO()");
			rs = stmt.executeQuery();

			if (rs.next()) {

				resultado.setMedia(rs.getFloat("MEDIA"));
				resultado.setProductoMasCaro(rs.getString("PRODUCTO_MAS_CARO"));
				resultado.setPrecioMaximo(rs.getFloat("PRECIO_MAXIMO"));
				resultado.setClasificacion(rs.getString("CLASIFICACION"));
			}

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			try {
				if (rs != null)
					rs.close();
				closeConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return resultado;
	}

}
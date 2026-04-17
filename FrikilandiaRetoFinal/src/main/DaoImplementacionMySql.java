package main;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import excepciones.CodigoException;
import excepciones.LoginException;
import modelo.Carrito;
import modelo.Carta;
import modelo.Categoria;
import modelo.Cliente;
import modelo.LineaCarrito;
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
	final String LOGIN_TRABAJADOR = "SELECT * FROM Trabajador WHERE Cod_T = ? AND contrasenia = ?";
	final String LOGIN_CLIENTE = "SELECT * FROM Cliente WHERE DNI = ? AND contrasenia = ?";
	final String REGISTRAR_CLIENTE = "INSERT INTO cliente (nombre, dni, telefono, contrasenia) VALUES (?, ?, ?, ?)";
	final String INTRODUCIR_CARTA = "INSERT INTO Carta (id_C, nombre, tipo, precio) VALUES (?, ?, ?, ?)";
	final String CONTADOR_ID = "SELECT COUNT(*) FROM Carta WHERE tipo = ?";
	final String OBTENER_DINERO = "SELECT dinero FROM Cliente WHERE DNI = ?";
	final String ACTUALIZAR_DINERO = "UPDATE Cliente SET dinero = dinero - ? WHERE DNI = ?";
	final String SELECCIONAR_PRODUCTO = "SELECT * FROM Producto";
	final String REALIZAR_COMPRA = "INSERT INTO Compra (fecha, cantidad, cod_P, DNI) VALUES (CURDATE(), ?, ?, ?)";
	final String ABRIR_SOBRE = "UPDATE Compra SET cantidad = cantidad - 1 WHERE cod_P = ? AND DNI = ? LIMIT 1";
	final String SELECCIONAR_CARTA_ESPECIAL = "SELECT * FROM Carta WHERE precio > 5 AND tipo = ? ORDER BY RAND() LIMIT 1";
	final String SELECCIONAR_CARTA_COMUN = "SELECT * FROM Carta WHERE precio <= 5 AND tipo = ? ORDER BY RAND() LIMIT 1";
	final String SUMAR_DINERO = "UPDATE Cliente SET dinero = dinero + ? WHERE DNI = ?";
	final String INSERT_TRABAJADOR = "INSERT INTO Trabajador (Cod_T, nombre, tipo, contrasenia) VALUES (?, ?, ?, ?)";
	final String SELECT_TRABAJADORES = "SELECT * FROM Trabajador";
    final String DELETE_TRABAJADOR = "DELETE FROM Trabajador WHERE Cod_T = ?";
    final String UPDATE_TRABAJADOR = "UPDATE Trabajador SET nombre = ?, tipo = ?, contrasenia = ? WHERE Cod_T = ?";
    final String AÑADIR_PRODUCTO = "INSERT INTO Producto (cod_P, precio, descripcion, tipo, id_categoria) VALUES (?,?,?,?,?)";
    final String LEER_CATEGORIA = "SELECT * FROM Categoria";
    final String ELIMINAR_PRODUCTO ="DELETE from Producto where Cod_P=?";
    final String EXISTE_PRODUCTO = "SELECT 1 FROM Producto WHERE Cod_P = ?";
    final String MODIFICAR_PRODUCTO = "UPDATE PRODUCTO SET precio=?, descripcion=? WHERE Cod_P=?";
    final String LISTAR_PRODUCTO = "SELECT * FROM Producto";
    final String MEDIA_Y_MAS_CARO = "CALL MEDIA_Y_MAS_CARO()";
    final String TOTAL_GASTADO = "SELECT totalGastadoCliente(?)";
    final String HISTORIAL_CLIENTE = "CALL historialComprasCliente(?)";
	

	private void openConnection() {
		try {
			con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/Frikilandia?serverTimezone=Europe/Madrid&useSSL=false", "root",
					"1234");
		} catch (SQLException e) {
			System.out.println("Error al intentar abrir la BD");
		}
	}

	private void closeConnection() throws SQLException {
		if (stmt != null) {
			stmt.close();
		}
		if (con != null)
			con.close();
	}

	@Override
	public Usuario login(Usuario usuario) throws LoginException {

		ResultSet rs = null;
		Usuario usu = null;

		openConnection();

		try {
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
            stmt.setDouble(2, produ.getPrecio());
            stmt.setString(3, produ.getDescripcion());
            stmt.setString(4, produ.getTipo().name());
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
                if (rs != null) rs.close();
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
		        stmt.setInt(1, Cod_P);
		        ResultSet rs = stmt.executeQuery();
		        if (rs.next()) {
		            existe = true;
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
		        stmt.setDouble(1, produc.getPrecio());
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
	            
	            map.put(p.getCodP(), p);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (rs != null) rs.close();
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
	            if (rs != null) rs.close();
	            closeConnection();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    return resultado;
	}
	
	@Override
    public void insertarTrabajador(Trabajador t) {

        openConnection();

        try {
            stmt = con.prepareStatement(INSERT_TRABAJADOR);
            stmt.setInt(1, t.getCod_T());
            stmt.setString(2, t.getNombre());
            stmt.setString(3, t.getTipo().toString());
            stmt.setString(4, t.getContrasenia());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return;
        } finally {
            try {
                closeConnection();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
	
	@Override
    public ArrayList<Trabajador> obtenerTrabajadores() {

        ArrayList<Trabajador> lista = new ArrayList<>();

        openConnection();

        try {
            stmt = con.prepareStatement(SELECT_TRABAJADORES);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                Trabajador t = new Trabajador();
                t.setCod_T(rs.getInt("Cod_T"));
                t.setNombre(rs.getString("nombre"));
                t.setTipo(Tipo.valueOf(rs.getString("tipo").toUpperCase()));
                t.setContrasenia(rs.getString("contrasenia"));

                lista.add(t);
            }

            rs.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return lista;
        } finally {
            try {
                closeConnection();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        return lista;
    }

    @Override
    public void eliminarTrabajador(int codT) {

        openConnection();

        try {
            stmt = con.prepareStatement(DELETE_TRABAJADOR);
            stmt.setInt(1, codT);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return;
        } finally {
            try {
                closeConnection();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public void actualizarTrabajador(Trabajador t) {

        openConnection();

        try {
            stmt = con.prepareStatement(UPDATE_TRABAJADOR);
            stmt.setString(1, t.getNombre());
            stmt.setString(2, t.getTipo().toString());
            stmt.setString(3, t.getContrasenia());
            stmt.setInt(4, t.getCod_T());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return;
        } finally {
            try {
                closeConnection();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

	@Override
	public void registrarCliente(Cliente cliente) throws Exception {

	    openConnection();

	    try {
	        stmt = con.prepareStatement(REGISTRAR_CLIENTE);

	        stmt.setString(1, cliente.getNombre());
	        stmt.setString(2, cliente.getIdentificacion());
	        stmt.setString(3, cliente.getTelefono());
	        stmt.setString(4, cliente.getContrasenia());

	        stmt.executeUpdate();

	    } catch (SQLException e) {
	        throw new Exception("Error al registrar cliente");
	    } finally {
	        closeConnection();
	    }
	}

	@Override
	public void registrarCarta(Carta carta) throws Exception {

		openConnection();
		
		try {
			stmt = con.prepareStatement(INTRODUCIR_CARTA);

			stmt.setString(1, carta.getId_C());
			stmt.setString(2, carta.getNombre());
			stmt.setString(3, carta.getTipo());
			stmt.setDouble(4, carta.getPrecio());

			stmt.executeUpdate();

		} catch (SQLException e) {
			throw new Exception("Error al registrar carta");
		} finally {
			closeConnection();
		}
	}

	public String generarIdCarta(String tipo) throws Exception {

	    ResultSet rs = null;
	    String prefijo = "";

	    switch (tipo) {
	        case "One Piece":
	            prefijo = "OP";
	            break;
	        case "Magic":
	            prefijo = "MG";
	            break;
	        case "Pokemon":
	            prefijo = "PK";
	            break;
	    }

	    openConnection();

	    try {
	        stmt = con.prepareStatement(CONTADOR_ID);
	        stmt.setString(1, tipo);

	        rs = stmt.executeQuery();

	        int numero = 1;

	        if (rs.next()) {
	            numero = rs.getInt(1) + 1;
	        }

	        return String.format("%s-%02d", prefijo, numero);

	    } catch (SQLException e) {
	        throw new Exception("Error generando ID");
	    } finally {
	        if (rs != null) rs.close();
	        closeConnection();
	    }
	}

	@Override
	public List<Producto> obtenerProductos() throws Exception {
	    List<Producto> lista = new ArrayList<>();
	    ResultSet rs = null;

	    openConnection();

	    try {
	        stmt = con.prepareStatement(SELECCIONAR_PRODUCTO);
	        rs = stmt.executeQuery();

	        while (rs.next()) {
	            Producto p = new Producto();
	            p.setCodP(rs.getInt("cod_P"));
	            p.setPrecio(rs.getDouble("precio"));
	            p.setDescripcion(rs.getString("descripcion"));
	            TipoProducto tipoEnum = TipoProducto.valueOf(rs.getString("tipo").toUpperCase());
	            p.setTipo(tipoEnum);

	            lista.add(p);
	        }

	    } catch (SQLException e) {
	        throw new Exception("Error al obtener productos");
	    } finally {
	        if (rs != null) rs.close();
	        closeConnection();
	    }

	    return lista;
	}
	
	@Override
	public void realizarCompra(Carrito carrito, String dniCliente) throws Exception {

	    ResultSet rs = null;

	    openConnection();

	    try {
	        con.setAutoCommit(false);

	        stmt = con.prepareStatement(OBTENER_DINERO);
	        stmt.setString(1, dniCliente);
	        rs = stmt.executeQuery();

	        double dinero = 0;

	        if (rs.next()) {
	            dinero = rs.getDouble("dinero");
	        } else {
	            throw new Exception("Cliente no encontrado");
	        }

	        double total = carrito.calcularTotal();

	        if (dinero < total) {
	            throw new Exception("No tienes suficiente dinero");
	        }

	        stmt = con.prepareStatement(ACTUALIZAR_DINERO);
	        stmt.setDouble(1, total);
	        stmt.setString(2, dniCliente);
	        stmt.executeUpdate();

	        for (LineaCarrito l : carrito.getLineas()) {

	            stmt = con.prepareStatement(REALIZAR_COMPRA);

	            stmt.setInt(1, l.getCantidad());
	            stmt.setInt(2, l.getProducto().getCodP());
	            stmt.setString(3, dniCliente);

	            stmt.executeUpdate();
	        }

	        con.commit();

	    } catch (Exception e) {
	        con.rollback();
	        throw e;

	    } finally {
	        if (rs != null) rs.close();
	        con.setAutoCommit(true);
	        closeConnection();
	    }
	}
	
	@Override
	public double obtenerDineroCliente(String dni) throws Exception {
	    ResultSet rs = null;
	    double dinero = 0;

	    openConnection();

	    try {
	        stmt = con.prepareStatement(OBTENER_DINERO);
	        stmt.setString(1, dni);
	        rs = stmt.executeQuery();

	        if (rs.next()) {
	            dinero = rs.getDouble("dinero");
	        }

	    } catch (SQLException e) {
	        throw new Exception("Error al obtener dinero");
	    } finally {
	        if (rs != null) rs.close();
	        closeConnection();
	    }

	    return dinero;
	}
	
	@Override
	public List<Carta> abrirSobre(int codProducto, String dniCliente) throws Exception {
	    Random rand = new Random();

	    List<Carta> cartas = new ArrayList<>();
	    ResultSet rs = null;

	    openConnection();

	    try {
	        con.setAutoCommit(false);

	        stmt = con.prepareStatement(ABRIR_SOBRE);
	        stmt.setInt(1, codProducto);
	        stmt.setString(2, dniCliente);

	        int filas = stmt.executeUpdate();

	        if (filas == 0) {
	            throw new Exception("No tienes sobres de este tipo");
	        }

	        String tipoCarta = obtenerTipoCarta(codProducto);

	        boolean hayEspecial = rand.nextInt(100) < 30;
	        int posicionEspecial = rand.nextInt(12);

	        for (int i = 0; i < 12; i++) {

	            if (hayEspecial && i == posicionEspecial) {
	                stmt = con.prepareStatement(SELECCIONAR_CARTA_ESPECIAL);
	            } else {
	                stmt = con.prepareStatement(SELECCIONAR_CARTA_COMUN);
	            }

	            stmt.setString(1, tipoCarta);

	            rs = stmt.executeQuery();

	            if (rs.next()) {
	                Carta c = new Carta();
	                c.setId_C(rs.getString("id_C"));
	                c.setNombre(rs.getString("nombre"));
	                c.setTipo(rs.getString("tipo"));

	                double precio = rs.getDouble("precio");

	                boolean foil = rand.nextInt(5) == 0;
	                c.setFoil(foil);

	                if (foil) {
	                    precio *= 2;
	                }

	                c.setPrecio(precio);

	                cartas.add(c);
	            }
	        }

	        con.commit();

	    } catch (Exception e) {
	        con.rollback();
	        throw e;

	    } finally {
	        if (rs != null) rs.close();
	        con.setAutoCommit(true);
	        closeConnection();
	    }

	    return cartas;
	}
	
	private String obtenerTipoCarta(int codProducto) {
	    switch (codProducto) {
	        case 101: return "Pokemon";
	        case 102: return "Magic";
	        case 103: return "One Piece";
	    }
	    return "";
	}
	
	@Override
	public void sumarDineroCliente(String dni, double cantidad) throws Exception {
	    openConnection();

	    try {
	        stmt = con.prepareStatement(SUMAR_DINERO);
	        stmt.setDouble(1, cantidad);
	        stmt.setString(2, dni);
	        stmt.executeUpdate();

	    } catch (SQLException e) {
	        throw new Exception("Error al sumar dinero");
	    } finally {
	        closeConnection();
	    }
	}

	@Override
	public double obtenerTotalGastadoCliente(String dni) {
	    ResultSet rs = null;
	    double total = 0;

	    openConnection();

	    try {
	        stmt = con.prepareStatement(TOTAL_GASTADO);
	        stmt.setString(1, dni);

	        rs = stmt.executeQuery();

	        if (rs.next()) {
	            total = rs.getDouble(1);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            closeConnection();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    return total;
	}
	
	@Override
	public List<String> obtenerHistorialCliente(String dni) {
	    List<String> lista = new ArrayList<>();
	    ResultSet rs = null;

	    openConnection();

	    try {
	        stmt = con.prepareStatement(HISTORIAL_CLIENTE);
	        stmt.setString(1, dni);

	        rs = stmt.executeQuery();

	        while (rs.next()) {
	            String linea = rs.getString("fecha") + " - " +
	                           rs.getString("descripcion") + " - " +
	                           rs.getInt("cantidad") + " - " +
	                           rs.getDouble("precio") + "€";

	            lista.add(linea);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            closeConnection();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    return lista;
	}
}
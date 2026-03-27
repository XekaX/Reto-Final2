package controlador;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import excepciones.LoginException;
import modelo.Cliente;
import modelo.Tipo;
import modelo.Trabajador;
import modelo.Usuario;

public class DaoImplementacionMySql implements Dao {

    private Connection con;
    private PreparedStatement stmt;

    final String LOGIN_TRABAJADOR = "SELECT * FROM Trabajador WHERE Cod_T = ? AND contrasenia = ?";
    final String LOGIN_CLIENTE = "SELECT * FROM Cliente WHERE DNI = ? AND contrasenia = ?";

    final String INSERT_TRABAJADOR =
            "INSERT INTO Trabajador (Cod_T, nombre, tipo, contrasenia) VALUES (?, ?, ?, ?)";

    final String SELECT_TRABAJADORES =
            "SELECT * FROM Trabajador";

    final String DELETE_TRABAJADOR =
            "DELETE FROM Trabajador WHERE Cod_T = ?";

    final String UPDATE_TRABAJADOR =
            "UPDATE Trabajador SET nombre = ?, tipo = ?, contrasenia = ? WHERE Cod_T = ?";

    private void openConnection() {
        try {
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/Frikilandia?serverTimezone=Europe/Madrid&useSSL=false",
                    "root",
                    "abcd*1234");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void closeConnection() throws SQLException {
        if (stmt != null) stmt.close();
        if (con != null) con.close();
    }

    @Override
    public Usuario login(Usuario usuario) throws LoginException {

        ResultSet rs = null;
        Usuario usu = null;

        openConnection();

        try {

            stmt = con.prepareStatement(LOGIN_TRABAJADOR);
            stmt.setInt(1, Integer.parseInt(usuario.getIdentificacion()));
            stmt.setString(2, usuario.getContrasenia());
            rs = stmt.executeQuery();

            if (rs.next()) {

                usu = new Trabajador();

                String tipo = rs.getString("tipo");
                Tipo tipoEnum = Tipo.valueOf(tipo.toUpperCase());

                ((Trabajador) usu).setTipo(tipoEnum);
                ((Trabajador) usu).setNombre(rs.getString("nombre"));
                ((Trabajador) usu).setCod_P(rs.getInt("Cod_T"));

                usu.setIdentificacion(rs.getString("Cod_T"));

            } else {

                if (rs != null) rs.close();
                if (stmt != null) stmt.close();

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
            System.out.println(e.getMessage());
            return null;
        } finally {

            try {
                if (rs != null) rs.close();
                closeConnection();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        return usu;
    }

    // ---------------- ADMIN ----------------

    @Override
    public void insertarTrabajador(Trabajador t) {

        openConnection();

        try {
            stmt = con.prepareStatement(INSERT_TRABAJADOR);
            stmt.setInt(1, t.getCod_P());
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
                t.setCod_P(rs.getInt("Cod_T"));
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
            stmt.setInt(4, t.getCod_P());

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
}
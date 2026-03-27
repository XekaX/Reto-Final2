package controlador;

import java.util.ArrayList;

import excepciones.LoginException;
import modelo.Trabajador;
import modelo.Usuario;
import vistas.Login;

public class Main {

    private static Dao dao = new DaoImplementacionMySql();

    public static void main(String[] args) {

        Login login = new Login();
        login.setVisible(true);
    }

    public static Usuario login(Usuario usuario) throws LoginException {
        return dao.login(usuario);
    }

    public static void insertarTrabajador(Trabajador t) {
        dao.insertarTrabajador(t);
    }

    public static ArrayList<Trabajador> obtenerTrabajadores() {
        return dao.obtenerTrabajadores();
    }

    public static void eliminarTrabajador(int codT) {
        dao.eliminarTrabajador(codT);
    }

    public static void actualizarTrabajador(Trabajador t) {
        dao.actualizarTrabajador(t);
    }
}
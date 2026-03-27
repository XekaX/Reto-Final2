package controlador;

import java.util.ArrayList;

import excepciones.LoginException;
import modelo.Trabajador;
import modelo.Usuario;

public interface Dao {

    public Usuario login(Usuario usuario) throws LoginException;

    // ADMIN
    public void insertarTrabajador(Trabajador t);
    public ArrayList<Trabajador> obtenerTrabajadores();
    public void eliminarTrabajador(int codT);
    public void actualizarTrabajador(Trabajador t); // 🔥 NUEVO
}
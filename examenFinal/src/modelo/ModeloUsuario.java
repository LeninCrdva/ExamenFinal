package modelo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lenin
 */
public class ModeloUsuario extends Usuario {

    public ModeloUsuario() {
    }

    public ModeloUsuario(boolean permiso, int idUsuario, String nombre, String contrasena, byte imagen) {
        super(permiso, idUsuario, nombre, contrasena, imagen);
    }

    public List<Usuario> listUsuario() {
        List<Usuario> lista = new ArrayList<>();

        String sql = "SELECT * FROM usuario";
        ConnectionCase con = new ConnectionCase();
        ResultSet rs = con.Consulta(sql);
        try {
            while (rs.next()) {
                Usuario user = new Usuario();
                user.setIdUsuario(rs.getInt(5));
                user.setNombre(rs.getString(1));
                user.setPermiso(rs.getBoolean(3));
                //user.setImagen(rs.getByte(4));
                lista.add(user);
            }
            rs.close();
            return lista;
        } catch (SQLException ex) {
            Logger.getLogger(ModeloUsuario.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public List<Usuario> listUsuarioSearch(String consulta) {
        List<Usuario> lista = new ArrayList<>();

        String sql = "SELECT * FROM usuario WHERE idUsuario = " + consulta + " OR nombre like '%" + consulta + "'%";
        ConnectionCase con = new ConnectionCase();
        ResultSet rs = con.Consulta(sql);
        try {
            while (rs.next()) {
                Usuario user = new Usuario();
                user.setIdUsuario(rs.getInt(1));
                user.setNombre(rs.getString(2));
                user.setPermiso(rs.getBoolean(3));
                //user.setImagen(rs.getByte(5));
                lista.add(user);
            }
            return lista;
        } catch (SQLException ex) {
            Logger.getLogger(ModeloUsuario.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public SQLException GrabaUsuarioDB() {
        String sql = "INSERT INTO usuario (nombre, contrasena, permiso, foto) "
                + "VALUES ('" + getNombre() + "','" + getContrasena() + "',"
                + isPermiso() + ", '" + getImagen() + "')";

        ConnectionCase con = new ConnectionCase();
        SQLException ex = con.Accion(sql);
        return ex;
    }

    public SQLException EditPersonaDB() {
        String sql = "UPDATE usuario SET nombre = '" + getNombre() + "',"
                + " contrasena = '" + getContrasena() + "', permiso = " + isPermiso() + ", foto = '" + getImagen()
                + "' WHERE idUsuario = " + getIdUsuario() + "";

        ConnectionCase con = new ConnectionCase();
        SQLException ex = con.Accion(sql);
        return ex;
    }

    public SQLException DeletePhisicPerson() {
        String sql = "DELETE FROM usuario WHERE idUsuario = " + getIdUsuario() + "";

        ConnectionCase con = new ConnectionCase();
        SQLException ex = con.Accion(sql);
        return ex;
    }

    public Usuario getUser(int valor) {
        try {
            Usuario user = new Usuario();

            String sql = "SELECT * FROM usuario where idusuario = " + valor;
            ConnectionCase con = new ConnectionCase();
            ResultSet rs = con.Consulta(sql);

            while (rs.next()) {
                user.setIdUsuario(rs.getInt(5));
                user.setNombre(rs.getString(1));
                user.setPermiso(rs.getBoolean(3));
                //user.setImagen(rs.getByte(5));
            }
            return user;
        } catch (SQLException ex) {
            Logger.getLogger(ModeloUsuario.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}

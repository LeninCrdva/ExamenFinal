package modelo;

public class Persona {
    private int idUsuario;
    private String nombre;
    private String contrasena;
    private byte imagen;

    public Persona() {
    }

    public Persona(int idUsuario, String nombre, String contrasena, byte imagen) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.contrasena = contrasena;
        this.imagen = imagen;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public byte getImagen() {
        return imagen;
    }

    public void setImagen(byte imagen) {
        this.imagen = imagen;
    }

    @Override
    public String toString() {
        return idUsuario + nombre + contrasena + imagen;
    }
    
    
}

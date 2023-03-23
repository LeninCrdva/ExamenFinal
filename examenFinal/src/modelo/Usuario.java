package modelo;

public class Usuario extends Persona{
    private boolean permiso;

    public Usuario() {
    }

    public Usuario(boolean permiso) {
        this.permiso = permiso;
    }

    public Usuario(boolean permiso, int idUsuario, String nombre, String contrasena, byte imagen) {
        super(idUsuario, nombre, contrasena, imagen);
        this.permiso = permiso;
    }

    public boolean isPermiso() {
        return permiso;
    }

    public void setPermiso(boolean permiso) {
        this.permiso = permiso;
    }

    @Override
    public String toString() {
        return String.valueOf(permiso);
    }
    
    
}

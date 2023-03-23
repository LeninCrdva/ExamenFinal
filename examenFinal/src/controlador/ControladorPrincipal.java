package controlador;

import modelo.ModeloUsuario;
import vista.VistaPrincipal;
import vista.VistaUsuarios;

public class ControladorPrincipal {
    VistaPrincipal vistaPrincipal;

    public ControladorPrincipal(VistaPrincipal vista) {
        this.vistaPrincipal = vista;
        this.vistaPrincipal.setVisible(true);
        this.vistaPrincipal.setLocationRelativeTo(null);
        vistaPrincipal.setVisible(true);
    }
    
    public void iniciarControl(){
        vistaPrincipal.getBtnUsuario().addActionListener(l -> crudUsuario());
    }
    
    private void crudUsuario(){
        VistaUsuarios vista = new VistaUsuarios();
        ModeloUsuario md = new ModeloUsuario();
        
        ControladorUsuario control = new ControladorUsuario(vista, md);
        
        vistaPrincipal.getDskPrin().add(vista);
        
        control.iniciarControl();
    }
}

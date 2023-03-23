package mainMVC;

import controlador.ControladorPrincipal;
import vista.VistaPrincipal;

public class MainMVC {

    public static void main(String[] args) {
        VistaPrincipal vs = new VistaPrincipal();
        ControladorPrincipal ctr = new ControladorPrincipal(vs);
        ctr.iniciarControl();
    }
}

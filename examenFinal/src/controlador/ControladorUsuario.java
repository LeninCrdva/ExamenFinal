package controlador;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import modelo.ConnectionCase;
import modelo.ModeloUsuario;
import modelo.Usuario;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import vista.VistaUsuarios;

/**
 *
 * @author Lenin
 */
public class ControladorUsuario {

    private final VistaUsuarios vista;
    private final ModeloUsuario modelo;

    public ControladorUsuario(VistaUsuarios vista, ModeloUsuario modelo) {
        this.vista = vista;
        this.modelo = modelo;
        vista.setVisible(true);
    }

    public void iniciarControl() {
        cargarUsuarios();
        vista.getBtnCrear().addActionListener(l -> abrirDialogo(1));
        vista.getBtnActualizar().addActionListener(l -> abrirDialogo(2));
        vista.getBtnAceptar().addActionListener(l -> crearEditarPersona());
        vista.getBtnCancelar().addActionListener(l -> cleanCamps());
        vista.getBtnSubir().addActionListener(l -> cargarImg());
        vista.getTxtBuscar().addActionListener(l -> buscarUsuario());
        vista.getBtnEliminar().addActionListener(l -> eliminarUsuario(vista.getTblUsuarios()));
        vista.getBtnPrint().addActionListener(l -> imprimirPersona());
    }

    private void cargarUsuarios() {
        //Control para consultar a la BD/modelo y luego cargar en la vista
        List<Usuario> listap = modelo.listUsuario();
        
        DefaultTableModel mTabla;
        mTabla = (DefaultTableModel) vista.getTblUsuarios().getModel();
        mTabla.setNumRows(0);

        vista.getTblUsuarios().getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        vista.getTblUsuarios().getTableHeader().setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        vista.getTblUsuarios().getTableHeader().setOpaque(false);
        vista.getTblUsuarios().getTableHeader().setBackground(new Color(32, 136, 203));
        vista.getTblUsuarios().getTableHeader().setForeground(new Color(255, 255, 255));
        vista.getTblUsuarios().setRowHeight(25);

        listap.stream().forEach(us -> {
            String[] filanueva = {String.valueOf(us.getIdUsuario()), us.getNombre(), String.valueOf(us.isPermiso())};
            mTabla.addRow(filanueva);
        });
    }

    private void abrirDialogo(int ce) {
        String title;
        boolean openwindow = false;
        if (ce == 1) {
            title = "Crear Persona";
            vista.getDlgCrear().setName("crear");
            vista.getTxtContra().setEnabled(true);
            openwindow = true;
        } else {
            title = "Editar Persona";
            vista.getDlgCrear().setName("editar");
            try {
                openwindow = uploadDates(vista.getTblUsuarios());
            } catch (ParseException ex) {
                Logger.getLogger(ControladorUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }
            vista.getTxtContra().setEnabled(false);
        }

        if (openwindow) {
            vista.getDlgCrear().setLocationRelativeTo(null);
            vista.getDlgCrear().setSize(380, 250);
            vista.getDlgCrear().setTitle(title);
            vista.getDlgCrear().setVisible(true);
        }
    }

    private boolean uploadDates(JTable table) throws ParseException {
        boolean a = false;
        if (table.getSelectedRowCount() == 1) {
            a = true;
            Usuario user = modelo.getUser(Integer.parseInt(vista.getTblUsuarios().getValueAt(vista.getTblUsuarios().getSelectedRow(), 0).toString()));
            vista.getLblID().setText((String.valueOf(vista.getTblUsuarios().getValueAt(vista.getTblUsuarios().getSelectedRow(), 0))));
            vista.getTxtNombre().setText((String.valueOf(vista.getTblUsuarios().getValueAt(vista.getTblUsuarios().getSelectedRow(), 1))));
            vista.getLblNombre().setText(String.valueOf(vista.getTblUsuarios().getValueAt(vista.getTblUsuarios().getSelectedRow(), 1)));
            vista.getTxtContra().setText(user.getContrasena());
            vista.getCckPermisos().setSelected(Boolean.parseBoolean(vista.getTblUsuarios().getValueAt(vista.getTblUsuarios().getSelectedRow(), 2).toString()));
        } else {
            JOptionPane.showMessageDialog(null, "SELECCIONE UNA FILA DE LA TABLA");
        }
        return a;
    }

    private void imprimirPersona() {
        if (vista.getTblUsuarios().getSelectedRowCount() != 0) {
            try {
                ConnectionCase con = new ConnectionCase();
                JasperReport jr = (JasperReport) JRLoader.loadObject(getClass().getResource("/Vista/Reporte/reporte.jasper"));
                //DECLARACION DEL MAP Y AGREGACIÓN DE LOS DATOS, SIN EMBARGO, SE DEBEN CAMBIAR POR DATOS DINAMICOS
                Map<String, Object> parametro = new HashMap<>();
                parametro.put("ID", Integer.valueOf(vista.getTblUsuarios().getValueAt(vista.getTblUsuarios().getSelectedRow(), 0).toString()));
                JasperPrint jp = JasperFillManager.fillReport(jr, parametro, con.getCon());
                JasperViewer jv = new JasperViewer(jp, false);
                jv.setVisible(true);
            } catch (JRException ex) {
                Logger.getLogger(ControladorUsuario.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "NECESITA SELECCIONAR UNA FILA DE LA TABLA");
        }
    }

    private void crearEditarPersona() {
        if (vista.getDlgCrear().getName().equals("crear")) {
            //INSERTAR
            String nombre = vista.getTxtNombre().getText();
            String contra = vista.getTxtContra().getText();
            boolean permiso = vista.getCckPermisos().isSelected();
            //byte foto = Byte.parseByte(vista.getLblImg().getIcon().toString());

            modelo.setNombre(nombre);
            modelo.setContrasena(contra);
            modelo.setPermiso(permiso);
            //modelo.setImagen(foto);
            if (!nombre.isEmpty() || !contra.isEmpty()) {

                if (modelo.GrabaUsuarioDB() == null) {
                    JOptionPane.showMessageDialog(null, "SE HA CREADO A LA PERSONA CON ÉXITO");
                } else {
                    JOptionPane.showMessageDialog(null, "NO SE HA PODIDO CREAR A LA PERSONA");
                }
            } else {
                JOptionPane.showMessageDialog(null, "NECESITA PROPORCIONAR DATOS");
            }
        } else if (vista.getDlgCrear().getName().equals("editar")) {
            //EDITAR
            String id = vista.getLblID().getText();
            String nombre = vista.getTxtNombre().getText();
            String contra = vista.getTxtContra().getText();
            boolean permiso = vista.getCckPermisos().isSelected();
            //byte foto = Byte.parseByte(vista.getLblImg().getText());
            if (!nombre.isEmpty() || !contra.isEmpty() || !id.isEmpty()) {
                modelo.setIdUsuario(Integer.parseInt(id));
                modelo.setNombre(nombre);
                modelo.setContrasena(contra);
                modelo.setPermiso(permiso);
                //modelo.setImagen(foto);

                if (modelo.EditPersonaDB() == null) {
                    JOptionPane.showMessageDialog(null, "SE HA EDITADO A LA PERSONA CON ÉXITO");
                } else {
                    JOptionPane.showMessageDialog(null, "NO SE HA PODIDO EDITAR A LA PERSONA");
                }
            } else {
                JOptionPane.showMessageDialog(null, "NO SE PUEDEN INGRESAR DATOS VACIOS");
            }

        }
        cleanCamps();
        cargarUsuarios();
    }

    private void eliminarUsuario(JTable table) {
        if (table.getSelectedRowCount() == 1) {
            modelo.setIdUsuario(Integer.parseInt((table.getValueAt(table.getSelectedRow(), 0)).toString()));
            int confirmado = JOptionPane.showConfirmDialog(null, "ESTÁ SEGURO DE ELIMINAR A LA PERSONA");
            if (JOptionPane.OK_OPTION == confirmado) {
                if (modelo.DeletePhisicPerson() == null) {
                    JOptionPane.showMessageDialog(null, "SE HA ELIMNADO A LA PERSONA CON ÉXITO");
                } else {
                    JOptionPane.showMessageDialog(null, "NO SE HA PODIDO ELIMINAR A LA PERSONA");
                }
            } else {
                System.out.println("SE HA CANCELADO LA OPERACION");
            }

        } else {
            JOptionPane.showMessageDialog(null, "NECESITA SELECCIONAR UNA FILA PRIMERO");
        }
    }

    private void buscarUsuario() {
        String consulta = vista.getTxtBuscar().getText();
        List<Usuario> listap = modelo.listUsuarioSearch(consulta);

        DefaultTableModel mTabla;
        mTabla = (DefaultTableModel) vista.getTblUsuarios().getModel();
        mTabla.setNumRows(0);
        vista.getTblUsuarios().getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        vista.getTblUsuarios().getTableHeader().setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        vista.getTblUsuarios().getTableHeader().setOpaque(false);
        vista.getTblUsuarios().getTableHeader().setBackground(new Color(32, 136, 203));
        vista.getTblUsuarios().getTableHeader().setForeground(new Color(255, 255, 255));
        vista.getTblUsuarios().setRowHeight(25);

        listap.stream().forEach(us -> {
            String[] filanueva = {String.valueOf(us.getIdUsuario()), us.getNombre(), String.valueOf(us.isPermiso())};
            mTabla.addRow(filanueva);
        });
    }

    private void cleanCamps() {
        vista.getTxtBuscar().setText("");
        vista.getTxtContra().setText("");
        vista.getTxtNombre().setText("");
        vista.getCckPermisos().setSelected(false);
        vista.getDlgCrear().dispose();
    }

    private void cargarImg() {
        String Ruta = "";
        JFileChooser jFileChooser = new JFileChooser();
        FileNameExtensionFilter filtrado = new FileNameExtensionFilter("JGP, PNG & GIF", "jpg", "png", "gif");
        jFileChooser.setFileFilter(filtrado);

        int respuesta = jFileChooser.showOpenDialog(vista.getDlgCrear());

        if (respuesta == JFileChooser.APPROVE_OPTION) {
            Ruta = jFileChooser.getSelectedFile().getPath();

            Image mImagen = new ImageIcon(Ruta).getImage();
            ImageIcon mIcono = new ImageIcon(mImagen.getScaledInstance(vista.getLblImg().getWidth(), vista.getLblImg().getHeight(), Image.SCALE_SMOOTH));
            vista.getLblImg().setIcon(mIcono);

        }
    }
}

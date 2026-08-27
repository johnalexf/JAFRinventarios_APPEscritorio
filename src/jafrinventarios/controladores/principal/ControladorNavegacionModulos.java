
package jafrinventarios.controladores.principal;

import jafrinventarios.controladores.clientes.ControladorClientes;
import jafrinventarios.controladores.perfil.ControladorPerfil;
import jafrinventarios.controladores.productos.ControladorProductos;
import jafrinventarios.controladores.proveedores.ControladorProveedores;
import jafrinventarios.controladores.usuarios.ControladorUsuarios;
import jafrinventarios.modelos.ModeloSesionUsuario;
import jafrinventarios.servicios.clientes.ServicioClientes;
import jafrinventarios.servicios.productos.ServicioProductos;
import jafrinventarios.servicios.proveedores.ServicioProveedores;
import jafrinventarios.servicios.usuarios.ServicioUsuarios;
import jafrinventarios.vistas.clientes.ClientesPanel;
import jafrinventarios.vistas.perfil.PerfilPanel;
import jafrinventarios.vistas.principal.DialogoMenu;
import jafrinventarios.vistas.principal.HeaderPanel;
import jafrinventarios.vistas.principal.Menu;
import jafrinventarios.vistas.principal.PrincipalFrame;
import jafrinventarios.vistas.productos.ProductosPanel;
import jafrinventarios.vistas.proveedores.ProveedoresPanel;
import jafrinventarios.vistas.usuarios.UsuariosPanel;
import jafrinventarios.vistas.utilidades.iconos.IconosSecciones;
import javax.swing.JPanel;

/**
 *
 * @author JOHN FORERO
 */
public class ControladorNavegacionModulos {
    
    private final PrincipalFrame ventanaPrincipal;
    private final HeaderPanel headerPanel;
    private final DialogoMenu dialogoMenu;
    private JPanel moduloActual;
    private boolean esAdministrador;

    
    private final Menu menuFlotante;
    /*
    Creamos un menu fijo para el modulo de perfil ya que el mismo menu no puede
    existir en jdialog y en perfilPanel a la vez
    */
    private final Menu menuFijoPerfil;
    
    public ControladorNavegacionModulos(PrincipalFrame ventanaPrincipal) {
        this.ventanaPrincipal = ventanaPrincipal;
        this.headerPanel = ventanaPrincipal.getHeaderPanel();
        
        consultarSiEsAdministrador();
        
        this.menuFlotante = new Menu(esAdministrador);
        this.menuFijoPerfil = new Menu(esAdministrador);
        
        this.dialogoMenu = new DialogoMenu(
                                    ventanaPrincipal, 
                                    ventanaPrincipal.getContenedorPrincipal(), 
                                    menuFlotante);
        asignarNombreEmpresa();
        asignarNombreRolUsuario();
        cambiarSeccion(IconosSecciones.INICIO);
        
        inicializarEventos();
    }
    
    private void consultarSiEsAdministrador(){
        this.esAdministrador = ModeloSesionUsuario.getInstancia().isAdministrador();
    }
    
    
    private void asignarNombreEmpresa(){
        headerPanel.asignarNombreEmpresa(
                ModeloSesionUsuario.getInstancia().getNombreEmpresa()
        );
    }
    
    
    private void asignarNombreRolUsuario(){
        headerPanel.asignarNombreRolUsuario(
                ModeloSesionUsuario.getInstancia().getNombreRolUsuario()
        );
    }
    
    
    private void inicializarEventos() {
        // Nos conectamos al botón del Header pasando por el Frame
        headerPanel.getBtnMenu().addActionListener( e -> mostrarDialogoMenu());
        
        configurarEventosBotonesMenu(menuFlotante);
        configurarEventosBotonesMenu(menuFijoPerfil);
        
    }
    
    
    private void configurarEventosBotonesMenu(Menu menuPanel){
        menuPanel.getBtnLinkSeccionInicio().addActionListener( e -> cambiarSeccion(IconosSecciones.INICIO));
        menuPanel.getBtnLinkSeccionProductos().addActionListener( e -> cambiarSeccion(IconosSecciones.PRODUCTOS));
        menuPanel.getBtnLinkSeccionClientes().addActionListener( e -> cambiarSeccion(IconosSecciones.CLIENTES));
        menuPanel.getBtnLinkSeccionVentas().addActionListener( e -> cambiarSeccion(IconosSecciones.VENTAS));
        menuPanel.getBtnLinkSeccionInventario().addActionListener( e -> cambiarSeccion(IconosSecciones.INVENTARIO));
        
        if(esAdministrador){
            menuPanel.getBtnLinkSeccionUsuarios().addActionListener( e -> cambiarSeccion(IconosSecciones.USUARIOS));           
            menuPanel.getBtnLinkSeccionProveedores().addActionListener( e -> cambiarSeccion(IconosSecciones.PROVEEDORES));
            menuPanel.getBtnLinkSeccionCompras().addActionListener( e -> cambiarSeccion(IconosSecciones.COMPRAS));
            menuPanel.getBtnLinkSeccionReporte().addActionListener( e -> cambiarSeccion(IconosSecciones.REPORTE));
        }
    
    }

    
    private void mostrarDialogoMenu() {       
        dialogoMenu.mostrar();
    }
    
    
    private void cambiarSeccion(IconosSecciones seccionAsignada){
        
        headerPanel.asignarSeccion(seccionAsignada);
        
        moduloActual = new JPanel();
        
        //TODO: Pendiente switch para crear el controlador de cada jpanel de la seccion seleccionada
        switch(seccionAsignada){
            case INICIO:
                moduloActual =  new PerfilPanel(menuFijoPerfil, esAdministrador);
                new ControladorPerfil( (PerfilPanel) moduloActual, new ServicioUsuarios() );
                break;
            case USUARIOS:
                moduloActual =  new UsuariosPanel();
                new ControladorUsuarios( (UsuariosPanel)  moduloActual, new ServicioUsuarios() );
                break;
            case PRODUCTOS:
                moduloActual = new ProductosPanel();
                new ControladorProductos( (ProductosPanel) moduloActual, new ServicioProductos() );
                break;
            case PROVEEDORES:
                moduloActual = new ProveedoresPanel();
                new ControladorProveedores( (ProveedoresPanel) moduloActual, new ServicioProveedores() );
                break;
            case CLIENTES:
                moduloActual = new ClientesPanel();
                new ControladorClientes( (ClientesPanel) moduloActual, new ServicioClientes() );
                break;
        }
        
       ventanaPrincipal.agregarPanelModulo(moduloActual);
        
       dialogoMenu.dispose();
    }
    
    
    
    
}

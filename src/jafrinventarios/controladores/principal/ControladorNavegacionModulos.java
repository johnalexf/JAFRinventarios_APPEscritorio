/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jafrinventarios.controladores.principal;

import jafrinventarios.vistas.perfil.PerfilPanel;
import jafrinventarios.vistas.principal.DialogoMenu;
import jafrinventarios.vistas.principal.HeaderPanel;
import jafrinventarios.vistas.principal.Menu;
import jafrinventarios.vistas.principal.PrincipalFrame;
import jafrinventarios.vistas.utilidades.iconos.IconosSecciones;
import javax.swing.JPanel;

/**
 *
 * @author JOHN FORERO
 */
public class ControladorNavegacionModulos {
    
    private PrincipalFrame ventanaPrincipal;
    private HeaderPanel headerPanel;
    private DialogoMenu dialogoMenu;
    private JPanel moduloActual;
    private boolean esAdministrador;

    
    private Menu menuFlotante;
    /*
    Creamos un menu fijo para el modulo de perfil ya que el mismo menu no puede
    existir en jdialog y en perfilPanel a la vez
    */
    private Menu menuFijoPerfil;
    
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
        /*
        TODO consulta a la base de datos o al singleton que almacene la sesion
        del usuario si es administrador
        */
        this.esAdministrador = true;
    }
    
    private void asignarNombreEmpresa(){
        
        //TODO Consultar a la base de datos
        //Simulacion consulta
        String nombreEmpresa = "Nombre de la empresa";
        
        headerPanel.asignarNombreEmpresa(nombreEmpresa);
    }
    
    
    private void asignarNombreRolUsuario(){
        
        //TODO Consultar a la base de datos
        //Simulacion consulta
        String nombreRol = "Administrador";
        
        headerPanel.asignarNombreRolUsuario(nombreRol);
        menuFlotante.setTituloRol(nombreRol);
        menuFijoPerfil.setTituloRol(nombreRol);
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
        dialogoMenu.hacerVisibleDialogo();
    }
    
    
    private void cambiarSeccion(IconosSecciones seccionAsignada){
        
        headerPanel.asignarSeccion(seccionAsignada);
        
        moduloActual = new JPanel();
        
        //TODO: Pendiente switch para crear el controlador de cada jpanel de la seccion seleccionada
        switch(seccionAsignada){
            case INICIO:
                moduloActual =  new PerfilPanel(menuFijoPerfil, esAdministrador);
                break;
                
        }
        
       ventanaPrincipal.agregarPanelModulo(moduloActual);
        
       dialogoMenu.dispose();
    }
    
    
    
    
}

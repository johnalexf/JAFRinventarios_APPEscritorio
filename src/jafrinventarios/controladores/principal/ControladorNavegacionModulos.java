/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jafrinventarios.controladores.principal;

import jafrinventarios.vistas.principal.DialogoMenu;
import jafrinventarios.vistas.principal.HeaderPanel;
import jafrinventarios.vistas.principal.Menu;
import jafrinventarios.vistas.principal.PrincipalFrame;
import jafrinventarios.vistas.utilidades.iconos.IconosSecciones;

/**
 *
 * @author JOHN FORERO
 */
public class ControladorNavegacionModulos {
    
    private PrincipalFrame ventanaPrincipal;
    private HeaderPanel headerPanel;
    private Menu menuPanel;
    private DialogoMenu dialogoMenu;

    
    public ControladorNavegacionModulos(PrincipalFrame ventanaPrincipal) {
        this.ventanaPrincipal = ventanaPrincipal;
        this.headerPanel = ventanaPrincipal.getHeaderPanel();
        
        this.menuPanel = new Menu();
        
        this.dialogoMenu = new DialogoMenu(
                                    ventanaPrincipal, 
                                    ventanaPrincipal.getContenedorPrincipal(), 
                                    menuPanel);
        
        cambiarSeccion(IconosSecciones.INICIO);
        
        inicializarEventos();
    }
    
    
    private void inicializarEventos() {
        // Nos conectamos al botón del Header pasando por el Frame
        headerPanel.getBtnMenu().addActionListener( e -> mostrarDialogoMenu());
        menuPanel.getBtnLinkSeccionInicio().addActionListener( e -> cambiarSeccion(IconosSecciones.INICIO));
        menuPanel.getBtnLinkSeccionUsuarios().addActionListener( e -> cambiarSeccion(IconosSecciones.USUARIOS));
        menuPanel.getBtnLinkSeccionProductos().addActionListener( e -> cambiarSeccion(IconosSecciones.PRODUCTOS));
        menuPanel.getBtnLinkSeccionProveedores().addActionListener( e -> cambiarSeccion(IconosSecciones.PROVEEDORES));
        menuPanel.getBtnLinkSeccionClientes().addActionListener( e -> cambiarSeccion(IconosSecciones.CLIENTES));
        menuPanel.getBtnLinkSeccionCompras().addActionListener( e -> cambiarSeccion(IconosSecciones.COMPRAS));
        menuPanel.getBtnLinkSeccionVentas().addActionListener( e -> cambiarSeccion(IconosSecciones.VENTAS));
        menuPanel.getBtnLinkSeccionInventario().addActionListener( e -> cambiarSeccion(IconosSecciones.INVENTARIO));
        menuPanel.getBtnLinkSeccionReporte().addActionListener( e -> cambiarSeccion(IconosSecciones.REPORTE));
    }

    
    private void mostrarDialogoMenu() {       
        dialogoMenu.hacerVisibleDialogo();
    }
    
    private void cambiarSeccion(IconosSecciones seccionAsignada){
        
        headerPanel.asignarNombreSeccion(seccionAsignada);
        
        //TODO: Pendiente switch para crear el controlador de cada jpanel de la seccion seleccionada
        
        dialogoMenu.dispose();
    }
    
    
    
    
}

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
import jafrinventarios.vistas.utilidades.dialogos.DialogoBaseConSombra;

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
        
        inicializarEventos();
    }
    
    private void inicializarEventos() {
        // Nos conectamos al botón del Header pasando por el Frame
        headerPanel.getBtnMenu().addActionListener( e -> mostrarDialogoMenu());
    }

    
    private void mostrarDialogoMenu() {       
        dialogoMenu.hacerVisibleDialogo();
    }
    
    
    
    
}

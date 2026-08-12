/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jafrinventarios.controladores.usuarios;

import jafrinventarios.modelos.ModeloSesionUsuario;
import jafrinventarios.vistas.usuarios.DialogoFormularioUsuario;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author JOHN FORERO
 */
public class ControladorDialogoUsuarios {
    
    private DialogoFormularioUsuario dialogoUsuario;
    private boolean esAdministrador;

    
    private ControladorDialogoUsuarios(
            DialogoFormularioUsuario dialogoUsuario, 
            DialogoFormularioUsuario.TipoDialogo tipoDialogo,
            boolean esAdminsitrador
    ) {
        this.dialogoUsuario = dialogoUsuario;
        this.esAdministrador = esAdminsitrador;
        
        dialogoUsuario.hacerVisibleDialogo();
    }
    
    
    public static void crear(JFrame ventanaPadre, DialogoFormularioUsuario.TipoDialogo tipoDialogo, boolean esAdministrador){
        
        //Para pruebas se dejara esAdministrador como parametro de la funcion crear
        //boolean esAdminsitrador = ModeloSesionUsuario.getInstancia().esAdministrador();
    
        DialogoFormularioUsuario dialogoUsuario = 
                new DialogoFormularioUsuario( ventanaPadre,
                                              tipoDialogo,
                                              esAdministrador
                );
        
        new ControladorDialogoUsuarios(dialogoUsuario, tipoDialogo, esAdministrador);
        
    }
    
    
    
}

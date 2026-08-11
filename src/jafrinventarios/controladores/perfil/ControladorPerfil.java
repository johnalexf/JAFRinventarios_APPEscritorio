/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jafrinventarios.controladores.perfil;

import jafrinventarios.controladores.ControladorNavegacionGlobal;
import jafrinventarios.modelos.ModeloSesionUsuario;
import jafrinventarios.vistas.usuarios.DialogoFormularioUsuario;
import jafrinventarios.vistas.perfil.PerfilPanel;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author JOHN FORERO
 */
public class ControladorPerfil {
    
    private PerfilPanel moduloPerfil;
    private boolean esAdministrador;

    public ControladorPerfil(PerfilPanel moduloPerfil) {
        this.moduloPerfil = moduloPerfil;
        this.esAdministrador = ModeloSesionUsuario.getInstancia().esAdministrador();
        
        cargarDatosPerfil();
        
        configurarEventosBotones();
    }
    
    
    private void cargarDatosPerfil(){
        // Simulacion consulta a la base de datos
        HashMap<String, String> datosBD = new HashMap<>();
        datosBD.put("nombreEmpresa", "Albania");
        datosBD.put("nombreUsuario", "John Forero");
        datosBD.put("alias", "johnalex");
        datosBD.put("correo", "john@gmail.com");
        datosBD.put("telefono", "3202173409");
        
        if(esAdministrador){
            datosBD.put("codigo", "A@$d654Vf0");
        }
        
        moduloPerfil.escribirDatos(datosBD);
        
    }
    
    
    private void configurarEventosBotones(){
        moduloPerfil.getBtnCerrarSesion().addActionListener(e -> procesarCierreSesion());
        moduloPerfil.getBtnEditarUsuario().addActionListener(e -> procesarEdicionUsuario());
    }
    
    
    private void procesarCierreSesion(){
        ModeloSesionUsuario.getInstancia().cerrarSesion();
        ControladorNavegacionGlobal.getInstancia().iniciarPantallaAcceso();
    }
    
    
    private void procesarEdicionUsuario(){
        JFrame ventanaPadre = (JFrame) SwingUtilities.getWindowAncestor(moduloPerfil);
        
        //TODO crear el controlador, por el momento se mantienen estas lineas de codigo para hacer pruebas
        DialogoFormularioUsuario ModalEditarUsuario =  DialogoFormularioUsuario.crearDialogoEditarPerfil(ventanaPadre, esAdministrador);
        ModalEditarUsuario.hacerVisibleDialogo();
    }
    
}

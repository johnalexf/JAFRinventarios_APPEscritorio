/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jafrinventarios.controladores.perfil;

import jafrinventarios.controladores.ControladorNavegacionGlobal;
import jafrinventarios.controladores.usuarios.ControladorDialogoUsuarios;
import jafrinventarios.modelos.ModeloSesionUsuario;
import jafrinventarios.servicios.usuarios.ServicioUsuarios;
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
        
        cargarDatosPerfil( obtenerDatosPerfil() );
        
        configurarEventosBotones();
    }
    
    
    private HashMap<String, String> obtenerDatosPerfil(){
        // Simulacion consulta a la base de datos con 
        //ModeloSesionUsuario.getInstancia().getIdUsuario();
        //
        // Aqui el controlador se encarga de convertir la respuesta del modelo
        // o del servicio del modelo, en una respuesta que entienda la vista modeloPerfil
        HashMap<String, String> datosPerfil = new HashMap<>();
        datosPerfil.put("nombreEmpresa", "Albania");
        datosPerfil.put("nombreUsuario", "John Forero");
        datosPerfil.put("alias", "johnalex");
        datosPerfil.put("correo", "john@gmail.com");
        datosPerfil.put("telefono", "3202173409");
        
        if(esAdministrador){
            datosPerfil.put("codigo", "A@$d654Vf0");
        }
    
        return datosPerfil;
    }
    
    
    private void cargarDatosPerfil( HashMap<String, String> datosPerfil ){
        moduloPerfil.escribirDatos(datosPerfil);
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
        
        boolean seEditoUsuario;
        //TODO por el momento se mantienen estas lineas de codigo para hacer pruebas
        seEditoUsuario = ControladorDialogoUsuarios.editarPerfil(ventanaPadre, true, new ServicioUsuarios());
        seEditoUsuario = ControladorDialogoUsuarios.editarPerfil(ventanaPadre, false, new ServicioUsuarios());
        
        if(seEditoUsuario){
            cargarDatosPerfil( obtenerDatosPerfil() );
        }

    }
    
}

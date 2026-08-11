/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jafrinventarios.controladores.perfil;

import jafrinventarios.controladores.ControladorNavegacionGlobal;
import jafrinventarios.vistas.perfil.PerfilPanel;
import java.util.HashMap;

/**
 *
 * @author JOHN FORERO
 */
public class ControladorPerfil {
    
    private PerfilPanel moduloPerfil;
    /*
    TODO: temporalmente se manejara con un variable local de la clase, pero
    se pretende crear una instancia singleton, que almacene el id del usuario
    y una variable booleana para saber si es o no administrador, ya que se necesita
    para todos los diferentes modulos
    */
    private boolean esAdministrador;
    

    public ControladorPerfil(PerfilPanel moduloPerfil, boolean esAdminstrador) {
        this.moduloPerfil = moduloPerfil;
        this.esAdministrador = esAdminstrador;
        
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
        ControladorNavegacionGlobal.getInstancia().iniciarPantallaAcceso();
    }
    
    
    private void procesarEdicionUsuario(){
    
    }
    
}

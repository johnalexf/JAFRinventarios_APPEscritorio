/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jafrinventarios.controladores.perfil;

import jafrinventarios.DTOs.usuarios.DTOUsuarioTabla;
import jafrinventarios.controladores.ControladorNavegacionGlobal;
import jafrinventarios.controladores.usuarios.ControladorDialogoUsuarios;
import jafrinventarios.modelos.ModeloSesionUsuario;
import jafrinventarios.servicios.usuarios.ServicioUsuarios;
import jafrinventarios.vistas.perfil.PerfilPanel;
import java.util.HashMap;

/**
 *
 * @author JOHN FORERO
 */
public class ControladorPerfil {
    
    private PerfilPanel moduloPerfil;
    
    private ServicioUsuarios servicioUsuarios;
    
    private boolean esAdministrador;

    
    /*
    ============================================================================
                        CONSTRUCTOR PUBLICO
    ============================================================================
    */
    public ControladorPerfil(PerfilPanel moduloPerfil, ServicioUsuarios servicioUsuarios ) {
        this.moduloPerfil = moduloPerfil;
        this.servicioUsuarios = servicioUsuarios;
        this.esAdministrador = ModeloSesionUsuario.getInstancia().esAdministrador();
        
        cargarDatosPerfil( obtenerDatosPerfil() );
        
        configurarEventosBotones();
    }
    
    
    /*
    ============================================================================
                METODOS PARA CONSULTAR A LOS SERVICIOS
    ============================================================================
    */
    
    private DTOUsuarioTabla obtenerDatosDTOUsuario( int idUsuario ){
        return servicioUsuarios.obtenerDatosDTOUsuario(idUsuario);
    }
    
    private String obtenerCodigoRegistroVendedor(){
        return servicioUsuarios.obtenerCodigoRegistroVendedor();
    }
    
    
    /*
    ============================================================================
            METODOS CONFIGURACION INICIAL PLASMAR INFORMACION DE PERFIL
    ============================================================================
    */
    
    
    private HashMap<String, String> obtenerDatosPerfil(){
        
        DTOUsuarioTabla datosPerfil = obtenerDatosDTOUsuario( 
                ModeloSesionUsuario.getInstancia().getIdUsuario()
        );
                     
        HashMap<String, String> datosPerfilEmpaquetados = new HashMap<>();
        datosPerfilEmpaquetados.put("nombreEmpresa", ModeloSesionUsuario.getInstancia().getNombreEmpresa() );
        datosPerfilEmpaquetados.put("nombreUsuario", datosPerfil.getNombreCompletoUsuario() );
        datosPerfilEmpaquetados.put("alias", datosPerfil.getAliasUsuario() );
        datosPerfilEmpaquetados.put("correo", datosPerfil.getCorreoUsuario() );
        datosPerfilEmpaquetados.put("telefono", datosPerfil.getTelefonoUsuario() );
        
        if( esAdministrador ){
            String codigo = obtenerCodigoRegistroVendedor();
            datosPerfilEmpaquetados.put("codigo", codigo);
        }
    
        return datosPerfilEmpaquetados;
    }
    
    
    private void cargarDatosPerfil( HashMap<String, String> datosPerfilEmpaquetados ){
        moduloPerfil.escribirDatos( datosPerfilEmpaquetados );
    }
    
    
    /*
    ============================================================================
                            METODO EVENTOS BOTONES
    ============================================================================
    */
    private void configurarEventosBotones(){
        moduloPerfil.getBtnCerrarSesion().addActionListener(e -> procesarCierreSesion());
        moduloPerfil.getBtnEditarUsuario().addActionListener(e -> procesarEdicionUsuario());
    }
    
    
    private void procesarCierreSesion(){
        ModeloSesionUsuario.getInstancia().cerrarSesion();
        ControladorNavegacionGlobal.getInstancia().iniciarPantallaAcceso();
    }
    
    
    private void procesarEdicionUsuario(){
              
        boolean seEditoUsuario;

        seEditoUsuario = ControladorDialogoUsuarios.editarPerfil(
                moduloPerfil.getVentanaPadre(), esAdministrador , new ServicioUsuarios()
        );
        
        if(seEditoUsuario){
            cargarDatosPerfil( obtenerDatosPerfil() );
        }

    }
    
}

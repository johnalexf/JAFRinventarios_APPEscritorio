/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jafrinventarios.servicios.acceso;

import jafrinventarios.DTOs.acceso.DTOCredenciales;
import jafrinventarios.modelos.ModeloSesionUsuario;

/**
 *
 * @author JOHN FORERO
 */
public class ServicioAutenticacion {
    /*
    Esta clase esta destinada para editar contraseña, recuperarla e iniciar sesion
    TODO pendiente de su estructuracion interna.
    */
    
    private String mensajeError;
    
    
    public ServicioAutenticacion(){}
    
    
    public DTOCredenciales iniciarSesion( String correo, String contrasena ){
        
        DTOCredenciales credencialesUsuario;
        /*
        Se verifica si el correo esta en la base de datos y si la contrasena
        encriptada es la misma
        
        Si todo esta correcta se hace la peticion de los siguientes datos
        int idUsuario;
        String nombreRol;
        boolean esAdministrador; Esta variable se agrega por medio de un if validando si el nombre del rol es "Administrador"
        int idEmpresa; 
        String nombreEmpresa;
        */
        credencialesUsuario = new DTOCredenciales( 0, "Administrador", true, 0, "Albania" );
        
        return credencialesUsuario;
    }
    
    protected String encriptarContrasena( String contrasena ){
    
        String contrasenaEncriptada = contrasena + "pendiente";
        /*
        TODO Encriptar contrasena
        */
        return contrasenaEncriptada;
    }
    
}

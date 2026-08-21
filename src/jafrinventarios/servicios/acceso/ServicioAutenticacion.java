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
    
    
    public String generarCodigo (){
        
        StringBuilder codigo = new StringBuilder();
        
        for( int i=0; i<10; i++){
            double numAleatorio = Math.random();
            int codigoASCII;
            
            if( numAleatorio < 0.35 )  
                codigoASCII = (int) ( Math.random()*(57 - 48) + 48 );
            else if(numAleatorio < 0.75 ) 
                codigoASCII = (int) ( Math.random()*(90 - 65) + 65 );
            else 
                codigoASCII = (int) ( Math.random()*(122 - 97) + 97 );
            
            char caracter = (char) codigoASCII;
            codigo.append(caracter);
        }
        
        return codigo.toString();
    }
    
    
    public int obtenerIdUsuarioConCorreo ( String correo ){
        
        
        /*
        TODO
        Hacer la validacion en la base de datos si el correo existe
        y enviar el id al que pertenece ese correo.
        devolver -1 si no existe
        */
        int idUsuario = 1;
        //guardamos el id del usuario para saber a que correo pertenece o
        //podemos guardar tambien el correo
        
        return idUsuario;
    }
    
    
    public boolean enviarCodigoCorreo ( String correo, String codigo ){
        
        /*
            TODO    
        Enviar el correo con el codigo, para ello existira una funcion en este 
        servicio que pueda enviar correos, dependiendo de la configuracion
        si es muy extensa, se hara un servicio aparte que sea destinado solo
        para enviar codigos.
        Si hubo alguna falla se envia true
        */
    
        return true;
    
    }
    
    
    // Este metodo es solo para cuando un usuario ya ha iniciado sesion
    public boolean validarContrasenaAntigua( int idUsuario, String contrasenaAntigua ){
    
        /*
        Hacer la consulta de si la contraseña coincide para el usuario, para esto
        hay que encriptar la contraseña antes de verificar.
        
            si es valida la contrasena se devuelve true;
        
        */
        
        
       return true;
    }
    
    
    
    public boolean cambiarContrasena ( int idUsuario, String contrasenaNueva ){
    
        /*
        encriptamos la contraseña y se la guardamos al idUsuario
        
        */
    
        return true;
    }
    
}

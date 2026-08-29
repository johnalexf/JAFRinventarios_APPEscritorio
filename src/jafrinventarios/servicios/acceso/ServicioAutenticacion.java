
package jafrinventarios.servicios.acceso;

import jafrinventarios.DTOs.acceso.DTOCredenciales;
import jafrinventarios.servicios.excepciones.ExcepcionValidacionBD;
import java.util.HashMap;
import java.util.Map;

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
    
    
    public DTOCredenciales iniciarSesion( String correo, String contrasena ) throws Exception {

        DTOCredenciales credencialesUsuario;
        /*
        Se verifica si el correo esta en la base de datos y si la contrasena
        encriptada es la misma

        Si todo esta correcta se hace la peticion de los siguientes datos
        int idUsuario;
        String nombreRol;
        boolean isAdministrador; Esta variable se agrega por medio de un if validando si el nombre del rol es "Administrador"
        int idEmpresa; 
        String nombreEmpresa;
        */

        boolean correoExiste = true;
        if( !correoExiste ){
            throw new ExcepcionValidacionBD( 
                    new HashMap<>( Map.of( "correo", "Este correo no esta registrado" ))
            );
        }

        boolean contrasenaCorrecta = true;
        if( !contrasenaCorrecta ){
            throw new ExcepcionValidacionBD( 
                    new HashMap<>( Map.of( "contrasena", "La contraseña no coincide" ))
            );
        }


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
    
    
    public static String generarCodigo (){
        
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
    
    
    public int obtenerIdUsuarioConCorreo ( String correo ) throws Exception {
        
        
        /*
        TODO
        Hacer la validacion en la base de datos si el correo existe
        y enviar el id al que pertenece ese correo.
        */
        int idUsuario = 1;
        boolean correoExiste = true;
        if( !correoExiste ){
            throw new ExcepcionValidacionBD( 
                    new HashMap<>( Map.of( "correo", "Este correo no esta registrado" ))
            );
        }
        
        return idUsuario;
    }
    
    
    public void enviarCodigoCorreo ( String correo, String codigo ) throws Exception{
        
        /*
            TODO    
        Enviar el correo con el codigo, para ello existira una funcion en este 
        servicio que pueda enviar correos, dependiendo de la configuracion
        si es muy extensa, se hara un servicio aparte que sea destinado solo
        para enviar codigos.
        */
        //throw new RuntimeException("Correo no se pudo enviar");
    
    }
    
    
    // Este metodo es solo para cuando un usuario ya ha iniciado sesion
    public boolean validarContrasenaAntigua( int idUsuario, String contrasenaAntigua ) throws Exception{
    
        /*
        Hacer la consulta de si la contraseña coincide para el usuario, para esto
        hay que encriptar la contraseña antes de verificar.
        
            si es valida la contrasena se devuelve true;
        throw new RuntimeException("No se pudo validar la contraseña");
        */
        
        
       return true;
    }
    
    
    
    public void cambiarContrasena ( int idUsuario, String contrasenaNueva ) throws Exception{
    
        /*
        encriptamos la contraseña y se la guardamos al idUsuario
        throw new RuntimeException("No se pudo guardar la contraseña");
        */
    
    }
    
}

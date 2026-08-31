
package jafrinventarios.servicios.acceso;


import jafrinventarios.modelos.usuarios.ModeloUsuario;
import jafrinventarios.servicios.ConexionDB;
import jafrinventarios.servicios.usuarios.ServicioUsuarios;
import java.sql.Connection;

/**
 *
 * @author JOHN FORERO
 */
public class ServicioRegistro {
    /*
    Esta clase funcionara para poder registrar un usuario sin haber iniciado sesion
    en el caso de que un administrador desee crear un usuario lo puede hacer desde 
    el servicioUsuarios ya que la creacion cambia puesto que la contraseña se enviara
    por correo al usuario creado, en cambio desde esta clase el registro pide la clave
    y un codigo de acceso
    */
    
    public boolean isValidoCodigo( String codigo, boolean isRegistroAdminsitrador ) throws Exception {
        
        int idEmpresa = ServicioEmpresa.obtenerIdEmpresa();
        
        if(isRegistroAdminsitrador){
            
            if( !codigo.equals("JaFr_1pp4*") )
                return false;

            if( idEmpresa == -1 ){
                //Si aun no hay empresas el codigo sigue siendo valido
                return true;        
            }else{
                /*
                Si ya hay una empresa entonces no se pueden crear mas usuarios administradores POR MEDIO DE CODIGO
                El codigo es solo valido para crear UN SOLO usuario administrador, por que la intencion es crear la empresa a la par.
                Recordar que se va crear solo una empresa y se uso esa tabla para poder centralizar el nombre y un codigo para dejar 
                registrar otros usuarios que no sean administradores.
                */
                throw new Exception("Este codigo era de un unico uso");
            }
                
                
        }else{
        
            if(idEmpresa == -1){
                throw new Exception( "Error aun no existe una empresa creada, primero se debe crear el usuario administrador" );
            }
            
            try {
                String codigoEmpresa = ServicioEmpresa.obtenerCodigoEmpresa();
                return codigoEmpresa.equals(codigo);
            } catch (Exception e) {
                throw new Exception("No se pudo validar el codigo \n" + e.getMessage());
            }
            
        }
        
    }
      
    
    public void registrarAdministrador ( ModeloUsuario usuario, String contrasena, String nombreEmpresa ) throws Exception{
    
        usuario.setIdEmpresa( ServicioEmpresa.crearEmpresa(nombreEmpresa) );
        usuario.setContrasenaUsuario( ServicioSeguridad.hashearContrasena(contrasena) );

        ServicioUsuarios.registrarUsuario(usuario);

    }
    
    
    public void registrarNoAdministrador ( ModeloUsuario usuario, String contrasena ) throws Exception{
    
        usuario.setIdEmpresa( ServicioEmpresa.obtenerIdEmpresa() );
        usuario.setContrasenaUsuario( ServicioSeguridad.hashearContrasena(contrasena) );
        
        ServicioUsuarios.registrarUsuario( usuario );
        
        ServicioEmpresa.editarCodigoEmpresa( usuario.getIdEmpresa() );

    }
    
    
}

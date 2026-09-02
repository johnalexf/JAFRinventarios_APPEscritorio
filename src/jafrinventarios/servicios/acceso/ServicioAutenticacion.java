
package jafrinventarios.servicios.acceso;

import jafrinventarios.DTOs.acceso.DTOCredenciales;
import jafrinventarios.servicios.ConexionDB;
import jafrinventarios.servicios.excepciones.ExcepcionValidacionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

        int idUsuario = obtenerIdUsuarioConCorreo(correo);
        
        boolean contrasenaCorrecta = validarContrasena(idUsuario, contrasena);
        
        if( !contrasenaCorrecta ){
            throw new ExcepcionValidacionBD( 
                    new HashMap<>( Map.of( "contrasena", "La contraseña no coincide" ))
            );
        }
        
        return obtenerDTOCredenciales(idUsuario);
            
    }
    
    
    
    public int obtenerIdUsuarioConCorreo ( String correo ) throws Exception {
        
        Connection conexionBD = ConexionDB.getConnection();
        
        String sentenciaSQL = "SELECT id_usuario FROM usuarios WHERE correo_usuario = ?";
        
        try( PreparedStatement consulta = conexionBD.prepareStatement(sentenciaSQL)){
        
            consulta.setString(1, correo);
            try(ResultSet respuesta = consulta.executeQuery()){
                if(respuesta.next())
                    return respuesta.getInt("id_usuario");
                else
                    throw new ExcepcionValidacionBD( 
                        new HashMap<>(  Map.of( "correo", "Este correo no esta registrado" ) )
                    );
            }
        
        }

    }
    
    
    // Metodo para verificar si la contraseña es la misma almacenada para el idUsuario
    public boolean validarContrasena( int idUsuario, String contrasena ) throws Exception{
    
        Connection conexionBD = ConexionDB.getConnection();
        
        String sentenciaSQL = "SELECT contrasena_usuario FROM usuarios WHERE id_usuario = ?";
        
        try( PreparedStatement consulta = conexionBD.prepareStatement(sentenciaSQL)){
        
            consulta.setInt(1, idUsuario);
            
            try( ResultSet respuesta = consulta.executeQuery() ){
                if( respuesta.next() ){
                    return ServicioSeguridad.contrasenaCoincideConHash(
                            contrasena, 
                            respuesta.getString("contrasena_usuario")
                    );
                }
                else
                    throw new Exception("No se pudo validar la contraseña");
            }
        
        }

    }
    
    
    
    private DTOCredenciales obtenerDTOCredenciales (int idUsuario) throws Exception{
    
        Connection conexionDB = ConexionDB.getConnection();
        
        String sentenciaSQL = 
                "SELECT \n" +
                "    us.id_usuario AS 'idUsuario',\n" +
                "    rol.nombre_rol AS 'nombreRol',\n" +
                "    us.id_empresa AS 'idEmpresa',\n" +
                "    emp.nombre_empresa AS 'nombreEmpresa'\n" +
                "FROM\n" +
                "    usuarios us\n" +
                "INNER JOIN\n" +
                "    empresa emp\n" +
                "ON us.id_empresa = emp.id_empresa\n" +
                "INNER JOIN\n" +
                "    roles rol\n" +
                "ON us.id_rol_usuario = rol.id_rol\n" +
                "WHERE\n" +
                "    us.id_usuario = ?";
        
        try( PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL)){
            
            consulta.setInt(1, idUsuario);

            try( ResultSet respuesta = consulta.executeQuery() ){
                if(respuesta.next()){
                    return new DTOCredenciales(
                                    respuesta.getInt("idUsuario"),
                                    respuesta.getString("nombreRol"),
                                    /*
                                    No existe una variable booleana que determine si es administrador o no
                                    pues los roles se pensaron si por alguna eventualidad se decide agregar otro
                                    tipo de rol, por el momento lo comprobamos de esta manera, sin embargo se 
                                    puede pensar en una variable que especifique si es administrador, e incluso si
                                    es superAdministrador, que seria el primer administrador registrado
                            TODO: Existe un punto de mejora, pues para evaluar si un usuario es administrador
                            en este servicio se tiene en cuenta una comparacion con string, y en el servicio de 
                            usuarios se evalua con el id_rol asumiendo que es igual a 1, por el momento se deja tal cual como esta
                            pero un mejora seria crear la variable booleana isAdministrador, asi no se depende de asumir por texto o id_rol
                                    */
                                    respuesta.getString("nombreRol").toLowerCase().equals("administrador"),
                                    respuesta.getInt("idEmpresa"),
                                    respuesta.getString("nombreEmpresa")
                            );
                }else{
                    throw new Exception("Error al obtener los datos del usuario");
                }
            }
        
        }
    
    }
    
    
    public void cambiarContrasena ( int idUsuario, String contrasenaNueva ) throws Exception{
    
        Connection conexionDB =  ConexionDB.getConnection();
        
        String sentenciaSQL = 
                "UPDATE\n" +
                "    usuarios\n" +
                "SET\n" +
                "    contrasena_usuario = ?\n" +
                "WHERE\n" +
                "    id_usuario = ?";
        
        try( PreparedStatement consulta =  conexionDB.prepareStatement(sentenciaSQL)){
            
            consulta.setString(1, ServicioSeguridad.hashearContrasena(contrasenaNueva));
            consulta.setInt(2, idUsuario);
            
            int filasAfectadas = consulta.executeUpdate(); 
            if( filasAfectadas != 1 ){
                throw new Exception("No se pudo guardar la contraseña");
            }
        }
    
    }
    
}

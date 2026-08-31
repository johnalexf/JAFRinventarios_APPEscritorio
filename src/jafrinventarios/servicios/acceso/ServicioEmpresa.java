
package jafrinventarios.servicios.acceso;

import jafrinventarios.modelos.usuarios.ModeloEmpresa;
import jafrinventarios.servicios.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author JOHN FORERO
 */
public class ServicioEmpresa {
    
    
    public static int crearEmpresa ( String nombreEmpresa ) throws Exception{
    
        Connection conexionBD = ConexionDB.getConnection();

        ModeloEmpresa empresa = new ModeloEmpresa();
        empresa.setNombreEmpresa(nombreEmpresa);
        empresa.setCodigoRegistroUsuarioVendedor( ServicioSeguridad.generarCodigo() );

        String sentenciaSQL = 
                "INSERT INTO \n" +
                "	empresa\n" +
                "    (nombre_empresa, codigo_registro_usuario_vendedor) \n" +
                "VALUES ( ? , ? )";
        
        /*
            Usamos RETURN_GENERATED_KEYS Para que en la misma consulta se retorne el id de la empresa creada
        */
        try( PreparedStatement consulta = conexionBD.prepareStatement( sentenciaSQL, Statement.RETURN_GENERATED_KEYS )){
        
            consulta.setString(1, empresa.getNombreEmpresa());
            consulta.setString(2, empresa.getCodigoRegistroUsuarioVendedor());
            
            int filasAfectadas = consulta.executeUpdate();
            
            if( filasAfectadas == 1){
                try( ResultSet respuesta = consulta.getGeneratedKeys() ){ 
                    if( respuesta.next() ){
                        empresa.setIdEmpresa( respuesta.getInt( 1 ) );
                    }else{
                        throw new Exception( "Error al obtener el id de la empresa" );
                    }
                }
                
            }else{
                throw new Exception ("No se creo la empresa en la base de datos");
            }       
        }
        
        return empresa.getIdEmpresa();
    }
    
    
    public static int obtenerIdEmpresa() throws Exception{
        /*
        Recordar que el sistema esta diseñado para solo guardar una empresa,
        por tanto se busca el id de la unica empresa registrada.
        */
        
        Connection conexionBD = ConexionDB.getConnection();
        
        String sentenciaSQL = "SELECT id_empresa FROM empresa";
        
        /*
        Usamos try-with-resources para cerrar la consulta y no dejarla abierta
        para evitar almacenamiento de las mismas en cache y acumulacion que
        puede generar un colapso de la conexion con la base de datos.
        */
        try (PreparedStatement consulta = conexionBD.prepareStatement( sentenciaSQL ) ) {
                
            try( ResultSet respuesta = consulta.executeQuery() ){
                //En respuesta la fila 0 es el inicio, por eso usamos next para revisar si hay datos
                if(respuesta.next()){
                   return respuesta.getInt("id_empresa");
               }else{
                   // Respondemos con un -1 para indicar que no existe una empresa creada
                   return -1;
               }
            }
        }catch(Exception e){
            throw new Exception("Error al obtener el id de la empresa");
        }
        
    }
    
    
    public static void editarCodigoEmpresa( int idEmpresa ) throws Exception{
    
        Connection conexionBD = ConexionDB.getConnection();
        
        String sentenciaSQL = "UPDATE empresa \n" +
                                "SET codigo_registro_usuario_vendedor = ? \n" +
                                "WHERE id_empresa = ?";
        
        try (PreparedStatement consulta = conexionBD.prepareStatement( sentenciaSQL ) ) {
            
            consulta.setString(1, ServicioSeguridad.generarCodigo());
            consulta.setInt(2, idEmpresa);
            
            int filasAfectadas = consulta.executeUpdate();
            
            if(filasAfectadas != 1)
                 throw new Exception("Error al modificar el codigo de registro de un vendedor en la empresa");
        }
    }
    
    
    public static String obtenerCodigoEmpresa() throws Exception{
        
        /*
        Hacer la consulta para obtener el codigo que puede compartir un
        administrador a los vendedores para que se registren, valido solo por
        un uso
        */
        Connection conexionBD = ConexionDB.getConnection();
        
        String sentenciaSQL =  "SELECT codigo_registro_usuario_vendedor FROM empresa";
        
        try (PreparedStatement consulta = conexionBD.prepareStatement( sentenciaSQL ) ) {
                   
            try( ResultSet respuesta = consulta.executeQuery() ){
                if( respuesta.next() )
                    return respuesta.getString(1);
                else
                    throw new Exception("Error al obtener el codigo de la base de datos");
            }        
        }
    
    }
    
    
    
}

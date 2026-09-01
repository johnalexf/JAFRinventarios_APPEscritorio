
package jafrinventarios.servicios.usuarios;

import jafrinventarios.servicios.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedHashMap;

/**
 *
 * @author JOHN FORERO
 */
public class ServicioRoles {
    
    
    /*
     * En el futuro, este método se conectará a la base de datos (SELECT * FROM roles),
     * llenará una lista de objetos ModeloRol y luego extraerá el diccionario.
     */
    public static LinkedHashMap<Integer, String> obtenerDiccionarioRoles() throws Exception{
        
        Connection conexionDB = ConexionDB.getConnection();
        
        // Armamos el diccionario exacto que necesita la vista (ID -> Nombre)
        LinkedHashMap<Integer, String> diccionarioRoles = new LinkedHashMap<>();
        
        String sentenciaSQL = 
                "SELECT\n" +
                "    id_rol AS id,\n" +
                "    nombre_rol AS nombreRol\n" +
                "FROM\n" +
                "    roles";
        
        try( PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL) ){
        
            try( ResultSet respuesta = consulta.executeQuery()){
                while( respuesta.next() ){
                    diccionarioRoles.put( respuesta.getInt("id"), respuesta.getString("nombreRol") );
                }
                return diccionarioRoles;
            }
        
        }catch(Exception e){
            throw new Exception("No se pudo obtener la lista de roles \n" + e.getMessage());
        }

        
    }
    
}

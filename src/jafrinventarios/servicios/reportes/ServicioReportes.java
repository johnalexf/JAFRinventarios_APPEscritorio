
package jafrinventarios.servicios.reportes;

import jafrinventarios.DTOs.reportes.DTOFiltroReporte;
import jafrinventarios.servicios.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author JOHN FORERO
 */
public class ServicioReportes {

    
    public ServicioReportes() {
    }
    
    
    public DTOFiltroReporte completarFiltro( DTOFiltroReporte filtro )throws Exception{
        
        Connection conexionDB = ConexionDB.getConnection();
        String sentenciaSQL;
        
        if( filtro.getIdProveedor() != null ){           
            sentenciaSQL = "SELECT nombre_comercial FROM proveedores WHERE id_proveedor = ?";
            try( PreparedStatement sentencia = conexionDB.prepareStatement(sentenciaSQL) ){
                sentencia.setInt(1, filtro.getIdProveedor());
                try( ResultSet respuesta = sentencia.executeQuery()){
                    if( respuesta.next() ){
                        filtro.setProveedor( respuesta.getString("nombre_comercial"));
                    }else
                        throw new Exception( "El id del proveedor no existe en la base de datos " );
                }
            }
        }
               
        if( filtro.getIdCliente() != null ){           
            sentenciaSQL = "SELECT nombre_negocio FROM clientes WHERE id_cliente = ?";
            try( PreparedStatement sentencia = conexionDB.prepareStatement(sentenciaSQL) ){
                sentencia.setInt(1, filtro.getIdCliente());
                try( ResultSet respuesta = sentencia.executeQuery()){
                    if( respuesta.next() ){
                        filtro.setCliente(respuesta.getString("nombre_negocio") );
                    }else
                        throw new Exception( "El id del cliente no existe en la base de datos " );
                }
            }
        }
               
        if( filtro.getIdProducto() != null ){           
            sentenciaSQL = "SELECT nombre_producto FROM productos WHERE id_producto = ?";
            try( PreparedStatement sentencia = conexionDB.prepareStatement(sentenciaSQL) ){
                sentencia.setInt(1, filtro.getIdProducto());
                try( ResultSet respuesta = sentencia.executeQuery()){
                    if( respuesta.next() ){
                        filtro.setProducto(respuesta.getString("nombre_producto") );
                    }else
                        throw new Exception( "El id del producto no existe en la base de datos " );
                }
            }
        }
               
        if( filtro.getIdUsuario() != null ){           
            sentenciaSQL = "SELECT alias_usuario FROM usuarios WHERE id_usuario = ?";
            try( PreparedStatement sentencia = conexionDB.prepareStatement(sentenciaSQL) ){
                sentencia.setInt(1, filtro.getIdUsuario());
                try( ResultSet respuesta = sentencia.executeQuery()){
                    if( respuesta.next() ){
                        filtro.setUsuario(respuesta.getString("alias_usuario") );
                    }else
                        throw new Exception( "El id del usuario no existe en la base de datos " );
                }
            }
        }
        
        return filtro;
        
    }
    
    
    
    
}

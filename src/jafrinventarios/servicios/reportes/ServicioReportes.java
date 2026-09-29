
package jafrinventarios.servicios.reportes;

import jafrinventarios.DTOs.reportes.DTOFiltroReporte;
import jafrinventarios.DTOs.reportes.cantidadesAComprar.DTOProductoComprar;
import jafrinventarios.DTOs.reportes.cantidadesAComprar.DTOProveedorPedido;
import jafrinventarios.DTOs.reportes.cantidadesAComprar.DTOReporteCantidadesAComprar;
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
    
    
    
    public DTOReporteCantidadesAComprar obtenerInformacionCantidadesAComprar ( ) throws Exception{
    
        DTOReporteCantidadesAComprar reporte =  new DTOReporteCantidadesAComprar();
    
        Connection conexionDB = ConexionDB.getConnection();
        
        String sentenciaSQL = "SELECT\n" +
                            "    prod.id_producto AS 'idProducto',\n" +
                            "    prod.id_proveedor AS 'idProveedor',\n" +
                            "    prod.nombre_producto AS 'nombreProducto',\n" +
                            "    prod.precio_compra AS 'precioCompra',\n" +
                            "    (prod.cantidad_minima_stock - prod.cantidad_disponible) AS 'cantidadAComprar',\n" +
                            "    prov.nombre_comercial AS 'nombreComercial',\n" +
                            "    CONCAT( prov.primer_nombre_contacto, \" \" , \n" +
                            "            prov.segundo_nombre_contacto, \" \", \n" +
                            "            prov.primer_apellido_contacto, \" \", \n" +
                            "            prov.segundo_apellido_contacto ) AS 'nombreContacto',\n" +
                            "    prov.correo_proveedor AS 'correo',\n" +
                            "    prov.telefono_contacto AS 'telefono',\n" +
                            "    prov.direccion_proveedor AS 'direccion'\n" +
                            "FROM \n" +
                            "    productos prod\n" +
                            "INNER JOIN \n" +
                            "    proveedores prov\n" +
                            "ON \n" +
                            "    prod.id_proveedor = prov.id_proveedor\n" +
                            "WHERE\n" +
                            "    prod.habilitado = 1 AND\n" +
                            "    (prod.cantidad_minima_stock - prod.cantidad_disponible) > 0\n" +
                            "ORDER BY \n" +
                            "    prod.nombre_producto ASC;";
        
        try( PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL);
             ResultSet respuesta = consulta.executeQuery();
           ){
            while(respuesta.next()){
                if( !reporte.existeProveedor( respuesta.getInt("idProveedor") ) ){
                    reporte.agregarProveedor( respuesta.getInt("idProveedor"), 
                            new DTOProveedorPedido( respuesta.getString( "nombreComercial" ),
                                                    respuesta.getString( "nombreContacto" ),
                                                    respuesta.getString("correo"),
                                                    respuesta.getString("telefono"),
                                                    respuesta.getString("direccion")
                            )
                    );
                }
                reporte.agregarProducto( respuesta.getInt("idProveedor") , 
                            new DTOProductoComprar( respuesta.getInt("idProducto"),
                                                    respuesta.getString("nombreProducto"),
                                                    respuesta.getDouble("precioCompra"),
                                                    respuesta.getInt("cantidadAComprar")
                            )
                );
                
            }
            //Si la consulta no retorna informacion, igual retornamos el reporte
            //sin datos para que se interprete como ningun producto por comprar.
            return reporte;
        }
        
    }
    
    
}

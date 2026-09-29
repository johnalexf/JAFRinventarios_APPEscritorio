
package jafrinventarios.servicios.reportes;

import jafrinventarios.DTOs.reportes.DTOFiltroReporte;
import jafrinventarios.DTOs.reportes.cantidadesAComprar.DTOProductoComprar;
import jafrinventarios.DTOs.reportes.cantidadesAComprar.DTOProveedorPedido;
import jafrinventarios.DTOs.reportes.cantidadesAComprar.DTOReporteCantidadesAComprar;
import jafrinventarios.DTOs.reportes.transacciones.DTOConsolidadoTransacciones;
import jafrinventarios.DTOs.reportes.transacciones.DTOProductoTransacciones;
import jafrinventarios.DTOs.reportes.transacciones.DTOTerceroComercial;
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
    
    
    
    public DTOConsolidadoTransacciones obtenerConsolidadoVentas( DTOFiltroReporte filtro ) throws Exception{
    
        DTOConsolidadoTransacciones reporte = new DTOConsolidadoTransacciones();
        
        Connection conexionDB = ConexionDB.getConnection();
        
        String sentenciaSQL = "SELECT\n" +
                            "    ven.id_cliente AS 'idCliente',\n" +
                            "    det.id_producto AS 'idProducto',\n" +
                            "    prod.nombre_producto AS 'nombreProducto',\n" +
                            "    SUM(det.cantidad_producto) AS 'totalCantidadProducto',\n" +
                            "    det.precio_unitario_producto AS 'precioProducto',\n" +
                            "    SUM(det.precio_total_producto) AS 'totalPrecioProducto'\n" +
                            "FROM\n" +
                            "    detalle_de_ventas det\n" +
                            "INNER JOIN\n" +
                            "    ventas ven\n" +
                            "ON\n" +
                            "    det.id_venta = ven.id_venta\n" +
                            "INNER JOIN\n" +
                            "    productos prod\n" +
                            "ON\n" +
                            "    det.id_producto = prod.id_producto\n" +
                            "WHERE\n" +
                            "    (ven.fecha_hora_venta BETWEEN ? AND ?)\n" ;
        
        if( filtro.getIdCliente() != null )
            sentenciaSQL += "    AND (ven.id_cliente = ?)\n" ;
        
        if( filtro.getIdProducto() != null )
            sentenciaSQL += "    AND (det.id_producto = ?)\n" ;
        
        if( filtro.getIdUsuario() != null )
            sentenciaSQL += "    AND (ven.id_usuario = ?)\n";
        
        sentenciaSQL +=     "GROUP BY\n" +
                            "    ven.id_cliente,\n" +
                            "    det.id_producto,\n" +
                            "    prod.nombre_producto,\n" +
                            "    det.precio_unitario_producto\n" +
                            "ORDER BY \n" +
                            "    ven.id_cliente ASC";
    
        try( PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL) ){
        
            int marcador = 1;
            consulta.setObject( marcador++ , filtro.getFechaInferior() );
            consulta.setObject( marcador++ , filtro.getFechaSuperior());
            
            if( filtro.getIdCliente() != null )
                consulta.setInt(marcador++, filtro.getIdCliente());
        
            if( filtro.getIdProducto() != null )
                consulta.setInt(marcador++, filtro.getIdProducto());

            if( filtro.getIdUsuario() != null )
                consulta.setInt(marcador++, filtro.getIdUsuario());
            
            try( ResultSet respuesta = consulta.executeQuery() ){
                while( respuesta.next() ){
                    if( !reporte.existeTerceroComercial( respuesta.getInt("idCliente") ) ){
                        reporte.inicializarTerceroComercial( respuesta.getInt("idCliente"));
                    }
                    reporte.agregarProducto( respuesta.getInt("idCliente"), 
                            new DTOProductoTransacciones( respuesta.getInt("idProducto"),
                                                          respuesta.getString("nombreProducto"),
                                                          respuesta.getDouble("precioProducto"),
                                                          respuesta.getInt("totalCantidadProducto"),
                                                          respuesta.getDouble("totalPrecioProducto")
                            )
                    );
                }
            }
            
        }
        
        if ( !reporte.isEmpty() ){
            
            sentenciaSQL = "SELECT\n" +
                            "    cli.id_cliente AS 'idCliente',\n" +
                            "    cli.nombre_negocio AS 'nombreCliente',\n" +
                            "    COUNT(ven.id_venta) AS 'cantidadVentas'\n" +
                            "FROM\n" +
                            "    ventas ven\n" +
                            "INNER JOIN\n" +
                            "    clientes cli\n" +
                            "ON\n" +
                            "    ven.id_cliente = cli.id_cliente\n";
                
            if( filtro.getIdProducto() != null )
                sentenciaSQL += "INNER JOIN\n" +
                                "    detalle_de_ventas det\n" +
                                "ON \n" +
                                "    det.id_venta = ven.id_venta \n" ;
            
            sentenciaSQL += "WHERE\n" +
                            "    (ven.fecha_hora_venta BETWEEN ? AND ?) \n" ;
                  
            if( filtro.getIdCliente() != null )
                sentenciaSQL += "    AND (ven.id_cliente = ?)\n" ;

            if( filtro.getIdProducto() != null )
                sentenciaSQL += "    AND (det.id_producto = ?)\n" ;

            if( filtro.getIdUsuario() != null )
                sentenciaSQL += "    AND (ven.id_usuario = ?)\n";
            
            
            sentenciaSQL += "GROUP BY\n" +
                            "    cli.id_cliente,\n" +
                            "    cli.nombre_negocio";
            
            try( PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL) ){
        
                int marcador = 1;
                consulta.setObject( marcador++ , filtro.getFechaInferior() );
                consulta.setObject( marcador++ , filtro.getFechaSuperior());

                if( filtro.getIdCliente() != null )
                    consulta.setInt(marcador++, filtro.getIdCliente());

                if( filtro.getIdProducto() != null )
                    consulta.setInt(marcador++, filtro.getIdProducto());

                if( filtro.getIdUsuario() != null )
                    consulta.setInt(marcador++, filtro.getIdUsuario());

                try( ResultSet respuesta = consulta.executeQuery() ){
                    while( respuesta.next() ){
                        reporte.agregarInformacionTercero(
                                respuesta.getInt("idCliente"), 
                                respuesta.getString("nombreCliente"), 
                                respuesta.getInt("cantidadVentas")
                        );
                    }
                }

            }

        }
        
        return reporte;
    
    }
    
    
    
    public DTOConsolidadoTransacciones obtenerConsolidadoCompras( DTOFiltroReporte filtro ) throws Exception{
    
        DTOConsolidadoTransacciones reporte = new DTOConsolidadoTransacciones();
        
        Connection conexionDB = ConexionDB.getConnection();
        
        String sentenciaSQL = "SELECT\n" +
                            "    comp.id_proveedor AS 'idProveedor',\n" +
                            "    det.id_producto AS 'idProducto',\n" +
                            "    prod.nombre_producto AS 'nombreProducto',\n" +
                            "    SUM(det.cantidad_producto) AS 'totalCantidadProducto',\n" +
                            "    det.precio_unitario_producto AS 'precioProducto',\n" +
                            "    SUM(det.precio_total_producto) AS 'totalPrecioProducto'\n" +
                            "FROM\n" +
                            "    detalle_de_compras det\n" +
                            "INNER JOIN\n" +
                            "    compras comp\n" +
                            "ON\n" +
                            "    det.id_compra = comp.id_compra\n" +
                            "INNER JOIN\n" +
                            "    productos prod\n" +
                            "ON\n" +
                            "    det.id_producto = prod.id_producto\n" +
                            "WHERE\n" +
                            "    (comp.fecha_hora_compra BETWEEN ? AND ? )\n" ;
        
        if( filtro.getIdProveedor()!= null )
            sentenciaSQL += "    AND (comp.id_proveedor = ?)\n" ;
        
        if( filtro.getIdProducto() != null )
            sentenciaSQL += "    AND (det.id_producto = ?)\n" ;
        
        if( filtro.getIdUsuario() != null )
            sentenciaSQL += "    AND (comp.id_usuario = ?)\n";
        
        sentenciaSQL +=     "GROUP BY\n" +
                            "    comp.id_proveedor,\n" +
                            "    det.id_producto,\n" +
                            "    prod.nombre_producto,\n" +
                            "    det.precio_unitario_producto\n" +
                            "ORDER BY \n" +
                            "    comp.id_proveedor ASC";
    
        try( PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL) ){
        
            int marcador = 1;
            consulta.setObject( marcador++ , filtro.getFechaInferior() );
            consulta.setObject( marcador++ , filtro.getFechaSuperior());
            
            if( filtro.getIdProveedor() != null )
                consulta.setInt(marcador++, filtro.getIdProveedor());
        
            if( filtro.getIdProducto() != null )
                consulta.setInt(marcador++, filtro.getIdProducto());

            if( filtro.getIdUsuario() != null )
                consulta.setInt(marcador++, filtro.getIdUsuario());
            
            try( ResultSet respuesta = consulta.executeQuery() ){
                while( respuesta.next() ){
                    if( !reporte.existeTerceroComercial( respuesta.getInt("idProveedor") ) ){
                        reporte.inicializarTerceroComercial( respuesta.getInt("idProveedor"));
                    }
                    reporte.agregarProducto( respuesta.getInt("idProveedor"), 
                            new DTOProductoTransacciones( respuesta.getInt("idProducto"),
                                                          respuesta.getString("nombreProducto"),
                                                          respuesta.getDouble("precioProducto"),
                                                          respuesta.getInt("totalCantidadProducto"),
                                                          respuesta.getDouble("totalPrecioProducto")
                            )
                    );
                }
            }
            
        }
        
        if ( !reporte.isEmpty() ){
            
            sentenciaSQL = "SELECT\n" +
                            "    comp.id_proveedor AS 'idProveedor',\n" +
                            "    prov.nombre_comercial AS 'nombreProveedor',\n" +
                            "    COUNT( comp.id_compra ) AS 'cantidadCompras'\n" +
                            "FROM\n" +
                            "    compras comp\n" +
                            "INNER JOIN\n" +
                            "    proveedores prov\n" +
                            "ON\n" +
                            "    comp.id_proveedor = prov.id_proveedor \n";
                
            if( filtro.getIdProducto() != null )
                sentenciaSQL += "INNER JOIN\n" +
                                "    detalle_de_compras det\n" +
                                "ON\n" +
                                "    det.id_compra = comp.id_compra \n" ;
            
            sentenciaSQL += "WHERE\n" +
                            "    (comp.fecha_hora_compra BETWEEN ? AND ?) \n" ;
                  
            if( filtro.getIdProveedor()!= null )
                sentenciaSQL += "    AND (comp.id_proveedor = ?)\n" ;
        
            if( filtro.getIdProducto() != null )
                sentenciaSQL += "    AND (det.id_producto = ?)\n" ;

            if( filtro.getIdUsuario() != null )
                sentenciaSQL += "    AND (comp.id_usuario = ?)\n";
            
            
            sentenciaSQL += "GROUP BY\n" +
                            "    comp.id_proveedor,\n" +
                            "    prov.nombre_comercial";
            
            try( PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL) ){
        
                int marcador = 1;
                consulta.setObject( marcador++ , filtro.getFechaInferior() );
                consulta.setObject( marcador++ , filtro.getFechaSuperior());

                if( filtro.getIdProveedor() != null )
                    consulta.setInt(marcador++, filtro.getIdProveedor());

                if( filtro.getIdProducto() != null )
                    consulta.setInt(marcador++, filtro.getIdProducto());

                if( filtro.getIdUsuario() != null )
                    consulta.setInt(marcador++, filtro.getIdUsuario());

                try( ResultSet respuesta = consulta.executeQuery() ){
                    while( respuesta.next() ){
                        reporte.agregarInformacionTercero(
                                respuesta.getInt("idProveedor"), 
                                respuesta.getString("nombreProveedor"), 
                                respuesta.getInt("cantidadCompras")
                        );
                    }
                }

            }

        }
        
        return reporte;
    
    }
    
}

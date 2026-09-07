
package jafrinventarios.servicios.ventas;


import jafrinventarios.DTOs.ventas.DTODetalleVentaTabla;
import jafrinventarios.DTOs.ventas.DTOVentaTabla;
import jafrinventarios.modelos.ventas.ModeloDetalleVenta;
import jafrinventarios.modelos.ventas.ModeloVenta;
import jafrinventarios.servicios.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author JOHN FORERO
 */
public class ServicioVentas {

    public ServicioVentas() {
    }
    
    public List<DTOVentaTabla> obtenerTodasLasVentas () throws Exception{
    
        LinkedHashMap<Integer, DTOVentaTabla> diccionarioVentas = new LinkedHashMap<>();
        
        Connection conexionDB = ConexionDB.getConnection();
        
        String sentenciaSQL = 
                "SELECT\n" +
                "    ven.id_venta AS 'id',\n" +
                "    ven.fecha_hora_venta AS 'fecha',\n" +
                "    ven.total_venta AS 'total',\n" +
                "    cli.nombre_negocio AS 'nombreCliente',\n" +
                "    us.alias_usuario AS 'aliasUsuario'\n" +
                "FROM\n" +
                "    ventas ven\n" +
                "INNER JOIN \n" +
                "    clientes cli\n" +
                "ON ven.id_cliente = cli.id_cliente\n" +
                "INNER JOIN\n" +
                "    usuarios us\n" +
                "ON\n" +
                "    ven.id_usuario = us.id_usuario\n" +
                "ORDER BY 1 ASC";
        
        try(
            PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL);
            ResultSet respuesta = consulta.executeQuery();
            ){
            
            while(respuesta.next()){
                diccionarioVentas.put( 
                        respuesta.getInt("id"), 
                        new DTOVentaTabla(
                                respuesta.getInt("id"),
                                respuesta.getTimestamp("fecha"),
                                respuesta.getDouble("total"),
                                respuesta.getString("nombreCliente"),
                                respuesta.getString("aliasUsuario")
                        )
                );
            }
        
        }
        
        if( diccionarioVentas.isEmpty() ) 
            return new ArrayList<>();
        
        sentenciaSQL =  "SELECT\n" +
                        "    det.id_venta AS 'idVenta',\n" +
                        "    prod.nombre_producto AS 'nombreProducto',\n" +
                        "    det.cantidad_producto AS 'cantidad',\n" +
                        "    det.precio_unitario_producto AS 'precio',\n" +
                        "    det.precio_total_producto AS 'total'\n" +
                        "FROM\n" +
                        "    detalle_de_ventas det\n" +
                        "INNER JOIN\n" +
                        "    productos prod\n" +
                        "ON det.id_producto = prod.id_producto\n" +
                        "WHERE\n" +
                        "    det.id_venta IN (";
        for(int i=0; i<diccionarioVentas.size()-1; i++){
            sentenciaSQL+= "? ," ;
        }
            sentenciaSQL+= "? )\n ORDER BY 1";
        
        try(PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL)){
        
            int contadorIds = 0;
            for(int id: diccionarioVentas.keySet()){
                consulta.setInt( ++contadorIds, id );
            }

            try(ResultSet respuesta = consulta.executeQuery()){
            
                while ( respuesta.next() ) { 
                    DTOVentaTabla venta = diccionarioVentas.get(respuesta.getInt("idVenta"));
                    venta.agregarDetalle(
                            new DTODetalleVentaTabla(
                                    respuesta.getString("nombreProducto"),
                                    respuesta.getInt("cantidad"),
                                    respuesta.getDouble("precio"),
                                    respuesta.getDouble("total")
                          )
                    );
                }

            }
        
        }
        
        return new ArrayList<>(diccionarioVentas.values());
        
    }
    
    
    
    public List<DTOVentaTabla> obtenerListaVentasPorFiltro ( String filtro ) throws Exception{
    
        LinkedHashMap<Integer, DTOVentaTabla> diccionarioVentas = new LinkedHashMap<>();
        
        Connection conexionDB = ConexionDB.getConnection();
        
        String sentenciaSQL = 
                "SELECT\n" +
                "    ven.id_venta AS 'idVenta',\n" +
                "    ven.fecha_hora_venta AS 'fecha',\n" +
                "    ven.total_venta AS 'totalVenta',\n" +
                "    cli.nombre_negocio AS 'nombreCliente',\n" +
                "    us.alias_usuario AS 'aliasUsuario',\n" +
                "\n" +
                "    prod.nombre_producto AS 'nombreProducto',\n" +
                "    det.cantidad_producto AS 'cantidad',\n" +
                "    det.precio_unitario_producto AS 'precio',\n" +
                "    det.precio_total_producto AS 'totalDetalle'\n" +
                "FROM    ventas ven\n" +
                "INNER JOIN clientes cli           ON ven.id_cliente = cli.id_cliente\n" +
                "INNER JOIN usuarios us            ON ven.id_usuario = us.id_usuario\n" +
                "INNER JOIN detalle_de_ventas det  ON ven.id_venta = det.id_venta\n" +
                "INNER JOIN productos prod         ON det.id_producto = prod.id_producto\n" +
                "WHERE\n" +
                "    (   cli.nombre_negocio LIKE ? OR\n" +
                "        us.alias_usuario LIKE ? OR\n" +
                "        prod.nombre_producto LIKE ?\n" +
                "      )\n" +
                "ORDER BY 1";
        
        filtro = "%" + filtro + "%";
        
        try(PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL)){
        
            consulta.setString(1, filtro);
            consulta.setString(2, filtro);
            consulta.setString(3, filtro);

            try(ResultSet respuesta = consulta.executeQuery()){
            
                while ( respuesta.next() ) { 
                    
                    if( !diccionarioVentas.containsKey( respuesta.getInt("idVenta") )){
                        diccionarioVentas.put(
                                respuesta.getInt("idVenta"), 
                                new DTOVentaTabla(
                                    respuesta.getInt("idVenta"),
                                    respuesta.getTimestamp("fecha"),
                                    respuesta.getDouble("totalVenta"),
                                    respuesta.getString("nombreCliente"),
                                    respuesta.getString("aliasUsuario")
                                )
                        );
                    
                    }
                    
                    DTOVentaTabla compra = diccionarioVentas.get(respuesta.getInt("idVenta"));
                    compra.agregarDetalle(
                            new DTODetalleVentaTabla(
                                    respuesta.getString("nombreProducto"),
                                    respuesta.getInt("cantidad"),
                                    respuesta.getDouble("precio"),
                                    respuesta.getDouble("totalDetalle")
                          )
                    );
                }

            }
        
        }
        
        return new ArrayList<>(diccionarioVentas.values());
        
    }
    
    
    /*
        Consultar una venta para armar un DTOVentaTabla con el fin de 
        actualizar la tabla si se edito una venta o se creo una nueva
    */
    public DTOVentaTabla obtenerDatosDTOVenta ( Integer idVenta ) throws Exception{
    
        DTOVentaTabla venta;
        
        Connection conexionDB = ConexionDB.getConnection();
        
        String sentenciaSQL = 
                "SELECT\n" +
                "    ven.id_venta AS 'id',\n" +
                "    ven.fecha_hora_venta AS 'fecha',\n" +
                "    ven.total_venta AS 'totalVenta',\n" +
                "    cli.nombre_negocio AS 'nombreCliente',\n" +
                "    us.alias_usuario AS 'aliasUsuario',\n" +
                "\n" +
                "    prod.nombre_producto AS 'nombreProducto',\n" +
                "    det.cantidad_producto AS 'cantidad',\n" +
                "    det.precio_unitario_producto AS 'precio',\n" +
                "    det.precio_total_producto AS 'totalDetalle'\n" +
                "FROM    ventas ven\n" +
                "INNER JOIN clientes cli           ON ven.id_cliente = cli.id_cliente\n" +
                "INNER JOIN usuarios us            ON ven.id_usuario = us.id_usuario\n" +
                "INNER JOIN detalle_de_ventas det  ON ven.id_venta = det.id_venta\n" +
                "INNER JOIN productos prod         ON det.id_producto = prod.id_producto\n" +
                "WHERE\n" +
                "    ven.id_venta = ?";
        
        try(PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL)){
        
            consulta.setInt(1, idVenta);

            try(ResultSet respuesta = consulta.executeQuery()){
            
                if ( respuesta.next() ) { 
                    venta = new DTOVentaTabla(
                            respuesta.getInt("id"),
                            respuesta.getTimestamp("fecha"),
                            respuesta.getDouble("totalVenta"),
                            respuesta.getString("nombreCliente"),
                            respuesta.getString("aliasUsuario")
                        );       
                    do{
                        venta.agregarDetalle(
                                new DTODetalleVentaTabla(
                                        respuesta.getString("nombreProducto"),
                                        respuesta.getInt("cantidad"),
                                        respuesta.getDouble("precio"),
                                        respuesta.getDouble("totalDetalle")
                              )
                        );
                    }while(respuesta.next());
                }else
                    throw new Exception("No existe una venta con el id : " + idVenta );
                
            }
        
        }
        
        return venta;
        
    }
    
    
    /*
        Consultar una compra para armar un ModeloVentaTabla con el fin de 
        poder editarlo
    */
    public ModeloVenta obtenerModeloVenta ( Integer idVenta ) throws Exception{
    
        ModeloVenta venta;
        
        Connection conexionDB = ConexionDB.getConnection();
        
        String sentenciaSQL = 
                "SELECT\n" +
                "    ven.id_venta AS 'idVenta',\n" +
                "    ven.fecha_hora_venta AS 'fecha',\n" +
                "    ven.total_venta AS 'totalVenta',\n" +
                "    ven.id_cliente AS 'idCliente',\n" +
                "    ven.id_usuario AS 'idUsuario',\n" +
                "\n" +
                "    det.id_detalle_venta AS 'idDetalle',\n" +
                "    det.id_producto AS 'idProducto',\n" +
                "    det.cantidad_producto AS 'cantidadProducto',\n" +
                "    det.precio_unitario_producto AS 'precioProducto',\n" +
                "    det.precio_total_producto AS 'totalDetalle'\n" +
                "FROM    ventas ven\n" +
                "INNER JOIN detalle_de_ventas det  ON ven.id_venta = det.id_venta\n" +
                "WHERE\n" +
                "    ven.id_venta = ?";
        
        try(PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL)){
        
            consulta.setInt(1, idVenta);

            try(ResultSet respuesta = consulta.executeQuery()){
            
                if ( respuesta.next() ) { 
                    venta = new ModeloVenta(
                            respuesta.getInt("idVenta"),
                            respuesta.getTimestamp("fecha"),
                            respuesta.getDouble("totalVenta"),
                            respuesta.getInt("idCliente"),
                            respuesta.getInt("idUsuario")
                        );       
                    do{
                        venta.agregarDetalle(
                                new ModeloDetalleVenta(
                                        respuesta.getInt("idDetalle"),
                                        respuesta.getInt("idProducto"),
                                        respuesta.getInt("cantidadProducto"),
                                        respuesta.getDouble("precioProducto"),
                                        respuesta.getDouble("totalDetalle")
                              )
                        );
                    }while(respuesta.next());
                }else
                    throw new Exception("No existe una venta con el id : " + idVenta );
                
            }
        
        }
        
        return venta;
        
    }
    
    
    
}

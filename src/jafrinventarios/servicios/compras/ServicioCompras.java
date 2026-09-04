
package jafrinventarios.servicios.compras;

import jafrinventarios.DTOs.compras.DTOCompraTabla;
import jafrinventarios.DTOs.compras.DTODetalleCompraTabla;
import jafrinventarios.modelos.compras.ModeloCompra;
import jafrinventarios.servicios.ConexionDB;
import jafrinventarios.servicios.excepciones.ExcepcionValidacionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author JOHN FORERO
 */
public class ServicioCompras {
    
    
    public List<DTOCompraTabla> obtenerTodasLasCompras () throws Exception{
    
        LinkedHashMap<Integer, DTOCompraTabla> diccionarioCompras = new LinkedHashMap<>();
        
        Connection conexionDB = ConexionDB.getConnection();
        
        String sentenciaSQL = 
                "SELECT\n" +
        "    comp.id_compra AS 'id',\n" +
        "    comp.fecha_hora_compra AS 'fecha',\n" +
        "    comp.total_compra AS 'total',\n" +
        "    pv.nombre_comercial AS 'nombreProveedor',\n" +
        "    us.alias_usuario AS 'aliasUsuario'\n" +
        "FROM\n" +
        "    compras comp\n" +
        "INNER JOIN \n" +
        "    proveedores pv\n" +
        "ON comp.id_proveedor = pv.id_proveedor\n" +
        "INNER JOIN\n" +
        "    usuarios us\n" +
        "ON\n" +
        "    comp.id_usuario = us.id_usuario\n" +
        "ORDER BY 1 ASC;";
        
        try(
            PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL);
            ResultSet respuesta = consulta.executeQuery();
            ){
            
            while(respuesta.next()){
                diccionarioCompras.put( 
                        respuesta.getInt("id"), 
                        new DTOCompraTabla(
                                respuesta.getInt("id"),
                                respuesta.getTimestamp("fecha"),
                                respuesta.getDouble("total"),
                                respuesta.getString("nombreProveedor"),
                                respuesta.getString("aliasUsuario")
                        )
                );
            }
        
        }
        
        if( diccionarioCompras.isEmpty() ) 
            return new ArrayList<>();
        
        sentenciaSQL =  "SELECT\n" +
                        "    det.id_compra AS 'idCompra',\n" +
                        "    prod.nombre_producto AS 'nombreProducto',\n" +
                        "    det.cantidad_producto AS 'cantidad',\n" +
                        "    det.precio_unitario_producto AS 'precio',\n" +
                        "    det.precio_total_producto AS 'total'\n" +
                        "FROM\n" +
                        "    detalle_de_compras det\n" +
                        "INNER JOIN\n" +
                        "    productos prod\n" +
                        "ON det.id_producto = prod.id_producto\n" +
                        "WHERE\n" +
                        "    det.id_compra IN ( ";
        for(int i=0; i<diccionarioCompras.size()-1; i++){
            sentenciaSQL+= "? ," ;
        }
            sentenciaSQL+= "? )\n ORDER BY 1";
        
        try(PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL)){
        
            int contadorIds = 0;
            for(int id: diccionarioCompras.keySet()){
                consulta.setInt( ++contadorIds, id );
            }

            try(ResultSet respuesta = consulta.executeQuery()){
            
                while ( respuesta.next() ) { 
                    DTOCompraTabla compra = diccionarioCompras.get(respuesta.getInt("idCompra"));
                    compra.agregarDetalle(
                            new DTODetalleCompraTabla(
                                    respuesta.getString("nombreProducto"),
                                    respuesta.getInt("cantidad"),
                                    respuesta.getDouble("precio"),
                                    respuesta.getDouble("total")
                          )
                    );
                }

            }
        
        }
        
        return new ArrayList<>(diccionarioCompras.values());
        
    }
    
    
}

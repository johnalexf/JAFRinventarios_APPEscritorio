
package jafrinventarios.servicios.compras;

import jafrinventarios.DTOs.compras.DTOCompraTabla;
import jafrinventarios.DTOs.compras.DTODetalleCompraTabla;
import jafrinventarios.modelos.compras.ModeloCompra;
import jafrinventarios.modelos.compras.ModeloDetalleCompra;
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
    
    
    public List<DTOCompraTabla> obtenerListaComprasPorFiltro ( String filtro ) throws Exception{
    
        LinkedHashMap<Integer, DTOCompraTabla> diccionarioCompras = new LinkedHashMap<>();
        
        Connection conexionDB = ConexionDB.getConnection();
        
        String sentenciaSQL = 
                "SELECT\n" +
                "    comp.id_compra AS 'idCompra',\n" +
                "    comp.fecha_hora_compra AS 'fecha',\n" +
                "    comp.total_compra AS 'totalCompra',\n" +
                "    pv.nombre_comercial AS 'nombreProveedor',\n" +
                "    us.alias_usuario AS 'aliasUsuario',\n" +
                "\n" +
                "    prod.nombre_producto AS 'nombreProducto',\n" +
                "    det.cantidad_producto AS 'cantidad',\n" +
                "    det.precio_unitario_producto AS 'precio',\n" +
                "    det.precio_total_producto AS 'totalDetalle'\n" +
                "FROM    compras comp\n" +
                "INNER JOIN proveedores pv         ON comp.id_proveedor = pv.id_proveedor\n" +
                "INNER JOIN usuarios us            ON comp.id_usuario = us.id_usuario\n" +
                "INNER JOIN detalle_de_compras det ON comp.id_compra = det.id_compra\n" +
                "INNER JOIN productos prod         ON det.id_producto = prod.id_producto\n" +
                "WHERE\n" +
                "    (   pv.nombre_comercial LIKE ? OR\n" +
                "        us.alias_usuario LIKE ? OR\n" +
                "        prod.nombre_producto LIKE ?\n" +
                "      )" +
                "\n ORDER BY 1";
        
        filtro = "%" + filtro + "%";
        
        try(PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL)){
        
            consulta.setString(1, filtro);
            consulta.setString(2, filtro);
            consulta.setString(3, filtro);

            try(ResultSet respuesta = consulta.executeQuery()){
            
                while ( respuesta.next() ) { 
                    
                    if( !diccionarioCompras.containsKey( respuesta.getInt("idCompra") )){
                        diccionarioCompras.put(
                                respuesta.getInt("idCompra"), 
                                new DTOCompraTabla(
                                    respuesta.getInt("idCompra"),
                                    respuesta.getTimestamp("fecha"),
                                    respuesta.getDouble("totalCompra"),
                                    respuesta.getString("nombreProveedor"),
                                    respuesta.getString("aliasUsuario")
                                )
                        );
                    
                    }
                    
                    DTOCompraTabla compra = diccionarioCompras.get(respuesta.getInt("idCompra"));
                    compra.agregarDetalle(
                            new DTODetalleCompraTabla(
                                    respuesta.getString("nombreProducto"),
                                    respuesta.getInt("cantidad"),
                                    respuesta.getDouble("precio"),
                                    respuesta.getDouble("totalDetalle")
                          )
                    );
                }

            }
        
        }
        
        return new ArrayList<>(diccionarioCompras.values());
        
    }
    
    /*
        Consultar una compra para armar un DTOCompraTabla con el fin de 
        actualizar la tabla si se edito una compra o se creo una nueva
    */
    public DTOCompraTabla obtenerDatosDTOCompra ( Integer idCompra ) throws Exception{
    
        DTOCompraTabla compra;
        
        Connection conexionDB = ConexionDB.getConnection();
        
        String sentenciaSQL = 
                "SELECT\n" +
                "    comp.id_compra AS 'idCompra',\n" +
                "    comp.fecha_hora_compra AS 'fecha',\n" +
                "    comp.total_compra AS 'totalCompra',\n" +
                "    pv.nombre_comercial AS 'nombreProveedor',\n" +
                "    us.alias_usuario AS 'aliasUsuario',\n" +
                "\n" +
                "    prod.nombre_producto AS 'nombreProducto',\n" +
                "    det.cantidad_producto AS 'cantidad',\n" +
                "    det.precio_unitario_producto AS 'precio',\n" +
                "    det.precio_total_producto AS 'totalDetalle'\n" +
                "FROM    compras comp\n" +
                "INNER JOIN proveedores pv         ON comp.id_proveedor = pv.id_proveedor\n" +
                "INNER JOIN usuarios us            ON comp.id_usuario = us.id_usuario\n" +
                "INNER JOIN detalle_de_compras det ON comp.id_compra = det.id_compra\n" +
                "INNER JOIN productos prod         ON det.id_producto = prod.id_producto\n" +
                "WHERE\n" +
                "    comp.id_compra = ?";
        
        try(PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL)){
        
            consulta.setInt(1, idCompra);

            try(ResultSet respuesta = consulta.executeQuery()){
            
                if ( respuesta.next() ) { 
                    compra = new DTOCompraTabla(
                            respuesta.getInt("idCompra"),
                            respuesta.getTimestamp("fecha"),
                            respuesta.getDouble("totalCompra"),
                            respuesta.getString("nombreProveedor"),
                            respuesta.getString("aliasUsuario")
                        );       
                    do{
                        compra.agregarDetalle(
                                new DTODetalleCompraTabla(
                                        respuesta.getString("nombreProducto"),
                                        respuesta.getInt("cantidad"),
                                        respuesta.getDouble("precio"),
                                        respuesta.getDouble("totalDetalle")
                              )
                        );
                    }while(respuesta.next());
                }else
                    throw new Exception("No existe una compra con el id : " + idCompra );
                
            }
        
        }
        
        return compra;
        
    }
    
    
    /*
        Consultar una compra para armar un ModeloCompraTabla con el fin de 
        poder editarlo
    */
    public ModeloCompra obtenerModeloCompra ( Integer idCompra ) throws Exception{
    
        ModeloCompra compra;
        
        Connection conexionDB = ConexionDB.getConnection();
        
        String sentenciaSQL = 
                "SELECT\n" +
                "    comp.id_compra AS 'idCompra',\n" +
                "    comp.fecha_hora_compra AS 'fecha',\n" +
                "    comp.total_compra AS 'totalCompra',\n" +
                "    comp.id_proveedor AS 'idProveedor',\n" +
                "    comp.id_usuario AS 'idUsuario',\n" +
                "\n" +
                "    det.id_detalle_compra AS 'idDetalle',\n" +
                "    det.id_producto AS 'idProducto',\n" +
                "    det.cantidad_producto AS 'cantidadProducto',\n" +
                "    det.precio_unitario_producto AS 'precioProducto',\n" +
                "    det.precio_total_producto AS 'totalDetalle'\n" +
                "FROM    compras comp\n" +
                "INNER JOIN detalle_de_compras det ON comp.id_compra = det.id_compra\n" +
                "WHERE\n" +
                "    comp.id_compra = ?";
        
        try(PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL)){
        
            consulta.setInt(1, idCompra);

            try(ResultSet respuesta = consulta.executeQuery()){
            
                if ( respuesta.next() ) { 
                    compra = new ModeloCompra(
                            respuesta.getInt("idCompra"),
                            respuesta.getTimestamp("fecha"),
                            respuesta.getDouble("totalCompra"),
                            respuesta.getInt("idProveedor"),
                            respuesta.getInt("idUsuario")
                        );       
                    do{
                        compra.agregarDetalle(
                                new ModeloDetalleCompra(
                                        respuesta.getInt("idDetalle"),
                                        respuesta.getInt("idProducto"),
                                        respuesta.getInt("cantidadProducto"),
                                        respuesta.getDouble("precioProducto"),
                                        respuesta.getDouble("totalDetalle")
                              )
                        );
                    }while(respuesta.next());
                }else
                    throw new Exception("No existe una compra con el id : " + idCompra );
                
            }
        
        }
        
        return compra;
        
    }
    
    
    public int crearCompra ( ModeloCompra compra ) throws Exception{
        
        List<ModeloDetalleCompra> detalles = compra.getDetalles();
        if(detalles.isEmpty())
            throw new Exception("La compra no se puede crear sin detalles");
        
        Connection conexionDB = ConexionDB.getConnection();
        
        try{
 
            // 1. Apagar el autoguardado para iniciar la transacción
            /*
            Esto nos permite configurar a MySQL en un estado de espera a confirmar
            que toda la informacion esperada, ya se ha cargado; esto con el fin 
            de que en dado caso que no se complete, MySQL no guarda ninguno de los registros
            ya que los tiene listos en un espacio de memoria pero aun no almacenados en 
            la base de datos.
            */
            conexionDB.setAutoCommit(false);
            
            String sentenciaSQL =
                    "INSERT INTO\n" +
                    "    compras(\n" +
                    "        fecha_hora_compra,\n" +
                    "        total_compra,\n" +
                    "        id_proveedor,\n" +
                    "        id_usuario \n" +
                    "    )\n" +
                    "VALUES\n" +
                    "    ( ? , ? , ? , ? )";

            try( PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL , Statement.RETURN_GENERATED_KEYS)){

                java.sql.Timestamp fecha = new java.sql.Timestamp( compra.getFechaHoraCompra().getTime());
                consulta.setTimestamp(1, fecha);
                consulta.setDouble(2, compra.getTotalCompra());
                consulta.setInt(3, compra.getIdProveedor());
                consulta.setInt(4, compra.getIdUsuario());

                int filasAfectadas = consulta.executeUpdate();
                if( filasAfectadas == 1 ){
                    try( ResultSet respuesta = consulta.getGeneratedKeys() ){ 
                        if( respuesta.next() ){
                            compra.setIdCompra( respuesta.getInt( 1 ) );
                        }else{
                            throw new Exception( "Error al obtener el id de la compra" );
                        }
                    }
                }else
                    throw new Exception("No se pudo crear la compra");

            }

            sentenciaSQL = "INSERT INTO\n" +
                            "    detalle_de_compras(\n" +
                            "        id_compra,\n" +
                            "        id_producto,\n" +
                            "        cantidad_producto,\n" +
                            "        precio_unitario_producto,\n" +
                            "        precio_total_producto\n" +
                            "    )\n" +
                            "VALUES \n";

            for( int i=0; i<detalles.size()-1 ; i++){
                sentenciaSQL += "( ? , ? , ? , ? , ?), \n";
            }
            sentenciaSQL += "( ? , ? , ? , ? , ? )";

            try(PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL)){

                int indiceValor = 0;
                for(ModeloDetalleCompra detalle: detalles){
                    consulta.setInt( ++indiceValor, compra.getIdCompra() );
                    consulta.setInt( ++indiceValor, detalle.getIdProducto() );
                    consulta.setInt( ++indiceValor, detalle.getCantidadProducto() );
                    consulta.setDouble(++indiceValor, detalle.getPrecioUnitarioProducto() );
                    consulta.setDouble(++indiceValor, detalle.getPrecioTotalProducto() );
                }

                int filasAfectadas = consulta.executeUpdate();
                if( filasAfectadas != detalles.size()){
                    throw new Exception("No se pudieron crear los detalles de la compra");
                }
            }

            // 2. Si las dos inserciones fueron exitosas, guardamos los cambios definitivamente
            conexionDB.commit();
            return compra.getIdCompra();
            
        } catch (Exception e) {
            // 3. Si hubo cualquier error, revertimos absolutamente todo
            conexionDB.rollback();
            throw e; 
        } finally {
            // 4. Restauramos el comportamiento por defecto de la conexión para no afectar otros módulos
            conexionDB.setAutoCommit(true);
        }
           
    }
    
}

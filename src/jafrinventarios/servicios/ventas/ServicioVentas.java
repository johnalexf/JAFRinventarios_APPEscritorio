
package jafrinventarios.servicios.ventas;


import jafrinventarios.DTOs.ventas.DTODetalleVentaTabla;
import jafrinventarios.DTOs.ventas.DTOVentaTabla;
import jafrinventarios.modelos.productos.ModeloProducto;
import jafrinventarios.modelos.ventas.ModeloDetalleVenta;
import jafrinventarios.modelos.ventas.ModeloVenta;
import jafrinventarios.servicios.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
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
                "ORDER BY ven.id_venta DESC";
        
        try(
            PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL);
            ResultSet respuesta = consulta.executeQuery();
            ){
            
            while(respuesta.next()){
                diccionarioVentas.put( 
                        respuesta.getInt("id"), 
                        new DTOVentaTabla(
                                respuesta.getInt("id"),
                                respuesta.getObject("fecha", LocalDateTime.class),
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
            sentenciaSQL+= "? )\n ORDER BY det.id_venta DESC, det.id_detalle_venta ASC";
        
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
                "ORDER BY ven.id_venta DESC, det.id_detalle_venta ASC";
        
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
                                     respuesta.getObject("fecha", LocalDateTime.class),
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
                "    ven.id_venta = ? "+
                "ORDER BY det.id_detalle_venta ASC";
        
        try(PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL)){
        
            consulta.setInt(1, idVenta);

            try(ResultSet respuesta = consulta.executeQuery()){
            
                if ( respuesta.next() ) { 
                    venta = new DTOVentaTabla(
                            respuesta.getInt("id"),
                            respuesta.getObject("fecha", LocalDateTime.class),
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
                "    ven.id_venta = ? "+
                "ORDER BY det.id_detalle_venta ASC";
        
        try(PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL)){
        
            consulta.setInt(1, idVenta);

            try(ResultSet respuesta = consulta.executeQuery()){
            
                if ( respuesta.next() ) { 
                    venta = new ModeloVenta(
                            respuesta.getInt("idVenta"),
                             respuesta.getObject("fecha", LocalDateTime.class),
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
    
    
    public int crearVenta ( ModeloVenta venta ) throws Exception{
        
        if(venta.getDetalles().isEmpty())
            throw new Exception("La venta no se puede crear sin detalles");
        
        Connection conexionDB = ConexionDB.getConnection();
        
        comprobarDisponibilidadProductos(conexionDB, venta.getDetalles());
        
        try{

            // 1. Apagar el autoguardado para iniciar la transacción
            conexionDB.setAutoCommit(false);
            
            venta.setIdVenta( crearDatosGenerales(conexionDB, venta) );

            crearDetalles(conexionDB, venta.getDetalles(), venta.getIdVenta());
            
            actualizarCantidadDisponible(conexionDB, venta.getDetalles(), false);
            
            // 2. Si las dos inserciones fueron exitosas, guardamos los cambios definitivamente
            conexionDB.commit();
            return venta.getIdVenta();
            
        } catch (Exception e) {
            // 3. Si hubo cualquier error, revertimos absolutamente todo
            conexionDB.rollback();
            throw new Exception("La venta no se registro debido a : \n" + e.getMessage());
        } finally {
            // 4. Restauramos el comportamiento por defecto de la conexión para no afectar otros módulos
            conexionDB.setAutoCommit(true);
        }
           
    }
    
    
    public void editarVenta( ModeloVenta ventaAEditar ) throws Exception{
        
        if(ventaAEditar.getDetalles().isEmpty())
            throw new Exception("La venta no se puede editar sin detalles");
        
        ModeloVenta ventaOriginal = obtenerModeloVenta(ventaAEditar.getIdVenta());
        
        if(ventaOriginal.equals(ventaAEditar))
            throw new Exception("No hay cambios en la venta para guardar");
        
        Connection conexionDB = ConexionDB.getConnection();
        
        try{
            conexionDB.setAutoCommit(false);
            
            if( !ventaOriginal.sonIgualesDatosGenerales(ventaAEditar) ){
                actualizarDatosGenerales(conexionDB, ventaAEditar);
            }
            
            if( !ventaOriginal.sonIgualesDetalles(ventaAEditar) ){
            
                /*Si no son iguales las lista de detalles, entonces eliminamos
                todos los detalles, no sin antes sumar las cantidades a los
                productos disponibles y despues creamos los nuevos detalles
                disminuyendo las cantidades a el stock disponible*/
                
                /*
                Sumar la cantidad de producto de cada detalle original antes de eliminarlos
                */
                actualizarCantidadDisponible(conexionDB, ventaOriginal.getDetalles(), true);
                
                /*
                Eliminar los detalles de la venta original
                */
                eliminarDetalles(conexionDB, ventaOriginal.getIdVenta(), ventaOriginal.getDetalles().size());
                
                
                comprobarDisponibilidadProductos(conexionDB, ventaAEditar.getDetalles());
               
                /*
                Crear los nuevos detalles
                */
                crearDetalles(conexionDB, ventaAEditar.getDetalles(), ventaAEditar.getIdVenta());

                /*
                Actualizar la cantidad de producto disponible
                */
                actualizarCantidadDisponible(conexionDB, ventaAEditar.getDetalles(), false);
                        
            }
            
            conexionDB.commit();
            
        } catch (Exception e) {
            conexionDB.rollback();
            throw new Exception("La venta no se edito debido a : \n" + e.getMessage());
        } finally {
            conexionDB.setAutoCommit(true);
        }
    
    }
    
    
    public void comprobarDisponibilidadProductos ( Connection conexionDB, ArrayList<ModeloDetalleVenta> detalles ) throws Exception{
    
        String sentenciaSQL = 
                    "SELECT\n" +
                    "    id_producto AS 'id',\n" +
                    "    nombre_producto AS 'nombreProducto',\n" +
                    "    cantidad_disponible AS 'cantidadDisponible'\n" +
                    "FROM\n" +
                    "    productos\n" +
                    "WHERE\n" +
                    "    id_producto IN ( ";
        for ( int i=0; i< detalles.size()-1; i++) {
            sentenciaSQL += " ? ,"; 
        }
        sentenciaSQL += " ? )";
        
        HashMap<Integer, ModeloProducto> diccionarioProductos = new HashMap<>();
        
        try( PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL)){
        
            int indiceConsulta=0;
            for( ModeloDetalleVenta detalle : detalles){
                consulta.setInt( ++indiceConsulta , detalle.getIdProducto());
            }
            
            try(ResultSet respuesta = consulta.executeQuery() ){
                while( respuesta.next() ){
                    ModeloProducto producto = new ModeloProducto();
                    producto.setNombreProducto( respuesta.getString("nombreProducto"));
                    producto.setCantidadDisponible(respuesta.getInt("cantidadDisponible"));
                    diccionarioProductos.put(
                                respuesta.getInt("id"), 
                                producto
                    );
                }
            }
        }
        
        StringBuilder errores = new StringBuilder();
        
        for( ModeloDetalleVenta detalle : detalles){
            if( !diccionarioProductos.containsKey( detalle.getIdProducto() )  )
                throw new Exception("Error al verificar las cantidades disponibles");
            ModeloProducto producto = diccionarioProductos.get(detalle.getIdProducto());
            if( producto.getCantidadDisponible() < detalle.getCantidadProducto() ){
                errores.append("\n ")
                       .append(producto.getNombreProducto())
                       .append(" disponibles ").append(producto.getCantidadDisponible())
                       .append(" solicitadas ").append(detalle.getCantidadProducto());
            }
        }
        
        if( errores.length() != 0){
            throw new Exception(
                    "Los siguientes productos no tienen suficientes cantidades disponibles para la venta"+
                     errores.toString()
            );
        }

    }
    
    
    public void eliminarVenta ( int idVenta ) throws Exception{
    
        ModeloVenta venta = obtenerModeloVenta(idVenta);
        
        Connection conexionDB = ConexionDB.getConnection();
        
        try {

            conexionDB.setAutoCommit(false);
            
            actualizarCantidadDisponible(conexionDB, venta.getDetalles(), true);
            
            eliminarDetalles(conexionDB, idVenta, venta.getDetalles().size());
            
            String sentenciaSQL = "DELETE FROM ventas WHERE id_venta = ?";
            
            try(PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL)){
                consulta.setInt(1, idVenta);
                int filasAfectadas = consulta.executeUpdate();
                if(filasAfectadas != 1)
                    throw new Exception("No se pudo eliminar los datos generales de la venta");
            }
            
           conexionDB.commit();
            
        } catch (Exception e) {
            conexionDB.rollback();
            throw new Exception("La venta no se pudo eliminar debido a que : \n" + e.getMessage());
        } finally {
            conexionDB.setAutoCommit(true);
        }
    
    }
    
    
    
    private int crearDatosGenerales( Connection conexionDB, ModeloVenta venta) throws Exception{
        
        String sentenciaSQL =
                "INSERT INTO\n" +
                "    ventas(\n" +
                "        fecha_hora_venta,\n" +
                "        total_venta,\n" +
                "        id_cliente,\n" +
                "        id_usuario \n" +
                "    )\n" +
                "VALUES\n" +
                "    ( ? , ? , ? , ? )";

        try( PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL , Statement.RETURN_GENERATED_KEYS)){

            consulta.setObject(1, venta.getFechaHoraVenta());
            consulta.setDouble(2, venta.getTotalVenta());
            consulta.setInt(3, venta.getIdCliente());
            consulta.setInt(4, venta.getIdUsuario());

            int filasAfectadas = consulta.executeUpdate();
            if( filasAfectadas == 1 ){
                try( ResultSet respuesta = consulta.getGeneratedKeys() ){ 
                    if( respuesta.next() ){
                        return ( respuesta.getInt( 1 ) );
                    }else{
                        throw new Exception( "Error al obtener el id de la venta" );
                    }
                }
            }else
                throw new Exception("No se pudo crear el registro base de la venta");

        }
    }
    
    
    private void crearDetalles(Connection conexionDB, ArrayList<ModeloDetalleVenta> detalles, Integer idVenta) throws Exception{
        
        String sentenciaSQL = 
                    "INSERT INTO\n" +
                    "    detalle_de_ventas(\n" +
                    "        id_venta,\n" +
                    "        id_producto,\n" +
                    "        cantidad_producto,\n" +
                    "        precio_unitario_producto,\n" +
                    "        precio_total_producto\n" +
                    "    )\n" +
                    "VALUES\n" +
                    "    ( ? , ? , ? , ? , ?)";

        try(PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL)){

            for(ModeloDetalleVenta detalle: detalles){
                consulta.setInt( 1, idVenta );
                consulta.setInt( 2, detalle.getIdProducto() );
                consulta.setInt( 3, detalle.getCantidadProducto() );
                consulta.setDouble( 4, detalle.getPrecioUnitarioProducto() );
                consulta.setDouble( 5, detalle.getPrecioTotalProducto() );
                consulta.addBatch();
            }

            int[] resultados = consulta.executeBatch();

            for (int filasAfectadas: resultados ){
                if( filasAfectadas != 1 )
                    throw new Exception("No se pudieron crear los detalles de la venta");
            }

        }
    
    }
    
    
    private void actualizarCantidadDisponible(Connection conexionDB,  ArrayList<ModeloDetalleVenta> detalles, boolean sumar) throws Exception{
        
        String operador = (sumar)? "+":"-";
        
        String sentenciaSQL = 
                        " UPDATE\n" +
                        "    productos\n" +
                        "SET\n" +
                        "    cantidad_disponible = cantidad_disponible " + operador + " ? \n" +
                        "WHERE\n" +
                        "    id_producto = ?";

        try(PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL)){

            for(ModeloDetalleVenta detalle: detalles){
                consulta.setInt( 1, detalle.getCantidadProducto() );
                consulta.setInt( 2, detalle.getIdProducto() );

                //Usamos addBatch() para generar un paquete que despues se enviara en lote
                consulta.addBatch();
            }

            // Ejecutar todo el lote de un solo golpe en la base de datos
            int[] resultados = consulta.executeBatch();

            for (int filasAfectadas: resultados ){
                if( filasAfectadas != 1 )
                    throw new Exception("No se pudieron actualizar todos los productos");
            }


        }
    }
  
    
    private void actualizarDatosGenerales(Connection conexionDB, ModeloVenta venta) throws Exception{
        String sentenciaSQL =
                "UPDATE\n" +
                "    ventas\n" +
                "SET\n" +
                "    fecha_hora_venta = ?,\n" +
                "    total_venta = ?,\n" +
                "    id_cliente = ?,\n" +
                "    id_usuario = ?\n" +
                "WHERE\n" +
                "    id_venta = ?";

        try( PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL)){

            consulta.setObject(1, venta.getFechaHoraVenta());
            consulta.setDouble(2, venta.getTotalVenta());
            consulta.setInt(3, venta.getIdCliente());
            consulta.setInt(4, venta.getIdUsuario());

            consulta.setInt(5, venta.getIdVenta());

            int filasAfectadas = consulta.executeUpdate();
            if( filasAfectadas != 1 )
                throw new Exception("No se pudo editar el registro base de la venta");

        } 
    
    }
        
    
    private void eliminarDetalles(Connection conexionDB, int idVenta, int numeroDetalles) throws Exception{
        
        String sentenciaSQL = "DELETE FROM detalle_de_ventas WHERE id_venta = ?";

        try(PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL)){
            consulta.setInt( 1, idVenta );
            int filasAfectadas = consulta.executeUpdate();
            if(filasAfectadas != numeroDetalles)
                throw new Exception("No se pudieron actualizar correctamente los detalles de la venta");
        }
    
    }
    
    
}

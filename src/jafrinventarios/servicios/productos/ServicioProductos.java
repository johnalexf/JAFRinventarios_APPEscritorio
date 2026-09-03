/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jafrinventarios.servicios.productos;

import jafrinventarios.DTOs.productos.DTOProductoTabla;
import jafrinventarios.modelos.productos.ModeloProducto;
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
public class ServicioProductos {

    
    public ServicioProductos() {
    }
    
    
    /*
    Metodo estatico para no instanciar el servicio para los controladores que 
    solo necesitan de esta funcion
    */ 
    public static LinkedHashMap<Integer, String> obtenerDiccionarioProductos(boolean soloHabilitados) throws Exception{
    
        LinkedHashMap<Integer, String> diccionarioProductos = new LinkedHashMap<>();
        
        Connection conexionDB = ConexionDB.getConnection();
        
        String sentenciaSQL = 
                "SELECT\n" +
                "    id_producto,\n" +
                "    nombre_producto\n" +
                "FROM\n" +
                "    productos\n";
        if(soloHabilitados)
            sentenciaSQL +=  "WHERE  habilitado = 1";
        
        try(
            PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL);
            ResultSet respuesta = consulta.executeQuery();
            ){
        
            while( respuesta.next() ){
                diccionarioProductos.put( respuesta.getInt("id_producto"), respuesta.getString("nombre_producto"));
            }
        }
        
        return diccionarioProductos;
        
    }
    
    
    public List<DTOProductoTabla> obtenerTodosLosProductos( boolean isAdministrador ) throws Exception{
    
        List<DTOProductoTabla> listaProductosTablas = new ArrayList<>();
        
        Connection conexionDB = ConexionDB.getConnection();
        
        String sentenciaSQL = 
                "SELECT\n" +
                "    pd.id_producto AS 'id',\n" +
                "    pd.nombre_producto AS 'nombreProducto',\n" +
                "    pd.precio_compra AS 'precioCompra',\n" +
                "    pd.precio_venta AS 'precioVenta',\n" +
                "    pd.cantidad_minima_stock AS 'cantidadMin',\n" +
                "    pd.cantidad_disponible AS 'cantidadDisponible',\n" +
                "    pd.habilitado AS 'habilitado',\n" +
                "    pv.nombre_comercial AS 'nombreProveedor'\n" +
                "FROM\n" +
                "    productos pd\n" +
                "INNER JOIN\n" +
                "    proveedores pv\n" +
                "ON\n" +
                "    pd.id_proveedor = pv.id_proveedor\n";
        
        if(!isAdministrador)
            sentenciaSQL +=  "WHERE  pd.habilitado = 1" ;
        
        try(
            PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL);
            ResultSet respuesta = consulta.executeQuery();
            ){

            while( respuesta.next() ){
                listaProductosTablas.add(
                        new DTOProductoTabla(
                                respuesta.getInt("id"), 
                                respuesta.getString("nombreProducto"), 
                                respuesta.getDouble("precioCompra"), 
                                respuesta.getDouble("precioVenta"), 
                                respuesta.getInt("cantidadMin"), 
                                respuesta.getInt("cantidadDisponible"), 
                                respuesta.getBoolean("habilitado"), 
                                respuesta.getString("nombreProveedor")
                        )
                );
            }
        }
        
        return listaProductosTablas;
    
    }
    
    
    public List<DTOProductoTabla> obtenerListaProductosPorFiltro ( String filtro , boolean isAdministrador ) throws Exception {
    
                List<DTOProductoTabla> listaProductosTablas = new ArrayList<>();
        
        Connection conexionDB = ConexionDB.getConnection();
        
        String sentenciaSQL = 
                "SELECT\n" +
                "    pd.id_producto AS 'id',\n" +
                "    pd.nombre_producto AS 'nombreProducto',\n" +
                "    pd.precio_compra AS 'precioCompra',\n" +
                "    pd.precio_venta AS 'precioVenta',\n" +
                "    pd.cantidad_minima_stock AS 'cantidadMin',\n" +
                "    pd.cantidad_disponible AS 'cantidadDisponible',\n" +
                "    pd.habilitado AS 'habilitado',\n" +
                "    pv.nombre_comercial AS 'nombreProveedor'\n" +
                "FROM\n" +
                "    productos pd\n" +
                "INNER JOIN\n" +
                "    proveedores pv\n" +
                "ON\n" +
                "    pd.id_proveedor = pv.id_proveedor\n" +
                "WHERE ";
        
        if(!isAdministrador)
            sentenciaSQL +=  " pd.habilitado = 1 AND" ;
        
        sentenciaSQL += 
                "    (\n" +
                "        pd.nombre_producto LIKE ? OR\n" +
                "        pv.nombre_comercial LIKE ?\n" +
                "    )";
        
        filtro = "%" + filtro + "%";
        
        try( PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL) ){
            
            consulta.setString(1, filtro);
            consulta.setString(2, filtro);
            
            try( ResultSet respuesta = consulta.executeQuery() ){
                while( respuesta.next() ){
                    listaProductosTablas.add(
                            new DTOProductoTabla(
                                    respuesta.getInt("id"), 
                                    respuesta.getString("nombreProducto"), 
                                    respuesta.getDouble("precioCompra"), 
                                    respuesta.getDouble("precioVenta"), 
                                    respuesta.getInt("cantidadMin"), 
                                    respuesta.getInt("cantidadDisponible"), 
                                    respuesta.getBoolean("habilitado"), 
                                    respuesta.getString("nombreProveedor")
                            )
                    );
                }
            }
            
        }
        
        return listaProductosTablas;
    
    }
    
    
    /*
    Metodo para obtener un solo DTOProductoTabla, sera utilizado en dado caso que
    se necesite obtener los datos de un producto en especifico cuando el usuario
    haya cambiado sus valores o haya creado uno nuevo
    */
    public DTOProductoTabla obtenerDatosDTOProducto ( int idProducto ) throws Exception{
    
        Connection conexionDB = ConexionDB.getConnection();
        
        String sentenciaSQL = 
                "SELECT\n" +
                "    pd.id_producto AS 'id',\n" +
                "    pd.nombre_producto AS 'nombreProducto',\n" +
                "    pd.precio_compra AS 'precioCompra',\n" +
                "    pd.precio_venta AS 'precioVenta',\n" +
                "    pd.cantidad_minima_stock AS 'cantidadMin',\n" +
                "    pd.cantidad_disponible AS 'cantidadDisponible',\n" +
                "    pd.habilitado AS 'habilitado',\n" +
                "    pv.nombre_comercial AS 'nombreProveedor'\n" +
                "FROM\n" +
                "    productos pd\n" +
                "INNER JOIN\n" +
                "    proveedores pv\n" +
                "ON\n" +
                "    pd.id_proveedor = pv.id_proveedor\n" +
                "WHERE\n" +
                "    pd.id_producto = ?";
        
        try(PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL)){
        
            consulta.setInt(1, idProducto);
            
            try(ResultSet respuesta = consulta.executeQuery()){
                if(respuesta.next()){
                    return new DTOProductoTabla(
                            respuesta.getInt("id"),
                            respuesta.getString("nombreProducto"),
                            respuesta.getDouble("precioCompra"),
                            respuesta.getDouble("precioVenta"),
                            respuesta.getInt("cantidadMin"),
                            respuesta.getInt("cantidadDisponible"),
                            respuesta.getBoolean("habilitado"),
                            respuesta.getString("nombreProveedor")
                    );
                }else{
                    throw new Exception("No existe un producto con id : " + idProducto);
                }
            }
        }
        
    }
    
    
    /*
    Metodo para entregar un ModeloProducto, destinado unicamente para poder editarlo
    segun la solucion para mostrar una lista usamos el DTO.
    */
    public ModeloProducto obtenerModeloProducto ( int idProducto ) throws Exception{
    
       Connection conexionDB = ConexionDB.getConnection();
       
       String sentenciaSQL = 
               "SELECT\n" +
                "    id_producto AS 'idProducto',\n" +
                "    id_proveedor AS 'idProveedor',\n" +
                "    nombre_producto AS 'nombreProducto',\n" +
                "    precio_compra AS 'precioCompra',\n" +
                "    precio_venta AS 'precioVenta',\n" +
                "    cantidad_minima_stock AS 'cantidadMin',\n" +
                "    cantidad_disponible AS 'cantidadDisponible',\n" +
                "    habilitado\n" +
                "FROM\n" +
                "    productos\n" +
                "WHERE\n" +
                "    id_producto = ?";
       
       try( PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL)){
       
           consulta.setInt(1, idProducto);
           
           try( ResultSet respuesta = consulta.executeQuery()){
               if(respuesta.next()){
                   return new ModeloProducto(
                           respuesta.getInt("idProducto"),
                           respuesta.getInt("idProveedor"),
                           respuesta.getString("nombreProducto"),
                           respuesta.getDouble("precioCompra"),
                           respuesta.getDouble("precioVenta"),
                           respuesta.getInt("cantidadMin"),
                           respuesta.getInt("cantidadDisponible"),
                           respuesta.getBoolean("habilitado")
                   );
               }else{
                   throw new Exception("No existe un producto con id : " + idProducto);
               }
           }
       
       }
        
    }
    
    
    private void validarDatosUnicoProducto ( ModeloProducto producto ) throws Exception{
    
        Connection conexionDB = ConexionDB.getConnection();

        String sentenciaSQL = 
                "SELECT\n" +
                "    EXISTS (\n" +
                "        SELECT 1 FROM productos WHERE (nombre_producto = ?) \n";
        
        if(producto.getIdProducto() != null)
                sentenciaSQL += "   AND id_producto != ?\n";
        
        sentenciaSQL +="    ) AS nombreDuplicado";
        
        try( PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL)){
            
            consulta.setString(1, producto.getNombreProducto());
            
            if(producto.getIdProducto() != null)
                consulta.setInt(2, producto.getIdProducto());
            
            try(ResultSet respuesta = consulta.executeQuery()){
                if(respuesta.next()){
                
                    if(respuesta.getBoolean("nombreDuplicado")){
                        HashMap<String, String> error = new HashMap<>();
                        error.put("nombreProducto", "Este nombre ya esta registrado");
                        throw new ExcepcionValidacionBD( error );
                    }
                }
            }
        }
    
    }
    
    
    public void editarProducto ( ModeloProducto producto, boolean isAdministrador ) throws Exception{
    
        if(!isAdministrador)
            throw new Exception("Solo el usuario administrador puede editar un producto");
        
        validarDatosUnicoProducto(producto);
        
        Connection conexionDB = ConexionDB.getConnection();
        
        String sentenciaSQL = 
                "UPDATE\n" +
                "    productos\n" +
                "SET\n" +
                "    id_proveedor = ?,\n" +
                "    nombre_producto = ?,\n" +
                "    precio_compra = ?,\n" +
                "    precio_venta = ?,\n" +
                "    cantidad_minima_stock = ?,\n" +
                "    cantidad_disponible = ?\n" +
                "WHERE\n" +
                "    id_producto = ?";
        
        try(PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL)){
        
            consulta.setInt(1, producto.getIdProveedor());
            consulta.setString(2, producto.getNombreProducto());
            consulta.setDouble(3, producto.getPrecioCompra());
            consulta.setDouble(4, producto.getPrecioVenta());
            consulta.setInt(5, producto.getCantidadMinimaStock());
            consulta.setInt(6, producto.getCantidadDisponible());
            
            consulta.setInt(7, producto.getIdProducto());
            
            int filasAfectadas = consulta.executeUpdate();
            
            if(filasAfectadas != 1){
                throw new Exception("El producto no se edito correctamente");
            }
        }
        
    }
    
    
    /*
    Metodo para conmutar el estado de habilitado de un producto
    */
    public void asignarEstadoProducto ( int idProducto, boolean habilitado, boolean isAdministrador ) throws Exception {
    
        if(!isAdministrador)
                throw new Exception("Solo el usuario administrador puede modificar el estado de un producto");
        
        Connection conexionDB = ConexionDB.getConnection();
        
        String sentenciaSQL = "UPDATE productos SET habilitado = ? WHERE id_producto = ?";
        
        try( PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL)){
            
            consulta.setBoolean(1, habilitado);
            consulta.setInt(2, idProducto);
            
            int filasAfectadas = consulta.executeUpdate();
            
            if( filasAfectadas != 1 )
                throw new Exception("No se pudo modificar el estado del producto");
            
        }
    }
    
    
    /*
    Metodo para crear un producto, 
    se ignora el id pues este valor se lo asigna la base de datos
    el cual hay que retornar.
    */
    public int crearProducto ( ModeloProducto producto, boolean isAdministrador ) throws Exception{
    
        if(!isAdministrador)
            throw new Exception("Solo el usuario administrador puede crear un producto");
       
        validarDatosUnicoProducto(producto);
        
        Connection conexionDB = ConexionDB.getConnection();
        
        String sentenciaSQL = 
                "INSERT INTO\n" +
                "    productos(\n" +
                "        id_proveedor,\n" +
                "        nombre_producto,\n" +
                "        precio_compra,\n" +
                "        precio_venta,\n" +
                "        cantidad_minima_stock,\n" +
                "        cantidad_disponible,\n" +
                "        habilitado\n" +
                "    )\n" +
                "VALUES\n" +
                "    ( ? , ? , ? , ? , ? , ? , ? )";
        
        try(PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL, Statement.RETURN_GENERATED_KEYS)){
        
            consulta.setInt(1, producto.getIdProveedor());
            consulta.setString(2, producto.getNombreProducto());
            consulta.setDouble(3, producto.getPrecioCompra());
            consulta.setDouble(4, producto.getPrecioVenta());
            consulta.setInt(5, producto.getCantidadMinimaStock());
            consulta.setInt(6, producto.getCantidadDisponible());
            consulta.setBoolean(7, true);
            
            int filasAfectadas = consulta.executeUpdate();
            
            if(filasAfectadas == 1){
               try(ResultSet respuesta = consulta.getGeneratedKeys()){
                   if( respuesta.next() ){
                        //Retornamos el id del cliente creado
                        return ( respuesta.getInt( 1 ) );
                    }else{
                        throw new Exception( "Error al obtener el id del producto" );
                    }
               }
            }else
                throw new Exception("El producto no se creo correctamente");
        }
        
    }
    
    
    /*
    Metodo para eliminar un producto.
    Pero si este tiene alguna relacion con otra tabla no se puede eliminar
    */
    public void eliminarProducto ( int idProducto , boolean isAdministrador ) throws Exception{
        
        if(!isAdministrador)
            throw new Exception("Solo el usuario administrador puede eliminar un producto");
       
        if(!isProductoEliminable(idProducto))
            throw new Exception("Este producto no se puede eliminar");
        
        Connection conexionDB = ConexionDB.getConnection();
        
        String sentenciaSQL = "DELETE FROM productos WHERE id_producto = ?";
        
        try( PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL) ){
            
            consulta.setInt(1, idProducto);
            
            int filasAfectadas = consulta.executeUpdate();
            
            if( filasAfectadas != 1 )
                throw new Exception("No se pudo eliminar el cliente");
            
        }
    }
    
    
    /*
    Metodo para consultar si un producto hace parte del registro de otra tabla
    */
    public boolean isProductoEliminable ( int idProducto ) throws Exception{
    
        Connection conexionDB = ConexionDB.getConnection();
        String sentenciaSQL = 
            "SELECT(\n" +
            "    EXISTS( SELECT 1 FROM detalle_de_ventas WHERE id_producto = ?) OR\n" +
            "    EXISTS( SELECT 1 FROM detalle_de_compras WHERE id_producto = ?)\n" +
            ") AS tieneRegistros";
            
        try (PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL)) {
            consulta.setInt(1, idProducto);
            consulta.setInt(2, idProducto);
            try (ResultSet respuesta = consulta.executeQuery()) {
                if (respuesta.next()) {
                    boolean tieneRegistros = respuesta.getBoolean("tieneRegistros");
                    return !tieneRegistros; // Si tiene registros no es eliminable
                }else{
                    //En dado caso que no se reciba una respuesta, para proteger
                    //la integridad de los datos, retornamos un false, para que no se 
                    //pueda eliminar
                    return false;
                }
            }
        }
        
    }
    
}

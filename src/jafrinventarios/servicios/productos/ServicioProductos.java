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
import java.util.ArrayList;
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
    public static LinkedHashMap<Integer, String> obtenerDiccionarioProductos() throws Exception{
    
        LinkedHashMap<Integer, String> diccionarioProductos = new LinkedHashMap<>();
        
        Connection conexionDB = ConexionDB.getConnection();
        
        String sentenciaSQL = 
                "SELECT\n" +
                "    id_producto,\n" +
                "    nombre_producto\n" +
                "FROM\n" +
                "    productos\n" +
                "WHERE\n" +
                "    habilitado = 1;";
        
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
    
        /*
        TODO
        Si no se encuentra un usuario con el id, devolver el error con throw new
        de igual manera si pasa algun error en la conexion
        Por el momento se simula un resultado
        */
        
        DTOProductoTabla productoConsultado = new DTOProductoTabla(10, "Galletas de Mantequilla (Caja)", 3500.0, 5000.0, 15, 2, false, "Pastelería Delicias");
        
        return productoConsultado;
        
    }
    
    
    /*
    Metodo para entregar un ModeloProducto, destinado unicamente para poder editarlo
    segun la solucion para mostrar una lista usamos el DTO.
    */
    public ModeloProducto obtenerModeloProducto ( int idProducto ) throws Exception{
    
        /*
        TODO
        hacer la consulta, si no se encuentra devolver un error
        si pasa algun error de conexion se devuelve un error diferente
        Por el momento se hace una simulacion de resultado
        */
        
        ModeloProducto productoConsultado = 
                new ModeloProducto(
                    10, 1, "Galletas de Mantequilla (Caja)", 3500.0, 5000.0, 15, 2, true
                );
        
        return productoConsultado;
        
    }
    
    
    
    public void editarProducto ( ModeloProducto producto, boolean isAdministrador ) throws Exception{
    
        /*
        TODO
        La seccion productos se le puede mostrar a cualquier usuario
        pero solo el administrador puede crear y editar usuario
        Por seguridad para blindar en dado caso que el boton de crear y editar
        se le muestren a todos los usuarios, se solicita en los argumentos
        si es un usuario administrado, si no se devuelve 
        un error : Permisos denegados para este usuario, solo el adminsitrador puede editar
        
        Para editar se tendra encuenta el id para saber que producto es el que hay que modificar
        y se actualizaran los demas datos que contenga el producto
        
        Manejar try catch para controlar errores de respuesta de la base de datos con
        catch (SQLIntegrityConstraintViolationException e) {
            
            String errorBD = e.getMessage();
            HashMap<String, String> errores = new HashMap<>();

            // Buscar palabras clave en el error de la base de datos
            if (errorBD.contains("nombreProducto_UNIQUE")) {
                errores.put("nombreProducto", "Ya está registrado este nombre.");
            } 
            
            si es que llega un valor negativo en las cantidades, verificar, de
            igual manera se va configurar tanto la base de datos como la vista
            para que no permita capturar numeros negativos

            // Lanzar la excepción personalizada con el mapa listo para la vista
            throw new ExcepcionValidacionBD(errores);
        }
        */
        
        
    }
    
    
    /*
    Metodo para conmutar el estado de habilitado de un producto
    */
    public void conmutarEstadoProducto( int idProducto, boolean isAdministrador ) throws Exception {
    
    
        /*
        TODO
        con el idProducto verificamos que valor tiene el parametro habilitado
        y lo conmutamos
        
        Manejar los errores 
        */
    }
    
    
    /*
    Metodo para consultar si un producto esta habilitado
    */
    public boolean isProductoHabilitado ( int idProducto ) throws Exception{
    
        /*
        TODO : pendiente la respectiva consulta con manejo de errores
        por el momento se simula con true;
        */
        
        return true;
    }
    
    
    /*
    Metodo para crear un producto, 
    se ignora el id pues este valor se lo asigna la base de datos
    el cual hay que retornar.
    */
    public int crearProducto ( ModeloProducto producto, boolean isAdministrador ) throws Exception{
    
        /*
        TODO:
        Se tiene en cuenta si es administrador para permitirle ejecutar la accion
        
        Manejo de errores con 
        catch (SQLIntegrityConstraintViolationException e) {
        si hay datos duplicados en la base de datos o que no correspondan
        
        y con catch (Exception e) { para otros errores
        
        por el momento retornamos -1 para indicar que no se creo el usuario
        sin embargo la idea es manejarlo con los errores throw new
        */
        return -1;
    }
    
    
    /*
    Metodo para eliminar un producto.
    Pero si este tiene alguna relacion con otra tabla no se puede eliminar
    */
    public void eliminarProducto ( int idProducto , boolean isAdministrador ) throws Exception{
        
        /*
        TODO:
        Hacer la respectiva consulta y envio de errores segun el caso.
        */
    }
    
    
    /*
    Metodo para consultar si un producto hace parte del registro de otra tabla
    */
    public boolean isProductoEliminable ( int idProducto ) throws Exception{
    
        /*
        TODO
        Hacer la consulta y manejo de errores para enviarlos personalizados
        segun corresponda
        
        Por el momento se simula que no tiene registros asociados
        */
        
        return false;
    }
    
}

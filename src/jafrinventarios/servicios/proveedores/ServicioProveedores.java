/*
 Este servicio es solo para un administrador, por tanto la vista 
nunca le mostrara los proveedores, es por esto que en ninguno de los metodos
se considera necesario el uso de la variable booleana como parametro
isAdministrador
 */
package jafrinventarios.servicios.proveedores;

import jafrinventarios.DTOs.proveedores.DTOProveedorTabla;
import jafrinventarios.modelos.proveedores.ModeloProveedor;
import jafrinventarios.servicios.ConexionDB;
import jafrinventarios.servicios.excepciones.ExcepcionValidacionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author JOHN FORERO
 */
public class ServicioProveedores {

    
    public ServicioProveedores() {
    }
    
    /*
    Metodo estatico para no instanciar el servicio para los controladores que 
    solo necesitan de esta funcion
    */
    public static LinkedHashMap<Integer, String> obtenerDiccionarioProveedores() throws Exception{
    
        LinkedHashMap<Integer, String> diccionarioProveedores = new LinkedHashMap<>();
        
        Connection conexionDB = ConexionDB.getConnection();
        
        String sentenciaSQL =
                "SELECT\n" +
                "    id_proveedor AS 'id',\n" +
                "    nombre_comercial AS 'nombre'\n" +
                "FROM\n" +
                "    proveedores\n" +
                "WHERE\n" +
                "    habilitado = 1;";
        
        try(
            PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL);
            ResultSet respuesta = consulta.executeQuery();
            ){
        
            while( respuesta.next() ){
                diccionarioProveedores.put( respuesta.getInt("id"), respuesta.getString("nombre"));
            }
        }
        
        return diccionarioProveedores;
        
    }
    
    public List<DTOProveedorTabla> obtenerTodosLosProveedores( ) throws Exception{
    
        List<DTOProveedorTabla> listaProveedores = new ArrayList<>();
        
        Connection conexionDB = ConexionDB.getConnection();
        
        String sentenciaSQL = 
                "SELECT\n" +
                "    id_proveedor AS 'id',\n" +
                "    nombre_comercial AS 'nombreComercial',\n" +
                "    CONCAT(\n" +
                "        primer_nombre_contacto, ' ',\n" +
                "        segundo_nombre_contacto, ' ',\n" +
                "        primer_apellido_contacto,  ' ',\n" +
                "        segundo_apellido_contacto\n" +
                "    ) AS 'nombreContacto',\n" +
                "    telefono_contacto AS 'telefono',\n" +
                "    direccion_proveedor AS 'direccion',\n" +
                "    correo_proveedor AS 'correo',\n" +
                "    habilitado\n" +
                "FROM\n" +
                "    proveedores";
        
        try(
            PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL);
            ResultSet respuesta = consulta.executeQuery();
            ){

            while( respuesta.next() ){
                listaProveedores.add(
                        new DTOProveedorTabla(
                                respuesta.getInt("id"), 
                                respuesta.getString("nombreComercial"), 
                                respuesta.getString("nombreContacto"), 
                                respuesta.getString("telefono"), 
                                respuesta.getString("direccion"), 
                                respuesta.getString("correo"), 
                                respuesta.getBoolean("habilitado")
                        )
                );
            }
        }
        
        return listaProveedores;
    
    }
    
    
    public List<DTOProveedorTabla> obtenerListaProveedoresPorFiltro ( String filtro ) throws Exception {
    
        List<DTOProveedorTabla> listaProveedores = new ArrayList<>();
        
        Connection conexionDB = ConexionDB.getConnection();
        
        String sentenciaSQL = 
                "SELECT\n" +
                "    id_proveedor AS 'id',\n" +
                "    nombre_comercial AS 'nombreComercial',\n" +
                "    CONCAT(\n" +
                "        primer_nombre_contacto, ' ',\n" +
                "        segundo_nombre_contacto, ' ',\n" +
                "        primer_apellido_contacto,  ' ',\n" +
                "        segundo_apellido_contacto\n" +
                "    ) AS 'nombreContacto',\n" +
                "    telefono_contacto AS 'telefono',\n" +
                "    direccion_proveedor AS 'direccion',\n" +
                "    correo_proveedor AS 'correo',\n" +
                "    habilitado\n" +
                "FROM\n" +
                "    proveedores\n"+
                "WHERE\n" +
                "    (\n" +
                "        nombre_comercial LIKE ? OR\n" +
                "        CONCAT(\n" +
                "        primer_nombre_contacto, ' ',\n" +
                "        segundo_nombre_contacto, ' ',\n" +
                "        primer_apellido_contacto,  ' ',\n" +
                "        segundo_apellido_contacto\n" +
                "        ) LIKE ? OR\n" +
                "        telefono_contacto LIKE ? OR\n" +
                "        direccion_proveedor LIKE ? OR\n" +
                "        correo_proveedor LIKE ? \n" +
                "    )";
        
        filtro = "%" + filtro + "%";
        
        try( PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL) ){
            
            consulta.setString(1, filtro);
            consulta.setString(2, filtro);
            consulta.setString(3, filtro);
            consulta.setString(4, filtro);
            consulta.setString(5, filtro);
            
            try( ResultSet respuesta = consulta.executeQuery() ){
                while( respuesta.next() ){
                    listaProveedores.add(
                            new DTOProveedorTabla(
                                    respuesta.getInt("id"), 
                                    respuesta.getString("nombreComercial"), 
                                    respuesta.getString("nombreContacto"), 
                                    respuesta.getString("telefono"), 
                                    respuesta.getString("direccion"), 
                                    respuesta.getString("correo"), 
                                    respuesta.getBoolean("habilitado")
                            )
                    );
                }
            }
        }
        
        return listaProveedores;
    }
    
    
    /*
    Metodo para obtener un solo DTOProveedorTabla, sera utilizado en dado caso que
    se necesite obtener los datos de un proveedor en especifico cuando el usuario
    haya cambiado sus valores o haya creado uno nuevo
    */
    public DTOProveedorTabla obtenerDatosDTOProveedor ( int idProveedor ) throws Exception{
    
        Connection conexionDB = ConexionDB.getConnection();
        
        String sentenciaSQL = 
                "SELECT\n" +
                "    id_proveedor AS 'id',\n" +
                "    nombre_comercial AS 'nombreComercial',\n" +
                "    CONCAT(\n" +
                "        primer_nombre_contacto, ' ',\n" +
                "        segundo_nombre_contacto, ' ',\n" +
                "        primer_apellido_contacto,  ' ',\n" +
                "        segundo_apellido_contacto\n" +
                "    ) AS 'nombreContacto',\n" +
                "    telefono_contacto AS 'telefono',\n" +
                "    direccion_proveedor AS 'direccion',\n" +
                "    correo_proveedor AS 'correo',\n" +
                "    habilitado\n" +
                "FROM\n" +
                "    proveedores\n"+
                "WHERE\n" +
                "    id_proveedor = ?";
        
        try( PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL) ){
            
            consulta.setInt(1, idProveedor);
            
            try( ResultSet respuesta = consulta.executeQuery() ){
                if( respuesta.next() ){
                    return new DTOProveedorTabla(
                                    respuesta.getInt("id"), 
                                    respuesta.getString("nombreComercial"), 
                                    respuesta.getString("nombreContacto"), 
                                    respuesta.getString("telefono"), 
                                    respuesta.getString("direccion"), 
                                    respuesta.getString("correo"), 
                                    respuesta.getBoolean("habilitado")
                            );
                }else{
                    throw new Exception("No existe un proveedor con id : " + idProveedor );
                }
            }
        }
        
    }
    
    
    /*
    Metodo para entregar un ModeloProveedor, destinado unicamente para poder editarlo
    segun la solucion para mostrar una lista usamos el DTO y no el modelo.
    */
    public ModeloProveedor obtenerModeloProveedor ( int idProveedor ) throws Exception{
    
        Connection conexionDB = ConexionDB.getConnection();
        
        String sentenciaSQL = 
                "SELECT\n" +
                "    id_proveedor AS 'id',\n" +
                "    nombre_comercial AS 'nombreComercial',\n" +
                "    primer_nombre_contacto AS 'primerNombre',\n" +
                "    segundo_nombre_contacto AS 'segundoNombre',\n" +
                "    primer_apellido_contacto AS 'primerApellido',\n" +
                "    segundo_apellido_contacto AS 'segundoApellido',\n" +
                "    telefono_contacto AS 'telefono',\n" +
                "    direccion_proveedor AS 'direccion',\n" +
                "    correo_proveedor AS 'correo',\n" +
                "    habilitado\n" +
                "FROM\n" +
                "    proveedores\n" +
                "WHERE\n" +
                "    id_proveedor = ?";
           
        try( PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL) ){
            
            consulta.setInt(1, idProveedor);
            
            try( ResultSet respuesta = consulta.executeQuery() ){
                if( respuesta.next() ){
                    return new ModeloProveedor(
                                    respuesta.getInt("id"), 
                                    respuesta.getString("nombreComercial"), 
                                    respuesta.getString("primerNombre"), 
                                    respuesta.getString("segundoNombre"), 
                                    respuesta.getString("primerApellido"), 
                                    respuesta.getString("segundoApellido"), 
                                    respuesta.getString("telefono"), 
                                    respuesta.getString("direccion"), 
                                    respuesta.getString("correo"), 
                                    respuesta.getBoolean("habilitado")
                            );
                }else{
                    throw new Exception("No existe un proveedor con id : " + idProveedor );
                }
            }
        }
        
    }
    
    
    private void validarDatosUnicosProveedor( ModeloProveedor proveedor ) throws Exception{
         
        Connection conexionBD = ConexionDB.getConnection();
        
        String sentenciaSQL = 
            "SELECT\n" +
            "    nombre_comercial , telefono_contacto, correo_proveedor\n" +
            "FROM\n" +
            "    proveedores\n" +
            "WHERE\n" +
            "    (nombre_comercial = ? OR telefono_contacto = ? OR correo_proveedor = ?)";
        
        //Si es un usuario nuevo se espera que no este asignado el ID
        if( proveedor.getIdProveedor() != null )
            sentenciaSQL += " AND id_proveedor != ?";
        
        try (PreparedStatement consulta = conexionBD.prepareStatement( sentenciaSQL ) ) {
              
            consulta.setString(1, proveedor.getNombreComercial());
            consulta.setString(2, proveedor.getTelefonoContacto());
            consulta.setString(3, proveedor.getCorreoProveedor());
            
            if( proveedor.getIdProveedor() != null )
                consulta.setInt(4, proveedor.getIdProveedor());
            
            try( ResultSet respuesta = consulta.executeQuery() ){
                
                HashMap<String, String> erroresBD = new HashMap<>();
                
                while(respuesta.next()){
                    if(respuesta.getString( "nombre_comercial").equals(proveedor.getNombreComercial()))
                       erroresBD.put("nombreComercial", "Este nombre ya esta registrado");
                    if(respuesta.getString("telefono_contacto").equals(proveedor.getTelefonoContacto()))
                        erroresBD.put("telefonoContacto", "Este telefono ya esta registrado");
                    if(respuesta.getString("correo_proveedor").equals(proveedor.getCorreoProveedor()))
                        erroresBD.put("correoProveedor", "Este correo ya esta registrado");
                }
                
                if(!erroresBD.isEmpty())
                    throw new ExcepcionValidacionBD(erroresBD);
            }
        } 
    }
    
    
    public void editarProveedor ( ModeloProveedor proveedor ) throws Exception{
    
        /*
        TODO
        
        Para editar se tendra encuenta el id para saber que proveedor es el que hay que modificar
        y se actualizaran los demas datos que contenga el proveedor
        
        Manejar try catch para controlar errores de respuesta de la base de datos con
        catch (SQLIntegrityConstraintViolationException e) {
            
            String errorBD = e.getMessage();
            HashMap<String, String> errores = new HashMap<>();

            // Buscar palabras clave en el error de la base de datos
            if (errorBD.contains("nombreComercial_UNIQUE")) {
                errores.put("nombreComercial", "Ya está registrado este nombre.");
            } 

            // Lanzar la excepción personalizada con el mapa listo para la vista
            throw new ExcepcionValidacionBD(errores);
        }
        */
        
        
    }
    
    
    /*
    Metodo para conmutar el estado de habilitado de un proveedor
    */
    public void conmutarEstadoProveedor( int idProveedor ) throws Exception {
    
    
        /*
        TODO
        con el idProveedor verificamos que valor tiene el parametro habilitado
        y lo conmutamos
        
        Manejar los errores 
        */
    }
    
    
    /*
    Metodo para consultar si un proveedor esta habilitado
    */
    public boolean isProveedorHabilitado ( int idProveedor ) throws Exception{
    
        /*
        TODO : pendiente la respectiva consulta con manejo de errores
        por el momento se simula con true;
        */
        
        return true;
    }
    
    
    /*
    Metodo para crear un proveedor, 
    se ignora el id pues este valor se lo asigna la base de datos
    el cual hay que retornar.
    */
    public int crearProveedor ( ModeloProveedor proveedor ) throws Exception{
    
        /*
        TODO:

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
    Metodo para eliminar un proveedor.
    Pero si este tiene alguna relacion con otra tabla no se puede eliminar
    */
    public void eliminarProveedor ( int idProveedor ) throws Exception{
        
        /*
        TODO:
        Hacer la respectiva consulta y envio de errores segun el caso.
        */
    }
    
    
    /*
    Metodo para consultar si un proveedor hace parte del registro de otra tabla
    */
    public boolean isProveedorEliminable ( int idProveedor ) throws Exception{
    
        /*
        TODO
        Hacer la consulta y manejo de errores para enviarlos personalizados
        segun corresponda
        
        Por el momento se simula que no tiene registros asociados
        */
        
        return false;
    }
    
}

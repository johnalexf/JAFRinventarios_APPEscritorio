/*
La seccion clientes la puede ver cualquier usuarios,
sin embargo solo los administradores pueden hacer todas las operaciones CRUD,
mientras que los demas solo pueden leer los que estan habilitados,
por consiguiente para personalizar la consulta y mantener unos permisos
se usa una variable isAdministrador que debe enviar el controlador
 */
package jafrinventarios.servicios.clientes;

import jafrinventarios.DTOs.clientes.DTOClienteTabla;
import jafrinventarios.modelos.clientes.ModeloCliente;
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
public class ServicioClientes {

    
    public ServicioClientes() {
    }
    
    /*
    Metodo estatico para no instanciar el servicio para los controladores que 
    solo necesitan de esta funcion
    */
    public static LinkedHashMap<Integer, String> obtenerDiccionarioClientes() throws Exception{
    
        LinkedHashMap<Integer, String> diccionarioClientes = new LinkedHashMap<>();
        
        Connection conexionDB = ConexionDB.getConnection();
        
        String sentenciaSQL =
                "SELECT\n" +
                "    id_cliente AS 'id',\n" +
                "    nombre_negocio AS 'nombre'\n" +
                "FROM\n" +
                "    clientes\n" +
                "WHERE\n" +
                "    habilitado = 1";
        
        try(
            PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL);
            ResultSet respuesta = consulta.executeQuery();
            ){
        
            while( respuesta.next() ){
                diccionarioClientes.put( respuesta.getInt("id"), respuesta.getString("nombre"));
            }
        }
        
        return diccionarioClientes;
    }
    

    public List<DTOClienteTabla> obtenerTodosLosClientes( boolean isAdministrador ) throws Exception{
    
        List<DTOClienteTabla> listaClientes = new ArrayList<>();
        
        Connection conexionDB = ConexionDB.getConnection();
        
        String sentenciaSQL = 
                "SELECT\n" +
                "    id_cliente AS 'id',\n" +
                "    nombre_negocio AS 'nombreNegocio',\n" +
                "    CONCAT(\n" +
                "        primer_nombre_contacto, ' ',\n" +
                "        segundo_nombre_contacto, ' ',\n" +
                "        primer_apellido_contacto,  ' ',\n" +
                "        segundo_apellido_contacto\n" +
                "    ) AS 'nombreContacto',\n" +
                "    telefono_contacto AS 'telefono',\n" +
                "    direccion_cliente AS 'direccion',\n" +
                "    correo_cliente AS 'correo',\n" +
                "    habilitado\n" +
                "FROM\n" +
                "    clientes";
        
        if(!isAdministrador)
            sentenciaSQL +=  "\n WHERE habilitado = 1" ;
        
        try(
            PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL);
            ResultSet respuesta = consulta.executeQuery();
            ){

            while( respuesta.next() ){
                listaClientes.add(
                        new DTOClienteTabla(
                                respuesta.getInt("id"), 
                                respuesta.getString("nombreNegocio"), 
                                respuesta.getString("nombreContacto"), 
                                respuesta.getString("telefono"), 
                                respuesta.getString("direccion"), 
                                respuesta.getString("correo"), 
                                respuesta.getBoolean("habilitado")
                        )
                );
            }
        }
        
        return listaClientes;
    
    }
    
    
    public List<DTOClienteTabla> obtenerListaClientesPorFiltro ( String filtro , boolean isAdministrador ) throws Exception {
    
        List<DTOClienteTabla> listaClientes = new ArrayList<>();
        
        Connection conexionDB = ConexionDB.getConnection();
        
        String sentenciaSQL = 
                "SELECT\n" +
                "    id_cliente AS 'id',\n" +
                "    nombre_negocio AS 'nombreNegocio',\n" +
                "    CONCAT(\n" +
                "        primer_nombre_contacto, ' ',\n" +
                "        segundo_nombre_contacto, ' ',\n" +
                "        primer_apellido_contacto,  ' ',\n" +
                "        segundo_apellido_contacto\n" +
                "    ) AS 'nombreContacto',\n" +
                "    telefono_contacto AS 'telefono',\n" +
                "    direccion_cliente AS 'direccion',\n" +
                "    correo_cliente AS 'correo',\n" +
                "    habilitado\n" +
                "FROM\n" +
                "    clientes\n" +
                "WHERE\n" +
                "    (\n" +
                "        nombre_negocio LIKE ? OR\n" +
                "        CONCAT(\n" +
                "        primer_nombre_contacto, ' ',\n" +
                "        segundo_nombre_contacto, ' ',\n" +
                "        primer_apellido_contacto,  ' ',\n" +
                "        segundo_apellido_contacto\n" +
                "        ) LIKE ? OR\n" +
                "        telefono_contacto LIKE ? OR\n" +
                "        direccion_cliente LIKE ? OR\n" +
                "        correo_cliente LIKE ? \n" +
                "    )\n";
        
        if(!isAdministrador)
            sentenciaSQL +=  "AND habilitado = 1 " ;
        
        try( PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL) ){
            
            consulta.setString(1, filtro);
            consulta.setString(2, filtro);
            consulta.setString(3, filtro);
            consulta.setString(4, filtro);
            consulta.setString(5, filtro);
            
            try( ResultSet respuesta = consulta.executeQuery() ){
                while( respuesta.next() ){
                    listaClientes.add(
                            new DTOClienteTabla(
                                    respuesta.getInt("id"), 
                                    respuesta.getString("nombreNegocio"), 
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
        
        return listaClientes;
    }
    
    
    /*
    Metodo para obtener un solo DTOClienteTabla, sera utilizado en dado caso que
    se necesite obtener los datos de un cliente en especifico cuando el usuario
    haya cambiado sus valores o haya creado uno nuevo
    */
    public DTOClienteTabla obtenerDatosDTOCliente ( int idCliente ) throws Exception{
    
        /*
        TODO
        Si no se encuentra un usuario con el id, devolver el error con throw new
        de igual manera si pasa algun error en la conexion
        Por el momento se simula un resultado
        */
        
        DTOClienteTabla clienteConsultado = new DTOClienteTabla(10, "Cafetería Central", "Diana Patricia Ortiz", "3169990000", "Calle 100 # 15-20", "centralcafeteria@empresa.com", true);
        
        return clienteConsultado;
        
    }
    
    
    /*
    Metodo para entregar un ModeloCliente, destinado unicamente para poder editarlo
    segun la solucion para mostrar una lista usamos el DTO y no el modelo.
    */
    public ModeloCliente obtenerModeloCliente ( int idCliente ) throws Exception{
    
        /*
        TODO
        hacer la consulta, si no se encuentra devolver un error
        si pasa algun error de conexion se devuelve un error diferente
        Por el momento se hace una simulacion de resultado
        */
        
        ModeloCliente clienteConsultado = 
                new ModeloCliente(
                   10, "Cafetería Central", "Diana", "Patricia", "Ortiz", "", "3169990000", "Calle 100 # 15-20", "centralcafeteria@empresa.com", true);
        
        return clienteConsultado;
        
    }
    
    
    
    public void editarCliente ( ModeloCliente cliente , boolean isAdministrador ) throws Exception{
    
        /*
        TODO
        
        Para editar se tendra encuenta el id para saber que cliente es el que hay que modificar
        y se actualizaran los demas datos que contenga el cliente
        
        Manejar try catch para controlar errores de respuesta de la base de datos con
        catch (SQLIntegrityConstraintViolationException e) {
            
            String errorBD = e.getMessage();
            HashMap<String, String> errores = new HashMap<>();

            // Buscar palabras clave en el error de la base de datos
            if (errorBD.contains("nombreNegocio_UNIQUE")) {
                errores.put("nombreNegocio", "Ya está registrado este nombre.");
            } 

            // Lanzar la excepción personalizada con el mapa listo para la vista
            throw new ExcepcionValidacionBD(errores);
        }
        */
        
        
    }
    
    
    /*
    Metodo para conmutar el estado de habilitado de un cliente
    */
    public void conmutarEstadoCliente( int idCliente, boolean isAdministrador ) throws Exception {
    
    
        /*
        TODO
        con el idCliente verificamos que valor tiene el parametro habilitado
        y lo conmutamos
        
        Manejar los errores 
        */
    }
    
    
    /*
    Metodo para consultar si un cliente esta habilitado
    */
    public boolean isClienteHabilitado ( int idCliente ) throws Exception{
    
        /*
        TODO : pendiente la respectiva consulta con manejo de errores
        por el momento se simula con true;
        */
        
        return true;
    }
    
    
    /*
    Metodo para crear un cliente, 
    se ignora el id pues este valor se lo asigna la base de datos
    el cual hay que retornar.
    */
    public int crearCliente ( ModeloCliente cliente , boolean isAdministrador) throws Exception{
    
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
    Metodo para eliminar un cliente.
    Pero si este tiene alguna relacion con otra tabla no se puede eliminar
    */
    public void eliminarCliente ( int idCliente, boolean isAdministrador ) throws Exception{
        
        /*
        TODO:
        Hacer la respectiva consulta y envio de errores segun el caso.
        */
    }
    
    
    /*
    Metodo para consultar si un cliente hace parte del registro de otra tabla
    */
    public boolean isClienteEliminable ( int idCliente ) throws Exception{
    
        /*
        TODO
        Hacer la consulta y manejo de errores para enviarlos personalizados
        segun corresponda
        
        Por el momento se simula que no tiene registros asociados
        */
        
        return false;
    }
}

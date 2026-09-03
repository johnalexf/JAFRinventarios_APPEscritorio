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
                "    id_cliente = ?";
                ;
        
        try( PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL) ){
            
            consulta.setInt(1, idCliente);
            
            try( ResultSet respuesta = consulta.executeQuery() ){
                if( respuesta.next() ){
                    return new DTOClienteTabla(
                                    respuesta.getInt("id"), 
                                    respuesta.getString("nombreNegocio"), 
                                    respuesta.getString("nombreContacto"), 
                                    respuesta.getString("telefono"), 
                                    respuesta.getString("direccion"), 
                                    respuesta.getString("correo"), 
                                    respuesta.getBoolean("habilitado")
                            );
                }else{
                    throw new Exception("No existe un cliente con id : " + idCliente );
                }
            }
        }
        
    }
    
    
    /*
    Metodo para entregar un ModeloCliente, destinado unicamente para poder editarlo
    segun la solucion para mostrar una lista usamos el DTO y no el modelo.
    */
    public ModeloCliente obtenerModeloCliente ( int idCliente ) throws Exception{
    
        Connection conexionDB = ConexionDB.getConnection();
        
        String sentenciaSQL = 
                "SELECT\n" +
                "    id_cliente AS 'id',\n" +
                "    nombre_negocio AS 'nombreNegocio',\n" +
                "    primer_nombre_contacto AS 'primerNombre',\n" +
                "    segundo_nombre_contacto AS 'segundoNombre',\n" +
                "    primer_apellido_contacto AS 'primerApellido',\n" +
                "    segundo_apellido_contacto AS 'segundoApellido',\n" +
                "    telefono_contacto AS 'telefono',\n" +
                "    direccion_cliente AS 'direccion',\n" +
                "    correo_cliente AS 'correo',\n" +
                "    habilitado\n" +
                "FROM\n" +
                "    clientes\n" +
                "WHERE\n" +
                "    id_cliente = ?";
           
        try( PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL) ){
            
            consulta.setInt(1, idCliente);
            
            try( ResultSet respuesta = consulta.executeQuery() ){
                if( respuesta.next() ){
                    return new ModeloCliente(
                                    respuesta.getInt("id"), 
                                    respuesta.getString("nombreNegocio"), 
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
                    throw new Exception("No existe un cliente con id : " + idCliente );
                }
            }
        }
        
    }
    
    
    private void validarDatosUnicosCliente( ModeloCliente cliente ) throws Exception{
         
        Connection conexionBD = ConexionDB.getConnection();
        
        String sentenciaSQL = 
            "SELECT\n" +
            "    nombre_negocio , telefono_contacto, correo_cliente\n" +
            "FROM\n" +
            "    clientes\n" +
            "WHERE\n" +
            "    (nombre_negocio = ? OR telefono_contacto = ? OR correo_cliente = ?)\n" ;
        
        //Si es un cliente nuevo se espera que no este asignado el ID
        if( cliente.getIdCliente() != null )
            sentenciaSQL += "    AND id_cliente != ?";
        
        try (PreparedStatement consulta = conexionBD.prepareStatement( sentenciaSQL ) ) {
              
            consulta.setString(1, cliente.getNombreNegocio());
            consulta.setString(2, cliente.getTelefonoContacto());
            consulta.setString(3, cliente.getCorreoCliente());
            
            if( cliente.getIdCliente() != null )
                consulta.setInt(4, cliente.getIdCliente());
            
            try( ResultSet respuesta = consulta.executeQuery() ){
                
                HashMap<String, String> erroresBD = new HashMap<>();
                
                while(respuesta.next()){
                    if(respuesta.getString( "nombre_negocio").toLowerCase().equals(cliente.getNombreNegocio().toLowerCase()))
                       erroresBD.put("nombreNegocio", "Este nombre ya esta registrado");
                    if(respuesta.getString("telefono_contacto").equals(cliente.getTelefonoContacto()))
                        erroresBD.put("telefonoContacto", "Este telefono ya esta registrado");
                    if(respuesta.getString("correo_cliente").toLowerCase().equals(cliente.getCorreoCliente().toLowerCase()))
                        erroresBD.put("correoCliente", "Este correo ya esta registrado");
                }
                
                if(!erroresBD.isEmpty())
                    throw new ExcepcionValidacionBD(erroresBD);
            }
        } 
    }
    
    
    public void editarCliente ( ModeloCliente cliente , boolean isAdministrador ) throws Exception{
    
        if(!isAdministrador)
            throw new Exception("Solo el usuario administrador puede editar un cliente");
        
        validarDatosUnicosCliente(cliente);
        
        Connection conexionDB = ConexionDB.getConnection();
        
        String sentenciaSQL = 
                "UPDATE\n" +
                "    clientes\n" +
                "SET\n" +
                "    nombre_negocio = ? ,\n" +
                "    primer_nombre_contacto = ? ,\n" +
                "    segundo_nombre_contacto = ? ,\n" +
                "    primer_apellido_contacto = ? ,\n" +
                "    segundo_apellido_contacto = ? ,\n" +
                "    telefono_contacto = ? ,\n" +
                "    direccion_cliente = ? ,\n" +
                "    correo_cliente = ? \n" +
                "WHERE\n" +
                "    id_cliente = ?";
        
        try( PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL)){
        
            consulta.setString(1, cliente.getNombreNegocio());
            consulta.setString(2, cliente.getPrimerNombreContacto());
            consulta.setString(3, cliente.getSegundoNombreContacto());
            consulta.setString(4, cliente.getPrimerApellidoContacto());
            consulta.setString(5, cliente.getSegundoApellidoContacto());
            consulta.setString(6, cliente.getTelefonoContacto());
            consulta.setString(7, cliente.getDireccionCliente());
            consulta.setString(8, cliente.getCorreoCliente());
            
            consulta.setInt(9, cliente.getIdCliente());
            
            int filasAfectadas = consulta.executeUpdate();
            if(filasAfectadas != 1)
                throw new Exception("El cliente no se edito correctamente");
        }
        
    }
    
    
    /*
    Metodo para asignar el estado de habilitado a un cliente
    */
    public void asignarEstadoCliente( int idCliente, boolean habilitado, boolean isAdministrador) throws Exception {
    
        if(!isAdministrador)
            throw new Exception("Solo el usuario administrador puede modificar el estado de un cliente");
        
        Connection conexionDB = ConexionDB.getConnection();
        
        String sentenciaSQL = "UPDATE clientes SET habilitado = ? WHERE id_cliente = ?";
        
        try( PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL)){
            
            consulta.setBoolean(1, habilitado);
            consulta.setInt(2, idCliente);
            
            int filasAfectadas = consulta.executeUpdate();
            
            if( filasAfectadas != 1 )
                throw new Exception("No se pudo modificar el estado del cliente");
            
        }
    }
    
    
    /*
    Metodo para crear un cliente, 
    se ignora el id pues este valor se lo asigna la base de datos
    el cual hay que retornar.
    */
    public int crearCliente ( ModeloCliente cliente , boolean isAdministrador) throws Exception{
    
        if(!isAdministrador)
            throw new Exception("Solo el usuario administrador puede crear un cliente");
        
        validarDatosUnicosCliente(cliente);
                
        Connection conexionDB = ConexionDB.getConnection();
        
        String sentenciaSQL = 
                "INSERT INTO\n" +
                "    clientes(\n" +
                "        nombre_negocio,\n" +
                "        primer_nombre_contacto,\n" +
                "        segundo_nombre_contacto,\n" +
                "        primer_apellido_contacto,\n" +
                "        segundo_apellido_contacto,\n" +
                "        telefono_contacto,\n" +
                "        direccion_cliente,\n" +
                "        correo_cliente,\n" +
                "        habilitado\n" +
                "    )\n" +
                "VALUES\n" +
                "    ( ? , ? , ? , ? , ? , ? , ? , ? , ? )";
        
        try( PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL, Statement.RETURN_GENERATED_KEYS)){
        
            consulta.setString(1, cliente.getNombreNegocio());
            consulta.setString(2, cliente.getPrimerNombreContacto());
            consulta.setString(3, cliente.getSegundoNombreContacto());
            consulta.setString(4, cliente.getPrimerApellidoContacto());
            consulta.setString(5, cliente.getSegundoApellidoContacto());
            consulta.setString(6, cliente.getTelefonoContacto());
            consulta.setString(7, cliente.getDireccionCliente());
            consulta.setString(8, cliente.getCorreoCliente());
            consulta.setBoolean( 9, true );
            
            int filasAfectadas = consulta.executeUpdate();
            if(filasAfectadas == 1)
                try( ResultSet respuesta = consulta.getGeneratedKeys() ){ 
                    if( respuesta.next() ){
                        //Retornamos el id del cliente creado
                        return ( respuesta.getInt( 1 ) );
                    }else{
                        throw new Exception( "Error al obtener el id del cliente" );
                    }
                }
            else
                throw new Exception("No se pudo crear el cliente");
            
        }
    }
    
    
    /*
    Metodo para eliminar un cliente.
    Pero si este tiene alguna relacion con otra tabla no se puede eliminar
    */
    public void eliminarCliente ( int idCliente, boolean isAdministrador ) throws Exception{
        
        if(!isAdministrador)
            throw new Exception("Solo el usuario administrador puede eliminar un cliente");
        
        if(!isClienteEliminable(idCliente))
            throw new Exception("Este cliente no se puede eliminar");
        
        Connection conexionDB = ConexionDB.getConnection();
        
        String sentenciaSQL = "DELETE FROM clientes WHERE id_cliente = ?";
        
        try( PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL)){
            
            consulta.setInt(1, idCliente);
            
            int filasAfectadas = consulta.executeUpdate();
            
            if( filasAfectadas != 1 )
                throw new Exception("No se pudo eliminar el cliente");
            
        }
    }
    
    
    /*
    Metodo para consultar si un cliente hace parte del registro de otra tabla
        la unica relacion que va tener un cliente es con ventas, por ende
        se consulta si tiene algun registro en ventas
    */
    public boolean isClienteEliminable ( int idCliente ) throws Exception{
    /*  TODO descomentar cuando este la tabla de ventas
        Connection conexionDB = ConexionDB.getConnection();
        String sentenciaSQL = 
            "SELECT(\n" +
            "    EXISTS(\n" +
            "        SELECT 1 FROM ventas WHERE id_cliente = ? \n" +
            "        ) \n" +
            "   )  AS tieneRegistros";
            
        try (PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL)) {
            consulta.setInt(1, idCliente);
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
    */
    //Para pruebas de eliminacion devolvemos true
    return true;
    }
}

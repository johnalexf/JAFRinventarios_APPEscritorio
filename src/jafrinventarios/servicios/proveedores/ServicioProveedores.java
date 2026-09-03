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
import java.sql.Statement;
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
    /*
    TODO: 
    Este metodo del servicio lo consume por el momento el controlador del
    dialogo de productos, en el caso de editar un producto donde su proveedor
    este deshabilitado, la vista no podra mostrar su proveedor y por ende
    no se podra modificar solo los precios del mismo, en un caso hipotetico,
    se podria decir que si se deshabilita un proveedor se deberia deshabilitar sus 
    productos?, ahora si pasamos a una venta donde se desee editar solo la cantidad
    de productos, entonces el diccionario de proveedores y el diccionario de productos tiene
    que diferenciar en dos aspectos, uno que entregue todos sin importar si estan habilitados
    este seria ideal para cuando se necesite editar un registro, pero cuando se cree uno nuevo
    solo mostrar los que estan habilitados, seria una solucion para este problema,
    sin embargo para no alargar mas el codigo se ignora que cuando se edite una venta
    se pueda asociar a un registro que no este habilitado, esta seria una mejora a futuro,
    despues de terminar la app.
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
        
        //Si es un proveedor nuevo se espera que no este asignado el ID
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
                    if(respuesta.getString( "nombre_comercial").toLowerCase().equals(proveedor.getNombreComercial().toLowerCase()))
                       erroresBD.put("nombreComercial", "Este nombre ya esta registrado");
                    if(respuesta.getString("telefono_contacto").equals(proveedor.getTelefonoContacto()))
                        erroresBD.put("telefonoContacto", "Este telefono ya esta registrado");
                    if(respuesta.getString("correo_proveedor").toLowerCase().equals(proveedor.getCorreoProveedor().toLowerCase()))
                        erroresBD.put("correoProveedor", "Este correo ya esta registrado");
                }
                
                if(!erroresBD.isEmpty())
                    throw new ExcepcionValidacionBD(erroresBD);
            }
        } 
    }
    
    
    public void editarProveedor ( ModeloProveedor proveedor ) throws Exception{
    
        validarDatosUnicosProveedor(proveedor);
        
        Connection conexionDB = ConexionDB.getConnection();
        
        String sentenciaSQL = 
                "UPDATE\n" +
                "    proveedores\n" +
                "SET\n" +
                "    nombre_comercial = ?,\n" +
                "    primer_nombre_contacto = ?,\n" +
                "    segundo_nombre_contacto = ?,\n" +
                "    primer_apellido_contacto = ?,\n" +
                "    segundo_apellido_contacto = ?,\n" +
                "    telefono_contacto = ?,\n" +
                "    direccion_proveedor = ?,\n" +
                "    correo_proveedor= ?\n" +
                "WHERE\n" +
                "    id_proveedor = ?";
        
        try( PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL)){
        
            consulta.setString(1, proveedor.getNombreComercial());
            consulta.setString(2, proveedor.getPrimerNombreContacto());
            consulta.setString(3, proveedor.getSegundoNombreContacto());
            consulta.setString(4, proveedor.getPrimerApellidoContacto());
            consulta.setString(5, proveedor.getSegundoApellidoContacto());
            consulta.setString(6, proveedor.getTelefonoContacto());
            consulta.setString(7, proveedor.getDireccionProveedor());
            consulta.setString(8, proveedor.getCorreoProveedor());
            
            consulta.setInt(9, proveedor.getIdProveedor() );
            
            int filasAfectadas = consulta.executeUpdate();
            if(filasAfectadas != 1)
                throw new Exception("El proveedor no se edito correctamente");
        }
        
    }
    
    
    /*
    Metodo para conmutar el estado de habilitado de un proveedor
    */
    public void asignarEstadoProveedor( int idProveedor, boolean habilitado) throws Exception {
    
        Connection conexionDB = ConexionDB.getConnection();
        
        String sentenciaSQL = "UPDATE proveedores SET habilitado = ? WHERE id_proveedor = ?";
        
        try( PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL)){
            
            consulta.setBoolean(1, habilitado);
            consulta.setInt(2, idProveedor);
            
            int filasAfectadas = consulta.executeUpdate();
            
            if( filasAfectadas != 1 )
                throw new Exception("No se pudo modificar el estado del proveedor");
            
        }
    }
    
    
    /*
    Metodo para crear un proveedor, 
    se ignora el id pues este valor se lo asigna la base de datos
    el cual hay que retornar.
    */
    public int crearProveedor ( ModeloProveedor proveedor ) throws Exception{
    
        validarDatosUnicosProveedor(proveedor);
        
        Connection conexionDB = ConexionDB.getConnection();
        
        String sentenciaSQL = 
                "INSERT INTO\n" +
                "    proveedores(\n" +
                "        nombre_comercial,\n" +
                "        primer_nombre_contacto,\n" +
                "        segundo_nombre_contacto,\n" +
                "        primer_apellido_contacto,\n" +
                "        segundo_apellido_contacto,\n" +
                "        telefono_contacto,\n" +
                "        direccion_proveedor,\n" +
                "        correo_proveedor,\n" +
                "        habilitado\n" +
                "    )\n" +
                "VALUES\n" +
                "    ( ? , ? , ? , ? , ? , ? , ? , ? , ? )";
        
        try( PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL, Statement.RETURN_GENERATED_KEYS)){
        
            consulta.setString(1, proveedor.getNombreComercial());
            consulta.setString(2, proveedor.getPrimerNombreContacto());
            consulta.setString(3, proveedor.getSegundoNombreContacto());
            consulta.setString(4, proveedor.getPrimerApellidoContacto());
            consulta.setString(5, proveedor.getSegundoApellidoContacto());
            consulta.setString(6, proveedor.getTelefonoContacto());
            consulta.setString(7, proveedor.getDireccionProveedor());
            consulta.setString(8, proveedor.getCorreoProveedor());
            consulta.setBoolean( 9, true );
            
            int filasAfectadas = consulta.executeUpdate();
            if(filasAfectadas == 1)
                try( ResultSet respuesta = consulta.getGeneratedKeys() ){ 
                    if( respuesta.next() ){
                        //Retornamos el id del proveedor creado
                        return ( respuesta.getInt( 1 ) );
                    }else{
                        throw new Exception( "Error al obtener el id del proveedor" );
                    }
                }
            else
                throw new Exception("No se pudo crear el proveedor");
            
        }
        
    }
    
    
    /*
    Metodo para eliminar un proveedor.
    Pero si este tiene alguna relacion con otra tabla no se puede eliminar
    */
    public void eliminarProveedor ( int idProveedor  ) throws Exception{
        
        if( !isProveedorEliminable(idProveedor) )
            throw new Exception("Este proveedor no se puede eliminar");
        
        Connection conexionDB = ConexionDB.getConnection();
        
        String sentenciaSQL = "DELETE FROM proveedores WHERE id_proveedor = ?";
        
        try( PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL)){
            consulta.setInt(1, idProveedor);
            int filasAfectadas = consulta.executeUpdate();
            if( filasAfectadas != 1 )
                throw new Exception("El proveedor no se pudo eliminar");
        }
    }
    
    
    /*
    Metodo para consultar si un proveedor hace parte del registro de otra tabla
    */
    public boolean isProveedorEliminable ( int idProveedor ) throws Exception{
    
        /*
        Segun la solucion del proyecto un proveedor esta destinado a asignarse
        a los productos, y cuando se vaya a registrar una compra al seleccionar el
        proveedor se le mostraran al usuario los productos que corresponde a dicho
        proveedor, por tanto solo es necesario validar si el proveedor tiene productos relacionados
        y no es necesario validar si tiene compras registradas
        */
        
        Connection conexionDB = ConexionDB.getConnection();
        String sentenciaSQL = 
            "SELECT(\n" +
            "    EXISTS(\n" +
            "        SELECT 1 FROM productos WHERE id_proveedor = ? \n" +
            "        ) \n" +
            "   )  AS tieneRegistros";
            
        try (PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL)) {
            consulta.setInt(1, idProveedor);
            try (ResultSet respuesta = consulta.executeQuery()) {
                if (respuesta.next()) {
                    boolean tieneRegistros = respuesta.getBoolean("tieneRegistros");
                    return !tieneRegistros; // Si tiene registros no es eliminable
                }else{
                    //En dado caso que no se reciba una respuesta, para proteger
                    //la integridad de los datos, retornamos un falsa, para que no se 
                    //pueda eliminar
                    return false;
                }
            }
        }
    }
    
    
    
}

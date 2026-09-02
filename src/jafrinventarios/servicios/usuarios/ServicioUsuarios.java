
package jafrinventarios.servicios.usuarios;

import jafrinventarios.DTOs.usuarios.DTOUsuarioTabla;
import jafrinventarios.modelos.usuarios.ModeloUsuario;
import jafrinventarios.servicios.ConexionDB;
import jafrinventarios.servicios.acceso.ServicioSeguridad;
import jafrinventarios.servicios.correo.ServicioCorreos;
import jafrinventarios.servicios.excepciones.ExcepcionValidacionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author JOHN FORERO
 */
public class ServicioUsuarios {
    
    public ServicioUsuarios() {
    }

    
    public List<DTOUsuarioTabla> obtenerTodosLosUsuarios() throws Exception{
        
        List<DTOUsuarioTabla> listaUsuarios = new ArrayList<>();
        
        Connection conexionDB = ConexionDB.getConnection();
        
        String sentenciaSQL = 
                "SELECT\n" +
                "    us.id_usuario AS 'id',"+
                "    us.alias_usuario AS 'alias',\n" +
                "    us.telefono_usuario AS 'telefono',\n" +
                "    us.correo_usuario AS 'correo',\n" +
                "    CONCAT(\n" +
                "        us.primer_nombre_usuario, \" \",\n" +
                "        us.segundo_nombre_usuario, \" \",\n" +
                "        us.primer_apellido_usuario, \" \",\n" +
                "        us.segundo_apellido_usuario\n" +
                "    ) AS 'nombreCompleto',\n" +
                "    rol.nombre_rol AS 'rol',\n" +
                "    us.habilitado AS 'habilitado'\n" +
                "FROM\n" +
                "    usuarios us\n" +
                "INNER JOIN\n" +
                "    roles rol\n" +
                "ON us.id_rol_usuario = rol.id_rol\n";
        
        try( PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL);
             ResultSet respuesta = consulta.executeQuery();
            ){

            while( respuesta.next() ){
                listaUsuarios.add( 
                        new DTOUsuarioTabla(
                            respuesta.getInt("id"),
                            respuesta.getString("alias"),
                            respuesta.getString("telefono"),
                            respuesta.getString("correo"),
                            respuesta.getString("nombreCompleto"),
                            respuesta.getString("rol"),
                            respuesta.getBoolean("habilitado")
                        )
                );
            }
            
        }
        
        return listaUsuarios;
    }
    
    
    public List<DTOUsuarioTabla> obtenerListaUsuariosPorFiltro( String filtro ) throws Exception {
        
        List<DTOUsuarioTabla> listaUsuarios = new ArrayList<>();
        
        filtro = "%" + filtro + "%";
        
        Connection conexionDB = ConexionDB.getConnection();
        
        String sentenciaSQL = 
                "SELECT\n" +
                "    us.id_usuario AS 'id',\n" +
                "    us.alias_usuario AS 'alias',\n" +
                "    us.telefono_usuario AS 'telefono',\n" +
                "    us.correo_usuario AS 'correo',\n" +
                "    CONCAT(\n" +
                "        us.primer_nombre_usuario, \" \",\n" +
                "        us.segundo_nombre_usuario, \" \",\n" +
                "        us.primer_apellido_usuario, \" \",\n" +
                "        us.segundo_apellido_usuario\n" +
                "    ) AS 'nombreCompleto',\n" +
                "    rol.nombre_rol AS 'rol',\n" +
                "    us.habilitado AS 'habilitado'\n" +
                "FROM\n" +
                "    usuarios us\n" +
                "INNER JOIN\n" +
                "    roles rol\n" +
                "ON us.id_rol_usuario = rol.id_rol\n" +
                "WHERE\n" +
                "    us.alias_usuario LIKE ? OR\n" +
                "    us.telefono_usuario LIKE ? OR\n" +
                "    us.correo_usuario LIKE ? OR\n" +
                "    CONCAT(us.primer_nombre_usuario, ' ', us.segundo_nombre_usuario, ' ', us.primer_apellido_usuario, ' ', us.segundo_apellido_usuario) LIKE ? OR\n" +
                "    rol.nombre_rol LIKE ?";
        
        try( PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL) ){

            consulta.setString(1, filtro);
            consulta.setString(2, filtro);
            consulta.setString(3, filtro);
            consulta.setString(4, filtro);
            consulta.setString(5, filtro);
            
            try( ResultSet respuesta = consulta.executeQuery()){
                while( respuesta.next() ){
                    listaUsuarios.add( 
                            new DTOUsuarioTabla(
                                respuesta.getInt("id"),
                                respuesta.getString("alias"),
                                respuesta.getString("telefono"),
                                respuesta.getString("correo"),
                                respuesta.getString("nombreCompleto"),
                                respuesta.getString("rol"),
                                respuesta.getBoolean("habilitado")
                            )
                    );
                }
            }
            
        }
        
        return listaUsuarios;
    }
    
    
    // Este metodo entrega un usuario tipo DTOUsuarioTabla y esta destinado solo para mostrar los datos en una tabla o una tarjeta de perfil
    public DTOUsuarioTabla obtenerDatosDTOUsuario( int idUsuario ) throws Exception{
    
        Connection conexionDB = ConexionDB.getConnection();
        
        String sentenciaSQL = 
                "SELECT\n" +
                "    us.alias_usuario AS 'alias',\n" +
                "    us.telefono_usuario AS 'telefono',\n" +
                "    us.correo_usuario AS 'correo',\n" +
                "    CONCAT(\n" +
                "        us.primer_nombre_usuario, \" \",\n" +
                "        us.segundo_nombre_usuario, \" \",\n" +
                "        us.primer_apellido_usuario, \" \",\n" +
                "        us.segundo_apellido_usuario\n" +
                "    ) AS 'nombreCompleto',\n" +
                "    rol.nombre_rol AS 'rol',\n" +
                "    us.habilitado AS 'habilitado'\n" +
                "FROM\n" +
                "    usuarios us\n" +
                "INNER JOIN\n" +
                "    roles rol\n" +
                "ON us.id_rol_usuario = rol.id_rol\n" +
                "WHERE\n" +
                "    us.id_usuario = ?";
        
        try( PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL)){
        
            consulta.setInt(1, idUsuario);
            
            try(ResultSet respuesta = consulta.executeQuery()){
            
                if( respuesta.next() ){
                    return new DTOUsuarioTabla(
                            idUsuario,
                            respuesta.getString("alias"),
                            respuesta.getString("telefono"),
                            respuesta.getString("correo"),
                            respuesta.getString("nombreCompleto"),
                            respuesta.getString("rol"),
                            respuesta.getBoolean("habilitado")
                    );  
                }else
                    throw new Exception("No existe un usuario con id : " + idUsuario);
            }
        }
        
    }
    
    //Este metodo devuelve un usuario del tipo ModeloUsuario que servira de base para poder editarlo y devolverlo para guardar los cambios
    public ModeloUsuario obtenerModeloUsuario( int idUsuario ) throws Exception{
    
        Connection conexionDB = ConexionDB.getConnection();
        
        String sentenciaSQL = 
                "SELECT\n" +
                "    id_empresa,\n" +
                "    alias_usuario AS 'alias',\n" +
                "    telefono_usuario AS 'telefono',\n" +
                "    correo_usuario AS 'correo',\n" +
                "    primer_nombre_usuario AS 'primerNombre',\n" +
                "    segundo_nombre_usuario AS 'segundoNombre',\n" +
                "    primer_apellido_usuario AS 'primerApellido',\n" +
                "    segundo_apellido_usuario AS 'segundoApellido',\n" +
                "    id_rol_usuario AS 'idRol',\n" +
                "    habilitado\n" +
                "FROM\n" +
                "    usuarios\n" +
                "WHERE\n" +
                "    id_usuario = ?";
        
        try( PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL)){
        
            consulta.setInt(1, idUsuario);
            
            try(ResultSet respuesta = consulta.executeQuery()){
            
                if( respuesta.next() ){
                    return new ModeloUsuario(
                            idUsuario,
                            respuesta.getInt("id_empresa"),
                            respuesta.getString("alias"),
                            respuesta.getString("telefono"),
                            respuesta.getString("correo"),
                            respuesta.getString("primerNombre"),
                            respuesta.getString("segundoNombre"),
                            respuesta.getString("primerApellido"),
                            respuesta.getString("segundoApellido"),
                            "",// Enviamos la contraseña vacia
                            respuesta.getInt("idRol"),
                            respuesta.getBoolean("habilitado")
                    );  
                }else
                    throw new Exception("No existe un usuario con id : " + idUsuario);
            }
        }
        
    }
    
    
    public void editarPerfil( ModeloUsuario usuario , boolean isAdministrador ) throws Exception{
    
        validarDatosUnicosExcluyendoIdUsuario(usuario);
        
        if(isAdministrador)
            editarPerfilAdministrador(usuario);
        else
            editarPerfilNoAdministrador(usuario);
        
    }
    
        
    private void editarPerfilAdministrador(ModeloUsuario usuario)throws Exception{
        
        Connection conexionDB = ConexionDB.getConnection();
        
        String sentenciaSQL = 
                "UPDATE\n" +
                "    usuarios\n" +
                "SET\n" +
                "    alias_usuario = ?,\n" +
                "    telefono_usuario = ?,\n" +
                "    correo_usuario = ?,\n" +
                "    primer_nombre_usuario = ?,\n" +
                "    segundo_nombre_usuario = ?,\n" +
                "    primer_apellido_usuario = ?,\n" +
                "    segundo_apellido_usuario = ?\n" +
                "WHERE\n" +
                "    id_usuario = ?";
        
        try( PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL)){
            
            consulta.setString(1, usuario.getAliasUsuario());
            consulta.setString(2, usuario.getTelefonoUsuario());
            consulta.setString(3, usuario.getCorreoUsuario());
            consulta.setString(4, usuario.getPrimerNombreUsuario());
            consulta.setString(5, usuario.getSegundoNombreUsuario());
            consulta.setString(6, usuario.getPrimerApellidoUsuario());
            consulta.setString(7, usuario.getSegundoApellidoUsuario());
            
            consulta.setInt(8, usuario.getIdUsuario());
            
            int filasAfectadas = consulta.executeUpdate();
            
            if(filasAfectadas != 1)
                throw new Exception("El usuario no edito correctamente");
        
        }
    
    }
    
    private void editarPerfilNoAdministrador(ModeloUsuario usuario) throws Exception{
    
        Connection conexionDB = ConexionDB.getConnection();
        
        String sentenciaSQL = 
                "UPDATE\n" +
                "    usuarios\n" +
                "SET\n" +
                "    telefono_usuario = ?,\n" +
                "    correo_usuario = ?\n" +
                "WHERE\n" +
                "    id_usuario = ?";
        
        try( PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL)){
            
            consulta.setString(1, usuario.getTelefonoUsuario());
            consulta.setString(2, usuario.getCorreoUsuario());
            
            consulta.setInt(3, usuario.getIdUsuario());
            
            int filasAfectadas = consulta.executeUpdate();
            
            if(filasAfectadas != 1)
                throw new Exception("El usuario no se edito correctamente");
        
        }
    }
    
    
    
    public void editarOtroUsuario( ModeloUsuario usuario ) throws Exception{
    
        validarDatosUnicosExcluyendoIdUsuario(usuario);
        
        Connection conexionDB = ConexionDB.getConnection();
        
        String sentenciaSQL = 
                "UPDATE\n" +
                "    usuarios\n" +
                "SET\n" +
                "    alias_usuario = ?,\n" +
                "    telefono_usuario = ?,\n" +
                "    correo_usuario = ?,\n" +
                "    primer_nombre_usuario = ?,\n" +
                "    segundo_nombre_usuario = ?,\n" +
                "    primer_apellido_usuario = ?,\n" +
                "    segundo_apellido_usuario = ?,\n" +
                "    id_rol_usuario = ?"+
                "WHERE\n" +
                "    id_usuario = ?";
        
        try( PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL)){
            
            consulta.setString(1, usuario.getAliasUsuario());
            consulta.setString(2, usuario.getTelefonoUsuario());
            consulta.setString(3, usuario.getCorreoUsuario());
            consulta.setString(4, usuario.getPrimerNombreUsuario());
            consulta.setString(5, usuario.getSegundoNombreUsuario());
            consulta.setString(6, usuario.getPrimerApellidoUsuario());
            consulta.setString(7, usuario.getSegundoApellidoUsuario());
            consulta.setInt(8, usuario.getIdRolUsuario());
            
            consulta.setInt(9, usuario.getIdUsuario());
            
            int filasAfectadas = consulta.executeUpdate();
            
            if(filasAfectadas != 1)
                throw new Exception("El usuario no edito correctamente");
        
        }
        
    }

    
    public void asignarEstadoUsuario (  int idUsuario, boolean habilitado  ) throws Exception {
    
        if( !habilitado && isUltimoAdministrador(idUsuario)){
                throw new Exception("Este usuario administrador no se puede deshabilitar \n"
                                  + "Por que quedaria sin un usuario que gestione la aplicacion."
                );
        }
        
        
        Connection conexionDB = ConexionDB.getConnection();
        
        String sentenciaSQL = "UPDATE usuarios SET habilitado = ? WHERE id_usuario = ?";
        
        try( PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL)){
            
            consulta.setBoolean(1, habilitado);
            consulta.setInt(2, idUsuario);
            
            int filasAfectadas = consulta.executeUpdate();
            
            if( filasAfectadas != 1 )
                throw new Exception("No se pudo modificar el estado del usuario");
            
        }
        
    }
    
    
    
    public int crearUsuario( ModeloUsuario usuario ) throws Exception{
    
        String contrasenaAleatoria = ServicioSeguridad.generarContrasena();
        usuario.setContrasenaUsuario( ServicioSeguridad.hashearContrasena(contrasenaAleatoria) );
        int idUsuarioCreado = registrarUsuario(usuario);
        
        try {
            /*
            Enviar correo con la contrasena, en dado caso que suceda un error
            se elimina el usuario, pues la operacion no se considera completa
            si el correo con la contrasena no se envia, puesto que esto no le garantiza
            al usuario creado que pueda iniciar sesion.
            */    
            ServicioCorreos.enviarCredenciales(
                                usuario.getCorreoUsuario(),  
                                usuario.getNombreCompletoUsuario(), 
                                contrasenaAleatoria
            );
            return idUsuarioCreado;
        } catch (Exception e) {
            throw new Exception("Error al tratar de enviar el correo con credenciasles \n"+ 
                    "El usuario se ha creado con el correo "+ usuario.getCorreoUsuario() + 
                    " para iniciar sesion por favor recuperar la contraseña"
                    );
        }
        
    }
    
    
    public void eliminarUsuario( int idUsuario ) throws Exception{
        
        if( !isUsuarioEliminable(idUsuario) )
            throw new Exception("Este usuario no se puede eliminar");
        
        Connection conexionDB = ConexionDB.getConnection();
        
        String sentenciaSQL = "DELETE FROM usuarios WHERE id_usuario = ?";
        
        try( PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL)){
            consulta.setInt(1, idUsuario);
            int filasAfectadas = consulta.executeUpdate();
            if( filasAfectadas != 1 )
                throw new Exception("El usuario no se pudo eliminar");
        }

    }
    
    
public boolean isUsuarioEliminable(int idUsuario ) throws Exception {
                
        // REGLA 1: Evitar que se elimine al último administrador
        if(isUltimoAdministrador(idUsuario)) return false; //Si es el ultimo administrador no es eliminable
        
        
        // REGLA 2: Validar si tiene registros en otras tablas (Llaves foráneas)
        // TODO: Descomentar y ajustar los nombres de tablas cuando se creen los módulos de ventas/compras
        /*
        Connection conexionDB = ConexionDB.getConnection();
        sentenciaSQL = 
            "SELECT (" +
            "   EXISTS(SELECT 1 FROM ventas WHERE id_usuario = ?) OR " +
            "   EXISTS(SELECT 1 FROM compras WHERE id_usuario = ?) " +
            ") AS tiene_registros";
            
        try (PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL)) {
            consulta.setInt(1, idUsuario);
            consulta.setInt(2, idUsuario);
            try (ResultSet respuesta = consulta.executeQuery()) {
                if (respuesta.next()) {
                    boolean tieneRegistros = respuesta.getBoolean("tiene_registros");
                    if (tieneRegistros) {
                        return false; // Tiene historial, NO es eliminable (pero se puede modificar su estado habilitado)
                    }
                }
            }
        }
        */
        
        // Si pasa las validaciones (o si aún no hay tablas de ventas), se puede eliminar
        return true;
    }
    
    /*
    Metodo para validar si un usuario es administrador y si es el ultimo administrador
    */
    private boolean isUltimoAdministrador ( int idUsuario ) throws Exception{
        
        Connection conexionDB = ConexionDB.getConnection();

        // PASO 1: Validar si el usuario objetivo es un administrador
        String sentenciaSQL = "SELECT id_rol_usuario FROM usuarios WHERE id_usuario = ?";
        try (PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL)) {
            consulta.setInt(1, idUsuario);
            try (ResultSet respuesta = consulta.executeQuery()) {
                if (respuesta.next()) {
                    // Asumimos que 1 es el ID del rol Administrador
                    if (respuesta.getInt("id_rol_usuario") != 1) {
                        return false; // No es administrador, no aplica esta regla
                    }
                } else {
                    throw new Exception("El usuario no existe");
                }
            }
        }
        
        // PASO 2: Si es administrador, contamos cuántos administradores existen en total
        sentenciaSQL = "SELECT COUNT(*) FROM usuarios WHERE id_rol_usuario = 1";
        try (
            PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL);
            ResultSet respuesta = consulta.executeQuery()
            ) {
            if ( respuesta.next() ) {
                return respuesta.getInt(1) <= 1; 
            }else{
                //Si no se obtuvo una respuesta de la consulta, para evitar 
                //eliminar o deshabilitar el ultimo administrador retornamos
                return true;
            }
        }
        
    }
    
    
    
    /*
    Metodo para validar si los datos que se desean cambiar de un usuario
    no los tiene otro usuario, por lo tanto se excluye los datos propios
    del mismo usuario a validar
    */
    private void validarDatosUnicosExcluyendoIdUsuario( ModeloUsuario usuario ) throws Exception{
        Connection conexionBD = ConexionDB.getConnection();
        
        String sentenciaSQL = 
                "SELECT \n" +
                "    alias_usuario, correo_usuario, telefono_usuario \n" +
                "FROM usuarios \n" +
                "WHERE \n" +
                "    (alias_usuario = ? OR correo_usuario = ? OR telefono_usuario = ?)\n" +
                "    AND id_usuario != ?";

        try (PreparedStatement consulta = conexionBD.prepareStatement( sentenciaSQL ) ) {
              
            consulta.setString(1, usuario.getAliasUsuario());
            consulta.setString(2, usuario.getCorreoUsuario());
            consulta.setString(3, usuario.getTelefonoUsuario());
            
            consulta.setInt(4, usuario.getIdUsuario());
            
            try( ResultSet respuesta = consulta.executeQuery() ){
                
                HashMap<String, String> erroresBD = new HashMap<>();
                
                while(respuesta.next()){
                    if(respuesta.getString( "alias_usuario").equals(usuario.getAliasUsuario()))
                       erroresBD.put("alias", "El alias ya está en uso");
                    if(respuesta.getString("correo_usuario").equals(usuario.getCorreoUsuario()))
                        erroresBD.put("correo", "Este correo ya esta registrado");
                    if(respuesta.getString("telefono_usuario").equals(usuario.getTelefonoUsuario()))
                        erroresBD.put("telefono", "Este telefono ya esta registrado");
                }
                
                if(!erroresBD.isEmpty())
                    throw new ExcepcionValidacionBD(erroresBD);
            }
        } 
    }
    
    
    
    /*
    ============================================================================
                               METODOS ESTATICOS
    ============================================================================
    */ 
    
    /*
    Metodo para validar si los datos de la base de datos determinados como unicos
    no los tiene un usuario ya almacenado en la base de datos
    */
    private static void validarDatosUnicosUsuario( ModeloUsuario usuario ) throws Exception{
        Connection conexionBD = ConexionDB.getConnection();
        
        String sentenciaSQL = 
                "SELECT alias_usuario, correo_usuario, telefono_usuario \n" +
                "FROM usuarios \n" +
                "WHERE alias_usuario = ? OR correo_usuario = ? OR telefono_usuario = ?";

        try (PreparedStatement consulta = conexionBD.prepareStatement( sentenciaSQL ) ) {
              
            consulta.setString(1, usuario.getAliasUsuario());
            consulta.setString(2, usuario.getCorreoUsuario());
            consulta.setString(3, usuario.getTelefonoUsuario());
            
            try( ResultSet respuesta = consulta.executeQuery() ){
                
                HashMap<String, String> erroresBD = new HashMap<>();
                
                while(respuesta.next()){
                    if(respuesta.getString( "alias_usuario").equals(usuario.getAliasUsuario()))
                       erroresBD.put("alias", "El alias ya está en uso");
                    if(respuesta.getString("correo_usuario").equals(usuario.getCorreoUsuario()))
                        erroresBD.put("correo", "Este correo ya esta registrado");
                    if(respuesta.getString("telefono_usuario").equals(usuario.getTelefonoUsuario()))
                        erroresBD.put("telefono", "Este telefono ya esta registrado");
                }
                
                if(!erroresBD.isEmpty())
                    throw new ExcepcionValidacionBD(erroresBD);
            }
        } 
    }
    
    
    /*
    Registrar usuario funciona para el registro desde ServicioRegistro y tambien
    para esta misma clase ServicioUsuarios para cuando se desee crear un usuario
    */
    public static int registrarUsuario (ModeloUsuario usuario) throws Exception{
    
        Connection conexionBD = ConexionDB.getConnection();

        validarDatosUnicosUsuario(usuario);
        
        String sentenciaSQL = 
                "INSERT INTO \n" +
                "    usuarios(\n" +
                "        id_empresa,\n" +
                "        alias_usuario,\n" +
                "        telefono_usuario,\n" +
                "        correo_usuario,\n" +
                "        primer_nombre_usuario,\n" +
                "        segundo_nombre_usuario,\n" +
                "        primer_apellido_usuario,\n" +
                "        segundo_apellido_usuario,\n" +
                "        contrasena_usuario,\n" +
                "        id_rol_usuario,\n" +
                "        habilitado\n" +
                "    )\n" +
                "VALUES\n" +
                "    ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? )";

        try( PreparedStatement consulta = conexionBD.prepareStatement( sentenciaSQL, Statement.RETURN_GENERATED_KEYS ) ){

            consulta.setInt( 1, usuario.getIdEmpresa() );
            consulta.setString(2, usuario.getAliasUsuario());
            consulta.setString(3, usuario.getTelefonoUsuario());
            consulta.setString(4, usuario.getCorreoUsuario());
            consulta.setString(5, usuario.getPrimerNombreUsuario());
            consulta.setString(6, usuario.getSegundoNombreUsuario());
            consulta.setString(7, usuario.getPrimerApellidoUsuario());
            consulta.setString(8, usuario.getSegundoApellidoUsuario());
            consulta.setString(9, usuario.getContrasenaUsuario());
            consulta.setInt(10, usuario.getIdRolUsuario());
            consulta.setBoolean(11, true);

            int filasAfectadas = consulta.executeUpdate();
            if(filasAfectadas == 1)
                try( ResultSet respuesta = consulta.getGeneratedKeys() ){ 
                    if( respuesta.next() ){
                        //Retornamos el id del usuario creado
                        return ( respuesta.getInt( 1 ) );
                    }else{
                        throw new Exception( "Error al obtener el id del usuario" );
                    }
                }
            else
                throw new Exception("No se pudo crear el usuario");
        }
    
    }
 
    
}


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

    
    private List<DTOUsuarioTabla> simulacionConsultaBDTodosUsuarios () {
        List<DTOUsuarioTabla> listaUsuarios = new ArrayList<>();
        
        listaUsuarios.add(new DTOUsuarioTabla(1, "john1", "3202173409", "john1@gmail.com", "John Forero", "Administrador", true));
        listaUsuarios.add(new DTOUsuarioTabla(2, "amartinez", "3101234567", "amartinez@empresa.com", "Ana María Martínez López", "Vendedor", true));
        listaUsuarios.add(new DTOUsuarioTabla(3, "cramirez", "3119876543", "cramirez@empresa.com", "Carlos Andrés Ramírez Gómez", "Vendedor", true));
        listaUsuarios.add(new DTOUsuarioTabla(4, "dcastro", "3004567890", "dcastro@empresa.com", "Diana Castro Vega", "Administrador", true));
        listaUsuarios.add(new DTOUsuarioTabla(5, "lherrera", "3205554433", "lherrera@empresa.com", "Luis Fernando Herrera Díaz", "Vendedor", false)); 
        listaUsuarios.add(new DTOUsuarioTabla(6, "mrojas", "3156667788", "mrojas@empresa.com", "Marta Lucía Rojas Silva", "Vendedor", true));
        listaUsuarios.add(new DTOUsuarioTabla(7, "jospina", "3129998877", "jospina@empresa.com", "Jorge Iván Ospina Cruz", "Vendedor", true));
        listaUsuarios.add(new DTOUsuarioTabla(8, "vquintero", "3182223344", "vquintero@empresa.com", "Valentina Quintero Ríos", "Administrador", true));
        listaUsuarios.add(new DTOUsuarioTabla(9, "sgalvis", "3194445566", "sgalvis@empresa.com", "Sergio Esteban Galvis Mora", "Vendedor", false));
        listaUsuarios.add(new DTOUsuarioTabla(10, "pnavarro", "3017778899", "pnavarro@empresa.com", "Patricia Navarro Pérez", "Vendedor", true));
        
        return listaUsuarios;
    }

    
    public List<DTOUsuarioTabla> obtenerTodosLosUsuarios() throws Exception{
        
        //TODO: Aqui se hara la consulta a la base de datos y se armara
        //la lista de todos los usuarios, por el momento se simula tanto la consulta
        // como el empaquetado con la siguiente funcion.
        List<DTOUsuarioTabla> listaUsuarios = simulacionConsultaBDTodosUsuarios();
        
        return listaUsuarios;
    }
    
    
    public List<DTOUsuarioTabla> obtenerListaUsuariosPorFiltro( String filtro ) throws Exception {
        System.out.println("Buscando en la BD de Usuarios el término: " + filtro);
        
        //TODO: Aqui se hara la consulta a la base de datos y se armara
        //la lista de todos los usuarios que cumplan el filtro, por el momento se simula que no hay coincidencias
        List<DTOUsuarioTabla> listaUsuarios = new ArrayList<>();
        
        try {
            
            //Si el resultado no arroja ningun valor, entonces mandamos la lista vacia
        } catch (Exception e) {
            // Si se puede determinar si el error es por base de datos
            //Entonces crear el error
            //throw new Exception("Error de conexion");
            
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

    
    public void asignarEstadoUsuario (  int idUsuario , boolean habilitado  ) throws Exception {
    
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
            eliminarUsuario(idUsuarioCreado);
            throw new Exception("Error al crear el usuario \n"+ e.getMessage());
        }
        
    }
    
    
    public void eliminarUsuario( int idUsuario ) throws Exception{
        
        /*
        Para eliminar un usuario se hara la respectiva consulta, sin embargo
        la base de datos no debe permitir eliminar un usuario si este
        es la clave foranea de cualquier tabla, como ventas o productos
        
        Es importante tambien tener en cuenta en un futuro que el mismo administrador
        no se pueda eliminar en gestion de usuarios, en su momento se planteara
        el codigo para evitar esta eventualidad
        
        //si hay error crear el error segun sea el caso
        //    throw new RuntimeException("No se pudo completar la operacion o falla en el servicio");
        
        Aqui tambien se va verificar si el idUsuario es clave foranea para
        evitar eliminarlo si tiene registros asociados en dado caso
        se devolvera el error con el mensaje 
        throw new RuntimeException("Usuario con registros asociados no se puede eliminar");
        
        tambien existira una regla que un usuario adminsitrador no se puede eliminar
        aqui para ser mas especifico se puede crear una variable superAdminsitrador
        que solo sera el primer adminsitrador registrado y este no se podra eliminar
        dependiendo del tiempo para implementar la solucion se decidira cual opcion
        realizar
        */

    }
    
    
    public boolean isUsuarioEliminable ( int idUsuario ) throws Exception{
    
        boolean respuestaConsulta = false;
        
        /*
        Esta consulta se encargara de verificar si el usuario es clave foranea
        de otra tabla, esto permite personalizar el dialogo para mostrar el boton
        eliminar si no tiene una relacion
        o deshabilitar si tiene una relacion
        
        //si hay error crear el error segun sea el caso
            //    throw new RuntimeException("No se pudo completar la operacion o falla en el servicio");
        
        */
        
        
        return respuestaConsulta;
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

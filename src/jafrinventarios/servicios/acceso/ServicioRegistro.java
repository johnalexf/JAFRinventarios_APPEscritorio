
package jafrinventarios.servicios.acceso;

import jafrinventarios.modelos.usuarios.ModeloEmpresa;
import jafrinventarios.modelos.usuarios.ModeloUsuario;
import jafrinventarios.servicios.Conexion_DB;
import jafrinventarios.servicios.excepciones.ExcepcionValidacionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

/**
 *
 * @author JOHN FORERO
 */
public class ServicioRegistro {
    /*
    Esta clase funcionara para poder registrar un usuario sin haber iniciado sesion
    en el caso de que un administrador desee crear un usuario lo puede hacer desde 
    el servicioUsuarios ya que la creacion cambia puesto que la contraseña se enviara
    por correo al usuario creado, en cambio desde esta clase el registro pide la clave
    y un codigo de acceso
    */
    
    public boolean isValidoCodigo( String codigo, boolean isRegistroAdminsitrador ) throws Exception {
    
        Connection conexionBD = Conexion_DB.getConnection();
        
        if(isRegistroAdminsitrador){
            
            if( !codigo.equals("JaFr_1pp4*") )
                return false;

            String sentenciaSQL = "SELECT COUNT(*) AS 'cantidadEmpresas' FROM empresa";
            
            /*
            Usamos try-with-resources para cerrar la consulta y no dejarla abierta
            para evitar almacenamiento de las mismas en cache y acumulacion que
            puede generar un colapso de la conexion con la base de datos.
            */
            try (PreparedStatement consulta = conexionBD.prepareStatement( sentenciaSQL ) ) {
                
                try( ResultSet respuesta = consulta.executeQuery() ){
                    //En respuesta la fila 0 es el inicio, por eso usamos next para revisar si hay datos
                    if(respuesta.next()){
                        if (respuesta.getInt("cantidadEmpresas") == 0) {
                            //Si aun no hay empresas el codigo sigue siendo valido
                            return true;        
                        }else{
                            /*
                            Si ya hay una empresa entonces no se pueden crear mas usuarios administradores POR MEDIO DE CODIGO
                            El codigo es solo valido para crear UN SOLO usuario administrador, por que la intencion es crear la empresa a la par.
                            Recordar que se va crear solo una empresa y se uso esa tabla para poder centralizar el nombre y un codigo para dejar 
                            registrar otros usuarios que no sean administradores.
                            */
                            throw new Exception("Este codigo era de un unico uso");
                        }
                    }
                    return false; 
                }
            } 
                
                
        }else{
        
            String sentenciaSQL =  "SELECT id_empresa FROM empresa WHERE codigo_registro_usuario_vendedor = ?";
            
            try (PreparedStatement consulta = conexionBD.prepareStatement( sentenciaSQL ) ) {
                
                consulta.setString(1, codigo);
                
                try( ResultSet respuesta = consulta.executeQuery() ){
                    //Si hay un valor es por que si coincidio la contraseña
                    return respuesta.next();
                }        
            }
        }
        
    }
    
    
    
    private int crearEmpresa ( String nombreEmpresa ) throws Exception{
    
        Connection conexionBD = Conexion_DB.getConnection();

        ModeloEmpresa empresa = new ModeloEmpresa();
        empresa.setNombreEmpresa(nombreEmpresa);
        empresa.setCodigoRegistroUsuarioVendedor( ServicioAutenticacion.generarCodigo() );

        String sentenciaSQL = 
                "INSERT INTO \n" +
                "	empresa\n" +
                "    (nombre_empresa, codigo_registro_usuario_vendedor) \n" +
                "VALUES ( ? , ? )";
        
        /*
            Usamos RETURN_GENERATED_KEYS Para que en la misma consulta se retorne el id de la empresa creada
        */
        try( PreparedStatement consulta = conexionBD.prepareStatement( sentenciaSQL, Statement.RETURN_GENERATED_KEYS )){
        
            consulta.setString(1, empresa.getNombreEmpresa());
            consulta.setString(2, empresa.getCodigoRegistroUsuarioVendedor());
            
            int filasAfectadas = consulta.executeUpdate();
            
            if( filasAfectadas == 1){
                try( ResultSet respuesta = consulta.getGeneratedKeys() ){ 
                    if( respuesta.next() ){
                        empresa.setIdEmpresa( respuesta.getInt( 1 ) );
                    }else{
                        throw new Exception( "Error al obtener el id de la empresa" );
                    }
                }
                
            }else{
                throw new Exception ("No se creo la empresa en la base de datos");
            }       
        }
        
        return empresa.getIdEmpresa();
    }
    
    
    
    public void registrarAdministrador ( ModeloUsuario usuario, String contrasena, String nombreEmpresa ) throws Exception{
    
        
            /*

                Dado que es un adminsitrador se creara la empresa primero

                1 Crear la empresa con el nombreEmpresa, aparte existira un metodo 
                    en Servicio autenticacion que creara un codigo aleatorio de 10 digitos  
                    se usara dicho metodo, para crear la empresa con el nombre y el codigo

                2 En el mismo servicio de autenticacion, existira un metodo para 
                    encriptar la contraseña

                3 Crear el usuario asociandolo al id de la empresa creada


            */
   
        /*
        Ejemplo de manejo de la respuesta de la base de datos
        Dejamos comentado hasta que se haga la conexion a la base de datos
        catch (SQLIntegrityConstraintViolationException e) {
            
            String errorBD = e.getMessage();
            HashMap<String, String> errores = new HashMap<>();

            // Buscar palabras clave en el error de la base de datos
            if (errorBD.contains("correo_UNIQUE")) {
                errores.put("correo", "Este correo ya está registrado.");
            } 
            if (errorBD.contains("alias_UNIQUE")) {
                errores.put("alias", "El alias ya está en uso.");
            }

            // Lanzar la excepción personalizada con el mapa listo para la vista
            throw new ExcepcionValidacionBD(errores);
        }*/


    }
    
    
    public void registrarNoAdministrador ( ModeloUsuario usuario, String contrasena ) throws Exception{
    
        try {
            
            /*
                Como No es administrador

                1. En la base de datos solo existira una empresa, por tanto se obtiene el id de ella

                2 encriptar la contraseña

                3 Crear el usuario asociandolo al id de la empresa 

                4 Modificar el codigo de acceso de la empresa

            */
        } 
        /*
        Ejemplo de manejo de la respuesta de la base de datos
        Dejamos comentado hasta que se haga la conexion a la base de datos
        catch (SQLIntegrityConstraintViolationException e) {
            
            String errorBD = e.getMessage();
            HashMap<String, String> errores = new HashMap<>();

            // Buscar palabras clave en el error de la base de datos
            if (errorBD.contains("correo_UNIQUE")) {
                errores.put("correo", "Este correo ya está registrado.");
            } 
            if (errorBD.contains("alias_UNIQUE")) {
                errores.put("alias", "El alias ya está en uso.");
            }

            // Lanzar la excepción personalizada con el mapa listo para la vista
            throw new ExcepcionValidacionBD(errores);
        }*/
        catch (Exception e) {
        }


    }
    
}

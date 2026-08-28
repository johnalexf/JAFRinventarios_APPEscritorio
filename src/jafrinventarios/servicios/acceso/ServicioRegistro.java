
package jafrinventarios.servicios.acceso;

import jafrinventarios.modelos.usuarios.ModeloUsuario;
import jafrinventarios.servicios.excepciones.ExcepcionValidacionBD;
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
    
        boolean respuestaBD = true;
        /*
            TODO
            Buscar en la base de datos que tipo de id es,
            Si es para un administrador existira un codigo de un unico uso
            que proveera el desarrollador, por tanto primero se consulta si hay
            usuarios registrados y si no hay se valida el codigo con uno escrito
            en estas lineas de codigo
        
            En dado caso que no sea para validar el codigo de un usuario administrador
            es decir que sea para un vendedor, entonces debera existir una unica empresa
            a la cual se va consultar el codigo de acceso que tiene en sus 
            atributos y se compara con este.
        
        */
        
        return respuestaBD;
    }
    
    
    
    public void registrarAdministrador ( ModeloUsuario usuario, String contrasena, String nombreEmpresa ) throws Exception{
    
        try {
            
            /*

                Dado que es un adminsitrador se creara la empresa primero

                1 Crear la empresa con el nombreEmpresa, aparte existira un metodo 
                    en Servicio autenticacion que creara un codigo aleatorio de 10 digitos  
                    se usara dicho metodo, para crear la empresa con el nombre y el codigo

                2 En el mismo servicio de autenticacion, existira un metodo para 
                    encriptar la contraseña

                3 Crear el usuario asociandolo al id de la empresa creada


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

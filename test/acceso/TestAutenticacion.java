
package acceso;

import jafrinventarios.DTOs.acceso.DTOCredenciales;
import jafrinventarios.servicios.acceso.ServicioAutenticacion;
import jafrinventarios.servicios.excepciones.ExcepcionValidacionBD;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 *
 * @author JOHN FORERO
 */
public class TestAutenticacion {
    
    @Test
    public void testInicioSesionExitoso(){
    
        ServicioAutenticacion servicioAuth = new ServicioAutenticacion();
        
        //Datos reales de un usuario habilitado en la base de datos local
        String correo = "johnalexfr@hotmail.com";
        String contrasena = "Forero12*";
        
        try {
            DTOCredenciales credenciales = servicioAuth.iniciarSesion(correo, contrasena);
            
            assertTrue("El Id del usuario debe ser mayor a 0", credenciales.getIdUsuario() > 0);
            
            System.out.println("Prueba de integracion exitosa. Rol obtenido : " + credenciales.getNombreRol());
            
        } catch (ExcepcionValidacionBD e) {
            fail( e.getMessage() + e.getErroresEnString() );
        } catch (Exception e) {
            fail("La prueba fallo, verificar la conexion a la base de datos. Error:" + e.getMessage());
        }
    }
}


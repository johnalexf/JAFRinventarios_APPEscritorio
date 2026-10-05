package usuarios;

import jafrinventarios.modelos.usuarios.ModeloUsuario;
import jafrinventarios.servicios.ConexionDB;
import jafrinventarios.servicios.excepciones.ExcepcionValidacionBD;
import jafrinventarios.servicios.usuarios.ServicioUsuarios;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.sql.Connection;

/**
 *
 * @author JOHN FORERO
 */
public class TestCRUDUsuarios {

    private ServicioUsuarios servicioUsuarios;
    private Connection conexion;

    /*
    Metodo para hacer una configuracion o preparar el ambiente de la prueba,
    el nombre setUp() es opcional pues puede llevar cualquier nombre, sin embargo
    debido a como se utilizaba antes y para referencias claras con otros 
    desarrolladores se prefiere usar setUP
    */
    @Before
    public void setUp() throws Exception {
        servicioUsuarios = new ServicioUsuarios();
        
        // Obtenemos la instancia centralizada de la conexión
        conexion = ConexionDB.getConnection();
        
        // DESACTIVAMOS el autoguardado. Aquí inicia la transacción temporal.
        conexion.setAutoCommit(false);
    }

    
    
    /*
    Metodo de limpieza de los cambios o pruebas que se hayan realizado, este se 
    ejecuta despues de terminar el test, de igual manera el nombre cumple las
    mismas caracteristicas mencioandas para setUp
    */
    @After
    public void tearDown() throws Exception {
        if (conexion != null) {
            // DESHACEMOS cualquier cambio (Insert, Update, Delete) en la BD.
            conexion.rollback();
            // Restauramos el comportamiento normal de la conexión para no afectar el resto del sistema.
            conexion.setAutoCommit(true);
        }
    }

    
    @Test
    public void testFlujoCompletoCRUDUsuarios() {
        try {
            // Instanciar: Preparamos un ModeloUsuario de ejemplo 
            // Usamos rol 2 ( Vendedor ) .
            ModeloUsuario usuarioNuevo = new ModeloUsuario(
                    null, // idUsuario (La BD lo autogenera)
                    1, // idEmpresa
                    "aliasTestQA", 
                    "3000000000", 
                    "testqa@jafr.com", 
                    "Juan", 
                    "Carlos", 
                    "Perez", 
                    "Gomez", 
                    "ClaveSegura123*", 
                    2, // idRolUsuario
                    true // habilitado
            );
            
            System.out.println("Usuario original instanciado en memoria : \n"+ usuarioNuevo.toString());

            
            //Insertar: Usamos el método estático que inserta y retorna el ID.
            int idGenerado = ServicioUsuarios.registrarUsuario(usuarioNuevo);
            assertTrue("El ID generado debe ser mayor a 0", idGenerado > 0);

            
            // Leer: Extraemos el usuario recién guardado de la BD temporal.
            ModeloUsuario usuarioLeido = servicioUsuarios.obtenerModeloUsuario(idGenerado);
            System.out.println("Usuario original traido desde la base de datos : \n"+ usuarioLeido.toString());
            
            assertNotNull("El usuario leído no debe ser nulo", usuarioLeido);
            assertTrue( "El usuario deberia ser igual al creado inicialmente", usuarioNuevo.equals(usuarioLeido));

            
            // Actualizar: Modificamos atributos usando el modelo leído y enviamos la actualización.
            usuarioLeido.setAliasUsuario("aliasModificado");
            usuarioLeido.setTelefonoUsuario("3111111111");
            servicioUsuarios.editarOtroUsuario(usuarioLeido);
            
            System.out.println("Usuario original modificado en memoria : \n"+ usuarioLeido.toString());

            
            // Verificar: Volvemos a leer para confirmar que el UPDATE tuvo efecto en memoria
            ModeloUsuario usuarioActualizado = servicioUsuarios.obtenerModeloUsuario(idGenerado);
            assertTrue( "El usuario editado deberia ser igual al editado inicialmente", usuarioLeido.equals(usuarioActualizado));

            System.out.println("Usuario modificado obtenido desde la base de datos : \n"+ usuarioLeido.toString());
            
            
            // Eliminar: Borramos el registro.
            servicioUsuarios.eliminarUsuario(idGenerado);

            
            // Confirmar borrado: Intentamos leerlo de nuevo. Debe lanzar la excepción controlada.
            try {
                servicioUsuarios.obtenerModeloUsuario(idGenerado);
                fail("La prueba falló. Se esperaba una excepción porque el usuario debió ser eliminado.");
            } catch (Exception e) {
                // Verificamos que el mensaje de la excepción sea el que se programo en el servicio.
                System.out.println(e.getMessage());
                assertTrue("El mensaje de error debe indicar que no existe", 
                        e.getMessage().contains("No existe un usuario con id"));
            }

            System.out.println("Prueba de integración CRUD de Usuarios finalizada con éxito.");

        } catch (ExcepcionValidacionBD e) {
            fail( e.getMessage() + e.getErroresEnString() );
        }  catch (Exception e) {
            fail("El flujo CRUD falló debido a una excepción inesperada: " + e.getMessage());
        }
    }
}
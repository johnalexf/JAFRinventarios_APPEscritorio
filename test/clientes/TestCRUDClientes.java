package clientes;

import jafrinventarios.modelos.clientes.ModeloCliente;
import jafrinventarios.servicios.ConexionDB;
import jafrinventarios.servicios.clientes.ServicioClientes;
import jafrinventarios.servicios.excepciones.ExcepcionValidacionBD;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.sql.Connection;

/**
 *
 * @author JOHN FORERO
 */
public class TestCRUDClientes {

    private ServicioClientes servicioClientes;
    private Connection conexion;

    
    @Before
    public void setUp() throws Exception {
        servicioClientes = new ServicioClientes();
        
        // Obtenemos la instancia centralizada de la conexión
        conexion = ConexionDB.getConnection();
        
        // DESACTIVAMOS el autoguardado. Aquí inicia la transacción temporal.
        conexion.setAutoCommit(false);
    }


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
    public void testFlujoCompletoCRUDClientes() {
        try {
            // Instanciar: Preparamos un ModeloCliente de ejemplo 
            ModeloCliente clienteNuevo = new ModeloCliente(
                    null,
                    "Cliente de prueba",
                    "Pepito",
                    "",
                    "Perez",
                    "",
                    "3000000000",
                    "Calle 145 # 23 - 65",
                    "clientePrueba@gmail.com",
                    true
            );

            System.out.println("Cliente original instanciado en memoria : \n"+ clienteNuevo.toString());

            
            //Insertar: Usamos el método que inserta y retorna el ID.
            int idGenerado = servicioClientes.crearCliente(clienteNuevo, true);
            clienteNuevo.setIdCliente(idGenerado);
            assertTrue("El ID generado debe ser mayor a 0", idGenerado > 0);
            
            System.out.println("Cliente original cargado a la base de datos \n");
            
            // Leer: Extraemos el cliente recién guardado de la BD.
            ModeloCliente clienteLeido = servicioClientes.obtenerModeloCliente(idGenerado);
            System.out.println("Cliente original traido desde la base de datos : \n"+ clienteLeido.toString());
            
            assertNotNull("El cliente leído no debe ser nulo", clienteLeido);
            assertTrue( "El cliente deberia ser igual al creado inicialmente", clienteNuevo.equals(clienteLeido));

            
            // Actualizar: Modificamos atributos usando el modelo leído y enviamos la actualización.
            clienteLeido.setCorreoCliente( "clienteEditado@gmail.com" );
            clienteLeido.setNombreNegocio("Cliente modificado");
            clienteLeido.setDireccionCliente( "Carrera 90 A 124 # 10" );
            System.out.println("Cliente original modificado en memoria : \n"+ clienteLeido.toString());
            
            servicioClientes.editarCliente(clienteLeido, true);
            System.out.println("Cliente modificado actualizado en la base de datos \n");

            
            // Verificar: Volvemos a leer para confirmar que el UPDATE tuvo efecto en memoria
            ModeloCliente clienteActualizado = servicioClientes.obtenerModeloCliente(idGenerado);
            assertTrue( "El cliente actualizado desde la base de datos debe ser igual al editado en memorita", 
                        clienteLeido.equals(clienteActualizado)
            );

            System.out.println("Cliente modificado obtenido desde la base de datos : \n"+ clienteActualizado.toString());
            
            
            // Eliminar: Borramos el registro.
            servicioClientes.eliminarCliente(idGenerado, true);

            
            // Confirmar borrado: Intentamos leerlo de nuevo. Debe lanzar la excepción controlada.
            try {
                servicioClientes.obtenerModeloCliente(idGenerado);
                fail("La prueba falló. Se esperaba una excepción porque el cliente debió ser eliminado.");
            } catch (Exception e) {
                // Verificamos que el mensaje de la excepción sea el que se programo en el servicio.
                System.out.println(e.getMessage() + "\n");
                assertTrue("El mensaje de error debe indicar que no existe", 
                        e.getMessage().contains("No existe un cliente con id"));
            }

            System.out.println("Prueba de integración CRUD de Clientes finalizada con éxito.");

        } catch (ExcepcionValidacionBD e) {
            fail( e.getMessage() + e.getErroresEnString() );
        }  catch (Exception e) {
            fail("El flujo CRUD falló debido a una excepción inesperada: " + e.getMessage());
        }
    }
}
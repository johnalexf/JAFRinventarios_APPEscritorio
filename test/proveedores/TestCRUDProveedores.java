package proveedores;

import jafrinventarios.modelos.proveedores.ModeloProveedor;
import jafrinventarios.servicios.ConexionDB;
import jafrinventarios.servicios.proveedores.ServicioProveedores;
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
public class TestCRUDProveedores {

    private ServicioProveedores servicioProveedores;
    private Connection conexion;

    
    @Before
    public void setUp() throws Exception {
        servicioProveedores = new ServicioProveedores();
        
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
    public void testFlujoCompletoCRUDProveedores() {
        try {
            // Instanciar: Preparamos un ModeloProveedor de ejemplo 
            ModeloProveedor proveedorNuevo = new ModeloProveedor(
                    null,
                    "Proveedor de prueba",
                    "Pepito",
                    "",
                    "Perez",
                    "",
                    "3000000000",
                    "Calle 145 # 23 - 65",
                    "proveedorPrueba@gmail.com",
                    true
            );

            System.out.println("Proveedor original instanciado en memoria : \n"+ proveedorNuevo.toString());

            
            //Insertar: Usamos el método que inserta y retorna el ID.
            int idGenerado = servicioProveedores.crearProveedor(proveedorNuevo);
            proveedorNuevo.setIdProveedor(idGenerado);
            assertTrue("El ID generado debe ser mayor a 0", idGenerado > 0);
            
            System.out.println("Proveedor original cargado a la base de datos \n");
            
            // Leer: Extraemos el proveedor recién guardado de la BD.
            ModeloProveedor proveedorLeido = servicioProveedores.obtenerModeloProveedor(idGenerado);
            System.out.println("Proveedor original traido desde la base de datos : \n"+ proveedorLeido.toString());
            
            assertNotNull("El proveedor leído no debe ser nulo", proveedorLeido);
            assertTrue( "El proveedor deberia ser igual al creado inicialmente", proveedorNuevo.equals(proveedorLeido));

            
            // Actualizar: Modificamos atributos usando el modelo leído y enviamos la actualización.
            proveedorLeido.setCorreoProveedor( "proveedorEditado@gmail.com" );
            proveedorLeido.setNombreComercial("Proveedor modificado");
            proveedorLeido.setDireccionProveedor( "Carrera 90 A 124 # 10" );
            System.out.println("Proveedor original modificado en memoria : \n"+ proveedorLeido.toString());
            
            servicioProveedores.editarProveedor(proveedorLeido);
            System.out.println("Proveedor modificado actualizado en la base de datos \n");

            
            // Verificar: Volvemos a leer para confirmar que el UPDATE tuvo efecto en memoria
            ModeloProveedor proveedorActualizado = servicioProveedores.obtenerModeloProveedor(idGenerado);
            assertTrue( "El proveedor actualizado desde la base de datos debe ser igual al editado en memoria", 
                        proveedorLeido.equals(proveedorActualizado)
            );

            System.out.println("Proveedor modificado obtenido desde la base de datos : \n"+ proveedorActualizado.toString());
            
            
            // Eliminar: Borramos el registro.
            servicioProveedores.eliminarProveedor(idGenerado);

            
            // Confirmar borrado: Intentamos leerlo de nuevo. Debe lanzar la excepción controlada.
            try {
                servicioProveedores.obtenerModeloProveedor(idGenerado);
                fail("La prueba falló. Se esperaba una excepción porque el proveedor debió ser eliminado.");
            } catch (Exception e) {
                // Verificamos que el mensaje de la excepción sea el que se programo en el servicio.
                System.out.println(e.getMessage() + "\n");
                assertTrue("El mensaje de error debe indicar que no existe", 
                        e.getMessage().contains("No existe un proveedor con id"));
            }

            System.out.println("Prueba de integración CRUD de Proveedores finalizada con éxito.");

        } catch (ExcepcionValidacionBD e) {
            fail( e.getMessage() + e.getErroresEnString() );
        }  catch (Exception e) {
            fail("El flujo CRUD falló debido a una excepción inesperada: " + e.getMessage());
        }
    }
}
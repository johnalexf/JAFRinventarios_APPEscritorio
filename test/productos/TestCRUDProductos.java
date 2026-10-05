package productos;

import jafrinventarios.modelos.productos.ModeloProducto;
import jafrinventarios.servicios.ConexionDB;
import jafrinventarios.servicios.productos.ServicioProductos;
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
public class TestCRUDProductos {

    private ServicioProductos servicioProductos;
    private Connection conexion;

    
    @Before
    public void setUp() throws Exception {
        servicioProductos = new ServicioProductos();
        
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
    public void testFlujoCompletoCRUDProductos() {
        try {
            // Instanciar: Preparamos un ModeloProducto de ejemplo 
            ModeloProducto productoNuevo = new ModeloProducto(
                    null,
                    1,
                    "Producto prueba",
                    10000.0,
                    12000.0,
                    200,
                    300,
                    true
            );

            System.out.println("Producto original instanciado en memoria : \n"+ productoNuevo.toString());

            
            //Insertar: Usamos el método que inserta y retorna el ID.
            int idGenerado = servicioProductos.crearProducto(productoNuevo, true);
            productoNuevo.setIdProducto(idGenerado);
            assertTrue("El ID generado debe ser mayor a 0", idGenerado > 0);
            
            System.out.println("Producto original cargado a la base de datos \n");
            
            // Leer: Extraemos el producto recién guardado de la BD.
            ModeloProducto productoLeido = servicioProductos.obtenerModeloProducto(idGenerado);
            System.out.println("Producto original traido desde la base de datos : \n"+ productoLeido.toString());
            
            assertNotNull("El producto leído no debe ser nulo", productoLeido);
            assertTrue( "El producto deberia ser igual al creado inicialmente", productoNuevo.equals(productoLeido));

            
            // Actualizar: Modificamos atributos usando el modelo leído y enviamos la actualización.
            productoLeido.setNombreProducto("Producto modifcado");
            productoLeido.setCantidadDisponible(100);
            productoLeido.setPrecioVenta(13000.0);
            System.out.println("Producto original modificado en memoria : \n"+ productoLeido.toString());
            
            servicioProductos.editarProducto(productoLeido, true);
            System.out.println("Producto modificado actualizado en la base de datos \n");

            
            // Verificar: Volvemos a leer para confirmar que el UPDATE tuvo efecto en memoria
            ModeloProducto productoActualizado = servicioProductos.obtenerModeloProducto(idGenerado);
            assertTrue( "El producto actualizado desde la base de datos debe ser igual al editado en memoria", 
                        productoLeido.equals(productoActualizado)
            );

            System.out.println("Producto modificado obtenido desde la base de datos : \n"+ productoActualizado.toString());
            
            
            // Eliminar: Borramos el registro.
            servicioProductos.eliminarProducto(idGenerado, true);

            
            // Confirmar borrado: Intentamos leerlo de nuevo. Debe lanzar la excepción controlada.
            try {
                servicioProductos.obtenerModeloProducto(idGenerado);
                fail("La prueba falló. Se esperaba una excepción porque el producto debió ser eliminado.");
            } catch (Exception e) {
                // Verificamos que el mensaje de la excepción sea el que se programo en el servicio.
                System.out.println(e.getMessage() + "\n");
                assertTrue("El mensaje de error debe indicar que no existe", 
                        e.getMessage().contains("No existe un producto con id"));
            }

            System.out.println("Prueba de integración CRUD de Productos finalizada con éxito.");

        } catch (ExcepcionValidacionBD e) {
            fail( e.getMessage() + e.getErroresEnString() );
        }  catch (Exception e) {
            fail("El flujo CRUD falló debido a una excepción inesperada: " + e.getMessage());
        }
    }
}
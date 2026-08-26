/*
 Este servicio es solo para un administrador, por tanto la vista 
nunca le mostrara los proveedores, es por esto que en ninguno de los metodos
se considera necesario el uso de la variable booleana como parametro
isAdministrador
 */
package jafrinventarios.servicios.proveedores;

import jafrinventarios.DTOs.proveedores.DTOProveedorTabla;
import jafrinventarios.modelos.proveedores.ModeloProveedor;
import java.util.ArrayList;
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
    public static LinkedHashMap<Integer, String> obtenerDiccionarioProveedores() throws Exception{
    
        //TODO Por el momento se hace la simulacion
        List<DTOProveedorTabla> proveedoresBD = simulacionConsultaBDTodosProveedores();
        
        LinkedHashMap<Integer, String> diccionarioProveedores = new LinkedHashMap<>();
        proveedoresBD.forEach( proveedor -> {
            diccionarioProveedores.put( proveedor.getIdProveedor() , proveedor.getNombreComercial() );
        });
        
        return diccionarioProveedores;
        
    }
    
    //TODO Esta es una simulacion cuando se conecte a la base de datos se eliminara
    private static List<DTOProveedorTabla> simulacionConsultaBDTodosProveedores () {
        List<DTOProveedorTabla> listaProveedoresTablas = new ArrayList<>();
        
        listaProveedoresTablas.add(new DTOProveedorTabla(1, "Panificadora El Trigo", "Carlos Alberto Ruiz", "3101234567", "Calle 45 # 12-34", "contacto@eltrigo.com", true));
        listaProveedoresTablas.add(new DTOProveedorTabla(2, "Pastelería Delicias", "María Fernanda Gómez", "3209876543", "Carrera 15 # 80-21", "ventas@delicias.com", true));
        listaProveedoresTablas.add(new DTOProveedorTabla(3, "Dulcería La Alegría", "Jorge Enrique Silva", "3154567890", "Calle 13 # 65-10", "pedidos@laalegria.com", true));
        listaProveedoresTablas.add(new DTOProveedorTabla(4, "Embutidos San Jorge", "Luis Ernesto Castro", "3003216549", "Avenida Boyacá # 72-15", "distribucion@sanjorge.com", true));
        listaProveedoresTablas.add(new DTOProveedorTabla(5, "Lácteos La Sabana", "Ana Lucía Martínez", "3112223344", "Autopista Norte # 150-20", "logistica@lacteossabana.com", true));
        listaProveedoresTablas.add(new DTOProveedorTabla(6, "Bebidas y Refrescos S.A.", "Andrés Felipe Rojas", "3145556677", "Carrera 30 # 45-67", "pedidos@bebidas.com", true));
        listaProveedoresTablas.add(new DTOProveedorTabla(7, "Snacks El Dorado", "Carmen Rosa Pérez", "3167778899", "Calle 80 # 68-14", "ventas@eldorado.com", true));
        listaProveedoresTablas.add(new DTOProveedorTabla(8, "Plásticos y Empaques", "Javier Alfonso López", "3189990011", "Calle 19 # 22-10", "ventas@empaques.com", true));
        listaProveedoresTablas.add(new DTOProveedorTabla(9, "Salsas La Casera", "Diana Carolina Toro", "3123459876", "Carrera 68 # 12-45", "distribucion@lacasera.com", false));
        listaProveedoresTablas.add(new DTOProveedorTabla(10, "Abarrotes El Mayorista", "Pedro Pablo Sánchez", "3015674321", "Zona Industrial Bodega 15", "contacto@elmayorista.com", false));

        return listaProveedoresTablas;
    }
    
    
    public List<DTOProveedorTabla> obtenerTodosLosProveedores( ) throws Exception{
    
        /*
        TODO: Realizar la consulta y el armado de la lista
        por el momento se simula una lista
        */
        
        return simulacionConsultaBDTodosProveedores();
    
    }
    
    
    public List<DTOProveedorTabla> obtenerListaProveedoresPorFiltro ( String filtro ) throws Exception {
    
        List<DTOProveedorTabla> listaProveedores = new ArrayList<>();
        /*
        TODO:
        Hacer la consulta a la base de datos, si no se encuentra algun resultado
        la lista se manda vacia, si llega a presentarse algun error manejarlo con
        try catch y crear el throw new Excepcion para globalizar si es alguna
        falla de conexion a la base de datos
        */
        
        return listaProveedores;
    }
    
    
    /*
    Metodo para obtener un solo DTOProveedorTabla, sera utilizado en dado caso que
    se necesite obtener los datos de un proveedor en especifico cuando el usuario
    haya cambiado sus valores o haya creado uno nuevo
    */
    public DTOProveedorTabla obtenerDatosDTOProveedor ( int idProveedor ) throws Exception{
    
        /*
        TODO
        Si no se encuentra un usuario con el id, devolver el error con throw new
        de igual manera si pasa algun error en la conexion
        Por el momento se simula un resultado
        */
        
        DTOProveedorTabla proveedorConsultado = new DTOProveedorTabla(10, "Abarrotes El Mayorista", "Pedro Pablo Sánchez", "3015674321", "Zona Industrial Bodega 15", "contacto@elmayorista.com", false);
        
        return proveedorConsultado;
        
    }
    
    
    /*
    Metodo para entregar un ModeloProveedor, destinado unicamente para poder editarlo
    segun la solucion para mostrar una lista usamos el DTO y no el modelo.
    */
    public ModeloProveedor obtenerModeloProveedor ( int idProveedor ) throws Exception{
    
        /*
        TODO
        hacer la consulta, si no se encuentra devolver un error
        si pasa algun error de conexion se devuelve un error diferente
        Por el momento se hace una simulacion de resultado
        */
        
        ModeloProveedor proveedorConsultado = 
                new ModeloProveedor (
                        10, "Abarrotes El Mayorista", "Pedro", "Pablo", "Sánchez", "", "3015674321", "Zona Industrial Bodega 15", "contacto@elmayorista.com", true);
        
        return proveedorConsultado;
        
    }
    
    
    
    public void editarProveedor ( ModeloProveedor proveedor ) throws Exception{
    
        /*
        TODO
        
        Para editar se tendra encuenta el id para saber que proveedor es el que hay que modificar
        y se actualizaran los demas datos que contenga el proveedor
        
        Manejar try catch para controlar errores de respuesta de la base de datos con
        catch (SQLIntegrityConstraintViolationException e) {
            
            String errorBD = e.getMessage();
            HashMap<String, String> errores = new HashMap<>();

            // Buscar palabras clave en el error de la base de datos
            if (errorBD.contains("nombreComercial_UNIQUE")) {
                errores.put("nombreComercial", "Ya está registrado este nombre.");
            } 

            // Lanzar la excepción personalizada con el mapa listo para la vista
            throw new ExcepcionValidacionBD(errores);
        }
        */
        
        
    }
    
    
    /*
    Metodo para conmutar el estado de habilitado de un proveedor
    */
    public void conmutarEstadoProveedor( int idProveedor ) throws Exception {
    
    
        /*
        TODO
        con el idProveedor verificamos que valor tiene el parametro habilitado
        y lo conmutamos
        
        Manejar los errores 
        */
    }
    
    
    /*
    Metodo para consultar si un proveedor esta habilitado
    */
    public boolean isProveedorHabilitado ( int idProveedor ) throws Exception{
    
        /*
        TODO : pendiente la respectiva consulta con manejo de errores
        por el momento se simula con true;
        */
        
        return true;
    }
    
    
    /*
    Metodo para crear un proveedor, 
    se ignora el id pues este valor se lo asigna la base de datos
    el cual hay que retornar.
    */
    public int crearProveedor ( ModeloProveedor proveedor ) throws Exception{
    
        /*
        TODO:

        Manejo de errores con 
        catch (SQLIntegrityConstraintViolationException e) {
        si hay datos duplicados en la base de datos o que no correspondan
        
        y con catch (Exception e) { para otros errores
        
        por el momento retornamos -1 para indicar que no se creo el usuario
        sin embargo la idea es manejarlo con los errores throw new
        */
        return -1;
    }
    
    
    /*
    Metodo para eliminar un proveedor.
    Pero si este tiene alguna relacion con otra tabla no se puede eliminar
    */
    public void eliminarProveedor ( int idProveedor ) throws Exception{
        
        /*
        TODO:
        Hacer la respectiva consulta y envio de errores segun el caso.
        */
    }
    
    
    /*
    Metodo para consultar si un proveedor hace parte del registro de otra tabla
    */
    public boolean tieneRegistrosAsociados ( int idProveedor ) throws Exception{
    
        /*
        TODO
        Hacer la consulta y manejo de errores para enviarlos personalizados
        segun corresponda
        
        Por el momento se simula que no tiene registros asociados
        */
        
        return false;
    }
    
}

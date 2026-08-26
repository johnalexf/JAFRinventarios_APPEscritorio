/*
La seccion clientes la puede ver cualquier usuarios,
sin embargo solo los administradores pueden hacer todas las operaciones CRUD,
mientras que los demas solo pueden leer los que estan habilitados,
por consiguiente para personalizar la consulta y mantener unos permisos
se usa una variable isAdministrador que debe enviar el controlador
 */
package jafrinventarios.servicios.clientes;

import jafrinventarios.DTOs.clientes.DTOClienteTabla;
import jafrinventarios.modelos.clientes.ModeloCliente;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author JOHN FORERO
 */
public class ServicioClientes {

    
    public ServicioClientes() {
    }
    
    /*
    Metodo estatico para no instanciar el servicio para los controladores que 
    solo necesitan de esta funcion
    */
    public static LinkedHashMap<Integer, String> obtenerDiccionarioClientes() throws Exception{
    
        //TODO Por el momento se hace la simulacion
        List<DTOClienteTabla> clientesBD = simulacionConsultaBDTodosClientes();
        
        LinkedHashMap<Integer, String> diccionarioClientes = new LinkedHashMap<>();
        clientesBD.forEach( cliente -> {
            diccionarioClientes.put( cliente.getIdCliente() , cliente.getNombreNegocio());
        });
        
        return diccionarioClientes;
        
    }
    
    //TODO Esta es una simulacion cuando se conecte a la base de datos se eliminara
    private static List<DTOClienteTabla> simulacionConsultaBDTodosClientes () {
        List<DTOClienteTabla> listaClientesTablas = new ArrayList<>();
        
        listaClientesTablas.add(new DTOClienteTabla(1, "Tienda La Esperanza", "José Antonio Vargas", "3109998877", "Calle 22 # 14-50", "contacto@laesperanza.com", true));
        listaClientesTablas.add(new DTOClienteTabla(2, "Minimercado El Primo", "Marta Cecilia Rojas", "3201112233", "Carrera 50 # 100-24", "elprimo@minimercado.com", true));
        listaClientesTablas.add(new DTOClienteTabla(3, "Panadería La Mejor", "Luis Fernando Muñoz", "3156667788", "Avenida 68 # 45-12", "lamejorpanaderia@gmail.com", true));
        listaClientesTablas.add(new DTOClienteTabla(4, "Cigarrería El Punto", "Blanca Stella Gómez", "3004445566", "Calle 80 # 72-10", "elpuntocigarreria@hotmail.com", true));
        listaClientesTablas.add(new DTOClienteTabla(5, "Surtimax Las Orquídeas", "Carlos Eduardo López", "3112229988", "Carrera 15 # 120-45", "surtilasorquideas@empresa.com", true));
        listaClientesTablas.add(new DTOClienteTabla(6, "Tienda Mi Casita", "Ana María Castro", "3148885522", "Calle 13 # 25-60", "micasitatienda@gmail.com", true));
        listaClientesTablas.add(new DTOClienteTabla(7, "Bodega Mayorista Sur", "Roberto Carlos Díaz", "3173334455", "Autopista Sur # 45-90", "bodegasur@distribucion.com", true));
        listaClientesTablas.add(new DTOClienteTabla(8, "Minimercado Los Pinos", "Sandra Milena Ruiz", "3012223344", "Transversal 91 # 130-15", "lospinos@minimercado.com", true));
        listaClientesTablas.add(new DTOClienteTabla(9, "Tienda Escolar San Juan", "Mario Alberto Silva", "3125556677", "Carrera 7 # 45-10", "sanjuanescolar@gmail.com", false));
        listaClientesTablas.add(new DTOClienteTabla(10, "Cafetería Central", "Diana Patricia Ortiz", "3169990000", "Calle 100 # 15-20", "centralcafeteria@empresa.com", false));
        
        return listaClientesTablas;
    }
    
    
    public List<DTOClienteTabla> obtenerTodosLosClientes( boolean isAdministrador ) throws Exception{
    
        /*
        TODO: Realizar la consulta y el armado de la lista
        por el momento se simula una lista
        */
        
        return simulacionConsultaBDTodosClientes();
    
    }
    
    
    public List<DTOClienteTabla> obtenerListaClientesPorFiltro ( String filtro , boolean isAdministrador ) throws Exception {
    
        List<DTOClienteTabla> listaClientes = new ArrayList<>();
        /*
        TODO:
        Hacer la consulta a la base de datos, si no se encuentra algun resultado
        la lista se manda vacia, si llega a presentarse algun error manejarlo con
        try catch y crear el throw new Excepcion para globalizar si es alguna
        falla de conexion a la base de datos
        */
        
        return listaClientes;
    }
    
    
    /*
    Metodo para obtener un solo DTOClienteTabla, sera utilizado en dado caso que
    se necesite obtener los datos de un cliente en especifico cuando el usuario
    haya cambiado sus valores o haya creado uno nuevo
    */
    public DTOClienteTabla obtenerDatosDTOCliente ( int idCliente ) throws Exception{
    
        /*
        TODO
        Si no se encuentra un usuario con el id, devolver el error con throw new
        de igual manera si pasa algun error en la conexion
        Por el momento se simula un resultado
        */
        
        DTOClienteTabla clienteConsultado = new DTOClienteTabla(10, "Cafetería Central", "Diana Patricia Ortiz", "3169990000", "Calle 100 # 15-20", "centralcafeteria@empresa.com", true);
        
        return clienteConsultado;
        
    }
    
    
    /*
    Metodo para entregar un ModeloCliente, destinado unicamente para poder editarlo
    segun la solucion para mostrar una lista usamos el DTO y no el modelo.
    */
    public ModeloCliente obtenerModeloCliente ( int idCliente ) throws Exception{
    
        /*
        TODO
        hacer la consulta, si no se encuentra devolver un error
        si pasa algun error de conexion se devuelve un error diferente
        Por el momento se hace una simulacion de resultado
        */
        
        ModeloCliente clienteConsultado = 
                new ModeloCliente(
                   10, "Cafetería Central", "Diana", "Patricia", "Ortiz", "", "3169990000", "Calle 100 # 15-20", "centralcafeteria@empresa.com", true);
        
        return clienteConsultado;
        
    }
    
    
    
    public void editarCliente ( ModeloCliente cliente , boolean isAdministrador ) throws Exception{
    
        /*
        TODO
        
        Para editar se tendra encuenta el id para saber que cliente es el que hay que modificar
        y se actualizaran los demas datos que contenga el cliente
        
        Manejar try catch para controlar errores de respuesta de la base de datos con
        catch (SQLIntegrityConstraintViolationException e) {
            
            String errorBD = e.getMessage();
            HashMap<String, String> errores = new HashMap<>();

            // Buscar palabras clave en el error de la base de datos
            if (errorBD.contains("nombreNegocio_UNIQUE")) {
                errores.put("nombreNegocio", "Ya está registrado este nombre.");
            } 

            // Lanzar la excepción personalizada con el mapa listo para la vista
            throw new ExcepcionValidacionBD(errores);
        }
        */
        
        
    }
    
    
    /*
    Metodo para conmutar el estado de habilitado de un cliente
    */
    public void conmutarEstadoCliente( int idCliente, boolean isAdministrador ) throws Exception {
    
    
        /*
        TODO
        con el idCliente verificamos que valor tiene el parametro habilitado
        y lo conmutamos
        
        Manejar los errores 
        */
    }
    
    
    /*
    Metodo para consultar si un cliente esta habilitado
    */
    public boolean isClienteHabilitado ( int idCliente ) throws Exception{
    
        /*
        TODO : pendiente la respectiva consulta con manejo de errores
        por el momento se simula con true;
        */
        
        return true;
    }
    
    
    /*
    Metodo para crear un cliente, 
    se ignora el id pues este valor se lo asigna la base de datos
    el cual hay que retornar.
    */
    public int crearCliente ( ModeloCliente cliente , boolean isAdministrador) throws Exception{
    
        /*
        TODO:
        Se tiene en cuenta si es administrador para permitirle ejecutar la accion
        
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
    Metodo para eliminar un cliente.
    Pero si este tiene alguna relacion con otra tabla no se puede eliminar
    */
    public void eliminarCliente ( int idCliente, boolean isAdministrador ) throws Exception{
        
        /*
        TODO:
        Hacer la respectiva consulta y envio de errores segun el caso.
        */
    }
    
    
    /*
    Metodo para consultar si un cliente hace parte del registro de otra tabla
    */
    public boolean tieneRegistrosAsociados ( int idCliente ) throws Exception{
    
        /*
        TODO
        Hacer la consulta y manejo de errores para enviarlos personalizados
        segun corresponda
        
        Por el momento se simula que no tiene registros asociados
        */
        
        return false;
    }
}

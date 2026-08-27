/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jafrinventarios.servicios.productos;

import jafrinventarios.DTOs.productos.DTOProductoTabla;
import jafrinventarios.modelos.productos.ModeloProducto;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author JOHN FORERO
 */
public class ServicioProductos {

    public ServicioProductos() {
    }
    
        /*
    Metodo estatico para no instanciar el servicio para los controladores que 
    solo necesitan de esta funcion
    */
    public static LinkedHashMap<Integer, String> obtenerDiccionarioProductos() throws Exception{
    
        //TODO Por el momento se hace la simulacion
        List<DTOProductoTabla> productosBD = simulacionConsultaBDTodosProductos();
        
        LinkedHashMap<Integer, String> diccionarioProductos = new LinkedHashMap<>();
        productosBD.forEach( producto -> {
            diccionarioProductos.put( producto.getIdProducto(), producto.getNombreProducto());
        });
        
        return diccionarioProductos;
        
    }
    
    //TODO Esta es una simulacion cuando se conecte a la base de datos se eliminara
    private static List<DTOProductoTabla> simulacionConsultaBDTodosProductos () {
        List<DTOProductoTabla> listaProductosTablas = new ArrayList<>();
        
        listaProductosTablas.add(new DTOProductoTabla(1, "Pan Tajado Blanco (Bolsa)", 2500.0, 3500.0, 20, 50, true, "Panificadora El Trigo"));
        listaProductosTablas.add(new DTOProductoTabla(2, "Tostadas de Ajo (Paquete)", 1800.0, 2500.0, 15, 30, true, "Panificadora El Trigo"));
        listaProductosTablas.add(new DTOProductoTabla(3, "Pastelito de Guayaba (Unidad)", 800.0, 1200.0, 50, 120, true, "Pastelería Delicias"));
        listaProductosTablas.add(new DTOProductoTabla(4, "Croissant de Queso", 1200.0, 2000.0, 30, 80, true, "Panificadora El Trigo"));
        listaProductosTablas.add(new DTOProductoTabla(5, "Chupetas de Fresa (Paquete x50)", 6000.0, 8500.0, 10, 25, true, "Dulcería La Alegría"));
        listaProductosTablas.add(new DTOProductoTabla(6, "Gomas de Oso (Caja x24)", 12000.0, 18000.0, 5, 18, true, "Dulcería La Alegría"));
        listaProductosTablas.add(new DTOProductoTabla(7, "Salchichón Cervecero (Unidad)", 7000.0, 10000.0, 15, 40, true, "Embutidos San Jorge"));
        listaProductosTablas.add(new DTOProductoTabla(8, "Salchichón Tradicional (Mitad)", 4000.0, 5500.0, 20, 0, false, "Embutidos San Jorge"));
        listaProductosTablas.add(new DTOProductoTabla(9, "Torta de Vainilla (Porción)", 1500.0, 2500.0, 10, 5, true, "Pastelería Delicias"));
        listaProductosTablas.add(new DTOProductoTabla(10, "Galletas de Mantequilla (Caja)", 3500.0, 5000.0, 15, 2, false, "Pastelería Delicias"));
        
        return listaProductosTablas;
    }
    
    
    public List<DTOProductoTabla> obtenerTodosLosProductos( boolean isAdministrador ) throws Exception{
    
        /*
        TODO: Realizar la consulta y el armado de la lista
        por el momento se simula una lista
        
        si no es administrador se devuelven todos los habilitados
        ya que a este tipo de usuarios esos productos no es necesario mostrarlos
        */
        
        return simulacionConsultaBDTodosProductos();
    
    }
    
    
    public List<DTOProductoTabla> obtenerListaProductosPorFiltro ( String filtro , boolean isAdministrador ) throws Exception {
    
        List<DTOProductoTabla> listaProductos = new ArrayList<>();
        /*
        TODO:
        Hacer la consulta a la base de datos, si no se encuentra algun resultado
        la lista se manda vacia, si llega a presentarse algun error manejarlo con
        try catch y crear el throw new Excepcion para globalizar si es alguna
        falla de conexion a la base de datos
        */
        
        return listaProductos;
    }
    
    
    /*
    Metodo para obtener un solo DTOProductoTabla, sera utilizado en dado caso que
    se necesite obtener los datos de un producto en especifico cuando el usuario
    haya cambiado sus valores o haya creado uno nuevo
    */
    public DTOProductoTabla obtenerDatosDTOProducto ( int idProducto ) throws Exception{
    
        /*
        TODO
        Si no se encuentra un usuario con el id, devolver el error con throw new
        de igual manera si pasa algun error en la conexion
        Por el momento se simula un resultado
        */
        
        DTOProductoTabla productoConsultado = new DTOProductoTabla(10, "Galletas de Mantequilla (Caja)", 3500.0, 5000.0, 15, 2, false, "Pastelería Delicias");
        
        return productoConsultado;
        
    }
    
    
    /*
    Metodo para entregar un ModeloProducto, destinado unicamente para poder editarlo
    segun la solucion para mostrar una lista usamos el DTO.
    */
    public ModeloProducto obtenerModeloProducto ( int idProducto ) throws Exception{
    
        /*
        TODO
        hacer la consulta, si no se encuentra devolver un error
        si pasa algun error de conexion se devuelve un error diferente
        Por el momento se hace una simulacion de resultado
        */
        
        ModeloProducto productoConsultado = 
                new ModeloProducto(
                    10, 1, "Galletas de Mantequilla (Caja)", 3500.0, 5000.0, 15, 2, true
                );
        
        return productoConsultado;
        
    }
    
    
    
    public void editarProducto ( ModeloProducto producto, boolean isAdministrador ) throws Exception{
    
        /*
        TODO
        La seccion productos se le puede mostrar a cualquier usuario
        pero solo el administrador puede crear y editar usuario
        Por seguridad para blindar en dado caso que el boton de crear y editar
        se le muestren a todos los usuarios, se solicita en los argumentos
        si es un usuario administrado, si no se devuelve 
        un error : Permisos denegados para este usuario, solo el adminsitrador puede editar
        
        Para editar se tendra encuenta el id para saber que producto es el que hay que modificar
        y se actualizaran los demas datos que contenga el producto
        
        Manejar try catch para controlar errores de respuesta de la base de datos con
        catch (SQLIntegrityConstraintViolationException e) {
            
            String errorBD = e.getMessage();
            HashMap<String, String> errores = new HashMap<>();

            // Buscar palabras clave en el error de la base de datos
            if (errorBD.contains("nombreProducto_UNIQUE")) {
                errores.put("nombreProducto", "Ya está registrado este nombre.");
            } 
            
            si es que llega un valor negativo en las cantidades, verificar, de
            igual manera se va configurar tanto la base de datos como la vista
            para que no permita capturar numeros negativos

            // Lanzar la excepción personalizada con el mapa listo para la vista
            throw new ExcepcionValidacionBD(errores);
        }
        */
        
        
    }
    
    
    /*
    Metodo para conmutar el estado de habilitado de un producto
    */
    public void conmutarEstadoProducto( int idProducto, boolean isAdministrador ) throws Exception {
    
    
        /*
        TODO
        con el idProducto verificamos que valor tiene el parametro habilitado
        y lo conmutamos
        
        Manejar los errores 
        */
    }
    
    
    /*
    Metodo para consultar si un producto esta habilitado
    */
    public boolean isProductoHabilitado ( int idProducto ) throws Exception{
    
        /*
        TODO : pendiente la respectiva consulta con manejo de errores
        por el momento se simula con true;
        */
        
        return true;
    }
    
    
    /*
    Metodo para crear un producto, 
    se ignora el id pues este valor se lo asigna la base de datos
    el cual hay que retornar.
    */
    public int crearProducto ( ModeloProducto producto, boolean isAdministrador ) throws Exception{
    
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
    Metodo para eliminar un producto.
    Pero si este tiene alguna relacion con otra tabla no se puede eliminar
    */
    public void eliminarProducto ( int idProducto , boolean isAdministrador ) throws Exception{
        
        /*
        TODO:
        Hacer la respectiva consulta y envio de errores segun el caso.
        */
    }
    
    
    /*
    Metodo para consultar si un producto hace parte del registro de otra tabla
    */
    public boolean isProductoEliminable ( int idProducto ) throws Exception{
    
        /*
        TODO
        Hacer la consulta y manejo de errores para enviarlos personalizados
        segun corresponda
        
        Por el momento se simula que no tiene registros asociados
        */
        
        return false;
    }
    
}

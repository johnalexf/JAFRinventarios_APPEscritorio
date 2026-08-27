/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jafrinventarios.controladores.productos;

import jafrinventarios.controladores.utilidades.ControladorBusquedaYAccionLibre;
import jafrinventarios.controladores.utilidades.FuncionesBusquedaYAccionLibre;
import jafrinventarios.servicios.productos.ServicioProductos;
import jafrinventarios.vistas.productos.FilaTablaProductos;
import jafrinventarios.vistas.productos.ProductosPanel;
import java.util.LinkedHashMap;

/**
 *
 * @author JOHN FORERO
 */
public class ControladorProductos {
    
    private final ProductosPanel panelProductos;
    private final ServicioProductos servicioProductos;

    /*
    Diccionario con cada fila de cada registro de productos, permite:
    * Buscar el boton para asignarle la funcion de editar
    * Buscar la fila de un registro que se edito
    * Elimnar la fila si el registro se elimino
    */
    private LinkedHashMap<Integer, FilaTablaProductos> tablaProductos;
    
    /*
    ============================================================================
                        CONSTRUCTOR PUBLICO
    ============================================================================
    */
    public ControladorProductos(ProductosPanel panelProductos, ServicioProductos servicioProductos) {
        this.panelProductos = panelProductos;
        this.servicioProductos = servicioProductos;
        
        /*
        Instanciar el controlador de la barra de busqueda y boton de accion libre
        pasando como parametro la instancia de la interfaz que permite asignar
        las funciones correspondientes que necesita ejecutar el controlador de 
        la barra de busqueda.
        */
        new ControladorBusquedaYAccionLibre(
                panelProductos.getPanelBusquedaYAccionLibre(),
                funcionesBusquedaYAccionLibre(),
                "Id, nombre o proveedor",
                "Agregar Nuevo producto"
        );
        
        
    }
    
    
    /* 
    Metodo para crear la instancia de la interfaz FuncionesBusquedaYAccionLibre
    que contiene el metodo para poder buscar segun un filtro
    y para ejecutar la accion libre (crear producto).   
    */
    private FuncionesBusquedaYAccionLibre funcionesBusquedaYAccionLibre(){
        return new FuncionesBusquedaYAccionLibre() {
            
            @Override
            public boolean ejecutarBusqueda(String terminoBusqueda) {
                //TODO Hacer la funcion de busqueda
                // return procesarBusqueda( terminoBusqueda )
                return false;
            }
            
            @Override
            public void limpiarBusqueda(){
                //TODO reiniciarTabla();
            }

            @Override
            public void ejecutarAccionLibre() {
                // TODO crearProducto();
            }
            
        };
    }
    
    
    
}

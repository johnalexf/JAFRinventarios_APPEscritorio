/*
Esta clase es el consolidado a utilizar para recolectar la informacion
ya sea de compras o ventas.
*/
package jafrinventarios.DTOs.reportes.transacciones;

import java.util.LinkedHashMap;

/**
 *
 * @author JOHN FORERO
 */
public class DTOConsolidadoTransacciones {

    private double precioTotalTransacciones;
    private int cantidadTransacciones;
    private int cantidadProductos;

    private LinkedHashMap<Integer, DTOTerceroComercial> terceros;
    
    
    /*
    ============================================================================
                                CONSTRUCTOR
    ============================================================================
    */

    public DTOConsolidadoTransacciones() {
        this.precioTotalTransacciones = 0.0;
        this.cantidadTransacciones = 0;
        this.cantidadProductos = 0;
        this.terceros = new LinkedHashMap<>();
    }
    
    
    /*
    ============================================================================
                                    GETTERS
    ============================================================================
    */

    public double getPrecioTotalTransacciones() {
        return precioTotalTransacciones;
    }

    public int getCantidadTransacciones() {
        return cantidadTransacciones;
    }

    public int getCantidadProductos() {
        return cantidadProductos;
    }

    public LinkedHashMap<Integer, DTOTerceroComercial> getTerceros() {
        return terceros;
    }
    
    public int getCantidadTerceros(){
        return terceros.size();
    }
    
    
    /*
    ============================================================================
                                    SETTERS
    ============================================================================
    */

    public void setPrecioTotalTransacciones(double precioTotalTransacciones) {
        this.precioTotalTransacciones = precioTotalTransacciones;
    }

    public void setCantidadTransacciones(int cantidadTransacciones) {
        this.cantidadTransacciones = cantidadTransacciones;
    }

    public void setCantidadProductos(int cantidadProductos) {
        this.cantidadProductos = cantidadProductos;
    }
    
    
    /*
    ============================================================================
                           Agregar tercero comercial
    ============================================================================
    */
    public void agregarTerceroComercial( Integer idTercero, DTOTerceroComercial tercero ){
        terceros.put(idTercero, tercero);
    }
    
    
    /*
    ============================================================================
                           Agregar producto
    ============================================================================
    */
    public void agregarProducto( Integer idTercero, DTOProductoTransacciones producto ){
        if( terceros.containsKey(idTercero) ){
            terceros.get(idTercero).agregarProducto(producto);
        }
    }
    
    
    
}

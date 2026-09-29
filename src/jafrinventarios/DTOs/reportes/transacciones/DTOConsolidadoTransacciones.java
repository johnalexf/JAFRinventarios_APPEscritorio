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
    
    public boolean isEmpty(){
        return terceros.isEmpty();
    }
    
    /*
    ============================================================================
                                    SETTERS
    ============================================================================
    */

    public void setCantidadTransacciones(int cantidadTransacciones) {
        this.cantidadTransacciones = cantidadTransacciones;
    }
    
    public boolean existeTerceroComercial( Integer idTercero ){
        return terceros.containsKey(idTercero);
    }
    
    /*
    ============================================================================
                 Inicializar y agregar informacion a tercero comercial
    ============================================================================
    */
    public void inicializarTerceroComercial( Integer idTercero ){
        terceros.put( idTercero, new DTOTerceroComercial() );
    }
    
    public void agregarInformacionTercero( Integer idTercero, String nombre, int cantidadTransacciones ){
        if( existeTerceroComercial(idTercero) ){
            DTOTerceroComercial tercero = terceros.get( idTercero );
            tercero.setNombre(nombre);
            tercero.setCantidadTransacciones(cantidadTransacciones);
        } 
    }
    
    
    /*
    ============================================================================
                           Agregar producto
    ============================================================================
    */
    public void agregarProducto( Integer idTercero, DTOProductoTransacciones producto ){
        if( existeTerceroComercial(idTercero) ){
            terceros.get(idTercero).agregarProducto(producto);
            cantidadProductos++;
            precioTotalTransacciones += producto.getTotalPrecio();
        }
    }
    
    
    
}

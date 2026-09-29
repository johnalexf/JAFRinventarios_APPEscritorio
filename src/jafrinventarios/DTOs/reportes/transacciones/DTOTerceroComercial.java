
package jafrinventarios.DTOs.reportes.transacciones;

import java.util.ArrayList;

/**
 *
 * @author JOHN FORERO
 */
public class DTOTerceroComercial {
    
    private String nombre;
    private int cantidadTransacciones;
    private double precioTotalTransacciones;
    private ArrayList<DTOProductoTransacciones> productos;
    
    
    /*
    ============================================================================
                                CONSTRUCTOR
    ============================================================================
    */

    public DTOTerceroComercial() {
        this.nombre = "";
        this.cantidadTransacciones = 0;
        this.precioTotalTransacciones = 0.0;
        this.productos = new ArrayList<>();
    }
    
    /*
    ============================================================================
                                GETTERS
    ============================================================================
    */

    public String getNombre() {
        return nombre;
    }

    public int getCantidadTransacciones() {
        return cantidadTransacciones;
    }

    public double getPrecioTotalTransacciones() {
        return precioTotalTransacciones;
    }

    public ArrayList<DTOProductoTransacciones> getProductos() {
        return productos;
    }
    
    public int getCantidadProductos(){
        return productos.size();
    }
    
    
    /*
    ============================================================================
                                SETTERS
    ============================================================================
    */

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCantidadTransacciones(int cantidadTransacciones) {
        this.cantidadTransacciones = cantidadTransacciones;
    }

    
    /*
    ============================================================================
                            Agregar un producto
    ============================================================================
    */
    public void agregarProducto( DTOProductoTransacciones producto ){
        productos.add(producto);
        precioTotalTransacciones += producto.getTotalPrecio();
    }
    
    
    
    
}

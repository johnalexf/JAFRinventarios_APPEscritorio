
package jafrinventarios.DTOs.reportes.transacciones;

/**
 *
 * @author JOHN FORERO
 */
public class DTOProductoTransacciones {
    
    private Integer idProducto;
    private String nombreProducto;
    private double precio;
    private int cantidad;
    private double totalPrecio;
    
    /*
    ============================================================================
                                CONSTRUCTOR
    ============================================================================
    */

    public DTOProductoTransacciones(Integer idProducto, String nombreProducto, double precio, int cantidad, double totalPrecio) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.precio = precio;
        this.cantidad = cantidad;
        this.totalPrecio = totalPrecio;
    }
    
    
    /*
    ============================================================================
                                GETTERS
    ============================================================================
    */

    public Integer getIdProducto() {
        return idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public double getPrecio() {
        return precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getTotalPrecio() {
        return totalPrecio;
    }
    
    
    
}

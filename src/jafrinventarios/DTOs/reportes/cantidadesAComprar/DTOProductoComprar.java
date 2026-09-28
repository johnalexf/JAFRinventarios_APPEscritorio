
package jafrinventarios.DTOs.reportes.cantidadesAComprar;

/**
 *
 * @author JOHN FORERO
 */
public class DTOProductoComprar {
    
    private Integer idProducto;
    private String nombreProducto;
    private double precioCompra;
    private int cantidadesAComprar;
    
        
    /*
    ============================================================================
                                CONSTRUCTOR
    ============================================================================
    */

    public DTOProductoComprar(Integer idProducto, String nombreProducto, double precioCompra, int cantidadesAComprar) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.precioCompra = precioCompra;
        this.cantidadesAComprar = cantidadesAComprar;
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

    public double getPrecioCompra() {
        return precioCompra;
    }

    public int getCantidadesAComprar() {
        return cantidadesAComprar;
    }
    
    
    
}

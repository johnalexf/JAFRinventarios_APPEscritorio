
package jafrinventarios.DTOs.productos;

/**
 *
 * @author JOHN FORERO
 */
public class DTOProductoPrecio {
    
    private String nombreProducto;
    private double precioProducto;

    public DTOProductoPrecio(String nombreProducto, double precioProducto) {
        this.nombreProducto = nombreProducto;
        this.precioProducto = precioProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public double getPrecioProducto() {
        return precioProducto;
    }
    
    
}

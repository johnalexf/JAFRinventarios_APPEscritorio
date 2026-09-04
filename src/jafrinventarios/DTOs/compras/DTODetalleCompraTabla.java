
package jafrinventarios.DTOs.compras;

/**
 *
 * @author JOHN FORERO
 */
public class DTODetalleCompraTabla {
    
    private final String nombreProducto;
    private final int cantidadProducto;
    private final double precioUnitarioProducto;
    private final double precioTotalProducto;
    
    /*
    ============================================================================
                                CONSTRUCTOR
    ============================================================================
    */

    public DTODetalleCompraTabla( 
                        String nombreProducto, 
                        int cantidadProducto, 
                        double precioUnitarioProducto, 
                        double precioTotalProducto) {
        this.nombreProducto = nombreProducto;
        this.cantidadProducto = cantidadProducto;
        this.precioUnitarioProducto = precioUnitarioProducto;
        this.precioTotalProducto = precioTotalProducto;
    }
    
    
    /*
    ============================================================================
                                  GETTERS
    ============================================================================
    */

    public String getNombreProducto() {
        return nombreProducto;
    }

    public int getCantidadProducto() {
        return cantidadProducto;
    }

    public double getPrecioUnitarioProducto() {
        return precioUnitarioProducto;
    }

    public double getPrecioTotalProducto() {
        return precioTotalProducto;
    }
    
    
    /*
    Este modelo no necesita setters puesta esta destinado exclusivamente 
    para obtener la informacion de forma que sea consistente a lo que espera 
    ver el usuario, para editar un detalle se usara como tal ModeloDetallleCompra
    */ 
    
}


package jafrinventarios.DTOs.productos;

/**
 *
 * @author JOHN FORERO
 */
public class DTOProductoTabla {
    
    private int idProducto;
    private String nombreProducto;
    private double precioCompra;
    private double precioVenta;
    private int cantidadMinimaStock;
    private int cantidadDisponible;
    private boolean habilitado;
    
    /*
        La diferencia clave entre ModeloProducto es que necesitamos pedir
    a la base de datos el nombre del proveedor en vez de el id, para poder
    mostrar en una tabla todos los productos.
    */
    private String nombreProveedor;
    
    
    /*
    ============================================================================
                                CONSTRUCTOR
    ============================================================================
    */
    
    public DTOProductoTabla(    int idProducto, 
                                String nombreProducto, 
                                double precioCompra, 
                                double precioVenta, 
                                int cantidadMinimaStock, 
                                int cantidadDisponible, 
                                boolean habilitado, 
                                String nombreProveedor) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
        this.cantidadMinimaStock = cantidadMinimaStock;
        this.cantidadDisponible = cantidadDisponible;
        this.habilitado = habilitado;
        this.nombreProveedor = nombreProveedor;
    }
    
    
    /*
    ============================================================================
                                  GETTERS
    ============================================================================
    */

    public int getIdProducto() {
        return idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public double getPrecioCompra() {
        return precioCompra;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public int getCantidadMinimaStock() {
        return cantidadMinimaStock;
    }

    public int getCantidadDisponible() {
        return cantidadDisponible;
    }

    public boolean isHabilitado() {
        return habilitado;
    }

    public String getNombreProveedor() {
        return nombreProveedor;
    }

    
    /*
    Este DTO no tiene setter pues su objetivo es solo para mostrar una lista
    de productos.
    */
}

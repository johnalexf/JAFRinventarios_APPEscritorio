
package jafrinventarios.modelos.productos;

/**
 *
 * @author JOHN FORERO
 */
public class ModeloProducto {
    
    private int idProducto;
    private int idProveedor;
    private String nombreProducto;
    private double precioCompra;
    private double precioVenta;
    private int cantidadMinimaStock;
    private int cantidadDisponible;
    private boolean habilitado;
    /* 
    Un producto puede estar deshabilitado si se considera que es necesario
    recervar las cantidades que quedan para algun cliente, o por alguna otra
    eventualidad como producto vencido o producto descontinuado
    */
    
    
    /*
    ============================================================================
                        CONSTRUCTORES
    ============================================================================
    */

    public ModeloProducto() {
    }

    
    public ModeloProducto(  int idProducto, 
                            int idProveedor, 
                            String nombreProducto, 
                            double precioCompra, 
                            double precioVenta, 
                            int cantidadMinimaStock, 
                            int cantidadDisponible, 
                            boolean habilitado) {
        this.idProducto = idProducto;
        this.idProveedor = idProveedor;
        this.nombreProducto = nombreProducto;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
        this.cantidadMinimaStock = cantidadMinimaStock;
        this.cantidadDisponible = cantidadDisponible;
        this.habilitado = habilitado;
    }
    
    
    /*
    ============================================================================
                                  GETTERS
    ============================================================================
    */

    public int getIdProducto() {
        return idProducto;
    }

    public int getIdProveedor() {
        return idProveedor;
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
    
    
    /*
    ============================================================================
                                  SETTERS
    ============================================================================
    */

    //El set de idProducto no se expone la unica fuente que lo asigna es la base de datos
    
    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public void setPrecioCompra(double precioCompra) {
        this.precioCompra = precioCompra;
    }

    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public void setCantidadMinimaStock(int cantidadMinimaStock) {
        this.cantidadMinimaStock = cantidadMinimaStock;
    }

    public void setCantidadDisponible(int cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }

    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }

    
    
    
}

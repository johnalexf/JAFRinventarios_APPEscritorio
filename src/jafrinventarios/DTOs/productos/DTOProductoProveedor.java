/*
 * Este DTO esta diseñado para obtener un producto con su nombre, nombre del proveedor
    y su respectiva cantidad disponible en stock, para mostrar una lista en la 
    seccion de inventario
 */
package jafrinventarios.DTOs.productos;

/**
 *
 * @author JOHN FORERO
 */
public class DTOProductoProveedor {
    
    private Integer idProducto;
    private String nombreProducto;
    private String nombreProveedor;
    private int cantidadDisponible;

    public DTOProductoProveedor(Integer idProducto, String nombreProducto, String nombreProveedor, int cantidadDisponible) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.nombreProveedor = nombreProveedor;
        this.cantidadDisponible = cantidadDisponible;
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public String getNombreProveedor() {
        return nombreProveedor;
    }

    public int getCantidadDisponible() {
        return cantidadDisponible;
    }
    
    
}

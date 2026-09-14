/*
 * Este DTO esta destinado para la seccion de inventario en donde permita
    actualizar la cantidad disponible de un producto
 */
package jafrinventarios.DTOs.productos;

/**
 *
 * @author JOHN FORERO
 */
public class DTOProductoCantidad {
    
    private Integer idProducto;
    private int cantidadDisponible;

    public DTOProductoCantidad(Integer idProducto, int cantidadDisponible) {
        this.idProducto = idProducto;
        this.cantidadDisponible = cantidadDisponible;
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public int getCantidadDisponible() {
        return cantidadDisponible;
    }
    
    
    /*
    No se exponen setters pues el controlador no deberia modificar el objeto
    instanciado, ya que solo cuando lo cree es cuando le asigna el nuevo valor
    de la cantidad disponible a actualizar
    */
    
}

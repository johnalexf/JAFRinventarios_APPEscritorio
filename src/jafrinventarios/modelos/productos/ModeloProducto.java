
package jafrinventarios.modelos.productos;

import java.util.Objects;

/**
 *
 * @author JOHN FORERO
 */
public class ModeloProducto {
    
    private Integer idProducto;
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

    
    public ModeloProducto(  Integer idProducto, 
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

    public Integer getIdProducto() {
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

    
    /*
    ============================================================================
                                EQUALS Y HASCHCODE
    ============================================================================
    */

    /*
    El hashCode() es la huella digital numérica del objeto creado
    Si dos objetos son iguales según el método .equals(), entonces 
    obligatoriamente deben devolver el mismo número en .hashCode()
    */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + this.idProducto;
        hash = 97 * hash + this.idProveedor;
        hash = 97 * hash + Objects.hashCode(this.nombreProducto);
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.precioCompra) ^ (Double.doubleToLongBits(this.precioCompra) >>> 32));
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.precioVenta) ^ (Double.doubleToLongBits(this.precioVenta) >>> 32));
        hash = 97 * hash + this.cantidadMinimaStock;
        hash = 97 * hash + this.cantidadDisponible;
        hash = 97 * hash + (this.habilitado ? 1 : 0);
        return hash;
    }
    
    
    /*
    equals se encarga de comparar el objeto creado con otro objeto, se espera
    que se comparen dos del tipo ModeloProducto para verificar si hubo un cambio
    en sus campos, antes de guardar por medio del servicio los cambios.
    */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ModeloProducto other = (ModeloProducto) obj;
        if (this.idProducto != other.idProducto) {
            return false;
        }
        if (this.idProveedor != other.idProveedor) {
            return false;
        }
        if (Double.doubleToLongBits(this.precioCompra) != Double.doubleToLongBits(other.precioCompra)) {
            return false;
        }
        if (Double.doubleToLongBits(this.precioVenta) != Double.doubleToLongBits(other.precioVenta)) {
            return false;
        }
        if (this.cantidadMinimaStock != other.cantidadMinimaStock) {
            return false;
        }
        if (this.cantidadDisponible != other.cantidadDisponible) {
            return false;
        }
        if (this.habilitado != other.habilitado) {
            return false;
        }
        if (!Objects.equals(this.nombreProducto, other.nombreProducto)) {
            return false;
        }
        return true;
    }
    
    
    /*
    ============================================================================
                             MÉTODO PARA CLONAR 
    ============================================================================
    */
    
    public ModeloProducto clonar(){
        
        return new ModeloProducto(
                this.idProducto,
                this.idProveedor,
                this.nombreProducto,
                this.precioCompra,
                this.precioVenta,
                this.cantidadMinimaStock,
                this.cantidadDisponible,
                this.habilitado
        );
    
    }
    
    
    /*
    ============================================================================
                MÉTODO OBTENER UN STRING CON TODOS LOS DATOS
    ============================================================================
    Se usara para cuando se necesite imprimir en consola y verificar que 
    los datos almacenados corresponden a los esperados.
    */

    @Override
    public String toString() {
        return "ModeloProducto" + 
                "\nidProducto=" + idProducto + 
                "\nidProveedor=" + idProveedor + 
                "\nnombreProducto=" + nombreProducto + 
                "\nprecioCompra=" + precioCompra + 
                "\nprecioVenta=" + precioVenta + 
                "\ncantidadMinimaStock=" + cantidadMinimaStock + 
                "\ncantidadDisponible=" + cantidadDisponible + 
                "\nhabilitado=" + habilitado + 
                '\n';
    }
    
    
    
    
}

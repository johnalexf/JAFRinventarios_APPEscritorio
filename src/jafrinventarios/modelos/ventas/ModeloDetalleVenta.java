
package jafrinventarios.modelos.ventas;

import java.util.Objects;

/**
 *
 * @author JOHN FORERO
 */
public class ModeloDetalleVenta {
    
    private Integer idDetalleVenta;
    private Integer idProducto;
    private int cantidadProducto;
    private double precioUnitarioProducto;
    private double precioTotalProducto;
    
    /*
    ============================================================================
                        CONSTRUCTORES
    ============================================================================
    */

    public ModeloDetalleVenta() {
    }

    public ModeloDetalleVenta(  Integer idDetalleVenta, 
                                Integer idProducto, 
                                int cantidadProducto, 
                                double precioUnitarioProducto, 
                                double precioTotalProducto) {
        this.idDetalleVenta = idDetalleVenta;
        this.idProducto = idProducto;
        this.cantidadProducto = cantidadProducto;
        this.precioUnitarioProducto = precioUnitarioProducto;
        this.precioTotalProducto = precioTotalProducto;
    }
    
    /*
    ============================================================================
                                  GETTERS
    ============================================================================
    */
    
    public Integer getIdDetalleVenta() {
        return idDetalleVenta;
    }

    public Integer getIdProducto() {
        return idProducto;
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
    ============================================================================
                                  SETTERS
    ============================================================================
    */

    public void setIdDetalleVenta(Integer idDetalleVenta) {
        this.idDetalleVenta = idDetalleVenta;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public void setCantidadProducto(int cantidadProducto) {
        this.cantidadProducto = cantidadProducto;
    }

    public void setPrecioUnitarioProducto(double precioUnitarioProducto) {
        this.precioUnitarioProducto = precioUnitarioProducto;
    }

    public void setPrecioTotalProducto(double precioTotalProducto) {
        this.precioTotalProducto = precioTotalProducto;
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
        hash = 41 * hash + Objects.hashCode(this.idDetalleVenta);
        hash = 41 * hash + Objects.hashCode(this.idProducto);
        hash = 41 * hash + this.cantidadProducto;
        hash = 41 * hash + (int) (Double.doubleToLongBits(this.precioUnitarioProducto) ^ (Double.doubleToLongBits(this.precioUnitarioProducto) >>> 32));
        hash = 41 * hash + (int) (Double.doubleToLongBits(this.precioTotalProducto) ^ (Double.doubleToLongBits(this.precioTotalProducto) >>> 32));
        return hash;
    }

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
        final ModeloDetalleVenta other = (ModeloDetalleVenta) obj;
        if (this.cantidadProducto != other.cantidadProducto) {
            return false;
        }
        if (Double.doubleToLongBits(this.precioUnitarioProducto) != Double.doubleToLongBits(other.precioUnitarioProducto)) {
            return false;
        }
        if (Double.doubleToLongBits(this.precioTotalProducto) != Double.doubleToLongBits(other.precioTotalProducto)) {
            return false;
        }
        if (!Objects.equals(this.idDetalleVenta, other.idDetalleVenta)) {
            return false;
        }
        if (!Objects.equals(this.idProducto, other.idProducto)) {
            return false;
        }
        return true;
    }

    
    /*
    ============================================================================
                             MÉTODO PARA CLONAR 
    ============================================================================
    */
    
    public ModeloDetalleVenta clonar(){
    
        return new ModeloDetalleVenta(
                this.idDetalleVenta,
                this.idProducto,
                this.cantidadProducto,
                this.precioUnitarioProducto,
                this.precioTotalProducto
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
        return "ModeloDetalleVenta" + 
                "\nidDetalleVenta=" + idDetalleVenta + 
                "\nidProducto=" + idProducto + 
                "\ncantidadProducto=" + cantidadProducto + 
                "\nprecioUnitarioProducto=" + precioUnitarioProducto + 
                "\nprecioTotalProducto=" + precioTotalProducto + 
                '\n';
    }
    
    
}

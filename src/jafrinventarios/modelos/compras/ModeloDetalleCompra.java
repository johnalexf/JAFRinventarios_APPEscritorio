/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jafrinventarios.modelos.compras;

import java.util.Objects;

/**
 *
 * @author JOHN FORERO
 */
public class ModeloDetalleCompra {
    
    private Integer idDetalleCompra;
    private Integer idCompra;
    private Integer idProducto;
    private int cantidadProducto = 0;
    private double precioUnitarioProducto = 0;
    private double precioTotalProducto = 0;

    
    /*
    ============================================================================
                        CONSTRUCTORES
    ============================================================================
    */
    //El constructor vacio se destina para crear nuevos registros
    //dejando la posiblidad de asingar los valos segun la necesidad
    public ModeloDetalleCompra() {
    }
    
    //El constructor con los atributos completos, esta exclusivamente destinado
    //para cuando se desee editar un registro
    public ModeloDetalleCompra( 
                        Integer idDetalleCompra, 
                        Integer idCompra, 
                        Integer idProducto, 
                        int cantidadProducto, 
                        double precioUnitarioProducto,
                        double precioTotalProducto) {
        this.idDetalleCompra = idDetalleCompra;
        this.idCompra = idCompra;
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

    public Integer getIdDetalleCompra() {
        return idDetalleCompra;
    }

    public Integer getIdCompra() {
        return idCompra;
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

    /*
    Los setters de id_detalle y precioTotal, no se exponen pues el id se lo asigna
    la base de datos al crearlo y de igual manera el totalProducto.
    */

    public void setIdCompra(Integer idCompra) {
        this.idCompra = idCompra;
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
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.idDetalleCompra);
        hash = 67 * hash + Objects.hashCode(this.idCompra);
        hash = 67 * hash + Objects.hashCode(this.idProducto);
        hash = 67 * hash + this.cantidadProducto;
        hash = 67 * hash + (int) (Double.doubleToLongBits(this.precioUnitarioProducto) ^ (Double.doubleToLongBits(this.precioUnitarioProducto) >>> 32));
        hash = 67 * hash + (int) (Double.doubleToLongBits(this.precioTotalProducto) ^ (Double.doubleToLongBits(this.precioTotalProducto) >>> 32));
        return hash;
    }

    
    /*
    equals se encarga de comparar el objeto creado con otro objeto, se espera
    que se comparen dos del tipo ModeloDetalleCompra para verificar si hubo un cambio
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
        final ModeloDetalleCompra other = (ModeloDetalleCompra) obj;
        if (this.cantidadProducto != other.cantidadProducto) {
            return false;
        }
        if (Double.doubleToLongBits(this.precioUnitarioProducto) != Double.doubleToLongBits(other.precioUnitarioProducto)) {
            return false;
        }
        if (Double.doubleToLongBits(this.precioTotalProducto) != Double.doubleToLongBits(other.precioTotalProducto)) {
            return false;
        }
        if (!Objects.equals(this.idDetalleCompra, other.idDetalleCompra)) {
            return false;
        }
        if (!Objects.equals(this.idCompra, other.idCompra)) {
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
    
    public ModeloDetalleCompra clonar(){
    
        return new ModeloDetalleCompra(
                this.idDetalleCompra,
                this.idCompra,
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
        return "ModeloDetalleCompra" + 
                "\nidDetalleCompra=" + idDetalleCompra + 
                "\nidCompra=" + idCompra + 
                "\nidProducto=" + idProducto + 
                "\ncantidadProducto=" + cantidadProducto + 
                "\nprecioUnitarioProducto=" + precioUnitarioProducto + 
                "\nprecioTotalProducto=" + precioTotalProducto +
                '\n';
    }
    
    
}

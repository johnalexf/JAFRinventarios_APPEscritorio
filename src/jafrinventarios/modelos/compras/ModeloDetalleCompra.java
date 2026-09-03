/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jafrinventarios.modelos.compras;

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
        calcularTotal();
    }

    public void setPrecioUnitarioProducto(double precioUnitarioProducto) {
        this.precioUnitarioProducto = precioUnitarioProducto;
        calcularTotal();
    }

    public void setPrecioTotalProducto(double precioTotalProducto) {
        this.precioTotalProducto = precioTotalProducto;
    }
    
    
    /*
    ============================================================================
                                METODOS INTERNOS
    ============================================================================
    */
    
    private void calcularTotal() {
        this.precioTotalProducto = Math.round(this.cantidadProducto * this.precioUnitarioProducto * 100.0) / 100.0;
    }

    
    
    
    
    
    
}

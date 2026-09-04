
package jafrinventarios.modelos.compras;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author JOHN FORERO
 */
public class ModeloCompra {
    
    private Integer idCompra;
    private Date fechaHoraCompra;
    private double totalCompra;
    private Integer idProveedor;
    private Integer idUsuario;
    
    private ArrayList<ModeloDetalleCompra> detalles;

    /*
    ============================================================================
                        CONSTRUCTORES
    ============================================================================
    */
    
    //Constructor para crear una nueva compra
    public ModeloCompra() {
        detalles = new ArrayList<>();
    }

    //Constructor para recolectar la consulta a la base de datos y poder editar el registro
    public ModeloCompra(    
                    Integer idCompra, 
                    Date fechaHoraCompra, 
                    double totalCompra, 
                    Integer idProveedor, 
                    Integer idUsuario, 
                    ArrayList<ModeloDetalleCompra> detalles) {
        this.idCompra = idCompra;
        this.fechaHoraCompra = fechaHoraCompra;
        this.totalCompra = totalCompra;
        this.idProveedor = idProveedor;
        this.idUsuario = idUsuario;
        this.detalles = detalles;
    }

    
    /*
    ============================================================================
                                  GETTERS
    ============================================================================
    */
    
    public Integer getIdCompra() {
        return idCompra;
    }

    public Date getFechaHoraCompra() {
        return fechaHoraCompra;
    }

    public double getTotalCompra() {
        return totalCompra;
    }

    public Integer getIdProveedor() {
        return idProveedor;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public ArrayList<ModeloDetalleCompra> getDetalles() {
        return new ArrayList<>(detalles);
    }
    
    /*
    ============================================================================
                                  SETTERS
    ============================================================================
    */


    public void setFechaHoraCompra(Date fechaHoraCompra) {
        this.fechaHoraCompra = fechaHoraCompra;
    }

    public void setTotalCompra(double totalCompra) {
        this.totalCompra = totalCompra;
    }

    public void setIdProveedor(Integer idProveedor) {
        this.idProveedor = idProveedor;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setDetalles(ArrayList<ModeloDetalleCompra> detalles) {
        this.detalles = new ArrayList<>(detalles);
    }

   
    
}

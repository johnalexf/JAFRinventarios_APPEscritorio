
package jafrinventarios.DTOs.reportes;

import java.time.LocalDate;

/**
 *
 * @author JOHN FORERO
 */
public class DTOFiltroReporte {
    
    LocalDate fechaInferior;
    LocalDate fechaSuperior;
    Integer idProveedor;
    Integer idCliente;
    Integer idProducto;
    Integer idUsuario;

    
    /*
    ============================================================================
                                CONSTRUCTOR
    ============================================================================
    */
    
    public DTOFiltroReporte() {
        idProveedor = null;
        idCliente = null;
        idProducto = null;
        idUsuario = null;
    }
    
    
    /*
    ============================================================================
                                  GETTERS
    ============================================================================
    */

    public LocalDate getFechaInferior() {
        return fechaInferior;
    }

    public LocalDate getFechaSuperior() {
        return fechaSuperior;
    }

    public Integer getIdProveedor() {
        return idProveedor;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }
    
    
    /*
    ============================================================================
                                  SETTERS
    ============================================================================
    */

    public void setFechaInferior(LocalDate fechaInferior) {
        this.fechaInferior = fechaInferior;
    }

    public void setFechaSuperior(LocalDate fechaSuperior) {
        this.fechaSuperior = fechaSuperior;
    }

    public void setIdProveedor(Integer idProveedor) {
        this.idProveedor = idProveedor;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }
    
    
    /*
    ============================================================================
                            RANGO DE FECHAS VALIDOS
    ============================================================================
    */
    public boolean isRangoFechasValido(){
        return ( fechaInferior.isBefore(fechaSuperior) || fechaInferior.isEqual(fechaSuperior) );
    }
            
}

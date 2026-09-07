
package jafrinventarios.DTOs.ventas;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author JOHN FORERO
 */
public class DTOVentaTabla {
    
    private final Integer idVenta;
    private final Date fechaHoraVenta;
    private final double totalVenta;
    private final String nombreNegocioCliente;
    private final String aliasUsuario;
    
    private final ArrayList<DTODetalleVentaTabla> detalles;
    
    /*
    ============================================================================
                                CONSTRUCTOR
    ============================================================================
    */

    public DTOVentaTabla(   Integer idVenta, 
                            Date fechaHoraVenta, 
                            double totalVenta, 
                            String nombreNegocioCliente, 
                            String aliasUsuario) {
        this.idVenta = idVenta;
        this.fechaHoraVenta = fechaHoraVenta;
        this.totalVenta = totalVenta;
        this.nombreNegocioCliente = nombreNegocioCliente;
        this.aliasUsuario = aliasUsuario;
        this.detalles = new ArrayList<>();
    }
    
    /*
    ============================================================================
                                  GETTERS
    ============================================================================
    */

    public Integer getIdVenta() {
        return idVenta;
    }

    public Date getFechaHoraVenta() {
        return fechaHoraVenta;
    }

    public double getTotalVenta() {
        return totalVenta;
    }

    public String getNombreNegocioCliente() {
        return nombreNegocioCliente;
    }

    public String getAliasUsuario() {
        return aliasUsuario;
    }

    public ArrayList<DTODetalleVentaTabla> getDetalles() {
        return detalles;
    }
    
    /*
    Este DTO no necesita setters puesta esta destinado exclusivamente 
    para obtener la informacion de forma que sea consistente a lo que espera 
    ver el usuario, para editar una VENTA se usara como tal ModeloVenta
    */
    
    public void agregarDetalle ( DTODetalleVentaTabla detalle ){
        detalles.add( detalle );
    }
}

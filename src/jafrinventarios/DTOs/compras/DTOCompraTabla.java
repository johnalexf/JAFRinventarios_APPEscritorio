
package jafrinventarios.DTOs.compras;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author JOHN FORERO
 */
public class DTOCompraTabla {
    
    private final Integer idCompra;
    private final Date fechaHoraCompra;
    private final double totalCompra;
    private final String nombreComercialProveedor;
    private final String aliasUsuario;
    
    private final ArrayList<DTODetalleCompraTabla> detalles;

    
    /*
    ============================================================================
                                CONSTRUCTOR
    ============================================================================
    */
    
    public DTOCompraTabla(  Integer idCompra, 
                            Date fechaHoraCompra,
                            double totalCompra, 
                            String nombreComercialProveedor, 
                            String aliasUsuario) {
        this.idCompra = idCompra;
        this.fechaHoraCompra = fechaHoraCompra;
        this.totalCompra = totalCompra;
        this.nombreComercialProveedor = nombreComercialProveedor;
        this.aliasUsuario = aliasUsuario;
        this.detalles = new ArrayList<>();
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

    public String getNombreComercialProveedor() {
        return nombreComercialProveedor;
    }

    public String getAliasUsuario() {
        return aliasUsuario;
    }

    public ArrayList<DTODetalleCompraTabla> getDetalles() {
        return new ArrayList<>(detalles);
    }
          
    
    /*
    Este modelo no necesita setters puesta esta destinado exclusivamente 
    para obtener la informacion de forma que sea consistente a lo que espera 
    ver el usuario, para editar una compra se usara como tal ModeloCompra
    */
    
    public void agregarDetalle( DTODetalleCompraTabla detalle ) {
        detalles.add( detalle );
    }
    
    
}

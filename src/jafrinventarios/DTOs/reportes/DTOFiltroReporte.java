
package jafrinventarios.DTOs.reportes;

import java.time.LocalDate;

/**
 *
 * @author JOHN FORERO
 */
public class DTOFiltroReporte {
    
    private LocalDate fechaInferior;
    private LocalDate fechaSuperior;
    private Integer idProveedor;
    private Integer idCliente;
    private Integer idProducto;
    private Integer idUsuario;
    
    private String proveedor;
    private String cliente;
    private String producto;
    private String usuario;

    
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
        proveedor = "";
        cliente = "";
        producto = "";
        usuario = "";
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

    public String getProveedor() {
        return proveedor;
    }

    public String getCliente() {
        return cliente;
    }

    public String getProducto() {
        return producto;
    }

    public String getUsuario() {
        return usuario;
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

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    
    
    /*
    ============================================================================
                            RANGO DE FECHAS VALIDOS
    ============================================================================
    */
    public boolean isRangoFechasValido(){
        return ( fechaInferior.isBefore(fechaSuperior) || fechaInferior.isEqual(fechaSuperior) );
    }

    
    /*
    ============================================================================
          to string para verificar por consola la informacion almacenada
    ============================================================================
    */
    @Override
    public String toString() {
        return "DTOFiltroReporte{\n" + 
                "fechaInferior=" + fechaInferior + 
                "\nfechaSuperior=" + fechaSuperior + 
                "\nidProveedor=" + idProveedor + 
                "\nidCliente=" + idCliente + 
                "\nidProducto=" + idProducto + 
                "\nidUsuario=" + idUsuario + 
                "\n}";
    }
      
            
}


package jafrinventarios.DTOs.clientes;

/**
 *
 * @author JOHN FORERO
 */
public class DTOClienteTabla {
    
    private int idCliente;
    private String nombreNegocio;
    private String nombreCompletoContacto;
    private String telefonoContacto;
    private String direccionCliente;
    private String correoCliente;
    private boolean habilitado;
    
    /*
    ============================================================================
                                CONSTRUCTOR
    ============================================================================
    */

    public DTOClienteTabla(int idCliente, String nombreNegocio, String nombreCompletoContacto, String telefonoContacto, String direccionCliente, String correoCliente, boolean habilitado) {
        this.idCliente = idCliente;
        this.nombreNegocio = nombreNegocio;
        this.nombreCompletoContacto = nombreCompletoContacto;
        this.telefonoContacto = telefonoContacto;
        this.direccionCliente = direccionCliente;
        this.correoCliente = correoCliente;
        this.habilitado = habilitado;
    }

    
    /*
    ============================================================================
                                  GETTERS
    ============================================================================
    */

    public int getIdCliente() {
        return idCliente;
    }

    public String getNombreNegocio() {
        return nombreNegocio;
    }

    public String getNombreCompletoContacto() {
        return nombreCompletoContacto;
    }

    public String getTelefonoContacto() {
        return telefonoContacto;
    }

    public String getDireccionCliente() {
        return direccionCliente;
    }

    public String getCorreoCliente() {
        return correoCliente;
    }

    public boolean isHabilitado() {
        return habilitado;
    }
    
    
    /*
    Este DTO no tiene setter pues su objetivo es solo para mostrar una lista
    de clientes.
    */
}


package jafrinventarios.DTOs.proveedores;

/**
 *
 * @author JOHN FORERO
 */
public class DTOProveedorTabla {
    
    private int idProveedor;
    private String nombreComercial;
    private String nombreCompletoContacto;
    private String telefonoContacto;
    private String direccionProveedor;
    private String correoProveedor;
    private boolean habilitado;
    
    
    /*
    ============================================================================
                                CONSTRUCTOR
    ============================================================================
    */

    public DTOProveedorTabla(int idProveedor, String nombreComercial, String nombreCompletoContacto, String telefonoContacto, String direccionProveedor, String correoProveedor, boolean habilitado) {
        this.idProveedor = idProveedor;
        this.nombreComercial = nombreComercial;
        this.nombreCompletoContacto = nombreCompletoContacto;
        this.telefonoContacto = telefonoContacto;
        this.direccionProveedor = direccionProveedor;
        this.correoProveedor = correoProveedor;
        this.habilitado = habilitado;
    }
    
    
    /*
    ============================================================================
                                  GETTERS
    ============================================================================
    */

    public int getIdProveedor() {
        return idProveedor;
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public String getNombreCompletoContacto() {
        return nombreCompletoContacto;
    }

    public String getTelefonoContacto() {
        return telefonoContacto;
    }

    public String getDireccionProveedor() {
        return direccionProveedor;
    }

    public String getCorreoProveedor() {
        return correoProveedor;
    }

    public boolean isHabilitado() {
        return habilitado;
    }
    
    
    /*
    Este DTO no tiene setter pues su objetivo es solo para mostrar una lista
    de proveedores.
    */
    
}


package jafrinventarios.DTOs.acceso;

/**
 *
 * @author JOHN FORERO
 */
public class DTOCredenciales {
    
    private int idUsuario;
    private String nombreRol;
    private boolean isAdministrador;
    private int idEmpresa; 
    private String nombreEmpresa;

    /* 
    =================================================================================
                                  Constructores 
    =================================================================================
    */
    
    public DTOCredenciales(){}

    public DTOCredenciales( int idUsuario, String nombreRol, boolean isAdministrador, int idEmpresa, String nombreEmpresa ) {
        this.idUsuario = idUsuario;
        this.nombreRol = nombreRol;
        this.isAdministrador = isAdministrador;
        this.idEmpresa = idEmpresa;
        this.nombreEmpresa = nombreEmpresa;
    }
   
    
    /* 
    =================================================================================
                                   GETTERS
    =================================================================================
    */

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public boolean isAdministrador() {
        return isAdministrador;
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }
    
    
    
    
}

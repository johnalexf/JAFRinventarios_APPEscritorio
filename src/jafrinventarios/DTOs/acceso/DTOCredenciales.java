
package jafrinventarios.DTOs.acceso;

/**
 *
 * @author JOHN FORERO
 */
public class DTOCredenciales {
    
    private int idUsuario;
    private String nombreRol;
    private boolean esAdministrador;
    private int idEmpresa; 
    private String nombreEmpresa;

    /* 
    =================================================================================
                                  Constructores 
    =================================================================================
    */
    
    public DTOCredenciales(){}

    public DTOCredenciales( int idUsuario, String nombreRol, boolean esAdministrador, int idEmpresa, String nombreEmpresa ) {
        this.idUsuario = idUsuario;
        this.nombreRol = nombreRol;
        this.esAdministrador = esAdministrador;
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

    public boolean esAdministrador() {
        return esAdministrador;
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }
    
    
    
    
}

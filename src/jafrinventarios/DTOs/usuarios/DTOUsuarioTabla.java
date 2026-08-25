
package jafrinventarios.DTOs.usuarios;

/**
 *
 * @author JOHN FORERO
 */
public class DTOUsuarioTabla {
    
    
    private int idUsuario;
    private String aliasUsuario;
    private String telefonoUsuario;
    private String correoUsuario;
    private String nombreCompletoUsuario;
    private String nombreRolUsuario;
    private boolean habilitado;

    public DTOUsuarioTabla(     int idUsuario, 
                                String aliasUsuario, 
                                String telefonoUsuario, 
                                String correoUsuario, 
                                String nombreCompletoUsuario, 
                                String nombreRolUsuario, 
                                boolean habilitado) {
        this.idUsuario = idUsuario;
        this.aliasUsuario = aliasUsuario;
        this.telefonoUsuario = telefonoUsuario;
        this.correoUsuario = correoUsuario;
        this.nombreCompletoUsuario = nombreCompletoUsuario;
        this.nombreRolUsuario = nombreRolUsuario;
        this.habilitado = habilitado;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getAliasUsuario() {
        return aliasUsuario;
    }

    public String getTelefonoUsuario() {
        return telefonoUsuario;
    }

    public String getCorreoUsuario() {
        return correoUsuario;
    }

    public String getNombreCompletoUsuario() {
        return nombreCompletoUsuario;
    }

    public String getNombreRolUsuario() {
        return nombreRolUsuario;
    }

    public boolean estaHabilitado() {
        return habilitado;
    }

   
    /*
    Este DTO no tiene setter pues su objetivo es solo para mostrar una lista
    de usuarios.
    */

}

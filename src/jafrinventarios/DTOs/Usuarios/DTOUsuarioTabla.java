/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jafrinventarios.DTOs.Usuarios;

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
    private boolean estaHabilitado;

    public DTOUsuarioTabla(     int idUsuario, 
                                String aliasUsuario, 
                                String telefonoUsuario, 
                                String correoUsuario, 
                                String nombreCompletoUsuario, 
                                String nombreRolUsuario, 
                                boolean estaHabilitado) {
        this.idUsuario = idUsuario;
        this.aliasUsuario = aliasUsuario;
        this.telefonoUsuario = telefonoUsuario;
        this.correoUsuario = correoUsuario;
        this.nombreCompletoUsuario = nombreCompletoUsuario;
        this.nombreRolUsuario = nombreRolUsuario;
        this.estaHabilitado = estaHabilitado;
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

    public boolean getEstaHabilitado() {
        return estaHabilitado;
    }

   
    /*
    Este DTO no tiene setter pues su objetivo es solo para mostrar una lista
    de usuarios.
    */

}

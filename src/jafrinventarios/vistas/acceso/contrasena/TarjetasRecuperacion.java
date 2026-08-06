/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jafrinventarios.vistas.acceso.contrasena;

/**
 *
 * @author JOHN FORERO
 */
public enum TarjetasRecuperacion {
    CORREO("cardCorreo"),
    CODIGO("cardConfirmarCodigo"),
    CONTRASENA_ANTIGUA("cardContrasenaAntigua"),
    CONTRASENA_NUEVA("cardContrasenaNueva");

    private String identificador;

    TarjetasRecuperacion(String identificador){
        this.identificador = identificador;
    }
    
    public String getIdentificador(){
        return identificador;
    }
        
}

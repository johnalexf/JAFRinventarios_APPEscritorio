/*
 * Clase para dar un nombre a las tarjetas que componen los diferentes pasos
    para recuperar una contraseña o para cambiarla
 * Se ha creado este enum para globalizar los nombres y evitar tener que depender
    de escribir el string del nombre de la tarjeta.
 */
package jafrinventarios.vistas.acceso.contrasena;

/**
 *
 * @author JOHN FORERO
 */
public enum NombresTarjetasContrasena {
    CORREO("cardCorreo"),
    CODIGO("cardConfirmarCodigo"),
    CONTRASENA_ANTIGUA("cardContrasenaAntigua"),
    CONTRASENA_NUEVA("cardContrasenaNueva");

    private String identificador;

    NombresTarjetasContrasena(String identificador){
        this.identificador = identificador;
    }
    
    public String getIdentificador(){
        return identificador;
    }
        
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jafrinventarios.controladores.validaciones;

/**
 *
 * @author JOHN FORERO
 */
public enum TipoDatoFormulario {
    // Definimos las reglas con su Regex y su mensaje
    
    NOMBRE("^[a-zA-ZáéíóúÁÉÍÓÚñÑ'\\-\\s]+$", "Solo se permiten letras"),
    //El telefono en colombia puede ser fijo o movil, ambos el tamaño es de 10
    //ya que el telefono fijo ahora se escribe como 601#######
    TELEFONO("^[0-9]{10}$", "Debe contener 10 números"),
    CORREO("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,}$", "Formato de correo inválido"),
    ALIAS("^[\\w]{5,}$","Formato de Alias no valido"),
    CONTRASENA(
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
        "Debe contener mayúsculas, minúsculas, números y un carácter especial"
    ),
    CODIGO("^.{10}$","Debe tener exactamente 10 caracteres"),
    NUMERO("^[0-9]+$", "Solo se permiten números enteros"),
    REQUERIDO(".*\\S.*", "Este campo no puede estar vacío");

    private final String regex;
    private final String mensajeError;

    //Estando dentro de un enum, podemos definir de cada variable que hemos creado
    //que interpretacion poseen cada uno de los elementos internos, por medio del
    //Constructor del mismo enum
    TipoDatoFormulario(String regex, String mensajeError) {
        this.regex = regex;
        this.mensajeError = mensajeError;
    }

    public String getRegex() {
        return regex; 
    }
    public String getMensajeError() {
        return mensajeError; 
    }
}

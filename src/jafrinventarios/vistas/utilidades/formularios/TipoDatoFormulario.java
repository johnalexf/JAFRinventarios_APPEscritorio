
package jafrinventarios.vistas.utilidades.formularios;

/**
 *
 * @author JOHN FORERO
 */
public enum TipoDatoFormulario {
    // Definimos las reglas con su Regex y su mensaje
    
    NOMBRE_PERSONA("^[a-zA-ZáéíóúÁÉÍÓÚñÑ'\\-\\s]+$", "Solo se permiten letras"),
    
    // Permite: letras, números, espacios, *, (), -, ., /
    NOMBRE_PRODUCTO("^[a-zA-Z0-9ÁÉÍÓÚáéíóúÑñ\\s\\*\\(\\)\\-\\.\\/]+$", "Nombre invalido"),
    
    //Permite: letras, números, espacios, ., -, &, ,
    NOMBRE_EMPRESA("^[a-zA-Z0-9ÁÉÍÓÚáéíóúÑñ\\s\\.\\-\\&\\,]+$", "Nombre invalido"),
    
    //El telefono en colombia puede ser fijo o movil, ambos el tamaño es de 10
    //ya que el telefono fijo ahora se escribe como 601#######
    TELEFONO("^[0-9]{10}$", "Debe contener 10 números"),
    
    CORREO("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,}$", "Formato de correo inválido"),
    ALIAS("^[\\w]{5,}$","Solo se permiten letras y números, min 5 caracteres"),
    CONTRASENA(
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d])\\S{8,}$",
        "Formato invalido"
    ),
    CODIGO("^.{10}$","Debe tener exactamente 10 caracteres"),
    NUMERO_ENTERO("^[0-9]+$", "Solo se permiten números enteros"),
    NUMERO_DOUBLE("^[0-9]+(?:\\.[0-9]{1,2})?$", "Solo reales de hasta 2 decimales"),
    /*
    REQUERIDO es para aquellos campos que por lo menos deben tener algun caracter
    ya que su contenido no depende de una validacion en la vista, si no que de una
    validacion de comprobacion en la base de datos, o para aquellos campos que sea 
    de texto libre como motivo de eliminacion de un campo para manejar logs.
    */
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

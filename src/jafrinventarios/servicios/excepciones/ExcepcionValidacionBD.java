


package jafrinventarios.servicios.excepciones;

import java.util.HashMap;

/**
 * Excepción personalizada para transportar errores de validación 
 * (como duplicados o reglas de negocio) desde la capa de Servicios 
 * hacia los Controladores.
 */
public class ExcepcionValidacionBD extends Exception {
    
    private final HashMap<String, String> errores;

    public ExcepcionValidacionBD(HashMap<String, String> errores) {
        // Le pasamos un mensaje genérico al padre por si se imprime en consola
        super("Existen errores de validacion en la base de datos");
        this.errores = errores;
    }

    public HashMap<String, String> getErrores() {
        return errores;
    }
}
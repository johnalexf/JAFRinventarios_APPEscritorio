
package jafrinventarios.servicios.excepciones;

import java.util.HashMap;
import java.util.Map;

/**
 * Excepción personalizada para transportar errores de validación 
 * (como duplicados o reglas de negocio) desde la capa de Servicios 
 * hacia los Controladores.
 */
public class ExcepcionValidacionBD extends Exception {
    
    private final HashMap<String, String> errores;

    public ExcepcionValidacionBD(HashMap<String, String> errores) {
        // Le pasamos un mensaje genérico al padre por si se imprime en consola
        super("Existen errores de validacion en la base de datos ");
        this.errores = errores;
    }

    public HashMap<String, String> getErrores() {
        return errores;
    }
    
    public String getErroresEnString(){
        StringBuilder erroresString = new StringBuilder();
        for ( Map.Entry< String, String> error : this.errores.entrySet() ) {
            erroresString.append("\n ( ")
                         .append(error.getKey())
                         .append(" : ")
                         .append(error.getValue())
                         .append(" ) ");
            
        }
        erroresString.append("\n");
        return erroresString.toString();
    }
    
}
/**
 * Define el resultado devuelto por los diálogos modales que permiten
 * modificar un registro, ya sea para actualizarlo o eliminarlo, en dado
 * caso que no se ejecuten cambios responder SIN_CAMBIOS
 * 
 * Nace de la necesidad de en vez de utilizar un booleano como respuesta
 * en donde un true puede significar que se actualizo o que se edito, se
 * pueda especificar exactamente cual fue la accion que se realizo.
 */
package jafrinventarios.controladores.utilidades;

/**
 *
 * @author JOHN FORERO
 */
public enum ResultadoDialogo {
    ACTUALIZADO,
    ELIMINADO,
    SIN_CAMBIOS
}
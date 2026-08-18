/*
 Esta interfaz fue diseñada para que el controladorBusquedaYRegistro sea quien
 active los eventos de escucha del boton accion libre y buscar coincidencias
 sin necesidad que dentro de dicho controlador se declare la logica de
 busqueda y la accion libre, por tanto esta interfaz es el puente que permite que el 
 controlador principal sea quien asigne el codigo de las funciones segun 
 corresponda y se las pase por medio de un objeto de esta interfaz.
 */
package jafrinventarios.controladores.utilidades;

/**
 *
 * @author JOHN FORERO
 */
public interface FuncionesBusquedaYAccionLibre {
    
    // Esta codificado en ControladorBusquedaYAccionLibre para que 
    // se dispare solo si la búsqueda es válida (no vacía, enter o clic)
    boolean ejecutarBusqueda(String terminoBusqueda);
    
    
    void limpiarBusqueda();
    
    // Esta codificado en ControladorBusquedaYAccionLibre para que
    // se dispare cuando hacen clic en "Agregar Nuevo"
    void ejecutarAccionLibre();
}

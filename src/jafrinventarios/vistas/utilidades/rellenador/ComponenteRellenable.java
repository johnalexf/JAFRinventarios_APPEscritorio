/*
    Clase abstracta base que permitira poder agregar a un diccionario diferentes
    tipos de campos rellenables, como label o comboBox
 */
package jafrinventarios.vistas.utilidades.rellenador;

import javax.swing.JComponent;

/**
 *
 * @author JOHN FORERO
 */
public abstract class ComponenteRellenable {
    
    private final JComponent componente;
    
    public ComponenteRellenable(JComponent componente){
        this.componente = componente;
    }
    
    protected abstract void escribirEnCampo(String dato);
    
}

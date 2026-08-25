
package jafrinventarios.vistas.utilidades.rellenador;

import javax.swing.text.JTextComponent;

/**
 *
 * @author JOHN FORERO
 */
public class ComponenteTexto extends ComponenteRellenable{

    private final JTextComponent componenteTexto;
    
    
    public ComponenteTexto(JTextComponent componenteTexto){
        super(componenteTexto);
        this.componenteTexto = componenteTexto;
    }
    
    
    @Override
    protected void escribirEnCampo(String dato) {
        componenteTexto.setText(dato);
    }
    
    
}

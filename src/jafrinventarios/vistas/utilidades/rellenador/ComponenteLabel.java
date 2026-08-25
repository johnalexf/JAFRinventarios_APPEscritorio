
package jafrinventarios.vistas.utilidades.rellenador;

import javax.swing.JLabel;

/**
 *
 * @author JOHN FORERO
 */
public class ComponenteLabel extends ComponenteRellenable{

    private final JLabel componenteLabel;
    
    
    public ComponenteLabel(JLabel campoLabel){
        super(campoLabel);
        this.componenteLabel = campoLabel;
    }
    
    
    @Override
    protected void escribirEnCampo(String dato) {
        componenteLabel.setText(dato);
    }
    
}

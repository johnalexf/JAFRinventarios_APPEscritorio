/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

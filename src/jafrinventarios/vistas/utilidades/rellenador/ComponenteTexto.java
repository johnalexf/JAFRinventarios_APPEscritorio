/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jafrinventarios.vistas.utilidades.rellenador;

import javax.swing.text.JTextComponent;

/**
 *
 * @author JOHN FORERO
 */
public class ComponenteTexto extends ComponenteRellenable{

    private JTextComponent componenteTexto;
    
    
    public ComponenteTexto(JTextComponent componenteTexto){
        super(componenteTexto);
        this.componenteTexto = componenteTexto;
    }
    
    
    @Override
    protected void escribirEnCampo(String dato) {
        componenteTexto.setText(dato);
    }
    
    
}

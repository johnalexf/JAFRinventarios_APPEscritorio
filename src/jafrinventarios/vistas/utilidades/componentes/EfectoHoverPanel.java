/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jafrinventarios.vistas.utilidades.componentes;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

/**
 *
 * @author JOHN FORERO
 */
public class EfectoHoverPanel {
    
    private final JPanel panel;
    private final Color colorNormal;
    
    private EfectoHoverPanel(JPanel panel){
        this.panel = panel;
        this.colorNormal = panel.getBackground();
        
        inicializarEventos();
    }
    
    private void inicializarEventos(){
    
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(colorNormal.darker()); // Color al hacer hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(colorNormal); // Color original
            }
        });
    
    }
    
     /*
    ============================================================================
        METODO ESTATICO UNICO PARA DECLARAR LA INTENCION DE ESTA CLASE
    ============================================================================
    Al usar esta clase la idea es presentar solo este metodo para que se 
    entienda que no se espera retornar un objeto si no que el mismo le 
    aplicara un efecto hover a un panel sin necesidad de instanciarlo
    desde afuera.
    */
    public static void aplicarEfecto(JPanel panel) {       
        new EfectoHoverPanel(panel); 
    }
            
}

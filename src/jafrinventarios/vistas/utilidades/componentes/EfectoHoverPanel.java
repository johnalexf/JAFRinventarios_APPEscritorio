
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
    private Color colorNormal;
    
    private EfectoHoverPanel(JPanel panel){
        this.panel = panel;
        
        inicializarEventos();
    }
    
    private void inicializarEventos(){
    
        panel.addMouseListener(new MouseAdapter() {
            
            @Override
            public void mouseEntered(MouseEvent e) {
                colorNormal = panel.getBackground();
                panel.setBackground(colorNormal.darker()); // Color al hacer hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                /*
                Ya no se guarda el color desde el constructor para no depender 
                del color que tenga el panel cuando recien se le aplica el estilo, 
                pues esto permite que si se cambia el color aun funcione el hover
                */
                if(colorNormal != null){
                    panel.setBackground(colorNormal); // Color original
                }
                colorNormal = null;

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

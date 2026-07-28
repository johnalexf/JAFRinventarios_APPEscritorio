/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jafrinventarios.vistas.utilidades;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author johna
 */

/**
 * ============================================================================
 * CLASE: GlassPaneSemiOscuro
 * ============================================================================
 * JPanel utilizado como GlassPane de un JFrame para oscurecer la ventana
 * mientras se muestra un JDialog.
 * 
 * El GlassPane SIEMPRE se dibuja encima de toda la interfaz.
 * Eso permite oscurecer toda la ventana sin modificar ninguno de los paneles.
 * Esta clase NO instala automáticamente el GlassPane dentro del JFrame,
 * solo representa el JPanel que será utilizado como GlassPane.
 *
 * ¿Por qué existe?
 * - Se reutilizará en varios diálogos de la aplicación.
 * - Mantiene separada la lógica del fondo oscuro del resto del código.
 *
 * ¿Cómo se usa?
 *
 * GlassPaneSemiOscuro glassPane = new GlassPaneSemiOscuro();
 * frame.setGlassPane(glassPane);
 * glassPane.setVisible(true);
 *
 * dialogo.setVisible(true);
 *
 * glassPane.setVisible(false);
 * ============================================================================
 */

public class GlassPaneSemiOscuro extends JPanel {

   public GlassPaneSemiOscuro() {

        /*
        * Este GlassPane usa transparencia, por lo que debe permanecer con
        * setOpaque(false). Declararlo como opaco(true) puede producir errores de
        * repintado debido a las optimizaciones internas de Swing.
        */
        setOpaque(false);
        
        // Negro con transparencia (Alpha = 120).
        setBackground(new Color(0, 0, 0, 120));
    }

    /*
     * Swing llama automáticamente este método cuando el componente necesita
     * redibujarse.
     *
     * Como este GlassPane utiliza setOpaque(false), Swing asume que el propio
     * componente NO pintará su fondo automáticamente.
     *
     * Por ese motivo sobrescribimos paintComponent() para dibujar manualmente
     * un rectángulo negro con transparencia que cubre todo el panel.
     *
     * Si no se hiciera este dibujo manual, el GlassPane sería completamente
     * transparente y no produciría el efecto de oscurecer la ventana.
     */
    @Override
    protected void paintComponent(Graphics g) {
        // Dibuja un rectángulo que cubre todo el panel con el color configurado.
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        // El color tiene transparencia, por lo que la aplicación sigue siendo visible
        // debajo del fondo oscuro.
    }

}
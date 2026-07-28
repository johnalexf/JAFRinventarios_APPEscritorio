/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jafrinventarios.vistas.utilidades;

import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 *
 * @author JOHN FORERO
 */
public class DialogoBaseConSombra extends JDialog {
    
    //Variable global para mejorar legibilidad del codigo
    // Se crea un JPanel especial con transparencia dedicado para asignar al glassPane del frame padre
    private final GlassPaneSemiOscuro panelSemiOscuro = new GlassPaneSemiOscuro();
    protected JFrame padreFrame;

    public DialogoBaseConSombra(JFrame parent) {
        //super(parent, modal);
        super(parent, true);
        this.padreFrame = parent;
    }

    private void mostrarGlassPanePadreDialogo() {
        if (padreFrame != null) {
            padreFrame.setGlassPane(panelSemiOscuro);
            panelSemiOscuro.setVisible(true);
        }
    }

    private void ocultarGlassPanePadreDialogo() {
        if (padreFrame != null) {
            panelSemiOscuro.setVisible(false);
        }
    }

    // Este es el método que heredarán los modales para abrirse
    protected void hacerVisibleDialogo() {
        // mostrar fondo semitransparente
        mostrarGlassPanePadreDialogo();
        // Recalcular tamaño del dialog segun el tamaño que necesite cada componente interno
        pack();
        // Centrar el dialogo con respecto a su padre
        setLocationRelativeTo(padreFrame);
        // Mostrar el diálogo (el código se pausa aquí hasta que se cierra el modal)
        setVisible(true);
    }
    
    // Al sobrescribir dispose, garantizamos que la sombra se limpie sola al cerrar
    @Override
    public void dispose() {
         // Al cerrar el modal, se deja de mostrar el fondo semitransparente
        ocultarGlassPanePadreDialogo();
        super.dispose();
    }
    
}

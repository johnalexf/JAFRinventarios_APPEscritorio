/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jafrinventarios.vistas.utilidades.dialogos;

import java.awt.Dialog;
import java.awt.Window;
import javax.swing.JDialog;

/**
 *
 * @author JOHN FORERO
 */
public class DialogoBaseConSombra extends JDialog {
    
    //Variable global para mejorar legibilidad del codigo
    // Se crea un JPanel especial con transparencia dedicado para asignar al glassPane del frame padre
    private final GlassPaneSemiOscuro panelSemiOscuro = new GlassPaneSemiOscuro();
    protected Window ventanaPadre;

    public DialogoBaseConSombra(Window parent) {
        /* Se decide usar Window para poder dibujar el fondo semioscuro sobre cualquier tipo de 
            ventana, ya sea un Jframe, un Jdialog u otros.
            super(parent, modal);
            Usamos ModalityType.APPLICATION_MODAL en lugar del boolean true, firma correcta que exige la clase Window
        */
        super(parent, Dialog.ModalityType.APPLICATION_MODAL);
        this.ventanaPadre = parent;
    }

    private void mostrarGlassPanePadreDialogo() {
        // Evaluamos si la ventana implementa la interfaz que maneja GlassPanes
        if (ventanaPadre instanceof javax.swing.RootPaneContainer) {
            // Hacemos el cast directamente a la interfaz
            ((javax.swing.RootPaneContainer) ventanaPadre).setGlassPane(panelSemiOscuro);
            panelSemiOscuro.setVisible(true);
        }
    }

    private void ocultarGlassPanePadreDialogo() {
        if (ventanaPadre != null) {
            panelSemiOscuro.setVisible(false);
        }
    }

    // Este es el método que heredarán los modales para abrirse
    public void hacerVisibleDialogo() {
        // mostrar fondo semitransparente
        mostrarGlassPanePadreDialogo();
        // Recalcular tamaño del dialog segun el tamaño que necesite cada componente interno
        pack();
        
        definirUbicacionDialogo();
        
        // Mostrar el diálogo (el código se pausa aquí hasta que se cierra el modal)
        setVisible(true);
    }
    
    protected void definirUbicacionDialogo(){
        // Centrar el dialogo con respecto a su padre
        setLocationRelativeTo(ventanaPadre);
    }
    
    // Al sobrescribir dispose, garantizamos que la sombra se limpie sola al cerrar
    @Override
    public void dispose() {
         // Al cerrar el modal, se deja de mostrar el fondo semitransparente
        ocultarGlassPanePadreDialogo();
        super.dispose();
    }
    
}

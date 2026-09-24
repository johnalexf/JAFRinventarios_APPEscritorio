/*
 * Clase para modificar el tamaño de un checkbox
 */
package jafrinventarios.vistas.utilidades.componentes;

/**
 *
 * @author JOHN FORERO
 */
public class CheckBox {
    
    public static void cambiarTamanoCheckBox( javax.swing.JCheckBox checkBox ){
     
        // --- FORZAR EL TAMAÑO DEL CHECKBOX ---
        javax.swing.Icon iconOriginal = javax.swing.UIManager.getIcon("CheckBox.icon");
        if (iconOriginal != null) {
            checkBox.setIcon(new javax.swing.Icon() {
                // Escala de crecimiento. 1.8 lo hace casi el doble de grande.
                double escala = 1.8; 
                
                @Override
                public void paintIcon(java.awt.Component c, java.awt.Graphics g, int x, int y) {
                    java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                    // Movemos el pincel a la posición original
                    g2.translate(x, y); 
                    // Obligamos a escalar el dibujo
                    g2.scale(escala, escala); 
                    // Dibujamos el icono nativo de FlatLaf en la nueva escala
                    iconOriginal.paintIcon(c, g2, 0, 0); 
                    g2.dispose();
                }
                
                @Override
                public int getIconWidth() { 
                    return (int) (iconOriginal.getIconWidth() * escala); 
                }
                
                @Override
                public int getIconHeight() { 
                    return (int) (iconOriginal.getIconHeight() * escala); 
                }
            });
        }
    
    }
    
    
}

package jafrinventarios.vistas.utilidades.componentes;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;

/**
 * Clase utilitaria para inyectar dinamismo a los botones para simular un Link.
 */
public class DinamismoLink {

    /**
     * Convierte un JButton estándar en un enlace con efecto Hover (subrayado y oscurecimiento automático).
     * El color base se extrae directamente de las propiedades del diseñador visual.
     * 
     * @param boton El botón a transformar.
     */
    public static void aplicarEfecto(JButton boton) {
        
        // Extraemos el color que ya tiene asignado desde el diseñador
        Color colorNormal = boton.getForeground();
        
        // Calculamos automáticamente el color oscuro usando la función nativa de Java
        Color colorHover = colorNormal.darker();
        
        // Limpiar el botón de estilos por defecto de Swing
        boton.setContentAreaFilled(false);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        aplicarEstadoNormal(boton, colorNormal);
        
        // Inyectar el dinamismo con los eventos del ratón
        boton.addMouseListener(new MouseAdapter() {
            
            @Override
            public void mouseEntered(MouseEvent e) {
                // El ratón entra: oscurecer texto y pintar un borde inferior (subrayado)
                boton.setForeground(colorHover);
                boton.setBorderPainted(true);
                boton.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, colorHover));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // El ratón sale: restaurar color normal y quitar el subrayado
                aplicarEstadoNormal(boton, colorNormal);
            }
            
            //Limpia el hover al instante del clic físico (Evita el congelamiento en modales)
            @Override
            public void mousePressed(MouseEvent e){
                aplicarEstadoNormal(boton, colorNormal);
            }
            
        });
        
        //Si el botón deja de mostrarse (se oculta su ventana), limpia el hover
        boton.addHierarchyListener(e -> {
            if (!boton.isShowing()) {
                aplicarEstadoNormal(boton, colorNormal);
            }
        });
    }
    
    private static void aplicarEstadoNormal(JButton boton, Color colorNormal){
        boton.setForeground(colorNormal);
        boton.setBorderPainted(false);
        // Establecer el estado visual inicial (borde invisible para no alterar el tamaño)
        boton.setBorder(BorderFactory.createEmptyBorder(0, 0, 1, 0));
    }
}
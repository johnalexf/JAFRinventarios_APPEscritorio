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

    private final JButton boton;
    private Color colorNormal ;
    /**
     * Convierte un JButton estándar en un enlace con efecto Hover (subrayado y oscurecimiento automático).
     * El color base se extrae directamente de las propiedades del diseñador visual.
     * 
     * @param boton El botón a transformar.
     */
    
    
    private DinamismoLink(JButton boton){
        this.boton = boton;
        
        limpiarEstilosBoton();
        
        inicializarEventosBoton();
    }
    
    private void limpiarEstilosBoton(){
        // Limpiar el botón de estilos por defecto de Swing
        boton.setContentAreaFilled(false);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        aplicarEstadoNormal();
        
    }
    
    private void aplicarEstadoNormal(){
        if(colorNormal != null){
            boton.setForeground(colorNormal);
        }
        colorNormal = null;
        boton.setBorderPainted(false);
        // Establecer el estado visual inicial (borde invisible para no alterar el tamaño)
        boton.setBorder(BorderFactory.createEmptyBorder(0, 0, 1, 0));
    }
    
    
    private void inicializarEventosBoton(){
        // Inyectar el dinamismo con los eventos del ratón
        boton.addMouseListener(new MouseAdapter() {
            
            @Override
            public void mouseEntered(MouseEvent e) {
                colorNormal = boton.getForeground();
                // El ratón entra: oscurecer texto y pintar un borde inferior (subrayado)
                Color colorHover = colorNormal.darker();
                boton.setForeground(colorHover);
                boton.setBorderPainted(true);
                boton.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, colorHover));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // El ratón sale: restaurar color normal y quitar el subrayado
                aplicarEstadoNormal();
            }
            
            //Limpia el hover al instante del clic físico (Evita el congelamiento en modales)
            @Override
            public void mousePressed(MouseEvent e){
                aplicarEstadoNormal();
            }
            
        });
        
        //Si el botón deja de mostrarse (se oculta su ventana), limpia el hover
        boton.addHierarchyListener(e -> {
            if (!boton.isShowing()) {
                aplicarEstadoNormal();
            }
        });
    }
    

    public static void aplicarEfecto(JButton boton) {
        
        new DinamismoLink(boton);
        
    }

}
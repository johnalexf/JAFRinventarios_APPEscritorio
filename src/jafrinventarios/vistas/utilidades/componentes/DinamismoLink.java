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
        
        aplicarEstilosBase();
        
        inicializarEventos();
    }
    
    
    private void aplicarEstilosBase(){
        // Limpiar el botón de estilos por defecto de Swing
        boton.setContentAreaFilled(false);
        boton.setFocusPainted(false);
        // Aplicar cursor
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
    
    
    private void inicializarEventos(){
        // Inyectar el dinamismo con los eventos del ratón
        boton.addMouseListener(new MouseAdapter() {
            
            @Override
            public void mouseEntered(MouseEvent e) {
                colorNormal = boton.getForeground();
                // El ratón entra: oscurece o aclara el texto y pintar un borde inferior (subrayado)
                
                // Calcular la luminancia (brillo percibido) del color actual
                // Fórmula estándar: 0.299*R + 0.587*G + 0.114*B
                double luminancia = (0.299 * colorNormal.getRed() + 
                                     0.587 * colorNormal.getGreen() + 
                                     0.114 * colorNormal.getBlue());
                
                Color colorHover;
                // Si el color es oscuro (luminancia menor a 80), lo aclaramos. Si es claro, lo oscurecemos.
                if (luminancia < 80) {
                    // No se utiliza colorNormal.brighter() por que la diferencia no se nota.
                    // Aclarado manual: Forzamos sumar +45 a cada canal de color
                    // Math.min asegura que el valor nunca se pase del límite máximo de 255
                    int r = Math.min(255, colorNormal.getRed() + 80);
                    int g = Math.min(255, colorNormal.getGreen() + 80);
                    int b = Math.min(255, colorNormal.getBlue() + 80);
                    colorHover = new Color(r, g, b);
                } else {
                    // Para colores medios y claros, el darker() nativo funciona perfecto
                    colorHover = colorNormal.darker();
                    
                }
              
                
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
    

    /*
    ============================================================================
        METODO ESTATICO UNICO PARA DECLARAR LA INTENCION DE ESTA CLASE
    ============================================================================
    Al usar esta clase la idea es presentar solo este metodo para que se 
    entienda que no se espera retornar un objeto si no que el mismo le 
    aplicara un efecto a un boton de tipo link sin necesidad de instanciarlo
    desde afuera.
    */
    public static void aplicarEfecto(JButton boton) {       
        new DinamismoLink(boton); 
    }

}
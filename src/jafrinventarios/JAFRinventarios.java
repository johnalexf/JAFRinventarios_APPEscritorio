/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jafrinventarios;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import jafrinventarios.vistas.acceso.AccesoFrame;


/**
 *
 * @author JOHN FORERO
 */
public class JAFRinventarios {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
      
        // Registrar la carpeta donde están los temas
        FlatLaf.registerCustomDefaultsSource("jafrinventarios.temas");
        
        // Activar FlatLaf (esto cambia el motor gráfico globalmente)
        FlatLightLaf.setup();
        
        
        // Le pasamos la tarea de crear la ventana al "trabajador exclusivo" de la interfaz (EDT), quitandoselo al main de esta clase
        // Si dejamos que el programa principal intente dibujarla directamente, 
        // chocarían haciendo el mismo trabajo y la pantalla se podría congelar.
        java.awt.EventQueue.invokeLater(() -> {

            AccesoFrame vistaInicial = new AccesoFrame();

            vistaInicial.setVisible(true);

        });
        
       
    }
    
}

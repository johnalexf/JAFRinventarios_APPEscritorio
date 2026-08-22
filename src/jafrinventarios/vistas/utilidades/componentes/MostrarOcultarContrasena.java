/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jafrinventarios.vistas.utilidades.componentes;

import jafrinventarios.vistas.utilidades.iconos.IconosBotones;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JPasswordField;

/**
 *
 * @author JOHN FORERO
 */
public final class MostrarOcultarContrasena {

    private static final ImageIcon ICONO_MOSTRAR = IconosBotones.OJO_ABIERTO.getIcono();
    private static final ImageIcon ICONO_OCULTAR = IconosBotones.OJO_CERRADO.getIcono();
    
    //Se pone en privado el constructor para evitar que se creen objetos con esta clase
    //No se borra el constructor por que java por defecto lo creara publico
    private MostrarOcultarContrasena() {
    }
    
    
     /**
     * Agrega al botón la funcionalidad de mostrar u ocultar la contraseña..
     * @param inputContrasena campo de contraseña a manipular como muestra su contenido
     * @param btnMostrarOcultarContrasena boton para manipular al campo contraseña
     */
    public static void agregarFuncionalidad( JPasswordField inputContrasena, AbstractButton btnMostrarOcultarContrasena ) {

        // Guardamos el carácter original con que se muestra la contraseña
        // oculta, que puede ser : (•, *, otros..) depende de version de java
        char caracterOriginal = inputContrasena.getEchoChar();

        // Estado inicial
        btnMostrarOcultarContrasena.setIcon(ICONO_MOSTRAR);

        btnMostrarOcultarContrasena.addActionListener(e -> {

            boolean contrasenaOculta = ( inputContrasena.getEchoChar() == caracterOriginal );

            if (contrasenaOculta) {
                // Mostrar contraseña
                inputContrasena.setEchoChar((char) 0);
                btnMostrarOcultarContrasena.setIcon(ICONO_OCULTAR);
               
            } else {
                // Ocultar contraseña
                inputContrasena.setEchoChar(caracterOriginal);
                btnMostrarOcultarContrasena.setIcon(ICONO_MOSTRAR);

            }
        });
    }

}

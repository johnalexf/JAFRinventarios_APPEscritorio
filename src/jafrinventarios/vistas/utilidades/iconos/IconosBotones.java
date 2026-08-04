/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jafrinventarios.vistas.utilidades.iconos;

import javax.swing.ImageIcon;

/**
 *
 * @author JOHN FORERO
 */
public enum IconosBotones {
    MENU("material-symbols--menu.png"),
    CERRAR("carbon--close-filled.png"),
    AYUDA("fluent--chat-help-24-filled.png"),
    OJO_CERRADO("mdi-light--eye-off.png"),
    OJO_ABIERTO("mdi-light--eye.png"),
    EDITAR("boxicons--edit-filled.png"),
    COPIAR("pixel--copy-solid.png");
    
    private final String nombreArchivo;
    private final String RUTA_ICONOS = "/jafrinventarios/recursos/iconos/botones/";

    private IconosBotones(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }
    
    public ImageIcon getIcono(){
        return  new ImageIcon(
                IconosBotones.class.getResource(
                        RUTA_ICONOS + nombreArchivo
                )
        );
    }
    
}

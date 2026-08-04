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
public enum IconosDialogosMensajePersonalizado {
    ERROR("vaadin--ban.png"),
    ADVERTENCIA("noto-v1--warning.png"),
    INFORMACION("mdi--information.png"),
    EXITO("lets-icons--check-fill.png"),
    CARGANDO("fluent--spinner-ios-16-filled.png");
    
    private final String nombreArchivo;
    private final String RUTA_ICONOS = "/jafrinventarios/recursos/iconos/dialogos/";

    private IconosDialogosMensajePersonalizado( String nombreArchivo ) {
        this.nombreArchivo = nombreArchivo;
    }
    
    public ImageIcon getIcono(){
        return  new ImageIcon(
                IconosDialogosMensajePersonalizado.class.getResource(
                        RUTA_ICONOS + nombreArchivo
                )
        );
    }
    
    
}

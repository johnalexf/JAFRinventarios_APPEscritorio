
package jafrinventarios.vistas.utilidades.iconos;

import javax.swing.ImageIcon;

/**
 *
 * @author JOHN FORERO
 */
public enum IconoDialogo {
    ERROR("vaadin--ban.png"),
    ADVERTENCIA("noto-v1--warning.png"),
    INFORMACION("mdi--information.png"),
    EXITO("lets-icons--check-fill.png"),
    CARGANDO("fluent--spinner-ios-16-filled.png");
    
    private final String nombreArchivo;
    private final String RUTA_ICONOS = "/jafrinventarios/recursos/iconos/dialogos/";

    private IconoDialogo( String nombreArchivo ) {
        this.nombreArchivo = nombreArchivo;
    }
    
    public ImageIcon getIcono(){
        return  new ImageIcon(
                IconoDialogo.class.getResource(
                        RUTA_ICONOS + nombreArchivo
                )
        );
    }
    
    
}

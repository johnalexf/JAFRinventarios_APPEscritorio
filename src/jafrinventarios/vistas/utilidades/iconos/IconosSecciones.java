
package jafrinventarios.vistas.utilidades.iconos;

import javax.swing.ImageIcon;

/**
 *
 * @author JOHN FORERO
 */
public enum IconosSecciones {
    INICIO("mdi--home.png", "Inicio"),
    USUARIOS("mdi--users.png", "Usuarios"),
    PRODUCTOS("mdi--bread.png", "Productos"),
    PROVEEDORES("clarity--building-line.png", "Proveedores"),
    CLIENTES("bxs--store.png", "Clientes"),
    COMPRAS("fa7-solid--bag-shopping.png", "Compras"),
    VENTAS("bi--cash-coin.png", "Ventas"),
    INVENTARIO("mingcute--cube-fill.png", "Inventario"),
    REPORTE("mdi--file-chart.png", "Reporte");
    
    private final String nombreSeccion;
    private final String nombreArchivo;
    private final String RUTA_ICONOS = "/jafrinventarios/recursos/iconos/secciones/";

    private IconosSecciones(String nombreArchivo, String nombreSeccion) {
        this.nombreArchivo = nombreArchivo;
        this.nombreSeccion = nombreSeccion;
    }
    
    public ImageIcon getIcono(){
        return  new ImageIcon(
                IconosSecciones.class.getResource(
                        RUTA_ICONOS + nombreArchivo
                )
        );
    }
    
    public String getNombreSeccion(){
        return nombreSeccion;
    }
    
}

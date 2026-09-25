
package jafrinventarios.controladores.reportes;

import jafrinventarios.servicios.clientes.ServicioClientes;
import jafrinventarios.servicios.productos.ServicioProductos;
import jafrinventarios.servicios.proveedores.ServicioProveedores;
import jafrinventarios.servicios.usuarios.ServicioUsuarios;
import jafrinventarios.vistas.reportes.ReportePanel;
import jafrinventarios.vistas.reportes.ReportePanel.TipoReporteEspecial;
import java.util.LinkedHashMap;

/**
 *
 * @author JOHN FORERO
 */
public class ControladorReportes {
    
    private final ReportePanel panelReportes;
    
    private TipoReporteEspecial tipoReporte;

/*
============================================================================
                    CONSTRUCTOR PUBLICO
============================================================================
*/
    public ControladorReportes(ReportePanel panelReportes) {
        this.panelReportes = panelReportes;
        inicializarBotonesPrincipales();
    }
    
/*
============================================================================
            METODOS PARA CONSULTAR A LOS SERVICIOS
============================================================================
*/
    
    private LinkedHashMap<Integer, String> obtenerDiccionarioProveedores() throws Exception{
        return ServicioProveedores.obtenerDiccionarioProveedores( false );
    }
    
    private LinkedHashMap<Integer, String> obtenerDiccionarioClientes() throws Exception{
        return ServicioClientes.obtenerDiccionarioClientes( false );
    }
    
    private LinkedHashMap<Integer, String> obtenerDiccionarioProductosPorProveedor( int idProveedor ) throws Exception{
        return ServicioProductos.obtenerDiccionarioProductosPorProveedor(idProveedor);
    }
    
    private LinkedHashMap<Integer, String> obtenerDiccionarioProductos() throws Exception{
        return ServicioProductos.obtenerDiccionarioProductos();
    }
    
    private LinkedHashMap<Integer, String> obtenerDiccionarioUsuarios() throws Exception{
        return ServicioUsuarios.obtenerDiccionarioUsuarios();
    }
    
    
/*
============================================================================
                    INICIALIZAR EVENTOS BOTONES PRINCIPALES
============================================================================
*/
    private void inicializarBotonesPrincipales(){
    
        panelReportes.getBtnReporteCantidadComprar().addActionListener( e -> { 
            generarReporteCantidadesComprar();
        });
        
        panelReportes.getBtnReporteCompras().addActionListener( e -> { 
            mostrarFiltroReporte( TipoReporteEspecial.ReporteCompras );
        });
        
        panelReportes.getBtnReporteVentas().addActionListener( e -> { 
            mostrarFiltroReporte( TipoReporteEspecial.ReporteVentas );
        });
        
        panelReportes.getBtnLinkCancelar().addActionListener( e -> { 
            panelReportes.mostrarPanelConfiguracionReporte(false);
        });
    }

/*
============================================================================
        METODOS PARA LAS ACCIONES (CREAR REPORTE DEPENDIENDO DEL TIPO)
============================================================================
*/
    private void generarReporteCantidadesComprar(){
    
    }
    
    private void mostrarFiltroReporte( TipoReporteEspecial tipoReporte ){
        this.tipoReporte = tipoReporte;
        panelReportes.mostrarConfiguracionReporte( tipoReporte );
    }
    
    
    
}

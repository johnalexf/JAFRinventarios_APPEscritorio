
package jafrinventarios.controladores.reportes;

import jafrinventarios.vistas.reportes.ReportePanel;
import jafrinventarios.vistas.reportes.ReportePanel.TipoReporteEspecial;

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

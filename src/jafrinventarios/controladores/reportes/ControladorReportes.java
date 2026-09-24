
package jafrinventarios.controladores.reportes;

import jafrinventarios.vistas.reportes.ReportePanel;

/**
 *
 * @author JOHN FORERO
 */
public class ControladorReportes {
    
    private final ReportePanel panelReportes;

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
            mostrarFiltroReporte();
        });
        
        panelReportes.getBtnReporteVentas().addActionListener( e -> { 
            mostrarFiltroReporte();
        });
        
    }

/*
============================================================================
        METODOS PARA LAS ACCIONES (CREAR REPORTE DEPENDIENDO DEL TIPO)
============================================================================
*/
    private void generarReporteCantidadesComprar(){
    
    }
    
    private void mostrarFiltroReporte(){
    
    }
    
    
    
}

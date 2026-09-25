
package jafrinventarios.controladores.reportes;

import jafrinventarios.servicios.clientes.ServicioClientes;
import jafrinventarios.servicios.productos.ServicioProductos;
import jafrinventarios.servicios.proveedores.ServicioProveedores;
import jafrinventarios.servicios.usuarios.ServicioUsuarios;
import jafrinventarios.vistas.reportes.ReportePanel;
import jafrinventarios.vistas.reportes.ReportePanel.TipoReporteEspecial;
import java.awt.event.ItemEvent;
import java.util.HashMap;
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
        /*
        NOTA: 
        Se inicializa los eventos para la seccion del filtro de reportes
        para ventas y compras, con esto se garantiza que una unica vez se va 
        realizar la inicializacion y que esta no se ejecute cada vez que el 
        usuario salga y entre a la seleccion de filtros.
        */
        inicializarBotonesPrincipales();
        poblarComboBoxIndependientes();
        inicializarCheckBoxs();
        inicializarEventoComboBoxProveedores();
        
        
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
    Poblar de la lista pertinente para los combo box independientes
    es decir todos menos productos.
============================================================================
*/
    private void poblarComboBoxIndependientes(){
    
        try {
            panelReportes.setListaProveedores( obtenerDiccionarioProveedores() );
            panelReportes.setListaClientes( obtenerDiccionarioClientes() );
            panelReportes.setListaUsuarios( obtenerDiccionarioUsuarios() );
        } catch (Exception e) {
            panelReportes.mostrarAlertaError(e.getMessage());
        }
    
    }

    
/*
============================================================================
                  INICIALIZAR EVENTOS CHECK BOXS
============================================================================
*/
    public void inicializarCheckBoxs(){
    
        panelReportes.getCheckBoxProveedores().addItemListener( (e) -> {
            if( e.getStateChange() == ItemEvent.SELECTED ){
                panelReportes.habilitarProveedores(true);
            }else if( e.getStateChange() == ItemEvent.DESELECTED  ){
                panelReportes.habilitarProveedores(false);
            }
        });
        
        panelReportes.getCheckBoxClientes().addItemListener((e) -> {
            if( e.getStateChange() == ItemEvent.SELECTED ){
                panelReportes.habilitarClientes(true);
            }else if( e.getStateChange() == ItemEvent.DESELECTED  ){
                panelReportes.habilitarClientes(false);
            }
        });
        
        panelReportes.getCheckBoxProductos().addItemListener((e) -> {
            if( e.getStateChange() == ItemEvent.SELECTED ){
                panelReportes.habilitarProductos(true);
            }else if( e.getStateChange() == ItemEvent.DESELECTED  ){
                panelReportes.habilitarProductos(false);
            }
        });
        
        panelReportes.getCheckBoxUsuarios().addItemListener((e) -> {
            if( e.getStateChange() == ItemEvent.SELECTED ){
                panelReportes.habilitarUsuarios(true);
            }else if( e.getStateChange() == ItemEvent.DESELECTED  ){
                panelReportes.habilitarUsuarios(false);
            }
        });
        
    }
    
    
    /*
    ============================================================================
                INICIALIZAR evento en comboBox proveedores
    ============================================================================
    */
    
    private void inicializarEventoComboBoxProveedores(){
    
        panelReportes.getComboBoxProveedores().addActionListener( e -> {  
            try {
                 HashMap<String, String> datosFormulario = panelReportes.recolectarDatosFormulario();
                 if( datosFormulario.containsKey("proveedor") ){
                     int idProveedor = Integer.parseInt( datosFormulario.get("proveedor"));
                     panelReportes.setListaProductos( obtenerDiccionarioProductosPorProveedor(idProveedor));
                     panelReportes.habilitarCheckBoxProductos(true);
                 }
            } catch ( Exception exception ) {
                LinkedHashMap<Integer, String> listaVacia = new LinkedHashMap<>();
                panelReportes.setListaProductos( listaVacia );
                panelReportes.habilitarCheckBoxProductos(false);
            }
            
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
        switch(tipoReporte){
            case ReporteCompras:
                panelReportes.habilitarCheckBoxProductos(false);
                break;
            case ReporteVentas:
                panelReportes.habilitarCheckBoxProductos(true);
                try {
                    panelReportes.setListaProductos( obtenerDiccionarioProductos() );
                } catch (Exception e) {
                    panelReportes.mostrarAlertaError( e.getMessage() );
                }
                break;
        }
        panelReportes.mostrarConfiguracionReporte( tipoReporte );
        
    }
    
    
    
}

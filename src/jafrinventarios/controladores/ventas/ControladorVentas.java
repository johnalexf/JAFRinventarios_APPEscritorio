
package jafrinventarios.controladores.ventas;

import jafrinventarios.DTOs.ventas.DTODetalleVentaTabla;
import jafrinventarios.DTOs.ventas.DTOVentaTabla;
import jafrinventarios.controladores.utilidades.ControladorBusquedaYAccionLibre;
import jafrinventarios.controladores.utilidades.FuncionesBusquedaYAccionLibre;
import jafrinventarios.controladores.utilidades.ResultadoDialogo;
import jafrinventarios.modelos.ModeloSesionUsuario;
import jafrinventarios.servicios.ventas.ServicioVentas;
import jafrinventarios.vistas.ventas.FilaTablaDetalleVenta;
import jafrinventarios.vistas.ventas.FilaTablaVentas;
import jafrinventarios.vistas.ventas.VentasPanel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author JOHN FORERO
 */
public class ControladorVentas {
    
    private final VentasPanel panelVentas;
    private final ServicioVentas servicioVentas;

    /*
    Diccionario con cada fila de cada registro de ventas, permite:
    * Buscar el boton para asignarle la funcion de editar
    * Buscar la fila de un registro que se edito
    * Elimnar la fila si el registro se elimino
    */
    private LinkedHashMap<Integer, FilaTablaVentas> diccionarioVentas;
    
    /*
    Variable para personalizar la vista
    */
    private boolean isAdministrador;
    
    /*
    ============================================================================
                        CONSTRUCTOR PUBLICO
    ============================================================================
    */
    public ControladorVentas(VentasPanel panelVentas, ServicioVentas servicioVentas) {
        this.panelVentas = panelVentas;
        this.servicioVentas = servicioVentas;
        this.isAdministrador = ModeloSesionUsuario.getInstancia().isAdministrador();
        this.diccionarioVentas = new LinkedHashMap<>(); 
        /*
        Instanciar el controlador de la barra de busqueda y boton de accion libre
        pasando como parametro la instancia de la interfaz que permite asignar
        las funciones correspondientes que necesita ejecutar el controlador de 
        la barra de busqueda.
        */
        new ControladorBusquedaYAccionLibre(
                panelVentas.getPanelBusquedaYAccionLibre(),
                funcionesBusquedaYAccionLibre(),
                "Nombre cliente, alias de usuario o nombre producto",
                "Agregar Nueva Venta",
                true
        );
        
        if(!isAdministrador)
            panelVentas.ocultarTituloEditar();
        
        mostrarTodosLasVentas();
        
    }
    
    
    /* 
    Metodo para crear la instancia de la interfaz FuncionesBusquedaYAccionLibre
    que contiene el metodo para poder buscar segun un filtro
    y para ejecutar la accion libre (crear venta).   
    */
    private FuncionesBusquedaYAccionLibre funcionesBusquedaYAccionLibre(){
        return new FuncionesBusquedaYAccionLibre() {
            
            @Override
            public boolean ejecutarBusqueda(String terminoBusqueda) {
                return procesarBusqueda(terminoBusqueda);
            }
            
            @Override
            public void limpiarBusqueda(){
                diccionarioVentas.clear();
                mostrarTodosLasVentas();
            }

            @Override
            public void ejecutarAccionLibre() {
                crearVenta();
            }
            
        };
    }
    
    
    /*
    ============================================================================
                    METODOS PARA CONSULTAR AL SERVICIO
    ============================================================================
    */
    private List<DTOVentaTabla> obtenerTodasLasVentas() throws Exception{
       return servicioVentas.obtenerTodasLasVentas();  
    }
        
    private List<DTOVentaTabla> obtenerListaVentasPorFiltro( String filtro ) throws Exception{
       return servicioVentas.obtenerListaVentasPorFiltro( filtro );  
    }
    
    private DTOVentaTabla obtenerDatosVenta( int idVenta ) throws Exception{
        return servicioVentas.obtenerDatosDTOVenta(idVenta );
    }
    
    
    /*
    ============================================================================
                METODOS PARA EL CONTROL DE LAS FILAS DE LA TABLA
    ============================================================================
    */
    private FilaTablaVentas asignarDatosAFilaVenta ( FilaTablaVentas filaVenta, DTOVentaTabla datosVenta ){
        SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
        filaVenta.setDatos(
            datosVenta.getIdVenta(),
            datosVenta.getNombreNegocioCliente(),
            formateador.format( datosVenta.getFechaHoraVenta()),
            datosVenta.getTotalVenta(),
            datosVenta.getAliasUsuario()            
        );
        if( !isAdministrador )
            filaVenta.ocultarBtnEditar();
        
        ArrayList<FilaTablaDetalleVenta> filasDetalleVenta = new ArrayList<>();
        int item = 0;
        for( DTODetalleVentaTabla detalle : datosVenta.getDetalles() ){
            FilaTablaDetalleVenta filaDetalle = new FilaTablaDetalleVenta();
            filaDetalle.setDatos(   ++item, 
                                    detalle.getNombreProducto(), 
                                    detalle.getCantidadProducto(), 
                                    detalle.getPrecioUnitarioProducto(), 
                                    detalle.getPrecioTotalProducto());
            filasDetalleVenta.add( filaDetalle );
        }
        
        filaVenta.inyectarFilasDetalles( filasDetalleVenta );
        
        return filaVenta;
    }
    
    
    private FilaTablaVentas crearNuevaFila ( DTOVentaTabla datosVenta ){
        FilaTablaVentas filaVenta = new FilaTablaVentas();
        return asignarDatosAFilaVenta(filaVenta, datosVenta);
    }
    
    
    private void agregarFilaADiccionario( int id, FilaTablaVentas filaVenta){
         diccionarioVentas.put( id, filaVenta );
    }
    
    
    private void estructurarDiccionario ( List<DTOVentaTabla> listaVentas ){
        listaVentas.forEach( venta -> {
            FilaTablaVentas filaVenta = crearNuevaFila( venta );
            if(isAdministrador)
                inicializarBotonEditar( venta.getIdVenta(), filaVenta );
            agregarFilaADiccionario( venta.getIdVenta(), filaVenta );
        } );
    
    }
    
    
    private void mostrarTodosLasVentas(){

        try {
            List<DTOVentaTabla> listaVentas = obtenerTodasLasVentas();
            if (!listaVentas.isEmpty()){
                estructurarDiccionario( listaVentas );
                panelVentas.inyectarFilas( diccionarioVentas );
            }
        } catch (Exception e) {
            panelVentas.mostrarModalError(e.getMessage());
        }
        
    }
    
    
    /*
    ======================================================================================
     METODO PARA ASIGNAR EL LISTENER AL BOTON DE EDITAR VENTA DE UNA FilaTablaVentas
    ======================================================================================
    */
        
    private void inicializarBotonEditar( Integer id, FilaTablaVentas fila ){
        fila.getBtnEditar().addActionListener(e -> editarVenta( id ) );
    }
    
    
    /*
    ============================================================================
                METODOS PARA LAS ACCIONES (CREAR, EDITAR Y BUSCAR)
    ============================================================================
    */
    
       
    private void crearVenta(){

        int idVentaCreada = ControladorDialogoVentas.crearVenta( 
                        panelVentas.getVentanaPadre() , 
                        servicioVentas
        );
        
        if(idVentaCreada != -1){
            
            try {
                DTOVentaTabla venta = obtenerDatosVenta( idVentaCreada );
                FilaTablaVentas fila = crearNuevaFila( venta );
                inicializarBotonEditar( venta.getIdVenta(), fila );
                
                boolean diccionarioVacio = diccionarioVentas.isEmpty();
                agregarFilaADiccionario( venta.getIdVenta(), fila );
                
                /*
                Si el diccionario estaba vacio significa que en la vista aun
                se muestra el mensaje de no hay ventas, por tanto es necesario
                remover el contenido, para ahi si asignarle una nueva fila
                */
                if( diccionarioVacio ) panelVentas.removerContenido();
                panelVentas.inyectarNuevaFila( fila );
                
            }catch (Exception e) {
                panelVentas.mostrarModalError(e.getMessage());
            }

        }
    }
    
        
    private void editarVenta( Integer idVenta ){
        
        ResultadoDialogo resultadoOperacion = 
                ControladorDialogoVentas.editarVenta(
                    panelVentas.getVentanaPadre() , idVenta, servicioVentas
                );
        
        if( resultadoOperacion == ResultadoDialogo.ACTUALIZADO ){  
            try {
                DTOVentaTabla venta = obtenerDatosVenta( idVenta );
                FilaTablaVentas fila = diccionarioVentas.get( idVenta );
                asignarDatosAFilaVenta( fila, venta );
            }catch (Exception e) {
                panelVentas.mostrarModalError( e.getMessage() );
            }
        }
        
        if( resultadoOperacion == ResultadoDialogo.ELIMINADO ){
            FilaTablaVentas fila = diccionarioVentas.get( idVenta );
            panelVentas.eliminarFila( fila );
            diccionarioVentas.remove( idVenta );
        }
        
    }
    
    
    private boolean procesarBusqueda( String filtro ){

        try {
            List<DTOVentaTabla> listaVentas = obtenerListaVentasPorFiltro( filtro );
            if( listaVentas.isEmpty() ){
                return false;
            }else{
                diccionarioVentas.clear();
                estructurarDiccionario( listaVentas );
                panelVentas.inyectarFilas( diccionarioVentas );
                return true;
            }
        } catch (Exception e) {
            panelVentas.mostrarModalError(e.getMessage());
            return false;
        }

    }

    
}

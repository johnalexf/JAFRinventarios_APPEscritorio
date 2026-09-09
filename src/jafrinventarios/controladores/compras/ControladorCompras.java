
package jafrinventarios.controladores.compras;

import jafrinventarios.DTOs.compras.DTOCompraTabla;
import jafrinventarios.DTOs.compras.DTODetalleCompraTabla;
import jafrinventarios.controladores.utilidades.ControladorBusquedaYAccionLibre;
import jafrinventarios.controladores.utilidades.FuncionesBusquedaYAccionLibre;
import jafrinventarios.controladores.utilidades.ResultadoDialogo;
import jafrinventarios.modelos.ModeloSesionUsuario;
import jafrinventarios.servicios.compras.ServicioCompras;
import jafrinventarios.vistas.compras.ComprasPanel;
import jafrinventarios.vistas.compras.FilaTablaCompras;
import jafrinventarios.vistas.compras.FilaTablaDetalleCompra;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author JOHN FORERO
 */
public class ControladorCompras {
    
    private final ComprasPanel panelCompras;
    private final ServicioCompras servicioCompras;

    /*
    Diccionario con cada fila de cada registro de compras, permite:
    * Buscar el boton para asignarle la funcion de editar
    * Buscar la fila de un registro que se edito
    * Elimnar la fila si el registro se elimino
    */
    private LinkedHashMap<Integer, FilaTablaCompras> diccionarioCompras;
    
    /*
    Variable para personalizar tanto la vista como para la consultas
    a los metodos del servicio
    */
    private boolean isAdministrador;
    
    /*
    ============================================================================
                        CONSTRUCTOR PUBLICO
    ============================================================================
    */
    public ControladorCompras(ComprasPanel panelCompras, ServicioCompras servicioCompras) {
        this.panelCompras = panelCompras;
        this.servicioCompras = servicioCompras;
        this.isAdministrador = ModeloSesionUsuario.getInstancia().isAdministrador();
        this.diccionarioCompras = new LinkedHashMap<>(); 
        /*
        Instanciar el controlador de la barra de busqueda y boton de accion libre
        pasando como parametro la instancia de la interfaz que permite asignar
        las funciones correspondientes que necesita ejecutar el controlador de 
        la barra de busqueda.
        */
        new ControladorBusquedaYAccionLibre(
                panelCompras.getPanelBusquedaYAccionLibre(),
                funcionesBusquedaYAccionLibre(),
                "Nombre proveedor, alias de usuario o nombre producto",
                "Agregar Nueva Compra",
                isAdministrador
        );
        
        if(!isAdministrador)
            panelCompras.ocultarTituloEditar();
        
        mostrarTodosLasCompras();
        
    }
    
    
    /* 
    Metodo para crear la instancia de la interfaz FuncionesBusquedaYAccionLibre
    que contiene el metodo para poder buscar segun un filtro
    y para ejecutar la accion libre (crear compra).   
    */
    private FuncionesBusquedaYAccionLibre funcionesBusquedaYAccionLibre(){
        return new FuncionesBusquedaYAccionLibre() {
            
            @Override
            public boolean ejecutarBusqueda(String terminoBusqueda) {
                return procesarBusqueda(terminoBusqueda);
            }
            
            @Override
            public void limpiarBusqueda(){
                diccionarioCompras.clear();
                mostrarTodosLasCompras();
            }

            @Override
            public void ejecutarAccionLibre() {
                crearCompra();
            }
            
        };
    }
    
    
    /*
    ============================================================================
                METODOS PARA CONSULTAR AL SERVICIO
    ============================================================================
    */
    private List<DTOCompraTabla> obtenerTodasLasCompras() throws Exception{
       return servicioCompras.obtenerTodasLasCompras();  
    }
        
    private List<DTOCompraTabla> obtenerListaComprasPorFiltro( String filtro ) throws Exception{
       return servicioCompras.obtenerListaComprasPorFiltro( filtro );  
    }
    
    private DTOCompraTabla obtenerDatosCompra( int idCompra ) throws Exception{
        return servicioCompras.obtenerDatosDTOCompra(idCompra );
    }
    
    
    /*
    ============================================================================
                METODOS PARA EL CONTROL DE LAS FILAS DE LA TABLA
    ============================================================================
    */
    private FilaTablaCompras asignarDatosAFilaCompra ( FilaTablaCompras filaCompra, DTOCompraTabla datosCompra ){
        DateTimeFormatter formateador = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm a");
        filaCompra.setDatos(
            datosCompra.getIdCompra(),
            datosCompra.getNombreComercialProveedor(),
            datosCompra.getFechaHoraCompra().format(formateador),
            datosCompra.getTotalCompra(),
            datosCompra.getAliasUsuario()            
        );
        if( !isAdministrador )
            filaCompra.ocultarBtnEditar();
        
        ArrayList<FilaTablaDetalleCompra> filasDetalleCompra = new ArrayList<>();
        int item = 0;
        for( DTODetalleCompraTabla detalle : datosCompra.getDetalles() ){
            FilaTablaDetalleCompra filaDetalle = new FilaTablaDetalleCompra();
            filaDetalle.setDatos(   ++item, 
                                    detalle.getNombreProducto(), 
                                    detalle.getCantidadProducto(), 
                                    detalle.getPrecioUnitarioProducto(), 
                                    detalle.getPrecioTotalProducto());
            filasDetalleCompra.add( filaDetalle );
        }
        
        filaCompra.inyectarFilasDetalles( filasDetalleCompra );
        
        return filaCompra;
    }
    
    
    private FilaTablaCompras crearNuevaFila ( DTOCompraTabla datosCompra ){
        FilaTablaCompras filaCompra = new FilaTablaCompras();
        return asignarDatosAFilaCompra(filaCompra, datosCompra);
    }
    
    
    private void agregarFilaADiccionario( int id, FilaTablaCompras filaCompra){
         diccionarioCompras.put( id, filaCompra );
    }
    
    
    private void estructurarDiccionario ( List<DTOCompraTabla> listaCompras ){
        listaCompras.forEach( compra -> {
            FilaTablaCompras filaCompra = crearNuevaFila( compra );
            if(isAdministrador)
                inicializarBotonEditar( compra.getIdCompra(), filaCompra );
            agregarFilaADiccionario( compra.getIdCompra(), filaCompra );
        } );
    
    }
    
    
    private void mostrarTodosLasCompras(){

        try {
            List<DTOCompraTabla> listaCompras = obtenerTodasLasCompras();
            if (!listaCompras.isEmpty()){
                estructurarDiccionario( listaCompras );
                panelCompras.inyectarFilas( diccionarioCompras );
            }
        } catch (Exception e) {
            panelCompras.mostrarModalError(e.getMessage());
        }
        
    }
    
    
    /*
    ======================================================================================
     METODO PARA ASIGNAR EL LISTENER AL BOTON DE EDITAR COMPRA DE UNA FilaTablaCompras
    ======================================================================================
    */
        
    private void inicializarBotonEditar( Integer id, FilaTablaCompras fila ){
        fila.getBtnEditar().addActionListener(e -> editarCompra( id ) );
    }
    
    
    /*
    ============================================================================
                METODOS PARA LAS ACCIONES (CREAR, EDITAR Y BUSCAR)
    ============================================================================
    */
    
       
    private void crearCompra(){

        int idCompraCreada = ControladorDialogoCompra.crearCompra( 
                        panelCompras.getVentanaPadre() , 
                        servicioCompras
        );
        
        if(idCompraCreada != -1){
            
            try {
                DTOCompraTabla compra = obtenerDatosCompra( idCompraCreada );
                FilaTablaCompras fila = crearNuevaFila( compra );
                inicializarBotonEditar( compra.getIdCompra(), fila );
                
                boolean diccionarioVacio = diccionarioCompras.isEmpty();
                agregarFilaADiccionario( compra.getIdCompra(), fila );
                
                /*
                Si el diccionario estaba vacio significa que en la vista aun
                se muestra el mensaje de no hay compras, por tanto es necesario
                remover el contenido, para ahi si asignarle una nueva fila
                */
                if( diccionarioVacio ) panelCompras.removerContenido();
                panelCompras.inyectarNuevaFila( fila );
                
            }catch (Exception e) {
                panelCompras.mostrarModalError(e.getMessage());
            }

        }
    }
    
        
    private void editarCompra( Integer idCompra ){
        
        ResultadoDialogo resultadoOperacion = 
                ControladorDialogoCompra.editarCompra(
                    panelCompras.getVentanaPadre() , idCompra, servicioCompras
                );
        
        if( resultadoOperacion == ResultadoDialogo.ACTUALIZADO ){  
            try {
                DTOCompraTabla compra = obtenerDatosCompra( idCompra );
                FilaTablaCompras fila = diccionarioCompras.get( idCompra );
                asignarDatosAFilaCompra( fila, compra );
            }catch (Exception e) {
                panelCompras.mostrarModalError( e.getMessage() );
            }
        }
        
        if( resultadoOperacion == ResultadoDialogo.ELIMINADO ){
            FilaTablaCompras fila = diccionarioCompras.get( idCompra );
            panelCompras.eliminarFila( fila );
            diccionarioCompras.remove( idCompra );
        }
        
    }
    
    
    private boolean procesarBusqueda( String filtro ){

        try {
            List<DTOCompraTabla> listaCompras = obtenerListaComprasPorFiltro( filtro );
            if( listaCompras.isEmpty() ){
                return false;
            }else{
                diccionarioCompras.clear();
                estructurarDiccionario( listaCompras );
                panelCompras.inyectarFilas( diccionarioCompras );
                return true;
            }
        } catch (Exception e) {
            panelCompras.mostrarModalError(e.getMessage());
            return false;
        }

    }

    
}

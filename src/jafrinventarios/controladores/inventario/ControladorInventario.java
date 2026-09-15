
package jafrinventarios.controladores.inventario;

import jafrinventarios.DTOs.productos.DTOProductoCantidad;
import jafrinventarios.DTOs.productos.DTOProductoProveedor;
import jafrinventarios.controladores.utilidades.ControladorBusquedaYAccionLibre;
import jafrinventarios.controladores.utilidades.FuncionesBusquedaYAccionLibre;
import jafrinventarios.modelos.ModeloSesionUsuario;
import jafrinventarios.servicios.productos.ServicioInventario;
import jafrinventarios.vistas.inventario.FilaTablaInventario;
import jafrinventarios.vistas.inventario.InventarioPanel;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author JOHN FORERO
 */
public class ControladorInventario {
    
    private ServicioInventario servicioInventario;
    
    private InventarioPanel panelInventario;
    
    private LinkedHashMap<FilaTablaInventario, DTOProductoProveedor> diccionarioFilasInventario;
    
    private ArrayList<DTOProductoCantidad> productosActualizar;
    
    private ControladorBusquedaYAccionLibre controladorBusquedaYAccionLibre;
    
    private boolean isAdministrador;
    
    /*
    ============================================================================
                        CONSTRUCTOR PUBLICO
    ============================================================================
    */
    public ControladorInventario(InventarioPanel panelInventario, ServicioInventario servicioInventario) {
        this.servicioInventario = servicioInventario;
        this.panelInventario = panelInventario;
        
        this.diccionarioFilasInventario = new LinkedHashMap<>();
        this.productosActualizar = new ArrayList<>();
        
        this.isAdministrador = ModeloSesionUsuario.getInstancia().isAdministrador();
        /*
        Instanciar el controlador de la barra de busqueda y boton de accion libre
        pasando como parametro la instancia de la interfaz que permite asignar
        las funciones correspondientes que necesita ejecutar el controlador de 
        la barra de busqueda.
        */
        controladorBusquedaYAccionLibre = new ControladorBusquedaYAccionLibre(
                panelInventario.getPanelBusquedaYAccionLibre(),
                funcionesBusquedaYAccionLibre(),
                "Nombre producto o nombre proveedor",
                "Verificar Inventario",
                isAdministrador //Determina si se muestra o no el boton de accion libre
        );
        
        mostrarTodosLosProductos();
        
        //Inicializar los botones del panelInventario (Cancelar verificacion y Finalizar Verificacion)
        inicializarEventosBotones();
        
    }
    
    /* 
    Metodo para crear la instancia de la interfaz FuncionesBusquedaYAccionLibre
    que contiene el metodo para poder buscar segun un filtro
    y para ejecutar la accion libre ( verificar inventario ).   
    */
    private FuncionesBusquedaYAccionLibre funcionesBusquedaYAccionLibre(){
        return new FuncionesBusquedaYAccionLibre() {
            
            @Override
            public boolean ejecutarBusqueda(String terminoBusqueda) {
                return procesarBusqueda(terminoBusqueda);
            }
            
            @Override
            public void limpiarBusqueda(){
                mostrarTodosLosProductos();
            }

            @Override
            public void ejecutarAccionLibre() {
                if(isAdministrador)
                    habilitarVerificarInventario();
            }
            
        };
    }
    
    
    private void configurarModoVerificacion( boolean modoVerificacion ){
    
        panelInventario.configurarModoVerificacion( modoVerificacion );
        
        diccionarioFilasInventario.forEach( ( filaInventario , producto ) -> {
            filaInventario.configurarModoVerificacion( modoVerificacion );
        });
        
        controladorBusquedaYAccionLibre.setEnableBotonAccionLibre( !modoVerificacion );
        controladorBusquedaYAccionLibre.setEnableBuscador( !modoVerificacion );
    
    }
    
    
    private void inicializarEventosBotones(){
        if(isAdministrador){
            panelInventario.getBtnCancelarVerificacion().addActionListener(e -> {
                    cancelarVerificacion();
            });
        }
    }
    
    
    /*
    ============================================================================
                METODOS PARA CONSULTAR A LOS SERVICIOS
    ============================================================================
    */
    
    private List<DTOProductoProveedor> obtenerTodosLosProductos() throws Exception{
        return servicioInventario.obtenerTodosLosProductos();
    }
    
    private List<DTOProductoProveedor> obtenerTodosLosProductosPorFiltro(String filtro) throws Exception{
        return servicioInventario.obtenerTodosLosProductosPorFiltro(filtro);
    }
    
    private void actualizarCantidadProductos ( ArrayList<DTOProductoCantidad> listaProductos ) throws Exception{
        servicioInventario.actualizarCantidadProductos(listaProductos);
    }
    
    
    /*
    ============================================================================
                METODOS PARA EL CONTROL DE LAS FILAS DE LA TABLA
    ============================================================================
    */
    
    private FilaTablaInventario crearNuevaFila ( DTOProductoProveedor producto ){
        FilaTablaInventario filaInventario = new FilaTablaInventario();
        
        filaInventario.setDatos( producto.getIdProducto(), 
                                 producto.getNombreProveedor(),
                                 producto.getNombreProducto(),
                                 producto.getCantidadDisponible()
        );
        
        if( isAdministrador )
            inicializarEventoCheckBox(filaInventario, producto);
        
        return filaInventario;
    }
    
    
    private void inicializarEventoCheckBox(FilaTablaInventario filaInventario, DTOProductoProveedor producto) {

        filaInventario.getCheckBoxConfirmar().addItemListener((ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if( filaInventario.obtenerValorNuevaCantidad().isEmpty() )
                    filaInventario.asignarValorNuevaCantidad( producto.getCantidadDisponible());
                filaInventario.setEstadoVisual(true);
            } else if (e.getStateChange() == ItemEvent.DESELECTED) { 
                filaInventario.setEstadoVisual(false);
            }
        });

    }

    
    private void agregarFilaADiccionario (FilaTablaInventario filaInventario, DTOProductoProveedor producto ){
        diccionarioFilasInventario.put(filaInventario, producto);
    }
    
    
    private void estructurarDiccionarioFilas ( List<DTOProductoProveedor> listaProductosInventario ){
    
        diccionarioFilasInventario.clear();
        listaProductosInventario.forEach( (producto) -> {
            FilaTablaInventario filaInventario = crearNuevaFila(producto);
            agregarFilaADiccionario(filaInventario, producto);
        });
        
    }
    
    
    private void mostrarTodosLosProductos(){
    
        try {
            List<DTOProductoProveedor> listaProductosInventario = obtenerTodosLosProductos();
            if( !listaProductosInventario.isEmpty() ){
                estructurarDiccionarioFilas(listaProductosInventario);
                
                configurarModoVerificacion(false);
                
                panelInventario.inyectarFilas( new ArrayList<>( diccionarioFilasInventario.keySet()) );
  
            }else
                controladorBusquedaYAccionLibre.setEnableBotonAccionLibre(false);
        } catch (Exception e) {
            panelInventario.mostrarModalError(e.getMessage());
        }
        
    
    }
    
    
    /*
    ============================================================================
             METODOS PARA LAS ACCIONES (Buscar, actualizar, cancelar)
    ============================================================================
    */
    
    private boolean procesarBusqueda ( String filtro ){
    
        try {
            List<DTOProductoProveedor> listaProductosInventario = obtenerTodosLosProductosPorFiltro(filtro);
            if( !listaProductosInventario.isEmpty() ){
                estructurarDiccionarioFilas(listaProductosInventario);
                
                configurarModoVerificacion(false);
                
                panelInventario.inyectarFilas( new ArrayList<>( diccionarioFilasInventario.keySet()) );
                return true;
            }else
                return false;
        } catch (Exception e) {
            panelInventario.mostrarModalError(e.getMessage());
            return false;
        }
        
    }
    
    private void habilitarVerificarInventario(){
    
        boolean deseaContinuar = panelInventario.mostrarModalAdvertenciaConRespuesta("¡Cuidado! "
                + "Esta a punto de entrar en el modo verificar el inventario, el cual esta diseñado "
                + "para ayudar en la confirmación del inventario manual que se realice, allí se podrá "
                + "editar la cantidad de los productos y se ira confirmando las cantidades "
                + "si concuerdan con las contadas realmente.");

        if(deseaContinuar){
            configurarModoVerificacion(true);
            
        }
        
    }
    
    
    private void cancelarVerificacion(){
    
        boolean deseaContinuar = panelInventario.mostrarModalAdvertenciaConRespuesta("Esta a punto de cancelar el modo verificacion de inventario"
                + "\n Por lo tanto los cambios que haya realizado no se guardaran y se perderan."
                + "\n ¿Desea cancelar el modo verificacion?");
        
        if(deseaContinuar){
        
            configurarModoVerificacion(false);
            diccionarioFilasInventario.forEach( ( filaInventario , producto ) -> {
                filaInventario.asignarValorNuevaCantidad( null );
                //Como el check box ya tiene un listener con tan solo asingar false, 
                //si la fila estaba marcada como confirmada, se restaura a su estado
                //normal.
                filaInventario.getCheckBoxConfirmar().setSelected(false);
            });
        
        }
    
    }
    
    
    
}

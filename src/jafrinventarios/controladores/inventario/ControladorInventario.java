
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
                ModeloSesionUsuario.getInstancia().isAdministrador()
        );
        
        mostrarTodosLosProductos();
        
        configurarModoVerificacion(false);
        
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
    
    private FilaTablaInventario crearNuevaFila ( DTOProductoProveedor datosInventario ){
        FilaTablaInventario filaInventario = new FilaTablaInventario();
        
        filaInventario.setDatos( datosInventario.getIdProducto(), 
                                 datosInventario.getNombreProveedor(),
                                 datosInventario.getNombreProducto(),
                                 datosInventario.getCantidadDisponible()
        );
        
        return filaInventario;
    }
    

    private void agregarFilaADiccionario (FilaTablaInventario filaInventario, DTOProductoProveedor datosInventario ){
        diccionarioFilasInventario.put(filaInventario, datosInventario);
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
    
    public boolean procesarBusqueda ( String filtro ){
    
        try {
            List<DTOProductoProveedor> listaProductosInventario = obtenerTodosLosProductosPorFiltro(filtro);
            if( !listaProductosInventario.isEmpty() ){
                estructurarDiccionarioFilas(listaProductosInventario);
                panelInventario.inyectarFilas( new ArrayList<>( diccionarioFilasInventario.keySet()) );
                return true;
            }else
                return false;
        } catch (Exception e) {
            panelInventario.mostrarModalError(e.getMessage());
            return false;
        }
        
    }
    
    public void habilitarVerificarInventario(){
    
        boolean deseaContinuar = panelInventario.mostrarModalAdvertenciaConRespuesta("¡Cuidado! "
                + "Esta a punto de entrar en el modo verificar el inventario, el cual esta diseñado "
                + "para ayudar en la confirmación del inventario manual que se realice, allí se podrá "
                + "editar la cantidad de los productos y se ira confirmando las cantidades "
                + "si concuerdan con las contadas realmente.");

        if(deseaContinuar){
            configurarModoVerificacion(true);
        }
        
    }
    
    
    
    
    
    
}

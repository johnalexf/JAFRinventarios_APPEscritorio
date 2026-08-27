
package jafrinventarios.controladores.productos;

import jafrinventarios.DTOs.productos.DTOProductoTabla;
import jafrinventarios.controladores.utilidades.ControladorBusquedaYAccionLibre;
import jafrinventarios.controladores.utilidades.FuncionesBusquedaYAccionLibre;
import jafrinventarios.modelos.ModeloSesionUsuario;
import jafrinventarios.servicios.productos.ServicioProductos;
import jafrinventarios.vistas.productos.FilaTablaProductos;
import jafrinventarios.vistas.productos.ProductosPanel;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author JOHN FORERO
 */
public class ControladorProductos {
    
    private final ProductosPanel panelProductos;
    private final ServicioProductos servicioProductos;

    /*
    Diccionario con cada fila de cada registro de productos, permite:
    * Buscar el boton para asignarle la funcion de editar
    * Buscar la fila de un registro que se edito
    * Elimnar la fila si el registro se elimino
    */
    private LinkedHashMap<Integer, FilaTablaProductos> diccionarioProductos;
    
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
    public ControladorProductos(ProductosPanel panelProductos, ServicioProductos servicioProductos) {
        this.panelProductos = panelProductos;
        this.servicioProductos = servicioProductos;
        this.isAdministrador = ModeloSesionUsuario.getInstancia().isAdministrador();
        this.diccionarioProductos = new LinkedHashMap<>(); 
        /*
        Instanciar el controlador de la barra de busqueda y boton de accion libre
        pasando como parametro la instancia de la interfaz que permite asignar
        las funciones correspondientes que necesita ejecutar el controlador de 
        la barra de busqueda.
        */
        new ControladorBusquedaYAccionLibre(
                panelProductos.getPanelBusquedaYAccionLibre(),
                funcionesBusquedaYAccionLibre(),
                "Id, nombre o proveedor",
                "Agregar Nuevo producto"
        );
        
        mostrarTodosLosProductos();
        
    }
    
    
    /* 
    Metodo para crear la instancia de la interfaz FuncionesBusquedaYAccionLibre
    que contiene el metodo para poder buscar segun un filtro
    y para ejecutar la accion libre (crear producto).   
    */
    private FuncionesBusquedaYAccionLibre funcionesBusquedaYAccionLibre(){
        return new FuncionesBusquedaYAccionLibre() {
            
            @Override
            public boolean ejecutarBusqueda(String terminoBusqueda) {
                //TODO Hacer la funcion de busqueda
                // return procesarBusqueda( terminoBusqueda )
                return false;
            }
            
            @Override
            public void limpiarBusqueda(){
                diccionarioProductos.clear();
                mostrarTodosLosProductos();
            }

            @Override
            public void ejecutarAccionLibre() {
                // TODO crearProducto();
            }
            
        };
    }
    
    
    /*
    ============================================================================
                METODOS PARA CONSULTAR AL SERVICIO
    ============================================================================
    */
    private List<DTOProductoTabla> obtenerTodosLosProductos() throws Exception{
       return servicioProductos.obtenerTodosLosProductos( isAdministrador );  
    }
        
    private List<DTOProductoTabla> obtenerListaProductosPorFiltro( String filtro ) throws Exception{
       return servicioProductos.obtenerListaProductosPorFiltro(filtro, isAdministrador );  
    }
    
    private DTOProductoTabla obtenerDatosProducto( int idProducto ) throws Exception{
        return servicioProductos.obtenerDatosDTOProducto( idProducto );
    }
    
    
    /*
    ============================================================================
                METODOS PARA EL CONTROL DE LAS FILAS DE LA TABLA
    ============================================================================
    */
    private FilaTablaProductos asignarDatosAFila ( FilaTablaProductos fila, DTOProductoTabla datosProducto ){
        fila.setDatos(
            datosProducto.getIdProducto(), 
            datosProducto.getNombreProducto(), 
            datosProducto.getNombreProveedor(), 
            datosProducto.getPrecioCompra(), 
            datosProducto.getPrecioVenta(), 
            datosProducto.getCantidadDisponible(), 
            datosProducto.getCantidadMinimaStock()
        );
        if( isAdministrador )
            fila.setEstadoVisual( datosProducto.isHabilitado() );
        else
            fila.ocultarBtnEditar();
        
        return fila;
    }
    
    
    private FilaTablaProductos crearNuevaFila ( DTOProductoTabla datosProducto ){
        FilaTablaProductos fila = new FilaTablaProductos();
        return asignarDatosAFila(fila, datosProducto);
    }
    
    
    private void agregarFilaADiccionario( int id, FilaTablaProductos fila){
         diccionarioProductos.put( id, fila );
    }
    
    
    private void estructurarDiccionario ( List<DTOProductoTabla> listaProductos ){
        listaProductos.forEach( producto -> {
            FilaTablaProductos fila = crearNuevaFila( producto );
            //TODO inicializarBotonEditar
            agregarFilaADiccionario( producto.getIdProducto(), fila );
        } );
    
    }
    
    
    private void mostrarTodosLosProductos(){

        try {
            List<DTOProductoTabla> listaProductos = obtenerTodosLosProductos();
            //TODO que pasa si la lista esta vacia? deberia mostrarse un panel que diga "Aun no hay productos guardados, puedes crearlos"
            estructurarDiccionario( listaProductos );
            panelProductos.inyectarFilas( diccionarioProductos );
        } catch (Exception e) {
            /*
            e.printStackTrace(); Util para hacer el seguimiento de un error
            que no tiene mensaje.
            */
            
            panelProductos.mostrarModalError(e.getMessage());
        }
        
    }
    


    
}


package jafrinventarios.controladores.proveedores;

import jafrinventarios.DTOs.proveedores.DTOProveedorTabla;
import jafrinventarios.controladores.utilidades.ControladorBusquedaYAccionLibre;
import jafrinventarios.controladores.utilidades.FuncionesBusquedaYAccionLibre;
import jafrinventarios.controladores.utilidades.ResultadoDialogo;
import jafrinventarios.modelos.ModeloSesionUsuario;
import jafrinventarios.servicios.proveedores.ServicioProveedores;
import jafrinventarios.vistas.proveedores.FilaTablaProveedores;
import jafrinventarios.vistas.proveedores.ProveedoresPanel;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author JOHN FORERO
 */
public class ControladorProveedores {
    
    private final ProveedoresPanel panelProveedores;
    private final ServicioProveedores servicioProveedores;

    /*
    Diccionario con cada fila de cada registro de proveedores, permite:
    * Buscar el boton para asignarle la funcion de editar
    * Buscar la fila de un registro que se edito
    * Elimnar la fila si el registro se elimino
    */
    private LinkedHashMap<Integer, FilaTablaProveedores> diccionarioProveedores;
    
    
    /*
    ============================================================================
                        CONSTRUCTOR PUBLICO
    ============================================================================
    */
    public ControladorProveedores(ProveedoresPanel panelProveedores, ServicioProveedores servicioProveedores) {
        this.panelProveedores = panelProveedores;
        this.servicioProveedores = servicioProveedores;
        this.diccionarioProveedores = new LinkedHashMap<>(); 
        /*
        Instanciar el controlador de la barra de busqueda y boton de accion libre
        pasando como parametro la instancia de la interfaz que permite asignar
        las funciones correspondientes que necesita ejecutar el controlador de 
        la barra de busqueda.
        */
        new ControladorBusquedaYAccionLibre(
                panelProveedores.getPanelBusquedaYAccionLibre(),
                funcionesBusquedaYAccionLibre(),
                "Id, Nombre comercial, Nombre contacto, Direccion, Correo o Telefono",
                "Agregar Nuevo proveedor"
        );
        
        mostrarTodosLosProveedores();
        
    }
    
    
    /* 
    Metodo para crear la instancia de la interfaz FuncionesBusquedaYAccionLibre
    que contiene el metodo para poder buscar segun un filtro
    y para ejecutar la accion libre (crear proveedor).   
    */
    private FuncionesBusquedaYAccionLibre funcionesBusquedaYAccionLibre(){
        return new FuncionesBusquedaYAccionLibre() {
            
            @Override
            public boolean ejecutarBusqueda(String terminoBusqueda) {
                return procesarBusqueda(terminoBusqueda);
            }
            
            @Override
            public void limpiarBusqueda(){
                diccionarioProveedores.clear();
                mostrarTodosLosProveedores();
            }

            @Override
            public void ejecutarAccionLibre() {
                crearProveedor();
            }
            
        };
    }
    
    
    /*
    ============================================================================
                METODOS PARA CONSULTAR AL SERVICIO
    ============================================================================
    */
    private List<DTOProveedorTabla> obtenerTodosLosProveedores() throws Exception{
       return servicioProveedores.obtenerTodosLosProveedores();  
    }
        
    private List<DTOProveedorTabla> obtenerListaProveedoresPorFiltro( String filtro ) throws Exception{
       return servicioProveedores.obtenerListaProveedoresPorFiltro(filtro );  
    }
    
    private DTOProveedorTabla obtenerDatosProveedor( int idProveedor ) throws Exception{
        return servicioProveedores.obtenerDatosDTOProveedor( idProveedor );
    }
    
    
    /*
    ============================================================================
                METODOS PARA EL CONTROL DE LAS FILAS DE LA TABLA
    ============================================================================
    */
    private FilaTablaProveedores asignarDatosAFila ( FilaTablaProveedores fila, DTOProveedorTabla datosProveedor ){
        fila.setDatos(
            datosProveedor.getIdProveedor(), 
            datosProveedor.getNombreComercial(), 
            datosProveedor.getNombreCompletoContacto(), 
            datosProveedor.getDireccionProveedor(), 
            datosProveedor.getCorreoProveedor(), 
            datosProveedor.getTelefonoContacto()
        );
        fila.setEstadoVisual( datosProveedor.isHabilitado() );
        
        return fila;
    }
    
    
    private FilaTablaProveedores crearNuevaFila ( DTOProveedorTabla datosProveedor ){
        FilaTablaProveedores fila = new FilaTablaProveedores();
        return asignarDatosAFila(fila, datosProveedor);
    }
    
    
    private void agregarFilaADiccionario( Integer id, FilaTablaProveedores fila){
         diccionarioProveedores.put( id, fila );
    }
    
    
    private void estructurarDiccionario ( List<DTOProveedorTabla> listaProveedores ){
        listaProveedores.forEach( proveedor -> {
            FilaTablaProveedores fila = crearNuevaFila( proveedor );
            inicializarBotonEditar( proveedor.getIdProveedor(), fila );
            agregarFilaADiccionario( proveedor.getIdProveedor(), fila );
        } );
    
    }
    
    
    private void mostrarTodosLosProveedores(){

        try {
            List<DTOProveedorTabla> listaProveedores = obtenerTodosLosProveedores();
            if (!listaProveedores.isEmpty()){
                estructurarDiccionario( listaProveedores );
                panelProveedores.inyectarFilas( diccionarioProveedores );
            }
        } catch (Exception e) {
            panelProveedores.mostrarModalError(e.getMessage());
        }
        
    }
    
    
    /*
    ===========================================================================================
     METODO PARA ASIGNAR EL LISTENER AL BOTON DE EDITAR PROVEEDOR DE UNA FilaTablaProveedores
    ============================================================================================
    */
        
    private void inicializarBotonEditar( Integer id, FilaTablaProveedores fila ){
        fila.getBtnEditar().addActionListener(e -> editarProveedor( id ) );
    }
    
    
    /*
    ============================================================================
                METODOS PARA LAS ACCIONES (CREAR, EDITAR Y BUSCAR)
    ============================================================================
    */
    
       
    private void crearProveedor(){

        int idProveedorCreado;
//              = ControladorDialogoProveedores.crearProveedor( 
//                        panelProveedores.getVentanaPadre() , 
//                        servicioProveedores);
        
        //TODO hasta no tener la conexion a la base de datos esta linea la mantenemos 
        idProveedorCreado = -1;
        
        if(idProveedorCreado != -1){
            
            try {
                DTOProveedorTabla proveedor = obtenerDatosProveedor( idProveedorCreado );
                FilaTablaProveedores fila = crearNuevaFila( proveedor );
                inicializarBotonEditar( proveedor.getIdProveedor(), fila );
                
                boolean diccionarioVacio = diccionarioProveedores.isEmpty();
                agregarFilaADiccionario( proveedor.getIdProveedor(), fila );
                
                /*
                Si el diccionario estaba vacio significa que en la vista aun
                se muestra el mensaje de no hay proveedores, por tanto es necesario
                remover el contenido, para ahi si asignarle una nueva fila
                */
                if( diccionarioVacio ) panelProveedores.removerContenido();
                panelProveedores.inyectarNuevaFila( fila );
                
            }catch (Exception e) {
                panelProveedores.mostrarModalError(e.getMessage());
            }

        }
    }
    
        
    private void editarProveedor( Integer idProveedor ){
        
        ResultadoDialogo resultadoOperacion = ResultadoDialogo.SIN_CAMBIOS;
//                ControladorDialogoProveedores.editarProveedor(
//                    panelProveedores.getVentanaPadre() , idProveedor, servicioProveedores
//                );
        
        //TODO hasta que se tenga la conexion a la base de datos mantenenmos esta linea
        resultadoOperacion = ResultadoDialogo.SIN_CAMBIOS;
        
        if( resultadoOperacion == ResultadoDialogo.ACTUALIZADO ){  
            try {
                DTOProveedorTabla proveedor = obtenerDatosProveedor( idProveedor );
                FilaTablaProveedores fila = diccionarioProveedores.get( idProveedor );
                asignarDatosAFila( fila, proveedor );
            }catch (Exception e) {
                panelProveedores.mostrarModalError( e.getMessage() );
            }
        }
        
        if( resultadoOperacion == ResultadoDialogo.ELIMINADO ){
            FilaTablaProveedores fila = diccionarioProveedores.get( idProveedor );
            panelProveedores.eliminarFila( fila );
            diccionarioProveedores.remove( idProveedor );
        }
        
    }
    
    
    private boolean procesarBusqueda( String filtro ){

        try {
            List<DTOProveedorTabla> listaProveedores = obtenerListaProveedoresPorFiltro( filtro );
            if( listaProveedores.isEmpty() ){
                return false;
            }else{
                diccionarioProveedores.clear();
                estructurarDiccionario( listaProveedores );
                panelProveedores.inyectarFilas( diccionarioProveedores );
                return true;
            }
        } catch (Exception e) {
            panelProveedores.mostrarModalError(e.getMessage());
            return false;
        }

    }


    
}

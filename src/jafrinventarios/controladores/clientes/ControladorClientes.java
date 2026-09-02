
package jafrinventarios.controladores.clientes;

import jafrinventarios.DTOs.clientes.DTOClienteTabla;
import jafrinventarios.controladores.utilidades.ControladorBusquedaYAccionLibre;
import jafrinventarios.controladores.utilidades.FuncionesBusquedaYAccionLibre;
import jafrinventarios.controladores.utilidades.ResultadoDialogo;
import jafrinventarios.modelos.ModeloSesionUsuario;
import jafrinventarios.servicios.clientes.ServicioClientes;
import jafrinventarios.vistas.clientes.FilaTablaClientes;
import jafrinventarios.vistas.clientes.ClientesPanel;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author JOHN FORERO
 */
public class ControladorClientes {
    
    private final ClientesPanel panelClientes;
    private final ServicioClientes servicioClientes;

    /*
    Diccionario con cada fila de cada registro de clientes, permite:
    * Buscar el boton para asignarle la funcion de editar
    * Buscar la fila de un registro que se edito
    * Elimnar la fila si el registro se elimino
    */
    private LinkedHashMap<Integer, FilaTablaClientes> diccionarioClientes;
    
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
    public ControladorClientes(ClientesPanel panelClientes, ServicioClientes servicioClientes) {
        this.panelClientes = panelClientes;
        this.servicioClientes = servicioClientes;
        this.isAdministrador = ModeloSesionUsuario.getInstancia().isAdministrador();
        this.diccionarioClientes = new LinkedHashMap<>(); 
        /*
        Instanciar el controlador de la barra de busqueda y boton de accion libre
        pasando como parametro la instancia de la interfaz que permite asignar
        las funciones correspondientes que necesita ejecutar el controlador de 
        la barra de busqueda.
        */
        new ControladorBusquedaYAccionLibre(
                panelClientes.getPanelBusquedaYAccionLibre(),
                funcionesBusquedaYAccionLibre(),
                "Id, Nombre negocio, Nombre contacto, Direccion, Correo o Telefono",
                "Agregar Nuevo Cliente",
                isAdministrador
        );
        
        if(!isAdministrador)
            panelClientes.ocultarTituloEditar();
        
        mostrarTodosLosClientes();
        
    }
    
    
    /* 
    Metodo para crear la instancia de la interfaz FuncionesBusquedaYAccionLibre
    que contiene el metodo para poder buscar segun un filtro
    y para ejecutar la accion libre (crear cliente).   
    */
    private FuncionesBusquedaYAccionLibre funcionesBusquedaYAccionLibre(){
        return new FuncionesBusquedaYAccionLibre() {
            
            @Override
            public boolean ejecutarBusqueda(String terminoBusqueda) {
                return procesarBusqueda(terminoBusqueda);
            }
            
            @Override
            public void limpiarBusqueda(){
                diccionarioClientes.clear();
                mostrarTodosLosClientes();
            }

            @Override
            public void ejecutarAccionLibre() {
                crearCliente();
            }
            
        };
    }
    
    
    /*
    ============================================================================
                METODOS PARA CONSULTAR AL SERVICIO
    ============================================================================
    */
    private List<DTOClienteTabla> obtenerTodosLosClientes() throws Exception{
       return servicioClientes.obtenerTodosLosClientes( isAdministrador );  
    }
        
    private List<DTOClienteTabla> obtenerListaClientesPorFiltro( String filtro ) throws Exception{
       return servicioClientes.obtenerListaClientesPorFiltro( filtro, isAdministrador );  
    }
    
    private DTOClienteTabla obtenerDatosCliente( int idCliente ) throws Exception{
        return servicioClientes.obtenerDatosDTOCliente( idCliente );
    }
    
    
    /*
    ============================================================================
                METODOS PARA EL CONTROL DE LAS FILAS DE LA TABLA
    ============================================================================
    */
    private FilaTablaClientes asignarDatosAFila ( FilaTablaClientes fila, DTOClienteTabla datosCliente ){
        fila.setDatos(
            datosCliente.getIdCliente(), 
            datosCliente.getNombreNegocio(), 
            datosCliente.getNombreCompletoContacto(), 
            datosCliente.getDireccionCliente(), 
            datosCliente.getCorreoCliente(), 
            datosCliente.getTelefonoContacto()
        );
        if( isAdministrador )
            fila.setEstadoVisual( datosCliente.isHabilitado() );
        else
            fila.ocultarBtnEditar();
        
        return fila;
    }
    
    
    private FilaTablaClientes crearNuevaFila ( DTOClienteTabla datosCliente ){
        FilaTablaClientes fila = new FilaTablaClientes();
        return asignarDatosAFila(fila, datosCliente);
    }
    
    
    private void agregarFilaADiccionario( Integer id, FilaTablaClientes fila){
         diccionarioClientes.put( id, fila );
    }
    
    
    private void estructurarDiccionario ( List<DTOClienteTabla> listaClientes ){
        listaClientes.forEach( cliente -> {
            FilaTablaClientes fila = crearNuevaFila( cliente );
            if(isAdministrador)
                inicializarBotonEditar( cliente.getIdCliente(), fila );
            agregarFilaADiccionario( cliente.getIdCliente(), fila );
        } );
    
    }
    
    
    private void mostrarTodosLosClientes(){

        try {
            List<DTOClienteTabla> listaClientes = obtenerTodosLosClientes();
            if (!listaClientes.isEmpty()){
                estructurarDiccionario( listaClientes );
                panelClientes.inyectarFilas( diccionarioClientes );
            }
        } catch (Exception e) {
            panelClientes.mostrarModalError(e.getMessage());
        }
        
    }
    
    
    /*
    ======================================================================================
     METODO PARA ASIGNAR EL LISTENER AL BOTON DE EDITAR CLIENTE DE UNA FilaTablaClientes
    ======================================================================================
    */
        
    private void inicializarBotonEditar( Integer id, FilaTablaClientes fila ){
        fila.getBtnEditar().addActionListener(e -> editarCliente( id ) );
    }
    
    
    /*
    ============================================================================
                METODOS PARA LAS ACCIONES (CREAR, EDITAR Y BUSCAR)
    ============================================================================
    */
    
       
    private void crearCliente(){

        int idClienteCreado = ControladorDialogoCliente.crearCliente( 
                        panelClientes.getVentanaPadre() , 
                        servicioClientes
        );
        
        //TODO hasta no tener la conexion a la base de datos esta linea la mantenemos 
        idClienteCreado = -1;
        
        if(idClienteCreado != -1){
            
            try {
                DTOClienteTabla cliente = obtenerDatosCliente( idClienteCreado );
                FilaTablaClientes fila = crearNuevaFila( cliente );
                inicializarBotonEditar( cliente.getIdCliente(), fila );
                
                boolean diccionarioVacio = diccionarioClientes.isEmpty();
                agregarFilaADiccionario( cliente.getIdCliente(), fila );
                
                /*
                Si el diccionario estaba vacio significa que en la vista aun
                se muestra el mensaje de no hay clientes, por tanto es necesario
                remover el contenido, para ahi si asignarle una nueva fila
                */
                if( diccionarioVacio ) panelClientes.removerContenido();
                panelClientes.inyectarNuevaFila( fila );
                
            }catch (Exception e) {
                panelClientes.mostrarModalError(e.getMessage());
            }

        }
    }
    
        
    private void editarCliente( Integer idCliente ){
        
        ResultadoDialogo resultadoOperacion = 
                ControladorDialogoCliente.editarCliente(
                    panelClientes.getVentanaPadre() , idCliente, servicioClientes
                );
        
        //TODO hasta que se tenga la conexion a la base de datos mantenenmos esta linea
        resultadoOperacion = ResultadoDialogo.SIN_CAMBIOS;
        
        if( resultadoOperacion == ResultadoDialogo.ACTUALIZADO ){  
            try {
                DTOClienteTabla cliente = obtenerDatosCliente( idCliente );
                FilaTablaClientes fila = diccionarioClientes.get( idCliente );
                asignarDatosAFila( fila, cliente );
            }catch (Exception e) {
                panelClientes.mostrarModalError( e.getMessage() );
            }
        }
        
        if( resultadoOperacion == ResultadoDialogo.ELIMINADO ){
            FilaTablaClientes fila = diccionarioClientes.get( idCliente );
            panelClientes.eliminarFila( fila );
            diccionarioClientes.remove( idCliente );
        }
        
    }
    
    
    private boolean procesarBusqueda( String filtro ){

        try {
            List<DTOClienteTabla> listaClientes = obtenerListaClientesPorFiltro( filtro );
            if( listaClientes.isEmpty() ){
                return false;
            }else{
                diccionarioClientes.clear();
                estructurarDiccionario( listaClientes );
                panelClientes.inyectarFilas( diccionarioClientes );
                return true;
            }
        } catch (Exception e) {
            panelClientes.mostrarModalError(e.getMessage());
            return false;
        }

    }


    
}

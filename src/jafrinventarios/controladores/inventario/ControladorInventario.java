
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
import java.util.Map;

/**
 *
 * @author JOHN FORERO
 */
public class ControladorInventario {
    
    private ServicioInventario servicioInventario;
    
    private InventarioPanel panelInventario;
    
    private LinkedHashMap<FilaTablaInventario, DTOProductoProveedor> diccionarioFilasInventario;
    
    private ControladorBusquedaYAccionLibre controladorBusquedaYAccionLibre;
    
    private boolean isAdministrador;
    
    //Se almacena el filtro para poder determinar actualizar los productos en la vista
    //despues de cualquier cambio en las cantidades
    private String filtro;
    
    /*
    ============================================================================
                        CONSTRUCTOR PUBLICO
    ============================================================================
    */
    public ControladorInventario(InventarioPanel panelInventario, ServicioInventario servicioInventario) {
        this.servicioInventario = servicioInventario;
        this.panelInventario = panelInventario;
        
        this.diccionarioFilasInventario = new LinkedHashMap<>();
        
        this.isAdministrador = ModeloSesionUsuario.getInstancia().isAdministrador();
        
        this.filtro = "";
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
            panelInventario.getBtnFinalizarVerificacion().addActionListener(e ->{ 
                    actualizarInventario();
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
    
        this.filtro = "";
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
                
                //En controlador de busqueda se encarga de mostrar todos los productos si esta funcion
                //procesarBusqueda detecta que la lista viene vacia, por consiguiente solo es necesario
                //Almacenar la palabra de filtro y ya en mostrarTodosProductos se deja vacia
                this.filtro = filtro;
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
        
        if(deseaContinuar)
            salirModoVerificacion();
    
    }
    
    private void salirModoVerificacion(){
        
        configurarModoVerificacion(false);
        diccionarioFilasInventario.forEach( ( filaInventario , producto ) -> {
            filaInventario.asignarValorNuevaCantidad( null );
            //Como el check box ya tiene un listener con tan solo asingar false, 
            //si la fila estaba marcada como confirmada, se restaura a su estado
            //normal.
            filaInventario.getCheckBoxConfirmar().setSelected(false);
        });
        
    }
    
    
    public void actualizarInventario(){
    
        /*
        ========================================================================
          Verificar si esta correctamente diligenciados los campos confirmados
        ========================================================================
        */
        //Por lo menos debe existir un campo confirmado
        boolean existeCamposConfirmados = false;
        boolean sonCamposValidos = true;
        
        for( FilaTablaInventario filaInventario : diccionarioFilasInventario.keySet()){ 
            if( filaInventario.getCheckBoxConfirmar().isSelected() ){
                existeCamposConfirmados = true;
                if( !filaInventario.validarCampoNuevaCantidad() )
                    sonCamposValidos = false;
            }
        }
        
        if( !existeCamposConfirmados ){
            panelInventario.mostrarModalError("No hay campos confirmados para actualizar");
            return;
        }
        
        if( !sonCamposValidos ){
            panelInventario.mostrarModalErrorFormatoCampos();
            return;
        }
        
        
        /*
        ========================================================================
            Extraer los datos de los campos confirmados
        ========================================================================
        */
        int camposIguales = 0;
        ArrayList<DTOProductoCantidad> productosActualizar = new ArrayList<>();
            
        try {
            for (Map.Entry<FilaTablaInventario, DTOProductoProveedor> entry : diccionarioFilasInventario.entrySet()) {
                FilaTablaInventario filaInventario = entry.getKey();

                if( filaInventario.getCheckBoxConfirmar().isSelected() ){
                    DTOProductoProveedor producto = entry.getValue();

                    int cantidadProductoFila = Integer.parseInt(filaInventario.obtenerValorNuevaCantidad());
                    if (cantidadProductoFila == producto.getCantidadDisponible()) {
                        camposIguales++;
                    }else{
                        productosActualizar.add( 
                                new DTOProductoCantidad(
                                        producto.getIdProducto(),
                                        cantidadProductoFila
                                )
                        );
                    }
                }
            }
        } catch (Exception e) {
            panelInventario.mostrarModalError("Error al recolectar la informacion de campos confirmados");
            return;
        }
        
        
        
        /*
        =======================================================================
           GUARDAR EN LA BASE DE DATOS solo si hay productos para actualizar
        =======================================================================
        */
        
        if( productosActualizar.isEmpty() ){
        
            boolean deseaContinuar = 
                    panelInventario.mostrarModalAdvertenciaConRespuesta(
                            "Usted ha confirmado " + camposIguales + " productos"+
                            "\nDe los cuales ninguno se detecto un cambio en la cantidad del mismo"+
                            "\nSi esta de acuerdo entonces presione en continuar y se finalizara la verificacion de inventario"
                    );
            
            if(deseaContinuar)
                salirModoVerificacion();
        
        }else{
            int totalCamposConfirmados = camposIguales + productosActualizar.size();
            
            boolean deseaContinuar = 
                panelInventario.mostrarModalAdvertenciaConRespuesta(
                        "Usted ha confirmado " + totalCamposConfirmados + " productos"+
                        "\nDe los cuales " +  productosActualizar.size() +" se detecto un cambio en la cantidad del mismo"+
                        "\nSi esta de acuerdo entonces presione en continuar y se actualizara el valor de esos productos"
                );
            
            if(deseaContinuar){
                try {
                    actualizarCantidadProductos(productosActualizar);
                    panelInventario.mostrarModalExito("El inventario ha sido actualizado correctamente.");
                    if(this.filtro.isEmpty())
                        mostrarTodosLosProductos();
                    else
                        procesarBusqueda(this.filtro);
    
                } catch (Exception e) {
                    panelInventario.mostrarModalError(e.getMessage());
                }           
            }
        
        }

    }
    
    
    
}

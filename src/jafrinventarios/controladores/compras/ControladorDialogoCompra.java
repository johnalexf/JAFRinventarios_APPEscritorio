
package jafrinventarios.controladores.compras;

import jafrinventarios.DTOs.productos.DTOProductoPrecio;
import jafrinventarios.controladores.utilidades.ResultadoDialogo;
import jafrinventarios.modelos.ModeloSesionUsuario;
import jafrinventarios.modelos.compras.ModeloCompra;
import jafrinventarios.modelos.compras.ModeloDetalleCompra;
import jafrinventarios.servicios.compras.ServicioCompras;
import jafrinventarios.servicios.productos.ServicioProductos;
import jafrinventarios.servicios.proveedores.ServicioProveedores;
import jafrinventarios.servicios.usuarios.ServicioUsuarios;
import jafrinventarios.vistas.compras.dialogoCompra.DialogoFormularioCompra;
import jafrinventarios.vistas.compras.dialogoCompra.DialogoFormularioCompra.TipoDialogo;
import jafrinventarios.vistas.compras.dialogoCompra.FilaFormularioDetalleCompra;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author JOHN FORERO
 */
public class ControladorDialogoCompra {
    
    private final DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a");
    
    private DialogoFormularioCompra dialogoCompra;
    private ServicioCompras servicioCompras;
    
    private TipoDialogo tipoDialogo;
    
    private ModeloCompra modeloCompra;
    
    private LinkedHashMap<Integer, DTOProductoPrecio> diccionarioProductosPrecio;
    private LinkedHashMap< Integer, String > diccionarioProductosId;
    
    /*Este diccionario mantendra la relacion entre el modeloDetalleCompra, con su 
    respectiva fila en la parte grafica.
    */
    private LinkedHashMap< FilaFormularioDetalleCompra, ModeloDetalleCompra > diccionarioDetalles;
    private double totalCompra = 0.0;
    
    /*
    Variable que en el caso de editar tendra el id del registro a modificar
    pero en el case de crear guardara el id de la compra creada.
    */
    private Integer idCompra;
    
    /*
    Variable para responde la operacion realizada al editar
    que pueden ser las siguientes opciones:  ACTUALIZADO, ELIMINADO, SIN_CAMBIOS 
    */
    private ResultadoDialogo resultadoEdicion = ResultadoDialogo.SIN_CAMBIOS;
    
    
    /*
    ============================================================================
      CONSTRUCTOR PRIVADO PARA EVITAR QUE SE CREE SIN SU DEBIDA CONFIGURACION
    ============================================================================
    */

    private ControladorDialogoCompra(   DialogoFormularioCompra dialogoCompra, 
                                        ServicioCompras servicioCompras, 
                                        TipoDialogo tipoDialogo, 
                                        Integer idCompra) {
        this.dialogoCompra = dialogoCompra;
        this.servicioCompras = servicioCompras;
        this.tipoDialogo = tipoDialogo;
        this.idCompra = idCompra;
        
        this.diccionarioProductosPrecio = new LinkedHashMap<>();
        this.diccionarioProductosId = new LinkedHashMap<>();
        
        this.diccionarioDetalles = new LinkedHashMap<>();
        
        configuracionInicial();
        
        this.dialogoCompra.mostrar();
    }
    
    
    private void configuracionInicial(){
    
        inicializarComboBoxProveedores( tipoDialogo == TipoDialogo.CREAR_NUEVA_COMPRA );
        
        if( tipoDialogo == TipoDialogo.EDITAR_COMPRA ){
            try {
                dialogoCompra.setEnableComboBoxProveedores(false);
                
                modeloCompra = obtenerModeloCompra(idCompra);
                totalCompra = modeloCompra.getTotalCompra();
                
                inicializarDiccionariosProductos( modeloCompra.getIdProveedor() );
                poblarDiccionarioFilasDetalles();
                
                cargarDatosAVista();
                
            } catch (Exception e) {
                dialogoCompra.mostrarAlertaError(e.getMessage());
            }
        }else{
            dialogoCompra.setEnableBtnAgregarProducto(false);
        }
        
        dialogoCompra.inicializarSelectorFechaHora();
        
        inicializarEventosBotonesPrincipales();
        
    }
    
    
    private void poblarDiccionarioFilasDetalles(){
    
        for(ModeloDetalleCompra modeloDetalle : modeloCompra.getDetalles()){
            
            FilaFormularioDetalleCompra filaDetalle = crearNuevaFilaDetalle();
            filaDetalle = asignarDatosFilaDetalle( filaDetalle, modeloDetalle );
            
            diccionarioDetalles.put( filaDetalle , modeloDetalle.clonar() );
  
            inicializarEventosFilaDetalle(filaDetalle);
                        
        }
        
    }
    
    
    private void cargarDatosAVista() throws Exception{
        
        dialogoCompra.setIdCompra(idCompra);

        String aliasUsuario = obtenerAliasUsuario(modeloCompra.getIdUsuario());
        dialogoCompra.setAliasUsuario(aliasUsuario);

        dialogoCompra.asignarDatosEnFormulario(
                new HashMap<>(
                    Map.of("proveedor", String.valueOf(modeloCompra.getIdProveedor()), 
                            "fechaHora", modeloCompra.getFechaHoraCompra().format(formatoFecha)
                    )
                )
        );

        dialogoCompra.setTotalCompra( modeloCompra.getTotalCompra());
        
        dialogoCompra.inyectarFilasDetalles(new ArrayList<>(diccionarioDetalles.keySet()));
    }
    
    
    private void inicializarEventosBotonesPrincipales(){
    
        dialogoCompra.getBtnAgregarProducto().addActionListener( e -> agregarFilaDetalle() );
        dialogoCompra.getBtnEnviarFormulario().addActionListener( e -> procesarFormulario() );
        
        if(tipoDialogo == TipoDialogo.EDITAR_COMPRA)
            dialogoCompra.getBtnLinkEliminarRegistro().addActionListener( e -> eliminarCompra());
    }
    
    /*
    ============================================================================
     METODOS ESTÁTICoS: Los únicos puntos de acceso para los demás controladores
    ============================================================================
    */
    public static ResultadoDialogo editarCompra (   java.awt.Window ventanaPadre,
                                                    Integer idCompra,
                                                    ServicioCompras servicioCompras){
        
        DialogoFormularioCompra dialogoCompra = 
                new DialogoFormularioCompra( ventanaPadre, TipoDialogo.EDITAR_COMPRA);
        
        ControladorDialogoCompra controlador =
                new ControladorDialogoCompra(
                        dialogoCompra,
                        servicioCompras,
                        TipoDialogo.EDITAR_COMPRA,
                        idCompra
                );
        
        return controlador.resultadoEdicion;
    
    }
    
    
    public static Integer crearCompra (   java.awt.Window ventanaPadre,
                                                    ServicioCompras servicioCompras){
        
        DialogoFormularioCompra dialogoCompra = 
                new DialogoFormularioCompra( ventanaPadre, TipoDialogo.CREAR_NUEVA_COMPRA);
        
        ControladorDialogoCompra controlador =
                new ControladorDialogoCompra(
                        dialogoCompra,
                        servicioCompras,
                        TipoDialogo.CREAR_NUEVA_COMPRA,
                        -1
                );
        
        return controlador.idCompra;
    
    }
    
    /*
    ============================================================================
                METODOS PARA CONSULTAR A LOS SERVICIOS
    ============================================================================
    */
    
    private LinkedHashMap<Integer, String> obtenerDiccionarioProveedores(boolean soloHabilitados) throws Exception{
        return ServicioProveedores.obtenerDiccionarioProveedores(soloHabilitados);
    }
    
    private LinkedHashMap<Integer, DTOProductoPrecio> obtenerDTOProductosPrecio(boolean soloHabilitados, int idProveedor) throws Exception{
        return ServicioProductos.obtenerProductosConPrecioCompra(soloHabilitados, idProveedor);
    }
    
    private String obtenerAliasUsuario ( int idUsuario ) throws Exception{
        return ServicioUsuarios.obtenerAliasUsuario(idUsuario);
    }
    
    private ModeloCompra obtenerModeloCompra( Integer idCompra ) throws Exception{
        return servicioCompras.obtenerModeloCompra(idCompra);
    }
    
    private int crearCompra( ModeloCompra compra ) throws Exception{
        return servicioCompras.crearCompra(compra);
    }
    
    private void editarCompra( ModeloCompra compra ) throws Exception{
        servicioCompras.editarCompra(compra);
    }
    
    private void eliminarCompra( int idCompra )throws Exception{
        servicioCompras.eliminarCompra(idCompra);
    }
    
  
    /*
    ============================================================================
        METODOS PARA RECOLECTAR LA INFORMACION QUE ENTREGA LOS SERVICIOS
    ============================================================================
    */
    private void inicializarComboBoxProveedores( boolean soloHabilitados ){
        try {
            LinkedHashMap<Integer, String> diccionarioProveedores = obtenerDiccionarioProveedores(soloHabilitados);
            if( diccionarioProveedores.isEmpty() )
                dialogoCompra.mostrarAlertaAdvertenciaSinRespuesta(
                        "Para poder crear una compra, debe existir por lo menos un proveedor\n"
                        + "Por favor dirigete a la seccion de proveedores y crea uno."
                );
            else{
                dialogoCompra.inicializarComboBoxProveedores(diccionarioProveedores);
                dialogoCompra.getComboBoxProveedores().addActionListener( e -> actualizarDiccionariosProductos() );
            }
        } catch (Exception e) {
            dialogoCompra.mostrarAlertaError(e.getMessage());
        }
    }
    
    
    private void actualizarDiccionariosProductos(){
        try {
            HashMap<String, String> datosFormulario = dialogoCompra.recolectarDatosFormulario();
            
            if(datosFormulario.containsKey("proveedor")){
                int idProveedor = Integer.parseInt( datosFormulario.get("proveedor"));
                inicializarDiccionariosProductos(idProveedor);
                dialogoCompra.setEnableBtnAgregarProducto(true);
            }
       
        } catch (Exception e) {
            //Si no seleccionaron un proveedor no dejamos que se agregue un producto
            dialogoCompra.setEnableBtnAgregarProducto(false);
        }
    }
    
    
    private void inicializarDiccionariosProductos( int idProveedor ){
        
        try {
            diccionarioProductosPrecio = obtenerDTOProductosPrecio(
                                            tipoDialogo == TipoDialogo.CREAR_NUEVA_COMPRA, 
                                            idProveedor);
            if( diccionarioProductosPrecio.isEmpty() )
                dialogoCompra.mostrarAlertaAdvertenciaSinRespuesta(
                        "Aun no hay productos relacionados al proveedor seleccionado\n"
                        + "Por favor dirigete a la seccion de productos y cree los productos relacionandolo con el proveedor."
                );
            else
                diccionarioProductosPrecio.forEach(
                        (idProducto, producto) -> {
                            diccionarioProductosId.put(idProducto, producto.getNombreProducto());
                        }
                );
        } catch (Exception e) {
            dialogoCompra.mostrarAlertaError(e.getMessage());
        }
    
    }
    
    
    /*
    ============================================================================
                METODOS PARA GESTIONAR LAS FILAS DE DETALLES
    ============================================================================
    */
    private FilaFormularioDetalleCompra crearNuevaFilaDetalle(){
    
        FilaFormularioDetalleCompra filaDetalle = new FilaFormularioDetalleCompra();
        
        filaDetalle.inicializarComboBoxProductos(diccionarioProductosId);
        filaDetalle.setItem( diccionarioDetalles.size()+ 1 );
        
        return filaDetalle;
    }
    
    
    private FilaFormularioDetalleCompra asignarDatosFilaDetalle ( 
                                            FilaFormularioDetalleCompra filaDetalle,
                                            ModeloDetalleCompra datosDetalle
    ){
    
        filaDetalle.asignarDatosEnFormulario(
            new HashMap<>(
                Map.of("producto", String.valueOf( datosDetalle.getIdProducto() ), 
                       "cantidadProducto", String.valueOf( datosDetalle.getCantidadProducto() )
                )
            )
        );
        filaDetalle.setPrecioUnitario( datosDetalle.getPrecioUnitarioProducto() );
        filaDetalle.setPrecioTotal( datosDetalle.getPrecioTotalProducto() );
    
        
        return filaDetalle;
    }
    
    
    private void inicializarEventosFilaDetalle ( FilaFormularioDetalleCompra filaDetalle ){
        
        inicializarEventoInputCantidad(filaDetalle);
        
        inicializarEventoComboBoxProductos(filaDetalle);
        
        inicializarEventoEliminarFila(filaDetalle);
        
    }
    
    private void inicializarEventoInputCantidad( FilaFormularioDetalleCompra filaDetalle ){
        
        filaDetalle.getInputCantidad().getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                actualizarCantidadProducto(filaDetalle);
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                actualizarCantidadProducto(filaDetalle);
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                actualizarCantidadProducto(filaDetalle);
            }
        });
        
    }
    
    
    private void inicializarEventoComboBoxProductos( FilaFormularioDetalleCompra filaDetalle ){
    
        filaDetalle.getComboBoxProductos().addActionListener( e -> {
            ModeloDetalleCompra modeloDetalle = diccionarioDetalles.get(filaDetalle);
            
            Integer idProducto = null;
            double precioProducto = 0.0;
            
            try {
                HashMap<String, String> datosFormulario = filaDetalle.recolectarDatosFormulario();
            
                if(datosFormulario.containsKey("producto")){
                    idProducto = Integer.parseInt( datosFormulario.get("producto"));          
                    precioProducto = diccionarioProductosPrecio.get(idProducto).getPrecioProducto();
                }
            } catch (Exception exception) {
                System.out.println("Error controlado: no se selecciono un producto o el retorno esperado no se puede convertir a numero");
            }
            
            modeloDetalle.setIdProducto(idProducto);
            modeloDetalle.setPrecioUnitarioProducto( precioProducto );

            filaDetalle.setPrecioUnitario( modeloDetalle.getPrecioUnitarioProducto());
            filaDetalle.setPrecioTotal( modeloDetalle.getPrecioTotalProducto()  );
            
            actualizarTotalCompra();
            
        });
        
    }
    
    
    private void actualizarCantidadProducto( FilaFormularioDetalleCompra filaDetalle ){
        
        ModeloDetalleCompra detalleCompra = diccionarioDetalles.get(filaDetalle);
        try {
            int cantidad = Integer.parseInt( filaDetalle.getInputCantidad().getText() );
            detalleCompra.setCantidadProducto(cantidad);

            filaDetalle.setPrecioTotal( detalleCompra.getPrecioTotalProducto()  );

        } catch (Exception e) {
            detalleCompra.setCantidadProducto(0);
            filaDetalle.setPrecioTotal( 0 );
        }
        
        actualizarTotalCompra();
    
    }
    
    
    private void actualizarTotalCompra (){
    
        double totalCompra = 0.0;
        if(!diccionarioDetalles.isEmpty()){
            for( ModeloDetalleCompra modeloDetalle: diccionarioDetalles.values()){
                totalCompra += modeloDetalle.getPrecioTotalProducto();
            }
        }
        this.totalCompra = totalCompra;
        dialogoCompra.setTotalCompra(totalCompra);
    
    }
    
    
    private void inicializarEventoEliminarFila( FilaFormularioDetalleCompra filaDetalle){
    
        filaDetalle.getBtnEliminar().addActionListener( e -> {
            boolean continuar = dialogoCompra.mostrarAlertaAdvertenciaConRespuesta(
                    "¿Quieres quitar este producto?\n"
                    + "Esta acción se aplicará solo en este borrador. No será permanente hasta que guardes los cambios de la compra.");
            
            if(continuar){
                diccionarioDetalles.remove(filaDetalle);
                actualizarValorItemDetalles();
                dialogoCompra.removerFilaDetalle(filaDetalle);
                actualizarTotalCompra();
                if(diccionarioDetalles.isEmpty()){
                    dialogoCompra.setEnableComboBoxProveedores(true);
                }
            }
            
        });
        
    }
    
    private void actualizarValorItemDetalles (){
    
        int item = 0;
        for( FilaFormularioDetalleCompra filaDetalle : diccionarioDetalles.keySet()){
            filaDetalle.setItem(++item);
        }
 
    }
    
    
    
    /*
    ============================================================================
                        METODOS OPERACION DE LOS BOTONES
    ============================================================================
    */
    
    private void agregarFilaDetalle(){
        
        FilaFormularioDetalleCompra filaDetalle = crearNuevaFilaDetalle();

        diccionarioDetalles.put( filaDetalle , new ModeloDetalleCompra() );

        inicializarEventosFilaDetalle(filaDetalle);
        
        dialogoCompra.inyectarNuevaFilaDetalle(filaDetalle);
        
        dialogoCompra.setEnableComboBoxProveedores(false);
    }
    
    
    private void procesarFormulario(){
    
        /*
        ========================================================================
               Verificar si esta correctamente diligenciado el formulario
        ========================================================================
        */
        //Antes de validar, por lo menos debe existir un detalle de compra
        if( diccionarioDetalles.isEmpty() ){
            dialogoCompra.mostrarAlertaError("No se puede guardar una compra sin productos, por favor cree por lo menos uno");
            return;
        }
        
        //Verificar que los campos esten diligenciados con un formato valido
        //En este caso necesitamos validar tanto en los datos generales de la compra
        //como en cada uno de los detalles
        boolean datosValidos = true;
        if( !dialogoCompra.validarFormulario() ) 
            datosValidos = false;
        for( FilaFormularioDetalleCompra filaDetalles : diccionarioDetalles.keySet()){
            if( !filaDetalles.validarFormulario() )
                datosValidos = false;
        }
        //dejamos que se validen todos, ya que la misma vista mostrara los errores en cada campo
        
        if( !datosValidos ){
            dialogoCompra.mostrarAlertaErrorFormatoCampos();
            return;
        }
        
        //Extraer los datos generales 
        HashMap<String, String> datosFormulario = dialogoCompra.recolectarDatosFormulario();
       
        
        /*
        ========================================================================
            Validar si hay cambios cuando es editar, 
            para la creacion solo instanciamos el modelo
        ========================================================================
        */
        ModeloCompra compraAProcesar = 
                ( tipoDialogo == TipoDialogo.EDITAR_COMPRA)
                ? modeloCompra.clonar()
                : new ModeloCompra();
        
        try {
            compraAProcesar = asignarDatosAModelo(compraAProcesar, datosFormulario);
        } catch (Exception e) {
            dialogoCompra.mostrarAlertaError( e.getMessage() );
            return;
        }
        
        //Verificar si los modelos son iguales en dado caso que tipoDialogo sea EDITAR
        if( tipoDialogo == TipoDialogo.EDITAR_COMPRA ){
            if ( modeloCompra.equals( compraAProcesar )) {
                dialogoCompra.mostrarAlertaError("No hay cambios para guardar");
                return;
            }
        }
        
        //Si no son iguales las compras asignamos el id del usuario que inicio sesion
        //de igual manera esta asignacion funciona para una nueva compra
        compraAProcesar.setIdUsuario(ModeloSesionUsuario.getInstancia().getIdUsuario());
        
        
        /*
        =======================================================================
        GUARDAR EN LA BASE DE DATOS solo si cumplio las anteriores validaciones
        =======================================================================
        */
        switch(tipoDialogo){
            case EDITAR_COMPRA:
                try {
                    editarCompra(compraAProcesar);
                    dialogoCompra.mostrarAlertaExitosa("Compra actualizada correctamente");
                    resultadoEdicion = ResultadoDialogo.ACTUALIZADO;
                    dialogoCompra.dispose();
                }catch (Exception e) {
                    dialogoCompra.mostrarAlertaError(e.getMessage());
                }
                break;
            case CREAR_NUEVA_COMPRA:
                try {
                    idCompra = crearCompra(compraAProcesar);
                    dialogoCompra.mostrarAlertaExitosa("Compra creada correctamente");
                    resultadoEdicion = ResultadoDialogo.ACTUALIZADO;
                    dialogoCompra.dispose();
                }catch (Exception e) {
                    dialogoCompra.mostrarAlertaError(e.getMessage());
                }
                break;
        }
    
    }
    
    
    private ModeloCompra asignarDatosAModelo ( ModeloCompra modeloCompra, HashMap<String, String> datos ) throws Exception{
    
        try {
            modeloCompra.setIdProveedor(Integer.parseInt( datos.get("proveedor")));
            modeloCompra.setFechaHoraCompra( LocalDateTime.parse( datos.get("fechaHora"), formatoFecha));
            modeloCompra.setDetalles( new ArrayList<>(diccionarioDetalles.values()) );
            modeloCompra.setTotalCompra(totalCompra);
            return modeloCompra;
        } catch (Exception e) {
            throw new Exception( "Error al convertir los datos del diccionario al tipo de variable del modelo" +
                                "\n" + e.getMessage()
            );
        }
    }
    
    
    private void eliminarCompra(){
    
        boolean deseaContinuar =
                dialogoCompra.mostrarAlertaAdvertenciaConRespuesta(
            "Esta a punto de eliminar la compra, este cambio es irreversible \nEsta seguro?"
        );
        
        if(deseaContinuar){
            try {
                eliminarCompra( idCompra );
                resultadoEdicion = ResultadoDialogo.ELIMINADO;
                dialogoCompra.mostrarAlertaExitosa("Compra eliminada correctamente");
                dialogoCompra.dispose();
            } catch (Exception e) {
                dialogoCompra.mostrarAlertaError(e.getMessage());
            }
        }
    
    }
    
}

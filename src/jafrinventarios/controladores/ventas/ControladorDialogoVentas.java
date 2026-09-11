
package jafrinventarios.controladores.ventas;

import jafrinventarios.DTOs.productos.DTOProductoPrecio;
import jafrinventarios.controladores.utilidades.ResultadoDialogo;
import jafrinventarios.modelos.ModeloSesionUsuario;
import jafrinventarios.modelos.ventas.ModeloDetalleVenta;
import jafrinventarios.modelos.ventas.ModeloVenta;
import jafrinventarios.servicios.clientes.ServicioClientes;
import jafrinventarios.servicios.productos.ServicioProductos;
import jafrinventarios.servicios.usuarios.ServicioUsuarios;
import jafrinventarios.servicios.ventas.ServicioVentas;
import jafrinventarios.vistas.ventas.dialogoVenta.DialogoFormularioVenta;
import jafrinventarios.vistas.ventas.dialogoVenta.DialogoFormularioVenta.TipoDialogo;
import jafrinventarios.vistas.ventas.dialogoVenta.FilaFormularioDetalleVenta;


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
public class ControladorDialogoVentas {
    
    private final DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a");
    
    private DialogoFormularioVenta dialogoVenta;
    private ServicioVentas servicioVentas;
    
    private TipoDialogo tipoDialogo;
    
    private ModeloVenta modeloVenta;
    
    private LinkedHashMap<Integer, DTOProductoPrecio> diccionarioProductosPrecio;
    private LinkedHashMap< Integer, String > diccionarioProductosId;
    
    /*Este diccionario mantendra la relacion entre el modeloDetalleVenta, con su 
    respectiva fila en la parte grafica.
    */
    private LinkedHashMap< FilaFormularioDetalleVenta, ModeloDetalleVenta > diccionarioDetalles;
    private double totalVenta = 0.0;
    
    /*
    Variable que en el caso de editar tendra el id del registro a modificar
    pero en el case de crear guardara el id de la venta creada.
    */
    private Integer idVenta;
    
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

    private ControladorDialogoVentas(   DialogoFormularioVenta dialogoVenta, 
                                        ServicioVentas servicioVentas, 
                                        TipoDialogo tipoDialogo, 
                                        Integer idVenta) {
        this.dialogoVenta = dialogoVenta;
        this.servicioVentas = servicioVentas;
        this.tipoDialogo = tipoDialogo;
        this.idVenta = idVenta;
        
        this.diccionarioProductosPrecio = new LinkedHashMap<>();
        this.diccionarioProductosId = new LinkedHashMap<>();
        
        this.diccionarioDetalles = new LinkedHashMap<>();
        
        configuracionInicial();
        
        this.dialogoVenta.mostrar();
    }
    
    
    private void configuracionInicial(){
    
        inicializarComboBoxClientes( tipoDialogo == TipoDialogo.CREAR_NUEVA_VENTA );
        inicializarDiccionariosProductos( tipoDialogo == TipoDialogo.CREAR_NUEVA_VENTA );
        
        if( tipoDialogo == TipoDialogo.EDITAR_VENTA ){
            try {
                modeloVenta = obtenerModeloVenta(idVenta);
                totalVenta = modeloVenta.getTotalVenta();
                
                
                poblarDiccionarioFilasDetalles();
                
                cargarDatosAVista();
                
            } catch (Exception e) {
                dialogoVenta.mostrarAlertaError(e.getMessage());
            }
        }else{
            dialogoVenta.setEnableBtnAgregarProducto(false);
        }
        
        dialogoVenta.inicializarSelectorFechaHora();
        
        inicializarEventosBotonesPrincipales();
        
    }
    
    
    private void poblarDiccionarioFilasDetalles(){
    
        for(ModeloDetalleVenta modeloDetalle : modeloVenta.getDetalles()){
            
            FilaFormularioDetalleVenta filaDetalle = crearNuevaFilaDetalle();
            filaDetalle = asignarDatosFilaDetalle( filaDetalle, modeloDetalle );
            
            diccionarioDetalles.put( filaDetalle , modeloDetalle.clonar() );
  
            inicializarEventosFilaDetalle(filaDetalle);
                        
        }
        
    }
    
    
    private void cargarDatosAVista() throws Exception{
        
        dialogoVenta.setIdVenta(idVenta);

        String aliasUsuario = obtenerAliasUsuario(modeloVenta.getIdUsuario());
        dialogoVenta.setAliasUsuario(aliasUsuario);

        dialogoVenta.asignarDatosEnFormulario(
                new HashMap<>(
                    Map.of("cliente", String.valueOf(modeloVenta.getIdCliente()), 
                            "fechaHora", modeloVenta.getFechaHoraVenta().format(formatoFecha)
                    )
                )
        );

        dialogoVenta.setTotalVenta(modeloVenta.getTotalVenta());
        
        dialogoVenta.inyectarFilasDetalles(new ArrayList<>(diccionarioDetalles.keySet()));
    }
    
    
    private void inicializarEventosBotonesPrincipales(){
    
        dialogoVenta.getBtnAgregarProducto().addActionListener( e -> agregarFilaDetalle() );
        dialogoVenta.getBtnEnviarFormulario().addActionListener( e -> procesarFormulario() );
        
        if(tipoDialogo == TipoDialogo.EDITAR_VENTA)
            dialogoVenta.getBtnLinkEliminarRegistro().addActionListener( e -> eliminarVenta());
    }
    
    /*
    ============================================================================
     METODOS ESTÁTICoS: Los únicos puntos de acceso para los demás controladores
    ============================================================================
    */
    public static ResultadoDialogo editarVenta (   java.awt.Window ventanaPadre,
                                                    Integer idVenta,
                                                    ServicioVentas servicioVentas){
        
        DialogoFormularioVenta dialogoVenta = 
                new DialogoFormularioVenta( ventanaPadre, TipoDialogo.EDITAR_VENTA);
        
        ControladorDialogoVentas controlador =
                new ControladorDialogoVentas(
                        dialogoVenta,
                        servicioVentas,
                        TipoDialogo.EDITAR_VENTA,
                        idVenta
                );
        
        return controlador.resultadoEdicion;
    
    }
    
    
    public static Integer crearVenta (   java.awt.Window ventanaPadre,
                                                    ServicioVentas servicioVentas){
        
        DialogoFormularioVenta dialogoVenta = 
                new DialogoFormularioVenta( ventanaPadre, TipoDialogo.CREAR_NUEVA_VENTA);
        
        ControladorDialogoVentas controlador =
                new ControladorDialogoVentas(
                        dialogoVenta,
                        servicioVentas,
                        TipoDialogo.CREAR_NUEVA_VENTA,
                        -1
                );
        
        return controlador.idVenta;
    
    }
    
    /*
    ============================================================================
                METODOS PARA CONSULTAR A LOS SERVICIOS
    ============================================================================
    */
    
    private LinkedHashMap<Integer, String> obtenerDiccionarioClientes(boolean soloHabilitados) throws Exception{
        return ServicioClientes.obtenerDiccionarioClientes(soloHabilitados);
    }
    
    private LinkedHashMap<Integer, DTOProductoPrecio> obtenerDTOProductosPrecio(boolean soloHabilitados) throws Exception{
        return ServicioProductos.obtenerProductosConPrecioVenta(soloHabilitados);
    }
    
    private String obtenerAliasUsuario ( int idUsuario ) throws Exception{
        return ServicioUsuarios.obtenerAliasUsuario(idUsuario);
    }
    
    private ModeloVenta obtenerModeloVenta( Integer idVenta ) throws Exception{
        return servicioVentas.obtenerModeloVenta(idVenta);
    }
    
    private int crearVenta( ModeloVenta venta ) throws Exception{
        return servicioVentas.crearVenta(venta);
    }
    
    private void editarVenta( ModeloVenta venta ) throws Exception{
        servicioVentas.editarVenta(venta);
    }
    
    private void eliminarVenta( int idVenta )throws Exception{
        servicioVentas.eliminarVenta(idVenta);
    }
    
  
    /*
    ============================================================================
        METODOS PARA RECOLECTAR LA INFORMACION QUE ENTREGA LOS SERVICIOS
    ============================================================================
    */
    private void inicializarComboBoxClientes( boolean soloHabilitados ){
        try {
            LinkedHashMap<Integer, String> diccionarioClientes = obtenerDiccionarioClientes(soloHabilitados);
            if( diccionarioClientes.isEmpty() )
                dialogoVenta.mostrarAlertaAdvertenciaSinRespuesta(
                        "Para poder crear una venta, debe existir por lo menos un cliente\n"
                        + "Por favor dirigete a la seccion de clientes y crea uno."
                );
            else
                dialogoVenta.inicializarComboBoxClientes(diccionarioClientes);
                dialogoVenta.getComboBoxClientes().addActionListener( e -> activarAgregarProducto() );
        } catch (Exception e) {
            dialogoVenta.mostrarAlertaError(e.getMessage());
        }
    }
    
    
    private void activarAgregarProducto(){
        try {
            HashMap<String, String> datosFormulario = dialogoVenta.recolectarDatosFormulario();
            
            if(datosFormulario.containsKey("cliente")){
                int idCliente = Integer.parseInt( datosFormulario.get("cliente"));
                //El recolector de formulario debe entregar un entero
                //En dado caso que no se un entero se entiende que aun el usuario
                //No ha seleccionado un cliente y por ende entra en el catch
                dialogoVenta.setEnableBtnAgregarProducto(true);
            }
       
        } catch (Exception exception) {
            //Si no seleccionaron un cliente no dejamos que se agregue un producto
            dialogoVenta.setEnableBtnAgregarProducto(false);
        }
    }
    
    
    private void inicializarDiccionariosProductos(boolean soloHabilitados){
        
        try {
            diccionarioProductosPrecio = obtenerDTOProductosPrecio(soloHabilitados);
            if( diccionarioProductosPrecio.isEmpty() )
                dialogoVenta.mostrarAlertaAdvertenciaSinRespuesta(
                        "Aun no hay productos almacenados\n"
                        + "Por favor dirigete a la seccion de productos y cree los productos."
                );
            else
                diccionarioProductosPrecio.forEach(
                        (idProducto, producto) -> {
                            diccionarioProductosId.put(idProducto, producto.getNombreProducto());
                        }
                );
        } catch (Exception e) {
            dialogoVenta.mostrarAlertaError(e.getMessage());
        }
    
    }
    
    
    /*
    ============================================================================
                METODOS PARA GESTIONAR LAS FILAS DE DETALLES
    ============================================================================
    */
    private FilaFormularioDetalleVenta crearNuevaFilaDetalle(){
    
        FilaFormularioDetalleVenta filaDetalle = new FilaFormularioDetalleVenta();
        
        filaDetalle.inicializarComboBoxProductos(diccionarioProductosId);
        filaDetalle.setItem( diccionarioDetalles.size()+ 1 );
        
        return filaDetalle;
    }
    
    
    private FilaFormularioDetalleVenta asignarDatosFilaDetalle ( 
                                            FilaFormularioDetalleVenta filaDetalle,
                                            ModeloDetalleVenta datosDetalle
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
    
    
    private void inicializarEventosFilaDetalle ( FilaFormularioDetalleVenta filaDetalle ){
        
        inicializarEventoInputCantidad(filaDetalle);
        
        inicializarEventoComboBoxProductos(filaDetalle);
        
        inicializarEventoEliminarFila(filaDetalle);
        
    }
    
    private void inicializarEventoInputCantidad( FilaFormularioDetalleVenta filaDetalle ){
        
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
    
    
    private void inicializarEventoComboBoxProductos( FilaFormularioDetalleVenta filaDetalle ){
    
        filaDetalle.getComboBoxProductos().addActionListener( e -> {
            ModeloDetalleVenta modeloDetalle = diccionarioDetalles.get(filaDetalle);
            
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
            
            actualizarTotalVenta();
            
        });
        
    }
    
    
    private void actualizarCantidadProducto( FilaFormularioDetalleVenta filaDetalle ){
        
        ModeloDetalleVenta detalleVenta = diccionarioDetalles.get(filaDetalle);
        try {
            int cantidad = Integer.parseInt( filaDetalle.getInputCantidad().getText() );
            detalleVenta.setCantidadProducto(cantidad);

            filaDetalle.setPrecioTotal( detalleVenta.getPrecioTotalProducto()  );

        } catch (Exception e) {
            detalleVenta.setCantidadProducto(0);
            filaDetalle.setPrecioTotal( 0 );
        }
        
        actualizarTotalVenta();
    
    }
    
    
    private void actualizarTotalVenta (){
    
        double totalVenta = 0.0;
        if(!diccionarioDetalles.isEmpty()){
            for( ModeloDetalleVenta modeloDetalle: diccionarioDetalles.values()){
                totalVenta += modeloDetalle.getPrecioTotalProducto();
            }
        }
        this.totalVenta = totalVenta;
        dialogoVenta.setTotalVenta(totalVenta);
    
    }
    
    
    private void inicializarEventoEliminarFila( FilaFormularioDetalleVenta filaDetalle){
    
        filaDetalle.getBtnEliminar().addActionListener( e -> {
            boolean continuar = dialogoVenta.mostrarAlertaAdvertenciaConRespuesta(
                    "¿Quieres quitar este producto?\n"
                    + "Esta acción se aplicará solo en este borrador. No será permanente hasta que guardes los cambios de la venta.");
            
            if(continuar){
                diccionarioDetalles.remove(filaDetalle);
                actualizarValorItemDetalles();
                dialogoVenta.removerFilaDetalle(filaDetalle);
                actualizarTotalVenta();
            }
            
        });
        
    }
    
    private void actualizarValorItemDetalles (){
    
        int item = 0;
        for( FilaFormularioDetalleVenta filaDetalle : diccionarioDetalles.keySet()){
            filaDetalle.setItem(++item);
        }
 
    }
    
    
    
    /*
    ============================================================================
                        METODOS OPERACION DE LOS BOTONES
    ============================================================================
    */
    
    private void agregarFilaDetalle(){
        
        FilaFormularioDetalleVenta filaDetalle = crearNuevaFilaDetalle();

        diccionarioDetalles.put( filaDetalle , new ModeloDetalleVenta() );

        inicializarEventosFilaDetalle(filaDetalle);
        
        dialogoVenta.inyectarNuevaFilaDetalle(filaDetalle);
        
    }
    
    
    private void procesarFormulario(){
    
        /*
        ========================================================================
               Verificar si esta correctamente diligenciado el formulario
        ========================================================================
        */
        //Antes de validar, por lo menos debe existir un detalle de venta
        if( diccionarioDetalles.isEmpty() ){
            dialogoVenta.mostrarAlertaError("No se puede guardar una venta sin productos, por favor cree por lo menos uno");
            return;
        }
        
        //Verificar que los campos esten diligenciados con un formato valido
        //En este caso necesitamos validar tanto en los datos generales de la venta
        //como en cada uno de los detalles
        boolean datosValidos = true;
        if( !dialogoVenta.validarFormulario() ) 
            datosValidos = false;
        for( FilaFormularioDetalleVenta filaDetalles : diccionarioDetalles.keySet()){
            if( !filaDetalles.validarFormulario() )
                datosValidos = false;
        }
        //dejamos que se validen todos, ya que la misma vista mostrara los errores en cada campo
        
        if( !datosValidos ){
            dialogoVenta.mostrarAlertaErrorFormatoCampos();
            return;
        }
        
        //Extraer los datos generales 
        HashMap<String, String> datosFormulario = dialogoVenta.recolectarDatosFormulario();
       
        
        /*
        ========================================================================
            Validar si hay cambios cuando es editar, 
            para la creacion solo instanciamos el modelo
        ========================================================================
        */
        ModeloVenta ventaAProcesar = 
                ( tipoDialogo == TipoDialogo.EDITAR_VENTA)
                ? modeloVenta.clonar()
                : new ModeloVenta();
        
        try {
            ventaAProcesar = asignarDatosAModelo(ventaAProcesar, datosFormulario);
        } catch (Exception e) {
            dialogoVenta.mostrarAlertaError( e.getMessage() );
            return;
        }
        
        //Verificar si los modelos son iguales en dado caso que tipoDialogo sea EDITAR
        if( tipoDialogo == TipoDialogo.EDITAR_VENTA ){
            if ( modeloVenta.equals( ventaAProcesar )) {
                dialogoVenta.mostrarAlertaError("No hay cambios para guardar");
                return;
            }
        }
        
        //Si no son iguales las ventas asignamos el id del usuario que inicio sesion
        //de igual manera esta asignacion funciona para una nueva venta
        ventaAProcesar.setIdUsuario(ModeloSesionUsuario.getInstancia().getIdUsuario());
        
        
        /*
        =======================================================================
        GUARDAR EN LA BASE DE DATOS solo si cumplio las anteriores validaciones
        =======================================================================
        */
        switch(tipoDialogo){
            case EDITAR_VENTA:
                try {
                    editarVenta(ventaAProcesar);
                    dialogoVenta.mostrarAlertaExitosa("Venta actualizada correctamente");
                    resultadoEdicion = ResultadoDialogo.ACTUALIZADO;
                    dialogoVenta.dispose();
                }catch (Exception e) {
                    dialogoVenta.mostrarAlertaError(e.getMessage());
                }
                break;
            case CREAR_NUEVA_VENTA:
                try {
                    idVenta = crearVenta(ventaAProcesar);
                    dialogoVenta.mostrarAlertaExitosa("Venta creada correctamente");
                    resultadoEdicion = ResultadoDialogo.ACTUALIZADO;
                    dialogoVenta.dispose();
                }catch (Exception e) {
                    dialogoVenta.mostrarAlertaError(e.getMessage());
                }
                break;
        }
    
    }
    
    
    private ModeloVenta asignarDatosAModelo ( ModeloVenta modeloVenta, HashMap<String, String> datos ) throws Exception{
    
        try {
            modeloVenta.setIdCliente(Integer.parseInt( datos.get("cliente")));
            modeloVenta.setFechaHoraVenta(LocalDateTime.parse( datos.get("fechaHora"), formatoFecha));
            modeloVenta.setDetalles( new ArrayList<>(diccionarioDetalles.values()) );
            modeloVenta.setTotalVenta(totalVenta);
            return modeloVenta;
        } catch (Exception e) {
            throw new Exception( "Error al convertir los datos del diccionario al tipo de variable del modelo" +
                                "\n" + e.getMessage()
            );
        }
    }
    
    
    private void eliminarVenta(){
    
        boolean deseaContinuar =
                dialogoVenta.mostrarAlertaAdvertenciaConRespuesta(
            "Esta a punto de eliminar la venta, este cambio es irreversible \nEsta seguro?"
        );
        
        if(deseaContinuar){
            try {
                eliminarVenta( idVenta );
                resultadoEdicion = ResultadoDialogo.ELIMINADO;
                dialogoVenta.mostrarAlertaExitosa("Venta eliminada correctamente");
                dialogoVenta.dispose();
            } catch (Exception e) {
                dialogoVenta.mostrarAlertaError(e.getMessage());
            }
        }
    
    }
    
}

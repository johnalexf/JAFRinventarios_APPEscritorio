
package jafrinventarios.controladores.compras;

import jafrinventarios.DTOs.productos.DTOProductoPrecio;
import jafrinventarios.controladores.utilidades.ResultadoDialogo;
import jafrinventarios.modelos.compras.ModeloCompra;
import jafrinventarios.modelos.compras.ModeloDetalleCompra;
import jafrinventarios.servicios.compras.ServicioCompras;
import jafrinventarios.servicios.productos.ServicioProductos;
import jafrinventarios.servicios.proveedores.ServicioProveedores;
import jafrinventarios.servicios.usuarios.ServicioUsuarios;
import jafrinventarios.vistas.compras.dialogoCompra.DialogoFormularioCompra;
import jafrinventarios.vistas.compras.dialogoCompra.DialogoFormularioCompra.TipoDialogo;
import jafrinventarios.vistas.compras.dialogoCompra.FilaFormularioDetalleCompra;
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
                
                modeloCompra = obtenerModeloCompra(idCompra);
                
                inicializarDiccionariosProductos( modeloCompra.getIdProveedor() );
                poblarDiccionarioFilasDetalles();
                
                cargarDatosAVista();
                
            } catch (Exception e) {
                dialogoCompra.mostrarAlertaError(e.getMessage());
            }
        }
        
        dialogoCompra.inicializarSelectorFechaHora();
        
    }
    
    private void poblarDiccionarioFilasDetalles(){
    
        for(ModeloDetalleCompra modeloDetalle : modeloCompra.getDetalles()){
            
            diccionarioDetalles.put(  
                    crearNuevaFilaDetalle(  modeloDetalle.getIdProducto(), 
                                            modeloDetalle.getCantidadProducto(), 
                                            modeloDetalle.getPrecioUnitarioProducto(), 
                                            modeloDetalle.getPrecioTotalProducto()
                    ),
                    modeloDetalle.clonar()
            );
                        
        }
        
    }
    
    
    private void cargarDatosAVista() throws Exception{
        
        dialogoCompra.setIdCompra(idCompra);

        String aliasUsuario = obtenerAliasUsuario(modeloCompra.getIdUsuario());
        dialogoCompra.setAliasUsuario(aliasUsuario);

        DateTimeFormatter formateador = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a");

        dialogoCompra.asignarDatosEnFormulario(
                new HashMap<>(
                    Map.of("proveedor", String.valueOf(modeloCompra.getIdProveedor()), 
                            "fechaHora", modeloCompra.getFechaHoraCompra().format(formateador)
                    )
                )
        );

        dialogoCompra.setTotalCompra( modeloCompra.getTotalCompra());
        
        dialogoCompra.inyectarFilasDetalles(new ArrayList<>(diccionarioDetalles.keySet()));
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
            else
                dialogoCompra.inicializarComboBoxProveedores(diccionarioProveedores);        
        } catch (Exception e) {
            dialogoCompra.mostrarAlertaError(e.getMessage());
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
    private FilaFormularioDetalleCompra crearNuevaFilaDetalle(
                                                        Integer idProducto,
                                                        int cantidadProducto,
                                                        double precioUnitarioProducto,
                                                        double precioTotalProducto
    ){
    
        FilaFormularioDetalleCompra filaDetalle = new FilaFormularioDetalleCompra();
        
        filaDetalle.inicializarComboBoxProductos(diccionarioProductosId);
        filaDetalle.setItem( diccionarioDetalles.size()+ 1 );
        filaDetalle.asignarDatosEnFormulario(
            new HashMap<>(
                Map.of("producto", String.valueOf( idProducto ), 
                       "cantidadProducto", String.valueOf(cantidadProducto)
                )
            )
        );
        filaDetalle.setPrecioUnitario( precioUnitarioProducto );
        filaDetalle.setPrecioTotal( precioTotalProducto );
    
        inicializarEventoInputCantidad(filaDetalle);
        
        return filaDetalle;
    }
    
    
    private void inicializarEventoInputCantidad( FilaFormularioDetalleCompra filaDetalle ){
        
        filaDetalle.getInputCantidad().getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                calcularTotalProducto(filaDetalle);
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                calcularTotalProducto(filaDetalle);
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                calcularTotalProducto(filaDetalle);
            }
        });
        
    }
    
    private void calcularTotalProducto( FilaFormularioDetalleCompra filaDetalle ){
        
        ModeloDetalleCompra detalleCompra = diccionarioDetalles.get(filaDetalle);
        try {
            int cantidad = Integer.parseInt( filaDetalle.getInputCantidad().getText() );
            detalleCompra.setCantidadProducto(cantidad);

            filaDetalle.setPrecioTotal( detalleCompra.getPrecioTotalProducto()  );

        } catch (Exception e) {
            detalleCompra.setCantidadProducto(0);
            filaDetalle.setPrecioTotal( 0 );
        }
    
    }
    
    
    
}

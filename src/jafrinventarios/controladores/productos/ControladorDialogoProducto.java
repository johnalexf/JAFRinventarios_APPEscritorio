
package jafrinventarios.controladores.productos;

import jafrinventarios.controladores.utilidades.ResultadoDialogo;
import jafrinventarios.modelos.ModeloSesionUsuario;
import jafrinventarios.modelos.productos.ModeloProducto;
import jafrinventarios.servicios.excepciones.ExcepcionValidacionBD;
import jafrinventarios.servicios.productos.ServicioProductos;
import jafrinventarios.servicios.proveedores.ServicioProveedores;
import jafrinventarios.vistas.productos.DialogoFormularioProducto;
import jafrinventarios.vistas.productos.DialogoFormularioProducto.TipoDialogo;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 *
 * @author JOHN FORERO
 */
public class ControladorDialogoProducto {
    
    private DialogoFormularioProducto dialogoProducto;
    private ServicioProductos servicioProductos;
    
    // Variable de soporte para personalizar el controlador si es para editar o crear
    private TipoDialogo tipoDialogo;
    
    private ModeloProducto modeloProducto;

    /*
    Variable que determina si un registro a editar tiene asociacion con otros
    en dado caso no se puede eliminar pero si habilitar
    */
    private boolean isProductoEliminable = false;
    
    /*
    Variable que en el caso de editar tendra el id del registro a modificar
    pero en el case de crear guardara el id del producto creado.
    */
    private int idProducto;
    
    /*
    Variable para responde la operacion realizada al editar
    que pueden ser las siguientes opciones:  ACTUALIZADO, ELIMINADO, SIN_CAMBIOS 
    */
    private ResultadoDialogo resultadoEdicion = ResultadoDialogo.SIN_CAMBIOS;
    
    private boolean usuarioAdministrador;
    
    
    /*
    ============================================================================
      CONSTRUCTOR PRIVADO PARA EVITAR QUE SE CREE SIN SU DEBIDA CONFIGURACION
    ============================================================================
    */
    private ControladorDialogoProducto( DialogoFormularioProducto dialogoProducto,
                                        TipoDialogo tipoDialogo,
                                        int idProducto,
                                        ServicioProductos servicioProductos) {
        this.dialogoProducto = dialogoProducto;
        this.tipoDialogo = tipoDialogo;
        this.idProducto = idProducto;
        this.servicioProductos = servicioProductos;
        
        this.usuarioAdministrador = ModeloSesionUsuario.getInstancia().isAdministrador();
        
        inicializarComboBoxProveedores();
        
        if ( tipoDialogo == TipoDialogo.EDITAR_PRODUCTO) {
            try {
                modeloProducto = obtenerModeloProducto( idProducto );
                isProductoEliminable = isProductoEliminable( idProducto );
                cargarDatosAVista( obtenerModeloEnDiccionario() );
            } catch (Exception e) {
                this.dialogoProducto.mostrarAlertaError(e.getMessage());
            }
        }
        
        inicializarEventosBotones();
        
        this.dialogoProducto.mostrar();
    }
    
    
    /*
    ============================================================================
     METODOS ESTÁTICoS: Los únicos puntos de acceso para los demás controladores
    ============================================================================
    */
    
    public static ResultadoDialogo editarProducto( java.awt.Window ventanaPadre,
                                            int idProducto,
                                            ServicioProductos servicioProductos ){
        
        DialogoFormularioProducto dialogoProducto =
            new DialogoFormularioProducto( ventanaPadre, TipoDialogo.EDITAR_PRODUCTO);
        
        ControladorDialogoProducto controlador =
                new ControladorDialogoProducto(
                        dialogoProducto,
                        TipoDialogo.EDITAR_PRODUCTO,
                        idProducto,
                        servicioProductos
                );
        
        return controlador.getResultadoEdicion();
    }
    
    
    public static int crearProducto( java.awt.Window ventanaPadre,
                                     ServicioProductos servicioProductos ){
        
        DialogoFormularioProducto dialogoProducto =
            new DialogoFormularioProducto( ventanaPadre, TipoDialogo.CREAR_NUEVO_PRODUCTO);
        
        ControladorDialogoProducto controlador =
                new ControladorDialogoProducto(
                        dialogoProducto,
                        TipoDialogo.CREAR_NUEVO_PRODUCTO,
                        -1,
                        servicioProductos
                );
        
        return controlador.getIdProducto();
    }
    
    
    /*
    ============================================================================
                METODOS PARA CONSULTAR A LOS SERVICIOS
    ============================================================================
    */
    
    private LinkedHashMap<Integer, String> obtenerDiccionarioProveedores() throws Exception{
        return ServicioProveedores.obtenerDiccionarioProveedores();
    }
    
    private ModeloProducto obtenerModeloProducto (int idProducto) throws Exception{
        return servicioProductos.obtenerModeloProducto(idProducto);
    }
    
    private boolean isProductoHabilitado (int idProducto) throws Exception{
        return servicioProductos.isProductoHabilitado(idProducto);
    }
    
    private int crearProducto( ModeloProducto producto ) throws Exception {
        return servicioProductos.crearProducto( producto, usuarioAdministrador );
    }
    
    private void editarProducto( ModeloProducto producto ) throws Exception {
        servicioProductos.editarProducto(producto, usuarioAdministrador);
    }
    
    private void conmutarEstadoProducto( int idProducto ) throws Exception{
        servicioProductos.conmutarEstadoProducto(idProducto, usuarioAdministrador);
    }
    
    private boolean isProductoEliminable ( int idProducto ) throws Exception{
        return servicioProductos.isProductoEliminable( idProducto );
    }
    
    private void eliminarProducto ( int idProducto ) throws Exception {
        servicioProductos.eliminarProducto( idProducto, usuarioAdministrador );
    }
    
    
    /*
    ============================================================================
                            METODOS INICIALES
    ============================================================================
    */
    
    private void inicializarComboBoxProveedores(){
        try {
            LinkedHashMap<Integer, String> diccionarioProveedores = obtenerDiccionarioProveedores();
            if( diccionarioProveedores.isEmpty() )
                dialogoProducto.mostrarAlertaAdvertenciaSinRespuesta(
                        "Para poder crear un producto, debe existir por lo menos un proveedor\n"
                        + "Por favor dirigete a la seccion de proveedores y crea uno."
                );
            else
                dialogoProducto.inicializarComboBoxProveedores(diccionarioProveedores);        
        } catch (Exception e) {
            dialogoProducto.mostrarAlertaError(e.getMessage());
        }
    }
    
    private HashMap<String, String> obtenerModeloEnDiccionario(){
    
        HashMap<String, String> diccionarioProducto = new HashMap<>();
        
        diccionarioProducto.put( "nombreProducto", modeloProducto.getNombreProducto() );
        diccionarioProducto.put( "proveedor", Integer.toString( modeloProducto.getIdProveedor() ) );
        diccionarioProducto.put( "precioCompra", Double.toString( modeloProducto.getPrecioCompra() ) );
        diccionarioProducto.put( "precioVenta", Double.toString( modeloProducto.getPrecioVenta() ) );
        diccionarioProducto.put( "cantidadMinimaStock", Integer.toString( modeloProducto.getCantidadMinimaStock() ));
        diccionarioProducto.put( "cantidadDisponible", Integer.toString( modeloProducto.getCantidadDisponible() ) );
    
        return diccionarioProducto;
        
    }
    
    private void cargarDatosAVista ( HashMap<String, String> producto ){
        dialogoProducto.setId( idProducto );
        dialogoProducto.asignarDatosEnFormulario( producto );
        if ( isProductoEliminable ) {
            dialogoProducto.mostrarBtnLinkEliminarProducto();
        }else{
            dialogoProducto.mostrarBtnEditarEstadoProducto();
            dialogoProducto.asignarIntencionBtnEditarEstadoProducto(
                                            modeloProducto.isHabilitado());
        }
    }
    
    
    private void inicializarEventosBotones(){
    
        if( tipoDialogo == TipoDialogo.EDITAR_PRODUCTO){
            if( isProductoEliminable )
                dialogoProducto.getBtnLinkEliminarProducto().addActionListener(e -> eliminarProducto() );
            else
                dialogoProducto.getBtnLinkEditarEstadoProducto().addActionListener( e -> conmutarEstadoProducto() );
        }
        
        dialogoProducto.getBtnEnviarFormulario().addActionListener( e -> procesarFormulario() );
    
    }
    
    
    /*
    ============================================================================
                        METODOS OPERACION DE LOS BOTONES
    ============================================================================
    */
    
    private void procesarFormulario(){
        
        // Verificar que los campos esten diligenciados con un formato valido
        if( !dialogoProducto.validarFormulario() ){
            dialogoProducto.mostrarAlertaErrorFormatoCampos();
            return;
        }
        
        //Extraer los datos del formulario
        HashMap<String, String> datosFormulario = dialogoProducto.recolectarDatosFormulario();
        
        
        /*
        Asignar datos a modelo, 
        clonamos si es para editar con el fin de comprobar si hubieron cambios, 
        pero si es para crear uno nuevo unicamente instanciamos el Modelo
        */
        ModeloProducto productoAProcesar =   
                (tipoDialogo == TipoDialogo.EDITAR_PRODUCTO)
                ? modeloProducto.clonar()
                : new ModeloProducto();
        
        try {
            productoAProcesar = asignarDatosAModelo( productoAProcesar, datosFormulario);
        } catch (Exception e) {
            dialogoProducto.mostrarAlertaError( e.getMessage() );
            return;
        }
        
        //Verificar si los modelos son iguales en dado caso que tipoDialogo sea EDITAR
        if( tipoDialogo == TipoDialogo.EDITAR_PRODUCTO ){
            if ( modeloProducto.equals( productoAProcesar )) {
                if ( resultadoEdicion == ResultadoDialogo.ACTUALIZADO) {
                    //Si entra aqui es por que se edito su estado habilitado
                    dialogoProducto.mostrarAlertaExitosa("Producto editado correctamente");
                    dialogoProducto.dispose();
                }else{
                    dialogoProducto.mostrarAlertaError("No hay cambios para guardar");
                    return;
                }  
            }
        }
        
        
        /*
        =======================================================================
        GUARDAR EN LA BASE DE DATOS solo si cumplio las anteriores validaciones
        =======================================================================
        */
        switch(tipoDialogo){
            case EDITAR_PRODUCTO:
                try {
                    editarProducto( productoAProcesar );
                    dialogoProducto.mostrarAlertaExitosa("Producto actualizado correctamente");
                    resultadoEdicion = ResultadoDialogo.ACTUALIZADO;
                    dialogoProducto.dispose();
                }catch (ExcepcionValidacionBD e) {
                    dialogoProducto.mostrarErroresValidacionCampos( e.getErrores() );
                }catch (Exception e) {
                    dialogoProducto.mostrarAlertaError(e.getMessage());
                }
                break;
            case CREAR_NUEVO_PRODUCTO:
                try {
                    idProducto = crearProducto( productoAProcesar );
                    dialogoProducto.mostrarAlertaExitosa("Producto creado correctamente");
                    dialogoProducto.dispose();
                }catch (ExcepcionValidacionBD e) {
                    dialogoProducto.mostrarErroresValidacionCampos( e.getErrores() );
                }catch (Exception e) {
                    dialogoProducto.mostrarAlertaError(e.getMessage());
                }
                break;
        }
    
    }
    
    private ModeloProducto asignarDatosAModelo( ModeloProducto producto, HashMap<String, String> datos ) throws Exception{
    
        try {
            if( datos.containsKey("nombreProducto") )
                producto.setNombreProducto( datos.get("nombreProducto"));
            if( datos.containsKey("proveedor") )
                producto.setIdProveedor( Integer.parseInt( datos.get("proveedor") ));
            if( datos.containsKey("precioCompra") )
                producto.setPrecioCompra(Double.parseDouble(datos.get("precioCompra")));
            if( datos.containsKey("precioVenta") )
                producto.setPrecioVenta( Double.parseDouble(datos.get("precioVenta")));
            if( datos.containsKey("cantidadMinimaStock") )
                producto.setCantidadMinimaStock( Integer.parseInt(datos.get("cantidadMinimaStock")));
            if( datos.containsKey("cantidadDisponible") )
                producto.setCantidadDisponible( Integer.parseInt(datos.get("cantidadDisponible"))); 
            
            return producto;
        } catch (Exception e) {
            throw new Exception( "Error al convertir los datos del diccionario al tipo de variable del modelo" +
                                "\n" + e.getMessage()
            );
        }
        
    }
    
    
    private void conmutarEstadoProducto(){
        boolean deseaContinuar = dialogoProducto.mostrarAlertaAdvertenciaConRespuesta(
            ( modeloProducto.isHabilitado()
            ? "Esta a punto de deshabilitar el producto y por tanto ya no podra registrar ventas o compras con el."
            : "Esta a punto de habilitar el producto y ahora lo podra usar en ventas o compras." )
            + "\nEsta seguro?"
        );
        
        if(deseaContinuar){
            try {
                conmutarEstadoProducto( idProducto );
                modeloProducto.setHabilitado( isProductoHabilitado(idProducto) );
                dialogoProducto.asignarIntencionBtnEditarEstadoProducto(
                                            modeloProducto.isHabilitado()
                );
                resultadoEdicion = ResultadoDialogo.ACTUALIZADO;
            } catch (Exception e) {
                dialogoProducto.mostrarAlertaError( e.getMessage() );
            }
        }
    
    }
    
    private void eliminarProducto(){
        boolean deseaContinuar = dialogoProducto.mostrarAlertaAdvertenciaConRespuesta(
            "Esta a punto de eliminar el producto, este cambio es irreversible \nEsta seguro?"
        );
        
        if(deseaContinuar){
            try {
                eliminarProducto(idProducto);
                resultadoEdicion = ResultadoDialogo.ELIMINADO;
                dialogoProducto.mostrarAlertaExitosa("Producto eliminado correctamente");
                dialogoProducto.dispose();
            } catch (Exception e) {
                dialogoProducto.mostrarAlertaError( e.getMessage() );
            }   
        }
    }
    
    /*
    ============================================================================
                 METODOS RESPUESTA DESPUES DE CIERRE DEL DIALOGO
    ============================================================================
    */
    
    /* 
    Funcion para retornar true en dado caso que se haya realizado 
    cualquier cambio a cualquier registro
    */
    public ResultadoDialogo getResultadoEdicion() {
        return resultadoEdicion;
    }
    
    //Funcion para retornar el id del usuario creado
    public int getIdProducto(){
        return idProducto;
    }
    
}

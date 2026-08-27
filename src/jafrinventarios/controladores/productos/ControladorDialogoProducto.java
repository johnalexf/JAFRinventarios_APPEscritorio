
package jafrinventarios.controladores.productos;

import jafrinventarios.controladores.utilidades.ResultadoDialogo;
import jafrinventarios.modelos.ModeloSesionUsuario;
import jafrinventarios.modelos.productos.ModeloProducto;
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
        
        //TODO inicializar botones
        
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
    
    
    
    
    /*
    ============================================================================
                 METODOS RESPUESTA DESPUES DE CIERRE DEL DIALOGO
    ============================================================================
    */
    private void inicializarComboBoxProveedores(){
        try {
            LinkedHashMap<Integer, String> diccionarioProveedores = obtenerDiccionarioProveedores();
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

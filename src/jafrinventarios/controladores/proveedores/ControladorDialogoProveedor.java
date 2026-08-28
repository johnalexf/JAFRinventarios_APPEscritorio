
package jafrinventarios.controladores.proveedores;

import jafrinventarios.controladores.utilidades.ResultadoDialogo;
import jafrinventarios.modelos.ModeloSesionUsuario;
import jafrinventarios.modelos.proveedores.ModeloProveedor;
import jafrinventarios.servicios.excepciones.ExcepcionValidacionBD;
import jafrinventarios.servicios.proveedores.ServicioProveedores;
import jafrinventarios.vistas.proveedores.DialogoFormularioProveedor;
import jafrinventarios.vistas.proveedores.DialogoFormularioProveedor.TipoDialogo;
import java.util.HashMap;

/**
 *
 * @author JOHN FORERO
 */
public class ControladorDialogoProveedor {
    
    private DialogoFormularioProveedor dialogoProveedor;
    private ServicioProveedores servicioProveedores;
    
    // Variable de soporte para personalizar el controlador si es para editar o crear
    private TipoDialogo tipoDialogo;
    
    private ModeloProveedor modeloProveedor;

    /*
    Variable que determina si un registro a editar tiene asociacion con otros
    en dado caso no se puede eliminar pero si habilitar
    */
    private boolean isProveedorEliminable = false;
    
    /*
    Variable que en el caso de editar tendra el id del registro a modificar
    pero en el caso de crear guardara el id del proveedor creado.
    */
    private int idProveedor;
    
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
    private ControladorDialogoProveedor( DialogoFormularioProveedor dialogoProveedor,
                                        TipoDialogo tipoDialogo,
                                        int idProveedor,
                                        ServicioProveedores servicioProveedores) {
        this.dialogoProveedor = dialogoProveedor;
        this.tipoDialogo = tipoDialogo;
        this.idProveedor = idProveedor;
        this.servicioProveedores = servicioProveedores;
        
        
        if ( tipoDialogo == TipoDialogo.EDITAR_PROVEEDOR) {
            try {
                modeloProveedor = obtenerModeloProveedor( idProveedor );
                isProveedorEliminable = isProveedorEliminable( idProveedor );
                cargarDatosAVista( obtenerModeloEnDiccionario() );
            } catch (Exception e) {
                this.dialogoProveedor.mostrarAlertaError(e.getMessage());
            }
        }
        
        inicializarEventosBotones();
        
        this.dialogoProveedor.mostrar();
    }
    
    
    /*
    ============================================================================
     METODOS ESTÁTICoS: Los únicos puntos de acceso para los demás controladores
    ============================================================================
    */
    
    public static ResultadoDialogo editarProveedor( java.awt.Window ventanaPadre,
                                            int idProveedor,
                                            ServicioProveedores servicioProveedores ){
        
        DialogoFormularioProveedor dialogoProveedor =
            new DialogoFormularioProveedor( ventanaPadre, TipoDialogo.EDITAR_PROVEEDOR);
        
        ControladorDialogoProveedor controlador =
                new ControladorDialogoProveedor(
                        dialogoProveedor,
                        TipoDialogo.EDITAR_PROVEEDOR,
                        idProveedor,
                        servicioProveedores
                );
        
        return controlador.getResultadoEdicion();
    }
    
    
    public static int crearProveedor( java.awt.Window ventanaPadre,
                                     ServicioProveedores servicioProveedores ){
        
        DialogoFormularioProveedor dialogoProveedor =
            new DialogoFormularioProveedor( ventanaPadre, TipoDialogo.CREAR_NUEVO_PROVEEDOR);
        
        ControladorDialogoProveedor controlador =
                new ControladorDialogoProveedor(
                        dialogoProveedor,
                        TipoDialogo.CREAR_NUEVO_PROVEEDOR,
                        -1,
                        servicioProveedores
                );
        
        return controlador.getIdProveedor();
    }
    
    
    /*
    ============================================================================
                METODOS PARA CONSULTAR A LOS SERVICIOS
    ============================================================================
    */
    
    
    private ModeloProveedor obtenerModeloProveedor (int idProveedor) throws Exception{
        return servicioProveedores.obtenerModeloProveedor(idProveedor);
    }
    
    private boolean isProveedorHabilitado (int idProveedor) throws Exception{
        return servicioProveedores.isProveedorHabilitado(idProveedor);
    }
    
    private int crearProveedor( ModeloProveedor proveedor ) throws Exception {
        return servicioProveedores.crearProveedor( proveedor);
    }
    
    private void editarProveedor( ModeloProveedor proveedor ) throws Exception {
        servicioProveedores.editarProveedor(proveedor);
    }
    
    private void conmutarEstadoProveedor( int idProveedor ) throws Exception{
        servicioProveedores.conmutarEstadoProveedor(idProveedor);
    }
    
    private boolean isProveedorEliminable ( int idProveedor ) throws Exception{
        return servicioProveedores.isProveedorEliminable( idProveedor );
    }
    
    private void eliminarProveedor ( int idProveedor ) throws Exception {
        servicioProveedores.eliminarProveedor( idProveedor );
    }
    
    
    /*
    ============================================================================
                            METODOS INICIALES
    ============================================================================
    */
    
    private HashMap<String, String> obtenerModeloEnDiccionario(){
    
        HashMap<String, String> diccionarioProveedor = new HashMap<>();
        
        diccionarioProveedor.put( "nombreComercial", modeloProveedor.getNombreComercial() );
        diccionarioProveedor.put( "correoProveedor", modeloProveedor.getCorreoProveedor() );
        diccionarioProveedor.put( "direccionProveedor", modeloProveedor.getDireccionProveedor() );
        diccionarioProveedor.put( "primerNombreContacto", modeloProveedor.getPrimerNombreContacto() );
        diccionarioProveedor.put( "segundoNombreContacto", modeloProveedor.getSegundoNombreContacto() );
        diccionarioProveedor.put( "primerApellidoContacto", modeloProveedor.getPrimerApellidoContacto() );
        diccionarioProveedor.put( "segundoApellidoContacto", modeloProveedor.getSegundoApellidoContacto() );
        diccionarioProveedor.put( "telefonoContacto", modeloProveedor.getTelefonoContacto() );
    
        return diccionarioProveedor;
        
    }
    
    private void cargarDatosAVista ( HashMap<String, String> proveedor ){
        dialogoProveedor.setId( idProveedor );
        dialogoProveedor.asignarDatosEnFormulario( proveedor );
        if ( isProveedorEliminable ) {
            dialogoProveedor.mostrarBtnLinkEliminarProveedor();
        }else{
            dialogoProveedor.mostrarBtnEditarEstadoProveedor();
            dialogoProveedor.asignarIntencionBtnEditarEstadoProveedor(
                                            modeloProveedor.isHabilitado());
        }
    }
    
    
    private void inicializarEventosBotones(){
    
        if( tipoDialogo == TipoDialogo.EDITAR_PROVEEDOR){
            if( isProveedorEliminable )
                dialogoProveedor.getBtnLinkEliminarProveedor().addActionListener(e -> eliminarProveedor() );
            else
                dialogoProveedor.getBtnLinkEditarEstadoProveedor().addActionListener( e -> conmutarEstadoProveedor() );
        }
        
        dialogoProveedor.getBtnEnviarFormulario().addActionListener( e -> procesarFormulario() );
    
    }
    
    
    /*
    ============================================================================
                        METODOS OPERACION DE LOS BOTONES
    ============================================================================
    */
    
    private void procesarFormulario(){
        
        // Verificar que los campos esten diligenciados con un formato valido
        if( !dialogoProveedor.validarFormulario() ){
            dialogoProveedor.mostrarAlertaErrorFormatoCampos();
            return;
        }
        
        //Extraer los datos del formulario
        HashMap<String, String> datosFormulario = dialogoProveedor.recolectarDatosFormulario();
        
        
        /*
        Asignar datos a modelo, 
        clonamos si es para editar con el fin de comprobar si hubieron cambios, 
        pero si es para crear uno nuevo unicamente instanciamos el Modelo
        */
        ModeloProveedor proveedorAProcesar =   
                (tipoDialogo == TipoDialogo.EDITAR_PROVEEDOR)
                ? modeloProveedor.clonar()
                : new ModeloProveedor();
        
        proveedorAProcesar = asignarDatosAModelo( proveedorAProcesar, datosFormulario);
        
        //Verificar si los modelos son iguales en dado caso que tipoDialogo sea EDITAR
        if( tipoDialogo == TipoDialogo.EDITAR_PROVEEDOR ){
            if ( modeloProveedor.equals( proveedorAProcesar )) {
                if ( resultadoEdicion == ResultadoDialogo.ACTUALIZADO) {
                    //Si entra aqui es por que se edito su estado habilitado
                    dialogoProveedor.mostrarAlertaExitosa("Proveedor editado correctamente");
                    dialogoProveedor.dispose();
                }else{
                    dialogoProveedor.mostrarAlertaError("No hay cambios para guardar");
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
            case EDITAR_PROVEEDOR:
                try {
                    editarProveedor( proveedorAProcesar );
                    dialogoProveedor.mostrarAlertaExitosa("Proveedor actualizado correctamente");
                    resultadoEdicion = ResultadoDialogo.ACTUALIZADO;
                    dialogoProveedor.dispose();
                }catch (ExcepcionValidacionBD e) {
                    dialogoProveedor.mostrarErroresValidacionCampos( e.getErrores() );
                }catch (Exception e) {
                    dialogoProveedor.mostrarAlertaError(e.getMessage());
                }
                break;
            case CREAR_NUEVO_PROVEEDOR:
                try {
                    idProveedor = crearProveedor( proveedorAProcesar );
                    dialogoProveedor.mostrarAlertaExitosa("Proveedor creado correctamente");
                    dialogoProveedor.dispose();
                }catch (ExcepcionValidacionBD e) {
                    dialogoProveedor.mostrarErroresValidacionCampos( e.getErrores() );
                }catch (Exception e) {
                    dialogoProveedor.mostrarAlertaError(e.getMessage());
                }
                break;
        }
    
    }
    
    private ModeloProveedor asignarDatosAModelo( ModeloProveedor proveedor, HashMap<String, String> datos ){
    
        if( datos.containsKey("nombreComercial") )
            proveedor.setNombreComercial(datos.get("nombreComercial"));
        if( datos.containsKey("correoProveedor") )
            proveedor.setCorreoProveedor(datos.get("correoProveedor"));
        if( datos.containsKey("direccionProveedor") )
            proveedor.setDireccionProveedor(datos.get("direccionProveedor"));
        if( datos.containsKey("primerNombreContacto") )
            proveedor.setPrimerNombreContacto(datos.get("primerNombreContacto"));
        if( datos.containsKey("segundoNombreContacto") )
            proveedor.setSegundoNombreContacto(datos.get("segundoNombreContacto"));
        if( datos.containsKey("primerApellidoContacto") )
            proveedor.setPrimerApellidoContacto(datos.get("primerApellidoContacto"));
        if( datos.containsKey("segundoApellidoContacto") )
            proveedor.setSegundoApellidoContacto(datos.get("segundoApellidoContacto"));
        if( datos.containsKey("telefonoContacto") )
            proveedor.setTelefonoContacto(datos.get("telefonoContacto"));

        return proveedor;
    }
    
    
    private void conmutarEstadoProveedor(){
        boolean deseaContinuar = dialogoProveedor.mostrarAlertaAdvertencia(
            modeloProveedor.isHabilitado()
            ? "Esta a punto de deshabilitar el proveedor y por tanto ya no podra registrar productos o compras con el."
            : "Esta a punto de habilitar el proveedor y ahora lo podra usar en productos o compras."
            + "\nEsta seguro?"
        );
        
        if(deseaContinuar){
            try {
                conmutarEstadoProveedor( idProveedor );
                modeloProveedor.setHabilitado( isProveedorHabilitado(idProveedor) );
                dialogoProveedor.asignarIntencionBtnEditarEstadoProveedor(
                                            modeloProveedor.isHabilitado()
                );
                resultadoEdicion = ResultadoDialogo.ACTUALIZADO;
            } catch (Exception e) {
                dialogoProveedor.mostrarAlertaError( e.getMessage() );
            }
        }
    
    }
    
    private void eliminarProveedor(){
        boolean deseaContinuar = dialogoProveedor.mostrarAlertaAdvertencia(
            "Esta a punto de eliminar el proveedor, este cambio es irreversible \nEsta seguro?"
        );
        
        if(deseaContinuar){
            try {
                eliminarProveedor(idProveedor);
                resultadoEdicion = ResultadoDialogo.ELIMINADO;
                dialogoProveedor.mostrarAlertaExitosa("Proveedor eliminado correctamente");
                dialogoProveedor.dispose();
            } catch (Exception e) {
                dialogoProveedor.mostrarAlertaError( e.getMessage() );
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
    public int getIdProveedor(){
        return idProveedor;
    }
    
}

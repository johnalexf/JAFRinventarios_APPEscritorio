
package jafrinventarios.controladores.clientes;

import jafrinventarios.controladores.utilidades.ResultadoDialogo;
import jafrinventarios.modelos.ModeloSesionUsuario;
import jafrinventarios.modelos.clientes.ModeloCliente;
import jafrinventarios.servicios.excepciones.ExcepcionValidacionBD;
import jafrinventarios.servicios.clientes.ServicioClientes;
import jafrinventarios.vistas.clientes.DialogoFormularioCliente;
import jafrinventarios.vistas.clientes.DialogoFormularioCliente.TipoDialogo;
import java.util.HashMap;

/**
 *
 * @author JOHN FORERO
 */
public class ControladorDialogoCliente {
    
    private DialogoFormularioCliente dialogoCliente;
    private ServicioClientes servicioClientes;
    
    // Variable de soporte para personalizar el controlador si es para editar o crear
    private TipoDialogo tipoDialogo;
    
    private ModeloCliente modeloCliente;

    /*
    Variable que determina si un registro a editar tiene asociacion con otros
    en dado caso no se puede eliminar pero si habilitar
    */
    private boolean isClienteEliminable = false;
    
    /*
    Variable que en el caso de editar tendra el id del registro a modificar
    pero en el caso de crear guardara el id del cliente creado.
    */
    private int idCliente;
    
    /*
    Variable para responde la operacion realizada al editar
    que pueden ser las siguientes opciones:  ACTUALIZADO, ELIMINADO, SIN_CAMBIOS 
    */
    private ResultadoDialogo resultadoEdicion = ResultadoDialogo.SIN_CAMBIOS;
    
    private boolean isAdministrador;
    
    
    /*
    ============================================================================
      CONSTRUCTOR PRIVADO PARA EVITAR QUE SE CREE SIN SU DEBIDA CONFIGURACION
    ============================================================================
    */
    private ControladorDialogoCliente( DialogoFormularioCliente dialogoCliente,
                                        TipoDialogo tipoDialogo,
                                        int idCliente,
                                        ServicioClientes servicioClientes) {
        this.dialogoCliente = dialogoCliente;
        this.tipoDialogo = tipoDialogo;
        this.idCliente = idCliente;
        this.servicioClientes = servicioClientes;
        
        this.isAdministrador = ModeloSesionUsuario.getInstancia().isAdministrador();
        
        
        if ( tipoDialogo == TipoDialogo.EDITAR_CLIENTE) {
            try {
                modeloCliente = obtenerModeloCliente( idCliente );
                isClienteEliminable = isClienteEliminable( idCliente );
                cargarDatosAVista( obtenerModeloEnDiccionario() );
            } catch (Exception e) {
                this.dialogoCliente.mostrarAlertaError(e.getMessage());
            }
        }
        
        inicializarEventosBotones();
        
        this.dialogoCliente.mostrar();
    }
    
    
    /*
    ============================================================================
     METODOS ESTÁTICoS: Los únicos puntos de acceso para los demás controladores
    ============================================================================
    */
    
    public static ResultadoDialogo editarCliente( java.awt.Window ventanaPadre,
                                            int idCliente,
                                            ServicioClientes servicioClientes ){
        
        DialogoFormularioCliente dialogoCliente =
            new DialogoFormularioCliente( ventanaPadre, TipoDialogo.EDITAR_CLIENTE);
        
        ControladorDialogoCliente controlador =
                new ControladorDialogoCliente(
                        dialogoCliente,
                        TipoDialogo.EDITAR_CLIENTE,
                        idCliente,
                        servicioClientes
                );
        
        return controlador.getResultadoEdicion();
    }
    
    
    public static int crearCliente( java.awt.Window ventanaPadre,
                                     ServicioClientes servicioClientes ){
        
        DialogoFormularioCliente dialogoCliente =
            new DialogoFormularioCliente( ventanaPadre, TipoDialogo.CREAR_NUEVO_CLIENTE);
        
        ControladorDialogoCliente controlador =
                new ControladorDialogoCliente(
                        dialogoCliente,
                        TipoDialogo.CREAR_NUEVO_CLIENTE,
                        -1,
                        servicioClientes
                );
        
        return controlador.getIdCliente();
    }
    
    
    /*
    ============================================================================
                METODOS PARA CONSULTAR A LOS SERVICIOS
    ============================================================================
    */
    
    
    private ModeloCliente obtenerModeloCliente (int idCliente) throws Exception{
        return servicioClientes.obtenerModeloCliente(idCliente);
    }
    
    private int crearCliente( ModeloCliente cliente ) throws Exception {
        return servicioClientes.crearCliente( cliente, isAdministrador );
    }
    
    private void editarCliente( ModeloCliente cliente ) throws Exception {
        servicioClientes.editarCliente(cliente, isAdministrador);
    }
    
    private void asignarEstadoCliente( int idCliente , boolean habilitado ) throws Exception{
        servicioClientes.asignarEstadoCliente(idCliente, habilitado, isAdministrador);
    }
    
    private boolean isClienteEliminable ( int idCliente ) throws Exception{
        return servicioClientes.isClienteEliminable( idCliente );
    }
    
    private void eliminarCliente ( int idCliente ) throws Exception {
        servicioClientes.eliminarCliente( idCliente, isAdministrador );
    }
    
    
    /*
    ============================================================================
                            METODOS INICIALES
    ============================================================================
    */
    
    private HashMap<String, String> obtenerModeloEnDiccionario(){
    
        HashMap<String, String> diccionarioCliente = new HashMap<>();
        
        diccionarioCliente.put( "nombreNegocio", modeloCliente.getNombreNegocio() );
        diccionarioCliente.put( "correoCliente", modeloCliente.getCorreoCliente() );
        diccionarioCliente.put( "direccionCliente", modeloCliente.getDireccionCliente() );
        diccionarioCliente.put( "primerNombreContacto", modeloCliente.getPrimerNombreContacto() );
        diccionarioCliente.put( "segundoNombreContacto", modeloCliente.getSegundoNombreContacto() );
        diccionarioCliente.put( "primerApellidoContacto", modeloCliente.getPrimerApellidoContacto() );
        diccionarioCliente.put( "segundoApellidoContacto", modeloCliente.getSegundoApellidoContacto() );
        diccionarioCliente.put( "telefonoContacto", modeloCliente.getTelefonoContacto() );
    
        return diccionarioCliente;
        
    }
    
    private void cargarDatosAVista ( HashMap<String, String> cliente ){
        dialogoCliente.setId( idCliente );
        dialogoCliente.asignarDatosEnFormulario( cliente );
        if ( isClienteEliminable ) {
            dialogoCliente.mostrarBtnLinkEliminarCliente();
        }else{
            dialogoCliente.mostrarBtnEditarEstadoCliente();
            dialogoCliente.asignarIntencionBtnEditarEstadoCliente(
                                            modeloCliente.isHabilitado());
        }
    }
    
    
    private void inicializarEventosBotones(){
    
        if( tipoDialogo == TipoDialogo.EDITAR_CLIENTE){
            if( isClienteEliminable )
                dialogoCliente.getBtnLinkEliminarCliente().addActionListener(e -> eliminarCliente() );
            else
                dialogoCliente.getBtnLinkEditarEstadoCliente().addActionListener( e -> conmutarEstadoCliente() );
        }
        
        dialogoCliente.getBtnEnviarFormulario().addActionListener( e -> procesarFormulario() );
    
    }
    
    
    /*
    ============================================================================
                        METODOS OPERACION DE LOS BOTONES
    ============================================================================
    */
    
    private void procesarFormulario(){
        
        // Verificar que los campos esten diligenciados con un formato valido
        if( !dialogoCliente.validarFormulario() ){
            dialogoCliente.mostrarAlertaErrorFormatoCampos();
            return;
        }
        
        //Extraer los datos del formulario
        HashMap<String, String> datosFormulario = dialogoCliente.recolectarDatosFormulario();
        
        /*
        Asignar datos a modelo, 
        clonamos si es para editar con el fin de comprobar si hubieron cambios, 
        pero si es para crear uno nuevo unicamente instanciamos el Modelo
        */
        ModeloCliente clienteAProcesar =   
                (tipoDialogo == TipoDialogo.EDITAR_CLIENTE)
                ? modeloCliente.clonar()
                : new ModeloCliente();
        
        clienteAProcesar = asignarDatosAModelo( clienteAProcesar, datosFormulario);
        
        //Verificar si los modelos son iguales en dado caso que tipoDialogo sea EDITAR
        if( tipoDialogo == TipoDialogo.EDITAR_CLIENTE ){
            if ( modeloCliente.equals( clienteAProcesar )) {
                if ( resultadoEdicion == ResultadoDialogo.ACTUALIZADO) {
                    //Si entra aqui es por que se edito su estado habilitado
                    dialogoCliente.mostrarAlertaExitosa("Cliente editado correctamente");
                    dialogoCliente.dispose();
                }else{
                    dialogoCliente.mostrarAlertaError("No hay cambios para guardar");
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
            case EDITAR_CLIENTE:
                try {
                    editarCliente( clienteAProcesar );
                    dialogoCliente.mostrarAlertaExitosa("Cliente actualizado correctamente");
                    resultadoEdicion = ResultadoDialogo.ACTUALIZADO;
                    dialogoCliente.dispose();
                }catch (ExcepcionValidacionBD e) {
                    mostrarErroresValidacion(  e.getErrores() );
                }catch (Exception e) {
                    dialogoCliente.mostrarAlertaError(e.getMessage());
                }
                break;
            case CREAR_NUEVO_CLIENTE:
                try {
                    idCliente = crearCliente( clienteAProcesar );
                    dialogoCliente.mostrarAlertaExitosa("Cliente creado correctamente");
                    dialogoCliente.dispose();
                }catch (ExcepcionValidacionBD e) {
                    mostrarErroresValidacion(  e.getErrores() );
                }catch (Exception e) {
                    dialogoCliente.mostrarAlertaError(e.getMessage());
                }
                break;
        }
    
    }
    
    private ModeloCliente asignarDatosAModelo( ModeloCliente cliente, HashMap<String, String> datos ){
    
        if( datos.containsKey("nombreNegocio") )
            cliente.setNombreNegocio(datos.get("nombreNegocio"));
        if( datos.containsKey("correoCliente") )
            cliente.setCorreoCliente(datos.get("correoCliente"));
        if( datos.containsKey("direccionCliente") )
            cliente.setDireccionCliente(datos.get("direccionCliente"));
        if( datos.containsKey("primerNombreContacto") )
            cliente.setPrimerNombreContacto(datos.get("primerNombreContacto"));
        if( datos.containsKey("segundoNombreContacto") )
            cliente.setSegundoNombreContacto(datos.get("segundoNombreContacto"));
        if( datos.containsKey("primerApellidoContacto") )
            cliente.setPrimerApellidoContacto(datos.get("primerApellidoContacto"));
        if( datos.containsKey("segundoApellidoContacto") )
            cliente.setSegundoApellidoContacto(datos.get("segundoApellidoContacto"));
        if( datos.containsKey("telefonoContacto") )
            cliente.setTelefonoContacto(datos.get("telefonoContacto"));
 

        return cliente;

    }
    
    
    private void conmutarEstadoCliente(){
        boolean deseaContinuar = dialogoCliente.mostrarAlertaAdvertencia(
            ( modeloCliente.isHabilitado()
            ? "Esta a punto de deshabilitar el cliente y por tanto ya no podra registrar ventas con el."
            : "Esta a punto de habilitar el cliente y ahora lo podra usar en ventas." )
            + "\nEsta seguro?"
        );
        
        if(deseaContinuar){
            try {
                asignarEstadoCliente(idCliente, !modeloCliente.isHabilitado());
                modeloCliente.setHabilitado( !modeloCliente.isHabilitado() );
                dialogoCliente.asignarIntencionBtnEditarEstadoCliente(
                                            modeloCliente.isHabilitado()
                );
                resultadoEdicion = ResultadoDialogo.ACTUALIZADO;
            } catch (Exception e) {
                dialogoCliente.mostrarAlertaError( e.getMessage() );
            }
        }
    
    }
    
    private void eliminarCliente(){
        boolean deseaContinuar = dialogoCliente.mostrarAlertaAdvertencia(
            "Esta a punto de eliminar el cliente, este cambio es irreversible \nEsta seguro?"
        );
        
        if(deseaContinuar){
            try {
                eliminarCliente(idCliente);
                resultadoEdicion = ResultadoDialogo.ELIMINADO;
                dialogoCliente.mostrarAlertaExitosa("Cliente eliminado correctamente");
                dialogoCliente.dispose();
            } catch (Exception e) {
                dialogoCliente.mostrarAlertaError( e.getMessage() );
            }   
        }
    }
    
    private void mostrarErroresValidacion( HashMap<String, String> errores ) {
        dialogoCliente.mostrarErroresValidacionCampos( errores );
        dialogoCliente.mostrarAlertaErroresValidacion(errores);
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
    public int getIdCliente(){
        return idCliente;
    }
    
}

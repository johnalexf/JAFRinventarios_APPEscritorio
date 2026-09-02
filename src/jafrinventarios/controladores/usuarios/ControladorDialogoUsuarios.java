
package jafrinventarios.controladores.usuarios;

import jafrinventarios.controladores.acceso.ControladorContrasena;
import jafrinventarios.controladores.utilidades.ResultadoDialogo;
import jafrinventarios.modelos.ModeloSesionUsuario;
import jafrinventarios.modelos.usuarios.ModeloUsuario;
import jafrinventarios.servicios.acceso.ServicioAutenticacion;
import jafrinventarios.servicios.excepciones.ExcepcionValidacionBD;
import jafrinventarios.servicios.usuarios.ServicioRoles;
import jafrinventarios.servicios.usuarios.ServicioUsuarios;
import jafrinventarios.vistas.usuarios.DialogoFormularioUsuario;
import jafrinventarios.vistas.usuarios.DialogoFormularioUsuario.TipoDialogo;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 *
 * @author JOHN FORERO
 */
public class ControladorDialogoUsuarios {
    
    private DialogoFormularioUsuario dialogoUsuario;
    
    private ServicioUsuarios servicioUsuarios;
    
    private ModeloUsuario modeloUsuario;
    
    //Variable de soporte para personalizar el controlador segun el tipo de dialogo:
    //EDITAR_PERFIL_PROPIO, EDITAR_OTRO_USUARIO y CREAR_NUEVO_USUARIO
    private TipoDialogo tipoDialogo;
    
    private boolean isUsuarioEliminable;
    
    // Variable con el id del usuario a editar, para el caso de CREAR_NUEVO_USUARIO
    // Esta variable sera la respuesta del nuevo usuario.
    private int idUsuario;
    
    // Variable usada para responder segun el resultado de la operacion para
    // las funciones de editar perfil u editar otro usuario
    private ResultadoDialogo resultadoDialogo = ResultadoDialogo.SIN_CAMBIOS;

    
    /*
    ============================================================================
      CONSTRUCTOR PRIVADO PARA EVITAR QUE SE CREE SIN SU DEBIDA CONFIGURACION
    ============================================================================
    */
    private ControladorDialogoUsuarios(
            DialogoFormularioUsuario dialogoUsuario, 
            TipoDialogo tipoDialogo,
            int idUsuario,
            ServicioUsuarios servicioUsuarios){
        
        this.dialogoUsuario = dialogoUsuario;
        this.servicioUsuarios = servicioUsuarios;
       
        this.tipoDialogo = tipoDialogo;
        this.idUsuario = idUsuario;
        
        if(tipoDialogo != TipoDialogo.EDITAR_PERFIL_PROPIO){
             inicializarComboBoxRoles();
        }
        
        if( tipoDialogo == TipoDialogo.EDITAR_OTRO_USUARIO ){
            try {
                isUsuarioEliminable = isUsuarioEliminable( idUsuario );
            } catch (Exception e) {
                dialogoUsuario.mostrarAlertaError(e.getMessage());
                /*
                NOTA: en dado caso que no se pueda conocer si el usuario se puede 
                eliminar, se asigna como falso  isUsuarioEliminable, 
                ya que de esto depende si se le muestra al usuario del programa si puede eliminar
                o deshabilitar el usuario, para evitar una eliminacion equivocada lo dejamos en false
                */
                isUsuarioEliminable = false;
            }    
        }
        
        if(tipoDialogo != TipoDialogo.CREAR_NUEVO_USUARIO){
            try {
                this.modeloUsuario = obtenerModeloUsuario( idUsuario );
            }catch (Exception e) {
                dialogoUsuario.mostrarAlertaError(e.getMessage());
                return;
            }
            
            cargarDatosAVista( obtenerModeloEnDiccionario() );
        }
        
        inicializarEventosBotones();
        
        dialogoUsuario.mostrar();
    }
    
    
    /*
    ============================================================================
     METODOS ESTÁTICAS: Los únicos puntos de acceso para los demás controladores
    ============================================================================
    */
    public static ResultadoDialogo editarPerfil( java.awt.Window ventanaPadre, ServicioUsuarios servicioUsuarios){
        
    
        DialogoFormularioUsuario dialogoUsuario = 
                new DialogoFormularioUsuario( ventanaPadre,
                                              TipoDialogo.EDITAR_PERFIL_PROPIO,
                                              ModeloSesionUsuario.getInstancia().isAdministrador()
                );
        
        ControladorDialogoUsuarios controlador =
                new ControladorDialogoUsuarios( 
                        dialogoUsuario, 
                        TipoDialogo.EDITAR_PERFIL_PROPIO,
                        ModeloSesionUsuario.getInstancia().getIdUsuario(),
                        servicioUsuarios
        );
        
        return controlador.obtenerResultadoDialogo();
        
    }
    
    
    public static ResultadoDialogo editarOtroUsuario( java.awt.Window ventanaPadre, int idUsuario, ServicioUsuarios servicioUsuarios){
        
    
        DialogoFormularioUsuario dialogoUsuario = 
                new DialogoFormularioUsuario( ventanaPadre,
                                              TipoDialogo.EDITAR_OTRO_USUARIO,
                                              true
                );
        
        ControladorDialogoUsuarios controlador =
            new ControladorDialogoUsuarios( dialogoUsuario, 
                                            TipoDialogo.EDITAR_OTRO_USUARIO,
                                            idUsuario,
                                            servicioUsuarios
        );
        
        return controlador.obtenerResultadoDialogo();
        
    }
    
    
    public static int crearUsuario( java.awt.Window ventanaPadre, ServicioUsuarios servicioUsuarios){
    
        DialogoFormularioUsuario dialogoUsuario = 
                new DialogoFormularioUsuario( ventanaPadre,
                                              TipoDialogo.CREAR_NUEVO_USUARIO,
                                              true
                );
        
        ControladorDialogoUsuarios controlador = 
            new ControladorDialogoUsuarios( dialogoUsuario, 
                                            TipoDialogo.CREAR_NUEVO_USUARIO, 
                                            -1,
                                            servicioUsuarios
        );
        
        return controlador.getIdUsuario();
        
    }
        
//==============================================================================
        
    
    /*
    ============================================================================
                METODOS PARA CONSULTAR A LOS SERVICIOS
    ============================================================================
    */
    
    private ModeloUsuario obtenerModeloUsuario( int idUsuario ) throws Exception{
        return servicioUsuarios.obtenerModeloUsuario(idUsuario);
    }
    
    private void editarPerfil( ModeloUsuario usuario ) throws Exception{
         servicioUsuarios.editarPerfil(usuario, ModeloSesionUsuario.getInstancia().isAdministrador() );
    }
    
    private void editarOtroUsuario( ModeloUsuario usuario ) throws Exception{
        servicioUsuarios.editarOtroUsuario(usuario);
    }
    
    private void asignarEstadoUsuario (  int idUsuario , boolean habilitado ) throws Exception{
        servicioUsuarios.asignarEstadoUsuario(idUsuario, habilitado);
    }
    
        
    private int crearUsuario( ModeloUsuario usuario ) throws Exception{
        /*
        Ya que la idea es mostrarle al usuario un modal el cual especifica
        que esta cargando la accion a realizar, en este caso crear un usuario,
        para ello es necesario utilizar variables inmutables es decir final
        dentro de la funcion lambda, por ello se desarrolla el codigo como se
        observa acontinuacion.
        */
        
        // Al recibir usuario como parametro se considera final mientras no se edite,
        //por tanto la funcion lambda lo acepta.
        
        //  Creamos un arreglo de 1 posición (inmutable en su estructura) para atrapar el ID
        final int[] idCapturado = new int[1];
        dialogoUsuario.mostrarAlertaCargando(
                "Creando usuario y enviando correo con credenciales",
                ()-> {
                    // Ejecutamos la función y guardamos el resultado ADENTRO de la caja
                    idCapturado[0] = servicioUsuarios.crearUsuario(usuario);
                }
        );
                    
        // Cuando el modal se cierra (el hilo terminó), sacamos el ID para usarlo    
        return idCapturado[0];
    }
    
    private LinkedHashMap<Integer, String> obtenerDiccionarioRoles() throws Exception{
        return  ServicioRoles.obtenerDiccionarioRoles();
    } 
    
    public boolean isUsuarioEliminable ( int idUsuario ) throws Exception{
        return servicioUsuarios.isUsuarioEliminable( idUsuario );
    }
    
    private void eliminarUsuario( int idUsuario ) throws Exception{
        servicioUsuarios.eliminarUsuario(idUsuario);
    }
    
    
    /*
    ============================================================================
                            METODOS INICIALES
    ============================================================================
    */
    
    private HashMap<String, String> obtenerModeloEnDiccionario(){
    
        HashMap<String, String> datosPerfil = new HashMap<>();
        
        datosPerfil.put("alias",modeloUsuario.getAliasUsuario() );
        datosPerfil.put("rol", String.valueOf(modeloUsuario.getIdRolUsuario() ) );
        datosPerfil.put("primerNombre",modeloUsuario.getPrimerNombreUsuario() );
        datosPerfil.put("segundoNombre",modeloUsuario.getSegundoNombreUsuario() );
        datosPerfil.put("primerApellido",modeloUsuario.getPrimerApellidoUsuario() );
        datosPerfil.put("segundoApellido",modeloUsuario.getSegundoApellidoUsuario() );
        datosPerfil.put("nombreCompleto",modeloUsuario.getNombreCompletoUsuario() );
        datosPerfil.put("telefono",modeloUsuario.getTelefonoUsuario() );
        datosPerfil.put("correo",modeloUsuario.getCorreoUsuario() );
        
        return datosPerfil;
        
    }
    
    
    private void cargarDatosAVista( HashMap<String, String> datosPerfil ){
        
        if(tipoDialogo == TipoDialogo.EDITAR_OTRO_USUARIO){
            dialogoUsuario.setId( Integer.toString(idUsuario) );
            if( isUsuarioEliminable ){
                dialogoUsuario.mostrarBtnLinkEliminarUsuario();
            }else{
                dialogoUsuario.mostrarBtnEditarEstadoUsuario();
                dialogoUsuario.asignarIntencionBtnEditarEstadoUsuario( modeloUsuario.isHabilitado() );
            }       
        }

        dialogoUsuario.asignarDatosEnFormulario(datosPerfil);
    
    }
    
    
    private void inicializarComboBoxRoles(){
        try {
            LinkedHashMap<Integer, String> diccionarioRoles = obtenerDiccionarioRoles();
            dialogoUsuario.inicializarComboBoxRoles( diccionarioRoles );
        } catch (Exception e) {
            dialogoUsuario.mostrarAlertaError(e.getMessage());
        }

    }
    
    
     // Activar eventos de escucha de clic en los botones
    private void inicializarEventosBotones() {
        dialogoUsuario.getBtnEnviarFormulario().addActionListener(e -> procesarFormulario());
        
        if ( tipoDialogo == TipoDialogo.EDITAR_PERFIL_PROPIO ){
            dialogoUsuario.getBtnLinkEditarContrasena().addActionListener( e -> mostrarDialogoEditarContrasena() );
        }
        
        if ( tipoDialogo == TipoDialogo.EDITAR_OTRO_USUARIO ){
            if( isUsuarioEliminable ){
               dialogoUsuario.getBtnLinkEliminarUsuario().addActionListener( e -> eliminarUsuario() );
            }else{
               dialogoUsuario.getBtnLinkEditarEstadoUsuario().addActionListener( e -> asignarEstadoHabilitadoUsuario() ); 
            }
            
        }
        
    }
    
    
    /*
    ============================================================================
                        METODOS OPERACION DE LOS BOTONES
    ============================================================================
    */
    
    
    private void procesarFormulario(){
        
        // Validaciones de los campos si corresponden a su tipo
        if( !dialogoUsuario.validarFormulario() ){
            dialogoUsuario.mostrarAlertaErrorFormatoCampos();
            return;
        }
        
        // Extracción y construcción del Modelo
        HashMap<String, String> datosFormulario = dialogoUsuario.recolectarDatosFormulario();

        ModeloUsuario usuarioAProcesar = 
                ( tipoDialogo != TipoDialogo.CREAR_NUEVO_USUARIO ) 
                ? modeloUsuario.clonar()
                : new ModeloUsuario();

        usuarioAProcesar = asignarDatosAModeloUsuario( usuarioAProcesar, datosFormulario );
        
        // Auditoría de Cambios si es editar usuario o perfil
        if( tipoDialogo != TipoDialogo.CREAR_NUEVO_USUARIO ){
            if( modeloUsuario.equals(usuarioAProcesar) ){
                //Este if es para cuando solo se deshabilito el usuario sin editar ningun campo
                if( obtenerResultadoDialogo() == ResultadoDialogo.ACTUALIZADO ){
                    dialogoUsuario.mostrarAlertaExitosa();
                    dialogoUsuario.dispose();
                }
                else{
                    dialogoUsuario.mostrarAlertaError("No hay cambios para guardar");
                    return;
                }
            }
        }
               
        
        /*
        ========================================================================
          LÓGICA DE BASE DE DATOS (Solo llegamos aquí si hay que guardar algo)
        ========================================================================
        */
           
        switch(tipoDialogo){
            case EDITAR_PERFIL_PROPIO:
                
                try {
                    editarPerfil(usuarioAProcesar);
                    resultadoDialogo = ResultadoDialogo.ACTUALIZADO;
                }catch( ExcepcionValidacionBD e ){ 
                    mostrarErroresValidacion(e.getErrores());
                    return;
                }catch (Exception e) {
                    dialogoUsuario.mostrarAlertaError(e.getMessage());
                    return;
                }
                
                break;
            case EDITAR_OTRO_USUARIO:
                                
                try {
                    if(usuarioAProcesar.getIdRolUsuario() != modeloUsuario.getIdRolUsuario() ){
                        boolean deseaContinuar = 
                        dialogoUsuario.mostrarAlertaAdvertencia(
                                "Editar el rol del usuario implica quitarle o darle permisos: \n"
                                + "Rol administrador: Tiene todos los permisos, para crear, editar y hasta de eliminar en todos los modulos \n"
                                + "Rol vendedor: Tiene permisos restringidos, solo puede crear y tiene acceso limitado a los modulos\n\n"
                                + "Esta seguro de realizar este cambio\n"
                        );
                        
                        if(!deseaContinuar)
                            return;
                    
                    }
                    editarOtroUsuario(usuarioAProcesar);
                    resultadoDialogo = ResultadoDialogo.ACTUALIZADO;
                }catch( ExcepcionValidacionBD e ){ 
                    mostrarErroresValidacion(e.getErrores());
                    return;
                }catch (Exception e) {
                    dialogoUsuario.mostrarAlertaError(e.getMessage());
                    return;
                }
                
                break;
            case CREAR_NUEVO_USUARIO:
                
                try {
                    idUsuario = crearUsuario(usuarioAProcesar);
                }catch( ExcepcionValidacionBD e ){ 
                    mostrarErroresValidacion(e.getErrores());
                    return;
                }catch (Exception e) {
                    dialogoUsuario.mostrarAlertaError(e.getMessage());
                    return;
                }
                
                break;
            
        }
        
        //Si no hubo ningun error se considera que fue una operacion terminada
        // y por ende se cierra el modal.
        dialogoUsuario.mostrarAlertaExitosa();
        dialogoUsuario.dispose();

    }
    
    
    private ModeloUsuario asignarDatosAModeloUsuario( ModeloUsuario usuario, HashMap<String, String> datosFormulario ){
    
        if( datosFormulario.containsKey("alias") )
            usuario.setAliasUsuario( datosFormulario.get("alias") );
        
        if( datosFormulario.containsKey("rol") )
            usuario.setIdRolUsuario(  Integer.parseInt( datosFormulario.get("rol") ) );
        
        if( datosFormulario.containsKey("primerNombre") )
            usuario.setPrimerNombreUsuario(datosFormulario.get("primerNombre") );
        
        if( datosFormulario.containsKey("segundoNombre") )
            usuario.setSegundoNombreUsuario( datosFormulario.get("segundoNombre") );
        
        if( datosFormulario.containsKey("primerApellido") )
            usuario.setPrimerApellidoUsuario(datosFormulario.get("primerApellido") );
        
        if( datosFormulario.containsKey("segundoApellido") )
            usuario.setSegundoApellidoUsuario(datosFormulario.get("segundoApellido") );
        
        if( datosFormulario.containsKey("telefono") )
            usuario.setTelefonoUsuario(datosFormulario.get("telefono") );
        
        if( datosFormulario.containsKey("correo") )
            usuario.setCorreoUsuario(datosFormulario.get("correo") );
        
        return usuario;
    }
    
    
    private void mostrarDialogoEditarContrasena(){
        ControladorContrasena.iniciarCambio( dialogoUsuario, new ServicioAutenticacion() );
    }
    
    
    private void asignarEstadoHabilitadoUsuario(){
    
        boolean deseaContinuar = 
                dialogoUsuario.mostrarAlertaAdvertencia(
                   modeloUsuario.isHabilitado() ? 
                   "Esta a punto de deshabilitar al usuario y por tanto este ya no podra iniciar sesion, sin embargo sus transacciones siguen almacenadas":
                   "Esta a punto de habilitar al usuario y por tanto este podra iniciar sesion."
                );

        if( !deseaContinuar )  return;
        
        try {
            asignarEstadoUsuario( idUsuario, !modeloUsuario.isHabilitado() );
            modeloUsuario.setHabilitado(  !modeloUsuario.isHabilitado()  );
            dialogoUsuario.asignarIntencionBtnEditarEstadoUsuario( modeloUsuario.isHabilitado() );
            resultadoDialogo = ResultadoDialogo.ACTUALIZADO;
        } catch (Exception e) {
            dialogoUsuario.mostrarAlertaError(e.getMessage());
        }


        
    }
    
    
    private void eliminarUsuario(){
     
        boolean deseaContinuar = 
                dialogoUsuario.mostrarAlertaAdvertencia(
                    "Esta a punto de eliminar el usuario, este cambio es irreversible \nEsta seguro?"
                );
        
        if( deseaContinuar ){
            try {
                eliminarUsuario( idUsuario );
                resultadoDialogo = ResultadoDialogo.ELIMINADO;
                dialogoUsuario.mostrarAlertaExitosa( " Usuario eliminado correctamente ");
                dialogoUsuario.dispose();
            } catch (Exception e) {
                dialogoUsuario.mostrarAlertaError( e.getMessage() );
            }
        }
    }
    
    
    private void mostrarErroresValidacion( HashMap<String, String> errores){
        dialogoUsuario.mostrarErroresValidacionCampos( errores );
        dialogoUsuario.mostrarAlertaErroresValidacion(errores);
    }
    
    
    /*
    ============================================================================
                 METODOS RESPUESTA DESPUES DE CIERRE DEL DIALOGO
    ============================================================================
    */
    
    /* 
    Funcion para retornar true en dado caso que se haya realizado 
    cualquier cambio a cualquier usuario
    */
    public ResultadoDialogo obtenerResultadoDialogo() {
        return resultadoDialogo;
    }
    
    //Funcion para retornar el id del usuario creado
    public int getIdUsuario(){
        return idUsuario;
    }
    
}

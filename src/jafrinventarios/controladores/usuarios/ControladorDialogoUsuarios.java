
package jafrinventarios.controladores.usuarios;

import jafrinventarios.controladores.acceso.ControladorContrasena;
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
    
    private boolean tieneRegistrosAsociados;
    
    // Variable con el id del usuario a editar, para el caso de CREAR_NUEVO_USUARIO
    // Esta variable sera la respuesta del nuevo usuario.
    private int idUsuario;
    
    // Variable usada para responder segun el resultado de la operacion para
    // las funciones de editar perfil u otro usuario
    private boolean operacionExitosa = false;

    
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
            tieneRegistrosAsociados = tieneRegistrosAsociados( idUsuario );
        }
        
        if(tipoDialogo != TipoDialogo.CREAR_NUEVO_USUARIO){
            try {
                this.modeloUsuario = obtenerModeloUsuario( idUsuario );
            }catch (Exception e) {
                //dialogoUsuario.mostrarAlertaError(e.getMessage());
                return;
            }
            
            cargarDatosAVista( empaquetarDatosUsuarioEnDiccionario() );
        }
        
        inicializarEventosBotones();
        
        dialogoUsuario.mostrar();
    }
    
    
    /*
    ============================================================================
     METODOS ESTÁTICAS: Los únicos puntos de acceso para los demás controladores
    ============================================================================
    */
    public static boolean editarPerfil( java.awt.Window ventanaPadre, ServicioUsuarios servicioUsuarios){
        
    
        DialogoFormularioUsuario dialogoUsuario = 
                new DialogoFormularioUsuario( ventanaPadre,
                                              TipoDialogo.EDITAR_PERFIL_PROPIO,
                                              ModeloSesionUsuario.getInstancia().esAdministrador()
                );
        
        ControladorDialogoUsuarios controlador =
                new ControladorDialogoUsuarios( 
                        dialogoUsuario, 
                        TipoDialogo.EDITAR_PERFIL_PROPIO,
                        ModeloSesionUsuario.getInstancia().getIdUsuario(),
                        servicioUsuarios
        );
        
        return controlador.isOperacionExitosa();
        
    }
    
    
    public static boolean editarOtroUsuario( java.awt.Window ventanaPadre, int idUsuario, ServicioUsuarios servicioUsuarios){
        
    
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
        
        return controlador.isOperacionExitosa();
        
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
         servicioUsuarios.editarPerfil(usuario, ModeloSesionUsuario.getInstancia().esAdministrador() );
    }
    
    private void editarOtroUsuario( ModeloUsuario usuario ) throws Exception{
        servicioUsuarios.editarOtroUsuario(usuario);
    }
    
    private boolean conmutarEstadoUsuario (  int idUsuario  ){
        return servicioUsuarios.conmutarEstadoUsuario(idUsuario);
    }
    
    private boolean esUsuarioHabilitado( int idUsuario ){
        return servicioUsuarios.esUsuarioHabilitado(idUsuario);
    }
        
    private int crearUsuario( ModeloUsuario usuario ) throws Exception{
        return servicioUsuarios.crearUsuario(usuario);
    }
    
    private LinkedHashMap<Integer, String> obtenerDiccionarioRoles(){
        return  ServicioRoles.obtenerDiccionarioRoles();
    } 
    
    public boolean tieneRegistrosAsociados ( int idUsuario ){
        return servicioUsuarios.tieneRegistrosAsociados( idUsuario );
    }
    
    private boolean eliminarUsuario( int idUsuario ){
        return servicioUsuarios.eliminarUsuario(idUsuario);
    }
    
    
    /*
    ============================================================================
                            METODOS INICIALES
    ============================================================================
    */
    
    private HashMap<String, String> empaquetarDatosUsuarioEnDiccionario(){
    
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
            if( tieneRegistrosAsociados ){
                dialogoUsuario.mostrarBtnEditarEstadoUsuario();
                dialogoUsuario.asignarIntencionBtnEditarEstadoUsuario( modeloUsuario.estaHabilitado() );
            }else{
                dialogoUsuario.mostrarBtnLinkEliminarUsuario();
            }       
        }

        dialogoUsuario.asignarDatosEnFormulario(datosPerfil);
    
    }
    
    
    private void inicializarComboBoxRoles(){
        LinkedHashMap<Integer, String> diccionarioRoles = obtenerDiccionarioRoles();
        dialogoUsuario.inicializarComboBoxRoles( diccionarioRoles );
    }
    
    
     // Activar eventos de escucha de clic en los botones
    private void inicializarEventosBotones() {
        dialogoUsuario.getBtnEnviarFormulario().addActionListener(e -> procesarFormulario());
        
        if ( tipoDialogo == TipoDialogo.EDITAR_PERFIL_PROPIO ){
            dialogoUsuario.getBtnLinkEditarContrasena().addActionListener( e -> mostrarDialogoEditarContrasena() );
        }
        
        if ( tipoDialogo == TipoDialogo.EDITAR_OTRO_USUARIO ){
            if( tieneRegistrosAsociados ){
               dialogoUsuario.getBtnLinkEditarEstadoUsuario().addActionListener( e -> conmutarEstadoUsuario() ); 
            }else{
               dialogoUsuario.getBtnLinkEliminarUsuario().addActionListener( e -> eliminarUsuario() );
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
        //TODO pendiente eliminar luego de que se guarde en la base de datos
        imprimirEnConsolaFormulario( datosFormulario );

        ModeloUsuario usuarioAProcesar = 
                ( tipoDialogo != TipoDialogo.CREAR_NUEVO_USUARIO ) 
                ? modeloUsuario.clonar()
                : new ModeloUsuario();

        usuarioAProcesar = asignarDatosAModeloUsuario( usuarioAProcesar, datosFormulario );
         //TODO pendiente eliminar luego de que se guarde en la base de datos
        System.out.println( modeloUsuario.toString() );
        System.out.println( usuarioAProcesar.toString());
        
        // Auditoría de Cambios si es editar usuario o perfil
        if( tipoDialogo != TipoDialogo.CREAR_NUEVO_USUARIO ){
            if( modeloUsuario.equals(usuarioAProcesar) ){
                
                //Si operacionExitosa es true significa que se cambio el estado del usuario
                if( operacionExitosa ){
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
                }catch( ExcepcionValidacionBD e ){ 
                    //dialogoUsuario.mostrarErroresValidacionCampos( e.getErrores() );
                    return;
                }catch (Exception e) {
                    //dialogoUsuario.mostrarAlertaError(e.getMessage());
                    return;
                }
                
                break;
            case EDITAR_OTRO_USUARIO:
                                
                try {
                    editarOtroUsuario(usuarioAProcesar);
                }catch( ExcepcionValidacionBD e ){ 
                    //dialogoUsuario.mostrarErroresValidacionCampos( e.getErrores() );
                    return;
                }catch (Exception e) {
                    //dialogoUsuario.mostrarAlertaError(e.getMessage());
                    return;
                }
                
                break;
            case CREAR_NUEVO_USUARIO:
                
                try {
                    idUsuario = crearUsuario(usuarioAProcesar);
                }catch( ExcepcionValidacionBD e ){ 
                    //dialogoUsuario.mostrarErroresValidacionCampos( e.getErrores() );
                    return;
                }catch (Exception e) {
                    //dialogoUsuario.mostrarAlertaError(e.getMessage());
                    return;
                }
                
                break;
        }
        
        //Si no hubo ningun error se considera una operacion exitosa
        operacionExitosa = true;
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
    
    //TODO eliminar despues de conectar la base de datos.
    // Funcion para verificar la recoleccion de datos del formulario
    private void imprimirEnConsolaFormulario( HashMap<String, String> datosFormulario ){
        datosFormulario.forEach(    (clave, valor) ->
            System.out.println(clave + " -> " + valor)
        );
    }
    
    
    private void mostrarDialogoEditarContrasena(){
        ControladorContrasena.iniciarCambio( dialogoUsuario, new ServicioAutenticacion() );
    }
    
    
    private void conmutarEstadoUsuario(){
    
        boolean deseaContinuar = 
                dialogoUsuario.mostrarAlertaAdvertencia(
                   modeloUsuario.estaHabilitado() ? 
                   "Esta a punto de deshabilitar al usuario y por tanto este ya no podra iniciar sesion, sin embargo sus transacciones siguen almacenadas":
                   "Esta a punto de habilitar al usuario y por tanto este podra iniciar sesion."
                );

        if( !deseaContinuar )  return;

        boolean seConmutoEstado = conmutarEstadoUsuario( idUsuario );
        if( seConmutoEstado ){
            modeloUsuario.setHabilitado(  esUsuarioHabilitado( idUsuario )  );
            dialogoUsuario.asignarIntencionBtnEditarEstadoUsuario( modeloUsuario.estaHabilitado() );
            operacionExitosa = true;
        }else{
            System.out.println("Error en la base de datos");
        }
        
    }
    
    
    private void eliminarUsuario(){
     
        boolean deseaContinuar = 
                dialogoUsuario.mostrarAlertaAdvertencia(
                    "Esta a punto de eliminar el usuario, este cambio es irreversible \nEsta seguro?"
                );
        
        if( deseaContinuar ){
            if( eliminarUsuario(idUsuario) ){
                dialogoUsuario.mostrarAlertaExitosa( " Usuario eliminado correctamente ");
                dialogoUsuario.dispose();
            }else{
                dialogoUsuario.mostrarAlertaError( "Error al tratar de eliminar el usuario" );
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
    cualquier cambio a cualquier usuario
    */
    public boolean isOperacionExitosa() {
        return operacionExitosa;
    }
    
    //Funcion para retornar el id del usuario creado
    public int getIdUsuario(){
        return idUsuario;
    }
    
}

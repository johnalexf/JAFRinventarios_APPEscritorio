/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jafrinventarios.controladores.usuarios;

import jafrinventarios.controladores.acceso.ControladorContrasena;
import jafrinventarios.modelos.ModeloSesionUsuario;
import jafrinventarios.modelos.usuarios.ModeloUsuario;
import jafrinventarios.servicios.usuarios.ServicioRoles;
import jafrinventarios.servicios.usuarios.ServicioUsuarios;
import jafrinventarios.vistas.usuarios.DialogoFormularioUsuario;
import jafrinventarios.vistas.utilidades.dialogos.DialogoMensajePersonalizado;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JFrame;

/**
 *
 * @author JOHN FORERO
 */
public class ControladorDialogoUsuarios {
    
    private DialogoFormularioUsuario dialogoUsuario;
    
    private ServicioUsuarios servicioUsuarios;
    
    //Variable de soporte para personalizar el controlador segun el tipo de dialogo:
    //EDITAR_PERFIL_PROPIO, EDITAR_OTRO_USUARIO y CREAR_NUEVO_USUARIO
    private DialogoFormularioUsuario.TipoDialogo tipoDialogo;
    
    private boolean esAdministrador;
    // Variable con el id del usuario a editar, para el caso de CREAR_NUEVO_USUARIO
    // Esta variable sera la respuesta del nuevo usuario.
    private int idUsuario;
    
    //Variable para personalizar el mensaje exitoso si es para crear o editar
    private String mensajeExitoso;
    
    //TODO esta variable es para pruebas, cuando se conecte la base de datos
    //se hara la consulta y se eliminara esta variable.
    private boolean esUsuarioHabilitado = true;
    
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
            DialogoFormularioUsuario.TipoDialogo tipoDialogo,
            boolean esAdminsitrador,
            int idUsuario,
            ServicioUsuarios servicioUsuarios){
        
        this.dialogoUsuario = dialogoUsuario;
        this.servicioUsuarios = servicioUsuarios;
        
        //Para pruebas se dejara esAdministrador como parametro del contralador
        //this.esAdminsitrador = ModeloSesionUsuario.getInstancia().esAdministrador();
        this.esAdministrador = esAdminsitrador;
        this.tipoDialogo = tipoDialogo;
        this.idUsuario = idUsuario;
        
        if(tipoDialogo != DialogoFormularioUsuario.TipoDialogo.EDITAR_PERFIL_PROPIO){
             inicializarComboBoxRoles();
        }
        
        if(tipoDialogo != DialogoFormularioUsuario.TipoDialogo.CREAR_NUEVO_USUARIO){
            cargarDatosAVista( obtenerDatosPerfil(), tipoDialogo);
            mensajeExitoso = "El usuario se ha actualizado correctamente";
        }else{
            mensajeExitoso = "Usuario creado correctamente \n La contraseña se le envia al usuario por correo"; 
        }
        
        inicializarEventosBotones();
        
        dialogoUsuario.hacerVisibleDialogo();
    }
    
    
    /*
    ============================================================================
     METODOS ESTÁTICAS: Los únicos puntos de acceso para los demás controladores
    ============================================================================
    */
    public static boolean editarPerfil(JFrame ventanaPadre, boolean esAdministrador, ServicioUsuarios servicioUsuarios){
        
        //Para pruebas se dejara esAdministrador como parametro de la funcion editarUsuario
        //boolean esAdminsitrador = ModeloSesionUsuario.getInstancia().esAdministrador();
    
        DialogoFormularioUsuario dialogoUsuario = 
                new DialogoFormularioUsuario( ventanaPadre,
                                              DialogoFormularioUsuario.TipoDialogo.EDITAR_PERFIL_PROPIO,
                                              esAdministrador
                );
        
        ControladorDialogoUsuarios controlador =
                new ControladorDialogoUsuarios( 
                        dialogoUsuario, 
                        DialogoFormularioUsuario.TipoDialogo.EDITAR_PERFIL_PROPIO, 
                        esAdministrador,
                        ModeloSesionUsuario.getInstancia().getIdUsuario(),
                        servicioUsuarios
        );
        
        return controlador.isOperacionExitosa();
        
    }
    
    
    public static boolean editarOtroUsuario(JFrame ventanaPadre, int idUsuario, ServicioUsuarios servicioUsuarios){
        
        //Para pruebas se dejara esAdministrador como parametro de la funcion editarUsuario
        //boolean esAdminsitrador = ModeloSesionUsuario.getInstancia().esAdministrador();
    
        DialogoFormularioUsuario dialogoUsuario = 
                new DialogoFormularioUsuario( ventanaPadre,
                                              DialogoFormularioUsuario.TipoDialogo.EDITAR_OTRO_USUARIO,
                                              true
                );
        
        ControladorDialogoUsuarios controlador =
            new ControladorDialogoUsuarios( dialogoUsuario, 
                                            DialogoFormularioUsuario.TipoDialogo.EDITAR_OTRO_USUARIO, 
                                            true,
                                            idUsuario,
                                            servicioUsuarios
        );
        
        return controlador.isOperacionExitosa();
        
    }
    
    
    public static int crearUsuario(JFrame ventanaPadre, ServicioUsuarios servicioUsuarios){
        
        //Para pruebas se dejara esAdministrador como parametro de la funcion editarUsuario
        //boolean esAdminsitrador = ModeloSesionUsuario.getInstancia().esAdministrador();
    
        DialogoFormularioUsuario dialogoUsuario = 
                new DialogoFormularioUsuario( ventanaPadre,
                                              DialogoFormularioUsuario.TipoDialogo.CREAR_NUEVO_USUARIO,
                                              true
                );
        
        ControladorDialogoUsuarios controlador = 
            new ControladorDialogoUsuarios( dialogoUsuario, 
                                            DialogoFormularioUsuario.TipoDialogo.CREAR_NUEVO_USUARIO, 
                                            true,
                                            -1,
                                            servicioUsuarios
        );
        
        return controlador.getIdUsuario();
        
    }
        
//==============================================================================
        
    
    /*
    ============================================================================
                METODOS PARA CONSULTAR AL SERVICIO
    ============================================================================
    */
    
    private ModeloUsuario obtenerDatosUsuario( int idUsuario ){
        return servicioUsuarios.obtenerDatosUsuario(idUsuario);
    }
    
    private boolean editarPerfil( ModeloUsuario usuario ){
        return servicioUsuarios.editarPerfil(usuario);
    }
    
    private boolean editarOtroUsuario( ModeloUsuario usuario ){
        return servicioUsuarios.editarOtroUsuario(usuario);
    }
    
    private boolean conmutarEstadoUsuario (  int idUsuario  ){
        return servicioUsuarios.conmutarEstadoUsuario(idUsuario);
    }
    
    private boolean esUsuarioHabilitado( int idUsuario ){
        return servicioUsuarios.esUsuarioHabilitado(idUsuario);
    }
        
    private boolean crearUsuario( ModeloUsuario usuario ){
        return servicioUsuarios.crearUsuario(usuario);
    }
    
    private LinkedHashMap<Integer, String> obtenerDiccionarioRoles(){
        return  ServicioRoles.obtenerDiccionarioRoles();
    } 
    
    
    /*
    ============================================================================
                            METODOS INICIALES
    ============================================================================
    */
    
    private HashMap<String, String> obtenerDatosPerfil(){
    
        ModeloUsuario usuario = obtenerDatosUsuario( idUsuario );
        
        HashMap<String, String> datosPerfil = new HashMap<>();
        
        datosPerfil.put("alias", usuario.getAliasUsuario() );
        datosPerfil.put("rol", String.valueOf( usuario.getIdRolUsuario() ) );
        datosPerfil.put("primerNombre", usuario.getPrimerNombreUsuario() );
        datosPerfil.put("segundoNombre", usuario.getSegundoNombreUsuario() );
        datosPerfil.put("primerApellido", usuario.getPrimerApellidoUsuario() );
        datosPerfil.put("segundoApellido", usuario.getSegundoApellidoUsuario() );
        datosPerfil.put("nombreCompleto", usuario.getNombreCompletoUsuario() );
        datosPerfil.put("telefono", usuario.getTelefonoUsuario() );
        datosPerfil.put("correo", usuario.getCorreoUsuario() );
        
        esUsuarioHabilitado = usuario.estaHabilitado();
        
        return datosPerfil;
        
    }
    
    
    private void cargarDatosAVista(HashMap<String, String> datosPerfil, DialogoFormularioUsuario.TipoDialogo tipoDialogo){
        
        if(tipoDialogo == DialogoFormularioUsuario.TipoDialogo.EDITAR_OTRO_USUARIO){
            dialogoUsuario.setId( Integer.toString(idUsuario) );
            dialogoUsuario.asignarIntencionBtnEditarEstadoUsuario(esUsuarioHabilitado);
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
        
        if ( tipoDialogo == DialogoFormularioUsuario.TipoDialogo.EDITAR_PERFIL_PROPIO ){
            dialogoUsuario.getBtnLinkEditarContrasena().addActionListener( e -> mostrarDialogoEditarContrasena() );
        }
        
        if ( tipoDialogo == DialogoFormularioUsuario.TipoDialogo.EDITAR_OTRO_USUARIO ){
            dialogoUsuario.getBtnLinkEditarEstadoUsuario().addActionListener( e -> conmutarEstadoUsuario() );
        }
        
    }
    
    
    /*
    ============================================================================
                        METODOS OPERACION DE LOS BOTONES
    ============================================================================
    */
    
    
    private void procesarFormulario(){
        
        if( !dialogoUsuario.validarFormulario() ){
            System.out.println("Formulario no valido");
            DialogoMensajePersonalizado.mostrarErrorFormatoCampos(dialogoUsuario);
            return;
        }
        
        // Si la validación de campos pasa, pedimos los datos limpios a la vista
        HashMap<String, String> datosFormulario = dialogoUsuario.recolectarDatosFormulario();
        imprimirEnConsolaFormulario( datosFormulario );

        // TODO: conexion al modelo para que guarde los datos y responda
        switch(tipoDialogo){
            case EDITAR_PERFIL_PROPIO:
                //TODO: Consulta para editar perfil propio
                break;
            case EDITAR_OTRO_USUARIO:
                //TODO: Consulta para editar otro usuario
                break;
            case CREAR_NUEVO_USUARIO:
                //TODO: Consulta para crear nuevo usuario
                // La contraseña se envia por correo al nuevo usuario
                break;
        }
        
        boolean respuestaBD = true;

        if(respuestaBD){
            
            DialogoMensajePersonalizado.mostrarExito(
                    dialogoUsuario, 
                    "Operacion Exitosa", 
                    mensajeExitoso
            );
            operacionExitosa = true;
            //TODO Cuando se crea un nuevo usuario se debe realizar el codigo
            // para obtener el id del usuario creado
            dialogoUsuario.dispose();
            
        }else{

            //codigo simulacion de respuesta de la base de datos
            dialogoUsuario.mostrarErrorRespuestaBDEnFormulario(
               new HashMap<>(
                    Map.of(
                        "alias", "El alias ya existe",
                        "contrasena", "Contraseña erronea"
                    )
               )
            );

        }

    }
    
    
    // Funcion para verificar la recoleccion de datos del formulario
    private void imprimirEnConsolaFormulario( HashMap<String, String> datosFormulario ){
        datosFormulario.forEach(    (clave, valor) ->
            System.out.println(clave + " -> " + valor)
        );
    }
    
    
    private void mostrarDialogoEditarContrasena(){
        ControladorContrasena.iniciarCambio(dialogoUsuario);
    }
    
    
    private void conmutarEstadoUsuario(){
    
        /*
        TODO Al tener la conexion con la base de datos, se debe crear una 
        funcion que permita conmutar su estado entre habilitado o inhabilitado
        De igual manera se realizara por parte de la vista la adecuacion para intercambiar
        el boton de deshabilitar por habilitar.
        */
        
     if( 
            !DialogoMensajePersonalizado.mostrarAdvertenciaConRespuesta(
                dialogoUsuario,
                "Advertencia", 
                esUsuarioHabilitado ? 
                "Esta a punto de deshabilitar al usuario y por tanto este ya no podra iniciar sesion, sin embargo sus transacciones siguen almacenadas":
                "Esta a punto de habilitar al usuario y por tanto este podra iniciar sesion."
             )
      ){
         return;
      }
        
        //TODO conexion con la base de datos para que conmute al usuario
        
        //TODO consulta del nuevo estado del usuario
        // simulacion de intercambio de estado
        operacionExitosa = true;
        esUsuarioHabilitado = !esUsuarioHabilitado;
        dialogoUsuario.asignarIntencionBtnEditarEstadoUsuario(esUsuarioHabilitado);
        
    }
    
    
    
    /*
    ============================================================================
                        METODOS RESPUESTA DE OPERACION
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

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
    
    private ModeloUsuario modeloUsuario;
    
    //Variable de soporte para personalizar el controlador segun el tipo de dialogo:
    //EDITAR_PERFIL_PROPIO, EDITAR_OTRO_USUARIO y CREAR_NUEVO_USUARIO
    private DialogoFormularioUsuario.TipoDialogo tipoDialogo;
    
    private boolean esAdministrador;
    
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
            this.modeloUsuario = obtenerModeloUsuario( idUsuario );
            cargarDatosAVista( empaquetarDatosUsuarioEnDiccionario() );
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
                METODOS PARA CONSULTAR A LOS SERVICIOS
    ============================================================================
    */
    
    private ModeloUsuario obtenerModeloUsuario( int idUsuario ){
        return servicioUsuarios.obtenerModeloUsuario(idUsuario);
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
        
    private int crearUsuario( ModeloUsuario usuario ){
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
        
        if(tipoDialogo == DialogoFormularioUsuario.TipoDialogo.EDITAR_OTRO_USUARIO){
            dialogoUsuario.setId( Integer.toString(idUsuario) );
            dialogoUsuario.asignarIntencionBtnEditarEstadoUsuario( modeloUsuario.estaHabilitado() );
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
        
        // Validaciones de los campos si corresponden a su tipo
        if( !dialogoUsuario.validarFormulario() ){
            System.out.println("Formulario no valido");
            DialogoMensajePersonalizado.mostrarErrorFormatoCampos(dialogoUsuario);
            return;
        }
        
        // Extracción y construcción del Modelo
        HashMap<String, String> datosFormulario = dialogoUsuario.recolectarDatosFormulario();
        imprimirEnConsolaFormulario( datosFormulario );

        ModeloUsuario usuarioAProcesar = 
                ( tipoDialogo != DialogoFormularioUsuario.TipoDialogo.CREAR_NUEVO_USUARIO ) 
                ? modeloUsuario.clonar()
                : new ModeloUsuario();

        usuarioAProcesar = asignarDatosAModeloUsuario( usuarioAProcesar, datosFormulario );
        System.out.println( modeloUsuario.toString() );
        System.out.println( usuarioAProcesar.toString());
        
        // Auditoría de Cambios si es editar usuario o perfil
        if( tipoDialogo != DialogoFormularioUsuario.TipoDialogo.CREAR_NUEVO_USUARIO ){
            if( modeloUsuario.equals(usuarioAProcesar) ){
                
                //Si operacionExitosa es true significa que se cambio el estado del usuario
                if( operacionExitosa ) procesarExitoYCierre();
                else{
                    mostrarError("No hay cambios para guardar");
                    return;
                }
            }
        }
               
        
        /*
        ========================================================================
          LÓGICA DE BASE DE DATOS (Solo llegamos aquí si hay que guardar algo)
        ========================================================================
        */
        
        boolean seGuardoUsuario = false;
        
        switch(tipoDialogo){
            case EDITAR_PERFIL_PROPIO:
                seGuardoUsuario = editarPerfil(usuarioAProcesar);
                break;
            case EDITAR_OTRO_USUARIO:
                seGuardoUsuario = editarOtroUsuario(usuarioAProcesar);
                break;
            case CREAR_NUEVO_USUARIO:
                idUsuario = crearUsuario(usuarioAProcesar);
                seGuardoUsuario = (idUsuario != -1); 
                break;
        }
        
        // Manejo de la respuesta
        if(seGuardoUsuario){
            operacionExitosa = true;
            procesarExitoYCierre();
        } else {
            // Simulación de errores (Más adelante esto vendrá del ServicioUsuarios)
            mostrarError("Error en la base de datos");
            dialogoUsuario.mostrarErrorRespuestaBDEnFormulario(
               new HashMap<>(Map.of("alias", "El alias ya existe"))
            );
        }

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
    
        boolean deseaContinuar = 
           DialogoMensajePersonalizado.mostrarAdvertenciaConRespuesta(
                   dialogoUsuario,
                   "Advertencia", 
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
    
    
    private void procesarExitoYCierre(){
        
        String mensajeExitoso = (tipoDialogo != DialogoFormularioUsuario.TipoDialogo.CREAR_NUEVO_USUARIO)
                ?  "El usuario se ha actualizado correctamente"
                :   "Usuario creado correctamente \n La contraseña se le envia al usuario por correo"; 
        
        
        DialogoMensajePersonalizado.mostrarExito(
                    dialogoUsuario, 
                    "Operacion Exitosa", 
                    mensajeExitoso
            );
    
        dialogoUsuario.dispose();
    }
    
    
    private void mostrarError( String mensaje ){
        DialogoMensajePersonalizado.mostrarError(dialogoUsuario, "Error", mensaje );
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

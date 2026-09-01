
package jafrinventarios.controladores.acceso;

import jafrinventarios.modelos.ModeloSesionUsuario;
import jafrinventarios.servicios.acceso.ServicioAutenticacion;
import jafrinventarios.servicios.acceso.ServicioSeguridad;
import jafrinventarios.servicios.correo.ServicioCorreos;
import jafrinventarios.servicios.excepciones.ExcepcionValidacionBD;
import jafrinventarios.vistas.acceso.contrasena.DialogoCambiarContrasena;
import jafrinventarios.vistas.acceso.contrasena.NombresTarjetasContrasena;
import jafrinventarios.vistas.utilidades.dialogos.DialogoAlerta;
import java.util.HashMap;

/**
 *
 * @author JOHN FORERO
 */
public class ControladorContrasena {
    
    private final DialogoCambiarContrasena ventanaContrasena;
    
    private final ServicioAutenticacion servicioAutenticacion;
    
    private int idUsuario = -1;
    private String correoRecuperacion;
    private String codigoRecuperacion;
    
    
    /*
    ========================================================================================
        CONSTRUCTOR PRIVADO PARA EVITAR QUE SE CREE SIN SU DEBIDA CONFIGURACION
    =========================================================================================
    */
    private ControladorContrasena(  DialogoCambiarContrasena ventana, 
                                    NombresTarjetasContrasena tarjetaInicial, 
                                    ServicioAutenticacion servicioAutenticacion) {
        this.ventanaContrasena = ventana;
        this.servicioAutenticacion = servicioAutenticacion;
        
        inicializarEventosBotones(tarjetaInicial);
    }

    private void setIdUsuario( int idUsuario ) {
        this.idUsuario = idUsuario;
    }
    
    /*
    ========================================================================================
        FÁBRICAS ESTÁTICAS: Los únicos puntos de acceso para los demás controladores
    =========================================================================================
    */

    public static void iniciarRecuperacion( java.awt.Window ventanaPadre , ServicioAutenticacion servicioAutenticacion ) {
        
        NombresTarjetasContrasena tarjetaInicial = NombresTarjetasContrasena.CORREO;
        
        // El propio controlador crea la vista que corresponde a la recuperación
        DialogoCambiarContrasena vista = DialogoCambiarContrasena.recuperarContrasena(ventanaPadre);
        
        // Se auto-instancia usando el constructor privado 
        ControladorContrasena controlador = new ControladorContrasena( vista, tarjetaInicial, servicioAutenticacion );
        
        // Muestra la ventana
        // Ejecutamos el controlador para que muestre el dialogo, es necesario hacerlo asi
        // por que un jdialog bloquea todas las demas ejecuciones, primero que el controlador
        // asigne los eventos de escucha de los botones y ahi si mostrar el dialogo
        controlador.iniciar(tarjetaInicial);
    }
    
    
    public static void iniciarCambio( java.awt.Window ventanaPadre, ServicioAutenticacion servicioAutenticacion ) {
        //Cuando es para cambiar la contraseña se tomara el id almacenado en ModeloSesionUsuario
        
        NombresTarjetasContrasena tarjetaInicial = NombresTarjetasContrasena.CONTRASENA_ANTIGUA;
        
        // El propio controlador crea la vista que corresponde al cambio
        DialogoCambiarContrasena vista = DialogoCambiarContrasena.cambiarContrasena(ventanaPadre);
        
        // Se auto-instancia 
        ControladorContrasena controlador = new ControladorContrasena(vista, tarjetaInicial, servicioAutenticacion );
        controlador.setIdUsuario( ModeloSesionUsuario.getInstancia().getIdUsuario() );
        
        controlador.iniciar(tarjetaInicial);
    }

// ========================================================================================
    
    
    /*
    ============================================================================
                    METODOS CONFIGURACION INICIAL DE LA VISTA
    ============================================================================
    */
    
    // Un método explícito para arrancar el flujo
    public void iniciar(NombresTarjetasContrasena tarjetaInicial) {
        ventanaContrasena.mostrarTarjeta(tarjetaInicial);
        ventanaContrasena.mostrarDialogo(); // Se hace visible HASTA AHORA
    }
    
    
    private void inicializarEventosBotones(NombresTarjetasContrasena tarjetaInicial) {
        
        if (  tarjetaInicial == NombresTarjetasContrasena.CORREO  ) {
            ventanaContrasena.getBtnEnviarCodigo().addActionListener(e -> {
                                procesarPaso(NombresTarjetasContrasena.CORREO);}
            );
            ventanaContrasena.getBtnConfirmarCodigo().addActionListener(e -> {
                                procesarPaso(NombresTarjetasContrasena.CODIGO);}
            );
        } else {
            ventanaContrasena.getBtnConfirmarContrasenaAntigua().addActionListener(e -> {
                                procesarPaso(NombresTarjetasContrasena.CONTRASENA_ANTIGUA);}
            );
        }
        
        ventanaContrasena.getBtnCambiarContrasena().addActionListener(e -> {
                                procesarPaso(NombresTarjetasContrasena.CONTRASENA_NUEVA);}
        );

    }
    
    
    /*
    ============================================================================
                METODOS PARA CONSULTAR A LOS SERVICIOS
    ============================================================================
    */
    
    private int obtenerIdUsuarioConCorreo( String correo ) throws Exception {
        return servicioAutenticacion.obtenerIdUsuarioConCorreo(correo);
    }
    
    private String generarCodigo(){
        return ServicioSeguridad.generarCodigo();
    }
    
    private void enviarCodigoCorreo( String correo, String codigo) throws Exception{
        ServicioCorreos.enviarCodigoRecuperacion(correo, codigo);
    }
    
    private boolean validarContrasenaAntigua( int idUsuario, String contrasenaAntigua) throws Exception{
        return servicioAutenticacion.validarContrasena(idUsuario, contrasenaAntigua);
    }
    
    private void cambiarContrasena( int idUsuario, String contrasenaNueva ) throws Exception {
        servicioAutenticacion.cambiarContrasena(idUsuario, contrasenaNueva);
    }
    
    /*
    ============================================================================
          METODOS PARA PROCESAR LOS PASOS PARA EL CAMBIO DE CONTRASEÑA
    ============================================================================
    */
    
    private void procesarPaso( NombresTarjetasContrasena tarjeta ){

        if(!ventanaContrasena.ejecutarValidacionFormulario(tarjeta)){
            ventanaContrasena.mostrarAlertaErrorFormatoCampos();
            return;
        }
        
        HashMap<String, String> datosFormulario = ventanaContrasena.recolectarDatosFormulario(tarjeta);
        
        switch (tarjeta) {
            case CORREO:
                procesarCorreoEnBD(datosFormulario.get("correo"));
                break;
            case CODIGO:
                procesarCodigo(datosFormulario.get("codigo"));
                break;
            case CONTRASENA_ANTIGUA:
                procesarContrasenaAntiguaEnBD(datosFormulario.get("contrasenaAntigua"));
                break;
            case CONTRASENA_NUEVA:
                procesarCambioContrasenaEnBD(datosFormulario.get("contrasenaNueva"));
                break;
        }
    
    }
    
    
    private void procesarCorreoEnBD( String correo ){

        try {
            idUsuario = obtenerIdUsuarioConCorreo(correo);
        
            codigoRecuperacion = generarCodigo();
            ventanaContrasena.mostrarAlertaCargando(
                    "Enviando correo con codigo de recuperacion", 
                    ()->{ enviarCodigoCorreo(correo, codigoRecuperacion); }
            );
            
            //Linea de prueba para ver el codigo de recuperacion
            System.out.println(" codigo de recuperacion : " + codigoRecuperacion );
            avanzarSiguienteTarjeta(NombresTarjetasContrasena.CODIGO);
            
        } catch ( ExcepcionValidacionBD e ){ 
            mostrarErroresValidacionCampos( NombresTarjetasContrasena.CORREO, e.getErrores() );        
        } 
        catch (Exception e) {
            // Errores en el servicio
            mostrarErrorServicio(e.getMessage());         
        }
        
        
    }
    
    
    
    private void procesarCodigo (String codigo){

        if ( codigoRecuperacion.equals(codigo) ) {
            avanzarSiguienteTarjeta(NombresTarjetasContrasena.CONTRASENA_NUEVA);
        } else {
            HashMap<String, String> errores = new HashMap<>();
            errores.put("codigo", "El codigo no coincide.");
            
            mostrarErroresValidacionCampos(NombresTarjetasContrasena.CODIGO, errores);
        }
        
    }
        
    
    
    private void procesarContrasenaAntiguaEnBD (String contrasena){
        
        try {
            if ( validarContrasenaAntigua(idUsuario, contrasena) ) {
                avanzarSiguienteTarjeta(NombresTarjetasContrasena.CONTRASENA_NUEVA);
            } else {
                HashMap<String, String> errores = new HashMap<>();
                errores.put("contrasenaAntigua", "La contraseña no coincide");

                mostrarErroresValidacionCampos(NombresTarjetasContrasena.CONTRASENA_ANTIGUA, errores);
            }
        } catch (Exception e) {
            // Errores en el servicio
            mostrarErrorServicio(e.getMessage()); 
        }
        
    }
            
    
    
    private void procesarCambioContrasenaEnBD (String contrasena){
        
        try {
            cambiarContrasena( idUsuario, contrasena );
            ventanaContrasena.mostrarAlertaExito("Contraseña cambiada correctamente");
            ventanaContrasena.dispose();
        } catch (Exception e) {
            // Errores en el servicio
            mostrarErrorServicio(e.getMessage());  
        }
        
        
    }
    
    
    private void avanzarSiguienteTarjeta(NombresTarjetasContrasena siguienteTarjeta) {
        ventanaContrasena.mostrarTarjeta(siguienteTarjeta);
    }
    
    
    /*
    ============================================================================
            METODO PARA MOSTRAR DIALOGOS ERROR RESPUESTA BD
    ============================================================================
    */

    private void mostrarErroresValidacionCampos(NombresTarjetasContrasena tarjeta, HashMap<String, String> errores ){
        // Le pasamos el error al ValidadorFormulario para que pinte el o los JLabel
        ventanaContrasena.mostrarErroresValidacionCampos(tarjeta, errores);

        // Mostramos un modal general para que el usuario sepa que algo falló
        ventanaContrasena.mostrarAlertaErroresValidacionCampos( errores );
    }
    
    private void mostrarErrorServicio( String mensaje ){
        ventanaContrasena.mostrarAlertaError("Error de conexion", mensaje);
    }
  
}

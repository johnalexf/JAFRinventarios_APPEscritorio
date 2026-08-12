/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jafrinventarios.controladores.acceso;

import jafrinventarios.vistas.acceso.contrasena.DialogoCambiarContrasena;
import jafrinventarios.vistas.acceso.contrasena.NombresTarjetasContrasena;
import jafrinventarios.vistas.utilidades.dialogos.DialogoMensajePersonalizado;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import javax.swing.JFrame;

/**
 *
 * @author JOHN FORERO
 */
public class ControladorContrasena {
    
    private final DialogoCambiarContrasena ventanaContrasena;
    
   // CONSTRUCTOR PRIVADO: Nadie desde afuera puede usar 'new ControladorContrasena'
    private ControladorContrasena(DialogoCambiarContrasena ventana, NombresTarjetasContrasena tarjetaInicial) {
        this.ventanaContrasena = ventana;
        
        inicializarEventosBotones(tarjetaInicial);
    }
    
    // =========================================================================
    // FÁBRICAS ESTÁTICAS: Los únicos puntos de acceso para los demás controladores
    // =========================================================================

    public static void iniciarRecuperacion(JFrame ventanaPadre) {
        
        NombresTarjetasContrasena tarjetaInicial = NombresTarjetasContrasena.CORREO;
        
        // El propio controlador crea la vista que corresponde a la recuperación
        DialogoCambiarContrasena vista = DialogoCambiarContrasena.recuperarContrasena(ventanaPadre);
        
        // Se auto-instancia usando el constructor privado 
        ControladorContrasena controlador = new ControladorContrasena(vista, tarjetaInicial);
        
        // Muestra la ventana
        // Ejecutamos el controlador para que muestre el dialogo, es necesario hacerlo asi
        // por que un jdialog bloquea todas las demas ejecuciones, primero que el controlador
        // asigne los eventos de escucha de los botones y ahi si mostrar el dialogo
        controlador.iniciar(tarjetaInicial);
    }
    
    public static void iniciarCambio(JFrame ventanaPadre) {
        //Cuando es para cambiar la contraseña se tomara el id almacenado en ModeloSesionUsuario
        
        NombresTarjetasContrasena tarjetaInicial = NombresTarjetasContrasena.CONTRASENA_ANTIGUA;
        
        // El propio controlador crea la vista que corresponde al cambio
        DialogoCambiarContrasena vista = DialogoCambiarContrasena.cambiarContrasena(ventanaPadre);
        
        // Se auto-instancia 
        ControladorContrasena controlador = new ControladorContrasena(vista, tarjetaInicial );
        
        controlador.iniciar(tarjetaInicial);
    }

    // =========================================================================
    
    
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
    
    
    private void procesarPaso( NombresTarjetasContrasena tarjeta ){

        if(!ventanaContrasena.ejecutarValidacionFormulario(tarjeta)){
            DialogoMensajePersonalizado.mostrarErrorFormatoCampos(ventanaContrasena);
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
                procesarContrasenaAntiguaEnBD(datosFormulario.get("contrasena"));
                break;
            case CONTRASENA_NUEVA:
                procesarCambioContrasenaEnBD(datosFormulario);
                break;
        }
    
    }
    
    
    private void procesarCorreoEnBD( String correo ){
        //TODO Pendiente el metodo para consultar a la base de datos el correo
        System.out.println("Consultando BD para el correo: " + correo);

        // Simulación de la respuesta de la consulta
        boolean correoExiste = true; 
        
        if (correoExiste) {
            // TODO: Enviar código al correo
            avanzarSiguienteTarjeta(NombresTarjetasContrasena.CODIGO);
        } else {
            // Armamos el diccionario de errores como respondería el backend
            HashMap<String, String> erroresBackend = new HashMap<>();
            erroresBackend.put("correo", "No se encuentra registrado.");
            
            mostrarErrorRespuestaBD(NombresTarjetasContrasena.CORREO, erroresBackend);
        }
        
    }
    
    
    
    private void procesarCodigo (String codigo){
        //TODO Pendiente el metodo para verificar codigo
        System.out.println("Verificando codigo enviado al correo");

        // Simulación de la respuesta de la consulta
        boolean codigoCoincide = true; 
        
        if (codigoCoincide) {
            avanzarSiguienteTarjeta(NombresTarjetasContrasena.CONTRASENA_NUEVA);
        } else {
            // Armamos el diccionario de errores como respondería el backend
            HashMap<String, String> erroresBackend = new HashMap<>();
            erroresBackend.put("codigo", "El codigo no coincide.");
            
            mostrarErrorRespuestaBD(NombresTarjetasContrasena.CODIGO, erroresBackend);
        }
        
    }
        
    
    
    private void procesarContrasenaAntiguaEnBD (String contrasena){
        //TODO Pendiente el metodo para verificar la contraseña actual
        System.out.println("Verificando contraseña");

        // Simulación de la respuesta de la consulta
        boolean contrasenaValida = true; 
        
        if (contrasenaValida) {
            avanzarSiguienteTarjeta(NombresTarjetasContrasena.CONTRASENA_NUEVA);
        } else {
            // Armamos el diccionario de errores como respondería el backend
            HashMap<String, String> erroresBackend = new HashMap<>();
            erroresBackend.put("contrasena", "La contraseña no coincide");
            
            mostrarErrorRespuestaBD(NombresTarjetasContrasena.CONTRASENA_ANTIGUA, erroresBackend);
        }
        
    }
            
    
    
    private void procesarCambioContrasenaEnBD (HashMap<String, String> formulario){
        //TODO Pendiente el metodo para verificar la contraseña actual
        System.out.println("Verificando y guardando contraseña nueva");

        // Simulación de la respuesta de la consulta
        boolean contrasenaNuevaValida = true; 
        
        if (contrasenaNuevaValida) {
            //
            ventanaContrasena.dispose();
        } else {
            // Armamos el diccionario de errores como respondería el backend
            HashMap<String, String> erroresBackend = new HashMap<>();
            erroresBackend.put("contrasenaNueva", "Invalida");
            
            mostrarErrorRespuestaBD(NombresTarjetasContrasena.CONTRASENA_NUEVA, erroresBackend);
        }
        
    }
    
    
    private void avanzarSiguienteTarjeta(NombresTarjetasContrasena siguienteTarjeta) {
        ventanaContrasena.mostrarTarjeta(siguienteTarjeta);
    }
    
    
    
    private void mostrarErrorRespuestaBD(NombresTarjetasContrasena tarjeta, HashMap<String, String> erroresBackend ){
        // Le pasamos el error al ValidadorFormulario para que pinte el o los JLabel
        ventanaContrasena.mostrarErrorRespuestaBD(tarjeta, erroresBackend);

        // Mostramos un modal general para que el usuario sepa que algo falló
        DialogoMensajePersonalizado.mostrarErrorRespuestaBD(ventanaContrasena, erroresBackend); 
    }

    
}

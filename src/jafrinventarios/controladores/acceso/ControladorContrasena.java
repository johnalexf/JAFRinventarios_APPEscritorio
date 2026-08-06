/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jafrinventarios.controladores.acceso;

import jafrinventarios.vistas.acceso.contrasena.DialogoCambiarContrasena;
import jafrinventarios.vistas.acceso.contrasena.TarjetasRecuperacion;
import jafrinventarios.vistas.utilidades.dialogos.DialogoMensajePersonalizado;
import jafrinventarios.vistas.utilidades.iconos.IconosDialogosMensajePersonalizado;
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
    private final ArrayList<TarjetasRecuperacion> secuenciaNavegacion;
    private String correoUsuario;

   // CONSTRUCTOR PRIVADO: Nadie desde afuera puede usar 'new ControladorContrasena'
    private ControladorContrasena(DialogoCambiarContrasena ventana, boolean esRecuperacion, String correoUsuario) {
        this.ventanaContrasena = ventana;
        this.secuenciaNavegacion = definirSecuencia(esRecuperacion);
        this.correoUsuario = correoUsuario; 
        
        inicializarEventosBotones(esRecuperacion);
    }
    
    // =========================================================================
    // FÁBRICAS ESTÁTICAS: Los únicos puntos de acceso para los demás controladores
    // =========================================================================

    public static void iniciarRecuperacion(JFrame ventanaPadre) {
        // El propio controlador crea la vista que corresponde a la recuperación
        DialogoCambiarContrasena vista = DialogoCambiarContrasena.recuperarContrasena(ventanaPadre);
        
        // Se auto-instancia usando el constructor privado (esRecuperacion = true, correo = null)
        ControladorContrasena controlador = new ControladorContrasena(vista, true, null);
        
        // Muestra la ventana
        // Ejecutamos el controlador para que muestre el dialogo, es necesario hacerlo asi
        // por que un jdialog bloquea todas las demas ejecuciones, primero que el controlador
        // asigne los eventos de escucha de los botones y ahi si mostrar el dialogo
        controlador.iniciar();
    }
    
    public static void iniciarCambio(JFrame ventanaPadre, String correoUsuario) {
        if(correoUsuario == null || correoUsuario.trim().isEmpty()){
            throw new IllegalArgumentException("\nSe requiere especificar el correo para cambiar la contraseña.\n");
        }
        
        // El propio controlador crea la vista que corresponde al cambio
        DialogoCambiarContrasena vista = DialogoCambiarContrasena.cambiarContrasena(ventanaPadre);
        
        // Se auto-instancia (esRecuperacion = false)
        ControladorContrasena controlador = new ControladorContrasena(vista, false, correoUsuario);
        
        controlador.iniciar();
    }

    // =========================================================================
    
    
    // Un método explícito para arrancar el flujo
    public void iniciar() {
        ventanaContrasena.mostrarTarjeta(secuenciaNavegacion.get(0));
        ventanaContrasena.mostrarDialogo(); // Se hace visible HASTA AHORA
    }
    
    
    // Regla de negocio: ¿Cuál es la secuencia?
    private ArrayList<TarjetasRecuperacion> definirSecuencia(boolean esRecuperacion) {
        if (esRecuperacion) {
            return new ArrayList<>(Arrays.asList(
                TarjetasRecuperacion.CORREO, 
                TarjetasRecuperacion.CODIGO, 
                TarjetasRecuperacion.CONTRASENA_NUEVA
            ));
        } else {
            return new ArrayList<>(Arrays.asList(
                TarjetasRecuperacion.CONTRASENA_ANTIGUA, 
                TarjetasRecuperacion.CONTRASENA_NUEVA
            ));
        }
    }
    
    private void inicializarEventosBotones(boolean esRecuperacion) {
        if (esRecuperacion) {
            ventanaContrasena.getBtnEnviarCodigo().addActionListener( e -> {
                                realizarPaso(TarjetasRecuperacion.CORREO);}
            );
            ventanaContrasena.getBtnConfirmarCodigo().addActionListener( e -> {
                                realizarPaso(TarjetasRecuperacion.CODIGO);}
            );
        } else {
            ventanaContrasena.getBtnConfirmarContrasenaAntigua().addActionListener( e -> {
                                realizarPaso(TarjetasRecuperacion.CONTRASENA_ANTIGUA);}
            );
        }
        
        ventanaContrasena.getBtnCambiarContrasena().addActionListener( e -> {
                                realizarPaso(TarjetasRecuperacion.CONTRASENA_NUEVA);}
        );

    }
    
    
    private void realizarPaso( TarjetasRecuperacion tarjeta ){

        if(!ventanaContrasena.ejecutarValidacionCampos(tarjeta)){
            DialogoMensajePersonalizado.mostrarErrorFormatoCampos(ventanaContrasena);
            return;
        }
        
        HashMap<String, String> formulario = ventanaContrasena.recolectarDatosFormulario(tarjeta);
        
        switch (tarjeta) {
            case CORREO:
                procesarCorreoEnBD(formulario.get("correo"));
                break;
            case CODIGO:
                procesarCodigo(formulario.get("codigo"));
                break;
            case CONTRASENA_ANTIGUA:
                procesarContrasenaAntiguaEnBD(formulario.get("contrasena"));
                break;
            case CONTRASENA_NUEVA:
                procesarCambioContrasenaEnBD(formulario);
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
            avanzarSiguienteTarjeta(TarjetasRecuperacion.CORREO);
        } else {
            // Armamos el diccionario de errores como respondería el backend
            HashMap<String, String> erroresBackend = new HashMap<>();
            erroresBackend.put("correo", "No se encuentra registrado.");
            
            mostrarErrorRespuestaBD(TarjetasRecuperacion.CORREO, erroresBackend);
        }
        
    }
    
    
    
    private void procesarCodigo (String codigo){
        //TODO Pendiente el metodo para verificar codigo
        System.out.println("Verificando codigo enviado al correo");

        // Simulación de la respuesta de la consulta
        boolean codigoCoincide = true; 
        
        if (codigoCoincide) {
            avanzarSiguienteTarjeta(TarjetasRecuperacion.CODIGO);
        } else {
            // Armamos el diccionario de errores como respondería el backend
            HashMap<String, String> erroresBackend = new HashMap<>();
            erroresBackend.put("codigo", "El codigo no coincide.");
            
            mostrarErrorRespuestaBD(TarjetasRecuperacion.CODIGO, erroresBackend);
        }
        
    }
        
    
    
    private void procesarContrasenaAntiguaEnBD (String contrasena){
        //TODO Pendiente el metodo para verificar la contraseña actual
        System.out.println("Verificando contraseña");

        // Simulación de la respuesta de la consulta
        boolean contrasenaValida = true; 
        
        if (contrasenaValida) {
            avanzarSiguienteTarjeta(TarjetasRecuperacion.CONTRASENA_ANTIGUA);
        } else {
            // Armamos el diccionario de errores como respondería el backend
            HashMap<String, String> erroresBackend = new HashMap<>();
            erroresBackend.put("contrasena", "La contraseña no coincide");
            
            mostrarErrorRespuestaBD(TarjetasRecuperacion.CONTRASENA_ANTIGUA, erroresBackend);
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
            
            mostrarErrorRespuestaBD(TarjetasRecuperacion.CONTRASENA_NUEVA, erroresBackend);
        }
        
    }
    
    
    private void avanzarSiguienteTarjeta(TarjetasRecuperacion tarjetaActual) {
        int indiceActual = secuenciaNavegacion.indexOf(tarjetaActual);
        
        // Verificamos que no estemos en la última tarjeta
        if (indiceActual < secuenciaNavegacion.size() - 1) {
            TarjetasRecuperacion siguienteTarjeta = secuenciaNavegacion.get(indiceActual + 1);
            ventanaContrasena.mostrarTarjeta(siguienteTarjeta);
        }
    }
    
    
    
    private void mostrarErrorRespuestaBD(TarjetasRecuperacion tarjeta, HashMap<String, String> erroresBackend ){
        // Le pasamos el error al ValidadorFormulario para que pinte el o los JLabel
        ventanaContrasena.mostrarErrorRespuestaBD(tarjeta, erroresBackend);

        // Mostramos un modal general para que el usuario sepa que algo falló
        DialogoMensajePersonalizado.mostrarErrorRespuestaBD(ventanaContrasena, erroresBackend); 
    }

    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jafrinventarios.controladores.acceso;

import jafrinventarios.vistas.acceso.contrasena.DialogoCambiarContrasena;
import jafrinventarios.vistas.acceso.contrasena.TarjetasRecuperacion;
import java.util.ArrayList;
import java.util.Arrays;
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
    
    
    }


    
}

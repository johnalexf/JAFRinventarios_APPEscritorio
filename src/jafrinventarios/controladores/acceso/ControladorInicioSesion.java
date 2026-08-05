
package jafrinventarios.controladores.acceso;

import jafrinventarios.controladores.ControladorNavegacionGlobal;
import jafrinventarios.vistas.acceso.InicioSesionPanel;
import jafrinventarios.vistas.acceso.DialogoCambiarContrasena;
import jafrinventarios.vistas.utilidades.dialogos.DialogoMensajePersonalizado;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class ControladorInicioSesion {

    private final InicioSesionPanel vistaInicio;

    // El constructor recibe la vista ya creada
    public ControladorInicioSesion(InicioSesionPanel vistaInicio) {
        this.vistaInicio = vistaInicio;
        inicializarEventosBotones();
    }

    // Activar eventos de escucha de clic en los botones
    private void inicializarEventosBotones() {
        
        this.vistaInicio.getBtnIngresar().addActionListener(e -> procesarIngreso());
        this.vistaInicio.getBtnLinkRecuperarContraseña().addActionListener(e -> procesarRecuperarContrasena());
    }

    private void procesarIngreso() {
        // Delegar a la vista que valide que no haya campos vacíos o con errores
        if ( !vistaInicio.ejecutarValidacionCampos() ) {
            JFrame ventanaPadre = (JFrame) SwingUtilities.getWindowAncestor(vistaInicio);
            DialogoMensajePersonalizado.mostrarDialogoErrorDatos(ventanaPadre);
            return; // Cortamos la ejecución aquí si hay errores visuales
        }

        // Si la validación de campos pasa, pedimos los datos limpios a la vista
        HashMap<String, String> datosFormulario = vistaInicio.recolectarDatosFormulario();
        
        datosFormulario.forEach(    (clave, valor) ->
            System.out.println(clave + " -> " + valor)
        );

        // Llamar a la capa de Modelo para consultar la BD
        System.out.println("Simulando consulta a BD : ");
        boolean credencialesValidas = true; // Simulación de éxito

        // Tomar decisión basada en la respuesta del Modelo
        if ( credencialesValidas ) {
            System.out.println("¡Ingreso exitoso!");
            
            ControladorNavegacionGlobal.getInstancia().cambiarAPrincipal();
            
        } else {
            // Mostrar error de credenciales inválidas
            System.out.println("Correo o contraseña incorrectos.");
        }
    }

    private void procesarRecuperarContrasena() {
        JFrame ventanaPadre = (JFrame) SwingUtilities.getWindowAncestor(vistaInicio);
        DialogoCambiarContrasena.recuperarContrasena(ventanaPadre);
    }
}
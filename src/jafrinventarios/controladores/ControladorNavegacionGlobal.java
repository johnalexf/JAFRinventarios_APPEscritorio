/*
 Clase aplicando patron de diseño singleton, en donde en ninguna parte del 
 proyecto se puede crear una instancia de la misma, si no que ella va a crear
 su propio objeto, el objetivo es que exista un unico controlador de navegacion
 global.
 */
package jafrinventarios.controladores;

import jafrinventarios.controladores.acceso.ControladorInicioSesion;
import jafrinventarios.controladores.acceso.ControladorRegistroUsuario;
import jafrinventarios.controladores.principal.ControladorNavegacionModulos;
import jafrinventarios.servicios.acceso.ServicioAutenticacion;
import jafrinventarios.servicios.acceso.ServicioRegistro;
import jafrinventarios.vistas.acceso.AccesoFrame;
import jafrinventarios.vistas.principal.PrincipalFrame;
import javax.swing.JFrame;

/**
 *
 * @author JOHN FORERO
 */
public class ControladorNavegacionGlobal {
    //Una variable estática que guarda la ÚNICA copia de sí misma.
    private static ControladorNavegacionGlobal instancia;

    // Aquí llevamos el registro de cuál es la ventana que está abierta actualmente.
    private JFrame ventanaActual;

    // CONSTRUCTOR PRIVADO: Al ser 'private', nadie más en todo el código puede usar 'new ControladorNavegacion()'.
    private ControladorNavegacionGlobal() {
    }

    /* LA PUERTA DE ENTRADA: Un método estático que  entrega la única copia.
        Si no existe, la crea. Si ya existe, devuelve la misma.
    
        Al tener la variable instancia de manera static, permanecera vivo el 
        objeto de la misma y se puede reutilizar cuando se quiera cambiar de pantallas
        desde accesoFrame a princalFrame o viceversa
    */
    public static ControladorNavegacionGlobal getInstancia() {
        if (instancia == null) {
            instancia = new ControladorNavegacionGlobal();
        }
        return instancia;
    }

    // =======================================================
    // MÉTODOS DE ENRUTAMIENTO (NAVEGACIÓN)
    // =======================================================

    public void iniciarPantallaAcceso() {
        cerrarVentanaActual();
        
        // Creamos la vista
        AccesoFrame vistaAcceso = new AccesoFrame();
        
        new ControladorInicioSesion(
                vistaAcceso.getInicioSesionPanel(),
                new ServicioAutenticacion()
        );
        new ControladorRegistroUsuario(
                vistaAcceso.getRegistroUsuarioPanel(),
                new ServicioRegistro()
        );
        
        // Actualizamos cuál es la ventana activa y la mostramos
        asignarYMostrarVentanaActual(vistaAcceso);
    }

    public void cambiarAPrincipal() {
        cerrarVentanaActual();
        
        // Creamos la vista principal
        PrincipalFrame vistaPrincipal = new PrincipalFrame();
        new ControladorNavegacionModulos(vistaPrincipal);
        // Actualizamos cuál es la ventana activa y la mostramos
        asignarYMostrarVentanaActual(vistaPrincipal);
    }
    
    private void asignarYMostrarVentanaActual(JFrame ventanaNueva){
        ventanaActual = ventanaNueva;
        ventanaActual.setVisible(true);
    }

    // Método de utilidad interno para destruir la ventana anterior sin dejar "basura" en la RAM
    private void cerrarVentanaActual() {
        if (ventanaActual != null) {
            ventanaActual.dispose(); 
        }
    }
}


package jafrinventarios.controladores.acceso;

import jafrinventarios.DTOs.acceso.DTOCredenciales;
import jafrinventarios.controladores.ControladorNavegacionGlobal;
import jafrinventarios.modelos.ModeloSesionUsuario;
import jafrinventarios.servicios.acceso.ServicioAutenticacion;
import jafrinventarios.vistas.acceso.InicioSesionPanel;
import jafrinventarios.vistas.utilidades.dialogos.DialogoAlerta;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class ControladorInicioSesion {

    private final InicioSesionPanel vistaInicio;
    private final ServicioAutenticacion servicioAutenticacion;

    // El constructor recibe la vista ya creada
    public ControladorInicioSesion(InicioSesionPanel vistaInicio, ServicioAutenticacion servicioAutenticacion ) {
        this.vistaInicio = vistaInicio;
        this.servicioAutenticacion = servicioAutenticacion;
        inicializarEventosBotones();
    }

    // Activar eventos de escucha de clic en los botones
    private void inicializarEventosBotones() {
        
        this.vistaInicio.getBtnIngresar().addActionListener(e -> procesarIngreso());
        this.vistaInicio.getBtnLinkRecuperarContraseña().addActionListener(e -> procesarRecuperarContrasena());
    }

    private void procesarIngreso() {
        // Delegar a la vista que valide que no haya campos vacíos o con errores
        if ( !vistaInicio.ejecutarValidacionFormulario() ) {
            JFrame ventanaPadre = (JFrame) SwingUtilities.getWindowAncestor(vistaInicio);
            DialogoAlerta.mostrarErrorFormatoCampos(ventanaPadre);
            return; // Cortamos la ejecución aquí si hay errores de formato de datos
        }

        // Si la validación de campos pasa, pedimos los datos limpios a la vista
        HashMap<String, String> datosFormulario = vistaInicio.recolectarDatosFormulario();
        
        DTOCredenciales credenciales = 
                servicioAutenticacion.iniciarSesion( 
                        datosFormulario.get("correo") , 
                        datosFormulario.get("contrasena")
                );

        if ( credenciales != null ) {

            ModeloSesionUsuario.getInstancia().iniciarSesion(
                                                 credenciales.getIdUsuario(),
                                                 credenciales.getNombreRol(),
                                                 credenciales.esAdministrador(),
                                                 credenciales.getIdEmpresa(),
                                                 credenciales.getNombreEmpresa()
            );
            
            ControladorNavegacionGlobal.getInstancia().cambiarAPrincipal();
            
        } else {
            // Mostrar error de credenciales inválidas
            System.out.println("Correo o contraseña incorrectos.");
            
            //codigo simulacion de respuesta de la base de datos
            vistaInicio.mostrarErrorRespuestaBD(
                   new HashMap<>(Map.of(
                        "correo", "Este correo no esta registrado",
                        "contrasena", "Contraseña erronea"
                    ))
            );
        }
    }

    
    private void procesarRecuperarContrasena() {
        JFrame ventanaPadre = (JFrame) SwingUtilities.getWindowAncestor(vistaInicio);
        
       /*
        El controlador de inicio de sesión no sabe NADA de cómo se ensambla adentro
            Este metodo estatico permite delegar al controlador que arme la vista
            segun corresponda y asi evitar que el desarrollador tenga que instanciar
            un dialogoCambiarContrasena incorrecto
        */
        ControladorContrasena.iniciarRecuperacion(ventanaPadre);
        
    }
}
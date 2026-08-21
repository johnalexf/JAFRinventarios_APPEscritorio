
package jafrinventarios.controladores.acceso;

import jafrinventarios.DTOs.acceso.DTOCredenciales;
import jafrinventarios.controladores.ControladorNavegacionGlobal;
import jafrinventarios.modelos.ModeloSesionUsuario;
import jafrinventarios.servicios.acceso.ServicioAutenticacion;
import jafrinventarios.servicios.excepciones.ExcepcionValidacionBD;
import jafrinventarios.vistas.acceso.InicioSesionPanel;
import java.util.HashMap;
import java.util.Map;

public class ControladorInicioSesion {

    private final InicioSesionPanel vistaInicio;
    private final ServicioAutenticacion servicioAutenticacion;

    
    /*
    ============================================================================
                        CONSTRUCTOR PUBLICO
    ============================================================================
    */
    public ControladorInicioSesion(InicioSesionPanel vistaInicio, ServicioAutenticacion servicioAutenticacion ) {
        this.vistaInicio = vistaInicio;
        this.servicioAutenticacion = servicioAutenticacion;
        inicializarEventosBotones();
    }
    
    
    /*
    ============================================================================
                            METODO EVENTOS BOTONES
    ============================================================================
    */
    // Activar eventos de escucha de clic en los botones
    private void inicializarEventosBotones() {
        
        this.vistaInicio.getBtnIngresar().addActionListener(e -> procesarIngreso());
        this.vistaInicio.getBtnLinkRecuperarContraseña().addActionListener(e -> procesarRecuperarContrasena());
    }

    
     /*
    ============================================================================
                    METODOS PARA CONSULTAR A LOS SERVICIOS
    ============================================================================
    */
    
    private DTOCredenciales iniciarSesion( String correo, String contrasena )throws Exception{
        return servicioAutenticacion.iniciarSesion(correo, contrasena);
    }
    
    
    /*
    ============================================================================
                    METODO PARA REALIZAR EL INGRESO
    ============================================================================
    */
    
    private void procesarIngreso() {
        // Delegar a la vista que valide que no haya campos vacíos o con errores
        if ( !vistaInicio.ejecutarValidacionFormulario() ) {
            vistaInicio.mostrarAlertaErrorFormatoCampos();
            return; // Cortamos la ejecución aquí si hay errores de formato de datos
        }

        // Si la validación de campos pasa, pedimos los datos limpios a la vista
        HashMap<String, String> datosFormulario = vistaInicio.recolectarDatosFormulario();
        
        try {
            DTOCredenciales credenciales = 
                iniciarSesion( datosFormulario.get("correo") , datosFormulario.get("contrasena")  );

            ModeloSesionUsuario.getInstancia().iniciarSesion(
                                                 credenciales.getIdUsuario(),
                                                 credenciales.getNombreRol(),
                                                 credenciales.esAdministrador(),
                                                 credenciales.getIdEmpresa(),
                                                 credenciales.getNombreEmpresa()
            );

            ControladorNavegacionGlobal.getInstancia().cambiarAPrincipal();
      
        }catch( ExcepcionValidacionBD e ){ 
            vistaInicio.mostrarErroresValidacionCampos( e.getErrores() );
        }catch (Exception e) {
            vistaInicio.mostrarAlertaError(e.getMessage());
        }
        
    }

    
    /*
    ============================================================================
                METODO PARA ACTIVAR LA RECUPERACION DE CONTRASEÑA
    ============================================================================
    */
    
    
    private void procesarRecuperarContrasena() {
       /*
        El controlador de inicio de sesión no sabe NADA de cómo se ensambla adentro
            Este metodo estatico permite delegar al controlador que arme la vista
            segun corresponda y asi evitar que el desarrollador tenga que instanciar
            un dialogoCambiarContrasena incorrecto
        */
        ControladorContrasena.iniciarRecuperacion( vistaInicio.getVentanaPadre(), servicioAutenticacion );
        
    }
}
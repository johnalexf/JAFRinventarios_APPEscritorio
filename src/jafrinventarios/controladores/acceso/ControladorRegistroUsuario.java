
package jafrinventarios.controladores.acceso;

import jafrinventarios.modelos.usuarios.ModeloUsuario;
import jafrinventarios.servicios.acceso.ServicioRegistro;
import jafrinventarios.servicios.excepciones.ExcepcionValidacionBD;
import jafrinventarios.servicios.usuarios.ServicioRoles;
import jafrinventarios.vistas.acceso.RegistroUsuarioPanel;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author JOHN FORERO
 */
public class ControladorRegistroUsuario {
    
    
    private RegistroUsuarioPanel vistaRegistro;
    
    private ServicioRegistro servicioRegistro;

    /*
    ============================================================================
                        CONSTRUCTOR PUBLICO
    ============================================================================
    */
    public ControladorRegistroUsuario(RegistroUsuarioPanel vistaRegistro, ServicioRegistro servicioRegistro ) {
        this.vistaRegistro = vistaRegistro;
        this.servicioRegistro = servicioRegistro;
        cargarRoles();
        inicializarEventoBoton();
    }
    
    
    /*
    ============================================================================
                    METODOS CONFIGURACION INICIAL DE LA VISTA
    ============================================================================
    */
    
    
    private void cargarRoles() {
        
        try {
            LinkedHashMap<Integer, String> diccionarioRoles = obtenerDiccionarioRoles();
        
            // Le asignamos el diccionario al comboBox
            this.vistaRegistro.inicializarComboBoxRoles( diccionarioRoles );
        } catch (Exception e) {
            vistaRegistro.mostrarAlertaError(e.getMessage());
        }
        
    }
    
    
    private void inicializarEventoBoton(){
        this.vistaRegistro.getBtnRegistrar().addActionListener(
                e -> procesarRegistro() 
        );
    
    }
    
    
    /*
    ============================================================================
                METODOS PARA CONSULTAR A LOS SERVICIOS
    ============================================================================
    */
    
    private LinkedHashMap<Integer, String> obtenerDiccionarioRoles() throws Exception{
        return ServicioRoles.obtenerDiccionarioRoles();
    }
    
    private boolean isValidoCodigo( String codigo, boolean isRegistroAdministrador) throws Exception{
        return servicioRegistro.isValidoCodigo( codigo, isRegistroAdministrador );
    }
    
    private void registrarAdministrador ( ModeloUsuario usuario, String contrasena, String nombreEmpresa ) throws Exception{
        servicioRegistro.registrarAdministrador(usuario, contrasena, nombreEmpresa);
    }
    
    private void registrarNoAdministrador ( ModeloUsuario usuario, String contrasena ) throws Exception{
        servicioRegistro.registrarNoAdministrador( usuario, contrasena );
    }
    
    
    /*
    ============================================================================
                    METODO PARA REALIZAR EL REGISTRO
    ============================================================================
    */
    
    private void procesarRegistro(){
        
          
        // Validaciones de los campos si corresponden a su tipo
        if ( !vistaRegistro.ejecutarValidacionFormulario() ) {
            vistaRegistro.mostrarAlertaErrorFormatoCampos();
            return; // Cortamos la ejecución aquí si hay errores de formato de datos
        }
        
        // Extracción y construcción del Modelo
        HashMap<String, String> datosFormulario = vistaRegistro.recolectarDatosFormulario();
        datosFormulario.forEach(    
            (clave, valor) ->   System.out.println(clave + " -> " + valor)
        );

        ModeloUsuario usuario = new ModeloUsuario();
        usuario = asignarDatosAModeloUsuario( usuario, datosFormulario );
      
        String codigo = datosFormulario.get("codigo");
        boolean registrarAdministrador = vistaRegistro.isRegistroAdministrador();
        
        // Auditoría de Codigo
        try {                    
            if( !isValidoCodigo( codigo, registrarAdministrador ) ){
                // TODO: La idea es que el servicio responda, puede responder
                // falla de conexion o este codigo solo permitia crear un usuario administrador
                vistaRegistro.mostrarAlertaError("El codigo no es valido");
                vistaRegistro.mostrarErroresValidacionCampos(
                       new HashMap<>(Map.of(
                            "codigo", "Este codigo no es valido"
                        ))
                );
                return;
            }
        } catch (Exception e) {
            vistaRegistro.mostrarAlertaError(e.getMessage());
            return;
        }

        
        // Extraccion contrasena y nombre de la empresa si existe
        String contrasena = (datosFormulario.containsKey("contrasena"))
                                ? datosFormulario.get("contrasena")
                                : "";
        
        String nombreEmpresa = (datosFormulario.containsKey("nombreEmpresa"))
                                ? datosFormulario.get("nombreEmpresa")
                                : "";
        
        
         /*
        ========================================================================
                                LÓGICA DE BASE DE DATOS 
        ========================================================================
        */
        try {
            
            if(registrarAdministrador)
                registrarAdministrador(usuario, contrasena, nombreEmpresa);
            else
                registrarNoAdministrador(usuario, contrasena);
            
            vistaRegistro.ejecutarLimpiezaFormulario();
            vistaRegistro.mostrarAlertaRegistroExitoso( usuario.getAliasUsuario() );
     
        }catch( ExcepcionValidacionBD e ){ 
            vistaRegistro.mostrarErroresValidacionCampos( e.getErrores() );
        }catch (Exception e) {
            vistaRegistro.mostrarAlertaError(e.getMessage());
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
    
    
}

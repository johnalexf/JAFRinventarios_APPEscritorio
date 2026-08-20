/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jafrinventarios.controladores.acceso;

import jafrinventarios.modelos.usuarios.ModeloUsuario;
import jafrinventarios.servicios.acceso.ServicioRegistro;
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
        LinkedHashMap<Integer, String> diccionarioRoles = ServicioRoles.obtenerDiccionarioRoles();
        
        // Le asignamos el diccionario al comboBox
        this.vistaRegistro.inicializarComboBoxRoles( diccionarioRoles );
    }
    
    
    private void inicializarEventoBoton(){
        this.vistaRegistro.getBtnRegistrar().addActionListener(
                e -> procesarRegistro() 
        );
    
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
        
        
        // Auditoría de Codigo
        if( !servicioRegistro.esValidoCodigo( codigo, usuario.getIdRolUsuario() ) ){
            // TODO: La idea es que el servicio responda, puede responder
            // falla de conexion, codigo no valido, o este codigo solo permitia crear un usuario administrador
            vistaRegistro.mostrarError("El codigo no es valido");
            vistaRegistro.mostrarErrorRespuestaBD(
                   new HashMap<>(Map.of(
                        "codigo", "Este codigo no es valido"
                    ))
            );
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
        boolean usuarioRegistrado = 
                servicioRegistro.registrarUsuario( codigo, usuario, contrasena, nombreEmpresa );


        // Manejo de la respuesta
        if ( usuarioRegistrado ) {

            vistaRegistro.ejecutarLimpiezaFormulario();
            vistaRegistro.mostrarAlertaRegistroExitoso( usuario.getAliasUsuario() );

        } else {
            // Mostrar error de credenciales inválidas
            System.out.println("EL o los datos ya existenten en la base de datos.");
            
            //codigo simulacion de respuesta de la base de datos
            vistaRegistro.mostrarErrorRespuestaBD(
                   new HashMap<>(Map.of(
                        "correo", "Este correo ya esta registrado",
                        "telefono", "El telefono ya existe"
                    ))
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
    
    
}

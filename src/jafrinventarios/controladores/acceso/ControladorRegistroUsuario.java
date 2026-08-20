/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jafrinventarios.controladores.acceso;

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
        
          
        // Delegar a la vista que valide que no haya campos vacíos o con errores
        if ( !vistaRegistro.ejecutarValidacionFormulario() ) {
            vistaRegistro.mostrarAlertaErrorFormatoCampos();
            return; // Cortamos la ejecución aquí si hay errores de formato de datos
        }
        
        // Si la validación de campos pasa, pedimos los datos limpios a la vista
        HashMap<String, String> datosFormulario = vistaRegistro.recolectarDatosFormulario();
        
        datosFormulario.forEach(    
            (clave, valor) ->   System.out.println(clave + " -> " + valor)
        );

        // Llamar a la capa de Modelo para consultar la BD
        System.out.println("Simulando consulta a BD : ");
        boolean credencialesValidas = true; // Simulación de respuesta

        // Tomar decisión basada en la respuesta del Modelo
        if ( credencialesValidas ) {

            vistaRegistro.ejecutarLimpiezaFormulario();
            vistaRegistro.mostrarAlertaRegistroExitoso( datosFormulario.get("alias"));

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
    
    
}

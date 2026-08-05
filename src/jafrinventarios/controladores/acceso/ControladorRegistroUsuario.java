/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jafrinventarios.controladores.acceso;

import jafrinventarios.vistas.acceso.RegistroUsuarioPanel;
import jafrinventarios.vistas.utilidades.dialogos.DialogoMensajePersonalizado;
import jafrinventarios.vistas.utilidades.iconos.IconosDialogosMensajePersonalizado;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author JOHN FORERO
 */
public class ControladorRegistroUsuario {
    
    private RegistroUsuarioPanel vistaRegistro;

    public ControladorRegistroUsuario(RegistroUsuarioPanel vistaRegistro) {
        this.vistaRegistro = vistaRegistro;
        inicializarEventoBoton();
    }
    
    private void inicializarEventoBoton(){
        this.vistaRegistro.getBtnRegistrar().addActionListener(
                e -> procesarRegistro() 
        );
    
    }
    
    private void procesarRegistro(){
        
        JFrame ventanaPadre = (JFrame) SwingUtilities.getWindowAncestor(vistaRegistro);
        
        // Delegar a la vista que valide que no haya campos vacíos o con errores
        if ( !vistaRegistro.ejecutarValidacionCampos() ) {
            
            DialogoMensajePersonalizado.mostrarDialogoErrorDatos(ventanaPadre);
            return; // Cortamos la ejecución aquí si hay errores visuales
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
            System.out.println("¡Registro exitoso!");
            DialogoMensajePersonalizado.mostrarDialogo(
                    ventanaPadre, 
                    "Registro exitoso", 
                    "El usuario " + datosFormulario.get("alias")
                    + "\n ha sido registrado correctamente"
                    + "\n ya puedes iniciar sesion." , 
                    IconosDialogosMensajePersonalizado.EXITO, 
                    false);
            //
            
        } else {
            // Mostrar error de credenciales inválidas
            System.out.println("Datos existentes en la base de datos.");
            
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

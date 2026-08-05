/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jafrinventarios.vistas.utilidades.validaciones;

import java.util.HashMap;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.text.JTextComponent;

/**
 *
 * @author JOHN FORERO
 */
public class ValidadorFormulario {
    
    /* Guardamos cada campo del formulario con una clave valor, en donde la clave 
        sera el name de dicho componente, permitiendo asi mostrar errores como 
        respuesta de la consulta a la base de datos.
        Ejemplo Correo no encontrado, contraseña incorrecta, alias repetido, etc.
    */
    private final HashMap<String, CampoValidable> listaCamposFormulario;

    public ValidadorFormulario() {
        listaCamposFormulario = new HashMap<>();
    }
    
    public void agregarCampo( JTextComponent input, JLabel lblError, TipoDatoFormulario tipoDato, boolean esObligatorio ){
        
        // Si falta el nombre, la aplicación se detiene aquí mismo con un error en consola
        validarNombreEnComponente(input);
        
        listaCamposFormulario.put( 
                input.getName() ,
                new CampoFormulario( input, lblError, tipoDato, esObligatorio)
        );
        
    }
    
    public void agregarCampoConfirmarContrasena( 
                                    JPasswordField inputConfirmarContrasena, 
                                    JPasswordField inputContrasena, 
                                    JLabel lblError ){
        // Si falta el nombre, la aplicación se detiene aquí mismo con un error en consola
        validarNombreEnComponente(inputConfirmarContrasena);
        
        listaCamposFormulario.put(
                inputConfirmarContrasena.getName(),
                new CampoConfirmarContrasena(inputConfirmarContrasena,inputContrasena,lblError)
        );
        
    }
    
    private void validarNombreEnComponente(JComponent componente){
        // Obtenemos el nombre asignado en las propiedades del diseñador visual
        String nombre = componente.getName();
        
        if (nombre == null || nombre.trim().isEmpty()) {
            // Esto detiene la ejecución del programa inmediatamente (Fail-Fast)
            // y te muestra en la consola exactamente qué componente falló.
            throw new IllegalArgumentException(
                "\n\n Error en agregar campo a validadorFormulario: "
                + "\n El componente de tipo " 
                + componente.getClass().getSimpleName() 
                + " no tiene un 'name' configurado. "
                + "\n Es obligatorio asignarlo en las propiedades de la vista. \n"
            );
        }
    }
    
    public boolean validar(){
        
        boolean respuesta = true;
        
        /*Con el for each hacemos que se validen todos los campos y se muestren
          los errores correspondientes en el lblError de cada input
        */
        for(CampoValidable campo : listaCamposFormulario.values()){
            // Cambiamos la respuesta a false si cualquiera de los campos
            // no es un dato valido
            if(!campo.validar()){
                respuesta = false;
            }
        
        }
        
        return respuesta;
    }
    
    public void limpiarErrores(){
        for(CampoValidable campo : listaCamposFormulario.values()){
            campo.limpiarError();
        }
    }
    
    public HashMap<String, String> recolectarDatos(){
        
        /* NOTA: Para que este metodo los resultados sean los esperados, es necesario
                 configurar el atributo name de cada uno de los componentes, con un nombre
                 unico y representativo en la base de datos o el modelo.
        */
        HashMap<String, String> recolectorDatos = new HashMap<>();
        listaCamposFormulario.values().forEach(campo -> {
            recolectorDatos.put(campo.getNameComponente(), campo.getValorComponente());
        });
        
        return recolectorDatos;
    }
    

}

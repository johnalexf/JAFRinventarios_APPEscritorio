/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jafrinventarios.vistas.utilidades.validaciones;

import java.util.HashMap;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
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

    //Constructor
    public ValidadorFormulario() {
        listaCamposFormulario = new HashMap<>();
    }
    
    /*
    Metodo para agregar campos que permitan obtener el texto con getText
    dentro de su propia caja
    */ 
    public void agregarCampo( 
            JTextComponent input, 
            JLabel lblError, 
            TipoDatoFormulario tipoDato, 
            boolean esObligatorio ){
        
        // Si falta el nombre, la aplicación se detiene aquí mismo con un error en consola
        validarNombreEnComponente(input);
        
        listaCamposFormulario.put( 
                input.getName() ,
                new CampoFormulario( input, lblError, tipoDato, esObligatorio)
        );
        
    }
    
    /*
    Metodo dedicada solamente para el campo confirmar contraseña
    por lo tanto necesita de un campo contraseña original para poder compararlos.
    */
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
    
    /*
    Metodo para agregar ComboBox, se requiere de un diccionario 
    clave: nombreItem, valor: idItem, para asi en la recoleccion de datos,
    enviar el id correspondiente a la base de datos.
    */
    public void agregarCampoComboBox(
                                JComboBox comboBox,
                                HashMap<String, Integer> listaOpcionesConId, 
                                JLabel lblError,
                                boolean esObligatorio ){
        validarNombreEnComponente(comboBox);
        
        listaCamposFormulario.put(
                comboBox.getName(), 
                new CampoComboBox(comboBox, listaOpcionesConId, lblError, esObligatorio)
        );
    }
    
    //Metodo para eliminar cualquier campo guardado por medio de el atributo name
    public void eliminarCualquierCampo(JComponent campo){
        
        validarNombreEnComponente(campo);
        
        listaCamposFormulario.remove(campo.getName());
        
    }
    
    /*
    Funcion para validar un componente tiene asignado el atributo name con un 
    valor diferente de espacios o nulo, en dado caso que no este asignado se
    para la ejecucion del programa y se avisa del respectivo error de forma 
    personalizada
    */
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
    
    // Metodo para validar todos los campos de la listaCamposFormulario
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
    
    /*
    Metodo para limpiar todos los errores que se hayan mostrado en cada uno
    de los lblError correspondiente de cada campo
    */
    public void limpiarErrores(){
        for(CampoValidable campo : listaCamposFormulario.values()){
            campo.limpiarError();
        }
    }
    
    /*
    Metodo para recolectar en un diccionario clave: nameItem , valor: valorItem
    que sera destinado a enviar al controlador para entregarlo a la base de datos
    */
    public HashMap<String, String> recolectarDatos(){
        
        /* NOTA: Para que este metodo los resultados sean los esperados, es necesario
                 configurar el atributo name de cada uno de los componentes, con un nombre
                 unico y representativo en la base de datos o el modelo.
        */
        HashMap<String, String> recolectorDatos = new HashMap<>();
        listaCamposFormulario.entrySet().forEach(campoFormulario -> {
            recolectorDatos.put(
                    campoFormulario.getKey(), 
                    campoFormulario.getValue().getValorComponente()
            );
        });
        
        return recolectorDatos;
    }
    
    
    /*
    Metodo para personalizar la respuesta que entrega la consulta a la base de datos
    en dado caso que un campo tenga un error se mostrara el mensaje correspondiente en
    su lblError
    */
    public void mostrarErrorRespuestaBD( HashMap<String, String> erroresCamposBD ){
       
        if(!erroresCamposBD.isEmpty()){
            
           erroresCamposBD.entrySet().forEach( campoErrorBD -> {
               CampoValidable campoAMostrarError = listaCamposFormulario.get(campoErrorBD.getKey());
               campoAMostrarError.mostrarError(campoErrorBD.getValue());
           });
        
        }
    }
    

}

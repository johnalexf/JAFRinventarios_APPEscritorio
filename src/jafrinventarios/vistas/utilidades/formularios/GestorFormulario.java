/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jafrinventarios.vistas.utilidades.formularios;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JLabel;

/**
 *
 * @author JOHN FORERO
 */
public class GestorFormulario {
    
    /* Guardamos cada campo del formulario con una clave valor, en donde la clave 
        sera el name de dicho componente, permitiendo asi mostrar errores como 
        respuesta de la consulta a la base de datos.
        Ejemplo Correo no encontrado, contraseña incorrecta, alias repetido, etc.
    */
    private final HashMap<String, CampoGestionable> listaCamposFormulario;

    //Constructor
    public GestorFormulario() {
        listaCamposFormulario = new HashMap<>();
    }
    
    /*
    Metodo para agregar campos que permitan obtener el texto con getText
    dentro de su propia caja
    */ 
    public void agregarCampoTexto( 
            javax.swing.text.JTextComponent input, 
            JLabel lblError, 
            TipoDatoFormulario tipoDato, 
            boolean esObligatorio ){
        
        // Si falta el nombre, la aplicación se detiene aquí mismo con un error en consola
        validarNombreEnComponente(input);
        
        listaCamposFormulario.put( 
                input.getName() ,
                new CampoTexto( input, lblError, tipoDato, esObligatorio)
        );
        
    }
    
    /*
    Metodo dedicada solamente para el campo confirmar contraseña
    por lo tanto necesita de un campo contraseña original para poder compararlos.
    */
    public void agregarCampoConfirmarContrasena( 
                                    javax.swing.JPasswordField inputConfirmarContrasena, 
                                    javax.swing.JPasswordField inputContrasena, 
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
    clave: idItem, valor: nombreItem, para asi en la recoleccion de datos,
    enviar el id correspondiente a la base de datos.
    */
    public void agregarCampoComboBox(
                                javax.swing.JComboBox comboBox,
                                String concepto,
                                LinkedHashMap< Integer, String > listaOpcionesConId, 
                                JLabel lblError,
                                boolean esObligatorio ){
        validarNombreEnComponente(comboBox);
        
        listaCamposFormulario.put(
                comboBox.getName(), 
                new CampoComboBox(comboBox, concepto, listaOpcionesConId, lblError, esObligatorio)
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
                "\n\n Error en agregar campo a GestorFormulario: "
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
        for(CampoGestionable campo : listaCamposFormulario.values()){
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
        for(CampoGestionable campo : listaCamposFormulario.values()){
            campo.limpiarError();
        }
    }
    
     /*
        Metodo para limpiar el contenido dentro de cada uno de los campos.

        CRÍTICO: No se puede recorrer 'listaCamposFormulario' directamente con NINGÚN bucle 
        (ni .forEach(), ni for-each clásico, ni iteradores), ya que 'campo.limpiarCampo()' 
        dispara los listeners de Swing configurados en cada campo. 
        Esos listeners desencadenan validaciones concurrentes 
        que intentan leer o modificar el mapa original al mismo tiempo, rompiendo el hilo visual 
        con un 'ConcurrentModificationException'.

        SOLUCIÓN DEFINITIVA: Se crea una copia instantánea de los campos en un ArrayList temporal. 
        El bucle recorre esta lista independiente aislada en memoria. Así, los listeners de Swing 
        pueden activarse e interactuar con el mapa original todo lo que quieran, ya que la lista 
        que se está iterando jamás sufre alteraciones.
    */
    public void limpiarCampos(){
        // Creamos la lista copia para aislar el bucle en memoria
        List<CampoGestionable> camposCopia = new ArrayList<>(listaCamposFormulario.values());
        
        // Iteramos de forma segura sobre la copia
        camposCopia.forEach(campo -> {
            campo.limpiarCampo();
        });
    }
    
    /*
    Metodo para recolectar en un diccionario clave: nameItem , valor: valorItem
    que sera destinado a enviar al controlador para entregarlo a la base de datos
    */
    public HashMap<String, String> recolectarDatos(){
        
        /* NOTA: Para que este metodo los resultados sean los esperados, es necesario
                 configurar el atributo name de cada uno de los componentes, con un nombre
                 unico y representativo que lo tiene que conocer el controlador.
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
    public void mostrarErroresExternos( HashMap<String, String> errores ){
       
        if(!errores.isEmpty()){
            
           errores.entrySet().forEach( error -> {
                if( listaCamposFormulario.containsKey( error.getKey()) ){
                    CampoGestionable campoAMostrarError = listaCamposFormulario.get(error.getKey());
                    campoAMostrarError.mostrarError(error.getValue());
                }else{
                    imprimirCampoInexistente( error.getKey() );
                }
           });
        
        }
    }
    
    /*
        Metodo para asignar en cada uno de los campos del formulario
        un valor que viene de un diccionario clave: nameItem , valor: valorItem
        que viene desde la consulta a la base de datos.
        Por lo general se pretende usar para modales que permitan editar 
        informacion de un registro de una determinada tabla.
    */
    public void asignarDatos( HashMap<String, String> datos ){
        
        if(!datos.isEmpty()){
            
           datos.entrySet().forEach( dato -> {
                if(listaCamposFormulario.containsKey(dato.getKey())){
                    CampoGestionable campo = listaCamposFormulario.get(dato.getKey());
                    campo.setValorComponente(dato.getValue());
                }else{
                    imprimirCampoInexistente( dato.getKey() );
                }
           });
        
        }
    }
    
    
    private void imprimirCampoInexistente( String nameCampo ){
        System.out.println("El campo con atributo name \"" + nameCampo + "\" No esta en el formulario");
    }
    

}

/*
    Clase utilitaria para agregar en un objeto todos los campos de un formulario
    o de una listado de informacion que se espera mostrar al usuario despues de 
    consultar la base de datos, como informacion de perfil o listas de productos
 */
package jafrinventarios.vistas.utilidades.rellenador;

import java.util.HashMap;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.text.JTextComponent;

/**
 *
 * @author JOHN FORERO
 */
public class RellenadorComponentes {
    
    /*
        Formulario contendra un diccionario de datos, donde la clave ser
        el atributo name del componente y el valor sera el componente mismo.
    */
    private final HashMap<String, ComponenteRellenable> diccionarioComponetes;
    
    //Constructor 
    public RellenadorComponentes(){
        diccionarioComponetes =  new HashMap<>();
    }
    
    
    /*
        Funcion para validar si un componente tiene asignado el atributo name con un 
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
                "\n\n Error en agregar campo a RellenadorComponentes: "
                + "\n El componente de tipo " 
                + componente.getClass().getSimpleName() 
                + " no tiene un 'name' configurado. "
                + "\n Es obligatorio asignarlo en las propiedades de la vista. \n"
            );
        }
    }
    
    
    //Metodo para agregar un componente que sea de tipo texto( ej: textField )
    public void agregarComponenteTexto(JTextComponent componenteTexto){
        
         /* 
            Si falta el nombre, 
            la aplicación se detiene aquí mismo con un error en consola,
            para que al momento de agregar un campo y ejecutar el programa
            se recuerde que es necesario asignarle el atributo y asi evitar 
            comportamientos inesperados.
         */
         validarNombreEnComponente(componenteTexto);
         
         diccionarioComponetes.put(componenteTexto.getName(), new ComponenteTexto(componenteTexto));
    }
    
    
     //Metodo para agregar un componente que sea de tipo label
    public void agregarComponenteLabel(JLabel componenteLabel){

         validarNombreEnComponente(componenteLabel);
         
         diccionarioComponetes.put(componenteLabel.getName(), new ComponenteLabel(componenteLabel));
         
    }
    
    
    /*
        TODO: Construir las clases y los metodos para campos de tipo comboBox
    */
    
    
    /*
        Se espera recibir un diccionario armado con el controlador,
        que contenga como clave el name del componente 
        y como valor el texto o el valor pertinente que se desee asignar
        al componente como clave
    */
    public void escribirEnFormulario( HashMap<String, String> datosBD){
        
        datosBD.forEach((clave,valor)-> {
              
            ComponenteRellenable campo = diccionarioComponetes.get(clave);
              
             campo.escribirEnCampo(valor);
             
        });
        
    } 
    
    
    
    
    
}

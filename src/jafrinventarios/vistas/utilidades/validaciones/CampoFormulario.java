/*
    Esta clase permite crear un objeto que contenga el input de un formulario con
    el label que mostrara el error pertinente si llega a ser necesario despues de 
    una validacion, ademas con la asignacion del TipoDatoFormulario y si esObligatorio
    se hacen las respectivas verificaciones si cumple con el tipo de dato esperado
 */
package jafrinventarios.vistas.utilidades.validaciones;

import javax.swing.text.JTextComponent;
import javax.swing.JLabel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author JOHN FORERO
 */
public class CampoFormulario extends CampoValidable{
    
    private final JTextComponent input;
    private final TipoDatoFormulario tipo;
    private final boolean esObligatorio;

    
    public CampoFormulario(JTextComponent input, JLabel lblError, TipoDatoFormulario tipo, boolean esObligatorio) {
        super(input, lblError);
        
        this.input = input;
        this.tipo = tipo;
        this.esObligatorio = esObligatorio;
        
        asignarValidacionEnTiempoReal();
    }

    
    @Override
    protected void asignarValidacionEnTiempoReal(){
    
        // Escuchamos cualquier cambio que ocurra en el contenido del input.
        // A diferencia de KeyListener, DocumentListener detecta escritura,
        // borrado, pegado, cortar, deshacer, etc.
        input.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                // Se ejecuta cuando el usuario agrega texto al campo.
                validar();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                // Se ejecuta cuando el usuario elimina texto del campo.
                validar();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Se ejecuta cuando cambian atributos del documento (por ejemplo,
                // estilos de texto). En un JTextField normalmente este método
                // no se utiliza, pero debe implementarse por la interfaz.
                validar();
            }
        });
    }
    
   
    private String obtenerTextoInput(){
        //Eliminamos espacios antes y despues del texto que esta dentro del input
        return input.getText().trim();
    }

    @Override
    protected boolean validar() {
        String textoInput = obtenerTextoInput();
        
        // ¿Está vacío?
        if(textoInput.isEmpty()){
            if( esObligatorio ){
                mostrarError("Este campo es obligatorio");
                return false;
            }
            
            // Si NO es obligatorio y está vacío, es válido
            // limpiamos el error en dado caso que se haya escrito datos y
            // despues el usuario los borro
            limpiarError();
            return true;

        }
        
        // Si llegó aquí es porque HAY texto. Evaluamos el Regex.
        if (   !textoInput.matches( tipo.getRegex() )  ) {
            mostrarError( tipo.getMensajeError() );
            return false;
        }
        
        // El campo es correcto si paso las dos validaciones
        // Limpiamos el error si ya se habia mostrado antes
        limpiarError();
        return true;
    }


}

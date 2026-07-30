/*
    Esta clase permite crear un objeto que contenga el input de un formulario con
    el label que mostrara el error pertinente si llega a ser necesario despues de 
    una validacion, ademas con la asignacion del TipoDatoFormulario y si esObligatorio
    se hacen las respectivas verificaciones si cumple con el tipo de dato esperado
 */
package jafrinventarios.controladores.validaciones;

import javax.swing.text.JTextComponent;
import javax.swing.JLabel;
import java.awt.Color;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author JOHN FORERO
 */
public class CampoFormulario {
    
    private final JTextComponent input;
    private final JLabel lblError;
    private final TipoDatoFormulario tipo;
    private final boolean esObligatorio;
    private static final String ESTILO_NORMAL_INPUT =
                            " borderColor:#777777; focusedBorderColor:#112355 ";

    
    public CampoFormulario(JTextComponent input, JLabel lblError, TipoDatoFormulario tipo, boolean esObligatorio) {
        this.input = input;
        this.lblError = lblError;
        this.tipo = tipo;
        this.esObligatorio = esObligatorio;
        
        // Configuramos el label en rojo para los errores y vacio
        if (this.lblError != null) {
            this.lblError.setForeground(new Color(179,38,30));
            this.lblError.setText(""); 
        }
        
        asignarEstilosInput(false);
        
        asignarValidacionEnTiempoReal();
        
    }
    
    private void asignarEstilosInput(boolean Error){
        
        if(Error){
            // Con FlatLaf, esto pinta el borde del input en rojo
            input.putClientProperty("JComponent.outline", "error");
        }else{
            input.putClientProperty("JComponent.outline", null); 
            input.putClientProperty(
                "FlatLaf.style",
                ESTILO_NORMAL_INPUT
            );
        
        }
        
    }
    
    public void asignarValidacionEnTiempoReal(){
    
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
        
    
    
    public boolean validar() {
        //Eliminamos espacios antes y despues del texto que esta dentro del input
        String texto = input.getText().trim();
        
        // ¿Está vacío?
        if (texto.isEmpty()) {
            if (esObligatorio) {
                mostrarError("Este campo es obligatorio");
                return false;
            } else {
                // Si NO es obligatorio y está vacío, es válido 
                limpiarError();
                return true; 
            }
        }
        
        // Si llegó aquí es porque HAY texto. Evaluamos el Regex.
        if (   !texto.matches( tipo.getRegex() )  ) {
            mostrarError( tipo.getMensajeError() );
            return false;
        }
        
        // El campo es correcto, en dado caso que sea una segunda validacion
        // Limpiamos el error si ya se habia mostrado antes
        limpiarError();
        return true;
    }

   
    private void mostrarError(String mensaje) {
        if (lblError != null) {
            lblError.setText(mensaje);
        }
        
        asignarEstilosInput(true);

    }

    
    public void limpiarError() {
        if (lblError != null) {
            lblError.setText("");
        }
        // Limpiamos el borde de FlatLaf
        asignarEstilosInput(false);
    }
    
    
}

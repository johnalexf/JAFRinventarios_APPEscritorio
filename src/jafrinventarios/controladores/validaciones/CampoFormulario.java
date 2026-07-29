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

/**
 *
 * @author JOHN FORERO
 */
public class CampoFormulario {
    
    private final JTextComponent input;
    private final JLabel lblError;
    private final TipoDatoFormulario tipo;
    private final boolean esObligatorio;

    
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
        // Con FlatLaf, esto pinta el borde del input en rojo
        input.putClientProperty("JComponent.outline", "error"); 
    }

    
    private void limpiarError() {
        if (lblError != null) {
            lblError.setText("");
        }
        // Limpiamos el borde de FlatLaf
        input.putClientProperty("JComponent.outline", null); 
    }
    
    
}

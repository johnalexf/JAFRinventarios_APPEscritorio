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
import javax.swing.InputMap;
import javax.swing.InputVerifier;
import javax.swing.JComponent;

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
        
        // Usamos InputVerifier para validar el campo despues de perder el foco
        this.input.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent c) {
                // Como InputVerifier pide retornar true o false, 
                // simplemente llamamos a nuestro propio método validar()
                // que ya hace todo el trabajo de Regex y pintar de rojo.
                return validar(); 
            }
        });
        
        input.putClientProperty(
            "FlatLaf.style",
            "borderColor:#000000;focusedBorderColor:#112355"
        );
        
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


package jafrinventarios.vistas.utilidades.formularios;

import java.util.Arrays;
import javax.swing.JLabel;
import javax.swing.JPasswordField;

/**
 *
 * @author JOHN FORERO
 */
public class CampoConfirmarContrasena extends CampoGestionable {
    
    private final JPasswordField inputConfirmarContrasena;
    private final JPasswordField inputContrasena;
    
    
    public CampoConfirmarContrasena(JPasswordField inputConfirmarContrasena, 
                                    JPasswordField inputContrasena, 
                                    JLabel lblError) {
        
        super(inputConfirmarContrasena, lblError);
        this.inputConfirmarContrasena = inputConfirmarContrasena;
        this.inputContrasena = inputContrasena;
        
        asignarValidacionEnTiempoReal();
    }
    
    @Override
    protected void asignarValidacionEnTiempoReal() {
        
        inputConfirmarContrasena.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {

            @Override
            public void insertUpdate( javax.swing.event.DocumentEvent e ) {
                // Se ejecuta cuando el usuario agrega texto al campo.
                validar();
            }

            @Override
            public void removeUpdate( javax.swing.event.DocumentEvent e ) {
                // Se ejecuta cuando el usuario elimina texto del campo.
                validar();
            }

            @Override
            public void changedUpdate( javax.swing.event.DocumentEvent e ) {
                // Se ejecuta cuando cambian atributos del documento (por ejemplo,
                // estilos de texto). En un JTextField normalmente este método
                // no se utiliza, pero debe implementarse por la interfaz.
                validar();
            }
        });
    }
    
    @Override
    protected void limpiarCampo(){
        inputConfirmarContrasena.setText("");
    }
    
    
    @Override
    public boolean validar() {
        
        char[] contrasenaAConfirmar =  inputConfirmarContrasena.getPassword();
        char[] contrasenaOriginal = inputContrasena.getPassword();
        
        // ¿Está vacío?
        if ( contrasenaAConfirmar.length == 0 ) {
            mostrarError("Este campo es obligatorio");
            return false;
        }
        
        // Si llegó aquí es porque HAY texto. Evaluamos si las contraseñas no son iguales.
        if (   !Arrays.equals(contrasenaOriginal, contrasenaAConfirmar)  ) {
            mostrarError( "La contraseña no coincide" );
            return false;
        }
        
        // El campo es correcto
        // Limpiamos el error si ya se habia mostrado antes
        limpiarError();
        return true;
    }
    
    
    @Override
    protected String getValorComponente(){
        return String.valueOf(inputConfirmarContrasena.getPassword());
    }

    @Override
    protected void setValorComponente(String valor) {
        throw new UnsupportedOperationException("El campo confirmar contraseña no soporta asignar un valor."); 
    }



    
}

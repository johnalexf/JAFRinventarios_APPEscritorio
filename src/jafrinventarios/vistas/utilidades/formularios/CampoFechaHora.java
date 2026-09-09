/*
    Esta clase permite crear un objeto que contenga el JTextComponent de un formulario 
    que representa la asignacion de una fecha y hora, ademas se incluye el label 
    que mostrara el error pertinente si llega a ser necesario despues de 
    una validacion.
 */
package jafrinventarios.vistas.utilidades.formularios;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.text.JTextComponent;
import javax.swing.JLabel;

/**
 *
 * @author JOHN FORERO
 */
public class CampoFechaHora extends CampoGestionable{
    
    private final JTextComponent inputFechaHora;
    private final TipoDatoFormulario tipo;
    private final boolean esObligatorio;

    
    public CampoFechaHora(JTextComponent inputFechaHora, JLabel lblError, boolean esObligatorio) {
        super(inputFechaHora, lblError);
        
        this.inputFechaHora = inputFechaHora;
        this.tipo = TipoDatoFormulario.FECHA_HORA;
        this.esObligatorio = esObligatorio;
        
        asignarValidacionEnTiempoReal();
    }

    
    @Override
    protected void asignarValidacionEnTiempoReal(){
    
        // Escuchamos cualquier cambio que ocurra en el contenido del inputFechaHora.
        // A diferencia de KeyListener, DocumentListener detecta escritura,
        // borrado, pegado, cortar, deshacer, etc.
        inputFechaHora.getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {

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
        inputFechaHora.setText("");
    }
    

    @Override
    protected boolean validar() {
        String texto = getValorComponente();
        
        // ¿Está vacío?
        if(texto.isEmpty()){
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
        if (   !texto.matches( tipo.getRegex() )  ) {
            mostrarError( tipo.getMensajeError() );
            return false;
        }
        
        // El campo es correcto si paso las dos validaciones
        try {
            /*
            Apesar de que el regex valida que el formato sea el adecuado, 
            usamos try catch, en dado caso que el regex haya dejado pasar
            un formato diferente que no pueda tratar LocalDateTime.parse
            */
            
            // Convertimos el texto visual a un objeto de tiempo real
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a");
            LocalDateTime fechaIngresada = LocalDateTime.parse(texto, formato);
            
            //La fecha no puede ser del futuro
            if (fechaIngresada.isAfter(LocalDateTime.now())) {
                mostrarError("La fecha no puede ser futura");
                return false;
            }

            limpiarError();
            return true;
            
        } catch (Exception e) {
            mostrarError("Formato de fecha inválido");
            return false;
        }
    }
    
    
    @Override
    protected String getValorComponente(){
        return inputFechaHora.getText().trim();
    }

    @Override
    protected void setValorComponente(String valor) {
        inputFechaHora.setText(valor);
    }


}

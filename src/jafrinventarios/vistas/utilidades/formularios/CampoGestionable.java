/*
Esta clase se encarga de recibir cualquier campo de un formulario
para aplicarle los estilos y mostrar el mensaje pertinente cuando
tenga un error, como tal esta destinada para ser heredada y poder
personalizar la respectiva validacion dependiendo del tipo de campo
 */
package jafrinventarios.vistas.utilidades.formularios;

import java.awt.Color;
import javax.swing.JComponent;
import javax.swing.JLabel;

/**
 *
 * @author JOHN FORERO
 */
public abstract class CampoGestionable {
    
    private final JComponent componente;
    private final JLabel lblError;
    private final String ESTILO_NORMAL_INPUT =
                            " borderColor:#777777; focusedBorderColor:#112355 ";

    
    public CampoGestionable(JComponent componente, JLabel lblError) {
        
        this.componente = componente;
        this.lblError = lblError;
        
        // Configuramos el label en rojo para los errores y vacio
        if (this.lblError != null) {
            this.lblError.setForeground(new Color(179,38,30));
            this.lblError.setText(""); 
        }
        
        aplicarEstiloNormalAComponente();
        
    }
    
    
    private void aplicarEstiloNormalAComponente(){
        componente.putClientProperty("JComponent.outline", null); 
        componente.putClientProperty(
            "FlatLaf.style",
            ESTILO_NORMAL_INPUT
        );
    }
    
    private void aplicarEstiloErrorAComponente(){
         // Con FlatLaf, esto pinta el borde del input en rojo
            componente.putClientProperty("JComponent.outline", "error");
    }
    
    
        protected void mostrarError(String mensaje) {
        if (lblError != null) {
            lblError.setText(mensaje);
        }
        aplicarEstiloErrorAComponente();
    }

    
    protected void limpiarError() {
        if (lblError != null) {
            lblError.setText("");
        }
        // Limpiamos el borde de FlatLaf
        aplicarEstiloNormalAComponente();
    }
    
    
    protected abstract void limpiarCampo();
    
    
    protected abstract void asignarValidacionEnTiempoReal();
    
    
    protected abstract boolean validar();
    
    
    protected abstract String getValorComponente(); 

}


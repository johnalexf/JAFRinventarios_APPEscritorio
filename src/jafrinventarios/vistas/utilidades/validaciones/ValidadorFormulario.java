/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jafrinventarios.vistas.utilidades.validaciones;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.text.JTextComponent;

/**
 *
 * @author JOHN FORERO
 */
public class ValidadorFormulario {
    
    private List<CampoValidable> listaCamposFormulario;

    public ValidadorFormulario() {
        listaCamposFormulario = new ArrayList<>();
    }
    
    public void agregarCampo( JTextComponent input, JLabel lblError, TipoDatoFormulario tipoDato, boolean esObligatorio ){
        listaCamposFormulario.add(new CampoFormulario( input, lblError, tipoDato, esObligatorio));
    }
    
    public void agregarCampoConfirmarContrasena( 
                                    JPasswordField inputConfirmarContrasena, 
                                    JPasswordField inputContrasena, 
                                    JLabel lblError ){
        listaCamposFormulario.add(new CampoConfirmarContrasena(inputConfirmarContrasena,inputContrasena,lblError));
    }
    
    public boolean validar(){
        
        boolean respuesta = true;
        
        /*Con el for each hacemos que se validen todos los campos y se muestren
          los errores correspondientes en el lblError de cada input
        */
        for(CampoValidable campo : listaCamposFormulario){
            // Cambiamos la respuesta a false si cualquiera de los campos
            // no es un dato valido
            if(!campo.validar()){
                respuesta = false;
            }
        
        }
        
        return respuesta;
    }
    
    public void limpiarErrores(){
        for(CampoValidable campo : listaCamposFormulario){
            campo.limpiarError();
        }
    }
    
    public HashMap<String, String> recolectarDatos(){
        
        /* NOTA: Para que este metodo los resultados sean los esperados, es necesario
                 configurar el atributo name de cada uno de los componentes, con un nombre
                 unico y representativo en la base de datos o el modelo.
        */
        HashMap<String, String> recolectorDatos = new HashMap<>();
        listaCamposFormulario.forEach(campo -> {
            recolectorDatos.put(campo.getNameComponente(), campo.getValorComponente());
        });
        
        return recolectorDatos;
    }
    

}

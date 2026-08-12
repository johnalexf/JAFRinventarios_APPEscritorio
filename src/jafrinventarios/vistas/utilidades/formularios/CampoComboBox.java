/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jafrinventarios.vistas.utilidades.formularios;

import java.util.HashMap;
import javax.swing.JComboBox;
import javax.swing.JLabel;

/**
 *
 * @author JOHN FORERO
 */
public class CampoComboBox extends CampoGestionable{
    
    private final JComboBox comboBox;
    /* 
    Este mapa debe venir de la base de datos, con el diccionario de valores posibles
     que puede seleccionar el usuario de la siguiente manera:
     clave: nombreItem, valor: idItem 
    */
    private final HashMap<String, Integer> listaOpcionesConId;
    private final boolean esObligatorio;
    private final String concepto;

    public CampoComboBox(
            JComboBox comboBox,
            String concepto,
            HashMap<String, Integer> listaOpcionesConId, 
            JLabel lblError,
            boolean esObligatorio ) {
        super(comboBox, lblError);
        this.comboBox = comboBox;
        this.listaOpcionesConId = listaOpcionesConId;
        this.esObligatorio = esObligatorio;
        this.concepto = concepto;
        
        cargarListaDatosDisponibles();
    }
    
    
    private void cargarListaDatosDisponibles() {
        
        //Remover los items para asignar los de la base de datos
        comboBox.removeAllItems();
        comboBox.addItem("Seleccionar " + concepto);
        
        // Llenamos el ComboBox solo con los nombres (las claves del mapa)
        listaOpcionesConId.keySet().forEach(nombreItem -> {
            comboBox.addItem(nombreItem);
        });
        
    }

    
    @Override
    protected void asignarValidacionEnTiempoReal() {
        
        comboBox.addActionListener(e -> validar());
    
    }
    
    
    @Override
    protected void limpiarCampo(){
        comboBox.setSelectedIndex(0);
    }
    

    @Override
    protected boolean validar() {
        
        if( comboBox.getSelectedIndex() == 0 && esObligatorio){
            mostrarError("Este campo es obligatorio");
            return false;
        }
        
        limpiarError();
        return true;
    }

    /*
    Como la funcion getValorComponente es usada por el validadorFormulario
        para recolectar los datos en un mapa que se envia al controlador 
        en el caso de los comboBox nos interesa enviarle el id del item que 
        se haya seleccionado, el cual esta en listaOpcionesConId
    */
    @Override
    protected String getValorComponente() {
        
        String valorTextual = (String) comboBox.getSelectedItem();
        
        return String.valueOf(listaOpcionesConId.get(valorTextual));
        
    }
    
}

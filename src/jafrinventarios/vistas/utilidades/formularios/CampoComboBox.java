
package jafrinventarios.vistas.utilidades.formularios;

import java.util.LinkedHashMap;
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
     clave: idItem, valor: nombreItem 
    */
    private final LinkedHashMap<Integer , String> listaOpcionesConId;
    /*
    Se crea un mapa invertido para cuando se necesite devolver el id del item 
    seleccionado, esto con el fin de evitar recorrer toda la listaOpcionesConId 
    buscando cual es la clave del valor seleccionado, esto facilita que se pueda
    buscar el valor con el id de la siguiente manera listaOpcionesInvertida.get(valorTextual)
    */
    private final LinkedHashMap<String , Integer> listaOpcionesInvertida;
    private final boolean esObligatorio;
    private final String concepto;

    public CampoComboBox(
            JComboBox comboBox,
            String concepto,
            LinkedHashMap<Integer , String> listaOpcionesConId, 
            JLabel lblError,
            boolean esObligatorio ) {
        super(comboBox, lblError);
        this.comboBox = comboBox;
        this.listaOpcionesConId = listaOpcionesConId;
        this.listaOpcionesInvertida = invertirLista(listaOpcionesConId);
        this.esObligatorio = esObligatorio;
        this.concepto = concepto;
        
        cargarListaDatosDisponibles();
        
        asignarValidacionEnTiempoReal();
    }
    
    
    private LinkedHashMap< String , Integer > invertirLista( LinkedHashMap<Integer , String> lista ){
         LinkedHashMap< String , Integer > listaInvertida = new LinkedHashMap<>();
         
         lista.forEach( 
            (id , nombreItem ) -> {
                listaInvertida.put( nombreItem , id);
            } 
         );
        
         return listaInvertida;
    }
    
    
    private void cargarListaDatosDisponibles() {
        
        //Remover los items para asignar los de la base de datos
        comboBox.removeAllItems();
        comboBox.addItem("Seleccionar " + concepto);
        
        // Llenamos el ComboBox solo con los nombres (los valores del mapa)
        listaOpcionesConId.values().forEach(nombreItem -> {
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
    Como la funcion getValorComponente es usada por el gestorFormulario
        para recolectar los datos en un mapa que se envia al controlador, 
        en el caso de los comboBox nos interesa enviarle el id del item que 
        se haya seleccionado, el cual esta de facil busqueda en listaOpcionesInvertida
    */
    @Override
    protected String getValorComponente() {
        
        String valorTextual = (String) comboBox.getSelectedItem();
        
        return String.valueOf(listaOpcionesInvertida.get(valorTextual));
        
    }

    @Override
    protected void setValorComponente( String idString ) {
        
        // Si el valor es nulo o vacío, seleccionamos la opción por defecto ("Seleccionar...")
        //Validar si viene vacío o nulo ANTES de intentar convertir a número
        if (idString == null || idString.trim().isEmpty() || idString.equals("-1")) {
            comboBox.setSelectedIndex(0);
            return; // Cortamos la ejecución aquí
        }
        
        //Intentar convertir en numero
        try {
            Integer id = Integer.parseInt(idString);
            
            // Buscar el id en la lista de opciones
            if(  listaOpcionesConId.containsKey(id)  ){
                comboBox.setSelectedItem( listaOpcionesConId.get(id) );
            }else{
                System.out.println("El valor para el id\"" + id + "\" No esta en el comboBox " + concepto );
            }
            
        } catch ( NumberFormatException e ) {
            //Capturar el error si envían letras o formatos inválidos
            System.err.println("Error al asignar valor en " + concepto + ": '" + idString + "' no es un número válido.");
            comboBox.setSelectedIndex(0); // Reset visual por seguridad
        }
        
    }
    
}

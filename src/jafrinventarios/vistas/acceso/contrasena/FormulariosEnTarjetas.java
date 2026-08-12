/*
 *Esta clase permite reducir el codigo para validar, recolectar y mostrar errores 
     de respuesta de la base de datos, en cada uno de los formularios que existen
     por cada tarjeta.
 */
package jafrinventarios.vistas.acceso.contrasena;

import jafrinventarios.vistas.utilidades.formularios.GestorFormulario;
import java.util.HashMap;

/**
 *
 * @author JOHN FORERO
 */
public class FormulariosEnTarjetas {
    
    private final HashMap< NombresTarjetasContrasena , GestorFormulario> diccionarioFormulariosTarjetas;

    //Constructor
    public FormulariosEnTarjetas() {
        diccionarioFormulariosTarjetas = new HashMap<>();
    }
    
    //Agregar formulario con clave la tarjeta donde se encuentra
    public void agregarFormulario(NombresTarjetasContrasena tarjeta, GestorFormulario formulario){
        diccionarioFormulariosTarjetas.put(tarjeta, formulario);
    }
    
    //Obetener el formulario que esta dentro de la tarjeta
    private GestorFormulario getFormulario( NombresTarjetasContrasena tarjeta ){
        return diccionarioFormulariosTarjetas.get(tarjeta);
    }
    
    
    public boolean validar( NombresTarjetasContrasena tarjeta ){
        GestorFormulario formulario = getFormulario(tarjeta);
        return formulario.validar();
    }
    
    
    public HashMap<String, String> recolectarDatos( NombresTarjetasContrasena tarjeta ){
        GestorFormulario formulario = getFormulario(tarjeta);
        return formulario.recolectarDatos();
    }
    
    
    public void mostrarErrorRespuestaBD(NombresTarjetasContrasena tarjeta, HashMap<String, String> erroresCamposBD ){
        GestorFormulario formulario = getFormulario(tarjeta);
        formulario.mostrarErrorRespuestaBD(erroresCamposBD);
    }
    

    
    
    
}

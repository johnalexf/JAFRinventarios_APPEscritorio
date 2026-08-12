/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jafrinventarios.vistas.acceso.contrasena;

import jafrinventarios.vistas.utilidades.formularios.GestorFormulario;
import java.util.HashMap;

/**
 *
 * @author JOHN FORERO
 */
public class FormulariosTarjetas {
    
    private final HashMap< TarjetasRecuperacion , GestorFormulario> diccionarioFormulariosTarjetas;

    //Constructor
    public FormulariosTarjetas() {
        diccionarioFormulariosTarjetas = new HashMap<>();
    }
    
    //Agregar formulario con clave la tarjeta donde se encuentra
    public void agregarFormulario(TarjetasRecuperacion tarjeta, GestorFormulario formulario){
        diccionarioFormulariosTarjetas.put(tarjeta, formulario);
    }
    
    //Obetener el formulario que esta dentro de la tarjeta
    private GestorFormulario getFormulario( TarjetasRecuperacion claveTarjeta ){
        return diccionarioFormulariosTarjetas.get(claveTarjeta);
    }
    
    
    public boolean validar( TarjetasRecuperacion claveTarjeta ){
        GestorFormulario formulario = getFormulario(claveTarjeta);
        return formulario.validar();
    }
    
    
    public HashMap<String, String> recolectarDatos( TarjetasRecuperacion claveTarjeta ){
        GestorFormulario formulario = getFormulario(claveTarjeta);
        return formulario.recolectarDatos();
    }
    
    
    public void mostrarErrorRespuestaBD(TarjetasRecuperacion claveTarjeta, HashMap<String, String> erroresCamposBD ){
        GestorFormulario formulario = getFormulario(claveTarjeta);
        formulario.mostrarErrorRespuestaBD(erroresCamposBD);
    }
    

    
    
    
}

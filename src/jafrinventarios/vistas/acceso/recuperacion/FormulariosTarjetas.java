/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jafrinventarios.vistas.acceso.recuperacion;

import jafrinventarios.vistas.utilidades.validaciones.ValidadorFormulario;
import java.util.HashMap;

/**
 *
 * @author JOHN FORERO
 */
public class FormulariosTarjetas {
    
    private final HashMap< TarjetasRecuperacion , ValidadorFormulario> diccionarioFormulariosTarjetas;

    //Constructor
    public FormulariosTarjetas() {
        diccionarioFormulariosTarjetas = new HashMap<>();
    }
    
    //Agregar formulario con clave la tarjeta donde se encuentra
    public void agregarFormulario(TarjetasRecuperacion tarjeta, ValidadorFormulario formulario){
        diccionarioFormulariosTarjetas.put(tarjeta, formulario);
    }
    
    //Obetener el formulario que esta dentro de la tarjeta
    private ValidadorFormulario getFormulario( TarjetasRecuperacion claveTarjeta ){
        return diccionarioFormulariosTarjetas.get(claveTarjeta);
    }
    
    
    public boolean validar( TarjetasRecuperacion claveTarjeta ){
        ValidadorFormulario formulario = getFormulario(claveTarjeta);
        return formulario.validar();
    }
    
    
    public HashMap<String, String> recolectarDatos( TarjetasRecuperacion claveTarjeta ){
        ValidadorFormulario formulario = getFormulario(claveTarjeta);
        return formulario.recolectarDatos();
    }
    
    
    public void mostrarErrorRespuestaBD(TarjetasRecuperacion claveTarjeta, HashMap<String, String> erroresCamposBD ){
        ValidadorFormulario formulario = getFormulario(claveTarjeta);
        formulario.mostrarErrorRespuestaBD(erroresCamposBD);
    }
    

    
    
    
}

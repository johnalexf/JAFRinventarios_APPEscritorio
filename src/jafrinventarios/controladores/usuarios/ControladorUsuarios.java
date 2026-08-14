/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jafrinventarios.controladores.usuarios;

import jafrinventarios.controladores.utilidades.ControladorBusquedaYAccionLibre;
import jafrinventarios.vistas.usuarios.UsuariosPanel;
import jafrinventarios.controladores.utilidades.FuncionesBusquedaYAccionLibre;

/**
 *
 * @author JOHN FORERO
 */
public class ControladorUsuarios {
    
    private final UsuariosPanel vista;

    public ControladorUsuarios(UsuariosPanel vista) {
        this.vista = vista;
        
        // Instanciamos el sub-controlador pasándole el panel incrustado y definiendo qué hacer
        new ControladorBusquedaYAccionLibre(   vista.getPanelBusquedaYAccionLibre(), 
                                               asignarFuncionesBusquedaYAccionLibre(),
                                               "Alias, Nombre, Correo, Telefono o Rol ",
                                               "Agregar Nuevo Usuario"
        );
    }
    
    private FuncionesBusquedaYAccionLibre asignarFuncionesBusquedaYAccionLibre(){
        return new FuncionesBusquedaYAccionLibre() {
            
            @Override
            public void ejecutarBusqueda(String terminoBusqueda) {
                System.out.println("Buscando en la BD de Usuarios el término: " + terminoBusqueda);
                // Aquí va tu lógica para filtrar la tabla de usuarios
            }

            @Override
            public void ejecutarAccionLibre() {
                System.out.println("Abriendo modal para crear nuevo usuario...");
                // Aquí llamas a tu método estático crearUsuario()
            }
            
        };
    }
}

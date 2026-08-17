/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jafrinventarios.controladores.usuarios;

import jafrinventarios.controladores.utilidades.ControladorBusquedaYAccionLibre;
import jafrinventarios.vistas.usuarios.UsuariosPanel;
import jafrinventarios.controladores.utilidades.FuncionesBusquedaYAccionLibre;
import jafrinventarios.modelos.usuarios.ModeloUsuario;
import jafrinventarios.vistas.usuarios.FilaTablaUsuarios;
import java.util.LinkedHashMap;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author JOHN FORERO
 */
public class ControladorUsuarios {
    
    private final UsuariosPanel moduloUsuarios;
    private LinkedHashMap<Integer, FilaTablaUsuarios> filasTablaUsuarios;

    public ControladorUsuarios(UsuariosPanel moduloUsuarios) {
        this.moduloUsuarios = moduloUsuarios;
        
        // Instanciamos el sub-controlador pasándole el panel incrustado y definiendo qué hacer
        new ControladorBusquedaYAccionLibre(   moduloUsuarios.getPanelBusquedaYAccionLibre(), 
                                               asignarFuncionesBusquedaYAccionLibre(),
                                               "Alias, Nombre, Correo, Telefono o Rol ",
                                               "Agregar Nuevo Usuario"
        );
        
        
        filasTablaUsuarios = new LinkedHashMap<>();
        crearFilasTablaUsuarios();
        inyectarFilasTablaUsuarios();
        inicializarEventosBotones();
        
        
    }
    
    private FuncionesBusquedaYAccionLibre asignarFuncionesBusquedaYAccionLibre(){
        return new FuncionesBusquedaYAccionLibre() {
            
            @Override
            public boolean ejecutarBusqueda(String terminoBusqueda) {
                System.out.println("Buscando en la BD de Usuarios el término: " + terminoBusqueda);
                // Aquí va tu lógica para filtrar la tabla de usuarios
                //Responde si encontro algo o no para que el controlador muestre un mensaje de error en dado caso que sea false
                return false;
                //TODO por el momento lo dejamos en false para ver el mensaje de error y hasta que se haga la respectiva busqueda
            }

            @Override
            public void ejecutarAccionLibre() {
                abrirModalCrearUsuario();
            }
            
        };
    }
    
    
    private void abrirModalCrearUsuario(){
        JFrame ventanaPadre = (JFrame) SwingUtilities.getWindowAncestor(moduloUsuarios);
        ControladorDialogoUsuarios.crearUsuario(ventanaPadre);
    }
    
    
    
    private void crearFilasTablaUsuarios(){
    
        LinkedHashMap<Integer, ModeloUsuario> listaUsuarios = new LinkedHashMap<>();
                 
         listaUsuarios.put(1, new ModeloUsuario(1, "john1", "3202173409", "john1@gmail.com", "john","", "forero", "", "Administrador", true));
         listaUsuarios.put(2, new ModeloUsuario(2, "john1", "3202173409", "john1@gmail.com", "john","", "forero", "", "Vendedor", true));
         listaUsuarios.put(3, new ModeloUsuario(3, "john1", "3202173409", "john1@gmail.com", "john","", "forero", "", "Vendedor", true));
         listaUsuarios.put(4, new ModeloUsuario(4, "john1", "3202173409", "john1@gmail.com", "john","", "forero", "", "Vendedor", true));
         listaUsuarios.put(5, new ModeloUsuario(5, "john1", "3202173409", "john1@gmail.com", "john","", "forero", "", "Vendedor", true));
         listaUsuarios.put(6, new ModeloUsuario(6, "john1", "3202173409", "john1@gmail.com", "john","", "forero", "", "Vendedor", true));
         
         listaUsuarios.forEach((Integer id, ModeloUsuario datosUsuario) ->{
             FilaTablaUsuarios filaDatosUsuarios = new FilaTablaUsuarios();
             filaDatosUsuarios.setDatos(
                     String.valueOf(  datosUsuario.getIdUsuario() ), 
                     datosUsuario.getAlias(), 
                     datosUsuario.getNombreCompleto(), 
                     datosUsuario.getCorreo(), 
                     datosUsuario.getTelefono(), 
                     datosUsuario.getRol()
             );
             filasTablaUsuarios.put(    
                     id,
                     filaDatosUsuarios
             );
         }
         );
         
    
    }
    
    private void inyectarFilasTablaUsuarios(){
        moduloUsuarios.inyectarFilasTablaUsuarios(filasTablaUsuarios);
    }
            
    
    private void inicializarEventosBotones(){
        
        filasTablaUsuarios.forEach( ( Integer id, FilaTablaUsuarios fila )->{
            fila.getBtnEditar().addActionListener(e -> 
                    ControladorDialogoUsuarios.editarOtroUsuario(getFramePadre(), id)
            );
        });
        
    }
    
    private JFrame getFramePadre(){
        return (JFrame) SwingUtilities.getWindowAncestor(moduloUsuarios);
    }
    
}

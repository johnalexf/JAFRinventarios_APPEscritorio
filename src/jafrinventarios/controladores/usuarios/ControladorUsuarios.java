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
    // hash map con las filas que representan los datos de cada usuario
    // se identifica con el id para poder acceder al boton, y tambien para cuando
    // se necesite actualizar la informacion de un item editado
    private LinkedHashMap<Integer, FilaTablaUsuarios> filasTablaUsuarios;

    public ControladorUsuarios(UsuariosPanel moduloUsuarios) {
        this.moduloUsuarios = moduloUsuarios;
        
        // Instanciamos el sub-controlador de la barra de busquedad y el boton de accion libre
        // pasándole el panel incrustado y definiendo qué hacer
        new ControladorBusquedaYAccionLibre(   moduloUsuarios.getPanelBusquedaYAccionLibre(), 
                                               asignarFuncionesBusquedaYAccionLibre(),
                                               "Alias, Nombre, Correo, Telefono o Rol ",
                                               "Agregar Nuevo Usuario"
        );
        
        
        filasTablaUsuarios = new LinkedHashMap<>();
        inicializarTablaUsuarios();
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
    
    
    // Metodo para optener cual es el frame padre de la vista JPanel modulo de usuarios
    private JFrame getFramePadre(){
        return (JFrame) SwingUtilities.getWindowAncestor(moduloUsuarios);
    }
    
    
    private void abrirModalCrearUsuario(){
        int idUsuarioCreado = ControladorDialogoUsuarios.crearUsuario( getFramePadre() );
        
        if(idUsuarioCreado != -1){
         /*   
            //TODO: pendiente pedir al modelo o al servicio la informacion del nuevo usuario
            datosUsuario 
            
            FilaTablaUsuarios filaDatosUsuario = 
                                    crearNuevaFilaTablaUsuarios(datosUsuario);
             
            agregarNuevaFilaTablaUsuarios(idUsuarioCreado, filaDatosUsuario);
            
            moduloUsuarios.inyectarNuevaFilaATablaUsuarios(filaDatosUsuario);
         */
        }
    }
    
    
    
    private void inicializarTablaUsuarios(){
    
        LinkedHashMap<Integer, ModeloUsuario> listaUsuarios = new LinkedHashMap<>();
            
        /*
        Simulacion de respuesta del servicio del modelo de usuarios
        */
         listaUsuarios.put(1, new ModeloUsuario(1, "john1", "3202173409", "john1@gmail.com", "john","", "forero", "", "Administrador", true));
         listaUsuarios.put(2, new ModeloUsuario(2, "john1", "3202173409", "john1@gmail.com", "john","", "forero", "", "Vendedor", true));
         listaUsuarios.put(3, new ModeloUsuario(3, "john1", "3202173409", "john1@gmail.com", "john","", "forero", "", "Vendedor", true));
         listaUsuarios.put(4, new ModeloUsuario(4, "john1", "3202173409", "john1@gmail.com", "john","", "forero", "", "Vendedor", true));
         listaUsuarios.put(5, new ModeloUsuario(5, "john1", "3202173409", "john1@gmail.com", "john","", "forero", "", "Vendedor", true));
         listaUsuarios.put(6, new ModeloUsuario(6, "john1", "3202173409", "john1@gmail.com", "john","", "forero", "", "Vendedor", true));
         
        
        listaUsuarios.forEach((Integer id, ModeloUsuario datosUsuario) ->{
             
            FilaTablaUsuarios filaDatosUsuario = crearNuevaFilaTablaUsuarios(datosUsuario);
             
            agregarNuevaFilaTablaUsuarios(id, filaDatosUsuario);
        }
        );
         
    
    }
    
    private FilaTablaUsuarios crearNuevaFilaTablaUsuarios( ModeloUsuario datosUsuario){
        FilaTablaUsuarios filaDatosUsuario = new FilaTablaUsuarios();
            filaDatosUsuario.setDatos(
                     String.valueOf(  datosUsuario.getIdUsuario() ), 
                     datosUsuario.getAlias(), 
                     datosUsuario.getNombreCompleto(), 
                     datosUsuario.getCorreo(), 
                     datosUsuario.getTelefono(), 
                     datosUsuario.getRol()
             );
        return filaDatosUsuario;
    }
    
    
    private void agregarNuevaFilaTablaUsuarios(int id, FilaTablaUsuarios fila){
                    filasTablaUsuarios.put(    
                     id,
                     fila
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
    
    
    private void editarUsuario(JFrame ventanaPadre, Integer idUsuario){
        
        boolean seEditoUsuario = ControladorDialogoUsuarios.editarOtroUsuario(getFramePadre(), idUsuario);
        
        if( seEditoUsuario ){
            //TODO hacer la consulta con el servicio o el modelo del usuario editado, 
            // para asignar los nuevos valores 
         /* ModeloUsuario usuarioEditado = 
            FilaTablaUsuarios filaDatosUsuario = filasTablaUsuarios.get(idUsuario);
            filaDatosUsuario.setDatos(
                     String.valueOf(  datosUsuario.getIdUsuario() ), 
                     datosUsuario.getAlias(), 
                     datosUsuario.getNombreCompleto(), 
                     datosUsuario.getCorreo(), 
                     datosUsuario.getTelefono(), 
                     datosUsuario.getRol()
             );
          */
        }
        
    }
    

}

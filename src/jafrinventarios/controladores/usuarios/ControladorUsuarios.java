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
    
    /* 
    LinkedHashMap con las filas que representan los datos de cada usuario
     se identifica con el id para poder acceder al boton, y tambien para cuando
     se necesite actualizar la informacion de un item editado
    */
    private LinkedHashMap<Integer, FilaTablaUsuarios> tablaDatosUsuarios;

     /*
    ============================================================================
                        CONSTRUCTOR PUBLICO
    ============================================================================
    */
    public ControladorUsuarios(UsuariosPanel moduloUsuarios) {
        
        this.moduloUsuarios = moduloUsuarios;
        
        /* 
        Instanciamos el sub-controlador de la barra de busquedad y el boton de accion libre
         pasándole el panel incrustado y definiendole qué hacer cuando el usuario
         desee buscar o cuando presione el boton de accion libre.
        */
        new ControladorBusquedaYAccionLibre(   
                moduloUsuarios.getPanelBusquedaYAccionLibre(), 
                asignarFuncionesBusquedaYAccionLibre(),
                "Alias, Nombre, Correo, Telefono o Rol ",
                "Agregar Nuevo Usuario"
        );
        
        
        tablaDatosUsuarios = new LinkedHashMap<>();
        estructurarTablaUsuarios(
                obtenerListaUsuariosBD("")
        );
        inyectarTablaAVista();
        
        
    }
    
    /* 
    Metodo para crear desde la interfaz FuncionesBusquedaYAccionLibre el objeto
    que contendra la asignacion que se requiere para realizar una busqueda
    o para ejecutar la accion libre que en este caso es crear un nuevo usuario.   
    */
    private FuncionesBusquedaYAccionLibre asignarFuncionesBusquedaYAccionLibre(){
        return new FuncionesBusquedaYAccionLibre() {
            
            @Override
            public boolean ejecutarBusqueda(String terminoBusqueda) {
                return procesarBusqueda(terminoBusqueda);
            }
            
            @Override
            public void limpiarBusqueda(){
                reiniciarTabla();
            }

            @Override
            public void ejecutarAccionLibre() {
                crearUsuario();
            }
            
        };
    }
    
    
     /*
    ============================================================================
                METODOS PARA EL CONTROL DE LAS FILAS DE LA TABLA
    ============================================================================
    */
        
    
    private FilaTablaUsuarios crearNuevaFilaTablaUsuarios( ModeloUsuario datosUsuario ){
        FilaTablaUsuarios filaDatosUsuario = new FilaTablaUsuarios();
        return asignarDatosAFila(filaDatosUsuario, datosUsuario);
    }
    
    
    private FilaTablaUsuarios asignarDatosAFila( FilaTablaUsuarios fila, ModeloUsuario datosUsuario ){
        fila.setDatos(
                     String.valueOf(  datosUsuario.getIdUsuario() ), 
                     datosUsuario.getAlias(), 
                     datosUsuario.getNombreCompleto(), 
                     datosUsuario.getCorreo(), 
                     datosUsuario.getTelefono(), 
                     datosUsuario.getRol()
        );
        fila.setIdentificarHabilitado( datosUsuario.getEstaHabilitado() );
        return fila;
    }
    
    
    private void agregarNuevaFilaTablaUsuarios(int id, FilaTablaUsuarios fila){
        tablaDatosUsuarios.put(  id,  fila   );
    }
    
    
    private LinkedHashMap<Integer, ModeloUsuario> obtenerListaUsuariosBD( String filtro ){
        
        LinkedHashMap<Integer, ModeloUsuario> listaUsuarios = new LinkedHashMap<>();
            
        /*
        Simulacion de respuesta del servicio del modelo de usuarios
        por el momento como no existe la funcion, asumimos que solo responde para
        cuando no hay una palabra de filtro, pero la idea es que el servicio del modelo
        pueda buscar y devolver una lista dependiendo de dicha busquedad
        */
        if( filtro.equals("") ){
            listaUsuarios.put(1, new ModeloUsuario(1, "john1", "3202173409", "john1@gmail.com", "john","", "forero", "", "Administrador", true));
            listaUsuarios.put(2, new ModeloUsuario(2, "john1", "3202173409", "john1@gmail.com", "john","", "forero", "", "Vendedor", true));
            listaUsuarios.put(3, new ModeloUsuario(3, "john1", "3202173409", "john1@gmail.com", "john","", "forero", "", "Vendedor", true));
            listaUsuarios.put(4, new ModeloUsuario(4, "john1", "3202173409", "john1@gmail.com", "john","", "forero", "", "Vendedor", true));
            listaUsuarios.put(5, new ModeloUsuario(5, "john1", "3202173409", "john1@gmail.com", "john","", "forero", "", "Vendedor", true));
            listaUsuarios.put(6, new ModeloUsuario(6, "john1", "3202173409", "john1@gmail.com", "john","", "forero", "", "Vendedor", false));
        }
        
        return listaUsuarios;
         
    }
    
    
    private void estructurarTablaUsuarios( LinkedHashMap<Integer, ModeloUsuario> listaUsuariosBD ){
         
        listaUsuariosBD.forEach((Integer id, ModeloUsuario datosUsuario) ->{
             
                FilaTablaUsuarios filaDatosUsuario = crearNuevaFilaTablaUsuarios(datosUsuario);
                
                inicializarEventoBotonEditar(id, filaDatosUsuario);

                agregarNuevaFilaTablaUsuarios(id, filaDatosUsuario);
            }
        );
         
    }
    
    
    private void limpiarTablaUsuarios(){
        tablaDatosUsuarios.clear();
    }
    
    
    private void reiniciarTabla(){
        limpiarTablaUsuarios();
        estructurarTablaUsuarios(
                obtenerListaUsuariosBD("")
        );
        inyectarTablaAVista();
    }
    
    
    private boolean procesarBusqueda( String filtro ){
        System.out.println("Buscando en la BD de Usuarios el término: " + filtro);
        
        LinkedHashMap<Integer, ModeloUsuario> listaUsuarios;
        
        listaUsuarios = obtenerListaUsuariosBD( filtro );
        
        if( listaUsuarios.isEmpty() ){
            return false;
        }else{
            limpiarTablaUsuarios();
            estructurarTablaUsuarios( listaUsuarios );
            inyectarTablaAVista();
            return true;
        }
  
    }
    
            
    private void inyectarTablaAVista(){
        moduloUsuarios.inyectarFilasTablaUsuarios(tablaDatosUsuarios);
    }
    
    
    private void inyectarNuevaFilaAVista(FilaTablaUsuarios fila){
        moduloUsuarios.inyectarNuevaFilaATablaUsuarios(fila);  
    }
    

    /*
    ============================================================================
    ============================================================================
    */
    
    
    
    /*
    ======================================================================================
     METODO PARA ASIGNAR EL LISTENER AL BOTON DE EDITAR USUARIO DE UNA FilaTablaUsuarios
    ======================================================================================
    */
        
    private void inicializarEventoBotonEditar(Integer id, FilaTablaUsuarios fila){
        fila.getBtnEditar().addActionListener(e -> editarUsuario(id));
    }
    
    
    
    /*
    ============================================================================
                METODOS PARA LAS ACCIONES (CREAR, EDITAR Y BUSCAR)
    ============================================================================
    */
    
    
    // Metodo para optener cual es el frame padre de la vista JPanel modulo de usuarios
    private JFrame getFramePadre(){
        return (JFrame) SwingUtilities.getWindowAncestor(moduloUsuarios);
    }
    
    
    private void crearUsuario(){
        /* 
        Con el metodo estatico ControladorDialogoUsuarios.crearUsuario se abre
         un modal para crear un usuario, si se crea este devuelve el Id de
         dicho usuario, en dado caso que no se cree se espera recibir un -1
        */
        int idUsuarioCreado = ControladorDialogoUsuarios.crearUsuario( getFramePadre() );
        
        if(idUsuarioCreado != -1){
         /*   
            //TODO: pendiente pedir al modelo o al servicio la informacion del nuevo usuario
            datosUsuario 
            
            FilaTablaUsuarios filaDatosUsuario = crearNuevaFilaTablaUsuarios(datosUsuario);
             
            agregarNuevaFilaTablaUsuarios(id, filaDatosUsuario);
            
            inicializarEventoBotonEditar(id, fila);
            
            inyectarNuevaFilaAVista( filaDatosUsuario );
         */
        }
    }
    
 
    private void editarUsuario(Integer idUsuario){
        
        boolean seEditoUsuario = 
                ControladorDialogoUsuarios.editarOtroUsuario(
                        getFramePadre(), idUsuario
                );
        
        if( seEditoUsuario ){
            //TODO hacer la consulta con el servicio o el modelo del usuario editado, 
            // para asignar los nuevos valores 
         /* ModeloUsuario usuarioEditado = funcion del modelo que retorna los datos del usuario
            FilaTablaUsuarios filaDatosUsuario = filasTablaUsuarios.get(idUsuario);
            asignarDatosAFila( filaDatosUsuario, datosUsuario );
          */
        }
        
    }
    

}

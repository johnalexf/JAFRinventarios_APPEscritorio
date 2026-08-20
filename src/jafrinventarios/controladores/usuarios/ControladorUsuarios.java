/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jafrinventarios.controladores.usuarios;

import jafrinventarios.DTOs.usuarios.DTOUsuarioTabla;
import jafrinventarios.controladores.utilidades.ControladorBusquedaYAccionLibre;
import jafrinventarios.vistas.usuarios.UsuariosPanel;
import jafrinventarios.controladores.utilidades.FuncionesBusquedaYAccionLibre;
import jafrinventarios.servicios.usuarios.ServicioUsuarios;
import jafrinventarios.vistas.usuarios.FilaTablaUsuarios;
import java.util.LinkedHashMap;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author JOHN FORERO
 */
public class ControladorUsuarios {
    
    
    private final UsuariosPanel moduloUsuarios;
    
    private final ServicioUsuarios servicioUsuarios;
    
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
    public ControladorUsuarios (UsuariosPanel moduloUsuarios , ServicioUsuarios servicioUsuarios) {
        
        this.moduloUsuarios = moduloUsuarios;
        this.servicioUsuarios = servicioUsuarios;
        
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
        estructurarTablaUsuarios( obtenerTodosLosUsuario() );
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
                METODOS PARA CONSULTAR AL SERVICIO
    ============================================================================
    */
        
    private List<DTOUsuarioTabla> obtenerTodosLosUsuario() {
       return servicioUsuarios.obtenerTodosLosUsuarios();  
    }
        
    private List<DTOUsuarioTabla> obtenerListaUsuariosPorFiltro( String filtro ){
       return servicioUsuarios.obtenerListaUsuariosPorFiltro(filtro);  
    }
    
    private DTOUsuarioTabla obtenerDatosUsuario( int idUsuario ){
        return servicioUsuarios.obtenerDatosDTOUsuario(idUsuario);
    }
    
    
     /*
    ============================================================================
                METODOS PARA EL CONTROL DE LAS FILAS DE LA TABLA
    ============================================================================
    */
       
    
    private FilaTablaUsuarios asignarDatosAFila( FilaTablaUsuarios fila, DTOUsuarioTabla datosUsuario ){
        fila.setDatos(
                     String.valueOf(  datosUsuario.getIdUsuario() ), 
                     datosUsuario.getAliasUsuario(), 
                     datosUsuario.getNombreCompletoUsuario(), 
                     datosUsuario.getCorreoUsuario(), 
                     datosUsuario.getTelefonoUsuario(), 
                     datosUsuario.getNombreRolUsuario()
        );
        fila.setEstadoVisual( datosUsuario.estaHabilitado() );
        return fila;
    }
    
    
    private FilaTablaUsuarios crearNuevaFilaTablaUsuarios( DTOUsuarioTabla datosUsuario ){
        FilaTablaUsuarios filaDatosUsuario = new FilaTablaUsuarios();
        return asignarDatosAFila(filaDatosUsuario, datosUsuario);
    }
    
    
    private void agregarNuevaFilaTablaUsuarios(int id, FilaTablaUsuarios fila){
        tablaDatosUsuarios.put(  id,  fila   );
    }
    
    
    private void estructurarTablaUsuarios( List<DTOUsuarioTabla> listaUsuariosBD ){
         
        listaUsuariosBD.forEach( (DTOUsuarioTabla datosUsuario) ->{
             
                FilaTablaUsuarios filaDatosUsuario = crearNuevaFilaTablaUsuarios(datosUsuario);
                
                inicializarEventoBotonEditar( datosUsuario.getIdUsuario() , filaDatosUsuario);

                agregarNuevaFilaTablaUsuarios( datosUsuario.getIdUsuario() , filaDatosUsuario);
            }
        );
         
    }
    
    
    private void limpiarTablaUsuarios(){
        tablaDatosUsuarios.clear();
    }
    
    
    private void reiniciarTabla(){
        limpiarTablaUsuarios();
        estructurarTablaUsuarios( obtenerTodosLosUsuario() );
        inyectarTablaAVista();
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
        int idUsuarioCreado = ControladorDialogoUsuarios.crearUsuario( getFramePadre() , servicioUsuarios);
        
        //TODO hasta no tener la conexion a la base de datos esta linea la mantenemos 
        idUsuarioCreado = -1;
        
        if(idUsuarioCreado != -1){
           
            DTOUsuarioTabla datosUsuario =  obtenerDatosUsuario( idUsuarioCreado );
            
            FilaTablaUsuarios filaDatosUsuario = crearNuevaFilaTablaUsuarios(datosUsuario);
             
            agregarNuevaFilaTablaUsuarios(idUsuarioCreado, filaDatosUsuario);
            
            inicializarEventoBotonEditar(idUsuarioCreado, filaDatosUsuario);
            
            inyectarNuevaFilaAVista( filaDatosUsuario );
 
        }
    }
    
 
    private void editarUsuario(Integer idUsuario){
        
        boolean seEditoUsuario = 
                ControladorDialogoUsuarios.editarOtroUsuario(
                        getFramePadre(), idUsuario, servicioUsuarios
                );
        
        //TODO hasta que se tenga la conexion a la base de datos mantenenmos esta linea
        seEditoUsuario = false;
        
        if( seEditoUsuario ){
            
            DTOUsuarioTabla datosUsuario =  obtenerDatosUsuario( idUsuario );

            FilaTablaUsuarios filaDatosUsuario = tablaDatosUsuarios.get(idUsuario);
            
            asignarDatosAFila( filaDatosUsuario, datosUsuario );
        
        }
        
    }
    
    
    private boolean procesarBusqueda( String filtro ){

        List<DTOUsuarioTabla> listaUsuarios = obtenerListaUsuariosPorFiltro( filtro );
        
        if( listaUsuarios.isEmpty() ){
            return false;
        }else{
            limpiarTablaUsuarios();
            estructurarTablaUsuarios( listaUsuarios );
            inyectarTablaAVista();
            return true;
        }
  
    }
    

}

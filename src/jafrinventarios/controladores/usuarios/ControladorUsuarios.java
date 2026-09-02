
package jafrinventarios.controladores.usuarios;

import jafrinventarios.controladores.utilidades.ControladorBusquedaYAccionLibre;
import jafrinventarios.controladores.utilidades.FuncionesBusquedaYAccionLibre;
import jafrinventarios.DTOs.usuarios.DTOUsuarioTabla;
import jafrinventarios.controladores.utilidades.ResultadoDialogo;
import jafrinventarios.servicios.usuarios.ServicioUsuarios;
import jafrinventarios.vistas.usuarios.FilaTablaUsuarios;
import jafrinventarios.vistas.usuarios.UsuariosPanel;

import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author JOHN FORERO
 */
public class ControladorUsuarios {
    
    private final UsuariosPanel panelUsuarios;
    private final ServicioUsuarios servicioUsuarios;
    
    /* 
    LinkedHashMap con las filas que representan los datos de cada usuario
     se identifica con el id para poder acceder al boton, y tambien para cuando
     se necesite actualizar la informacion de un item editado
    */
    private LinkedHashMap<Integer, FilaTablaUsuarios> diccionarioUsuarios;

     /*
    ============================================================================
                        CONSTRUCTOR PUBLICO
    ============================================================================
    */
    public ControladorUsuarios( UsuariosPanel panelUsuarios , ServicioUsuarios servicioUsuarios) {
        
        this.panelUsuarios = panelUsuarios;
        this.servicioUsuarios = servicioUsuarios;
        
        /* 
        Instanciamos el sub-controlador de la barra de busquedad y el boton de accion libre
         pasándole el panel incrustado y definiendole qué hacer cuando el usuario
         desee buscar o cuando presione el boton de accion libre.
        */
        new ControladorBusquedaYAccionLibre(   
                panelUsuarios.getPanelBusquedaYAccionLibre(), 
                asignarFuncionesBusquedaYAccionLibre(),
                "Alias, Nombre, Correo, Telefono o Rol ",
                "Agregar Nuevo Usuario"
        );
        
        
        diccionarioUsuarios = new LinkedHashMap<>();
        mostrarTodosLosUsuarios();
        
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
                diccionarioUsuarios.clear();
                mostrarTodosLosUsuarios();
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
        
    private List<DTOUsuarioTabla> obtenerTodosLosUsuarios() throws Exception{
       return servicioUsuarios.obtenerTodosLosUsuarios();  
    }
        
    private List<DTOUsuarioTabla> obtenerListaUsuariosPorFiltro( String filtro ) throws Exception{
       return servicioUsuarios.obtenerListaUsuariosPorFiltro(filtro);  
    }
    
    private DTOUsuarioTabla obtenerDatosUsuario( int idUsuario ) throws Exception{
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
        fila.setEstadoVisual( datosUsuario.isHabilitado() );
        return fila;
    }
    
    
    private FilaTablaUsuarios crearNuevaFila( DTOUsuarioTabla datosUsuario ){
        FilaTablaUsuarios filaDatosUsuario = new FilaTablaUsuarios();
        return asignarDatosAFila(filaDatosUsuario, datosUsuario);
    }
    
    
    private void agregarFilaADiccionario(int id, FilaTablaUsuarios fila){
        diccionarioUsuarios.put(  id,  fila   );
    }
    
    
    private void estructurarDiccionario( List<DTOUsuarioTabla> listaUsuarios ){
         
        listaUsuarios.forEach( usuario ->{
                FilaTablaUsuarios fila = crearNuevaFila(usuario);
                inicializarBotonEditar( usuario.getIdUsuario() , fila);
                agregarFilaADiccionario( usuario.getIdUsuario() , fila);
            }
        );
         
    }

    
    private void mostrarTodosLosUsuarios(){
        try {
            estructurarDiccionario( obtenerTodosLosUsuarios() );
            panelUsuarios.inyectarFilas(diccionarioUsuarios);
        } catch (Exception e) {
            panelUsuarios.mostrarModalError(e.getMessage());
        }

    }
    
    
    /*
    ======================================================================================
     METODO PARA ASIGNAR EL LISTENER AL BOTON DE EDITAR USUARIO DE UNA FilaTablaUsuarios
    ======================================================================================
    */
        
    private void inicializarBotonEditar(Integer id, FilaTablaUsuarios fila){
        fila.getBtnEditar().addActionListener(e -> editarUsuario(id));
    }
    
    
    
    /*
    ============================================================================
                METODOS PARA LAS ACCIONES (CREAR, EDITAR Y BUSCAR)
    ============================================================================
    */
    
    
    private void crearUsuario(){
        /* 
        Con el metodo estatico ControladorDialogoUsuarios.crearUsuario se abre
         un modal para crear un usuario, si se crea este devuelve el Id de
         dicho usuario, en dado caso que no se cree se espera recibir un -1
        */
        int idUsuarioCreado = 
                ControladorDialogoUsuarios.crearUsuario( 
                        panelUsuarios.getVentanaPadre() , 
                        servicioUsuarios);
        
        
        if(idUsuarioCreado != -1){
            
            try {
                DTOUsuarioTabla datosUsuario =  obtenerDatosUsuario( idUsuarioCreado );
                FilaTablaUsuarios fila = crearNuevaFila(datosUsuario);
                agregarFilaADiccionario(idUsuarioCreado, fila);
                inicializarBotonEditar(idUsuarioCreado, fila);
                panelUsuarios.inyectarNuevaFila( fila );
            }catch (Exception e) {
                panelUsuarios.mostrarModalError(e.getMessage());
            }

        }
    }
    
 
    private void editarUsuario(Integer idUsuario){
        
        ResultadoDialogo resultadoOperacion = 
                ControladorDialogoUsuarios.editarOtroUsuario(
                    panelUsuarios.getVentanaPadre() , idUsuario, servicioUsuarios
                );
        
        if( resultadoOperacion == ResultadoDialogo.ACTUALIZADO ){
            
            try {
                DTOUsuarioTabla datosUsuario =  obtenerDatosUsuario( idUsuario );
                FilaTablaUsuarios fila = diccionarioUsuarios.get(idUsuario);
                asignarDatosAFila( fila, datosUsuario );
            }catch (Exception e) {
                panelUsuarios.mostrarModalError(e.getMessage());
            }
        }
        
        if( resultadoOperacion == ResultadoDialogo.ELIMINADO ){
            FilaTablaUsuarios filaDatosUsuario = diccionarioUsuarios.get(idUsuario);
            panelUsuarios.eliminarFila( filaDatosUsuario );
            diccionarioUsuarios.remove(idUsuario);
        }
        
        
    }
    
    
    private boolean procesarBusqueda( String filtro ){

        try {
            List<DTOUsuarioTabla> listaUsuarios = obtenerListaUsuariosPorFiltro( filtro );
        
            if( listaUsuarios.isEmpty() ){
                /*
                  En ControladorBusquedaYAccionLibre, esta centralizado para que se 
                    muestre un mensaje de error si no se encontro ningun filtro
                */
                return false;
            }else{
                diccionarioUsuarios.clear();
                estructurarDiccionario( listaUsuarios );
                panelUsuarios.inyectarFilas( diccionarioUsuarios );
                return true;
            }
        } catch (Exception e) {
            panelUsuarios.mostrarModalError(e.getMessage());
            return false;
        }

    }
    

}

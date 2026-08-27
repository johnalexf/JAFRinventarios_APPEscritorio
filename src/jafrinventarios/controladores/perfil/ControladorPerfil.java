
package jafrinventarios.controladores.perfil;

import jafrinventarios.DTOs.usuarios.DTOUsuarioTabla;
import jafrinventarios.controladores.ControladorNavegacionGlobal;
import jafrinventarios.controladores.usuarios.ControladorDialogoUsuarios;
import jafrinventarios.controladores.utilidades.ResultadoDialogo;
import jafrinventarios.modelos.ModeloSesionUsuario;
import jafrinventarios.servicios.usuarios.ServicioUsuarios;
import jafrinventarios.vistas.perfil.PerfilPanel;
import java.util.HashMap;

/**
 *
 * @author JOHN FORERO
 */
public class ControladorPerfil {
    
    private PerfilPanel moduloPerfil;
    
    private ServicioUsuarios servicioUsuarios;
 
    /*
    ============================================================================
                        CONSTRUCTOR PUBLICO
    ============================================================================
    */
    public ControladorPerfil(PerfilPanel moduloPerfil, ServicioUsuarios servicioUsuarios ) {
        this.moduloPerfil = moduloPerfil;
        this.servicioUsuarios = servicioUsuarios;
        
        cargarDatosPerfil( obtenerDatosPerfil() );
        
        configurarEventosBotones();
    }
    
    
    /*
    ============================================================================
                METODOS PARA CONSULTAR A LOS SERVICIOS
    ============================================================================
    */
    
    private DTOUsuarioTabla obtenerDatosDTOUsuario( int idUsuario ) throws Exception{
        return servicioUsuarios.obtenerDatosDTOUsuario(idUsuario);
    }
    
    private String obtenerCodigoRegistroVendedor() throws Exception{
        return servicioUsuarios.obtenerCodigoRegistroVendedor();
    }
    
    
    /*
    ============================================================================
            METODOS CONFIGURACION INICIAL PLASMAR INFORMACION DE PERFIL
    ============================================================================
    */
    
    
    private HashMap<String, String> obtenerDatosPerfil(){
        
        HashMap<String, String> datosPerfilEmpaquetados = new HashMap<>();
        try {
            DTOUsuarioTabla datosPerfil = obtenerDatosDTOUsuario( 
                    ModeloSesionUsuario.getInstancia().getIdUsuario()
            );

            datosPerfilEmpaquetados.put("nombreEmpresa", ModeloSesionUsuario.getInstancia().getNombreEmpresa() );
            datosPerfilEmpaquetados.put("nombreUsuario", datosPerfil.getNombreCompletoUsuario() );
            datosPerfilEmpaquetados.put("alias", datosPerfil.getAliasUsuario() );
            datosPerfilEmpaquetados.put("correo", datosPerfil.getCorreoUsuario() );
            datosPerfilEmpaquetados.put("telefono", datosPerfil.getTelefonoUsuario() );

            if( ModeloSesionUsuario.getInstancia().isAdministrador() ){
                String codigo = obtenerCodigoRegistroVendedor();
                datosPerfilEmpaquetados.put("codigo", codigo);
            }
        }catch (Exception e) {
             moduloPerfil.mostrarAlertaError( "Error", e.getMessage());
        }
    
        return datosPerfilEmpaquetados;
    }
    
    
    private void cargarDatosPerfil( HashMap<String, String> datosPerfilEmpaquetados ){
        moduloPerfil.escribirDatos( datosPerfilEmpaquetados );
    }
    
    
    /*
    ============================================================================
                            METODO EVENTOS BOTONES
    ============================================================================
    */
    private void configurarEventosBotones(){
        moduloPerfil.getBtnCerrarSesion().addActionListener(e -> procesarCierreSesion());
        moduloPerfil.getBtnEditarUsuario().addActionListener(e -> procesarEdicionUsuario());
    }
    
    
    private void procesarCierreSesion(){
        ModeloSesionUsuario.getInstancia().cerrarSesion();
        ControladorNavegacionGlobal.getInstancia().iniciarPantallaAcceso();
    }
    
    
    private void procesarEdicionUsuario(){
              
        ResultadoDialogo  resultadoDialogo = ControladorDialogoUsuarios.editarPerfil(
                moduloPerfil.getVentanaPadre(), new ServicioUsuarios()
        );
        
        if( resultadoDialogo == ResultadoDialogo.ACTUALIZADO ){
            cargarDatosPerfil( obtenerDatosPerfil() );
        }

    }
    
}

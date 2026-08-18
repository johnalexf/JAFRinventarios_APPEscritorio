/*
 Este ControladorBusquedaYAccionLibre es quien activa los eventos de escucha 
 del boton accion libre y input de accion libre de PanelBusquedaYAccionLibre
 sin necesidad de que dentro de este controlador, se declare la logica de
 busqueda y accion libre, por tanto depende del objeto creado apartir de la interfaz
 FuncionesBusquedaYAccionLibre

 ESTA FUE DISEÑADA PARA CENTRALIZAR LA ACCION EN COMUN DEL BUSCADOR EN TODOS LOS
MODULOS DONDE SE UTILICE, EN DONDE CORRESPONDE A LO SIGUIENTE:
ACTIVAR LA ESCUCHA CUANDO SE PRESIONE ENTER DENTRO DEL BUSCADOR
ACTIVAR LA ESCUCHA CUANDO SE OPRIMA EN EL BOTON DE BUSCAR
SER EL FILTRO QUE DETECTE SI LA BUSQUEDA ESTA VACIA O CON ESPACIO

ASI POR MEDIO DE LA CREACION DEL OBJETO DE LA INTERFAZ FuncionesBusquedaYAccionLibre
ES COMO SE DELEGA LA VERDADERA ACCION DE BUSCAR Y DEL BOTON btnAccionLibre
 */
package jafrinventarios.controladores.utilidades;

import jafrinventarios.vistas.utilidades.componentes.PanelBusquedaYAccionLibre;
import jafrinventarios.vistas.utilidades.dialogos.DialogoMensajePersonalizado;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author JOHN FORERO
 */
public class ControladorBusquedaYAccionLibre {
    
    private final PanelBusquedaYAccionLibre panelBusquedaYAccion;
    private final FuncionesBusquedaYAccionLibre definidorFunciones;

    
    /*
    ============================================================================
                        CONSTRUCTOR PUBLICO
    ============================================================================
    */
    public ControladorBusquedaYAccionLibre( PanelBusquedaYAccionLibre panelBusquedaYAccion, 
                                            FuncionesBusquedaYAccionLibre definidorFunciones,
                                            String opcionesBusqueda,
                                            String textoBotonAccionLibre
                                            ) {
        
        this.panelBusquedaYAccion = panelBusquedaYAccion;
        this.definidorFunciones = definidorFunciones;
        
        asignarTextoAComponentes(opcionesBusqueda, textoBotonAccionLibre);
        
        inicializarEventos();
    }
    
    
    private void asignarTextoAComponentes(String opcionesBusqueda, String textoBotonAccionLibre){
        panelBusquedaYAccion.setPlaceholderInputBusqueda( opcionesBusqueda );
        panelBusquedaYAccion.setTextBtnAccionLibre( textoBotonAccionLibre );
    }

    
    private void inicializarEventos() {
        // Escuchar el Enter en el campo de texto
        panelBusquedaYAccion.getInputBusqueda().addActionListener(e -> validarYEjecutarBusqueda());

        // Escuchar el clic en el botón de la lupa
        panelBusquedaYAccion.getBtnBuscar().addActionListener(e -> validarYEjecutarBusqueda());
        
        // Escuchar el clic en el boton de accion libre para una accion destinada desde el controlador principal
        panelBusquedaYAccion.getBtnAccionLibre().addActionListener(e -> definidorFunciones.ejecutarAccionLibre());
    }

    
    private void validarYEjecutarBusqueda() {
        
        String termino = panelBusquedaYAccion.getInputBusqueda().getText().trim();
        
        // Validamos que no esté vacío (el placeholder nativo de FlatLaf no interfiere aquí)
        if (!termino.isEmpty()) {
            // Como la busqueda es valida (No es vacio y no son solo espacios)
            // Ejecuta la accion declarada en el controlador principal
            
            //TODO: Cuando el usuario realice una busqueda exitosa y desee cancelarla
            //Debe existir un boton "X" para devolverse a mostrar toda la lista completa
            //pendiente para cuando se pueda hacer una consulta para validar su correcto funcionamiento
            if( !definidorFunciones.ejecutarBusqueda(termino) ){
                
                JFrame ventanaPadre = (JFrame) SwingUtilities.getWindowAncestor(panelBusquedaYAccion);
                DialogoMensajePersonalizado.mostrarError( 
                        ventanaPadre , 
                        "Error en la búsqueda", 
                        "No se encontro ninguna relación con el texto " + termino
                        + "\n por favor intente con otro texto."
                );
                
            }
        }
        
    }
    
    
    
}

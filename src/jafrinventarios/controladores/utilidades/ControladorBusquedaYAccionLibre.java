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

/**
 *
 * @author JOHN FORERO
 */
public class ControladorBusquedaYAccionLibre {
    
    private final PanelBusquedaYAccionLibre panelBusquedaYAccion;
    private final FuncionesBusquedaYAccionLibre definidorFunciones;

    private final boolean mostrarBoton;
    
    /*
    ============================================================================
                        CONSTRUCTOR PUBLICO
    ============================================================================
    */
    public ControladorBusquedaYAccionLibre( PanelBusquedaYAccionLibre panelBusquedaYAccion, 
                                            FuncionesBusquedaYAccionLibre definidorFunciones,
                                            String opcionesBusqueda,
                                            String textoBotonAccionLibre,
                                            boolean mostrarBoton
                                            ) {
        
        this.panelBusquedaYAccion = panelBusquedaYAccion;
        this.definidorFunciones = definidorFunciones;
        this.mostrarBoton = mostrarBoton;
        
        if(!mostrarBoton) panelBusquedaYAccion.ocultarBoton();
        
        asignarTextoAComponentes(opcionesBusqueda, textoBotonAccionLibre);
        
        inicializarEventos();
    }
    
    
    private void asignarTextoAComponentes(String opcionesBusqueda, String textoBotonAccionLibre){
        panelBusquedaYAccion.setPlaceholderInputBusqueda( opcionesBusqueda );
        if(mostrarBoton)
            panelBusquedaYAccion.setTextBtnAccionLibre( textoBotonAccionLibre );
    }

    
    private void inicializarEventos() {
        // Escuchar el Enter en el campo de texto
        panelBusquedaYAccion.getInputBusqueda().addActionListener(e -> validarYEjecutarBusqueda());

        // Escuchar el clic en el botón de la lupa
        panelBusquedaYAccion.getBtnBuscar().addActionListener(e -> validarYEjecutarBusqueda());
        
        // Escuchar el clic en el boton de limpiar busqueda
        panelBusquedaYAccion.getBtnLimpiarBusqueda().addActionListener( e -> ejecutarLimpiezaDeBusqueda() );
        
        // Escuchar el clic en el boton de accion libre para una accion destinada desde el controlador principal
        if(mostrarBoton)
            panelBusquedaYAccion.getBtnAccionLibre().addActionListener(e -> definidorFunciones.ejecutarAccionLibre());
    }

    
    private void validarYEjecutarBusqueda() {
        
        String termino = panelBusquedaYAccion.getTextoLimpioInputBusqueda();
        
        // Validamos que no esté vacío (el placeholder nativo de FlatLaf no interfiere aquí)
        if (!termino.isEmpty()) {
            // Como la busqueda es valida ( No es vacio )
            // Ejecuta la accion declarada en el controlador principal

            if( !definidorFunciones.ejecutarBusqueda(termino) ){
                
                panelBusquedaYAccion.mostrarAlertaErrorBusqueda(termino);
                
                if( panelBusquedaYAccion.getIsVisibleBtnLimpiar() ){
                    ejecutarLimpiezaDeBusqueda();
                }else{
                    panelBusquedaYAccion.setLimpiarInputBusqueda();
                }
                
            }else{
                panelBusquedaYAccion.setVisibilidadBtnLimpiar(true);
            }
        }
        
    }
    
    
    private void ejecutarLimpiezaDeBusqueda(){
        definidorFunciones.limpiarBusqueda();
        panelBusquedaYAccion.setVisibilidadBtnLimpiar(false);
        panelBusquedaYAccion.setLimpiarInputBusqueda();
    }
    
    
    
}

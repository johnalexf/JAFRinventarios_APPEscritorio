/*
 Este controladorBusquedaYRegistro es quien activa los eventos de escucha 
 del boton agregar nuevo item y buscar coincidencias de PanelBusquedaYRegistro
 sin necesidad de que dentro de dicho este, se declare la logica de
 busqueda y agregar, por tanto depende del objeto creado apartir de la interfaz
 AccionesBusquedaYRegistro

 ESTA FUE DISEÑADA PARA CENTRALIZAR LA ACCION EN COMUN DEL BUSCADOR EN TODOS LOS
    MODULOS DONDE SE UTILICE, EN DONDE CORRESPONDE A LO SIGUIENTE
ACTIVAR LA ESCUCHA CUANDO SE PRESIONE ENTER DENTRO DEL BUSCADOR
ACTIVAR LA ESCUCHA CUANDO SE OPRIMA EN EL BOTON DE BUSCAR
SER EL FILTRO QUE DETECTE SI LA BUSQUEDA ESTA VACIA O CON ESPACIO

ASI POR MEDIO DE LA CREACION DEL OBJETO DE LA INTERFAZ ACCIONESBUSQUEDAYREGISTRO
ES COMO SE DELEGA LA VERDADERA ACCION DE BUSCAR Y DE CREAR UN NUEVO ITEM
 */
package jafrinventarios.controladores.utilidades;

import jafrinventarios.vistas.utilidades.componentes.PanelBusquedaYRegistro;

/**
 *
 * @author JOHN FORERO
 */
public class ControladorBusquedaYRegistro {
    private final PanelBusquedaYRegistro panelBusquedaYCreacion;
    private final AccionesBusquedaYRegistro definidorAcciones;

    public ControladorBusquedaYRegistro( PanelBusquedaYRegistro panelBusquedaYCreacion1, 
                                         AccionesBusquedaYRegistro definidorAcciones) {
        this.panelBusquedaYCreacion = panelBusquedaYCreacion1;
        this.definidorAcciones = definidorAcciones;
        inicializarEventos();
    }

    private void inicializarEventos() {
        // Escuchar el Enter en el campo de texto
        panelBusquedaYCreacion.getInputBusqueda().addActionListener(e -> validarYEjecutarBusqueda());

        // Escuchar el clic en el botón de la lupa
        panelBusquedaYCreacion.getBtnBuscar().addActionListener(e -> validarYEjecutarBusqueda());
        
        // Escuchar el clic en Agregar Nuevo
        panelBusquedaYCreacion.getBtnAgregarNuevo().addActionListener(e -> definidorAcciones.agregarNuevoItem());
    }

    private void validarYEjecutarBusqueda() {
        String termino = panelBusquedaYCreacion.getInputBusqueda().getText().trim();
        
        // Validamos que no esté vacío (el placeholder nativo de FlatLaf no interfiere aquí)
        if (!termino.isEmpty()) {
            // Como la busqueda es valida (No es vacio y no son solo espacios)
            // Ejecuta la accion declarada en el controlador principal
            definidorAcciones.ejecutarBusqueda(termino);
        }
    }
}

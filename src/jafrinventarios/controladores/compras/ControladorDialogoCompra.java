
package jafrinventarios.controladores.compras;

import jafrinventarios.controladores.utilidades.ResultadoDialogo;
import jafrinventarios.modelos.compras.ModeloCompra;
import jafrinventarios.servicios.compras.ServicioCompras;
import jafrinventarios.vistas.compras.dialogoCompra.DialogoFormularioCompra;
import jafrinventarios.vistas.compras.dialogoCompra.DialogoFormularioCompra.TipoDialogo;

/**
 *
 * @author JOHN FORERO
 */
public class ControladorDialogoCompra {
    
    private DialogoFormularioCompra dialogoCompra;
    private ServicioCompras servicioCompras;
    
    private TipoDialogo tipoDialogo;
    
    private ModeloCompra modeloCompra;
    
    /*
    Variable que en el caso de editar tendra el id del registro a modificar
    pero en el case de crear guardara el id de la compra creada.
    */
    private Integer idCompra;
    
    /*
    Variable para responde la operacion realizada al editar
    que pueden ser las siguientes opciones:  ACTUALIZADO, ELIMINADO, SIN_CAMBIOS 
    */
    private ResultadoDialogo resultadoEdicion = ResultadoDialogo.SIN_CAMBIOS;
    
    
    /*
    ============================================================================
      CONSTRUCTOR PRIVADO PARA EVITAR QUE SE CREE SIN SU DEBIDA CONFIGURACION
    ============================================================================
    */

    private ControladorDialogoCompra(   DialogoFormularioCompra dialogoCompra, 
                                        ServicioCompras servicioCompras, 
                                        TipoDialogo tipoDialogo, 
                                        Integer idCompra) {
        this.dialogoCompra = dialogoCompra;
        this.servicioCompras = servicioCompras;
        this.tipoDialogo = tipoDialogo;
        this.idCompra = idCompra;
        
        this.dialogoCompra.mostrar();
    }
    
    
    /*
    ============================================================================
     METODOS ESTÁTICoS: Los únicos puntos de acceso para los demás controladores
    ============================================================================
    */
    public static ResultadoDialogo editarCompra (   java.awt.Window ventanaPadre,
                                                    Integer idCompra,
                                                    ServicioCompras servicioCompras){
        
        DialogoFormularioCompra dialogoCompra = 
                new DialogoFormularioCompra( ventanaPadre, TipoDialogo.EDITAR_COMPRA);
        
        ControladorDialogoCompra controlador =
                new ControladorDialogoCompra(
                        dialogoCompra,
                        servicioCompras,
                        TipoDialogo.EDITAR_COMPRA,
                        idCompra
                );
        
        return controlador.resultadoEdicion;
    
    }
    
    
    public static Integer crearCompra (   java.awt.Window ventanaPadre,
                                                    ServicioCompras servicioCompras){
        
        DialogoFormularioCompra dialogoCompra = 
                new DialogoFormularioCompra( ventanaPadre, TipoDialogo.CREAR_NUEVA_COMPRA);
        
        ControladorDialogoCompra controlador =
                new ControladorDialogoCompra(
                        dialogoCompra,
                        servicioCompras,
                        TipoDialogo.CREAR_NUEVA_COMPRA,
                        -1
                );
        
        return controlador.idCompra;
    
    }
    
}


package jafrinventarios.DTOs.reportes.cantidadesAComprar;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 *
 * @author JOHN FORERO
 */
public class DTOReporteCantidadesAComprar {
    
    private LinkedHashMap<Integer, DTOProveedorPedido> proveedores;
    
    /*
    ============================================================================
                                CONSTRUCTOR
    ============================================================================
    */

    public DTOReporteCantidadesAComprar() {
        this.proveedores = new LinkedHashMap<>();
    }
    
    
    /*
    ============================================================================
                               Agregar proveedor
    ============================================================================
    */
    public void agregarProveedor( Integer idProveedor , DTOProveedorPedido proveedor){
        proveedores.put(idProveedor, proveedor);
    }
    
    /*
    ============================================================================
                        Agregar producto a proveedor
    ============================================================================
    */
    public void agregarProducto( Integer idProveedor, DTOProductoComprar producto){
        if( existeProveedor(idProveedor) ){
            proveedores.get(idProveedor).agregarProducto(producto);
        }
    }
    
    /*
    ============================================================================
                                    GETTERS
    ============================================================================
    */

    public ArrayList<DTOProveedorPedido> getProveedores() {
        return new ArrayList<>( proveedores.values() );
    }
    
    
    public boolean existeProveedor( Integer idProveedor ){
        return proveedores.containsKey(idProveedor);
    }
    
    public boolean isEmpty(){
        return proveedores.isEmpty();
    }
    
}


package jafrinventarios.DTOs.reportes.cantidadesAComprar;

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
        if( proveedores.containsKey(idProveedor)){
            proveedores.get(idProveedor).agregarProducto(producto);
        }
    }
    
    /*
    ============================================================================
                                    GETTER
    ============================================================================
    */

    public LinkedHashMap<Integer, DTOProveedorPedido> getProveedores() {
        return proveedores;
    }
    
}

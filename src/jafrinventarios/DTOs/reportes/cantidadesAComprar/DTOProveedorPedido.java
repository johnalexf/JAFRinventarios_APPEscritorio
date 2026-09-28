
package jafrinventarios.DTOs.reportes.cantidadesAComprar;

import java.util.ArrayList;

/**
 *
 * @author JOHN FORERO
 */
public class DTOProveedorPedido {
    
    private String nombreComercial;
    private String nombreContacto;
    private String correo;
    private String telefono;
    private String direccion;
    
    private ArrayList<DTOProductoComprar> productos;
    
        
    /*
    ============================================================================
                                CONSTRUCTOR
    ============================================================================
    */

    public DTOProveedorPedido(String nombreComercial, String nombreContacto, String correo, String telefono, String direccion) {
        this.nombreComercial = nombreComercial;
        this.nombreContacto = nombreContacto;
        this.correo = correo;
        this.telefono = telefono;
        this.direccion = direccion;
        this.productos = new ArrayList<>();
    }
    
        
    /*
    ============================================================================
                                GETTERS
    ============================================================================
    */

    public String getNombreComercial() {
        return nombreComercial;
    }

    public String getNombreContacto() {
        return nombreContacto;
    }

    public String getCorreo() {
        return correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public ArrayList<DTOProductoComprar> getProductos() {
        return productos;
    }
    
    
        
    /*
    ============================================================================
                            Agregar un producto
    ============================================================================
    */
    public void agregarProducto( DTOProductoComprar producto ) {
        this.productos.add(producto);
    }
    
    
}

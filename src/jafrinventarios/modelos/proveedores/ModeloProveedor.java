
package jafrinventarios.modelos.proveedores;

import java.util.Objects;

/**
 *
 * @author JOHN FORERO
 */
public class ModeloProveedor {
    
    private int idProveedor;
    private String nombreComercial;
    private String primerNombreContacto;
    private String segundoNombreContacto;
    private String primerApellidoContacto;
    private String segundoApellidoContacto;
    private String telefonoContacto;
    private String direccionProveedor;
    private String correoProveedor;
    private boolean habilitado;
    /* 
    Un proveedor puede estar deshabilitado para no seguir registrando compras
    a nombre de este, sin necesidad de eliminarlo, manteniendo asi la integridad
    de los datos.
    */
    
    /*
    ============================================================================
                        CONSTRUCTORES
    ============================================================================
    */

    public ModeloProveedor() {
    }

    public ModeloProveedor( int idProveedor, 
                            String nombreComercial, 
                            String primerNombreContacto, 
                            String segundoNombreContacto, 
                            String primerApellidoContacto, 
                            String segundoApellidoContacto, 
                            String telefonoContacto, 
                            String direccionProveedor, 
                            String correoProveedor, 
                            boolean habilitado) {
        this.idProveedor = idProveedor;
        this.nombreComercial = nombreComercial;
        this.primerNombreContacto = primerNombreContacto;
        this.segundoNombreContacto = segundoNombreContacto;
        this.primerApellidoContacto = primerApellidoContacto;
        this.segundoApellidoContacto = segundoApellidoContacto;
        this.telefonoContacto = telefonoContacto;
        this.direccionProveedor = direccionProveedor;
        this.correoProveedor = correoProveedor;
        this.habilitado = habilitado;
    }
    
    
    /*
    ============================================================================
                                  GETTERS
    ============================================================================
    */
    
    public int getIdProveedor() {
        return idProveedor;
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public String getPrimerNombreContacto() {
        return primerNombreContacto;
    }

    public String getSegundoNombreContacto() {
        return segundoNombreContacto;
    }

    public String getPrimerApellidoContacto() {
        return primerApellidoContacto;
    }

    public String getSegundoApellidoContacto() {
        return segundoApellidoContacto;
    }

    public String getTelefonoContacto() {
        return telefonoContacto;
    }

    public String getDireccionProveedor() {
        return direccionProveedor;
    }

    public String getCorreoProveedor() {
        return correoProveedor;
    }

    public boolean isHabilitado() {
        return habilitado;
    }
    
    
    /*
    ============================================================================
                                  SETTERS
    ============================================================================
    */
    
    //El set de idProveedor no se expone la unica fuente que lo asigna es la base de datos

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public void setPrimerNombreContacto(String primerNombreContacto) {
        this.primerNombreContacto = primerNombreContacto;
    }

    public void setSegundoNombreContacto(String segundoNombreContacto) {
        this.segundoNombreContacto = segundoNombreContacto;
    }

    public void setPrimerApellidoContacto(String primerApellidoContacto) {
        this.primerApellidoContacto = primerApellidoContacto;
    }

    public void setSegundoApellidoContacto(String segundoApellidoContacto) {
        this.segundoApellidoContacto = segundoApellidoContacto;
    }

    public void setTelefonoContacto(String telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }

    public void setDireccionProveedor(String direccionProveedor) {
        this.direccionProveedor = direccionProveedor;
    }

    public void setCorreoProveedor(String correoProveedor) {
        this.correoProveedor = correoProveedor;
    }

    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }

    
    /*
    ============================================================================
                                EQUALS Y HASCHCODE
    ============================================================================
    */

    
    /*
    El hashCode() es la huella digital numérica del objeto creado
    Si dos objetos son iguales según el método .equals(), entonces 
    obligatoriamente deben devolver el mismo número en .hashCode()
    */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + this.idProveedor;
        hash = 97 * hash + Objects.hashCode(this.nombreComercial);
        hash = 97 * hash + Objects.hashCode(this.primerNombreContacto);
        hash = 97 * hash + Objects.hashCode(this.segundoNombreContacto);
        hash = 97 * hash + Objects.hashCode(this.primerApellidoContacto);
        hash = 97 * hash + Objects.hashCode(this.segundoApellidoContacto);
        hash = 97 * hash + Objects.hashCode(this.telefonoContacto);
        hash = 97 * hash + Objects.hashCode(this.direccionProveedor);
        hash = 97 * hash + Objects.hashCode(this.correoProveedor);
        hash = 97 * hash + (this.habilitado ? 1 : 0);
        return hash;
    }

    /*
    equals se encarga de comparar el objeto creado con otro objeto, se espera
    que se comparen dos del tipo ModeloProveedor para verificar si hubo un cambio
    en sus campos, antes de guardar por medio del servicio los cambios.
    */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ModeloProveedor other = (ModeloProveedor) obj;
        if (this.idProveedor != other.idProveedor) {
            return false;
        }
        if (this.habilitado != other.habilitado) {
            return false;
        }
        if (!Objects.equals(this.nombreComercial, other.nombreComercial)) {
            return false;
        }
        if (!Objects.equals(this.primerNombreContacto, other.primerNombreContacto)) {
            return false;
        }
        if (!Objects.equals(this.segundoNombreContacto, other.segundoNombreContacto)) {
            return false;
        }
        if (!Objects.equals(this.primerApellidoContacto, other.primerApellidoContacto)) {
            return false;
        }
        if (!Objects.equals(this.segundoApellidoContacto, other.segundoApellidoContacto)) {
            return false;
        }
        if (!Objects.equals(this.telefonoContacto, other.telefonoContacto)) {
            return false;
        }
        if (!Objects.equals(this.direccionProveedor, other.direccionProveedor)) {
            return false;
        }
        if (!Objects.equals(this.correoProveedor, other.correoProveedor)) {
            return false;
        }
        return true;
    }

    
    /*
    ============================================================================
                             MÉTODO PARA CLONAR 
    ============================================================================
    */
    
    public ModeloProveedor clonar(){
    
        return new ModeloProveedor(
                        this.idProveedor,
                        this.nombreComercial,
                        this.primerNombreContacto,
                        this.segundoNombreContacto,
                        this.primerApellidoContacto,
                        this.segundoApellidoContacto,
                        this.telefonoContacto,
                        this.direccionProveedor,
                        this.correoProveedor,
                        this.habilitado
        );
    
    }
    
    
    /*
    ============================================================================
                MÉTODO OBTENER UN STRING CON TODOS LOS DATOS
    ============================================================================
    Se usara para cuando se necesite imprimir en consola y verificar que 
    los datos almacenados corresponden a los esperados.
    */
    
    @Override
    public String toString() {
        return "ModeloProveedor" + 
                "\nidProveedor=" + idProveedor + 
                "\nnombreComercial=" + nombreComercial + 
                "\nprimerNombreContacto=" + primerNombreContacto + 
                "\nsegundoNombreContacto=" + segundoNombreContacto + 
                "\nprimerApellidoContacto=" + primerApellidoContacto + 
                "\nsegundoApellidoContacto=" + segundoApellidoContacto + 
                "\ntelefonoContacto=" + telefonoContacto + 
                "\ndireccionProveedor=" + direccionProveedor + 
                "\ncorreoProveedor=" + correoProveedor + 
                "\nhabilitado=" + habilitado + 
                '\n';
    }
    
    
}


package jafrinventarios.modelos.clientes;

import java.util.Objects;

/**
 *
 * @author JOHN FORERO
 */
public class ModeloCliente {
    
    private Integer idCliente;
    private String nombreNegocio;
    private String primerNombreContacto;
    private String segundoNombreContacto;
    private String primerApellidoContacto;
    private String segundoApellidoContacto;
    private String telefonoContacto;
    private String direccionCliente;
    private String correoCliente;
    private boolean habilitado;
    
    
    /*
    ============================================================================
                            CONSTRUCTORES
    ============================================================================
    */

    public ModeloCliente() {
    }
    
    public ModeloCliente(Integer idCliente, String nombreNegocio, String primerNombreContacto, String segundoNombreContacto, String primerApellidoContacto, String segundoApellidoContacto, String telefonoContacto, String direccionCliente, String correoCliente, boolean habilitado) {
        this.idCliente = idCliente;
        this.nombreNegocio = nombreNegocio;
        this.primerNombreContacto = primerNombreContacto;
        this.segundoNombreContacto = segundoNombreContacto;
        this.primerApellidoContacto = primerApellidoContacto;
        this.segundoApellidoContacto = segundoApellidoContacto;
        this.telefonoContacto = telefonoContacto;
        this.direccionCliente = direccionCliente;
        this.correoCliente = correoCliente;
        this.habilitado = habilitado;
    }

    
    /*
    ============================================================================
                                  GETTERS
    ============================================================================
    */
    
    public Integer getIdCliente() {
        return idCliente;
    }

    public String getNombreNegocio() {
        return nombreNegocio;
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

    public String getDireccionCliente() {
        return direccionCliente;
    }

    public String getCorreoCliente() {
        return correoCliente;
    }

    public boolean isHabilitado() {
        return habilitado;
    }

    
    /*
    ============================================================================
                                  SETTERS
    ============================================================================
    */
    
    //El set de idCliente no se expone por que la unica fuente que lo asigna es la base de datos

    public void setNombreNegocio(String nombreNegocio) {
        this.nombreNegocio = nombreNegocio;
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

    public void setDireccionCliente(String direccionCliente) {
        this.direccionCliente = direccionCliente;
    }

    public void setCorreoCliente(String correoCliente) {
        this.correoCliente = correoCliente;
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
        int hash = 7;
        hash = 41 * hash + this.idCliente;
        hash = 41 * hash + Objects.hashCode(this.nombreNegocio);
        hash = 41 * hash + Objects.hashCode(this.primerNombreContacto);
        hash = 41 * hash + Objects.hashCode(this.segundoNombreContacto);
        hash = 41 * hash + Objects.hashCode(this.primerApellidoContacto);
        hash = 41 * hash + Objects.hashCode(this.segundoApellidoContacto);
        hash = 41 * hash + Objects.hashCode(this.telefonoContacto);
        hash = 41 * hash + Objects.hashCode(this.direccionCliente);
        hash = 41 * hash + Objects.hashCode(this.correoCliente);
        hash = 41 * hash + (this.habilitado ? 1 : 0);
        return hash;
    }

    
    /*
    equals se encarga de comparar el objeto creado con otro objeto, se espera
    que se comparen dos del tipo ModeloCliente para verificar si hubo un cambio
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
        final ModeloCliente other = (ModeloCliente) obj;
        if (this.idCliente != other.idCliente) {
            return false;
        }
        if (this.habilitado != other.habilitado) {
            return false;
        }
        if (!Objects.equals(this.nombreNegocio, other.nombreNegocio)) {
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
        if (!Objects.equals(this.direccionCliente, other.direccionCliente)) {
            return false;
        }
        if (!Objects.equals(this.correoCliente, other.correoCliente)) {
            return false;
        }
        return true;
    }
    
    
    
    /*
    ============================================================================
                             MÉTODO PARA CLONAR 
    ============================================================================
    */
    
    public ModeloCliente clonar(){
    
        return new ModeloCliente(
            this.idCliente,
            this.nombreNegocio,
            this.primerNombreContacto,
            this.segundoNombreContacto,
            this.primerApellidoContacto,
            this.segundoApellidoContacto,
            this.telefonoContacto,
            this.direccionCliente,
            this.correoCliente,
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
        return "ModeloCliente" + 
                "\nidCliente=" + idCliente + 
                "\nnombreNegocio=" + nombreNegocio + 
                "\nprimerNombreContacto=" + primerNombreContacto + 
                "\nsegundoNombreContacto=" + segundoNombreContacto + 
                "\nprimerApellidoContacto=" + primerApellidoContacto + 
                "\nsegundoApellidoContacto=" + segundoApellidoContacto + 
                "\ntelefonoContacto=" + telefonoContacto + 
                "\ndireccionCliente=" + direccionCliente + 
                "\ncorreoCliente=" + correoCliente + 
                "\nhabilitado=" + habilitado + 
                '\n';
    }
    
    
}

/*
 Esta clase nos va permitir hacer la simulacion y creacion de una lista de 
    usuarios para rellenar la tabla del modulo de usuarios,
    eventualmente despues de terminar toda la parte grafica de la aplicacion
    y pasar a la conexion con la base de datos, se dejara esta clase como
    la representacion exacta del modelo, y se planteara la creacion de una o 
    mas clases que permitan convertir la respuesta esperada para los controladores
    de la vista.
 */
package jafrinventarios.modelos.usuarios;

import java.util.Objects;

/**
 *
 * @author JOHN FORERO
 */
public class ModeloUsuario {
    
    private int idUsuario;
    private int idEmpresa;
    private String aliasUsuario;
    private String telefonoUsuario;
    private String correoUsuario;
    private String primerNombreUsuario;
    private String segundoNombreUsuario;
    private String primerApellidoUsuario;
    private String segundoApellidoUsuario;
    private String contrasenaUsuario;
    private int idRolUsuario;
    private boolean habilitado;

    
    /*
    ============================================================================
                        CONSTRUCTORES
    ============================================================================
    */

    public ModeloUsuario(){}
    
    public ModeloUsuario(   int idUsuario, 
                            int idEmpresa, 
                            String aliasUsuario, 
                            String telefonoUsuario, 
                            String correoUsuario, 
                            String primerNombreUsuario, 
                            String segundoNombreUsuario, 
                            String primerApellidoUsuario, 
                            String segundoApellidoUsuario, 
                            String contrasenaUsuario, 
                            int idRolUsuario, 
                            boolean habilitado) {
        this.idUsuario = idUsuario;
        this.idEmpresa = idEmpresa;
        this.aliasUsuario = aliasUsuario;
        this.telefonoUsuario = telefonoUsuario;
        this.correoUsuario = correoUsuario;
        this.primerNombreUsuario = primerNombreUsuario;
        this.segundoNombreUsuario = segundoNombreUsuario;
        this.primerApellidoUsuario = primerApellidoUsuario;
        this.segundoApellidoUsuario = segundoApellidoUsuario;
        this.contrasenaUsuario = contrasenaUsuario;
        this.idRolUsuario = idRolUsuario;
        this.habilitado = habilitado;
    }
   
    
    
    /*
    ============================================================================
                                  GETTERS
    ============================================================================
    */

    public int getIdUsuario() {
        return idUsuario;
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public String getAliasUsuario() {
        return aliasUsuario;
    }

    public String getTelefonoUsuario() {
        return telefonoUsuario;
    }

    public String getCorreoUsuario() {
        return correoUsuario;
    }

    public String getPrimerNombreUsuario() {
        return primerNombreUsuario;
    }

    public String getSegundoNombreUsuario() {
        return segundoNombreUsuario;
    }

    public String getPrimerApellidoUsuario() {
        return primerApellidoUsuario;
    }

    public String getSegundoApellidoUsuario() {
        return segundoApellidoUsuario;
    }

    public int getIdRolUsuario() {
        return idRolUsuario;
    }

    public boolean isHabilitado() {
        return habilitado;
    }
    
    public String getNombreCompletoUsuario(){
        return primerNombreUsuario + " " + segundoNombreUsuario + " " + primerApellidoUsuario + " " + segundoApellidoUsuario;
    }


    /*
    ============================================================================
                                    SETTERS
    ============================================================================
     */
    //El set de idUsuario no se expone la unica fuente que lo asigna es la base de datos

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }
    
    public void setAliasUsuario(String aliasUsuario) {
        this.aliasUsuario = aliasUsuario;
    }

    public void setTelefonoUsuario(String telefonoUsuario) {
        this.telefonoUsuario = telefonoUsuario;
    }

    public void setCorreoUsuario(String correoUsuario) {
        this.correoUsuario = correoUsuario;
    }

    public void setPrimerNombreUsuario(String primerNombreUsuario) {
        this.primerNombreUsuario = primerNombreUsuario;
    }

    public void setSegundoNombreUsuario(String segundoNombreUsuario) {
        this.segundoNombreUsuario = segundoNombreUsuario;
    }

    public void setPrimerApellidoUsuario(String primerApellidoUsuario) {
        this.primerApellidoUsuario = primerApellidoUsuario;
    }
    
    public void setSegundoApellidoUsuario(String segundoApellidoUsuario) {
        this.segundoApellidoUsuario = segundoApellidoUsuario;
    }

    public void setIdRolUsuario(int idRolUsuario) {
        this.idRolUsuario = idRolUsuario;
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
        hash = 67 * hash + Objects.hashCode(this.aliasUsuario);
        hash = 67 * hash + Objects.hashCode(this.telefonoUsuario);
        hash = 67 * hash + Objects.hashCode(this.correoUsuario);
        hash = 67 * hash + Objects.hashCode(this.primerNombreUsuario);
        hash = 67 * hash + Objects.hashCode(this.segundoNombreUsuario);
        hash = 67 * hash + Objects.hashCode(this.primerApellidoUsuario);
        hash = 67 * hash + Objects.hashCode(this.segundoApellidoUsuario);
        hash = 67 * hash + this.idRolUsuario;
        return hash;
    }

    
    /*
    equals se encarga de comparar el objeto creado con otro objeto, se espera
    que se comparen dos del tipo ModeloUsuarios para verificar si hubo un cambio
    en sus campos.
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
        final ModeloUsuario other = (ModeloUsuario) obj;
        if (this.idRolUsuario != other.idRolUsuario) {
            return false;
        }
        if (!Objects.equals(this.aliasUsuario, other.aliasUsuario)) {
            return false;
        }
        if (!Objects.equals(this.telefonoUsuario, other.telefonoUsuario)) {
            return false;
        }
        if (!Objects.equals(this.correoUsuario, other.correoUsuario)) {
            return false;
        }
        if (!Objects.equals(this.primerNombreUsuario, other.primerNombreUsuario)) {
            return false;
        }
        if (!Objects.equals(this.segundoNombreUsuario, other.segundoNombreUsuario)) {
            return false;
        }
        if (!Objects.equals(this.primerApellidoUsuario, other.primerApellidoUsuario)) {
            return false;
        }
        if (!Objects.equals(this.segundoApellidoUsuario, other.segundoApellidoUsuario)) {
            return false;
        }
        return true;
    }

    
    /*
    ============================================================================
                             MÉTODO PARA CLONAR 
    ============================================================================
    */
    public ModeloUsuario clonar() {
               
        return  new ModeloUsuario(
                this.idUsuario,
                this.idEmpresa,
                this.aliasUsuario,
                this.telefonoUsuario,
                this.correoUsuario,
                this.primerNombreUsuario,
                this.segundoNombreUsuario,
                this.primerApellidoUsuario,
                this.segundoApellidoUsuario,
                this.contrasenaUsuario,
                this.idRolUsuario,
                this.habilitado
        );
    
    }

    
    /*
    ============================================================================
        MÉTODO OBTENER UN STRING CON TODOS LOS DATOS sin la Contraseña
    ============================================================================
    */
    @Override
    public String toString() {
        return "ModeloUsuario \n" + 
                "idUsuario=" + idUsuario + 
                "\nidEmpresa=" + idEmpresa + 
                "\naliasUsuario=" + aliasUsuario + 
                "\ntelefonoUsuario=" + telefonoUsuario + 
                "\ncorreoUsuario=" + correoUsuario + 
                "\nprimerNombreUsuario=" + primerNombreUsuario + 
                "\nsegundoNombreUsuario=" + segundoNombreUsuario + 
                "\nprimerApellidoUsuario=" + primerApellidoUsuario + 
                "\nsegundoApellidoUsuario=" + segundoApellidoUsuario +
                "\nidRolUsuario=" + idRolUsuario + 
                "\nhabilitado=" + habilitado + 
                '\n';
    }
    
    
    
}

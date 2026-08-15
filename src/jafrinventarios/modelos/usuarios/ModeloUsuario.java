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

/**
 *
 * @author JOHN FORERO
 */
public class ModeloUsuario {
    
    private int idUsuario;
    //private int idEmpresa; para pruebas visuales no necesitamos empresa
    private String alias;
    private String telefono;
    private String correo;
    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;
    //private String contrasena; Tampoco necesitamos por el momento contraseña
    private String rol; 
    //El rol sabemos que debe ser entero por la relacion entre tablas de la base
    //de datos, por consiguiente lo manejamos como string para pruebas de la interfaz visual.
    private boolean estaHabilitado;

    
    /*
    ============================================================================
                        CONSTRUCTOR
    ============================================================================
    */
    public ModeloUsuario(int idUsuario, String alias, String telefono, String correo, String primerNombre, String segundoNombre, String primerApellido, String segundoApellido, String rol, boolean esHabilitado) {
        this.idUsuario = idUsuario;
        this.alias = alias;
        this.telefono = telefono;
        this.correo = correo;
        this.primerNombre = primerNombre;
        this.segundoNombre = segundoNombre;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
        this.rol = rol;
        this.estaHabilitado = esHabilitado;
    }
    
    
    /*
    ============================================================================
                       GETTERS
    ============================================================================
    */
    
    
    public int getIdUsuario() {
        return idUsuario;
    }

    public String getAlias() {
        return alias;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public String getPrimerNombre() {
        return primerNombre;
    }

    public String getSegundoNombre() {
        return segundoNombre;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public String getRol() {
        return rol;
    }

    public boolean getEstaHabilitado() {
        return estaHabilitado;
    }
    
    
    /*
    ============================================================================
                        SETTERS
    ============================================================================
    */

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }

    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public void setEstaHabilitado(boolean esHabilitado) {
        this.estaHabilitado = esHabilitado;
    }
    
    
    
}

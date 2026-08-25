
package jafrinventarios.modelos;

/**
 *
 * @author JOHN FORERO
 */
public class ModeloSesionUsuario {
    
    // Instancia estática privada (El Singleton)
    private static ModeloSesionUsuario instancia;

    // Variables de estado de la sesión
    private int idUsuario;
    private String nombreRol;
    private boolean isAdministrador;
    private int idEmpresa; 
    private String nombreEmpresa;

    /* 
    =================================================================================
    Constructor privado: evita que se haga "new ModeloSesionUsuario()" en otro lado
    =================================================================================
    */
    private ModeloSesionUsuario() {
        // Inicializamos con valores por defecto seguros
        asignarValoresPorDefecto();
    }
    
    private void asignarValoresPorDefecto(){
        this.idUsuario = -1;
        this.nombreRol = "";
        this.isAdministrador = false;
        this.idEmpresa = -1;
        this.nombreEmpresa = "";
    }
    
    /* 
    ============================================================================
            Método público estático para obtener la única instancia
    ============================================================================
    */
    public static ModeloSesionUsuario getInstancia() {
        if (instancia == null) {
            //Si no existe se crea una unica vez
            instancia = new ModeloSesionUsuario();
        }
        return instancia;
    }
    
    
    /* 
    ============================================================================
                            Metodos principales
    ============================================================================
    */

    public void iniciarSesion(  int idUsuario, 
                                String nombreRol, 
                                boolean isAdministrador, 
                                int idEmpresa,
                                String nombreEmpresa) {
        this.idUsuario = idUsuario;
        this.nombreRol = nombreRol;
        this.isAdministrador = isAdministrador;
        this.idEmpresa = idEmpresa;
        this.nombreEmpresa = nombreEmpresa;
    }

    public void cerrarSesion() {
        asignarValoresPorDefecto();
    }
    
    
    /* 
    ============================================================================
                                GETTERS
    ============================================================================
    */


    public int getIdUsuario() {
        return idUsuario;
    }
    
    public String getNombreRolUsuario(){
        return nombreRol;
    }

    public boolean isAdministrador() {
        return isAdministrador;
    }
    
    public int getIdEmpresa() {
        return idEmpresa;
    }
    
    public String getNombreEmpresa(){
        return nombreEmpresa;
    }
    
    //No se exponen setter puesto que despues de guardada la informacion, no se
    //debe modificar.

    
}

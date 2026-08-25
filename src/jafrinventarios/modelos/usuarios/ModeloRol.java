
package jafrinventarios.modelos.usuarios;

/**
 *
 * @author JOHN FORERO
 */
public class ModeloRol {
    
    private int idRol;
    private String nombreRol;

    public ModeloRol(int idRol, String nombreRol) {
        this.idRol = idRol;
        this.nombreRol = nombreRol;
    }

    public int getIdRol() {
        return idRol;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    /*
    No se exponen setters puesto que la solucion no contempla modificar
    el nombre del rol, estos se crean una unica vez desde la base de datos.
    */
    
    
}

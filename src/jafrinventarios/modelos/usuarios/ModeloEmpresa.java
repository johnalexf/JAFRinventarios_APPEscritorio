
package jafrinventarios.modelos.usuarios;

/**
 *
 * @author JOHN FORERO
 */
public class ModeloEmpresa {
    
    /*
    Este modelo se penso para crear una relacion con el nombre de la empresa
    para que este no se tenga que asignar manualmente si no consultando la
    base de datos, permitiendo asi que la libertad de ofrecer la app a 
    cualquier empresa sin depender del nombre
    
    Ademas ofrece un codigo de acceso dinamico que se utilizara para poder
    crear usuarios no administradores, como tal la aplicacion al iniciar pedira 
    un codigo de acceso para el administrador que sera de un unico uso
    despues para los vendedores se da la libertad de que se puedan crear con 
    este codigo al cual solo puede acceder un administrador desde la app
    
    */
    
    private int idEmpresa;
    private String nombreEmpresa;
    private char[] codigoRegistroUsuarioVendedor = new char[10];

    public ModeloEmpresa(int idEmpresa, String nombreEmpresa, char[] codigoRegistroUsuarioVendedor) {
        this.idEmpresa = idEmpresa;
        this.nombreEmpresa = nombreEmpresa;
        this.codigoRegistroUsuarioVendedor = codigoRegistroUsuarioVendedor;
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public char[] getCodigoRegistroUsuarioVendedor() {
        return codigoRegistroUsuarioVendedor;
    }

    public void setCodigoRegistroUsuarioVendedor(char[] codigoRegistroUsuarioVendedor) {
        this.codigoRegistroUsuarioVendedor = codigoRegistroUsuarioVendedor;
    }
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    private String rol;
    private boolean esAdministrador;

    // Constructor privado para evitar que hagan "new ModeloSesionUsuario()" en otro lado
    private ModeloSesionUsuario() {
        // Inicializamos con valores por defecto seguros
        this.idUsuario = -1;
        this.esAdministrador = false;
        this.rol = "";
    }

    // Método público estático para obtener la única instancia
    public static ModeloSesionUsuario getInstancia() {
        if (instancia == null) {
            //Si no existe se crea una unica vez
            instancia = new ModeloSesionUsuario();
        }
        return instancia;
    }

    // ==========================================
    // GETTERS Y SETTERS
    // ==========================================

    public void iniciarSesion(int idUsuario, String rol, boolean esAdministrador) {
        this.idUsuario = idUsuario;
        this.rol = rol;
        this.esAdministrador = esAdministrador;
    }

    public void cerrarSesion() {
        this.idUsuario = -1;
        this.rol = "";
        this.esAdministrador = false;
    }

    public int getIdUsuario() {
        return idUsuario;
    }
    
    public String getRolUsuario(){
        return rol;
    }

    public boolean esAdministrador() {
        return esAdministrador;
    }
}

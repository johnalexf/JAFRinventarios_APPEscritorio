/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jafrinventarios.servicios.usuarios;

import jafrinventarios.DTOs.Usuarios.DTOUsuarioTabla;
import jafrinventarios.modelos.usuarios.ModeloUsuario;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author JOHN FORERO
 */
public class ServicioUsuarios {
    
    
    public ServicioUsuarios() {
    }

    
    private List<DTOUsuarioTabla> simulacionConsultaBDTodosUsuarios () {
        List<DTOUsuarioTabla> listaUsuarios = new ArrayList<>();
        
        listaUsuarios.add(new DTOUsuarioTabla(1, "john1", "3202173409", "john1@gmail.com", "John Forero", "Administrador", true));
        listaUsuarios.add(new DTOUsuarioTabla(2, "amartinez", "3101234567", "amartinez@empresa.com", "Ana María Martínez López", "Vendedor", true));
        listaUsuarios.add(new DTOUsuarioTabla(3, "cramirez", "3119876543", "cramirez@empresa.com", "Carlos Andrés Ramírez Gómez", "Vendedor", true));
        listaUsuarios.add(new DTOUsuarioTabla(4, "dcastro", "3004567890", "dcastro@empresa.com", "Diana Castro Vega", "Administrador", true));
        listaUsuarios.add(new DTOUsuarioTabla(5, "lherrera", "3205554433", "lherrera@empresa.com", "Luis Fernando Herrera Díaz", "Vendedor", false)); 
        listaUsuarios.add(new DTOUsuarioTabla(6, "mrojas", "3156667788", "mrojas@empresa.com", "Marta Lucía Rojas Silva", "Vendedor", true));
        listaUsuarios.add(new DTOUsuarioTabla(7, "jospina", "3129998877", "jospina@empresa.com", "Jorge Iván Ospina Cruz", "Vendedor", true));
        listaUsuarios.add(new DTOUsuarioTabla(8, "vquintero", "3182223344", "vquintero@empresa.com", "Valentina Quintero Ríos", "Administrador", true));
        listaUsuarios.add(new DTOUsuarioTabla(9, "sgalvis", "3194445566", "sgalvis@empresa.com", "Sergio Esteban Galvis Mora", "Vendedor", false));
        listaUsuarios.add(new DTOUsuarioTabla(10, "pnavarro", "3017778899", "pnavarro@empresa.com", "Patricia Navarro Pérez", "Vendedor", true));
        
        return listaUsuarios;
    }

    
    public List<DTOUsuarioTabla> obtenerTodosLosUsuarios() {
        
        //TODO: Aqui se hara la consulta a la base de datos y se armara
        //la lista de todos los usuarios, por el momento se simula tanto la consulta
        // como el empaquetado con la siguiente funcion.
        List<DTOUsuarioTabla> listaUsuarios = simulacionConsultaBDTodosUsuarios();
        
        return listaUsuarios;
    }
    
    
    public List<DTOUsuarioTabla> obtenerListaUsuariosPorFiltro( String filtro ) {
        System.out.println("Buscando en la BD de Usuarios el término: " + filtro);
        
        //TODO: Aqui se hara la consulta a la base de datos y se armara
        //la lista de todos los usuarios que cumplan el filtro, por el momento se simula que no hay coincidencias
        List<DTOUsuarioTabla> listaUsuarios = new ArrayList<>();
        
        return listaUsuarios;
    }
    
    
    public DTOUsuarioTabla obtenerDatosUsuario( int idUsuario ){
    
        //Por el momento no se va usar hasta que se conecte con la base de datos
        // pero se deja listo para que el controlador quede lo mayor posible terminado
        // Simulacion de la consulta y creacion del usuario con el id especifico
        DTOUsuarioTabla usuarioConsultado = new DTOUsuarioTabla(10, "pnavarro", "3017778899", "pnavarro@empresa.com", "Patricia Navarro Pérez", "Vendedor", true);

        return usuarioConsultado;
    }
    
}

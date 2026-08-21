/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jafrinventarios.servicios.usuarios;

import jafrinventarios.DTOs.usuarios.DTOUsuarioTabla;
import jafrinventarios.modelos.usuarios.ModeloUsuario;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author JOHN FORERO
 */
public class ServicioUsuarios {
    
    //TODO este servicio debera tener un String o un StringBuilder que permita
    //almacenar si algun error paso para poder devolverlo al controlador
    // en dado caso se podra pensar en un hashmap que tenga dos concepto
    // errorGeneral para el error especifico
    // y errorespecifico para empaquetar en otro hashmap los errores por cada campo
    // como por ejemplo lo mas comun que este repetido como un correo, telefono o alias
    
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
    
    
    // Este metodo entrega un usuario tipo DTOUsuarioTabla y esta destinado solo para mostrar los datos en una tabla o una tarjeta de perfil
    public DTOUsuarioTabla obtenerDatosDTOUsuario( int idUsuario ){
    
        //Por el momento no se va usar hasta que se conecte con la base de datos
        // pero se deja listo para que el controlador quede lo mayor posible terminado
        // Simulacion de la consulta y creacion del usuario con el id especifico
        DTOUsuarioTabla usuarioConsultado = new DTOUsuarioTabla(10, "pnavarro", "3017778899", "pnavarro@empresa.com", "Patricia Navarro Pérez", "Vendedor", true);

        return usuarioConsultado;
    }
    
    //Este metodo devuelve un usuario del tipo ModeloUsuario que servira de base para poder editarlo y devolverlo para guardar los cambios
    public ModeloUsuario obtenerModeloUsuario( int idUsuario ){
    
        //Smulacion de conexion y empaquetado de la informacion de un usuario
       
        // La contraseña a pesar de que es parte del ModeloUsuario la dejamos vacia
        // de igual manera al hacer la consulta a la base de datos no se pedira este dato
        
        /* 
        El idEmpresa tiene una particularidad y es que solo existira en la base
        de datos una empresa, por tanto es irrelevante este dato pues no se utilizara
        para editar un usuario, sin embargo se enviara de todas maneras el asignado
        que tenga el usuario.
        */
        ModeloUsuario usuarioConsultado = 
                //new ModeloUsuario( idUsuario, idEmpresa,aliasUsuario, telefonoUsuario, correoUsuario, primerNombreUsuario, segundoNombreUsuario,
                //                    primerApellidoUsuario, segundoApellidoUsuario, contrasenaUsuario, idRolUsuario, estaHabilitado)
                new ModeloUsuario( idUsuario, 1, "john1", "3202173409", "john1@gmail.com", "john","", "forero", "", "", 1, true);

        return usuarioConsultado;
        
    }
    
    
    public boolean editarPerfil( ModeloUsuario usuario ){
    
        boolean respuestaConsulta = false;
  
        /*
            usuario.getIdUsuario(); Este es la clave para saber que usuario editar
        
        Cuando se desee editar el perfil solo se tendran en cuenta los siguientes campos
            usuario.getAliasUsuario();
            usuario.getTelefonoUsuario();
            usuario.getCorreoUsuario();
            usuario.getPrimerNombreUsuario();
            usuario.getSegundoNombreUsuario();
            usuario.getPrimerApellidoUsuario();
            usuario.getSegundoApellidoUsuario();
        
        Aunque hay una diferencia entre si es un perfil de un administrador a un vendedor
        la interfaz grafica y el controlador estan diseñados para que un vendedor no
        pueda modificar su nombre ni su alias, puesto esto puede alterar la integridad
        de los datos sin previo conocimiento del administrador, para modificar estos 
        valores es necesario que lo haga el adminsitrador desde la seccion usuarios.
        
        Por tanto cuando se estructure esta consulta se personalizara con el dato
        
         usuario.getIdRolUsuario() para lo cual se hara una consulta a la base 
        de datos para determinar si es un usuario administrador
        
        */

        return respuestaConsulta;
    }
    
    
    public boolean editarOtroUsuario( ModeloUsuario usuario ){
    
        boolean respuestaConsulta = false;
  
        /*
            usuario.getIdUsuario(); Este es la clave para saber que usuario editar
        
        Cuando se desee editar el usuario se tendran en cuenta los siguientes campos
            usuario.getAliasUsuario();
            usuario.getTelefonoUsuario();
            usuario.getCorreoUsuario();
            usuario.getPrimerNombreUsuario();
            usuario.getSegundoNombreUsuario();
            usuario.getPrimerApellidoUsuario();
            usuario.getSegundoApellidoUsuario();
            usuario.getIdRolUsuario();
        
            para deshabilitarlo se creara una funcion destinada para ello
        
        */

        return respuestaConsulta;
    }
    
    
    
    public boolean conmutarEstadoUsuario (  int idUsuario  ){
    
        boolean respuestaConsulta = false;
  
        /*
            idUsuario Este es la clave para saber que usuario editar
        
            consultamos su estado y lo conmutamos
        
            
        */
        
        return respuestaConsulta;
    }
    
    
    public boolean esUsuarioHabilitado( int idUsuario ){
    
        //Por el momento retornamos true, pero aca se hara la consulta y se 
        // devolvera su respectivo estado
        
        return true;
    }
    
    
    
    public int crearUsuario( ModeloUsuario usuario ){
    
        int respuestaConsulta = -1;
  
        /*
      
        Cuando se desee crear un usuario se tendran en cuenta los siguientes campos
            usuario.getIdEmpresa();
            usuario.getAliasUsuario();
            usuario.getTelefonoUsuario();
            usuario.getCorreoUsuario();
            usuario.getPrimerNombreUsuario();
            usuario.getSegundoNombreUsuario();
            usuario.getPrimerApellidoUsuario();
            usuario.getSegundoApellidoUsuario();
            usuario.getIdRolUsuario();
        
            usuario.estaHabilitado() Este campo por defecto deberia ser true, 
            por tanto se asignara como true al enviarlo a la base de datos y no 
            dependera del valor que traiga consigo
        */

        
        
        return respuestaConsulta;
    }
    
    
    public boolean eliminarUsuario( int idUsuario ){
    
        boolean respuestaConsulta = false;
        
        /*
        Para eliminar un usuario se hara la respectiva consulta, sin embargo
        la base de datos no debe permitir eliminar un usuario si este
        es la clave foranea de cualquier tabla, como ventas o productos
        
        Es importante tambien tener en cuenta en un futuro que el mismo administrador
        no se pueda eliminar en gestion de usuarios, en su momento se planteara
        el codigo para evitar esta eventualidad
        */
        
        
        return respuestaConsulta;
    }
    
    
    public boolean tieneRegistrosAsociados ( int idUsuario ){
    
        boolean respuestaConsulta = false;
        
        /*
        Esta consulta se encargara de verificar si el usuario es clave foranea
        de otra tabla, esto permite personalizar el dialogo para mostrar el boton
        eliminar si no tiene una relacion
        o deshabilitar si tiene una relacion
        */
        
        
        return respuestaConsulta;
    }
    
    
    
    
    public String obtenerCodigoRegistroVendedor(){
        
        /*
            Hacer la consulta para obtener el codigo que puede compartir un
            administrador a los vendedores para que se registren, valido solo por
            un uso
        */
        
        return "10Fras125G";
    
    }
    
}

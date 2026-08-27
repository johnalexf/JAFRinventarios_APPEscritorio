
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

    
    public List<DTOUsuarioTabla> obtenerTodosLosUsuarios() throws Exception{
        
        //TODO: Aqui se hara la consulta a la base de datos y se armara
        //la lista de todos los usuarios, por el momento se simula tanto la consulta
        // como el empaquetado con la siguiente funcion.
        List<DTOUsuarioTabla> listaUsuarios = simulacionConsultaBDTodosUsuarios();
        
        return listaUsuarios;
    }
    
    
    public List<DTOUsuarioTabla> obtenerListaUsuariosPorFiltro( String filtro ) throws Exception {
        System.out.println("Buscando en la BD de Usuarios el término: " + filtro);
        
        //TODO: Aqui se hara la consulta a la base de datos y se armara
        //la lista de todos los usuarios que cumplan el filtro, por el momento se simula que no hay coincidencias
        List<DTOUsuarioTabla> listaUsuarios = new ArrayList<>();
        
        try {
            
            //Si el resultado no arroja ningun valor, entonces mandamos la lista vacia
        } catch (Exception e) {
            // Si se puede determinar si el error es por base de datos
            //Entonces crear el error
            //throw new Exception("Error de conexion");
            
        }
        
        
        return listaUsuarios;
    }
    
    
    // Este metodo entrega un usuario tipo DTOUsuarioTabla y esta destinado solo para mostrar los datos en una tabla o una tarjeta de perfil
    public DTOUsuarioTabla obtenerDatosDTOUsuario( int idUsuario ) throws Exception{
    
        //Por el momento no se va usar hasta que se conecte con la base de datos
        // pero se deja listo para que el controlador quede lo mayor posible terminado
        // Simulacion de la consulta y creacion del usuario con el id especifico
        
        /*
        si la consulta no devuelve un usuario entonces lanzamos el error
        throw new Exception("No se encontró la información del perfil en la base de datos.");
        */
        
        DTOUsuarioTabla usuarioConsultado = new DTOUsuarioTabla(10, "pnavarro", "3017778899", "pnavarro@empresa.com", "Patricia Navarro Pérez", "Vendedor", true);

        return usuarioConsultado;
    }
    
    //Este metodo devuelve un usuario del tipo ModeloUsuario que servira de base para poder editarlo y devolverlo para guardar los cambios
    public ModeloUsuario obtenerModeloUsuario( int idUsuario ) throws Exception{
    
        //Smulacion de conexion y empaquetado de la informacion de un usuario
       
        // La contraseña a pesar de que es parte del ModeloUsuario la dejamos vacia
        // de igual manera al hacer la consulta a la base de datos no se pedira este dato
        
        /* 
        El idEmpresa tiene una particularidad y es que solo existira en la base
        de datos una empresa, por tanto es irrelevante este dato pues no se utilizara
        para editar un usuario, sin embargo se enviara de todas maneras el asignado
        que tenga el usuario.
        
        si la consulta no devuelve un usuario entonces lanzamos el error
        throw new Exception("No se encontró la información del perfil en la base de datos.");
        
        */
        ModeloUsuario usuarioConsultado = 
                //new ModeloUsuario( idUsuario, idEmpresa,aliasUsuario, telefonoUsuario, correoUsuario, primerNombreUsuario, segundoNombreUsuario,
                //                    primerApellidoUsuario, segundoApellidoUsuario, contrasenaUsuario, idRolUsuario, isHabilitado)
                new ModeloUsuario( idUsuario, 1, "john1", "3202173409", "john1@gmail.com", "john","", "forero", "", "", 1, true);

        return usuarioConsultado;
        
    }
    
    
    public void editarPerfil( ModeloUsuario usuario , boolean isAdministrador ) throws Exception{
    
        try {
  
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
           esAdministrador
        
        */
        } 
        /*
        Ejemplo de manejo de la respuesta de la base de datos
        Dejamos comentado hasta que se haga la conexion a la base de datos
        catch (SQLIntegrityConstraintViolationException e) {
            
            String errorBD = e.getMessage();
            HashMap<String, String> errores = new HashMap<>();

            // Buscar palabras clave en el error de la base de datos
            if (errorBD.contains("correo_UNIQUE")) {
                errores.put("correo", "Este correo ya está registrado.");
            } 
            if (errorBD.contains("alias_UNIQUE")) {
                errores.put("alias", "El alias ya está en uso.");
            }

            // Lanzar la excepción personalizada con el mapa listo para la vista
            throw new ExcepcionValidacionBD(errores);
        }*/
        catch (Exception e) {
        }
        
    }
    
    
    public void editarOtroUsuario( ModeloUsuario usuario ) throws Exception{
    
        try {
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
        } 
        /*Ejemplo de manejo de la respuesta de la base de datos
        Dejamos comentado hasta que se haga la conexion a la base de datos
        catch (SQLIntegrityConstraintViolationException e) {
            
            String errorBD = e.getMessage();
            HashMap<String, String> errores = new HashMap<>();

            // Buscar palabras clave en el error de la base de datos
            if (errorBD.contains("correo_UNIQUE")) {
                errores.put("correo", "Este correo ya está registrado.");
            } 
            if (errorBD.contains("alias_UNIQUE")) {
                errores.put("alias", "El alias ya está en uso.");
            }

            // Lanzar la excepción personalizada con el mapa listo para la vista
            throw new ExcepcionValidacionBD(errores);
        }*/
        catch (Exception e) {
        }
        
    }
    
    
    
    public void conmutarEstadoUsuario (  int idUsuario  ) throws Exception {
    
  
        /*
            idUsuario Este es la clave para saber que usuario editar
        
            consultamos su estado y lo conmutamos
        
        //si hay error crear el error segun sea el caso
        //    throw new RuntimeException("No se pudo editar el estado del usuario");
        */
        
    }
    
    
    public boolean isUsuarioHabilitado( int idUsuario ) throws Exception {
    
        //Por el momento retornamos true, pero aca se hara la consulta y se 
        // devolvera su respectivo estado
        
        //si hay error crear el error segun sea el caso
        //    throw new RuntimeException("No se pudo obtener el valor del usuario");
        
        return true;
    }
    
    
    
    public int crearUsuario( ModeloUsuario usuario ) throws Exception{
    
        try {
            
  
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
        
            usuario.isHabilitado() Este campo por defecto deberia ser true, 
            por tanto se asignara como true al enviarlo a la base de datos y no  
            dependera del valor que traiga consigo 
        */
        
        //cuando se concecte la base de datos se enviara el id correspondiente
            return -1;
        
        } 
         /*
        Ejemplo de manejo de la respuesta de la base de datos
        Dejamos comentado hasta que se haga la conexion a la base de datos
        catch (SQLIntegrityConstraintViolationException e) {
            
            String errorBD = e.getMessage();
            HashMap<String, String> errores = new HashMap<>();

            // Buscar palabras clave en el error de la base de datos
            if (errorBD.contains("correo_UNIQUE")) {
                errores.put("correo", "Este correo ya está registrado.");
            } 
            if (errorBD.contains("alias_UNIQUE")) {
                errores.put("alias", "El alias ya está en uso.");
            }

            // Lanzar la excepción personalizada con el mapa listo para la vista
            throw new ExcepcionValidacionBD(errores);
        }*/
        catch (Exception e) {
            return -1;
            //si hay error crear el error segun sea el caso
            //    throw new RuntimeException("No se pudo completar la operacion o falla en el servicio");
        }
        
        
    }
    
    
    public void eliminarUsuario( int idUsuario ) throws Exception{
        
        /*
        Para eliminar un usuario se hara la respectiva consulta, sin embargo
        la base de datos no debe permitir eliminar un usuario si este
        es la clave foranea de cualquier tabla, como ventas o productos
        
        Es importante tambien tener en cuenta en un futuro que el mismo administrador
        no se pueda eliminar en gestion de usuarios, en su momento se planteara
        el codigo para evitar esta eventualidad
        
        //si hay error crear el error segun sea el caso
        //    throw new RuntimeException("No se pudo completar la operacion o falla en el servicio");
        
        Aqui tambien se va verificar si el idUsuario es clave foranea para
        evitar eliminarlo si tiene registros asociados en dado caso
        se devolvera el error con el mensaje 
        throw new RuntimeException("Usuario con registros asociados no se puede eliminar");
        
        tambien existira una regla que un usuario adminsitrador no se puede eliminar
        aqui para ser mas especifico se puede crear una variable superAdminsitrador
        que solo sera el primer adminsitrador registrado y este no se podra eliminar
        dependiendo del tiempo para implementar la solucion se decidira cual opcion
        realizar
        */

    }
    
    
    public boolean isUsuarioEliminable ( int idUsuario ) throws Exception{
    
        boolean respuestaConsulta = false;
        
        /*
        Esta consulta se encargara de verificar si el usuario es clave foranea
        de otra tabla, esto permite personalizar el dialogo para mostrar el boton
        eliminar si no tiene una relacion
        o deshabilitar si tiene una relacion
        
        //si hay error crear el error segun sea el caso
            //    throw new RuntimeException("No se pudo completar la operacion o falla en el servicio");
        
        */
        
        
        return respuestaConsulta;
    }
    
    
    
    
    public String obtenerCodigoRegistroVendedor() throws Exception{
        
        /*
            Hacer la consulta para obtener el codigo que puede compartir un
            administrador a los vendedores para que se registren, valido solo por
            un uso
        */
        
        return "10Fras125G";
    
    }
    
}

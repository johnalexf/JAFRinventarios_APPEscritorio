
package jafrinventarios.servicios.usuarios;

import jafrinventarios.modelos.usuarios.ModeloRol;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author JOHN FORERO
 */
public class ServicioRoles {
    
    
    /*
     * En el futuro, este método se conectará a la base de datos (SELECT * FROM roles),
     * llenará una lista de objetos ModeloRol y luego extraerá el diccionario.
     */
    public static LinkedHashMap<Integer, String> obtenerDiccionarioRoles() throws Exception{
        
        // Simulamos lo que respondería la base de datos creando los modelos
        List<ModeloRol> rolesBD = new ArrayList<>();
        rolesBD.add(new ModeloRol(1, "Administrador"));
        rolesBD.add(new ModeloRol(2, "Vendedor"));
        
        // Armamos el diccionario exacto que necesita la vista (ID -> Nombre)
        LinkedHashMap<Integer, String> diccionarioRoles = new LinkedHashMap<>();
        
        rolesBD.forEach(rol -> {
            diccionarioRoles.put(rol.getIdRol(), rol.getNombreRol());
        });
        
        //si hay error crear el error segun sea el caso
        //    throw new RuntimeException("No se pudo obtener la lista de roles");
        
        return diccionarioRoles;
    }
    
}

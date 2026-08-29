/**
 * Esta clase ofrece metodos para realizar las siguientes operaciones:
 * Hashear una contraseña
 * Verificar una contraseña con un hash
 * Generar un codigo aleatorio de 10 digitos con numeros, letras en minuscula y letras en mayuscula
 * Generar una contraseña aleatoria
 */
package jafrinventarios.servicios.acceso;

import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author johna
 */
public class ServicioSeguridad {
    
    
    /**
     * hashearContrasena
     * Genera un hash BCrypt a partir de una contraseña.
     *
     * Un hash es un resultado generado a partir de la contraseña
     * que se utiliza para almacenarla de forma segura sin guardar
     * la contraseña original.
     *
     * BCrypt.gensalt() genera un valor aleatorio llamado "salt".
     * El salt se utiliza para que incluso dos contraseñas iguales
     * produzcan hashes diferentes.
     *
     * El salt y la información necesaria para verificar la contraseña
     * quedan incluidos dentro del hash generado por BCrypt.
     */
    public static String hashearContrasena(String contraseña) {

        // BCrypt genera el hash utilizando la contraseña
        // y un salt aleatorio generado automáticamente.
        return BCrypt.hashpw(contraseña, BCrypt.gensalt());
    }


    /**
     * verificarContraseña
     * Verifica si una contraseña coincide con un hash almacenado.
     *
     * BCrypt proporciona el método checkpw() para realizar esta
     * comprobación. El método utiliza la información contenida
     * dentro del hash almacenado para realizar nuevamente el
     * proceso y determinar si la contraseña ingresada es correcta.
     *
     * No es necesario desencriptar el hash ni generar un hash
     * manualmente para realizar la comparación.
     */
    public static boolean verificarContrasena(
            String contraseña,
            String hash) {

        // BCrypt compara la contraseña ingresada con el hash
        // y devuelve true si coinciden o false si no coinciden.
        return BCrypt.checkpw(contraseña, hash);
    }

    
    
    public static String generarCodigo (){
        
        StringBuilder codigo = new StringBuilder();
        
        for( int i=0; i<10; i++){
            double numAleatorio = Math.random();
            int codigoASCII;
            
            if( numAleatorio < 0.35 )  
                codigoASCII = (int) ( Math.random()*(57 - 48) + 48 );
            else if(numAleatorio < 0.75 ) 
                codigoASCII = (int) ( Math.random()*(90 - 65) + 65 );
            else 
                codigoASCII = (int) ( Math.random()*(122 - 97) + 97 );
            
            char caracter = (char) codigoASCII;
            codigo.append(caracter);
        }
        
        return codigo.toString();
    }
    
    
    public static String generarContrasena() {
    
    // Se reutiliza el método generarCodigo(), ya que
    // ambos requieren generar una cadena aleatoria
    // compuesta por letras y números.
    return generarCodigo();
}
    
    
}

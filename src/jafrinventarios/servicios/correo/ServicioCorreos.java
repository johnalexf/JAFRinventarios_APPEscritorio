/**
 * Este servicio utiliza dos driver jakarta.mail y jakarta.activation.
 * jakarta.mail permite enviar correos electronicos pero depende de
 * jakarta.activation para procesar el texto y los datos del cuerpo 
 * del mensaje.
 */

package jafrinventarios.servicios.correo;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author JOHN FORERO
 */
public class ServicioCorreos {

    // Configura aquí tu correo genérico y la clave de aplicación de 16 dígitos
    private static final String CORREO_REMITENTE = "notificaciones.jafr@gmail.com";
    private static final String CLAVE_APLICACION = "cxfrqkeingpjriok"; 

    
    private static void enviarCorreo(String correoDestino, String asuntoCorreo, String cuerpoCorreo) throws Exception {
        
        // 1. Configurar las propiedades del servidor SMTP de Gmail
        Properties propiedades = new Properties();
        propiedades.put("mail.smtp.host", "smtp.gmail.com");
        propiedades.put("mail.smtp.port", "587");
        propiedades.put("mail.smtp.auth", "true");
        propiedades.put("mail.smtp.starttls.enable", "true"); // Fundamental para la seguridad de Gmail

        // 2. Crear la sesión con autenticación
        Session sesion = Session.getInstance(propiedades, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(CORREO_REMITENTE, CLAVE_APLICACION);
            }
        });

        try {
            // 3. Construir el mensaje
            Message mensaje = new MimeMessage(sesion);
            mensaje.setFrom(new InternetAddress(CORREO_REMITENTE));
            mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(correoDestino));
            mensaje.setSubject(asuntoCorreo);

            // 4. Cuerpo del correo
            mensaje.setText(cuerpoCorreo);

            // 5. Enviar el correo
            Transport.send(mensaje);

        } catch (Exception e) {
            // Capturamos el error de la librería y lanzamos uno propio para el controlador
            throw new Exception("No se pudo enviar el correo de credenciales. Revisa tu conexión o la configuración del servidor.\n" + e.getMessage());
        }
    }
    
    
    public static void enviarCredenciales( String correoDestino, String nombreUsuario, String contrasenaUsuario ) throws Exception{
    
        String asuntoCorreo = "Bienvenido a JAFRinventarios - Credenciales de acceso";

        String cuerpoCorreo = "Hola " + nombreUsuario + ",\n\n"
                + "Tu cuenta en el sistema JAFR inventarios ha sido creada exitosamente.\n\n"
                + "Tus credenciales temporales son:\n"
                + "Usuario/Correo: " + correoDestino + "\n"
                + "Contraseña: " + contrasenaUsuario + "\n\n"
                + "Te recomendamos cambiar esta contraseña al ingresar por primera vez.\n\n"
                + "Saludos,\n"
                + "El equipo de administración.";

        enviarCorreo(correoDestino, asuntoCorreo, cuerpoCorreo);
        
    }
    
    
    public static void enviarCodigoRecuperacion( String correoDestino, String codigoRecuperacion ) throws Exception{
    
        String asuntoCorreo = "Recuperacion cuenta en JAFR inventarios";

        String cuerpoCorreo = "Hola usuario de JAFR inventarios,\n\n"
                + "Tu cuenta en el sistema JAFR inventarios esta en proceso de recuperacion.\n\n"
                + "Tu codigo para cambiar la contraseña es:\n"
                + "       " + codigoRecuperacion + "         \n\n"
                + "Recuerda no compartir el codigo, es solo para tu uso personal.\n\n"
                + "Saludos,\n"
                + "El equipo de administración.";

        enviarCorreo(correoDestino, asuntoCorreo, cuerpoCorreo);
        
    }
    
}
# Estructura de Librerías Externas (JAFRinventarios)

Este directorio contiene todas las dependencias de terceros (archivos `.jar`) necesarias para compilar y ejecutar el sistema de inventarios. Para facilitar su mantenimiento y portabilidad, están organizadas por módulos según su propósito.

## 📂 conexion_DB_MySQL
Contiene los controladores necesarios para la comunicación con la base de datos.
*   **`mysql-connector-java-8.0.22.jar`**: Es el driver JDBC oficial de MySQL. Permite que la aplicación Java se conecte, envíe consultas SQL y reciba datos del motor de base de datos MySQL.

## 📂 correo
Librerías encargadas de gestionar el protocolo SMTP para el envío de notificaciones y correos electrónicos.
*   **`jakarta.mail-1.6.7.jar`**: La API principal de Jakarta Mail que provee las clases para construir y enviar correos electrónicos.
*   **`jakarta.activation-1.2.2.jar`**: Dependencia requerida por Jakarta Mail para manejar diferentes tipos de datos MIME (como archivos adjuntos o contenido HTML en los correos).

## 📂 diseno_personalizado
Contiene el motor principal de la interfaz gráfica de usuario.
*   **`flatlaf-3.7.2.jar`**: Flat Look and Feel. Es la librería principal que sobrescribe el diseño clásico de Java Swing, dándole a toda la aplicación un aspecto moderno, plano y profesional.

## 📂 fecha_hora
Agrupa el selector de fecha/hora de DJ-Raven y todas las dependencias gráficas que este requiere para funcionar correctamente y verse moderno.
*   **`swing-datetime-picker-2.1.4.jar`**: El componente principal creado por DJ-Raven que provee el calendario y el reloj emergente interactivo.
*   **`miglayout-core-11.3.jar` & `miglayout-swing-5.3.jar`**: Gestor de diseño (Layout Manager) avanzado que utiliza el selector de fecha internamente para acomodar sus elementos visuales de forma responsiva.
*   **`flatlaf-extras-3.7.2.jar`**: Extensiones de FlatLaf requeridas para soporte avanzado de componentes de terceros.
*   **`jsvg-2.1.0.jar`**: Motor de renderizado vectorial. Permite que los iconos dentro del selector de fechas (como las flechas) se dibujen usando SVG para que no pierdan calidad al redimensionarse.

## 📂 hasheador
Herramientas de criptografía y seguridad.
*   **`jbcrypt-0.4.jar`**: Implementación de Java del algoritmo OpenBSD bcrypt. Se utiliza para encriptar (hashear) las contraseñas de los usuarios antes de guardarlas en la base de datos, garantizando la seguridad de las credenciales.

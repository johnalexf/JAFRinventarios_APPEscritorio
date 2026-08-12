/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jafrinventarios.controladores.usuarios;

import jafrinventarios.modelos.ModeloSesionUsuario;
import jafrinventarios.vistas.usuarios.DialogoFormularioUsuario;
import jafrinventarios.vistas.utilidades.dialogos.DialogoMensajePersonalizado;
import jafrinventarios.vistas.utilidades.formularios.TipoDatoFormulario;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author JOHN FORERO
 */
public class ControladorDialogoUsuarios {
    
    private DialogoFormularioUsuario dialogoUsuario;
    private DialogoFormularioUsuario.TipoDialogo tipoDialogo;
    private boolean esAdministrador;
    private int idUsuario;
    
    private String mensajeExitoso;

    
    private ControladorDialogoUsuarios(
            DialogoFormularioUsuario dialogoUsuario, 
            DialogoFormularioUsuario.TipoDialogo tipoDialogo,
            boolean esAdminsitrador
        ) 
    {
        
        this.dialogoUsuario = dialogoUsuario;
        this.esAdministrador = esAdminsitrador;
        this.tipoDialogo = tipoDialogo;
        
        //TODO hacer la logica respectiva para separar si es el id del usuario registrado o de otro usuario a modificar en gestion de usuarios
        this.idUsuario = ModeloSesionUsuario.getInstancia().getIdUsuario();
        
        
         if(tipoDialogo != DialogoFormularioUsuario.TipoDialogo.EDITAR_PERFIL_PROPIO){
             inicializarComboBoxRoles();
         }
        
        
        if(tipoDialogo != DialogoFormularioUsuario.TipoDialogo.CREAR_NUEVO_USUARIO){
            cargarDatos(tipoDialogo);
            mensajeExitoso = "El usuario se ha actualizado correctamente";
        }else{
            mensajeExitoso = "Usuario creado correctamente \n La contraseña se le envia al usuario por correo";
            
        }
        
        inicializarEventosBotones();
        
        dialogoUsuario.hacerVisibleDialogo();
    }
    
    
    public static void crear(JFrame ventanaPadre, DialogoFormularioUsuario.TipoDialogo tipoDialogo, boolean esAdministrador){
        
        //Para pruebas se dejara esAdministrador como parametro de la funcion crear
        //boolean esAdminsitrador = ModeloSesionUsuario.getInstancia().esAdministrador();
    
        DialogoFormularioUsuario dialogoUsuario = 
                new DialogoFormularioUsuario( ventanaPadre,
                                              tipoDialogo,
                                              esAdministrador
                );
        
        new ControladorDialogoUsuarios(dialogoUsuario, tipoDialogo, esAdministrador);
        
    }
    
    
    private void cargarDatos(DialogoFormularioUsuario.TipoDialogo tipoDialogo){
        
        if(tipoDialogo == DialogoFormularioUsuario.TipoDialogo.EDITAR_OTRO_USUARIO){
            //TODO asignar el id al formulario
        }
        
        //Simluacion consulta a la base de datos para saber la informacion del usuario
        HashMap<String, String> datosBD = new HashMap<>();
        datosBD.put("alias", "johnalex");
        datosBD.put("rol", "Administrador");
        datosBD.put("primerNombre", "John");
        datosBD.put("primerApellido", "Forero");
        datosBD.put("nombreCompleto", "John Forero Rubio");
        datosBD.put("telefono", "3202173409");
        datosBD.put("correo", "john@gmail.com");
        /*
        TODO Se pretende crear una funcion en el modelo que devuelva toda la informacion
             sin la contreña, y complementando el campo nombreCompleto, inicialmente
             aun que se podria evaluar para que solo entregue la informacion necesaria
             en el momento a llegar a implementar se tomara dicha decision.
        */

        dialogoUsuario.asignarDatosEnFormulario(datosBD);
    
    }
    
    
    private void inicializarComboBoxRoles(){
        System.out.println("Simulando consulta de roles a la BD...");
        
        // Clave: Nombre del rol a mostrar | Valor: ID del rol en la base de datos
        HashMap<String, Integer> rolesBD = new HashMap<>();
        rolesBD.put("Administrador", 1);
        rolesBD.put("Vendedor", 2);
        
        // Le ordenamos a la vista que dibuje estos datos
        dialogoUsuario.inicializarComboBoxRoles(rolesBD);
    
    }
    
    
     // Activar eventos de escucha de clic en los botones
    private void inicializarEventosBotones() {
        
        dialogoUsuario.getBtnEnviarFormulario().addActionListener(e -> procesarFormulario());
        
    }
    
    
    private void procesarFormulario(){
        
        if( !dialogoUsuario.validarFormulario() ){
            System.out.println("Formulario no valido");
            DialogoMensajePersonalizado.mostrarErrorFormatoCampos(dialogoUsuario);
            return;
        }
        
        // Si la validación de campos pasa, pedimos los datos limpios a la vista
        HashMap<String, String> datosFormulario = dialogoUsuario.recolectarDatosFormulario();
        imprimirEnConsolaFormulario( datosFormulario );

        // TODO: conexion al modelo para que guarde los datos y responda
        switch(tipoDialogo){
            case EDITAR_PERFIL_PROPIO:
                //TODO: Consultar para editar perfil propio
                break;
            case EDITAR_OTRO_USUARIO:
                //TODO: Consultar para editar perfil propio
                break;
            case CREAR_NUEVO_USUARIO:
                //TODO: Consultar para crear nuevo usuario
                // La contraseña se envia por correo al nuevo usuario
                break;
        }
        
        boolean respuestaBD = true;

        if(respuestaBD){
            
            DialogoMensajePersonalizado.mostrarExito(
                    dialogoUsuario, 
                    "Operacion Exitosa", 
                    mensajeExitoso
            );
            dialogoUsuario.dispose();
            
        }else{

            //codigo simulacion de respuesta de la base de datos
            dialogoUsuario.mostrarErrorRespuestaBDEnFormulario(
               new HashMap<>(
                    Map.of(
                        "alias", "El alias ya existe",
                        "contrasena", "Contraseña erronea"
                    )
               )
            );

        }

    }
    
    
    // Funcion para verificar la recoleccion de datos del formulario
    private void imprimirEnConsolaFormulario( HashMap<String, String> datosFormulario ){
        datosFormulario.forEach(    (clave, valor) ->
            System.out.println(clave + " -> " + valor)
        );
    }
    
}

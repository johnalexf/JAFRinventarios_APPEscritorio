/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jafrinventarios.vistas.acceso.recuperacion;

import jafrinventarios.vistas.utilidades.validaciones.TipoDatoFormulario;
import jafrinventarios.vistas.utilidades.validaciones.ValidadorFormulario;
import jafrinventarios.vistas.utilidades.dialogos.DialogoBaseConSombra;
import jafrinventarios.vistas.utilidades.componentes.MostrarOcultarContrasena;
import jafrinventarios.vistas.utilidades.iconos.IconosBotones;
import java.awt.CardLayout;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author JOHN FORERO
 */
public class DialogoCambiarContrasena extends DialogoBaseConSombra {

    private final CardLayout layaoutTarjetas;
    private final ValidadorFormulario formularioCorreo = new ValidadorFormulario();
    private final ValidadorFormulario formularioCodigo = new ValidadorFormulario();
    private final ValidadorFormulario formularioContrasenaAntigua = new ValidadorFormulario();
    private final ValidadorFormulario formularioContrasenaNueva = new ValidadorFormulario();
    
    private final FormulariosTarjetas formularios = new FormulariosTarjetas();
    /**
     * Creates new form DialogoCambiarContrasena
     */
    private DialogoCambiarContrasena(JFrame parent, String titulo, TarjetasRecuperacion tarjeta, boolean contrasenaAntigua) {
        super(parent);
        initComponents();
        btnCerrar.setIcon(IconosBotones.CERRAR.getIcono());
        
        layaoutTarjetas = (CardLayout) contenedorTarjetas.getLayout();
        tituloDialogo.setText(titulo);
        
        declararIdentificadorsATarjetas();
        configurarDinamismoAContrasenas(contrasenaAntigua);
        inyectarCamposAValidadores(contrasenaAntigua);
        recolectarFormularios(contrasenaAntigua);
        mostrarTarjeta(tarjeta);
        hacerVisibleDialogo();
    }
    
    private void declararIdentificadorsATarjetas(){
        contenedorTarjetas.add(contenedorCorreo, TarjetasRecuperacion.CORREO.getIdentificador());
        contenedorTarjetas.add(contenedorConfirmarCodigo, TarjetasRecuperacion.CODIGO.getIdentificador());
        contenedorTarjetas.add(contenedorContrasenaAntigua, TarjetasRecuperacion.CONTRASENA_ANTIGUA.getIdentificador());
        contenedorTarjetas.add(contenedorContrasenaNueva, TarjetasRecuperacion.CONTRASENA_NUEVA.getIdentificador());
    }
    
    private void configurarDinamismoAContrasenas(boolean contrasenaAntigua){
        
        MostrarOcultarContrasena.agregarFuncionalidad(inputContrasenaNueva, btnMostrarOcultarContrasenaNueva);
        MostrarOcultarContrasena.agregarFuncionalidad(inputConfirmarContrasenaNueva, btnMostrarOcultarConfirmarContrasenaNueva);
        if(contrasenaAntigua){
           MostrarOcultarContrasena.agregarFuncionalidad(inputContrasenaAntigua, btnMostrarOcultarContrasenaAntigua); 
        }
    
    }
    
    private void inyectarCamposAValidadores(boolean contrasenaAntigua){
        if(contrasenaAntigua){
            formularioContrasenaAntigua.agregarCampo(
                    inputContrasenaAntigua, lblErrorInputContrasenaAntigua, TipoDatoFormulario.REQUERIDO, true);
        }else{
            formularioCorreo.agregarCampo(
                    inputCorreo, lblErrorInputCorreo, TipoDatoFormulario.CORREO, true);
            formularioCodigo.agregarCampo(
                    inputConfirmarCodigo, lblErrorInputConfirmarCodigo, TipoDatoFormulario.CODIGO, true);
        }
        
        formularioContrasenaNueva.agregarCampo(
                inputContrasenaNueva, lblErrorInputContrasenaNueva, TipoDatoFormulario.CONTRASENA, true);
        formularioContrasenaNueva.agregarCampoConfirmarContrasena(
                inputConfirmarContrasenaNueva, inputContrasenaNueva, lblErrorInputConfirmarContrasenaNueva);
    }
    
    
    private void recolectarFormularios( boolean contrasenaAntigua ){
        if( contrasenaAntigua ){
            formularios.agregarFormulario(TarjetasRecuperacion.CONTRASENA_ANTIGUA, formularioContrasenaAntigua);
        }else{
            formularios.agregarFormulario(TarjetasRecuperacion.CORREO, formularioCorreo);
            formularios.agregarFormulario(TarjetasRecuperacion.CODIGO, formularioCodigo);
        }
        
        formularios.agregarFormulario(TarjetasRecuperacion.CONTRASENA_NUEVA, formularioContrasenaNueva);
    
    }
    
    
    /* 
        Metodos publicos para controlar la creacion del dialogo segun 
        la necesidad, si es para recuperar o cambiar la contraseña.
    */
    public static void recuperarContrasena(JFrame padreFrame){
        
        DialogoCambiarContrasena dialogoRecuperarContrasena = 
                new DialogoCambiarContrasena( 
                        padreFrame, 
                        "Recuperar contraseña",
                        TarjetasRecuperacion.CORREO,
                        false
                );
       
    }
    

    public static void cambiarContrasena(JFrame padreFrame){
        DialogoCambiarContrasena dialogoCambiarContrasena = 
                new DialogoCambiarContrasena( 
                        padreFrame, 
                        "Cambiar contraseña",
                        TarjetasRecuperacion.CONTRASENA_ANTIGUA,
                        true
                );
  
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        contenedorFondoBlanco = new javax.swing.JPanel();
        contenedorHeader = new javax.swing.JPanel();
        margin_left = new javax.swing.Box.Filler(new java.awt.Dimension(40, 0), new java.awt.Dimension(40, 0), new java.awt.Dimension(40, 32767));
        tituloDialogo = new javax.swing.JLabel();
        contenedorBtnCerrar = new javax.swing.JPanel();
        btnCerrar = new javax.swing.JButton();
        contenedorTarjetas = new javax.swing.JPanel();
        contenedorCorreo = new javax.swing.JPanel();
        contenedorEscribaCorreo = new javax.swing.JPanel();
        lblCorreo = new javax.swing.JLabel();
        contenedorInputYErrorCorreo = new javax.swing.JPanel();
        inputCorreo = new javax.swing.JTextField();
        lblErrorInputCorreo = new javax.swing.JLabel();
        contenedorBtnEnviarCodigo = new javax.swing.JPanel();
        btnEnviarCodigo = new javax.swing.JButton();
        contenedorConfirmarCodigo = new javax.swing.JPanel();
        contenedorEscribaCodigo = new javax.swing.JPanel();
        lblCodigo = new javax.swing.JLabel();
        contenedorInputYErrorConfirmarCodigo = new javax.swing.JPanel();
        inputConfirmarCodigo = new javax.swing.JTextField();
        lblErrorInputConfirmarCodigo = new javax.swing.JLabel();
        contenedorBtnConfirmarCodigo = new javax.swing.JPanel();
        btnConfirmarCodigo = new javax.swing.JButton();
        contenedorContrasenaAntigua = new javax.swing.JPanel();
        contenedorEscribaContrasenaAntigua = new javax.swing.JPanel();
        lblContrasenaAntigua = new javax.swing.JLabel();
        contenedorInputYErrorContrasenaAntigua = new javax.swing.JPanel();
        contenedorInputContrasenaAntigua = new javax.swing.JPanel();
        contenedorBtnMostrarOcultarContrasenaAntigua = new javax.swing.JPanel();
        btnMostrarOcultarContrasenaAntigua = new javax.swing.JButton();
        inputContrasenaAntigua = new javax.swing.JPasswordField();
        lblErrorInputContrasenaAntigua = new javax.swing.JLabel();
        contenedorBtnConfirmarContrasenaAntigua = new javax.swing.JPanel();
        btnConfirmarContrasenaAntigua = new javax.swing.JButton();
        contenedorContrasenaNueva = new javax.swing.JPanel();
        contenedorEscribaContrasenaNueva = new javax.swing.JPanel();
        lblContrasenaNueva = new javax.swing.JLabel();
        contenedorInputYErrorContrasenaNueva = new javax.swing.JPanel();
        contenedorInputContrasenaNueva = new javax.swing.JPanel();
        contenedorBtnMostrarOcultarContrasenaNueva = new javax.swing.JPanel();
        btnMostrarOcultarContrasenaNueva = new javax.swing.JButton();
        inputContrasenaNueva = new javax.swing.JPasswordField();
        lblErrorInputContrasenaNueva = new javax.swing.JLabel();
        lblTextAreaDescripcionContrasena = new javax.swing.JTextArea();
        lblConfirmarContrasenaNueva = new javax.swing.JLabel();
        contenedorInputYErrorConfirmarContrasenaNueva = new javax.swing.JPanel();
        contenedorInputConfirmarContrasenaNueva = new javax.swing.JPanel();
        contenedorBtnMostrarOcultarConfirmarContrasenaNueva = new javax.swing.JPanel();
        btnMostrarOcultarConfirmarContrasenaNueva = new javax.swing.JButton();
        inputConfirmarContrasenaNueva = new javax.swing.JPasswordField();
        lblErrorInputConfirmarContrasenaNueva = new javax.swing.JLabel();
        contenedorBtnCambiarContrasena = new javax.swing.JPanel();
        btnCambiarContrasena = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(500, 350));
        setName("dialogCambiarContrasena"); // NOI18N
        setUndecorated(true);

        contenedorFondoBlanco.setMinimumSize(new java.awt.Dimension(500, 380));
        contenedorFondoBlanco.setPreferredSize(new java.awt.Dimension(500, 380));
        contenedorFondoBlanco.setLayout(new java.awt.BorderLayout());

        contenedorHeader.setBackground(new java.awt.Color(255, 255, 255));
        contenedorHeader.setMinimumSize(new java.awt.Dimension(0, 70));
        contenedorHeader.setPreferredSize(new java.awt.Dimension(0, 70));
        contenedorHeader.setLayout(new java.awt.BorderLayout());
        contenedorHeader.add(margin_left, java.awt.BorderLayout.WEST);

        tituloDialogo.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        tituloDialogo.setForeground(new java.awt.Color(17, 35, 85));
        tituloDialogo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tituloDialogo.setText("Recuperar Contraseña");
        tituloDialogo.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        tituloDialogo.setMaximumSize(new java.awt.Dimension(3000, 60));
        tituloDialogo.setMinimumSize(new java.awt.Dimension(0, 60));
        tituloDialogo.setPreferredSize(new java.awt.Dimension(0, 60));
        contenedorHeader.add(tituloDialogo, java.awt.BorderLayout.CENTER);

        contenedorBtnCerrar.setMinimumSize(new java.awt.Dimension(40, 0));
        contenedorBtnCerrar.setOpaque(false);
        contenedorBtnCerrar.setPreferredSize(new java.awt.Dimension(40, 0));

        btnCerrar.setBackground(new java.awt.Color(255, 255, 255));
        btnCerrar.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        btnCerrar.setForeground(new java.awt.Color(17, 35, 85));
        btnCerrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jafrinventarios/recursos/iconos/botones/carbon--close-filled.png"))); // NOI18N
        btnCerrar.setToolTipText("");
        btnCerrar.setAlignmentY(0.0F);
        btnCerrar.setBorderPainted(false);
        btnCerrar.setFocusCycleRoot(true);
        btnCerrar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCerrar.setIconTextGap(0);
        btnCerrar.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnCerrar.setMaximumSize(new java.awt.Dimension(30, 26));
        btnCerrar.setMinimumSize(new java.awt.Dimension(30, 26));
        btnCerrar.setPreferredSize(new java.awt.Dimension(30, 26));
        btnCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarcerrarDialogo(evt);
            }
        });
        contenedorBtnCerrar.add(btnCerrar);

        contenedorHeader.add(contenedorBtnCerrar, java.awt.BorderLayout.EAST);

        contenedorFondoBlanco.add(contenedorHeader, java.awt.BorderLayout.PAGE_START);

        contenedorTarjetas.setBackground(new java.awt.Color(255, 255, 255));
        contenedorTarjetas.setLayout(new java.awt.CardLayout());

        contenedorCorreo.setOpaque(false);
        contenedorCorreo.setLayout(new java.awt.BorderLayout());

        contenedorEscribaCorreo.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 40, 0, 40));
        contenedorEscribaCorreo.setMinimumSize(new java.awt.Dimension(0, 0));
        contenedorEscribaCorreo.setOpaque(false);
        contenedorEscribaCorreo.setPreferredSize(new java.awt.Dimension(0, 0));
        contenedorEscribaCorreo.setLayout(new java.awt.GridLayout(2, 1));

        lblCorreo.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        lblCorreo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCorreo.setText("Escriba su correo y presione el botón enviar código. ");
        lblCorreo.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        lblCorreo.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 20, 1));
        contenedorEscribaCorreo.add(lblCorreo);

        contenedorInputYErrorCorreo.setOpaque(false);
        contenedorInputYErrorCorreo.setPreferredSize(new java.awt.Dimension(0, 60));
        contenedorInputYErrorCorreo.setLayout(new java.awt.BorderLayout());

        inputCorreo.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        inputCorreo.setMargin(new java.awt.Insets(4, 10, 4, 10));
        inputCorreo.setMinimumSize(new java.awt.Dimension(0, 45));
        inputCorreo.setName("correo"); // NOI18N
        inputCorreo.setPreferredSize(new java.awt.Dimension(0, 41));
        contenedorInputYErrorCorreo.add(inputCorreo, java.awt.BorderLayout.NORTH);

        lblErrorInputCorreo.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        lblErrorInputCorreo.setForeground(new java.awt.Color(179, 38, 30));
        lblErrorInputCorreo.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblErrorInputCorreo.setAlignmentX(0.5F);
        lblErrorInputCorreo.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 8, 0, 0));
        lblErrorInputCorreo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblErrorInputCorreo.setPreferredSize(new java.awt.Dimension(0, 17));
        contenedorInputYErrorCorreo.add(lblErrorInputCorreo, java.awt.BorderLayout.CENTER);

        contenedorEscribaCorreo.add(contenedorInputYErrorCorreo);

        contenedorCorreo.add(contenedorEscribaCorreo, java.awt.BorderLayout.CENTER);

        contenedorBtnEnviarCodigo.setMinimumSize(new java.awt.Dimension(89, 100));
        contenedorBtnEnviarCodigo.setOpaque(false);
        contenedorBtnEnviarCodigo.setPreferredSize(new java.awt.Dimension(555, 100));

        btnEnviarCodigo.setBackground(new java.awt.Color(30, 166, 177));
        btnEnviarCodigo.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        btnEnviarCodigo.setForeground(new java.awt.Color(255, 255, 255));
        btnEnviarCodigo.setText("Enviar Código");
        btnEnviarCodigo.setToolTipText("");
        btnEnviarCodigo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEnviarCodigo.setMargin(new java.awt.Insets(6, 12, 6, 12));
        btnEnviarCodigo.setMaximumSize(new java.awt.Dimension(200, 40));
        btnEnviarCodigo.setMinimumSize(new java.awt.Dimension(200, 40));
        btnEnviarCodigo.setPreferredSize(new java.awt.Dimension(200, 40));
        contenedorBtnEnviarCodigo.add(btnEnviarCodigo);

        contenedorCorreo.add(contenedorBtnEnviarCodigo, java.awt.BorderLayout.SOUTH);

        contenedorTarjetas.add(contenedorCorreo, "cardCorreo");

        contenedorConfirmarCodigo.setOpaque(false);
        contenedorConfirmarCodigo.setLayout(new java.awt.BorderLayout());

        contenedorEscribaCodigo.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 40, 0, 40));
        contenedorEscribaCodigo.setMinimumSize(new java.awt.Dimension(0, 0));
        contenedorEscribaCodigo.setOpaque(false);
        contenedorEscribaCodigo.setPreferredSize(new java.awt.Dimension(0, 0));
        contenedorEscribaCodigo.setLayout(new java.awt.GridLayout(2, 1));

        lblCodigo.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        lblCodigo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCodigo.setText("Escriba el código que llego a su correo");
        lblCodigo.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        lblCodigo.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 20, 1));
        contenedorEscribaCodigo.add(lblCodigo);

        contenedorInputYErrorConfirmarCodigo.setOpaque(false);
        contenedorInputYErrorConfirmarCodigo.setPreferredSize(new java.awt.Dimension(0, 60));
        contenedorInputYErrorConfirmarCodigo.setLayout(new java.awt.BorderLayout());

        inputConfirmarCodigo.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        inputConfirmarCodigo.setMargin(new java.awt.Insets(4, 10, 4, 10));
        inputConfirmarCodigo.setMinimumSize(new java.awt.Dimension(0, 45));
        inputConfirmarCodigo.setName("codigo"); // NOI18N
        inputConfirmarCodigo.setPreferredSize(new java.awt.Dimension(0, 41));
        contenedorInputYErrorConfirmarCodigo.add(inputConfirmarCodigo, java.awt.BorderLayout.NORTH);

        lblErrorInputConfirmarCodigo.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        lblErrorInputConfirmarCodigo.setForeground(new java.awt.Color(179, 38, 30));
        lblErrorInputConfirmarCodigo.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblErrorInputConfirmarCodigo.setAlignmentX(0.5F);
        lblErrorInputConfirmarCodigo.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 8, 0, 0));
        lblErrorInputConfirmarCodigo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblErrorInputConfirmarCodigo.setPreferredSize(new java.awt.Dimension(0, 17));
        contenedorInputYErrorConfirmarCodigo.add(lblErrorInputConfirmarCodigo, java.awt.BorderLayout.CENTER);

        contenedorEscribaCodigo.add(contenedorInputYErrorConfirmarCodigo);

        contenedorConfirmarCodigo.add(contenedorEscribaCodigo, java.awt.BorderLayout.CENTER);

        contenedorBtnConfirmarCodigo.setMinimumSize(new java.awt.Dimension(89, 100));
        contenedorBtnConfirmarCodigo.setOpaque(false);
        contenedorBtnConfirmarCodigo.setPreferredSize(new java.awt.Dimension(555, 100));

        btnConfirmarCodigo.setBackground(new java.awt.Color(30, 166, 177));
        btnConfirmarCodigo.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        btnConfirmarCodigo.setForeground(new java.awt.Color(255, 255, 255));
        btnConfirmarCodigo.setText("Confirmar Código");
        btnConfirmarCodigo.setToolTipText("");
        btnConfirmarCodigo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnConfirmarCodigo.setMargin(new java.awt.Insets(6, 12, 6, 12));
        btnConfirmarCodigo.setMaximumSize(new java.awt.Dimension(200, 40));
        btnConfirmarCodigo.setMinimumSize(new java.awt.Dimension(200, 40));
        btnConfirmarCodigo.setPreferredSize(new java.awt.Dimension(200, 40));
        contenedorBtnConfirmarCodigo.add(btnConfirmarCodigo);

        contenedorConfirmarCodigo.add(contenedorBtnConfirmarCodigo, java.awt.BorderLayout.SOUTH);

        contenedorTarjetas.add(contenedorConfirmarCodigo, "cardConfirmarCodigo");

        contenedorContrasenaAntigua.setOpaque(false);
        contenedorContrasenaAntigua.setLayout(new java.awt.BorderLayout());

        contenedorEscribaContrasenaAntigua.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 40, 0, 40));
        contenedorEscribaContrasenaAntigua.setMinimumSize(new java.awt.Dimension(0, 0));
        contenedorEscribaContrasenaAntigua.setOpaque(false);
        contenedorEscribaContrasenaAntigua.setPreferredSize(new java.awt.Dimension(0, 0));
        contenedorEscribaContrasenaAntigua.setLayout(new java.awt.GridLayout(2, 1));

        lblContrasenaAntigua.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        lblContrasenaAntigua.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblContrasenaAntigua.setText("Escriba su contraseña actual");
        lblContrasenaAntigua.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        lblContrasenaAntigua.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 20, 1));
        contenedorEscribaContrasenaAntigua.add(lblContrasenaAntigua);

        contenedorInputYErrorContrasenaAntigua.setOpaque(false);
        contenedorInputYErrorContrasenaAntigua.setPreferredSize(new java.awt.Dimension(0, 52));
        contenedorInputYErrorContrasenaAntigua.setLayout(new java.awt.BorderLayout());

        contenedorInputContrasenaAntigua.setBackground(new java.awt.Color(255, 255, 255));
        contenedorInputContrasenaAntigua.setMaximumSize(new java.awt.Dimension(0, 0));
        contenedorInputContrasenaAntigua.setMinimumSize(new java.awt.Dimension(0, 0));
        contenedorInputContrasenaAntigua.setName(""); // NOI18N
        contenedorInputContrasenaAntigua.setOpaque(false);
        contenedorInputContrasenaAntigua.setPreferredSize(new java.awt.Dimension(0, 41));
        contenedorInputContrasenaAntigua.setLayout(new javax.swing.OverlayLayout(contenedorInputContrasenaAntigua));

        contenedorBtnMostrarOcultarContrasenaAntigua.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 4));
        contenedorBtnMostrarOcultarContrasenaAntigua.setAlignmentY(0.55F);
        contenedorBtnMostrarOcultarContrasenaAntigua.setOpaque(false);
        contenedorBtnMostrarOcultarContrasenaAntigua.setPreferredSize(new java.awt.Dimension(0, 34));
        contenedorBtnMostrarOcultarContrasenaAntigua.setLayout(new javax.swing.BoxLayout(contenedorBtnMostrarOcultarContrasenaAntigua, javax.swing.BoxLayout.Y_AXIS));

        btnMostrarOcultarContrasenaAntigua.setBackground(new java.awt.Color(255, 255, 255));
        btnMostrarOcultarContrasenaAntigua.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        btnMostrarOcultarContrasenaAntigua.setForeground(new java.awt.Color(17, 35, 85));
        btnMostrarOcultarContrasenaAntigua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jafrinventarios/recursos/iconos/botones/mdi-light--eye-off.png"))); // NOI18N
        btnMostrarOcultarContrasenaAntigua.setToolTipText("");
        btnMostrarOcultarContrasenaAntigua.setAlignmentX(1.0F);
        btnMostrarOcultarContrasenaAntigua.setAlignmentY(0.0F);
        btnMostrarOcultarContrasenaAntigua.setBorderPainted(false);
        btnMostrarOcultarContrasenaAntigua.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMostrarOcultarContrasenaAntigua.setFocusCycleRoot(true);
        btnMostrarOcultarContrasenaAntigua.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMostrarOcultarContrasenaAntigua.setIconTextGap(0);
        btnMostrarOcultarContrasenaAntigua.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnMostrarOcultarContrasenaAntigua.setMaximumSize(new java.awt.Dimension(30, 26));
        btnMostrarOcultarContrasenaAntigua.setMinimumSize(new java.awt.Dimension(30, 26));
        btnMostrarOcultarContrasenaAntigua.setPreferredSize(new java.awt.Dimension(30, 26));
        contenedorBtnMostrarOcultarContrasenaAntigua.add(btnMostrarOcultarContrasenaAntigua);

        contenedorInputContrasenaAntigua.add(contenedorBtnMostrarOcultarContrasenaAntigua);

        inputContrasenaAntigua.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        inputContrasenaAntigua.setToolTipText("");
        inputContrasenaAntigua.setMargin(new java.awt.Insets(4, 10, 4, 10));
        inputContrasenaAntigua.setMinimumSize(new java.awt.Dimension(0, 34));
        inputContrasenaAntigua.setName("contrasena"); // NOI18N
        inputContrasenaAntigua.setPreferredSize(new java.awt.Dimension(0, 34));
        contenedorInputContrasenaAntigua.add(inputContrasenaAntigua);

        contenedorInputYErrorContrasenaAntigua.add(contenedorInputContrasenaAntigua, java.awt.BorderLayout.NORTH);

        lblErrorInputContrasenaAntigua.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        lblErrorInputContrasenaAntigua.setForeground(new java.awt.Color(179, 38, 30));
        lblErrorInputContrasenaAntigua.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblErrorInputContrasenaAntigua.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 8, 0, 0));
        lblErrorInputContrasenaAntigua.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblErrorInputContrasenaAntigua.setPreferredSize(new java.awt.Dimension(0, 17));
        lblErrorInputContrasenaAntigua.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        contenedorInputYErrorContrasenaAntigua.add(lblErrorInputContrasenaAntigua, java.awt.BorderLayout.CENTER);

        contenedorEscribaContrasenaAntigua.add(contenedorInputYErrorContrasenaAntigua);

        contenedorContrasenaAntigua.add(contenedorEscribaContrasenaAntigua, java.awt.BorderLayout.CENTER);

        contenedorBtnConfirmarContrasenaAntigua.setMinimumSize(new java.awt.Dimension(89, 100));
        contenedorBtnConfirmarContrasenaAntigua.setOpaque(false);
        contenedorBtnConfirmarContrasenaAntigua.setPreferredSize(new java.awt.Dimension(555, 100));

        btnConfirmarContrasenaAntigua.setBackground(new java.awt.Color(30, 166, 177));
        btnConfirmarContrasenaAntigua.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        btnConfirmarContrasenaAntigua.setForeground(new java.awt.Color(255, 255, 255));
        btnConfirmarContrasenaAntigua.setText("Confirmar");
        btnConfirmarContrasenaAntigua.setToolTipText("");
        btnConfirmarContrasenaAntigua.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnConfirmarContrasenaAntigua.setMargin(new java.awt.Insets(6, 12, 6, 12));
        btnConfirmarContrasenaAntigua.setMaximumSize(new java.awt.Dimension(200, 40));
        btnConfirmarContrasenaAntigua.setMinimumSize(new java.awt.Dimension(200, 40));
        btnConfirmarContrasenaAntigua.setPreferredSize(new java.awt.Dimension(200, 40));
        contenedorBtnConfirmarContrasenaAntigua.add(btnConfirmarContrasenaAntigua);

        contenedorContrasenaAntigua.add(contenedorBtnConfirmarContrasenaAntigua, java.awt.BorderLayout.SOUTH);

        contenedorTarjetas.add(contenedorContrasenaAntigua, "cardContrasenaAntigua");

        contenedorContrasenaNueva.setOpaque(false);
        contenedorContrasenaNueva.setLayout(new java.awt.BorderLayout());

        contenedorEscribaContrasenaNueva.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 40, 0, 40));
        contenedorEscribaContrasenaNueva.setOpaque(false);
        contenedorEscribaContrasenaNueva.setLayout(new java.awt.GridLayout(5, 1));

        lblContrasenaNueva.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        lblContrasenaNueva.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblContrasenaNueva.setText("Contraseña nueva:");
        lblContrasenaNueva.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        lblContrasenaNueva.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 20, 1));
        contenedorEscribaContrasenaNueva.add(lblContrasenaNueva);

        contenedorInputYErrorContrasenaNueva.setOpaque(false);
        contenedorInputYErrorContrasenaNueva.setPreferredSize(new java.awt.Dimension(0, 52));
        contenedorInputYErrorContrasenaNueva.setLayout(new java.awt.BorderLayout());

        contenedorInputContrasenaNueva.setBackground(new java.awt.Color(255, 255, 255));
        contenedorInputContrasenaNueva.setMaximumSize(new java.awt.Dimension(0, 0));
        contenedorInputContrasenaNueva.setMinimumSize(new java.awt.Dimension(0, 0));
        contenedorInputContrasenaNueva.setName(""); // NOI18N
        contenedorInputContrasenaNueva.setOpaque(false);
        contenedorInputContrasenaNueva.setPreferredSize(new java.awt.Dimension(0, 41));
        contenedorInputContrasenaNueva.setLayout(new javax.swing.OverlayLayout(contenedorInputContrasenaNueva));

        contenedorBtnMostrarOcultarContrasenaNueva.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 4));
        contenedorBtnMostrarOcultarContrasenaNueva.setAlignmentY(0.55F);
        contenedorBtnMostrarOcultarContrasenaNueva.setOpaque(false);
        contenedorBtnMostrarOcultarContrasenaNueva.setPreferredSize(new java.awt.Dimension(0, 34));
        contenedorBtnMostrarOcultarContrasenaNueva.setLayout(new javax.swing.BoxLayout(contenedorBtnMostrarOcultarContrasenaNueva, javax.swing.BoxLayout.Y_AXIS));

        btnMostrarOcultarContrasenaNueva.setBackground(new java.awt.Color(255, 255, 255));
        btnMostrarOcultarContrasenaNueva.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        btnMostrarOcultarContrasenaNueva.setForeground(new java.awt.Color(17, 35, 85));
        btnMostrarOcultarContrasenaNueva.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jafrinventarios/recursos/iconos/botones/mdi-light--eye-off.png"))); // NOI18N
        btnMostrarOcultarContrasenaNueva.setToolTipText("");
        btnMostrarOcultarContrasenaNueva.setAlignmentX(1.0F);
        btnMostrarOcultarContrasenaNueva.setAlignmentY(0.0F);
        btnMostrarOcultarContrasenaNueva.setBorderPainted(false);
        btnMostrarOcultarContrasenaNueva.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMostrarOcultarContrasenaNueva.setFocusCycleRoot(true);
        btnMostrarOcultarContrasenaNueva.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMostrarOcultarContrasenaNueva.setIconTextGap(0);
        btnMostrarOcultarContrasenaNueva.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnMostrarOcultarContrasenaNueva.setMaximumSize(new java.awt.Dimension(30, 26));
        btnMostrarOcultarContrasenaNueva.setMinimumSize(new java.awt.Dimension(30, 26));
        btnMostrarOcultarContrasenaNueva.setPreferredSize(new java.awt.Dimension(30, 26));
        contenedorBtnMostrarOcultarContrasenaNueva.add(btnMostrarOcultarContrasenaNueva);

        contenedorInputContrasenaNueva.add(contenedorBtnMostrarOcultarContrasenaNueva);

        inputContrasenaNueva.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        inputContrasenaNueva.setToolTipText("");
        inputContrasenaNueva.setMargin(new java.awt.Insets(4, 10, 4, 10));
        inputContrasenaNueva.setMinimumSize(new java.awt.Dimension(0, 34));
        inputContrasenaNueva.setName("contrasenaNueva"); // NOI18N
        inputContrasenaNueva.setPreferredSize(new java.awt.Dimension(0, 34));
        contenedorInputContrasenaNueva.add(inputContrasenaNueva);

        contenedorInputYErrorContrasenaNueva.add(contenedorInputContrasenaNueva, java.awt.BorderLayout.NORTH);

        lblErrorInputContrasenaNueva.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        lblErrorInputContrasenaNueva.setForeground(new java.awt.Color(179, 38, 30));
        lblErrorInputContrasenaNueva.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblErrorInputContrasenaNueva.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 8, 0, 0));
        lblErrorInputContrasenaNueva.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblErrorInputContrasenaNueva.setPreferredSize(new java.awt.Dimension(0, 17));
        lblErrorInputContrasenaNueva.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        contenedorInputYErrorContrasenaNueva.add(lblErrorInputContrasenaNueva, java.awt.BorderLayout.CENTER);

        contenedorEscribaContrasenaNueva.add(contenedorInputYErrorContrasenaNueva);

        lblTextAreaDescripcionContrasena.setEditable(false);
        lblTextAreaDescripcionContrasena.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        lblTextAreaDescripcionContrasena.setRows(5);
        lblTextAreaDescripcionContrasena.setText("La contraseña debe contener por lo menos:\n* Una mayuscula\n* Una minuscula\n* Un número\n* Un caracter especial\n* Minimo 8 caraceres");
        lblTextAreaDescripcionContrasena.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 8, 0, 0));
        lblTextAreaDescripcionContrasena.setCaretPosition(0);
        lblTextAreaDescripcionContrasena.setMinimumSize(new java.awt.Dimension(0, 0));
        lblTextAreaDescripcionContrasena.setOpaque(false);
        lblTextAreaDescripcionContrasena.setPreferredSize(new java.awt.Dimension(0, 90));
        contenedorEscribaContrasenaNueva.add(lblTextAreaDescripcionContrasena);

        lblConfirmarContrasenaNueva.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        lblConfirmarContrasenaNueva.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblConfirmarContrasenaNueva.setText("Confirmar Contraseña nueva:");
        lblConfirmarContrasenaNueva.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        lblConfirmarContrasenaNueva.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 20, 1));
        contenedorEscribaContrasenaNueva.add(lblConfirmarContrasenaNueva);

        contenedorInputYErrorConfirmarContrasenaNueva.setOpaque(false);
        contenedorInputYErrorConfirmarContrasenaNueva.setPreferredSize(new java.awt.Dimension(0, 52));
        contenedorInputYErrorConfirmarContrasenaNueva.setLayout(new java.awt.BorderLayout());

        contenedorInputConfirmarContrasenaNueva.setBackground(new java.awt.Color(255, 255, 255));
        contenedorInputConfirmarContrasenaNueva.setMaximumSize(new java.awt.Dimension(0, 0));
        contenedorInputConfirmarContrasenaNueva.setMinimumSize(new java.awt.Dimension(0, 0));
        contenedorInputConfirmarContrasenaNueva.setName(""); // NOI18N
        contenedorInputConfirmarContrasenaNueva.setOpaque(false);
        contenedorInputConfirmarContrasenaNueva.setPreferredSize(new java.awt.Dimension(0, 41));
        contenedorInputConfirmarContrasenaNueva.setLayout(new javax.swing.OverlayLayout(contenedorInputConfirmarContrasenaNueva));

        contenedorBtnMostrarOcultarConfirmarContrasenaNueva.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 4));
        contenedorBtnMostrarOcultarConfirmarContrasenaNueva.setAlignmentY(0.55F);
        contenedorBtnMostrarOcultarConfirmarContrasenaNueva.setOpaque(false);
        contenedorBtnMostrarOcultarConfirmarContrasenaNueva.setPreferredSize(new java.awt.Dimension(0, 34));
        contenedorBtnMostrarOcultarConfirmarContrasenaNueva.setLayout(new javax.swing.BoxLayout(contenedorBtnMostrarOcultarConfirmarContrasenaNueva, javax.swing.BoxLayout.Y_AXIS));

        btnMostrarOcultarConfirmarContrasenaNueva.setBackground(new java.awt.Color(255, 255, 255));
        btnMostrarOcultarConfirmarContrasenaNueva.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        btnMostrarOcultarConfirmarContrasenaNueva.setForeground(new java.awt.Color(17, 35, 85));
        btnMostrarOcultarConfirmarContrasenaNueva.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jafrinventarios/recursos/iconos/botones/mdi-light--eye-off.png"))); // NOI18N
        btnMostrarOcultarConfirmarContrasenaNueva.setToolTipText("");
        btnMostrarOcultarConfirmarContrasenaNueva.setAlignmentX(1.0F);
        btnMostrarOcultarConfirmarContrasenaNueva.setAlignmentY(0.0F);
        btnMostrarOcultarConfirmarContrasenaNueva.setBorderPainted(false);
        btnMostrarOcultarConfirmarContrasenaNueva.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnMostrarOcultarConfirmarContrasenaNueva.setFocusCycleRoot(true);
        btnMostrarOcultarConfirmarContrasenaNueva.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMostrarOcultarConfirmarContrasenaNueva.setIconTextGap(0);
        btnMostrarOcultarConfirmarContrasenaNueva.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnMostrarOcultarConfirmarContrasenaNueva.setMaximumSize(new java.awt.Dimension(30, 26));
        btnMostrarOcultarConfirmarContrasenaNueva.setMinimumSize(new java.awt.Dimension(30, 26));
        btnMostrarOcultarConfirmarContrasenaNueva.setPreferredSize(new java.awt.Dimension(30, 26));
        contenedorBtnMostrarOcultarConfirmarContrasenaNueva.add(btnMostrarOcultarConfirmarContrasenaNueva);

        contenedorInputConfirmarContrasenaNueva.add(contenedorBtnMostrarOcultarConfirmarContrasenaNueva);

        inputConfirmarContrasenaNueva.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        inputConfirmarContrasenaNueva.setToolTipText("");
        inputConfirmarContrasenaNueva.setMargin(new java.awt.Insets(4, 10, 4, 10));
        inputConfirmarContrasenaNueva.setMinimumSize(new java.awt.Dimension(0, 34));
        inputConfirmarContrasenaNueva.setName("confirmarContrasenaNueva"); // NOI18N
        inputConfirmarContrasenaNueva.setPreferredSize(new java.awt.Dimension(0, 34));
        contenedorInputConfirmarContrasenaNueva.add(inputConfirmarContrasenaNueva);

        contenedorInputYErrorConfirmarContrasenaNueva.add(contenedorInputConfirmarContrasenaNueva, java.awt.BorderLayout.NORTH);

        lblErrorInputConfirmarContrasenaNueva.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        lblErrorInputConfirmarContrasenaNueva.setForeground(new java.awt.Color(179, 38, 30));
        lblErrorInputConfirmarContrasenaNueva.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblErrorInputConfirmarContrasenaNueva.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 8, 0, 0));
        lblErrorInputConfirmarContrasenaNueva.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblErrorInputConfirmarContrasenaNueva.setPreferredSize(new java.awt.Dimension(0, 17));
        lblErrorInputConfirmarContrasenaNueva.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        contenedorInputYErrorConfirmarContrasenaNueva.add(lblErrorInputConfirmarContrasenaNueva, java.awt.BorderLayout.CENTER);

        contenedorEscribaContrasenaNueva.add(contenedorInputYErrorConfirmarContrasenaNueva);

        contenedorContrasenaNueva.add(contenedorEscribaContrasenaNueva, java.awt.BorderLayout.CENTER);

        contenedorBtnCambiarContrasena.setMinimumSize(new java.awt.Dimension(89, 100));
        contenedorBtnCambiarContrasena.setOpaque(false);
        contenedorBtnCambiarContrasena.setPreferredSize(new java.awt.Dimension(555, 100));

        btnCambiarContrasena.setBackground(new java.awt.Color(30, 166, 177));
        btnCambiarContrasena.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        btnCambiarContrasena.setForeground(new java.awt.Color(255, 255, 255));
        btnCambiarContrasena.setText("Cambiar contraseña");
        btnCambiarContrasena.setToolTipText("");
        btnCambiarContrasena.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCambiarContrasena.setMargin(new java.awt.Insets(6, 12, 6, 12));
        btnCambiarContrasena.setMaximumSize(new java.awt.Dimension(220, 40));
        btnCambiarContrasena.setMinimumSize(new java.awt.Dimension(220, 40));
        btnCambiarContrasena.setPreferredSize(new java.awt.Dimension(220, 40));
        contenedorBtnCambiarContrasena.add(btnCambiarContrasena);

        contenedorContrasenaNueva.add(contenedorBtnCambiarContrasena, java.awt.BorderLayout.SOUTH);

        contenedorTarjetas.add(contenedorContrasenaNueva, "cardContrasenaNueva");

        contenedorFondoBlanco.add(contenedorTarjetas, java.awt.BorderLayout.CENTER);

        getContentPane().add(contenedorFondoBlanco, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCerrarcerrarDialogo(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarcerrarDialogo
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnCerrarcerrarDialogo
   
    
    // =======================================================
    // MÉTODOS PÚBLICOS PARA EL CONTROLADOR
    // =======================================================
    
    //Exponer botones
    public JButton getBtnEnviarCodigo(){
        return btnEnviarCodigo;
    }
    
    public JButton getBtnConfirmarCodigo(){
        return btnConfirmarCodigo;
    }
        
    public JButton getBtnConfirmarContrasenaAntigua(){
        return btnConfirmarContrasenaAntigua;
    }
    
    public JButton getBtnCambiarContrasena(){
        return btnCambiarContrasena;
    }

    // Exponer la validación visual
    public boolean ejecutarValidacionCampos( TarjetasRecuperacion claveTarjeta ) {
        return formularios.validar( claveTarjeta );
    }
    
    // Exponer los datos del formulario en un hash map
    public HashMap<String, String> recolectarDatosFormulario( TarjetasRecuperacion claveTarjeta ){
        return formularios.recolectarDatos( claveTarjeta );
    }
    
    // Exponer mostrar errrores de la respuesta a la consulta de la BD en lblError
    public void mostrarErrorRespuestaBD(
                            TarjetasRecuperacion claveTarjeta,
                            HashMap<String, String> erroresCamposBD ){
        formularios.mostrarErrorRespuestaBD( claveTarjeta, erroresCamposBD);
    }

    
    // =======================================================
    // MÉTODOS PARA CAMBIAR TARJETAS DE FORMULARIO
    // =======================================================
    
    public void mostrarTarjeta(TarjetasRecuperacion tarjeta){
        // Mostramos la tarjeta correspondiente usando el CardLayout original
        layaoutTarjetas.show(contenedorTarjetas, tarjeta.getIdentificador());
        
        // Evaluamos si la tarjeta actual es la de la contraseña nueva
        // Para poder redimensionar el Dialog ya que esta card ocupa mas espacio
        if (  tarjeta == TarjetasRecuperacion.CONTRASENA_NUEVA  ) {
            // Aumentamos el tamaño del JDialog (500 de ancho por 640 de alto)
            this.setSize(500, 640); 
        } else {
            // Si es cualquier otra tarjeta, mantenemos el tamaño original de 500x320
            this.setSize(500, 320); 
        }
        
        // Como la ventana cambió de tamaño, se verá desfasada de su centro original.
        // Volvemos a centrarla respecto a la ventana padre.
        this.setLocationRelativeTo(this.getParent());
        
        // Refrescamos los componentes gráficos para evitar deformaciones visuales
        this.revalidate();
        this.repaint();
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCambiarContrasena;
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnConfirmarCodigo;
    private javax.swing.JButton btnConfirmarContrasenaAntigua;
    private javax.swing.JButton btnEnviarCodigo;
    private javax.swing.JButton btnMostrarOcultarConfirmarContrasenaNueva;
    private javax.swing.JButton btnMostrarOcultarContrasenaAntigua;
    private javax.swing.JButton btnMostrarOcultarContrasenaNueva;
    private javax.swing.JPanel contenedorBtnCambiarContrasena;
    private javax.swing.JPanel contenedorBtnCerrar;
    private javax.swing.JPanel contenedorBtnConfirmarCodigo;
    private javax.swing.JPanel contenedorBtnConfirmarContrasenaAntigua;
    private javax.swing.JPanel contenedorBtnEnviarCodigo;
    private javax.swing.JPanel contenedorBtnMostrarOcultarConfirmarContrasenaNueva;
    private javax.swing.JPanel contenedorBtnMostrarOcultarContrasenaAntigua;
    private javax.swing.JPanel contenedorBtnMostrarOcultarContrasenaNueva;
    private javax.swing.JPanel contenedorConfirmarCodigo;
    private javax.swing.JPanel contenedorContrasenaAntigua;
    private javax.swing.JPanel contenedorContrasenaNueva;
    private javax.swing.JPanel contenedorCorreo;
    private javax.swing.JPanel contenedorEscribaCodigo;
    private javax.swing.JPanel contenedorEscribaContrasenaAntigua;
    private javax.swing.JPanel contenedorEscribaContrasenaNueva;
    private javax.swing.JPanel contenedorEscribaCorreo;
    private javax.swing.JPanel contenedorFondoBlanco;
    private javax.swing.JPanel contenedorHeader;
    private javax.swing.JPanel contenedorInputConfirmarContrasenaNueva;
    private javax.swing.JPanel contenedorInputContrasenaAntigua;
    private javax.swing.JPanel contenedorInputContrasenaNueva;
    private javax.swing.JPanel contenedorInputYErrorConfirmarCodigo;
    private javax.swing.JPanel contenedorInputYErrorConfirmarContrasenaNueva;
    private javax.swing.JPanel contenedorInputYErrorContrasenaAntigua;
    private javax.swing.JPanel contenedorInputYErrorContrasenaNueva;
    private javax.swing.JPanel contenedorInputYErrorCorreo;
    private javax.swing.JPanel contenedorTarjetas;
    private javax.swing.JTextField inputConfirmarCodigo;
    private javax.swing.JPasswordField inputConfirmarContrasenaNueva;
    private javax.swing.JPasswordField inputContrasenaAntigua;
    private javax.swing.JPasswordField inputContrasenaNueva;
    private javax.swing.JTextField inputCorreo;
    private javax.swing.JLabel lblCodigo;
    private javax.swing.JLabel lblConfirmarContrasenaNueva;
    private javax.swing.JLabel lblContrasenaAntigua;
    private javax.swing.JLabel lblContrasenaNueva;
    private javax.swing.JLabel lblCorreo;
    private javax.swing.JLabel lblErrorInputConfirmarCodigo;
    private javax.swing.JLabel lblErrorInputConfirmarContrasenaNueva;
    private javax.swing.JLabel lblErrorInputContrasenaAntigua;
    private javax.swing.JLabel lblErrorInputContrasenaNueva;
    private javax.swing.JLabel lblErrorInputCorreo;
    private javax.swing.JTextArea lblTextAreaDescripcionContrasena;
    private javax.swing.Box.Filler margin_left;
    private javax.swing.JLabel tituloDialogo;
    // End of variables declaration//GEN-END:variables
}

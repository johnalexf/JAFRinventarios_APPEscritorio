/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jafrinventarios.vistas.usuarios;

import jafrinventarios.vistas.utilidades.componentes.DinamismoLink;
import jafrinventarios.vistas.utilidades.dialogos.DialogoAlerta;
import jafrinventarios.vistas.utilidades.dialogos.DialogoBaseConSombra;
import jafrinventarios.vistas.utilidades.formularios.TipoDatoFormulario;
import jafrinventarios.vistas.utilidades.formularios.GestorFormulario;
import java.awt.Window;
import java.util.HashMap;
import java.util.LinkedHashMap;
import javax.swing.JButton;

/**
 *
 * @author JOHN FORERO
 */
public class DialogoFormularioUsuario extends DialogoBaseConSombra{

    /*
      Objeto enum auxiliar para mejorar la legibilidad del codigo
    */
    public static enum TipoDialogo {
        EDITAR_PERFIL_PROPIO,
        EDITAR_OTRO_USUARIO,
        CREAR_NUEVO_USUARIO
    }
    
    private GestorFormulario formularioDatosUsuario;
    
    private TipoDialogo tipoDialogo;
    
    /*
    ============================================================================
                            CONSTRUCTOR PUBLICO
    ============================================================================
    El controlador ofrece metodos estaticos para poder establecer con el mismo
    cuales son los paremetros que sean acorde a la intencion del metodo estatico.
    */
    public DialogoFormularioUsuario(   Window parent, 
                                        TipoDialogo tipoDialogo, 
                                        boolean esAdministrador ) {
        
        super(parent);
        initComponents();
        
        formularioDatosUsuario = new GestorFormulario();
        this.tipoDialogo = tipoDialogo;
        
        /*
        Por defecto ocultamos los botones de editar estado o eliminar usuarios
        este pertenece unicamente a editar_otro_usuario
        Por ende el controlador especificara si dicho usuario tiene una relacion
        con otras tablas y segun esto dependera cual se muestra
        */
        btnLinkEditarEstadoUsuario.setVisible(false);
        btnLinkEliminarUsuario.setVisible(false);
        
        switch(tipoDialogo){
            case EDITAR_PERFIL_PROPIO:
                personalizacionEditarPerfil(esAdministrador);
                break;
            case EDITAR_OTRO_USUARIO:
                personalizacionEditarOtroUsuario();
            break;
            case CREAR_NUEVO_USUARIO:
                personalizacionCrearNuevoUsuario();
            break;
        }
        
        agregarCamposAGestorFormulario(esAdministrador);
        
    }
    
     
    private void ocultarContenedoresUsuarioNoAdministrador(){
        contenedorNombreCompleto.setVisible(false);
        contenedorNotaUsuarioNoAdminstrador.setVisible(false);
    }
    
    
    private void personalizacionEditarPerfil(boolean esAdministrador){
        
        // El id no necesita verlo el usuario que inicio sesion
        contenedorIdUsuario.setVisible(false);
        // Ni el usuario administrador ni el vendedor pueden modificar su rol en su perfil
        labelRol.setVisible(false);
        contenedorInputYErrorRol.setVisible(false);

        DinamismoLink.aplicarEfecto(btnLinkEditarContrasena);
        
        if(esAdministrador){
            ocultarContenedoresUsuarioNoAdministrador();
        }else{
            //Si no es administrador no tiene permiso para modificar su alias y nombre
            inputAlias.setEnabled(false);
            contenedorEntradasNombreCompleto.setVisible(false);      
        }
        
    }
    
    
    private void personalizacionBasicaAdministracionUsuarios(){
        ocultarContenedoresUsuarioNoAdministrador();    
        btnLinkEditarContrasena.setVisible(false);     
    }
    
    private void personalizacionEditarOtroUsuario(){
        personalizacionBasicaAdministracionUsuarios();
    }
    
    private void personalizacionCrearNuevoUsuario(){
        /* Como la mayoria de usos de este dialogo es para editar usuarios, 
            el titulo y el texto del boton estan predeterminados para dicha 
            funcion, por ende solo en crear un nuevo usuarios se personalizan.
        */
        tituloFormulario.setText("Crear Nuevo Usuario");
        btnEnviarFormulario.setText("Crear");
    
        personalizacionBasicaAdministracionUsuarios();
        contenedorIdUsuario.setVisible(false);
    }
    
    
    private void agregarCamposAGestorFormulario(boolean esAdministrador){
        
        agregarCampoAlias(esAdministrador);
        /*
            El combo box rol se agrega desde el controlador por medio de 
            inicializarComboBoxRoles Cuando sea para gestionar otros usuarios
        */
        
        agregarCamposNombreCompleto(esAdministrador);
        
        agregarCamposDatosContacto();
        
    }
    
    
    private void agregarCamposNombreCompleto(boolean esAdministrador){
        
        if(esAdministrador){
            formularioDatosUsuario.agregarCampoTexto( inputPrimerNombre, 
                                              lblErrorInputPrimerNombre, 
                                              TipoDatoFormulario.NOMBRE, 
                                              true);

            formularioDatosUsuario.agregarCampoTexto( inputSegundoNombre, 
                                              lblErrorInputSegundoNombre, 
                                              TipoDatoFormulario.NOMBRE, 
                                              false);

            formularioDatosUsuario.agregarCampoTexto( inputPrimerApellido, 
                                              lblErrorInputPrimerApellido, 
                                              TipoDatoFormulario.NOMBRE, 
                                              true);

            formularioDatosUsuario.agregarCampoTexto( inputSegundoApellido, 
                                              lblErrorInputSegundoApellido, 
                                              TipoDatoFormulario.NOMBRE, 
                                              false);
        }else{
            formularioDatosUsuario.agregarCampoTexto(   inputNombreCompleto, 
                                            null, 
                                            TipoDatoFormulario.NOMBRE, 
                                            false
            );
        }
    }

    
    private void agregarCamposDatosContacto(){
        formularioDatosUsuario.agregarCampoTexto( inputTelefono, 
                                          lblErrorInputTelefono, 
                                          TipoDatoFormulario.TELEFONO, 
                                          true);
        
        formularioDatosUsuario.agregarCampoTexto( inputCorreo, 
                                          lblErrorInputCorreo, 
                                          TipoDatoFormulario.CORREO, 
                                          true);
        
    }
    
    private void agregarCampoAlias( boolean esAdministrador ){
        formularioDatosUsuario.agregarCampoTexto( inputAlias, 
                                          lblErrorInputAlias, 
                                          TipoDatoFormulario.ALIAS, 
                                          esAdministrador);
    }

    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panelPrincipalScrolleable = new javax.swing.JScrollPane();
        contenedorFormulario = new javax.swing.JPanel();
        contenedorTituloFormulario = new javax.swing.JPanel();
        margin_left = new javax.swing.Box.Filler(new java.awt.Dimension(40, 0), new java.awt.Dimension(40, 0), new java.awt.Dimension(40, 32767));
        tituloFormulario = new javax.swing.JLabel();
        contenedorBtnCerrar = new javax.swing.JPanel();
        btnCerrar = new javax.swing.JButton();
        contenedorCuerpoFormulario = new javax.swing.JPanel();
        contenedorIdUsuario = new javax.swing.JPanel();
        labelID = new javax.swing.JLabel();
        lblDatoID = new javax.swing.JLabel();
        contenedorCuenta = new javax.swing.JPanel();
        contenedorTituloCuenta = new javax.swing.JPanel();
        subtituloCuenta = new javax.swing.JLabel();
        contenedorEntradasCuenta = new javax.swing.JPanel();
        labelAlias = new javax.swing.JLabel();
        contenedorInputYErrorAlias = new javax.swing.JPanel();
        inputAlias = new javax.swing.JTextField();
        lblErrorInputAlias = new javax.swing.JLabel();
        labelRol = new javax.swing.JLabel();
        contenedorInputYErrorRol = new javax.swing.JPanel();
        comboBoxRolUsuario = new javax.swing.JComboBox<>();
        lblErrorComboBoxRolUsuario = new javax.swing.JLabel();
        contenedorEntradaNombreCompleto = new javax.swing.JPanel();
        contenedorTituloNombreCompleto = new javax.swing.JPanel();
        subtituloNombreCompleto = new javax.swing.JLabel();
        contenedorEntradasNombreCompleto = new javax.swing.JPanel();
        labelPrimerNombre = new javax.swing.JLabel();
        contenedorInputYErrorPrimerNombre = new javax.swing.JPanel();
        inputPrimerNombre = new javax.swing.JTextField();
        lblErrorInputPrimerNombre = new javax.swing.JLabel();
        labelSegundoNombre = new javax.swing.JLabel();
        contenedorInputYErrorSegundoNombre = new javax.swing.JPanel();
        inputSegundoNombre = new javax.swing.JTextField();
        lblErrorInputSegundoNombre = new javax.swing.JLabel();
        labelPrimerApellido = new javax.swing.JLabel();
        contenedorInputYErrorPrimerApellido = new javax.swing.JPanel();
        inputPrimerApellido = new javax.swing.JTextField();
        lblErrorInputPrimerApellido = new javax.swing.JLabel();
        labelSegundoApellido = new javax.swing.JLabel();
        contenedorInputYErrorSegundoApellido = new javax.swing.JPanel();
        inputSegundoApellido = new javax.swing.JTextField();
        lblErrorInputSegundoApellido = new javax.swing.JLabel();
        contenedorNombreCompleto = new javax.swing.JPanel();
        labelNombreCompleto = new javax.swing.JLabel();
        inputNombreCompleto = new javax.swing.JTextField();
        contenedorDatosContacto = new javax.swing.JPanel();
        contenedorTituloDatosContacto = new javax.swing.JPanel();
        subtituloDatosContacto = new javax.swing.JLabel();
        contenedorEntradasDatosContacto = new javax.swing.JPanel();
        labelTelefono = new javax.swing.JLabel();
        contenedorInputYErrorTelefono = new javax.swing.JPanel();
        inputTelefono = new javax.swing.JTextField();
        lblErrorInputTelefono = new javax.swing.JLabel();
        labelCorreo = new javax.swing.JLabel();
        contenedorInputYErrorCorreo = new javax.swing.JPanel();
        inputCorreo = new javax.swing.JTextField();
        lblErrorInputCorreo = new javax.swing.JLabel();
        contenedorNotaUsuarioNoAdminstrador = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        contenedorBotones = new javax.swing.JPanel();
        btnEnviarFormulario = new javax.swing.JButton();
        btnLinkEditarContrasena = new javax.swing.JButton();
        btnLinkEditarEstadoUsuario = new javax.swing.JButton();
        btnLinkEliminarUsuario = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(600, 0));
        setName("dialogoUsuario"); // NOI18N
        setUndecorated(true);
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        panelPrincipalScrolleable.setBackground(new java.awt.Color(205, 205, 205));
        panelPrincipalScrolleable.setOpaque(false);

        contenedorFormulario.setBackground(new java.awt.Color(255, 255, 255));
        contenedorFormulario.setLayout(new java.awt.BorderLayout());

        contenedorTituloFormulario.setMinimumSize(new java.awt.Dimension(500, 80));
        contenedorTituloFormulario.setOpaque(false);
        contenedorTituloFormulario.setPreferredSize(new java.awt.Dimension(500, 80));
        contenedorTituloFormulario.setLayout(new java.awt.BorderLayout());
        contenedorTituloFormulario.add(margin_left, java.awt.BorderLayout.WEST);

        tituloFormulario.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        tituloFormulario.setForeground(new java.awt.Color(17, 35, 85));
        tituloFormulario.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tituloFormulario.setText("Editar Usuario");
        tituloFormulario.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        tituloFormulario.setAlignmentX(0.5F);
        tituloFormulario.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 10, 0));
        tituloFormulario.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        tituloFormulario.setVerifyInputWhenFocusTarget(false);
        contenedorTituloFormulario.add(tituloFormulario, java.awt.BorderLayout.CENTER);

        contenedorBtnCerrar.setMinimumSize(new java.awt.Dimension(40, 0));
        contenedorBtnCerrar.setOpaque(false);
        contenedorBtnCerrar.setPreferredSize(new java.awt.Dimension(40, 0));
        java.awt.FlowLayout flowLayout1 = new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 10);
        flowLayout1.setAlignOnBaseline(true);
        contenedorBtnCerrar.setLayout(flowLayout1);

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
                cerrarDialogo(evt);
            }
        });
        contenedorBtnCerrar.add(btnCerrar);

        contenedorTituloFormulario.add(contenedorBtnCerrar, java.awt.BorderLayout.EAST);

        contenedorFormulario.add(contenedorTituloFormulario, java.awt.BorderLayout.PAGE_START);

        contenedorCuerpoFormulario.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 50, 0, 50));
        contenedorCuerpoFormulario.setOpaque(false);
        contenedorCuerpoFormulario.setLayout(new java.awt.GridBagLayout());

        contenedorIdUsuario.setBackground(new java.awt.Color(205, 205, 205));
        contenedorIdUsuario.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        contenedorIdUsuario.setOpaque(false);
        contenedorIdUsuario.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 10));

        labelID.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        labelID.setText("Id :");
        contenedorIdUsuario.add(labelID);

        lblDatoID.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        lblDatoID.setText("001");
        contenedorIdUsuario.add(lblDatoID);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        contenedorCuerpoFormulario.add(contenedorIdUsuario, gridBagConstraints);

        contenedorCuenta.setOpaque(false);
        contenedorCuenta.setLayout(new javax.swing.BoxLayout(contenedorCuenta, javax.swing.BoxLayout.Y_AXIS));

        contenedorTituloCuenta.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(5, 10, -5, 0)));
        contenedorTituloCuenta.setOpaque(false);
        contenedorTituloCuenta.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        subtituloCuenta.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        subtituloCuenta.setText("Cuenta");
        subtituloCuenta.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        subtituloCuenta.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        contenedorTituloCuenta.add(subtituloCuenta);

        contenedorCuenta.add(contenedorTituloCuenta);

        contenedorEntradasCuenta.setBorder(javax.swing.BorderFactory.createEmptyBorder(14, 20, 0, 20));
        contenedorEntradasCuenta.setOpaque(false);
        contenedorEntradasCuenta.setLayout(new java.awt.GridBagLayout());

        labelAlias.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        labelAlias.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelAlias.setText("Alias :");
        labelAlias.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        labelAlias.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 0, 0, 0));
        labelAlias.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        labelAlias.setMaximumSize(new java.awt.Dimension(0, 0));
        labelAlias.setMinimumSize(new java.awt.Dimension(0, 0));
        labelAlias.setPreferredSize(new java.awt.Dimension(0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 2.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 10);
        contenedorEntradasCuenta.add(labelAlias, gridBagConstraints);

        contenedorInputYErrorAlias.setOpaque(false);
        contenedorInputYErrorAlias.setPreferredSize(new java.awt.Dimension(0, 52));
        contenedorInputYErrorAlias.setLayout(new java.awt.BorderLayout());

        inputAlias.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        inputAlias.setMargin(new java.awt.Insets(4, 10, 4, 10));
        inputAlias.setName("alias"); // NOI18N
        inputAlias.setPreferredSize(new java.awt.Dimension(0, 34));
        contenedorInputYErrorAlias.add(inputAlias, java.awt.BorderLayout.NORTH);

        lblErrorInputAlias.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        lblErrorInputAlias.setForeground(new java.awt.Color(179, 38, 30));
        lblErrorInputAlias.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 8, 0, 0));
        lblErrorInputAlias.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblErrorInputAlias.setPreferredSize(new java.awt.Dimension(0, 17));
        contenedorInputYErrorAlias.add(lblErrorInputAlias, java.awt.BorderLayout.SOUTH);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 3.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        contenedorEntradasCuenta.add(contenedorInputYErrorAlias, gridBagConstraints);

        labelRol.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        labelRol.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelRol.setText("Rol :");
        labelRol.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        labelRol.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 0, 0, 0));
        labelRol.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        labelRol.setMaximumSize(new java.awt.Dimension(0, 0));
        labelRol.setMinimumSize(new java.awt.Dimension(0, 0));
        labelRol.setPreferredSize(new java.awt.Dimension(0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 2.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 10);
        contenedorEntradasCuenta.add(labelRol, gridBagConstraints);

        contenedorInputYErrorRol.setOpaque(false);
        contenedorInputYErrorRol.setPreferredSize(new java.awt.Dimension(0, 52));
        contenedorInputYErrorRol.setLayout(new java.awt.BorderLayout());

        comboBoxRolUsuario.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        comboBoxRolUsuario.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccionar Tipo Usuario", "Administrador", "Vendedor" }));
        comboBoxRolUsuario.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        comboBoxRolUsuario.setName("rol"); // NOI18N
        comboBoxRolUsuario.setPreferredSize(new java.awt.Dimension(0, 34));
        contenedorInputYErrorRol.add(comboBoxRolUsuario, java.awt.BorderLayout.NORTH);

        lblErrorComboBoxRolUsuario.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        lblErrorComboBoxRolUsuario.setForeground(new java.awt.Color(179, 38, 30));
        lblErrorComboBoxRolUsuario.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 8, 0, 0));
        lblErrorComboBoxRolUsuario.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblErrorComboBoxRolUsuario.setPreferredSize(new java.awt.Dimension(0, 17));
        contenedorInputYErrorRol.add(lblErrorComboBoxRolUsuario, java.awt.BorderLayout.SOUTH);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 3.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        contenedorEntradasCuenta.add(contenedorInputYErrorRol, gridBagConstraints);

        contenedorCuenta.add(contenedorEntradasCuenta);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        contenedorCuerpoFormulario.add(contenedorCuenta, gridBagConstraints);

        contenedorEntradaNombreCompleto.setOpaque(false);
        contenedorEntradaNombreCompleto.setLayout(new javax.swing.BoxLayout(contenedorEntradaNombreCompleto, javax.swing.BoxLayout.Y_AXIS));

        contenedorTituloNombreCompleto.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(5, 10, -5, 0)));
        contenedorTituloNombreCompleto.setOpaque(false);
        contenedorTituloNombreCompleto.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        subtituloNombreCompleto.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        subtituloNombreCompleto.setText("Nombre Completo");
        subtituloNombreCompleto.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        subtituloNombreCompleto.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        contenedorTituloNombreCompleto.add(subtituloNombreCompleto);

        contenedorEntradaNombreCompleto.add(contenedorTituloNombreCompleto);

        contenedorEntradasNombreCompleto.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 0, 20));
        contenedorEntradasNombreCompleto.setOpaque(false);
        contenedorEntradasNombreCompleto.setLayout(new java.awt.GridBagLayout());

        labelPrimerNombre.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        labelPrimerNombre.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelPrimerNombre.setText("Primer Nombre :");
        labelPrimerNombre.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        labelPrimerNombre.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 0, 0, 0));
        labelPrimerNombre.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        labelPrimerNombre.setMaximumSize(new java.awt.Dimension(0, 0));
        labelPrimerNombre.setMinimumSize(new java.awt.Dimension(0, 0));
        labelPrimerNombre.setPreferredSize(new java.awt.Dimension(0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 2.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 10);
        contenedorEntradasNombreCompleto.add(labelPrimerNombre, gridBagConstraints);

        contenedorInputYErrorPrimerNombre.setOpaque(false);
        contenedorInputYErrorPrimerNombre.setPreferredSize(new java.awt.Dimension(0, 52));
        contenedorInputYErrorPrimerNombre.setLayout(new java.awt.BorderLayout());

        inputPrimerNombre.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        inputPrimerNombre.setMargin(new java.awt.Insets(4, 10, 4, 10));
        inputPrimerNombre.setName("primerNombre"); // NOI18N
        inputPrimerNombre.setPreferredSize(new java.awt.Dimension(0, 34));
        contenedorInputYErrorPrimerNombre.add(inputPrimerNombre, java.awt.BorderLayout.NORTH);

        lblErrorInputPrimerNombre.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        lblErrorInputPrimerNombre.setForeground(new java.awt.Color(179, 38, 30));
        lblErrorInputPrimerNombre.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 8, 0, 0));
        lblErrorInputPrimerNombre.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblErrorInputPrimerNombre.setPreferredSize(new java.awt.Dimension(0, 17));
        lblErrorInputPrimerNombre.setRequestFocusEnabled(false);
        contenedorInputYErrorPrimerNombre.add(lblErrorInputPrimerNombre, java.awt.BorderLayout.SOUTH);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 3.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        contenedorEntradasNombreCompleto.add(contenedorInputYErrorPrimerNombre, gridBagConstraints);

        labelSegundoNombre.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        labelSegundoNombre.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelSegundoNombre.setText("Segundo Nombre :");
        labelSegundoNombre.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        labelSegundoNombre.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 0, 0, 0));
        labelSegundoNombre.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        labelSegundoNombre.setMaximumSize(new java.awt.Dimension(0, 0));
        labelSegundoNombre.setMinimumSize(new java.awt.Dimension(0, 0));
        labelSegundoNombre.setPreferredSize(new java.awt.Dimension(0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 2.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 10);
        contenedorEntradasNombreCompleto.add(labelSegundoNombre, gridBagConstraints);

        contenedorInputYErrorSegundoNombre.setOpaque(false);
        contenedorInputYErrorSegundoNombre.setPreferredSize(new java.awt.Dimension(0, 52));
        contenedorInputYErrorSegundoNombre.setLayout(new java.awt.BorderLayout());

        inputSegundoNombre.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        inputSegundoNombre.setMargin(new java.awt.Insets(4, 10, 4, 10));
        inputSegundoNombre.setName("segundoNombre"); // NOI18N
        inputSegundoNombre.setPreferredSize(new java.awt.Dimension(0, 34));
        contenedorInputYErrorSegundoNombre.add(inputSegundoNombre, java.awt.BorderLayout.NORTH);

        lblErrorInputSegundoNombre.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        lblErrorInputSegundoNombre.setForeground(new java.awt.Color(179, 38, 30));
        lblErrorInputSegundoNombre.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 8, 0, 0));
        lblErrorInputSegundoNombre.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblErrorInputSegundoNombre.setPreferredSize(new java.awt.Dimension(0, 17));
        contenedorInputYErrorSegundoNombre.add(lblErrorInputSegundoNombre, java.awt.BorderLayout.SOUTH);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 3.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        contenedorEntradasNombreCompleto.add(contenedorInputYErrorSegundoNombre, gridBagConstraints);

        labelPrimerApellido.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        labelPrimerApellido.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        labelPrimerApellido.setText("Primer Apellido :");
        labelPrimerApellido.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        labelPrimerApellido.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 0, 0, 0));
        labelPrimerApellido.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        labelPrimerApellido.setMaximumSize(new java.awt.Dimension(0, 0));
        labelPrimerApellido.setMinimumSize(new java.awt.Dimension(0, 0));
        labelPrimerApellido.setPreferredSize(new java.awt.Dimension(0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 2.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 10);
        contenedorEntradasNombreCompleto.add(labelPrimerApellido, gridBagConstraints);

        contenedorInputYErrorPrimerApellido.setOpaque(false);
        contenedorInputYErrorPrimerApellido.setPreferredSize(new java.awt.Dimension(0, 52));
        contenedorInputYErrorPrimerApellido.setLayout(new java.awt.BorderLayout());

        inputPrimerApellido.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        inputPrimerApellido.setToolTipText("");
        inputPrimerApellido.setMargin(new java.awt.Insets(4, 10, 4, 10));
        inputPrimerApellido.setName("primerApellido"); // NOI18N
        inputPrimerApellido.setPreferredSize(new java.awt.Dimension(0, 34));
        contenedorInputYErrorPrimerApellido.add(inputPrimerApellido, java.awt.BorderLayout.NORTH);

        lblErrorInputPrimerApellido.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        lblErrorInputPrimerApellido.setForeground(new java.awt.Color(179, 38, 30));
        lblErrorInputPrimerApellido.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 8, 0, 0));
        lblErrorInputPrimerApellido.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblErrorInputPrimerApellido.setPreferredSize(new java.awt.Dimension(0, 17));
        contenedorInputYErrorPrimerApellido.add(lblErrorInputPrimerApellido, java.awt.BorderLayout.SOUTH);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 3.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        contenedorEntradasNombreCompleto.add(contenedorInputYErrorPrimerApellido, gridBagConstraints);

        labelSegundoApellido.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        labelSegundoApellido.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        labelSegundoApellido.setText("Segundo Apellido :");
        labelSegundoApellido.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        labelSegundoApellido.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 0, 0, 0));
        labelSegundoApellido.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        labelSegundoApellido.setMaximumSize(new java.awt.Dimension(0, 0));
        labelSegundoApellido.setMinimumSize(new java.awt.Dimension(0, 0));
        labelSegundoApellido.setPreferredSize(new java.awt.Dimension(0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 2.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        contenedorEntradasNombreCompleto.add(labelSegundoApellido, gridBagConstraints);

        contenedorInputYErrorSegundoApellido.setOpaque(false);
        contenedorInputYErrorSegundoApellido.setPreferredSize(new java.awt.Dimension(0, 52));
        contenedorInputYErrorSegundoApellido.setLayout(new java.awt.BorderLayout());

        inputSegundoApellido.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        inputSegundoApellido.setMargin(new java.awt.Insets(4, 10, 4, 10));
        inputSegundoApellido.setName("segundoApellido"); // NOI18N
        inputSegundoApellido.setPreferredSize(new java.awt.Dimension(0, 34));
        contenedorInputYErrorSegundoApellido.add(inputSegundoApellido, java.awt.BorderLayout.NORTH);

        lblErrorInputSegundoApellido.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        lblErrorInputSegundoApellido.setForeground(new java.awt.Color(179, 38, 30));
        lblErrorInputSegundoApellido.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 8, 0, 0));
        lblErrorInputSegundoApellido.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblErrorInputSegundoApellido.setPreferredSize(new java.awt.Dimension(0, 17));
        contenedorInputYErrorSegundoApellido.add(lblErrorInputSegundoApellido, java.awt.BorderLayout.SOUTH);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 3.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        contenedorEntradasNombreCompleto.add(contenedorInputYErrorSegundoApellido, gridBagConstraints);

        contenedorEntradaNombreCompleto.add(contenedorEntradasNombreCompleto);

        contenedorNombreCompleto.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 6, 20));
        contenedorNombreCompleto.setOpaque(false);
        contenedorNombreCompleto.setPreferredSize(new java.awt.Dimension(0, 60));
        contenedorNombreCompleto.setLayout(new java.awt.GridBagLayout());

        labelNombreCompleto.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        labelNombreCompleto.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        labelNombreCompleto.setText("Nombre  :");
        labelNombreCompleto.setMaximumSize(new java.awt.Dimension(0, 0));
        labelNombreCompleto.setMinimumSize(new java.awt.Dimension(0, 0));
        labelNombreCompleto.setPreferredSize(new java.awt.Dimension(0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 2.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        contenedorNombreCompleto.add(labelNombreCompleto, gridBagConstraints);

        inputNombreCompleto.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        inputNombreCompleto.setText("John Forero");
        inputNombreCompleto.setEnabled(false);
        inputNombreCompleto.setMargin(new java.awt.Insets(4, 10, 4, 10));
        inputNombreCompleto.setName("nombreCompleto"); // NOI18N
        inputNombreCompleto.setPreferredSize(new java.awt.Dimension(0, 34));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 3.0;
        gridBagConstraints.weighty = 1.0;
        contenedorNombreCompleto.add(inputNombreCompleto, gridBagConstraints);

        contenedorEntradaNombreCompleto.add(contenedorNombreCompleto);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        contenedorCuerpoFormulario.add(contenedorEntradaNombreCompleto, gridBagConstraints);

        contenedorDatosContacto.setOpaque(false);
        contenedorDatosContacto.setLayout(new javax.swing.BoxLayout(contenedorDatosContacto, javax.swing.BoxLayout.Y_AXIS));

        contenedorTituloDatosContacto.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createEmptyBorder(5, 10, -5, 0)));
        contenedorTituloDatosContacto.setOpaque(false);
        contenedorTituloDatosContacto.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        subtituloDatosContacto.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        subtituloDatosContacto.setText("Datos de contacto");
        subtituloDatosContacto.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        subtituloDatosContacto.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        contenedorTituloDatosContacto.add(subtituloDatosContacto);

        contenedorDatosContacto.add(contenedorTituloDatosContacto);

        contenedorEntradasDatosContacto.setBorder(javax.swing.BorderFactory.createEmptyBorder(14, 20, 0, 20));
        contenedorEntradasDatosContacto.setOpaque(false);
        contenedorEntradasDatosContacto.setLayout(new java.awt.GridBagLayout());

        labelTelefono.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        labelTelefono.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelTelefono.setText("Telefono :");
        labelTelefono.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        labelTelefono.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 0, 0, 0));
        labelTelefono.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        labelTelefono.setMaximumSize(new java.awt.Dimension(0, 0));
        labelTelefono.setMinimumSize(new java.awt.Dimension(0, 0));
        labelTelefono.setPreferredSize(new java.awt.Dimension(0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 2.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 10);
        contenedorEntradasDatosContacto.add(labelTelefono, gridBagConstraints);

        contenedorInputYErrorTelefono.setOpaque(false);
        contenedorInputYErrorTelefono.setPreferredSize(new java.awt.Dimension(0, 52));
        contenedorInputYErrorTelefono.setLayout(new java.awt.BorderLayout());

        inputTelefono.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        inputTelefono.setMargin(new java.awt.Insets(4, 10, 4, 10));
        inputTelefono.setName("telefono"); // NOI18N
        inputTelefono.setPreferredSize(new java.awt.Dimension(0, 34));
        contenedorInputYErrorTelefono.add(inputTelefono, java.awt.BorderLayout.NORTH);

        lblErrorInputTelefono.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        lblErrorInputTelefono.setForeground(new java.awt.Color(179, 38, 30));
        lblErrorInputTelefono.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 8, 0, 0));
        lblErrorInputTelefono.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblErrorInputTelefono.setPreferredSize(new java.awt.Dimension(0, 17));
        contenedorInputYErrorTelefono.add(lblErrorInputTelefono, java.awt.BorderLayout.SOUTH);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 3.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        contenedorEntradasDatosContacto.add(contenedorInputYErrorTelefono, gridBagConstraints);

        labelCorreo.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        labelCorreo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelCorreo.setText("Correo :");
        labelCorreo.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        labelCorreo.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 0, 0, 0));
        labelCorreo.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        labelCorreo.setMaximumSize(new java.awt.Dimension(0, 0));
        labelCorreo.setMinimumSize(new java.awt.Dimension(0, 0));
        labelCorreo.setPreferredSize(new java.awt.Dimension(0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 2.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        contenedorEntradasDatosContacto.add(labelCorreo, gridBagConstraints);

        contenedorInputYErrorCorreo.setOpaque(false);
        contenedorInputYErrorCorreo.setPreferredSize(new java.awt.Dimension(0, 52));
        contenedorInputYErrorCorreo.setLayout(new java.awt.BorderLayout());

        inputCorreo.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        inputCorreo.setMargin(new java.awt.Insets(4, 10, 4, 10));
        inputCorreo.setName("correo"); // NOI18N
        inputCorreo.setPreferredSize(new java.awt.Dimension(0, 34));
        contenedorInputYErrorCorreo.add(inputCorreo, java.awt.BorderLayout.NORTH);

        lblErrorInputCorreo.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        lblErrorInputCorreo.setForeground(new java.awt.Color(179, 38, 30));
        lblErrorInputCorreo.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 8, 0, 0));
        lblErrorInputCorreo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblErrorInputCorreo.setPreferredSize(new java.awt.Dimension(0, 17));
        contenedorInputYErrorCorreo.add(lblErrorInputCorreo, java.awt.BorderLayout.SOUTH);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 3.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        contenedorEntradasDatosContacto.add(contenedorInputYErrorCorreo, gridBagConstraints);

        contenedorDatosContacto.add(contenedorEntradasDatosContacto);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        contenedorCuerpoFormulario.add(contenedorDatosContacto, gridBagConstraints);

        contenedorNotaUsuarioNoAdminstrador.setBorder(javax.swing.BorderFactory.createEmptyBorder(14, 20, 10, 20));
        contenedorNotaUsuarioNoAdminstrador.setOpaque(false);
        contenedorNotaUsuarioNoAdminstrador.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));

        jLabel1.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        jLabel1.setText("<html>\n<p style=\"width: 320px; \">\n<b>Nota:</b> Como usuario vendedor no tiene autorizado cambiar su nombre o alias,\n si es necesario realizar un ajuste, por favor comuniquese con el administrador.\n</p>\n</html>");
        contenedorNotaUsuarioNoAdminstrador.add(jLabel1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        contenedorCuerpoFormulario.add(contenedorNotaUsuarioNoAdminstrador, gridBagConstraints);

        contenedorFormulario.add(contenedorCuerpoFormulario, java.awt.BorderLayout.CENTER);

        contenedorBotones.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 40, 10, 40));
        contenedorBotones.setOpaque(false);
        contenedorBotones.setPreferredSize(new java.awt.Dimension(500, 130));
        contenedorBotones.setLayout(new java.awt.GridBagLayout());

        btnEnviarFormulario.setBackground(new java.awt.Color(30, 166, 177));
        btnEnviarFormulario.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        btnEnviarFormulario.setForeground(new java.awt.Color(255, 255, 255));
        btnEnviarFormulario.setText("Actualizar");
        btnEnviarFormulario.setToolTipText("");
        btnEnviarFormulario.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEnviarFormulario.setMargin(new java.awt.Insets(6, 12, 6, 12));
        btnEnviarFormulario.setMaximumSize(new java.awt.Dimension(200, 40));
        btnEnviarFormulario.setMinimumSize(new java.awt.Dimension(200, 40));
        btnEnviarFormulario.setPreferredSize(new java.awt.Dimension(200, 40));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        contenedorBotones.add(btnEnviarFormulario, gridBagConstraints);

        btnLinkEditarContrasena.setFont(new java.awt.Font("Segoe UI", 1, 17)); // NOI18N
        btnLinkEditarContrasena.setForeground(new java.awt.Color(17, 35, 85));
        btnLinkEditarContrasena.setText("Editar Contrasena");
        btnLinkEditarContrasena.setAlignmentX(0.5F);
        btnLinkEditarContrasena.setIconTextGap(10);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        contenedorBotones.add(btnLinkEditarContrasena, gridBagConstraints);

        btnLinkEditarEstadoUsuario.setFont(new java.awt.Font("Segoe UI", 1, 17)); // NOI18N
        btnLinkEditarEstadoUsuario.setForeground(new java.awt.Color(200, 0, 0));
        btnLinkEditarEstadoUsuario.setText("Deshabilitar Usuario");
        btnLinkEditarEstadoUsuario.setAlignmentX(0.5F);
        btnLinkEditarEstadoUsuario.setIconTextGap(10);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        contenedorBotones.add(btnLinkEditarEstadoUsuario, gridBagConstraints);

        btnLinkEliminarUsuario.setFont(new java.awt.Font("Segoe UI", 1, 17)); // NOI18N
        btnLinkEliminarUsuario.setForeground(new java.awt.Color(200, 0, 0));
        btnLinkEliminarUsuario.setText("Eliminar Usuario");
        btnLinkEliminarUsuario.setAlignmentX(0.5F);
        btnLinkEliminarUsuario.setIconTextGap(10);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        contenedorBotones.add(btnLinkEliminarUsuario, gridBagConstraints);

        contenedorFormulario.add(contenedorBotones, java.awt.BorderLayout.PAGE_END);

        panelPrincipalScrolleable.setViewportView(contenedorFormulario);

        getContentPane().add(panelPrincipalScrolleable);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void cerrarDialogo(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cerrarDialogo
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_cerrarDialogo

    
    
    /*
    ============================================================================
                      MÉTODOS PÚBLICOS PARA EL CONTROLADOR
    ============================================================================
    */

    
    public void setId( String id ){
        lblDatoID.setText(id);
    }
    
    public void mostrarBtnLinkEliminarUsuario(){
        btnLinkEliminarUsuario.setVisible(true);
        DinamismoLink.aplicarEfecto(btnLinkEliminarUsuario);
    }
    
    public void mostrarBtnEditarEstadoUsuario(){
        btnLinkEditarEstadoUsuario.setVisible(true);
        DinamismoLink.aplicarEfecto(btnLinkEditarEstadoUsuario);
    }
    
    public void asignarIntencionBtnEditarEstadoUsuario(boolean esHabilitado){
        
        if(esHabilitado){
            //Si esta habilitado el usuario, se muestra el boton para demostrar
            //la intencion de poder deshabilitarlo
            btnLinkEditarEstadoUsuario.setForeground(new java.awt.Color(200, 0, 0));
            btnLinkEditarEstadoUsuario.setText("Deshabilitar Usuario");
        }else{
            btnLinkEditarEstadoUsuario.setForeground(new java.awt.Color(17, 35, 85));
            btnLinkEditarEstadoUsuario.setText("Habilitar Usuario");
        }
        btnLinkEditarEstadoUsuario.repaint();
        
    }
    
    //Exponer inicializar el combo box esperando la lista de roles a asignar
    public void inicializarComboBoxRoles( LinkedHashMap<Integer, String> diccionarioRoles ) {
        
        formularioDatosUsuario.agregarCampoComboBox(
                comboBoxRolUsuario,
                "Tipo de Usuario",
                diccionarioRoles, 
                lblErrorComboBoxRolUsuario, 
                true);
        
    }
    
    //Exponer botones
    public JButton getBtnEnviarFormulario(){
        return btnEnviarFormulario;
    }
    
    public JButton getBtnLinkEditarContrasena(){
        return btnLinkEditarContrasena;
    }
    
    public JButton getBtnLinkEditarEstadoUsuario(){
        return btnLinkEditarEstadoUsuario;
    }
    
    public JButton getBtnLinkEliminarUsuario(){
        return btnLinkEliminarUsuario;
    }
    
    //Exponer metodos para gestionar el formulario
    public boolean validarFormulario(){
        return formularioDatosUsuario.validar();
    }
    
    public HashMap<String, String> recolectarDatosFormulario(){
        return formularioDatosUsuario.recolectarDatos();
    }
    
    public void mostrarErrorRespuestaBDEnFormulario( HashMap<String, String> erroresCamposBD ){
        formularioDatosUsuario.mostrarErroresExternos(erroresCamposBD);
    }
    
    public void asignarDatosEnFormulario( HashMap<String, String> datosBD ){
        formularioDatosUsuario.asignarDatos(datosBD);
    }
    
    
    /* 
        Metodos para dialogos de mensajes de Alertas
    */
    public void mostrarAlertaErrorFormatoCampos(){
        DialogoAlerta.mostrarErrorFormatoCampos( this );
    }
    
    public void mostrarAlertaExitosa(){
        
        String mensajeExitoso = (tipoDialogo != TipoDialogo.CREAR_NUEVO_USUARIO)
                ?  "El usuario se ha actualizado correctamente"
                :   "Usuario creado correctamente \n La contraseña se le envia al usuario por correo"; 
            
        mostrarAlertaExitosa( mensajeExitoso );
    }
    
    
    public void mostrarAlertaExitosa(String mensajeExitoso ){
        
        DialogoAlerta.mostrarExito(
                    this, 
                    "Operacion Exitosa", 
                    mensajeExitoso
            );
        
    }
    
    public boolean mostrarAlertaAdvertencia( String mensaje ){
        
       return DialogoAlerta.mostrarAdvertenciaConRespuesta(
                   this,
                   "Advertencia", 
                   mensaje
                );
    
    }
    
    public void mostrarAlertaError( String mensaje ){
        DialogoAlerta.mostrarError( this, "Error", mensaje );
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnEnviarFormulario;
    private javax.swing.JButton btnLinkEditarContrasena;
    private javax.swing.JButton btnLinkEditarEstadoUsuario;
    private javax.swing.JButton btnLinkEliminarUsuario;
    private javax.swing.JComboBox<String> comboBoxRolUsuario;
    private javax.swing.JPanel contenedorBotones;
    private javax.swing.JPanel contenedorBtnCerrar;
    private javax.swing.JPanel contenedorCuenta;
    private javax.swing.JPanel contenedorCuerpoFormulario;
    private javax.swing.JPanel contenedorDatosContacto;
    private javax.swing.JPanel contenedorEntradaNombreCompleto;
    private javax.swing.JPanel contenedorEntradasCuenta;
    private javax.swing.JPanel contenedorEntradasDatosContacto;
    private javax.swing.JPanel contenedorEntradasNombreCompleto;
    private javax.swing.JPanel contenedorFormulario;
    private javax.swing.JPanel contenedorIdUsuario;
    private javax.swing.JPanel contenedorInputYErrorAlias;
    private javax.swing.JPanel contenedorInputYErrorCorreo;
    private javax.swing.JPanel contenedorInputYErrorPrimerApellido;
    private javax.swing.JPanel contenedorInputYErrorPrimerNombre;
    private javax.swing.JPanel contenedorInputYErrorRol;
    private javax.swing.JPanel contenedorInputYErrorSegundoApellido;
    private javax.swing.JPanel contenedorInputYErrorSegundoNombre;
    private javax.swing.JPanel contenedorInputYErrorTelefono;
    private javax.swing.JPanel contenedorNombreCompleto;
    private javax.swing.JPanel contenedorNotaUsuarioNoAdminstrador;
    private javax.swing.JPanel contenedorTituloCuenta;
    private javax.swing.JPanel contenedorTituloDatosContacto;
    private javax.swing.JPanel contenedorTituloFormulario;
    private javax.swing.JPanel contenedorTituloNombreCompleto;
    private javax.swing.JTextField inputAlias;
    private javax.swing.JTextField inputCorreo;
    private javax.swing.JTextField inputNombreCompleto;
    private javax.swing.JTextField inputPrimerApellido;
    private javax.swing.JTextField inputPrimerNombre;
    private javax.swing.JTextField inputSegundoApellido;
    private javax.swing.JTextField inputSegundoNombre;
    private javax.swing.JTextField inputTelefono;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel labelAlias;
    private javax.swing.JLabel labelCorreo;
    private javax.swing.JLabel labelID;
    private javax.swing.JLabel labelNombreCompleto;
    private javax.swing.JLabel labelPrimerApellido;
    private javax.swing.JLabel labelPrimerNombre;
    private javax.swing.JLabel labelRol;
    private javax.swing.JLabel labelSegundoApellido;
    private javax.swing.JLabel labelSegundoNombre;
    private javax.swing.JLabel labelTelefono;
    private javax.swing.JLabel lblDatoID;
    private javax.swing.JLabel lblErrorComboBoxRolUsuario;
    private javax.swing.JLabel lblErrorInputAlias;
    private javax.swing.JLabel lblErrorInputCorreo;
    private javax.swing.JLabel lblErrorInputPrimerApellido;
    private javax.swing.JLabel lblErrorInputPrimerNombre;
    private javax.swing.JLabel lblErrorInputSegundoApellido;
    private javax.swing.JLabel lblErrorInputSegundoNombre;
    private javax.swing.JLabel lblErrorInputTelefono;
    private javax.swing.Box.Filler margin_left;
    private javax.swing.JScrollPane panelPrincipalScrolleable;
    private javax.swing.JLabel subtituloCuenta;
    private javax.swing.JLabel subtituloDatosContacto;
    private javax.swing.JLabel subtituloNombreCompleto;
    private javax.swing.JLabel tituloFormulario;
    // End of variables declaration//GEN-END:variables
}

package jafrinventarios.vistas.utilidades.componentes;

import java.awt.Color;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import raven.datetime.DatePicker;

/**
 *
 * @author JOHN FORERO
 */
public class SelectorFecha {
    
    // Variables internas para llevar el rastro del valor elegido
    private LocalDate fechaSeleccionada;
    private final JTextField campoTextoDestino;

    /**
     * Constructor utilitario.
     * @param campoTexto El JTextField de donde se quiere que se escriba la fecha.
     */
    public SelectorFecha(JTextField campoTexto) {
        this.campoTextoDestino = campoTexto;

        configurarEstilosCampoTexto();
        
        // Inicializar el campo con la fecha
        asignarFechaInicial();
        
        // Configurar e iniciar los componentes flotantes
        configurarSelectorFlotante();
    }
    
    public void configurarEstilosCampoTexto(){
        campoTextoDestino.setEditable(false); // Evita que escriban letras a mano
        campoTextoDestino.setBackground(Color.WHITE);
    }

    private void configurarSelectorFlotante() {
        // 1. Instanciar el componente grafico
        DatePicker datePicker = new DatePicker();
        
        datePicker.setBackground(Color.WHITE);
        
        datePicker.setSelectedDate(fechaSeleccionada);

        // 2. Panel contenedor del selector fecha : 1 fila, 1 columnas, con 0px de espacio
        JPanel panelContenedor = new JPanel(new GridLayout(1, 1, 0, 0));
        panelContenedor.setBackground(java.awt.Color.WHITE);
                
        // Añadir un pequeño margen interno para que no toque los bordes del menú flotante
        panelContenedor.setBorder(new EmptyBorder(5, 5, 5, 5)); 
        
        panelContenedor.add(datePicker);  // Centro: calendario

        // 3. Metemos el panel dentro de un menú flotante (Popup) de Swing
        JPopupMenu popupFlotante = new JPopupMenu();
        //popupFlotante.setBackground(java.awt.Color.WHITE);
        popupFlotante.add(panelContenedor);

        // 4. Escuchamos cuando el usuario cambia el día en el calendario
        datePicker.addDateSelectionListener(e -> {
            fechaSeleccionada = datePicker.getSelectedDate();
            actualizarTextoCampo();
        });

        // 5. EVENTO CLAVE: Al hacer clic, se muestra el menú extendido hacia los lados
        campoTextoDestino.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {

                // Forzar a que el layout calcule el tamaño real del panel antes de mostrarlo
                panelContenedor.setSize(panelContenedor.getPreferredSize());
                
                // Calcular la posición X para que quede centrado respecto al campo de texto
                int campoAncho = campoTextoDestino.getWidth();
                int popupAncho = panelContenedor.getWidth();
                int posXInicial = (campoAncho / 2) - (popupAncho / 2);
                
                // Muestra el popup centrado horizontalmente justo debajo del campo
                popupFlotante.show(campoTextoDestino, posXInicial, campoTextoDestino.getHeight());
            }
        });
    }

    // Aplica el formato "dd/MM/yyyy"
    private void actualizarTextoCampo() {
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        campoTextoDestino.setText(fechaSeleccionada.format(formatoFecha));
    }
    
    private void asignarFechaInicial() {
        String textoActual = campoTextoDestino.getText().trim();
        
        if (!textoActual.isEmpty()) {
            try {
                // Si hay texto lo parseamos
                DateTimeFormatter formatoGlobal = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                this.fechaSeleccionada = java.time.LocalDate.parse(textoActual, formatoGlobal);
            } catch (Exception e) {
                // Si el formato es erróneo, asignamos fecha de hoy.
                asignarFechaHoy();
            }
        } else {
            // Si está vacío asignamos fecha de hoy
            asignarFechaHoy();
        }
    }
    
    private void asignarFechaHoy(){
        this.fechaSeleccionada = LocalDate.now();
        actualizarTextoCampo();
    }
}

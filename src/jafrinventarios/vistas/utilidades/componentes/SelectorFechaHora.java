package jafrinventarios.vistas.utilidades.componentes;

import java.awt.Color;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import raven.datetime.DatePicker;
import raven.datetime.TimePicker;

/**
 *
 * @author JOHN FORERO
 */
public class SelectorFechaHora {
    
    // Variables internas para llevar el rastro de los valores elegidos
    private LocalDate fechaSeleccionada = LocalDate.now();
    private LocalTime horaSeleccionada = LocalTime.now();
    private final JTextField campoTextoDestino;

    /**
     * Constructor utilitario.
     * @param campoTexto El JTextField de donde se quiere que se escriba la fecha y hora.
     */
    public SelectorFechaHora(JTextField campoTexto) {
        this.campoTextoDestino = campoTexto;
        this.campoTextoDestino.setEditable(false); // Evita que escriban letras a mano
        this.campoTextoDestino.setBackground(Color.WHITE);
        
        // Inicializar el campo con la fecha y hora de este momento
        actualizarTextoCampo();
        
        // Configurar e iniciar los componentes flotantes
        configurarSelectorFlotante();
    }

    private void configurarSelectorFlotante() {
        // 1. Instanciamos los componentes gráficos 
        DatePicker datePicker = new DatePicker();
        TimePicker timePicker = new TimePicker();
        
        datePicker.setBackground(Color.WHITE);
        timePicker.setBackground(Color.WHITE);
        
        // Configurar el reloj para use formato de 12 horas (AM/PM)
        timePicker.set24HourView(false); // Desactiva las 24 horas para un diseño más limpio

        // 2. Colocar lado a lado: 1 fila, 2 columnas, con 10px de espacio entre ellos
        JPanel panelContenedor = new JPanel(new GridLayout(1, 2, 10, 0));
        panelContenedor.setBackground(java.awt.Color.WHITE);
                
        // Añadir un pequeño margen interno para que no toque los bordes del menú flotante
        panelContenedor.setBorder(new EmptyBorder(5, 5, 5, 5)); 
        
        panelContenedor.add(datePicker);  // Izquierda: Calendario
        panelContenedor.add(timePicker);  // Derecha: Reloj

        // 3. Metemos el panel dentro de un menú flotante (Popup) de Swing
        JPopupMenu popupFlotante = new JPopupMenu();
        //popupFlotante.setBackground(java.awt.Color.WHITE);
        popupFlotante.add(panelContenedor);

        // 4. Escuchamos cuando el usuario cambia el día en el calendario
        datePicker.addDateSelectionListener(e -> {
            fechaSeleccionada = datePicker.getSelectedDate();
            actualizarTextoCampo();
        });

        // 5. Escuchamos cuando el usuario mueve las manecillas del reloj
        timePicker.addTimeSelectionListener(e -> {
            horaSeleccionada = timePicker.getSelectedTime();
            actualizarTextoCampo();
        });

        // 6. EVENTO CLAVE: Al hacer clic, se muestra el menú extendido hacia los lados
        campoTextoDestino.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {

                // Forzar a que el layout calcule el tamaño real del panel antes de mostrarlo
                panelContenedor.setSize(panelContenedor.getPreferredSize());
                
                // Calcular la posición X para que quede centrado respecto al campo de texto
                int campoAncho = campoTextoDestino.getWidth();
                int popupAncho = panelContenedor.getWidth();
                int posXCentrada = (campoAncho / 2) - (popupAncho / 2);
                
                // Muestra el popup centrado horizontalmente justo debajo del campo
                popupFlotante.show(campoTextoDestino, posXCentrada, campoTextoDestino.getHeight());
            }
        });
    }

    // Aplica el formato "dd/MM/yyyy hh:mm a" (12 horas AM/PM)
    private void actualizarTextoCampo() {
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("hh:mm a");
        campoTextoDestino.setText(fechaSeleccionada.format(formatoFecha) + " " + horaSeleccionada.format(formatoHora));
    }
}

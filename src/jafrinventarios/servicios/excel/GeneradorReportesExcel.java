package jafrinventarios.servicios.excel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Header;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;

/**
 * Clase principal encargada de construir los reportes en formato Excel (.xls).
 * Utiliza HSSF (Horrible SpreadSheet Format), la API de Apache POI para Excel 97-2003.
 * 
 * @author JOHN FORERO
 */
public class GeneradorReportesExcel {

    // HSSFWorkbook representa el archivo Excel completo (el libro).
    private HSSFWorkbook libro;
    
    // HSSFSheet representa una pestaña individual dentro del libro de Excel.
    private HSSFSheet hoja;
    
    // Título que se imprimirá en el encabezado
    private String tituloReporte;
    
    // Catálogo de estilos según el diseño de la plantilla de reportes
    private HSSFCellStyle estiloEncabezado;
    private HSSFCellStyle estiloTablaEncabezado;
    private HSSFCellStyle estiloTextoIzquierda;
    private HSSFCellStyle estiloTablaTextoIzquierda;
    private HSSFCellStyle estiloTablaTextoPequenoIzquierda;
    private HSSFCellStyle estiloTextoIzquierdaNegrita;
    private HSSFCellStyle estiloTextoIzquierdaSangria;
    private HSSFCellStyle estiloTablaNumeroCentro;
    private HSSFCellStyle estiloTablaMonedaCentro;
    private HSSFCellStyle estiloMonedaCentroNegrita;

    /**
     * Enum para estandarizar los altos de las filas según la plantilla original.
     * Los valores numéricos representan "puntos" (la unidad de medida de Excel).
     */
    public enum GrosorFila {
        DELGADO(8.1f),   // Usado para separadores visuales
        NORMAL(15.0f),   // Tamaño estándar de Excel
        GRUESO(20.1f);   // Usado para las filas de datos y totales

        private final float puntos;

        GrosorFila(float puntos) {
            this.puntos = puntos;
        }

        public float getPuntos() {
            return puntos;
        }
    }

    /**
     * Constructor del generador.
     * @param tituloReporte El título que aparecerá en la parte central del encabezado.
     */
    public GeneradorReportesExcel(String tituloReporte) {
        // 1. Instanciamos un libro de Excel completamente vacío en la memoria RAM
        this.libro = new HSSFWorkbook();
        this.tituloReporte = tituloReporte;
        inicializarHoja(tituloReporte);
        inicializarEstilos();
    }

    /**
     * Inicializa la hoja de trabajo con los grosores y configuraciones de impresión fijas.
     * @param nombreHoja El nombre que tendrá la pestaña (ej: "Cantidades a Comprar")
     */
    private void inicializarHoja(String nombreHoja) {
        
        // 2. Creamos la pestaña dentro del libro
        this.hoja = libro.createSheet(nombreHoja);
        
        /*
        ============================================================================
             1. CONFIGURACIONES DE IMPRESIÓN (Plantilla Fija)
        ============================================================================
        */
        // HSSFPrintSetup controla el cuadro de diálogo de "Configurar Página" en Excel
        HSSFPrintSetup configuracionImpresion = hoja.getPrintSetup();
        
        // Orientación horizontal en false para que sea en vertical.
        configuracionImpresion.setLandscape(false);
        // Tamaño Carta (1) o A4 (9). HSSFPrintSetup.LETTER_PAPERSIZE equivale a 1.
        configuracionImpresion.setPaperSize(HSSFPrintSetup.LETTER_PAPERSIZE);
        
        // Desactivamos el escalado automático para mantener los tamaños fijos que vamos a dar
        hoja.setAutobreaks(false);
        
        // Centrar la tabla horizontalmente al imprimir
        hoja.setHorizontallyCenter(true);
        
        /*
        ============================================================================
             2. ENCABEZADOS Y PIES DE PÁGINA (Impresión)
        ============================================================================
        */
        Header encabezado = hoja.getHeader();
        Footer piePagina = hoja.getFooter();
        
        // Obtener la fecha de hoy desde Java para que quede "congelada" en texto
        String fechaGeneracion = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        
        // Insertamos el nombre de la app a la izquierda, el título en el centro y la fecha a la derecha del encabezado
        encabezado.setLeft("&BJAFR");
        encabezado.setCenter("&B"+tituloReporte);
        encabezado.setRight(fechaGeneracion);
        
        // El pie de página central con la numeración dinámica nativa de Excel (&P = página, &N = total)
        piePagina.setCenter("Página &P de &N");

        /*
        ============================================================================
             3. ANCHOS DE COLUMNAS FIJOS (Para todos los reportes)
        ============================================================================
        */
        // En Apache POI, el ancho de columna se mide en incrementos de 1/256avo de carácter.
        // Formula aproximada: (ancho en excel * 256)
        // Fijamos de la columna 0 a la 25 (A hasta Z) según sea necesario
        
        hoja.setColumnWidth(0, (int)(0.83 * 256)); // Columna A
        for(int columna=1; columna <= 23 ; columna++){
            hoja.setColumnWidth( columna , (int)(2.86 * 256)); // Columna B a la X
        }
        hoja.setColumnWidth(24, (int)(0.83 * 256)); // Columna Y
        
        
    }
    
    
    
    /**
     * Crea el catálogo de estilos visuales en la memoria del libro de Excel.
     */
    private void inicializarEstilos() {
        
        // 1. FUENTES BASE
        HSSFFont fuenteNormal = libro.createFont();
        fuenteNormal.setFontName("Calibri");
        fuenteNormal.setFontHeightInPoints((short) 11);

        HSSFFont fuenteNegrita = libro.createFont();
        fuenteNegrita.setFontName("Calibri");
        fuenteNegrita.setFontHeightInPoints((short) 11);
        fuenteNegrita.setBold(true);
        
        HSSFFont fuentePequena = libro.createFont();
        fuentePequena.setFontName("Calibri");
        fuentePequena.setFontHeightInPoints((short) 10);
        
        // 2. CATÁLOGO DE COMBINACIONES
        
        // Para los encabezados de las columnas
        estiloEncabezado = libro.createCellStyle();
        estiloEncabezado.setFont(fuenteNegrita);
        estiloEncabezado.setAlignment(HorizontalAlignment.CENTER);
        estiloEncabezado.setVerticalAlignment(VerticalAlignment.CENTER);
        
        estiloTablaEncabezado = libro.createCellStyle();
        estiloTablaEncabezado.cloneStyleFrom(estiloEncabezado);
        asignarBordesCeldaTabla(estiloTablaEncabezado);

        // Para textos normales (nombres de productos, contactos)
        estiloTextoIzquierda = libro.createCellStyle();
        estiloTextoIzquierda.setFont(fuenteNormal);
        estiloTextoIzquierda.setAlignment(HorizontalAlignment.LEFT);
        estiloTextoIzquierda.setVerticalAlignment(VerticalAlignment.CENTER);
        
        estiloTablaTextoIzquierda = libro.createCellStyle();
        estiloTablaTextoIzquierda.cloneStyleFrom(estiloTextoIzquierda);
        asignarBordesCeldaTabla(estiloTablaTextoIzquierda);
        
        estiloTablaTextoPequenoIzquierda = libro.createCellStyle();
        estiloTablaTextoPequenoIzquierda.setFont(fuentePequena);
        estiloTablaTextoPequenoIzquierda.setAlignment(HorizontalAlignment.LEFT);
        estiloTablaTextoPequenoIzquierda.setVerticalAlignment(VerticalAlignment.CENTER);
        asignarBordesCeldaTabla(estiloTablaTextoPequenoIzquierda);
        
        estiloTextoIzquierdaNegrita = libro.createCellStyle();
        estiloTextoIzquierdaNegrita.setFont(fuenteNegrita);
        estiloTextoIzquierdaNegrita.setAlignment(HorizontalAlignment.LEFT);
        estiloTextoIzquierdaNegrita.setVerticalAlignment(VerticalAlignment.CENTER);

        // Para textos con espacio al inicio (como los detalles debajo de un proveedor)
        estiloTextoIzquierdaSangria = libro.createCellStyle();
        estiloTextoIzquierdaSangria.setFont(fuenteNormal);
        estiloTextoIzquierdaSangria.setAlignment(HorizontalAlignment.LEFT);
        estiloTextoIzquierdaSangria.setVerticalAlignment(VerticalAlignment.CENTER);
        estiloTextoIzquierdaSangria.setIndention((short) 1); // Aplica la sangría

        // Para IDs y cantidades
        estiloTablaNumeroCentro = libro.createCellStyle();
        estiloTablaNumeroCentro.setFont(fuenteNormal);
        estiloTablaNumeroCentro.setAlignment(HorizontalAlignment.CENTER);
        estiloTablaNumeroCentro.setVerticalAlignment(VerticalAlignment.CENTER);
        asignarBordesCeldaTabla(estiloTablaNumeroCentro);

        // Para precios y totales (centro)
        estiloTablaMonedaCentro = libro.createCellStyle();
        estiloTablaMonedaCentro.setFont(fuenteNormal);
        estiloTablaMonedaCentro.setAlignment(HorizontalAlignment.CENTER);
        estiloTablaMonedaCentro.setVerticalAlignment(VerticalAlignment.CENTER);
        asignarBordesCeldaTabla(estiloTablaMonedaCentro);
        
        estiloMonedaCentroNegrita = libro.createCellStyle();
        estiloMonedaCentroNegrita.setFont(fuenteNegrita);
        estiloMonedaCentroNegrita.setAlignment(HorizontalAlignment.CENTER);
        estiloMonedaCentroNegrita.setVerticalAlignment(VerticalAlignment.CENTER);
        
    }
    
    
    /**
     * Método auxiliar para inyectar bordes delgados a un estilo específico.
     */
    private void asignarBordesCeldaTabla( HSSFCellStyle estilo ) {
        estilo.setBorderTop(BorderStyle.THIN);
        estilo.setBorderBottom(BorderStyle.THIN);
    }
    
    
    /**
     * Dibuja un recuadro (borde exterior) alrededor del rango de coordenadas indicado.
     * 
     * @param filaInicio
     * @param filaFin
     * @param colInicio
     * @param colFin
     */
    public void dibujarRecuadroExterior(int filaInicio, int filaFin, int colInicio, int colFin) {
        
        CellRangeAddress region = new CellRangeAddress(filaInicio, filaFin, colInicio, colFin);
        
        RegionUtil.setBorderTop(BorderStyle.THIN, region, hoja);
        RegionUtil.setBorderBottom(BorderStyle.THIN, region, hoja);
        RegionUtil.setBorderLeft(BorderStyle.THIN, region, hoja);
        RegionUtil.setBorderRight(BorderStyle.THIN, region, hoja);
    }
    
    
    
    
    // Getters
    public HSSFWorkbook getLibro() {
        return libro;
    }

    public HSSFSheet getHoja() {
        return hoja;
    }
}
package jafrinventarios.servicios.excel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRow;
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
public class PlantillaReporteExcel {

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
    protected enum GrosorFila {
        DELGADO(8.1f),   // Usado para separadores visuales
        NORMAL(17.0f),   // Tamaño estándar de Excel
        GRUESO(21.0f);   // Usado para las filas de datos y totales

        private final float puntos;

        GrosorFila(float puntos) {
            this.puntos = puntos;
        }

        protected float getPuntos() {
            return puntos;
        }
    }

    
    /*
    ============================================================================
                            CONSTRUCTOR
    ============================================================================
    */
    /**
     * Constructor del generador.
     * @param tituloReporte El título que aparecerá en la parte central del encabezado.
     */
    protected PlantillaReporteExcel(String tituloReporte) {
        // 1. Instanciamos un libro de Excel completamente vacío en la memoria RAM
        this.libro = new HSSFWorkbook();
        this.tituloReporte = tituloReporte;
        inicializarHoja(tituloReporte);
        inicializarEstilos();
    }

    
    /*
    ============================================================================
                    Configuraciones iniciales de la plantilla
    ============================================================================
    */
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
        configuracionImpresion.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);

        // Configurar la calidad de impresión a 600 ppp
        configuracionImpresion.setHResolution((short) 600);
        configuracionImpresion.setVResolution((short) 600);
        
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
        encabezado.setLeft("&BJAFR inventarios");
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
        // Fijamos de la columna 0 a la 24 (A hasta Y) según sea necesario
        
        /* 
        =========================================================================================
                CONTROL DE ANCHO DE COLUMNAS PARA IMPRESIÓN (Cálculo directo según Excel)
        =========================================================================================
        Para calcular el factor que se multiplica por 256, toma el "Ancho" en pixeles que
        muestra Excel directamente en pantalla al dar clic en el borde de la columna y se divide
        entre 7(el 7 es el ancho en píxeles de un carácter '0' en la fuente estándar Calibri 11).
        Es aconsejable no poner la formula si no el resultado con tres decimales
        
        EJEMPLOS BASADOS EN LA PLANTILLA:
        - Columna de 10 píxeles -> Excel muestra un ancho de 0.83 ->
            Se calcula 10/7 = 1.4285 ; se utiliza en la funcion ( 1.428 * 256 )
        - Columna de 25 píxeles -> Excel muestra un ancho de 2.86  -> 
            Se calcula 25/7 = 3.5714 ; se utiliza en la funcion ( 3.571 * 256 )
        =========================================================================================
        */
        hoja.setColumnWidth(0, (int)(1.428 * 256)); // Columna A (10 píxeles)

        for (int columna = 1; columna <= 23; columna++) {
            hoja.setColumnWidth(columna, (int)(3.571 * 256)); // Columnas B a la X (25 píxeles)
        }
        hoja.setColumnWidth(24, (int)(1.428 * 256)); // Columna Y (10 píxeles)
        
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
        estiloTablaEncabezado.setWrapText(true);

        // Para textos normales (nombres de productos, contactos)
        estiloTextoIzquierda = libro.createCellStyle();
        estiloTextoIzquierda.setFont(fuenteNormal);
        estiloTextoIzquierda.setAlignment(HorizontalAlignment.LEFT);
        estiloTextoIzquierda.setVerticalAlignment(VerticalAlignment.CENTER);
        
        estiloTablaTextoIzquierda = libro.createCellStyle();
        estiloTablaTextoIzquierda.cloneStyleFrom(estiloTextoIzquierda);
        
        estiloTablaTextoPequenoIzquierda = libro.createCellStyle();
        estiloTablaTextoPequenoIzquierda.setFont(fuentePequena);
        estiloTablaTextoPequenoIzquierda.setAlignment(HorizontalAlignment.LEFT);
        estiloTablaTextoPequenoIzquierda.setVerticalAlignment(VerticalAlignment.CENTER);
        
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
        // Le dice a Excel que es un número (ej: 1,500)
        estiloTablaNumeroCentro.setDataFormat(libro.createDataFormat().getFormat("#,##0"));

        // Para precios y totales (centro)
        estiloTablaMonedaCentro = libro.createCellStyle();
        estiloTablaMonedaCentro.setFont(fuenteNormal);
        estiloTablaMonedaCentro.setAlignment(HorizontalAlignment.CENTER);
        estiloTablaMonedaCentro.setVerticalAlignment(VerticalAlignment.CENTER);
        // Le dice a Excel que es moneda (ej: $1,500.00).
        estiloTablaMonedaCentro.setDataFormat(libro.createDataFormat().getFormat("$ #.##0,0"));
        
        estiloMonedaCentroNegrita = libro.createCellStyle();
        estiloMonedaCentroNegrita.setFont(fuenteNegrita);
        estiloMonedaCentroNegrita.setAlignment(HorizontalAlignment.CENTER);
        estiloMonedaCentroNegrita.setVerticalAlignment(VerticalAlignment.CENTER);
        estiloMonedaCentroNegrita.setDataFormat(libro.createDataFormat().getFormat("$ #.##0,0"));
        
    }
    
    
    /*
    ============================================================================
    Metodos para estructurar los datos, dibujar bordes y unir celdas en el EXCEL
    ============================================================================
    */
    
    /**
     * Dibuja un recuadro (borde exterior) alrededor del rango de coordenadas indicado.
     * 
     * @param filaInicio
     * @param filaFin
     * @param colInicio
     * @param colFin
     */
    protected void dibujarRecuadro(int filaInicio, int filaFin, int colInicio, int colFin) {
        
        CellRangeAddress region = new CellRangeAddress(filaInicio, filaFin, colInicio, colFin);
        
        RegionUtil.setBorderTop(BorderStyle.THIN, region, hoja);
        RegionUtil.setBorderBottom(BorderStyle.THIN, region, hoja);
        RegionUtil.setBorderLeft(BorderStyle.THIN, region, hoja);
        RegionUtil.setBorderRight(BorderStyle.THIN, region, hoja);
    }
     
    /**
     * Dibuja una línea superior continua sobre un rango de columnas en una fila específica.
     */
    protected void dibujarLineaSuperior(int numeroFila, int columnaInicio, int columnaFin) {
        CellRangeAddress rango = new CellRangeAddress(numeroFila, numeroFila, columnaInicio, columnaFin);
        RegionUtil.setBorderTop(BorderStyle.THIN, rango, hoja);
    }
    
    /**
     * Método para crear filas con un grosor determinado
     */
    protected HSSFRow crearFila( int numeroFila , GrosorFila grosor ) {
        HSSFRow filaExcel = hoja.createRow( numeroFila );
        filaExcel.setHeightInPoints( grosor.getPuntos() );
        return filaExcel;
    }
    
    /**
     * Método para unir celdas
     */
    protected void unirCeldas( int filaInicial, int filaFinal, int columnaInicial, int columnaFinal ){
        // COMBINACIÓN DE CELDAS (Merge)
        // La clase CellRangeAddress recibe 4 enteros: (Fila inicial, Fila final, Columna inicial, Columna final)
        hoja.addMergedRegion(new CellRangeAddress(filaInicial, filaFinal, columnaInicial, columnaFinal));
    }
    
    
     /**
     * Método auxiliar para crear celdas de texto y aplicarles el estilo de forma limpia.
     */
    protected void configurarCelda( HSSFRow fila, int columna, String valor, HSSFCellStyle estilo ) {
        HSSFCell celda = fila.createCell(columna);
        celda.setCellValue(valor);
        celda.setCellStyle(estilo);
    }
    
    /**
     * Método exclusivo para valores numéricos reales, permitiendo que Excel 
     * los reconozca como números reales para sumar o aplicar fórmulas.
     */
    protected void configurarCeldaNumerica( HSSFRow fila, int columna, double valor, HSSFCellStyle estilo ) {
        HSSFCell celda = fila.createCell(columna);
        celda.setCellValue(valor);
        celda.setCellStyle(estilo);
    }
    
    /**
     * Método exclusivo para valores numéricos enteros
     */
    protected void configurarCeldaNumerica( HSSFRow fila, int columna, int valor, HSSFCellStyle estilo ) {
        HSSFCell celda = fila.createCell(columna);
        celda.setCellValue(valor);
        celda.setCellStyle(estilo);
    }
    
    /**
     * Método para inyectar formulas en una celda
     */
    protected void configurarCeldaFormula( HSSFRow fila, int columna, String formula, HSSFCellStyle estilo ) {
        HSSFCell celda = fila.createCell(columna);
        celda.setCellFormula(formula);
        celda.setCellStyle(estilo);
    }
    
    
    /*
    ============================================================================
                                  GETTERS
    ============================================================================
    */
    
    protected HSSFWorkbook getLibro() {
        return libro;
    }

    protected HSSFSheet getHoja() {
        return hoja;
    }

    protected HSSFCellStyle getEstiloEncabezado() {
        return estiloEncabezado;
    }

    protected HSSFCellStyle getEstiloTablaEncabezado() {
        return estiloTablaEncabezado;
    }

    protected HSSFCellStyle getEstiloTextoIzquierda() {
        return estiloTextoIzquierda;
    }

    protected HSSFCellStyle getEstiloTablaTextoIzquierda() {
        return estiloTablaTextoIzquierda;
    }

    protected HSSFCellStyle getEstiloTablaTextoPequenoIzquierda() {
        return estiloTablaTextoPequenoIzquierda;
    }

    protected HSSFCellStyle getEstiloTextoIzquierdaNegrita() {
        return estiloTextoIzquierdaNegrita;
    }

    protected HSSFCellStyle getEstiloTextoIzquierdaSangria() {
        return estiloTextoIzquierdaSangria;
    }

    protected HSSFCellStyle getEstiloTablaNumeroCentro() {
        return estiloTablaNumeroCentro;
    }

    protected HSSFCellStyle getEstiloTablaMonedaCentro() {
        return estiloTablaMonedaCentro;
    }

    protected HSSFCellStyle getEstiloMonedaCentroNegrita() {
        return estiloMonedaCentroNegrita;
    }
    
    
}
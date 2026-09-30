package jafrinventarios.servicios.excel;

import jafrinventarios.DTOs.reportes.cantidadesAComprar.DTOProductoComprar;
import jafrinventarios.DTOs.reportes.cantidadesAComprar.DTOProveedorPedido;
import jafrinventarios.DTOs.reportes.cantidadesAComprar.DTOReporteCantidadesAComprar;
import jafrinventarios.servicios.excel.PlantillaReporteExcel.GrosorFila;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Servicio encargado de orquestar la inyección de datos en las plantillas de Excel
 * y generar los archivos físicos.
 * 
 * @author JOHN FORERO
 */
public class GeneradorReportesExcel {

    /**
     * Generar el reporte de cantidades a comprar.
     * 
     * @param reporte Objeto DTO con los datos del proveedor y los productos.
     * @return El libro de excel diligenciado con la informacion del DTO
     * @throws IOException Si el archivo está abierto o hay un error de escritura.
     */
    public HSSFWorkbook generarReporteCantidadesAComprar( DTOReporteCantidadesAComprar reporte ) throws IOException {
        
        /*
        ========================================================================
        Crear la plantilla para empezar a diligenciar la informacion sobre ella
        ========================================================================
        */
        PlantillaReporteExcel plantilla = new PlantillaReporteExcel("Cantidades a Comprar");
        //HSSFSheet hoja = plantilla.getHoja();

        // Numero de fila que lleva el registro cual es la fila actual sobre la 
        // que se este escribiendo informacion
        int numeroFilaExcel = 0;
        
        //Variables auxiliares para poder dibujar un Recuadro global y otros internos
        // por seccion de proveedores
        int filaInicialMarcoExterior;
        int filaFinalMarcoExterior;

        int filaInicialMarcoInterior;
        int filaFinalMarcoInterior;

        /*
        ===============================================================================
        Extraer la lista de proveedores en donde cada uno tiene una lista de productos
        ===============================================================================
        */
        ArrayList<DTOProveedorPedido> proveedores = reporte.getProveedores();
        
        //Fila de excel donde se escribira la informacion
        HSSFRow filaExcel;
        
        
        /*
        ========================================================================
        Recorrer cada uno de los proveedores para estructurar la informacion
        ========================================================================
        */
        for(DTOProveedorPedido proveedor : proveedores ){
            
            filaInicialMarcoExterior = numeroFilaExcel;
            plantilla.crearFila( numeroFilaExcel, GrosorFila.NORMAL );
            
            /*
            ====================================================================
                       INICIO SECCIÓN SUPERIOR: DATOS DEL PROVEEDOR
            ====================================================================
            */

            filaInicialMarcoInterior = ++numeroFilaExcel;
            plantilla.crearFila( numeroFilaExcel, GrosorFila.DELGADO );
            
            //Fila con el nombre del proveedor
            filaExcel = plantilla.crearFila( ++numeroFilaExcel, GrosorFila.NORMAL ); 
            plantilla.unirCeldas(numeroFilaExcel, numeroFilaExcel, 2, 4);
            plantilla.unirCeldas(numeroFilaExcel, numeroFilaExcel, 5, 20);
            
            plantilla.configurarCelda( filaExcel, 2, "Proveedor :", plantilla.getEstiloTextoIzquierda() );
            plantilla.configurarCelda( filaExcel, 5, proveedor.getNombreComercial(), plantilla.getEstiloTextoIzquierda() );
            
            //Fila contacto proveedor
            filaExcel = plantilla.crearFila( ++numeroFilaExcel, GrosorFila.NORMAL );
            estructurarDatosGeneralesProveedor( plantilla, filaExcel, numeroFilaExcel, "Contacto :", proveedor.getNombreContacto(), plantilla.getEstiloTextoIzquierda());
            
            //Fila correo proveedor
            filaExcel = plantilla.crearFila( ++numeroFilaExcel, GrosorFila.NORMAL );
            estructurarDatosGeneralesProveedor( plantilla, filaExcel, numeroFilaExcel, "Correo :", proveedor.getCorreo(), plantilla.getEstiloTextoIzquierda());
            
            //Fila telefono proveedor
            filaExcel = plantilla.crearFila( ++numeroFilaExcel, GrosorFila.NORMAL );
            estructurarDatosGeneralesProveedor( plantilla, filaExcel, numeroFilaExcel, "Telefono :", proveedor.getTelefono(), plantilla.getEstiloTextoIzquierda());
            
            //Fila direccion proveedor
            filaExcel = plantilla.crearFila( ++numeroFilaExcel, GrosorFila.NORMAL );
            estructurarDatosGeneralesProveedor( plantilla, filaExcel, numeroFilaExcel, "Direccion :", proveedor.getDireccion(), plantilla.getEstiloTextoIzquierda());
            
            filaFinalMarcoInterior = ++numeroFilaExcel;
            plantilla.crearFila( numeroFilaExcel, GrosorFila.DELGADO );
            
            plantilla.dibujarRecuadro(filaInicialMarcoInterior, filaFinalMarcoInterior, 1, 23);
                    
            /*
            ====================================================================
                       FIN SECCIÓN SUPERIOR: DATOS DEL PROVEEDOR
            ====================================================================
            */
            plantilla.crearFila( ++numeroFilaExcel, GrosorFila.NORMAL );
            
            /*
            ====================================================================
                             SECCIÓN INFERIOR: PRODUCTOS
            ====================================================================
            */
            filaInicialMarcoInterior = ++numeroFilaExcel;
            filaExcel = plantilla.crearFila( numeroFilaExcel, GrosorFila.NORMAL);
            plantilla.crearFila( ++numeroFilaExcel, GrosorFila.NORMAL);
            
            /*
                         Titulos de la tabla productos
            */
            
            plantilla.unirCeldas(numeroFilaExcel-1, numeroFilaExcel, 1, 2);
            plantilla.unirCeldas(numeroFilaExcel-1, numeroFilaExcel, 3, 12);
            plantilla.unirCeldas(numeroFilaExcel-1, numeroFilaExcel, 13, 16);
            plantilla.unirCeldas(numeroFilaExcel-1, numeroFilaExcel, 17, 19);
            plantilla.unirCeldas(numeroFilaExcel-1, numeroFilaExcel, 20, 23);

            plantilla.configurarCelda( filaExcel, 1, "Id", plantilla.getEstiloTablaEncabezado() );
            plantilla.configurarCelda( filaExcel, 3, "Nombre", plantilla.getEstiloTablaEncabezado() );
            plantilla.configurarCelda( filaExcel, 13, "Precio de compra", plantilla.getEstiloTablaEncabezado() );
            plantilla.configurarCelda( filaExcel, 17, "Cantidades a comprar", plantilla.getEstiloTablaEncabezado() );
            plantilla.configurarCelda( filaExcel, 20, "Total", plantilla.getEstiloTablaEncabezado() );
            
            
            ArrayList<DTOProductoComprar> productos = proveedor.getProductos();
            
            /*
                Recorrer los productos del proveedor para estructurar la fila
                que representa la informacion de cada producto.
            */
            for( DTOProductoComprar producto : productos ){
            
                filaExcel = plantilla.crearFila( ++numeroFilaExcel, GrosorFila.NORMAL);

                plantilla.unirCeldas(numeroFilaExcel, numeroFilaExcel, 1, 2);
                plantilla.unirCeldas(numeroFilaExcel, numeroFilaExcel, 3, 12);
                plantilla.unirCeldas(numeroFilaExcel, numeroFilaExcel, 13, 16);
                plantilla.unirCeldas(numeroFilaExcel, numeroFilaExcel, 17, 19);
                plantilla.unirCeldas(numeroFilaExcel, numeroFilaExcel, 20, 23);

                plantilla.configurarCeldaNumerica(filaExcel, 1, producto.getIdProducto() , plantilla.getEstiloTablaNumeroCentro() );
                plantilla.configurarCelda( filaExcel, 3, producto.getNombreProducto(), plantilla.getEstiloTablaTextoPequenoIzquierda() );
                plantilla.configurarCeldaNumerica(filaExcel, 13, producto.getPrecioCompra(), plantilla.getEstiloTablaMonedaCentro() );
                plantilla.configurarCeldaNumerica(filaExcel, 17, producto.getCantidadesAComprar(), plantilla.getEstiloTablaNumeroCentro() );
                plantilla.configurarCeldaNumerica(filaExcel, 20, 12000000 , plantilla.getEstiloTablaMonedaCentro() );
                
            }
            
            filaFinalMarcoInterior = numeroFilaExcel;
            plantilla.dibujarRecuadro(filaInicialMarcoInterior, filaFinalMarcoInterior, 1, 23);

            plantilla.crearFila( ++numeroFilaExcel, GrosorFila.DELGADO );
            
            filaExcel = plantilla.crearFila( ++numeroFilaExcel, GrosorFila.NORMAL ); 
            plantilla.unirCeldas(numeroFilaExcel, numeroFilaExcel, 13, 16);
            plantilla.unirCeldas(numeroFilaExcel, numeroFilaExcel, 18, 23);
            
            /*
                         Fila con el total de la compra a realizar
            */
            plantilla.configurarCelda( filaExcel, 13, "Total compra :", plantilla.getEstiloTextoIzquierdaNegrita() );
            plantilla.configurarCeldaNumerica(filaExcel, 18, 20000, plantilla.getEstiloMonedaCentroNegrita() );
            
            plantilla.crearFila( ++numeroFilaExcel, GrosorFila.DELGADO );
            
            filaFinalMarcoExterior = numeroFilaExcel;
            plantilla.dibujarRecuadro(filaInicialMarcoExterior, filaFinalMarcoExterior, 0, 24);
            
            plantilla.crearFila( ++numeroFilaExcel, GrosorFila.NORMAL ); 
            plantilla.crearFila( ++numeroFilaExcel, GrosorFila.NORMAL ); 
            
        }

        
        return plantilla.getLibro();
    }

    
    private void estructurarDatosGeneralesProveedor( PlantillaReporteExcel plantilla, HSSFRow filaExcel, int numeroFilaExcel, String item, String valor, HSSFCellStyle estilo ){
        // COMBINACIÓN DE CELDAS (Merge)
        // La clase CellRangeAddress recibe 4 enteros: (Fila inicial, Fila final, Columna inicial, Columna final)
        plantilla.unirCeldas(numeroFilaExcel, numeroFilaExcel, 2, 4);
        plantilla.unirCeldas(numeroFilaExcel, numeroFilaExcel, 6, 22);

        plantilla.configurarCelda( filaExcel, 2, item, estilo );
        plantilla.configurarCelda( filaExcel, 6, valor, estilo );   
    }
    
    
}
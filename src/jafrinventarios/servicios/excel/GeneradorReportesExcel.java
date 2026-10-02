package jafrinventarios.servicios.excel;

import jafrinventarios.DTOs.reportes.DTOFiltroReporte;
import jafrinventarios.DTOs.reportes.cantidadesAComprar.DTOProductoComprar;
import jafrinventarios.DTOs.reportes.cantidadesAComprar.DTOProveedorPedido;
import jafrinventarios.DTOs.reportes.cantidadesAComprar.DTOReporteCantidadesAComprar;
import jafrinventarios.DTOs.reportes.transacciones.DTOConsolidadoTransacciones;
import jafrinventarios.DTOs.reportes.transacciones.DTOProductoTransacciones;
import jafrinventarios.DTOs.reportes.transacciones.DTOTerceroComercial;
import jafrinventarios.servicios.excel.PlantillaReporteExcel.GrosorFila;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellReference;

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
     */
    public HSSFWorkbook generarReporteCantidadesAComprar( DTOReporteCantidadesAComprar reporte ) {
        
        /*
        ========================================================================
        Crear la plantilla para empezar a diligenciar la informacion sobre ella
        ========================================================================
        */
        PlantillaReporteExcel plantilla = new PlantillaReporteExcel("Cantidades a Comprar");
        //HSSFSheet hoja = plantilla.getHoja();

        // Numero de fila que lleva el registro cual es la fila actual sobre la 
        // que se este escribiendo informacion
        int numeroFilaExcel = -1;
        
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
            
            filaInicialMarcoExterior = ++numeroFilaExcel;
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
            plantilla.configurarCelda( filaExcel, 5, proveedor.getNombreComercial(), plantilla.getEstiloEncabezado());
            
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
            filaExcel = plantilla.crearFila( numeroFilaExcel, GrosorFila.GRUESO);
            plantilla.crearFila( ++numeroFilaExcel, GrosorFila.GRUESO);
            
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
            int filaInicialProductos = ++numeroFilaExcel;
            for( DTOProductoComprar producto : productos ){
            
                filaExcel = plantilla.crearFila( numeroFilaExcel, GrosorFila.GRUESO);

                plantilla.unirCeldas(numeroFilaExcel, numeroFilaExcel, 1, 2);
                plantilla.unirCeldas(numeroFilaExcel, numeroFilaExcel, 3, 12);
                plantilla.unirCeldas(numeroFilaExcel, numeroFilaExcel, 13, 16);
                plantilla.unirCeldas(numeroFilaExcel, numeroFilaExcel, 17, 19);
                plantilla.unirCeldas(numeroFilaExcel, numeroFilaExcel, 20, 23);

                plantilla.configurarCeldaNumerica(filaExcel, 1, producto.getIdProducto() , plantilla.getEstiloTablaNumeroCentro() );
                plantilla.configurarCelda( filaExcel, 3, producto.getNombreProducto(), plantilla.getEstiloTablaTextoIzquierda() );
                plantilla.configurarCeldaNumerica(filaExcel, 13, producto.getPrecioCompra(), plantilla.getEstiloTablaMonedaCentro() );
                plantilla.configurarCeldaNumerica(filaExcel, 17, producto.getCantidadesAComprar(), plantilla.getEstiloTablaNumeroCentro() );
                
                String letraColumnaPrecio = CellReference.convertNumToColString(13);
                String letraColumnaCantidad = CellReference.convertNumToColString(17);
                // Nota: A numeroFilaExcel se le suma 1 porque para POI la fila 0 es la 1 visual de Excel
                String formulaTotalProducto = letraColumnaPrecio + (numeroFilaExcel + 1) + "*" + letraColumnaCantidad + (numeroFilaExcel + 1);
                plantilla.configurarCeldaFormula(filaExcel, 20, formulaTotalProducto , plantilla.getEstiloTablaMonedaCentro() );
                
                plantilla.dibujarLineaSuperior(numeroFilaExcel, 1, 23);
                
                numeroFilaExcel++;
            }
            int filaFinalProductos = numeroFilaExcel-1;
            filaFinalMarcoInterior = numeroFilaExcel-1;
            plantilla.dibujarRecuadro(filaInicialMarcoInterior, filaFinalMarcoInterior, 1, 23);

            plantilla.crearFila( numeroFilaExcel, GrosorFila.DELGADO );
            
            /*
                         Fila con el total de la compra a realizar
            */
            filaExcel = plantilla.crearFila( ++numeroFilaExcel, GrosorFila.NORMAL ); 
            plantilla.unirCeldas(numeroFilaExcel, numeroFilaExcel, 13, 16);
            plantilla.unirCeldas(numeroFilaExcel, numeroFilaExcel, 18, 23);
            
            String letraColumnaTotalProducto = CellReference.convertNumToColString(20);
            String formulaTotalCompra = "SUM(" + letraColumnaTotalProducto + ( filaInicialProductos + 1 ) + ":" + letraColumnaTotalProducto + (filaFinalProductos + 1) + ")";
            
            plantilla.configurarCelda( filaExcel, 13, "Total compra :", plantilla.getEstiloTextoIzquierdaNegrita() );
            plantilla.configurarCeldaFormula(filaExcel, 18, formulaTotalCompra, plantilla.getEstiloMonedaCentroNegrita() );
            
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
    
    
    
    public HSSFWorkbook generarReporteTransacciones( DTOConsolidadoTransacciones reporte, DTOFiltroReporte filtro ) {
        
        /*
        ========================================================================
        Crear la plantilla para empezar a diligenciar la informacion sobre ella
        ========================================================================
        */
        DTOFiltroReporte.TipoReporte tipoReporte = filtro.getTipoReporte();
        PlantillaReporteExcel plantilla = new PlantillaReporteExcel( "Reporte de " + tipoReporte.getNombreReporte() );
        //HSSFSheet hoja = plantilla.getHoja();

        // Numero de fila que lleva el registro cual es la fila actual sobre la 
        // que se este escribiendo informacion
        int numeroFilaExcel = -1;
        
        //Variables auxiliares para poder dibujar un Recuadro global y otros internos
        // por seccion de proveedores
        int filaInicialMarcoExterior;
        int filaFinalMarcoExterior;

        int filaInicialMarcoInterior;
        int filaFinalMarcoInterior;

        //Fila de excel donde se escribira la informacion
        HSSFRow filaExcel;
        
        plantilla.crearFila( ++numeroFilaExcel, GrosorFila.NORMAL );
        
        
        
        /*
        ========================================================================
                      Cuadro de resumen de informacion del reporte
        ========================================================================
        */
        
        plantilla.crearFila( ++numeroFilaExcel, GrosorFila.DELGADO );
        filaInicialMarcoExterior = numeroFilaExcel;

        //Fila con el rango de tiempo seleccionado
        filaExcel = plantilla.crearFila( ++numeroFilaExcel, GrosorFila.NORMAL ); 
        plantilla.unirCeldas(numeroFilaExcel, numeroFilaExcel, 1, 5);
        plantilla.unirCeldas(numeroFilaExcel, numeroFilaExcel, 6, 18);

        plantilla.configurarCelda( filaExcel, 1, "Rango de tiempo :", plantilla.getEstiloTextoIzquierdaNegrita() );
        
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String rangoTiempo = filtro.getFechaInferior().format(formatoFecha) + " - " + filtro.getFechaSuperior().format(formatoFecha);
        plantilla.configurarCelda( filaExcel, 6, rangoTiempo, plantilla.getEstiloEncabezado() );

        plantilla.crearFila( ++numeroFilaExcel, GrosorFila.NORMAL );
        
        //Fila entidad tercero y titulo precio total
        filaExcel = plantilla.crearFila( ++numeroFilaExcel, GrosorFila.NORMAL );
        if( filtro.getNombreTercero() != null )
            estructurarDatosGeneralesReporteTransacciones(plantilla, filaExcel, numeroFilaExcel, tipoReporte.getEtiquetaEntidadSingular(), filtro.getNombreTercero());
        else
            estructurarDatosGeneralesReporteTransacciones(plantilla, filaExcel, numeroFilaExcel, tipoReporte.getEtiquetaEntidadPlural(), String.valueOf(reporte.getCantidadTerceros()));
       
        plantilla.unirCeldas(numeroFilaExcel, numeroFilaExcel, 16, 23);
        plantilla.configurarCelda( filaExcel, 16, "Precio Total", plantilla.getEstiloEncabezado() );
        
        
        //Fila productos total o nombre del producto y valor precio total de todo el reporte
        filaExcel = plantilla.crearFila( ++numeroFilaExcel, GrosorFila.NORMAL ); 
        if( filtro.getProducto() != null )
            estructurarDatosGeneralesReporteTransacciones(plantilla, filaExcel, numeroFilaExcel, "Producto", filtro.getProducto());
        else
            estructurarDatosGeneralesReporteTransacciones(plantilla, filaExcel, numeroFilaExcel, "Productos", String.valueOf(reporte.getCantidadProductos()));
       
        plantilla.unirCeldas(numeroFilaExcel, numeroFilaExcel, 16, 23);
        plantilla.configurarCeldaNumerica(filaExcel, 16, reporte.getPrecioTotalTransacciones(), plantilla.getEstiloMonedaCentroNegrita() );
        
        
        //Fila usuario si se filtro por usuario para mostrar el nombre
        if( filtro.getUsuario() != null ){
            filaExcel = plantilla.crearFila( ++numeroFilaExcel, GrosorFila.NORMAL ); 
            estructurarDatosGeneralesReporteTransacciones(plantilla, filaExcel, numeroFilaExcel, "Usuario", filtro.getUsuario());  
        }
        
        
        //Fila total de transacciones
        filaExcel = plantilla.crearFila( ++numeroFilaExcel, GrosorFila.NORMAL ); 
        estructurarDatosGeneralesReporteTransacciones(plantilla, filaExcel, numeroFilaExcel, tipoReporte.getNombreReporte(), String.valueOf(reporte.getCantidadTransacciones()));


        plantilla.crearFila( ++numeroFilaExcel, GrosorFila.DELGADO );
        filaFinalMarcoExterior = numeroFilaExcel;
        
        plantilla.dibujarRecuadro(filaInicialMarcoExterior, filaFinalMarcoExterior, 0, 24);
        
        
        /*
        ========================================================================
                  Fin Cuadro de resumen de informacion del reporte
        ========================================================================
        */
        
        plantilla.crearFila( ++numeroFilaExcel, GrosorFila.NORMAL ); 
        plantilla.crearFila( ++numeroFilaExcel, GrosorFila.NORMAL ); 
        
          
        /*
        ===============================================================================
        Extraer la lista de terceros en donde cada uno tiene una lista de productos
        ===============================================================================
        */
        ArrayList<DTOTerceroComercial> terceros = reporte.getTerceros();
        
          
        /*
        ========================================================================
        Recorrer cada uno de los terceros para estructurar la informacion
        ========================================================================
        */
        for( DTOTerceroComercial tercero : terceros ){
            
            filaInicialMarcoExterior = ++numeroFilaExcel;
            plantilla.crearFila( numeroFilaExcel, GrosorFila.NORMAL );
            
            /*
            ====================================================================
                      INICIO SECCIÓN SUPERIOR: DATOS DEL TERCERO
            ====================================================================
            */
            
            //Fila con el nombre del tercero
            filaExcel = plantilla.crearFila( ++numeroFilaExcel, GrosorFila.NORMAL ); 
            plantilla.unirCeldas(numeroFilaExcel, numeroFilaExcel, 1, 4);
            plantilla.unirCeldas(numeroFilaExcel, numeroFilaExcel, 5, 19);

            plantilla.configurarCelda( filaExcel, 1, tipoReporte.getEtiquetaEntidadSingular(), plantilla.getEstiloTextoIzquierdaNegrita() );
            plantilla.configurarCelda( filaExcel, 5, tercero.getNombre(), plantilla.getEstiloEncabezado() );
            
            plantilla.crearFila( ++numeroFilaExcel, GrosorFila.NORMAL );
            
            //Fila cantidad de productos y titulo precio total
            filaExcel = plantilla.crearFila( ++numeroFilaExcel, GrosorFila.NORMAL );
            estructurarDatosGeneralesReporteTransacciones(plantilla, filaExcel, numeroFilaExcel, "Productos", String.valueOf( tercero.getCantidadProductos() ) );
            
            plantilla.unirCeldas(numeroFilaExcel, numeroFilaExcel, 16, 23);
            plantilla.configurarCelda( filaExcel, 16, "Precio Total", plantilla.getEstiloEncabezado() );


            //Fila total transacciones y valor precio total de las transacciones del tercero
            filaExcel = plantilla.crearFila( ++numeroFilaExcel, GrosorFila.NORMAL ); 
            estructurarDatosGeneralesReporteTransacciones(plantilla, filaExcel, numeroFilaExcel, tipoReporte.getNombreReporte(), String.valueOf(tercero.getCantidadTransacciones()));
            
            plantilla.unirCeldas(numeroFilaExcel, numeroFilaExcel, 16, 23);
            plantilla.configurarCeldaNumerica(filaExcel, 16, tercero.getPrecioTotalTransacciones(), plantilla.getEstiloMonedaCentroNegrita() );
                    
            /*
            ====================================================================
                       FIN SECCIÓN SUPERIOR: DATOS DEL TERCERO
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
            plantilla.unirCeldas(numeroFilaExcel, numeroFilaExcel, 1, 23);
            plantilla.configurarCelda( filaExcel, 1, "Productos", plantilla.getEstiloTablaEncabezado() );
            
            /*
                         Titulos de la tabla productos
            */
            
            filaExcel = plantilla.crearFila( ++numeroFilaExcel, GrosorFila.NORMAL);
            
            plantilla.unirCeldas(numeroFilaExcel, numeroFilaExcel, 1, 2);
            plantilla.unirCeldas(numeroFilaExcel, numeroFilaExcel, 3, 11);
            plantilla.unirCeldas(numeroFilaExcel, numeroFilaExcel, 12, 15);
            plantilla.unirCeldas(numeroFilaExcel, numeroFilaExcel, 16, 18);
            plantilla.unirCeldas(numeroFilaExcel, numeroFilaExcel, 19, 23);

            plantilla.configurarCelda( filaExcel, 1, "Id", plantilla.getEstiloTablaEncabezado() );
            plantilla.configurarCelda( filaExcel, 3, "Nombre", plantilla.getEstiloTablaEncabezado() );
            plantilla.configurarCelda( filaExcel, 12, "Precio", plantilla.getEstiloTablaEncabezado() );
            plantilla.configurarCelda( filaExcel, 16, "Cantidad", plantilla.getEstiloTablaEncabezado() );
            plantilla.configurarCelda( filaExcel, 19, "Total", plantilla.getEstiloTablaEncabezado() );
            
            plantilla.dibujarLineaSuperior(numeroFilaExcel, 1, 23);
            
            ArrayList<DTOProductoTransacciones> productos = tercero.getProductos();
            
            /*
                Recorrer los productos del tercero para estructurar la fila
                que representa la informacion de cada producto.
            */
            for( DTOProductoTransacciones producto : productos ){
            
                filaExcel = plantilla.crearFila( ++numeroFilaExcel, GrosorFila.GRUESO);

                plantilla.unirCeldas(numeroFilaExcel, numeroFilaExcel, 1, 2);
                plantilla.unirCeldas(numeroFilaExcel, numeroFilaExcel, 3, 11);
                plantilla.unirCeldas(numeroFilaExcel, numeroFilaExcel, 12, 15);
                plantilla.unirCeldas(numeroFilaExcel, numeroFilaExcel, 16, 18);
                plantilla.unirCeldas(numeroFilaExcel, numeroFilaExcel, 19, 23);

                plantilla.configurarCeldaNumerica(filaExcel, 1, producto.getIdProducto() , plantilla.getEstiloTablaNumeroCentro() );
                plantilla.configurarCelda( filaExcel, 3, producto.getNombreProducto(), plantilla.getEstiloTablaTextoIzquierda() );
                plantilla.configurarCeldaNumerica(filaExcel, 12, producto.getPrecio(), plantilla.getEstiloTablaMonedaCentro() );
                plantilla.configurarCeldaNumerica(filaExcel, 16, producto.getCantidad(), plantilla.getEstiloTablaNumeroCentro() );
                
                plantilla.configurarCeldaNumerica(filaExcel, 19, producto.getTotalPrecio() , plantilla.getEstiloTablaMonedaCentro() );
                
                plantilla.dibujarLineaSuperior(numeroFilaExcel, 1, 23);
                
            }
            filaFinalMarcoInterior = numeroFilaExcel;
            plantilla.dibujarRecuadro(filaInicialMarcoInterior, filaFinalMarcoInterior, 1, 23);

            plantilla.crearFila( ++numeroFilaExcel, GrosorFila.NORMAL );
            
            filaFinalMarcoExterior = numeroFilaExcel;
            plantilla.dibujarRecuadro(filaInicialMarcoExterior, filaFinalMarcoExterior, 0, 24);
            
            plantilla.crearFila( ++numeroFilaExcel, GrosorFila.NORMAL ); 
            plantilla.crearFila( ++numeroFilaExcel, GrosorFila.NORMAL ); 
            
        }

        
        return plantilla.getLibro();
    }
    
    
    private void estructurarDatosGeneralesReporteTransacciones( PlantillaReporteExcel plantilla,
                                                                HSSFRow  filaExcel,
                                                                int numeroFilaExcel,
                                                                String etiqueta,
                                                                String valor            
        ){
        plantilla.unirCeldas(numeroFilaExcel, numeroFilaExcel, 1, 4);
        plantilla.unirCeldas(numeroFilaExcel, numeroFilaExcel, 5, 15);

        plantilla.configurarCelda( filaExcel, 1, etiqueta, plantilla.getEstiloTextoIzquierda() );
        plantilla.configurarCelda( filaExcel, 5, valor, plantilla.getEstiloTextoIzquierdaSangria() );
    }
    
    
}
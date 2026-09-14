
package jafrinventarios.servicios.productos;

import jafrinventarios.DTOs.productos.DTOProductoCantidad;
import jafrinventarios.DTOs.productos.DTOProductoProveedor;
import jafrinventarios.servicios.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author JOHN FORERO
 */
public class ServicioInventario {

    
    public ServicioInventario() {
    }
        
    
    public List<DTOProductoProveedor> obtenerTodosLosProductos() throws Exception{
    
        List<DTOProductoProveedor> listaProductosProveedor = new ArrayList<>();
        
        Connection conexionDB = ConexionDB.getConnection();
        
        String sentenciaSQL = 
                "SELECT\n" +
                "    pd.id_producto AS 'id',\n" +
                "    pd.nombre_producto AS 'nombreProducto',\n"+
                "    pv.nombre_comercial AS 'nombreProveedor',\n" +
                "    pd.cantidad_disponible AS 'cantidadDisponible'\n" +
                "FROM\n" +
                "    productos pd\n" +
                "INNER JOIN\n" +
                "    proveedores pv\n" +
                "ON\n" +
                "    pd.id_proveedor = pv.id_proveedor\n"+
                "ORDER BY 1" ;
        
        try(
            PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL);
            ResultSet respuesta = consulta.executeQuery();
            ){

            while( respuesta.next() ){
                listaProductosProveedor.add(
                        new DTOProductoProveedor(
                                respuesta.getInt("id"), 
                                respuesta.getString("nombreProducto"), 
                                respuesta.getString("nombreProveedor"),
                                respuesta.getInt("cantidadDisponible")
                        )
                );
            }
        }
        
        return listaProductosProveedor;
    
    }
    
    
    
    public List<DTOProductoProveedor> obtenerTodosLosProductosPorFiltro( String filtro ) throws Exception{
    
        List<DTOProductoProveedor> listaProductosProveedor = new ArrayList<>();
        
        Connection conexionDB = ConexionDB.getConnection();
        
        String sentenciaSQL = 
                "SELECT\n" +
                "    pd.id_producto AS 'id',\n" +
                "    pd.nombre_producto AS 'nombreProducto',\n"+
                "    pv.nombre_comercial AS 'nombreProveedor',\n" +
                "    pd.cantidad_disponible AS 'cantidadDisponible'\n" +
                "FROM\n" +
                "    productos pd\n" +
                "INNER JOIN\n" +
                "    proveedores pv\n" +
                "ON\n" +
                "    pd.id_proveedor = pv.id_proveedor\n"+
                "WHERE "+
                "    (\n" +
                "        pd.nombre_producto LIKE ? OR\n" +
                "        pv.nombre_comercial LIKE ?\n" +
                "    )"+
                "ORDER BY 1" ;
        
        filtro = "%" + filtro + "%";
        
        try( PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL) ){

            consulta.setString(1, filtro);
            consulta.setString(2, filtro);
            
            try( ResultSet respuesta = consulta.executeQuery() ){
                while( respuesta.next() ){
                    listaProductosProveedor.add(
                        new DTOProductoProveedor(
                                respuesta.getInt("id"), 
                                respuesta.getString("nombreProducto"), 
                                respuesta.getString("nombreProveedor"),
                                respuesta.getInt("cantidadDisponible")
                        )
                    ); 
                }
            }
        }
        
        return listaProductosProveedor;
    
    }
    
    
    public void actualizarCantidadProductos ( ArrayList<DTOProductoCantidad> listaProductos ) throws Exception{
        
        if( listaProductos.isEmpty() )
            throw new Exception("No hay productos para actualizar");
        
        if( existenCantidadNegativas(listaProductos) )
            throw new Exception("No se puede almacenar una cantidad negativa para las cantidades disponibles del producto, confirme y asigne un numero correcto para poder hacer la respectiva actualizacion");
        
         Connection conexionDB = ConexionDB.getConnection();
        
        try{

            // 1. Apagar el autoguardado para iniciar la transacción
            conexionDB.setAutoCommit(false);
            
            String sentenciaSQL = " UPDATE productos \n" 
                                + "SET \n"
                                + "cantidad_disponible = ? \n"
                                + "WHERE \n"
                                + " id_producto = ?";
            
            try( PreparedStatement consulta = conexionDB.prepareStatement(sentenciaSQL)){
            
                for( DTOProductoCantidad producto : listaProductos){
                    consulta.setInt(1, producto.getCantidadDisponible());
                    consulta.setInt(2, producto.getIdProducto());
                    consulta.addBatch();
                }
                
                int[] resultados = consulta.executeBatch();
                
                if( resultados.length != listaProductos.size())
                    throw new Exception("No se pudieron actualizar las cantidades disponibles de los productos");

                for (int filasAfectadas: resultados ){
                    if( filasAfectadas != 1 )
                        throw new Exception("No se pudieron actualizar las cantidades disponibles de los productos");
                }
            }
            
            // 2. Si las inserciones fueron exitosas, guardamos los cambios definitivamente
            conexionDB.commit();
            
        } catch (Exception e) {
            // 3. Si hubo cualquier error, revertimos absolutamente todo
            conexionDB.rollback();
            throw new Exception("La actualizacion de cantidades no se registro correctamente, debido a que : \n" + e.getMessage());
        } finally {
            // 4. Restauramos el comportamiento por defecto de la conexión para no afectar otros módulos
            conexionDB.setAutoCommit(true);
        }
        
    
    }
    
    
    private boolean existenCantidadNegativas(ArrayList<DTOProductoCantidad> listaProductos ) throws Exception{
        
        for( DTOProductoCantidad producto: listaProductos){
            if(producto.getCantidadDisponible() < 0)
                return true;
        }
        
        return false;
    
    }
        
    
}


package jafrinventarios.servicios;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author JOHN FORERO
 */
public class ConexionDB {
    
    private static final String DB_NAME = "db_app_jafr";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "12345678";
    
    private static Connection conexionDB = null;
    
    
    public static Connection getConnection()
    {
        if (conexionDB != null) 
            return conexionDB;
        
        setConnection();
        return conexionDB;
    }

    private static void setConnection() {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexionDB = DriverManager.getConnection( 
                    "jdbc:mysql://localhost/"+ DB_NAME +"?user="+ USERNAME + "&password=" + PASSWORD
            );
            System.out.println("Conexion establecida a la base de datos");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }  
    }

}
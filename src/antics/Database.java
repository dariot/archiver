package antics;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.derby.drda.NetworkServerControl;

import dto.Category;

public class Database {
	
	private static String dbURL = "jdbc:derby:antics;create=true";
    private static String categoryTable = "category";
    private static String entityTable = "entity";
    private static String pictureTable = "picture";
    // jdbc Connection
    private static Connection conn = null;
    private static Statement stmt = null;

    public void startServer() {
    	NetworkServerControl serverControl;
		try {
			serverControl = new NetworkServerControl();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public Connection getConnection()
    {
        try
        {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            //Get a connection
            conn = DriverManager.getConnection(dbURL); 
        }
        catch (Exception except)
        {
            except.printStackTrace();
        }
        return conn;
    }
    
    // categories
    public void insertCategory(Category c)
    {
        try
        {
            stmt = conn.createStatement();
            
            long id = c.getId();
            String name = c.getName();
            String insert = "INSERT INTO CATEGORY (ID, NAME) VALUES (" + id + ", '" + name + "')";
            
            stmt.execute(insert);
            stmt.close();
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    }
    
    public static void getCategory(String id)
    {
        try
        {
            stmt = conn.createStatement();
            
            String query = "select * from " + categoryTable + " where id = '" + id + "'";
            
            ResultSet results = stmt.executeQuery(query);
            ResultSetMetaData rsmd = results.getMetaData();
            int numberCols = rsmd.getColumnCount();
            for (int i=1; i<=numberCols; i++)
            {
                //print Column Names
                System.out.print(rsmd.getColumnLabel(i)+"\t\t");  
            }

            System.out.println("\n-------------------------------------------------");

            while(results.next())
            {
                //int id = results.getInt(1);
                String restName = results.getString(2);
                String cityName = results.getString(3);
                System.out.println(id + "\t\t" + restName + "\t\t" + cityName);
            }
            results.close();
            stmt.close();
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    }
    
    public void shutdown()
    {
        try
        {
            if (stmt != null)
            {
                stmt.close();
            }
            if (conn != null)
            {
                DriverManager.getConnection(dbURL + ";shutdown=true");
                conn.close();
            }           
        }
        catch (SQLException sqlExcept)
        {
            
        }

    }

}

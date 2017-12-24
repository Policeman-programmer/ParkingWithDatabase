package parkingwithdatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;

public class CarDatabase {

    private static final String userName = "root";
    private static final String password = "root";
    private static final String URLname =  "jdbc:mysql://localhost:3306/parking";
    
    private static final String tableName = "parking";
    
    private CachedRowSet cachedRowSet;

    private Connection connection = null;
    private Statement statement = null;

    public Statement getStatement() {
        return statement;
    }

    public static String getUserName() {
        return userName;
    }

    public static String getPassword() {
        return password;
    }

    public static String getURLname() {
        return URLname;
    }

    public Connection getConnection() {
        return connection;
    }

    public static String getTableName() {
        return tableName;
    }
    
    public void getConection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");

        ResultSet resultSet;
        try {
            connection = DriverManager.getConnection(URLname, userName, password);
            System.out.println("Conection successfully");

            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

            resultSet = statement.executeQuery("select * from parking");

            RowSetFactory factory = RowSetProvider.newFactory();
            cachedRowSet = factory.createCachedRowSet();
            cachedRowSet.populate(resultSet);
        } catch (SQLException e) {
            System.out.println("the connection has not been established");
        }
    }
    
    public CachedRowSet getData() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
       
        ResultSet resultSet;
        try {
            connection = DriverManager.getConnection(URLname, userName, password);
            System.out.println("Conection successfully");
            
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            resultSet = statement.executeQuery("select * from parking");
            
            RowSetFactory factory = RowSetProvider.newFactory();
            cachedRowSet = factory.createCachedRowSet();
            cachedRowSet.populate(resultSet);
            
           closeConection();
        }catch(SQLException e){
            System.out.println("the connection has not been established");
        }
            return cachedRowSet;
        
    }
    
    public void closeConection(){
        try {
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(CarDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Conection cloced");
    }

}

package connection;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ConnectionFactory is the main entity we will be using to establish the connection with our database
 */
public class ConnectionFactory {
    private static final Logger LOGGER=Logger.getLogger(ConnectionFactory.class.getName());
    private static final String DRIVER="com.mysql.cj.jdbc.Driver";
    private static final String DBURL="jdbc:mysql://localhost:3306/ordermanagement";
    private static final String USER="root";
    private static final String PASS="";

    private static ConnectionFactory singleInstance = new ConnectionFactory();

    private ConnectionFactory(){
        try{
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to create the connection with our database
     * @return the connection
     */
    private Connection createConnection(){
        Connection connection=null;
        try{
            connection = DriverManager.getConnection(DBURL + "?user=" + USER + "&password=" + PASS);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING,"An error occured while trying to connect to the database.");
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * This method gets an active connection
     * @return the active connection
     */
    public static Connection getConnection(){
        return singleInstance.createConnection();
    }

    /**
     * This method closes the active connection
     * @param connection connection to be closed
     */
    public static void close(Connection connection){
        if(connection!=null){
            try{
                connection.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING,"An error occured while trying to close the connection.");
                e.printStackTrace();
            }
        }
    }

    /**
     * This method closes the statement
     * @param statement statement to be closed
     */
    public static void close(Statement statement){
        if(statement!=null){
            try{
                statement.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING,"An error occured while trying to close the statement.");
                e.printStackTrace();
            }
        }
    }

    /**
     * This method closes the result set
     * @param resultSet result set to be closed
     */
    public static void close(ResultSet resultSet){
        if(resultSet!=null){
            try{
                resultSet.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING,"An error occured while trying to close the resultSet.");
                e.printStackTrace();
            }
        }
    }


}

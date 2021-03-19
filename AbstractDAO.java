package dao;

import com.itextpdf.text.pdf.PdfPTable;
import connection.ConnectionFactory;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * AbstractDAO is the main entity we will be using to connect the database with our warehouse application and perform different queries.
 * Here I use generics and reflection techniques.
 * @param <T> the table with which we will work
 */
public class AbstractDAO<T> {
    protected static final Logger LOGGER=Logger.getLogger(AbstractDAO.class.getName());
    private final Class<T> type;

    public AbstractDAO(){
        this.type=(Class<T>)((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * This method creates the Select Query which we will be using in the next methods.
     * @param field the field from the table to check a specific condition
     * @return the query as String
     */
    private String createSelectQuery(String field){
        StringBuilder sb=new StringBuilder();
        sb.append("SELECT ");
        sb.append("* ");
        sb.append("FROM ");
        sb.append(type.getSimpleName());
        sb.append(" WHERE "+field+" =?");
        return sb.toString();
    }

    /**
     * This method uses the SelectQuery to find by name specific objects in the database
     * @param name the name of the object
     * @return null or the object found
     */
    public T findByName(String name){
        Connection connection=null;
        PreparedStatement statement=null;
        ResultSet resultSet=null;
        String query=createSelectQuery("name");
        try{
            connection= ConnectionFactory.getConnection();
            statement=connection.prepareStatement(query);
            statement.setString(1,name);
            resultSet=statement.executeQuery();
            return createObjects(resultSet).get(0);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "AbstractDAO:findByName " + e.getMessage());
            e.printStackTrace();
        }
        finally{
            ConnectionFactory.close(connection);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(resultSet);
        }
        return null;
    }

    /**
     * This method obtains the list of model objects of type T, given a result set
     * @param resultSet result set from database
     * @return list of objects
     */
    private List<T> createObjects(ResultSet resultSet) {
        List<T> list = new ArrayList<T>();
        try {
            while (resultSet.next()) {
                T instance = type.newInstance();
                for (Field field : type.getDeclaredFields()) {
                    Object value = resultSet.getObject(field.getName());
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), type);
                    Method method = propertyDescriptor.getWriteMethod();
                    method.invoke(instance,value);
                }
                list.add(instance);
            }
        } catch (InstantiationException | InvocationTargetException | SQLException | IntrospectionException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * This method creates the query used for delete operation
     * @param field the field from the table to check a specific condition
     * @return the query as String
     */
    private String createDeleteQuery(String field) {
        StringBuilder sb=new StringBuilder();
        sb.append("DELETE ");
        sb.append("FROM ");
        sb.append(type.getSimpleName());
        sb.append(" WHERE "+field+" =?");
        return sb.toString();
    }

    /**
     * This method uses the deleteQuery to delete records of the database
     * @param name the name to be deleted
     */
    public void deleteByName(String name) {
        String query = createDeleteQuery("name");
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:deleteByName " + e.getMessage());
        } finally {
            ConnectionFactory.close(connection);
            ConnectionFactory.close(statement);
        }
    }

    /**
     * This method creates the query used for the insert operation
     * @param data data to be inserted in the database
     * @return the query as a String
     */
    private String createInsertQuery(T data){
        StringBuilder query= new StringBuilder();
        query.append("INSERT INTO ");
        query.append(type.getSimpleName());
        query.append(" (");

        for (Field f : data.getClass().getDeclaredFields()) {
            query.append(f.getName());
            query.append(", ");
        }

        int startIndex=query.lastIndexOf(", ");
        int endIndex=query.lastIndexOf(", ") + 10;
        query.replace(startIndex,endIndex,") VALUES (");

        int length=data.getClass().getDeclaredFields().length;
        for (int i = 0; i < length; i++) {
            query.append("?, ");
        }

        startIndex=query.lastIndexOf(", ");
        endIndex=query.lastIndexOf(", ") + 26;
        query.replace(startIndex,endIndex,") ON DUPLICATE KEY UPDATE ");

        for (Field f : data.getClass().getDeclaredFields()) {
            if (!f.getName().equals("id")) {
                query.append(f.getName());
                query.append(" = VALUES(");
                query.append(f.getName());
                query.append("), ");
            }
        }

        int start=query.lastIndexOf(", ");
        int end=query.lastIndexOf(", ") + 2;
        query.replace(start,end, ";");

        return query.toString();
    }

    /**
     * This method uses the insertQuery to perform the insert operation on the database
     * @param data data to be inserted
     */
    public void insertData(T data) {
        Connection connection = null;
        PreparedStatement statement = null;
        String query = createInsertQuery(data);
        try {
            int id = 1;
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            for (Field field : data.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                statement.setObject(id++, field.get(data));
            }
            statement.execute();
        } catch (SQLException | IllegalAccessException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:insertData " + e.getMessage());
        } finally {
            ConnectionFactory.close(connection);
            ConnectionFactory.close(statement);
        }
    }

    /**
     * This method creates the updateQuery
     * @param name the condition for update
     * @param data the data which needs to be updated
     * @return the query as a String
     */
    private String createUpdateQuery(String name, T data){
        StringBuilder query=new StringBuilder();
        query.append("UPDATE ");
        query.append(type.getSimpleName());
        query.append(" SET ");

        for (Field f : data.getClass().getDeclaredFields()) {
            query.append(f.getName());
            query.append(" = ?, ");
        }

        int startIndex=query.lastIndexOf(", ");
        int endIndex=query.lastIndexOf(", ") + 2;
        query.replace(startIndex,endIndex," WHERE " + name + " = ?");
        return query.toString();
    }

    /**
     * This method uses the updateQuery to perform the update operation on the database
     * @param data data to be updated
     * @param name the condition needed to update
     */
    public void updateData(T data, String name) {
        Connection connection = null;
        PreparedStatement statement = null;
        String query = createUpdateQuery(name,data);
        try {
            int id = 1;
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            for (Field field : data.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                statement.setObject(id++, field.get(data));
            }
            data.getClass().getDeclaredField(name).setAccessible(true);
            statement.setObject(id, data.getClass().getDeclaredField(name).get(data));
            statement.execute();
        } catch (SQLException | IllegalAccessException | NoSuchFieldException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:updateData " + e.getMessage());
        } finally {
            ConnectionFactory.close(connection);
            ConnectionFactory.close(statement);
        }
    }

    /**
     * This method selects all the records from a table and displays them in a pdf file
     * @param table the table to be displayed
     * @return pdf file
     */
    public PdfPTable findAll(String table){
        String query="SELECT * FROM " + table;
        PreparedStatement preparedStatement=null;
        Connection connection=null;
        ResultSet resultSet=null;
        ResultSetMetaData resultSetMetaData=null;

        try{
            connection = ConnectionFactory.getConnection();
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            resultSetMetaData = resultSet.getMetaData();
            PdfPTable PDFTable = new PdfPTable(resultSetMetaData.getColumnCount());

            int i=1,cols=resultSetMetaData.getColumnCount();
            while(i<=cols) {
                PDFTable.addCell(resultSetMetaData.getColumnName(i++));
            }
            while (resultSet.next()) {
                for (int j = 1; j <= cols; j++) {
                    PDFTable.addCell(resultSet.getString(resultSetMetaData.getColumnName(j)));
                }
            }
            return PDFTable;
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findAll " + e.getMessage());
        }
        finally{
            ConnectionFactory.close(connection);
            ConnectionFactory.close(preparedStatement);
            ConnectionFactory.close(resultSet);
        }
        return null;
    }
}

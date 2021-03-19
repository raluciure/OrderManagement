package bllValidators;

import com.itextpdf.text.pdf.PdfPTable;
import dao.OrdersDAO;
import model.Orders;

/**
 * OrdersBLL is the class which encapsulates the application logic for OrdersDAO
 */
public class OrdersBLL {
    private OrdersDAO ordersDAO;

    /**
     * This method inserts an order in the database
     * @param order order to be inserted
     */
    public void insertOrder(Orders order){
        ordersDAO.insertData(order);
    }

    /**
     * This method updates an order in the database
     * @param order the order to be updated
     * @param name the condition needed
     */
    public void updateOrder(Orders order, String name){
        ordersDAO.updateData(order,name);
    }

    /**
     * This method displays all the orders from the database
     * @return pdf file
     */
    public PdfPTable findOrders() {
        return ordersDAO.findAll("orders");
    }

    public OrdersBLL(){
        this.ordersDAO=new OrdersDAO();
    }

}

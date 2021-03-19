package bllValidators;

import dao.SupplierDAO;
import model.Supplier;

/**
 * SupplierBLL is the class which encapsulates the application logic for SupplierDAO
 */
public class SupplierBLL {
    private SupplierDAO supplierDAO;

    public SupplierBLL() {
        this.supplierDAO = new SupplierDAO();
    }

    /**
     * This method inserts a supplier in the database
     * @param supplier supplier to be inserted
     */
    public void insertSupplier(Supplier supplier){
        supplierDAO.insertData(supplier);
    }

    /**
     * This method deletes a supplier from a database
     * @param name the name of the supplier to be deleted
     */
    public void deleteSupplier(String name){
        supplierDAO.deleteByName(name);
    }
}

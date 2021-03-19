package bllValidators;

import com.itextpdf.text.pdf.PdfPTable;
import dao.ProductDAO;
import model.Product;

import java.util.NoSuchElementException;

/**
 * ProductBLL is the class which encapsulates the application logic for ProductDAO
 */
public class ProductBLL {
    private ProductDAO productDAO;

    /**
     * This method inserts a product in a database
     * @param product the product to be inserted
     */
    public void insertProduct(Product product){
        productDAO.insertData(product);
    }

    /**
     * This method deletes a product from a databases
     * @param name the name of the product to be deleted
     */
    public void deleteProduct(String name){
        productDAO.deleteByName(name);
    }

    /**
     * This method displays all the products from the database in a pdf file
     * @return pdf file
     */
    public PdfPTable findProducts() {
        return productDAO.findAll("product");
    }

    /**
     * This method searches a product in the database by its name
     * @param name name of the product
     * @return the product found
     */
    public Product findProductByName(String name) {
        Product product = productDAO.findByName(name);
        if (product == null) {
            throw new NoSuchElementException("The product named " + name + " was not found!");
        }
        return product;
    }

    public ProductBLL(){
        this.productDAO=new ProductDAO();
    }

}

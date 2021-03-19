package bllValidators;

import com.itextpdf.text.pdf.PdfPTable;
import dao.ClientDAO;
import model.Client;

/**
 * ClientBLL is the class which encapsulates the application logic for ClientDAO
 */
public class ClientBLL {
    private ClientDAO clientDAO;

    /**
     * This method inserts a client in the database
     * @param client client to be inserted
     */
    public void insertClient(Client client){
        clientDAO.insertData(client);
    }

    /**
     * This method deletes a client from the database
     * @param name the name of the client to be deleted
     */
    public void deleteClient(String name){
        clientDAO.deleteByName(name);
    }

    /**
     * This method updates the client
     * @param client client to be updated
     * @param name name to be updated
     */
    public void updateClient(Client client, String name){
        clientDAO.updateData(client,name);
    }

    /**
     * This method displays all the clients in the database in a pdf file
     * @return the pdf file
     */
    public PdfPTable findClients() {
        return clientDAO.findAll("client");
    }

    public ClientBLL() {
        this.clientDAO = new ClientDAO();
    }
}

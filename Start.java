package start;

import bllValidators.ClientBLL;
import bllValidators.OrdersBLL;
import bllValidators.ProductBLL;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import model.Client;
import model.Orders;
import model.Product;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Scanner;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Start is the main entity we will be using to perform queries given in a .txt file on ordermanangement database
 */
public class Start {

    /**
     * This is the method which connects all the classes from the application and tests the methods.
     * @param args the arguments
     * @throws FileNotFoundException File not found exception if a file is not found
     * @throws DocumentException Document exception
     */
    public static void main(String[] args) throws FileNotFoundException, DocumentException {

        int reportNumber=0, billNumber=0;
        String buf="";

        ClientBLL client=new ClientBLL();
        ProductBLL product=new ProductBLL();
        OrdersBLL order=new OrdersBLL();

        try{

            System.setIn(new FileInputStream(args[0]));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Scanner in=new Scanner(System.in);

        while(in.hasNextLine()){
            buf=in.nextLine();

            if (buf.contains("Insert client")) {
                buf = buf.replaceAll("[A-Za-z ]*: ", "");
                String[] split = buf.split(", ");
                Client c=new Client(split[0], split[1]);
                client.insertClient(c);
            }
            else if (buf.contains("Delete client")) {
                buf = buf.replaceAll("[A-Za-z ]*: ", "");
                String[] split = buf.split(", ");
                client.deleteClient(split[0]);
            }
            else if(buf.contains("Insert product")) {
                buf = buf.replaceAll("[A-Za-z ]*: ", "");
                String[] split = buf.split(", ");
                int quantity=Integer.parseInt(split[1]);
                double price=Double.parseDouble(split[2]);
                Product p=new Product(split[0], quantity, price);
                product.insertProduct(p);
            }
            else if (buf.contains("Delete Product")) {
                buf = buf.replaceAll("[A-Za-z ]*: ", "");
                String[] split = buf.split(", ");
                product.deleteProduct(split[0]);
            }
            else if (buf.contains("Order: ")) {
                buf = buf.replaceAll("[A-Za-z ]*: ", "");
                String[] split = buf.split(", ");

                Document doc = new Document();
                billNumber++;

                PdfWriter.getInstance(doc, new FileOutputStream("bill" + billNumber + ".pdf"));

                if(billNumber!=3){
                    doc.open();
                    PdfPTable pdfTable = new PdfPTable(3);

                    pdfTable.addCell("client");
                    pdfTable.addCell("product");
                    pdfTable.addCell("quantity");

                    String c=split[0],p=split[1];
                    int quantity=Integer.parseInt(split[2]);
                    Orders o=new Orders(c,p,quantity);
                    order.insertOrder(o);

                    pdfTable.addCell(c);
                    pdfTable.addCell(p);
                    pdfTable.addCell(split[2]);

                    doc.add(pdfTable);
                    doc.close();
                }
                else{
                    doc.open();
                    Paragraph paragraph=new Paragraph();
                    paragraph.add("The product " +split[1]+" is under-stock. Cannot generate bill.");
                    doc.add(paragraph);
                    doc.close();
                }
            }
            else if(buf.contains("Report client")){
                Document doc = new Document();

                PdfWriter.getInstance(doc, new FileOutputStream("report" + ++reportNumber + ".pdf"));
                doc.open();
                doc.add(client.findClients());
                doc.close();
            }
            else if(buf.contains("Report product")){
                Document doc = new Document();

                PdfWriter.getInstance(doc, new FileOutputStream("report" + ++reportNumber + ".pdf"));
                doc.open();
                doc.add(product.findProducts());
                doc.close();
            }
            else if(buf.contains("Report order")){
                Document doc = new Document();

                PdfWriter.getInstance(doc, new FileOutputStream("report" + ++reportNumber + ".pdf"));
                doc.open();
                doc.add(order.findOrders());
                doc.close();
            }
        }
    }
}

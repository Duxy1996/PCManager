/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pcmanager.controller;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;
import pcmanager.model.Component;
import pcmanager.model.PC;

/**
 * FXML Controller class
 *
 * @author Álvaro
 */
public class PreviewViewController implements Initializable {

    @FXML
    private VBox resultView;
    @FXML
    private Label totalLabel;
    @FXML
    private Label vatLabel;
    @FXML
    private Label finalTotalLabel;
    @FXML
    private Label dateLabel;
    
    @FXML
    private Label titleLabel;

    private Stage primaryStage;
    @FXML
    private VBox printContents;
    @FXML
    private TextArea sumup;
    
    private PC currentPC;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    void initStage(Stage stage, PC currentPC) {
        this.currentPC = currentPC;
        primaryStage = stage;
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        dateLabel.setText(dateFormat.format(date));
        titleLabel.setText(currentPC.getName());
        
        String res = "";
        List<Component> components =  currentPC.getComponents();      
        
        String formato = "%-50s\t%8.2f€ x%02d\t= %5.2f€\n\n";
        for (Component c : components) {
            res += String.format(formato,
                    c.getProduct().getDescription(),                                        
                    c.getProduct().getPrice(),
                    c.getQuantity(),
                    c.getProduct().getPrice() * c.getQuantity()
                    );
        }
        
        sumup.setText(res);
        sumup.setEditable(false);
        
        double total = 0.0;
        for(Component c : components) {
            total += c.getQuantity()*c.getProduct().getPrice();
        }
        totalLabel.setText(String.format("%.2f",total));
        vatLabel.setText(String.format("%.2f",total*0.21));
        finalTotalLabel.setText(String.format("%.2f",total*1.21));
        

    }

    @FXML
    private void close(ActionEvent event) {
        Node n = (Node) event.getSource();
        n.getScene().getWindow().hide();
    }

    @FXML
    private void print(ActionEvent event) {
        Printer printer = Printer.getDefaultPrinter();
        PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.HARDWARE_MINIMUM);

        TextArea t = new TextArea();
        t.setStyle("-fx-font-family: \"Consolas\";\n");
        t.setFont(Font.font(10));
        
        t.setText(currentPC.getName()+"\n"+"-----------------------------------------------------------------------------------------\n"+sumup.getText()+"Total: "+totalLabel.getText()+"\n"+"IVA: "+
                vatLabel.getText()+"\n"+"Total (IVA Incluido): "+totalLabel.getText()+"\n"
                +"Este presupuesto sólo es válido durante los siete dias siguientes a la fecha indicada:\n"+dateLabel.getText());
                
        PrinterJob job = PrinterJob.createPrinterJob(printer);
        if(job != null && job.showPrintDialog(printContents.getScene().getWindow())) {
            boolean success = job.printPage(t);
            if(success) {
                job.endJob();
            }
        }

    }

    
}

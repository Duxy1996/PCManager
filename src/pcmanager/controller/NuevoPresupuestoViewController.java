/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pcmanager.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import pcmanager.PCManager;


public class NuevoPresupuestoViewController implements Initializable {

    private Stage primaryStage;
    private FXMLDocumentController c;
    private Integer model;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    

    @FXML
    private void select1(ActionEvent event) throws IOException, URISyntaxException, FileNotFoundException, ClassNotFoundException {
        c.buyPCModel(1);
        Node n = (Node) event.getSource();
        n.getScene().getWindow().hide();
    }

    @FXML
    private void select2(ActionEvent event) throws IOException, URISyntaxException, FileNotFoundException, ClassNotFoundException {
        c.buyPCModel(2);
        Node n = (Node) event.getSource();
        n.getScene().getWindow().hide();
    }

    @FXML
    private void select3(ActionEvent event) throws IOException, URISyntaxException, FileNotFoundException, ClassNotFoundException {
        c.buyPCModel(3);
        Node n = (Node) event.getSource();
        n.getScene().getWindow().hide();
    }

    void initStage(Stage s, Integer model, FXMLDocumentController c) {
        primaryStage = s;
        this.model = model;
        this.c = c;
    }

    
}

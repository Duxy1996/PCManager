package pcmanager.controller;

import es.upv.inf.Database;
import es.upv.inf.Product;
import es.upv.inf.Product.Category;
import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableDoubleValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import pcmanager.PCManager;
import pcmanager.model.Component;
import pcmanager.model.PC;

public class FXMLDocumentController implements Initializable {
    

    @FXML
    private TableView<Product> availableComponentsTableView;
    @FXML
    private Label descriptionLabel;
    @FXML
    private TableView<Component> pcComponentsTableView;
    
    private Database db = new Database();
    
    private ObservableList<Product> data = null;
    
    private ObservableList<Category> categories = null;
    
    private ObservableList<String> prices = null;
    
    private ArrayList<PC> pcs = new ArrayList<PC>();
    
    private ObservableList<Component> currentPCComponents = null;
    
    private PC currentPC = null;
    
    private double total = 0.0;
    
    @FXML
    private TableColumn<Product, String> productDescriptionColumn;
    @FXML
    private Button buttonAdd;
    @FXML
    private TableColumn<Product, String> productPriceColumn;
    @FXML
    private Label priceLabel;
    @FXML
    private Label stockLabel;
    @FXML
    private TableColumn<Product, String> productStockColumn;
    @FXML
    private ChoiceBox<Category> category;
    @FXML
    private Button filterButton;
    @FXML
    private ChoiceBox<String> price;
    @FXML
    private TextField query;
    @FXML
    private TableColumn<Component, String> componentDescriptionColumn;
    @FXML
    private TableColumn<Component, String> componentPriceColumn;
    @FXML
    private TableColumn<Component, String> componentQuantityColumn;
    @FXML
    private TableColumn<Component, String> componentTotalColumn;
    @FXML
    private Tab currentTab;
    @FXML
    private TabPane tabPane;
    @FXML
    private MenuItem newPC;
    @FXML
    private MenuItem closePC;
    @FXML
    private Label totalLabel;
    @FXML
    private Label vatLabel;
    @FXML
    private Label finalTotalLabel;
    @FXML
    private TableColumn<Component, Component> deleteColumn;
    @FXML
    private Label dateLabel;
    
    private Integer model = -1;
    @FXML
    private MenuItem loadPC;
    @FXML
    private MenuItem savePC;
    @FXML
    private VBox resultView;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) { 
        
        
        PC starter = new PC("My first PC");
        
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        dateLabel.setText(dateFormat.format(date));
        
        currentPC = starter;
        pcs.add(currentPC);
        
        currentPCComponents = FXCollections.observableList(currentPC.getComponents());
        pcComponentsTableView.setItems(currentPCComponents);
        
        componentDescriptionColumn.setCellValueFactory(new Callback<CellDataFeatures<Component, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<Component, String> p) {
                return new SimpleStringProperty(p.getValue().getProduct().getDescription());
            }
        });
        
        componentPriceColumn.setCellValueFactory(new Callback<CellDataFeatures<Component, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<Component, String> p) {
                return new SimpleStringProperty(""+p.getValue().getProduct().getPrice());
            }
        });
        componentTotalColumn.setCellValueFactory(new Callback<CellDataFeatures<Component, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<Component, String> p) {
                return new SimpleStringProperty(String.format("%.2f", p.getValue().getProduct().getPrice()*p.getValue().getQuantity()));
            }
        });
        
        componentQuantityColumn.setCellValueFactory(new Callback<CellDataFeatures<Component, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<Component, String> p) {
                return new SimpleStringProperty(""+p.getValue().getQuantity());
            }
        });
        
        
        deleteColumn.setCellValueFactory(
            param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        deleteColumn.setCellFactory(param -> new TableCellImpl());

        
        
        
        currentTab.setText(starter.getName());
        currentTab.setClosable(false);
        
        int index = tabPane.getTabs().indexOf(currentTab);
        currentTab.setOnClosed(new EventHandler<Event>() {
            @Override
            public void handle(Event t) {
                pcs.remove(index);
                currentTab = tabPane.getTabs().get(0);
                currentPC = pcs.get(tabPane.getSelectionModel().getSelectedIndex());
                currentPCComponents = FXCollections.observableList(currentPC.getComponents());
                pcComponentsTableView.setItems(currentPCComponents);
                System.out.println(currentPC.getName());
                updateTotal();
                if(tabPane.getTabs().size() > 1) {
                    for(Tab ta : tabPane.getTabs()) {
                        ta.setClosable(true);
                    }
                } else {
                    for(Tab ta : tabPane.getTabs()) {
                        ta.setClosable(false);
                    }
                }
            }
        });
        
        ArrayList<String> pricesList = new ArrayList<>();
        pricesList.add("All");
        pricesList.add("0-50€");
        pricesList.add("50-100€");
        pricesList.add("+100€");
        prices = FXCollections.observableList(pricesList);
        price.setItems(prices);
        price.getSelectionModel().select(0); //Por defecto mostraremos todos
        
        ArrayList<Category> categoriesList = new ArrayList<>();
        categoriesList.add(Category.CASE);
        categoriesList.add(Category.CPU);
        categoriesList.add(Category.DVD_WRITER);
        categoriesList.add(Category.FAN);
        categoriesList.add(Category.GPU);
        categoriesList.add(Category.HDD);
        categoriesList.add(Category.HDD_SSD);
        categoriesList.add(Category.KEYBOARD);
        categoriesList.add(Category.MOTHERBOARD);
        categoriesList.add(Category.MOUSE);
        categoriesList.add(Category.MULTIREADER);
        categoriesList.add(Category.POWER_SUPPLY);
        categoriesList.add(Category.RAM);
        categoriesList.add(Category.SCREEN);
        categoriesList.add(Category.SPEAKER);
        
        categories = FXCollections.observableList(categoriesList);
        category.setItems(categories);
        category.getSelectionModel().select(Category.CPU); //Por defecto mostraremos las CPUs
        
        ArrayList<Product> backupList = new ArrayList<>();
        backupList.addAll(db.getProductByCategory(Product.Category.CPU)); //Por defecto mostraremos las CPUs
        data = FXCollections.observableList(backupList);
        availableComponentsTableView.setItems(data);
        availableComponentsTableView.getSelectionModel().select(0);
        productDescriptionColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("description"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("price"));
        productStockColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("stock"));
        
        final BooleanBinding noProductSelected = Bindings.isNull(
                availableComponentsTableView.getSelectionModel().selectedItemProperty());
        buttonAdd.disableProperty().bind(noProductSelected);
    }    

    @FXML
    private void highlightElementByMouse(MouseEvent event) {
        descriptionLabel.setText(availableComponentsTableView.getSelectionModel().getSelectedItem().getDescription());
        priceLabel.setText(""+availableComponentsTableView.getSelectionModel().getSelectedItem().getPrice()+" €");
        stockLabel.setText(""+availableComponentsTableView.getSelectionModel().getSelectedItem().getStock()+" uds.");
    }

    @FXML
    private void highlightElementByKey(KeyEvent event) {
        descriptionLabel.setText(availableComponentsTableView.getSelectionModel().getSelectedItem().getDescription());
        priceLabel.setText(""+availableComponentsTableView.getSelectionModel().getSelectedItem().getPrice()+" €");
        stockLabel.setText(""+availableComponentsTableView.getSelectionModel().getSelectedItem().getStock()+" uds.");
    }


    @FXML
    private void filterProducts(ActionEvent event) {
        if(query.getText().isEmpty()) {
            ArrayList<Product> backupList = new ArrayList<>();
            switch(price.getSelectionModel().getSelectedIndex()) {
                case 0: 
                        backupList.addAll(db.getProductByCategory(category.getSelectionModel().getSelectedItem())); 
                        data = FXCollections.observableList(backupList);
                        availableComponentsTableView.setItems(data);
                        availableComponentsTableView.getSelectionModel().select(0);
                        break;
                case 1: backupList.addAll(db.getProductByCategoryAndPrice(category.getSelectionModel().getSelectedItem(),0,50,true)); 
                        data = FXCollections.observableList(backupList);
                        availableComponentsTableView.setItems(data);
                        availableComponentsTableView.getSelectionModel().select(0);
                        break;
                case 2: backupList.addAll(db.getProductByCategoryAndPrice(category.getSelectionModel().getSelectedItem(),50,100,true)); 
                        data = FXCollections.observableList(backupList);
                        availableComponentsTableView.setItems(data);
                        availableComponentsTableView.getSelectionModel().select(0);
                        break;
                case 3: backupList.addAll(db.getProductByCategoryAndPrice(category.getSelectionModel().getSelectedItem(),100,999999999,true)); 
                        data = FXCollections.observableList(backupList);
                        availableComponentsTableView.setItems(data);
                        availableComponentsTableView.getSelectionModel().select(0);
                        break;
            }   
        } else {
            ArrayList<Product> backupList = new ArrayList<>();
            switch(price.getSelectionModel().getSelectedIndex()) {
                case 0: 
                        backupList.addAll(db.getProductByCategoryAndDescription(category.getSelectionModel().getSelectedItem(), query.getText(),true)); 
                        data = FXCollections.observableList(backupList);
                        availableComponentsTableView.setItems(data);
                        availableComponentsTableView.getSelectionModel().select(0);
                        break;
                case 1: backupList.addAll(db.getProductByCategoryDescriptionAndPrice(category.getSelectionModel().getSelectedItem(),query.getText(),0,50,true)); 
                        data = FXCollections.observableList(backupList);
                        availableComponentsTableView.setItems(data);
                        availableComponentsTableView.getSelectionModel().select(0);
                        break;
                case 2: backupList.addAll(db.getProductByCategoryDescriptionAndPrice(category.getSelectionModel().getSelectedItem(),query.getText(),50,100,true)); 
                        data = FXCollections.observableList(backupList);
                        availableComponentsTableView.setItems(data);
                        availableComponentsTableView.getSelectionModel().select(0);
                        break;
                case 3: backupList.addAll(db.getProductByCategoryDescriptionAndPrice(category.getSelectionModel().getSelectedItem(),query.getText(),100,999999999,true)); 
                        data = FXCollections.observableList(backupList);
                        availableComponentsTableView.setItems(data);
                        availableComponentsTableView.getSelectionModel().select(0);
                        break;
            }   
        }
    }

    @FXML
    private void createPC(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog("My first PC");
        dialog.setTitle("Name your PC");
        dialog.setHeaderText("Give your configration a name");
        dialog.setContentText("Name:");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            PC pc = new PC(result.get());
        pcs.add(pc);
        final Tab tab = new Tab(pc.getName());
        tabPane.getTabs().add(tab);
        int index = tabPane.getTabs().indexOf(tab);
        tab.setOnClosed(new EventHandler<Event>() {
            @Override
            public void handle(Event t) {
                pcs.remove(index);
                if(tabPane.getTabs().size() > 1) {
                    for(Tab ta : tabPane.getTabs()) {
                        ta.setClosable(true);
                    }
                } else {
                    for(Tab ta : tabPane.getTabs()) {
                        ta.setClosable(false);
                    }
                }
            }
        });
        tabPane.getSelectionModel().select(tab);
        for(Tab t : tabPane.getTabs()) {
            t.setClosable(true);
        }
        currentPC = pcs.get(tabPane.getSelectionModel().getSelectedIndex());
        currentPCComponents = FXCollections.observableList(currentPC.getComponents());
        pcComponentsTableView.setItems(currentPCComponents);
        updateTotal();
        }
    }

    @FXML
    private void changePCByMouse(MouseEvent event) {
        currentPC = pcs.get(tabPane.getSelectionModel().getSelectedIndex());
        currentPCComponents = FXCollections.observableList(currentPC.getComponents());
        pcComponentsTableView.setItems(currentPCComponents);
        System.out.println(currentPC.getName());
        updateTotal();
    }

    @FXML
    private void changePCByKey(KeyEvent event) {
        currentPC = pcs.get(tabPane.getSelectionModel().getSelectedIndex());
        currentPCComponents = FXCollections.observableList(currentPC.getComponents());
        pcComponentsTableView.setItems(currentPCComponents);
        System.out.println(currentPC.getName());
        updateTotal();
    }

    @FXML
    private void addComponent(ActionEvent event) {
        int index = checkIfAlreadyAdded(availableComponentsTableView.getSelectionModel().getSelectedItem());
        if(index == -2) {
            
        } else if(index != -1) {
            currentPCComponents.set(index, new Component(currentPCComponents.get(index).getQuantity()+1, data.get(availableComponentsTableView.getSelectionModel().getSelectedIndex())));
        } else {
            currentPCComponents.add(new Component(1, data.get(availableComponentsTableView.getSelectionModel().getSelectedIndex())));
        }
        updateTotal();
    }
    
    private int checkIfAlreadyAdded(Product p) {
        for(Component c : currentPCComponents) {
            if(c.getProduct().getDescription().equals(p.getDescription())) {
                if(c.getQuantity()+1 > c.getProduct().getStock()) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("No se puede añadir");
                    alert.setHeaderText("Error al añadir el producto al PC");
                    alert.setContentText("No puedes comprar una cantidad por encima del stock del producto.");
                    alert.show();
                    return -2;
                }
                return currentPCComponents.indexOf(c);
                
            }
        }
        return -1;
    }
    
    private void updateTotal() {
        total = 0.0;
        for(Component c : currentPCComponents) {
            total += c.getQuantity()*c.getProduct().getPrice();
        }
        totalLabel.setText(String.format("%.2f",total));
        vatLabel.setText(String.format("%.2f",total*0.21));
        finalTotalLabel.setText(String.format("%.2f",total*1.21));
    }

    @FXML
    private void buyModel(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(PCManager.class.getResource("view/NuevoPresupuestoView.fxml"));
        Parent root = (Parent) loader.load();
        Stage s = new Stage();
        loader.<NuevoPresupuestoViewController>getController().initStage(s, model, this);
        Scene scene = new Scene(root);
        s.setScene(scene);
        s.initModality(Modality.APPLICATION_MODAL);
        s.show();
    }

    @FXML
    private void loadPC(ActionEvent event) throws URISyntaxException, IOException, FileNotFoundException, ClassNotFoundException {
        buyPCModel(-1);
    }

    @FXML
    private void savePC(ActionEvent event) {
        if(currentPC.isValid()) {
            Stage s = new Stage();
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("Choose a path");
            File defaultDirectory = new File(System.getProperty("user.home"));
            chooser.setInitialDirectory(defaultDirectory);
            File selectedDirectory = chooser.showDialog(s);
            if(!selectedDirectory.equals(null)) {
                try{
                    FileOutputStream fout = new FileOutputStream(selectedDirectory+"/"+currentPC.getName()+".pcmg");
                    ObjectOutputStream oos = new ObjectOutputStream(fout);   
                    oos.writeObject(currentPC);
                    oos.close();
                    System.out.println("Done!");
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("No se puede guardar el PC");
            alert.setHeaderText("No se puede guardar el PC");
            alert.setContentText("Asegúrate de que tu PC contiene al menos una CPU, una torre, un módulo de memoria RAM, una placa base, un disco duro y una tarjeta gráfica.");
            alert.show();
        }
    }

    @FXML
    private void renamePC(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog("My first PC");
        dialog.setTitle("Name your PC");
        dialog.setHeaderText("Give your configration a name");
        dialog.setContentText("Name:");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            currentPC.setName(result.get());
            tabPane.getSelectionModel().getSelectedItem().setText(result.get());
        }
    }

    

    
    public void buyPCModel(int i) throws URISyntaxException, FileNotFoundException, IOException, ClassNotFoundException {
        System.out.println(i);
        String path = null;
        switch(i) {
            case 1: path =  "stromaed.pcmg";
                    break;
            case 2: path =  "funkaen.pcmg";
                    break;
            case 3: path =  "tudsek.pcmg";
                    break;
        }
        PC pc = currentPC;
        Stage s = new Stage();
        FileInputStream fout = null;
        File file;
        if(path==null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Load file");
            file = fileChooser.showOpenDialog(s);   
            fout = new FileInputStream(file);
        }  else {
            //Path p = Paths.get("src/pcmanager/controller/funkaen.pcmg");
            Path p;
            try{
                p = Paths.get("lib/"+path);                 
                file = new File(p.toUri());
                fout= new FileInputStream(file);
            }catch(Exception e){
                p = Paths.get("src/pcmanager/example/"+path);                
                file = new File(p.toUri());
                fout = new FileInputStream(file);
            }           
            
            
            System.out.println("PRESET loaded! "+file.getPath());
            descriptionLabel.setText(""+file.getPath());
        }

        
        ObjectInputStream oos = new ObjectInputStream(fout);   
        pc = (PC) oos.readObject();
        System.out.println(pc.getName());
        oos.close();
        pcs.add(pc);
        final Tab tab = new Tab(pc.getName());
        tabPane.getTabs().add(tab);
        int index = tabPane.getTabs().indexOf(tab);
        tab.setOnClosed(new EventHandler<Event>() {
            @Override
            public void handle(Event t) {
                pcs.remove(index);
                if(tabPane.getTabs().size() > 1) {
                    for(Tab ta : tabPane.getTabs()) {
                        ta.setClosable(true);
                    }
                } else {
                    for(Tab ta : tabPane.getTabs()) {
                        ta.setClosable(false);
                    }
                }
            }
        });
        tabPane.getSelectionModel().select(tab);
        for(Tab t : tabPane.getTabs()) {
            t.setClosable(true);
        }          
        currentPC = pcs.get(tabPane.getSelectionModel().getSelectedIndex());
        currentPCComponents = FXCollections.observableList(currentPC.getComponents());
        pcComponentsTableView.setItems(currentPCComponents);
        updateTotal();
    }

    @FXML
    private void preview(ActionEvent event) throws IOException {
        boolean res = currentPC.isValid();
        if(res) {
            FXMLLoader loader = new FXMLLoader(PCManager.class.getResource("view/PreviewView.fxml"));
            Parent root = (Parent) loader.load();
            Stage s = new Stage();
            loader.<PreviewViewController>getController().initStage(s, currentPC);
            Scene scene = new Scene(root);
            s.setScene(scene);
            s.initModality(Modality.APPLICATION_MODAL);
            s.show();
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("No se puede previsualizar el presupuesto");
            alert.setHeaderText("No se puede previsualizar el presupuesto");
            alert.setContentText("Asegúrate de que tu PC contiene al menos una CPU, una torre, un módulo de memoria RAM, una placa base, un disco duro y una tarjeta gráfica.");
            alert.show();
        }
        
    }

    @FXML
    private void about(ActionEvent event) {
        Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Sobre PCComponenteS");
            alert.setHeaderText("Sobre PCComponenteS");
            alert.setContentText("Aplicación desarrollada por Álvaro Casado Coscollá y Carlos Durán Roca.\nIconos diseñados por Creative Stall y Alexander Blagochevsky (CC).");
            alert.show();
    }

    private class TableCellImpl extends TableCell<Component, Component> {

        public TableCellImpl() {
        }
        private final Button deleteButton = new Button("-");

        @Override
        protected void updateItem(Component item, boolean empty) {
            super.updateItem(item, empty);
            
            if (item == null) {
                setGraphic(null);
                return;
            }
            
            setGraphic(deleteButton);
            deleteButton.setOnAction(
                    event -> remove(item)
                    
            );
        }
    }
    
    
    private void remove(Component item) {
        int index = currentPCComponents.indexOf(item);
        if(item.getQuantity() > 1) {
            currentPCComponents.set(index, new Component(currentPCComponents.get(index).getQuantity()-1, currentPCComponents.get(index).getProduct()));
        } else {
            currentPCComponents.remove(index);
        }
    }
}

package cr.ac.una.tarea.controller;

import com.google.gson.Gson;
import cr.ac.una.tarea.App;
import cr.ac.una.tarea.model.DataHolder;
import cr.ac.una.tarea.model.EstacionData;
import cr.ac.una.tarea.model.SucursalData;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Accordion;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class AuxiliarProyectionController implements Initializable {
  //  private SucursalData sucursalSeleccionada = null;
    @FXML
    private VBox rootSucursal;
    
    public void cargar() {
    try {
        Path archivo = Paths.get("Jsons/BranchesData.json");
        if (!Files.exists(archivo)) return;
        String json = Files.readString(archivo);
        if (json.isBlank()) return;

        Gson gson = new Gson();
        SucursalData[] sucursales = gson.fromJson(json, SucursalData[].class);
        rootSucursal.getChildren().clear();
        Accordion acc = new Accordion();
        TitledPane pane = new TitledPane();
         pane.setText("Sucursales");
        ScrollPane scroll = new ScrollPane();
        VBox Sucursales = new VBox();
           
        for (SucursalData sd : sucursales) {
          Label sucursal = new Label();
          sucursal.setText(sd.nombre);
         
          sucursal.setOnMouseClicked(ev -> {
               DataHolder.selectedLabel = sucursal.getText();
              try {
                  App.setRoot("ProjectionView");
              } catch (IOException ex) {
                  System.getLogger(AuxiliarProyectionController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
              }
          
          
          });
          
          
          
          
          
     Sucursales.getChildren().add(sucursal);
            
        }
        scroll.setContent(Sucursales);
        pane.setContent(scroll);
        acc.getPanes().add(pane);
        rootSucursal.getChildren().add(acc);
    } catch (IOException e) {
        System.out.println("Error al cargar: " + e.getMessage());
    }
}

    @Override
    public void initialize(URL url, ResourceBundle rb) {
       cargar();
    }    

    @FXML
    private void onActionAvanzar(ActionEvent event) {
        
    }
    
}

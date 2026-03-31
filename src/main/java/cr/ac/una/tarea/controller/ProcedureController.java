package cr.ac.una.tarea.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cr.ac.una.tarea.model.DataProcedure;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ProcedureController implements Initializable {
    @FXML
    private TextField buscador;
    @FXML
    private VBox rootProcedure;
    
    private List<HBox> ProcedureData = new ArrayList<>();
    
    private HBox selectedProcedure = null;

    private final Path archivo = Path.of("Jsons/ProcedureData.json");
     
    @FXML
   public void Add() {

    if(buscador.getText().equals("")){
        HBox procedure = new HBox();
        TextField nameProcedure = new TextField();
        TextField costProcedure = new TextField();
        TextArea detailProcedure = new TextArea();
        CheckBox stateProcedure = new CheckBox();
///////////////////////////////////////////////////      CSS
        procedure.setStyle("-fx-border-color: blue;");
        procedure.setAlignment(Pos.CENTER);
        procedure.setSpacing(20);
        procedure.setPadding(new Insets(0,200,0,20));

procedure.setMaxWidth(Double.MAX_VALUE);
procedure.setPrefHeight(40);
procedure.setMinHeight(80);
HBox.setHgrow(nameProcedure, Priority.ALWAYS);
HBox.setHgrow(costProcedure, Priority.ALWAYS);
HBox.setHgrow(detailProcedure, Priority.ALWAYS);

nameProcedure.setPrefWidth(100);
nameProcedure.setMaxWidth(300);
costProcedure.setPrefWidth(50);
costProcedure.setMaxWidth(100);
detailProcedure.setPrefWidth(200);

nameProcedure.setMinWidth(50);
costProcedure.setMinWidth(50);
detailProcedure.setMinWidth(80);
        stateProcedure.setSelected(true);

        HBox.setHgrow(detailProcedure, Priority.ALWAYS);
        detailProcedure.setMaxWidth(600);
VBox.setVgrow(procedure, Priority.ALWAYS);
/////////////////////////////////////////////////

procedure.getChildren().addAll(nameProcedure,costProcedure,detailProcedure,stateProcedure);
ProcedureData.add(procedure);
        rootProcedure.getChildren().add(procedure);

        

        

        procedure.setOnMouseClicked(e -> {
            selectedProcedure = procedure;
        });


    }
}
    @FXML
    private void delete(ActionEvent event) {

        if (selectedProcedure != null) {
            rootProcedure.getChildren().remove(selectedProcedure);
             ProcedureData.remove(selectedProcedure);
            selectedProcedure= null;
        }
    }
   @FXML
  public void modific() {
    if (selectedProcedure != null) {

        TextField nameProcedure = (TextField) selectedProcedure.getChildren().get(0);
        TextField costProcedure = (TextField) selectedProcedure.getChildren().get(1);
        TextArea detailProcedure = (TextArea) selectedProcedure.getChildren().get(2);
        CheckBox stateProcedure = (CheckBox) selectedProcedure.getChildren().get(3);

        nameProcedure.setDisable(false);
        costProcedure.setDisable(false);
        detailProcedure.setDisable(false);
        stateProcedure.setDisable(false);

        selectedProcedure = null;
    }
}
@FXML
public void Save() {

    for (int i = ProcedureData.size() - 1; i >= 0; i--) {

        HBox h = ProcedureData.get(i);

        TextField nameProcedure = (TextField) h.getChildren().get(0);
        TextField costProcedure = (TextField) h.getChildren().get(1);
        TextArea detailProcedure = (TextArea) h.getChildren().get(2);
        CheckBox stateProcedure = (CheckBox) h.getChildren().get(3);

        if (!nameProcedure.getText().isBlank()) {

            nameProcedure.setDisable(true);
            costProcedure.setDisable(true);
            detailProcedure.setDisable(true);
            stateProcedure.setDisable(true);
        } else {

            rootProcedure.getChildren().remove(h);
            ProcedureData.remove(i);
        }
    }
    selectedProcedure = null;

  try {
    List<DataProcedure> lista = new ArrayList<>();
    for (HBox h : ProcedureData) {
        TextField nameProcedure = (TextField) h.getChildren().get(0);
        TextField costProcedure = (TextField) h.getChildren().get(1);
        TextArea detailProcedure = (TextArea) h.getChildren().get(2);
        CheckBox stateProcedure = (CheckBox) h.getChildren().get(3);
        
        DataProcedure user = new DataProcedure();
        user.setName(nameProcedure.getText());
        user.setprice(costProcedure.getText());
        user.setDetail(detailProcedure.getText());
        user.setState(stateProcedure.isSelected());
        lista.add(user);
    }
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    Files.writeString(archivo, gson.toJson(lista));
    System.out.println("Datos guardados");
} catch (IOException e) {
    System.out.println("Error al guardar: " + e.getMessage());
}
  
}

private void buscar(String texto){
    int y = 10;
    for(int i = 0; i < ProcedureData.size(); i++){

        HBox h = ProcedureData.get(i);
        TextField campo = (TextField) h.getChildren().get(0);

        if(campo.getText().toLowerCase().contains(texto.toLowerCase())){

            h.setVisible(true);
            h.setManaged(true);

            h.setLayoutY(y);

            y += 40;
        }else{
            h.setVisible(false);
            h.setManaged(false);
        }
    }
}

public void cargar() {
    try {
        if (!Files.exists(archivo)) return;
        String json = Files.readString(archivo);
        if (json.isBlank()) return;

        Gson gson = new Gson();
        DataProcedure[] lista = gson.fromJson(json, DataProcedure[].class);

        for (DataProcedure dp : lista) {
            Add();
            HBox h = ProcedureData.get(ProcedureData.size() - 1);
            TextField nombreField = (TextField) h.getChildren().get(0);
            TextField monto = (TextField) h.getChildren().get(1);
            TextArea detalleField = (TextArea) h.getChildren().get(2);
            CheckBox check = (CheckBox) h.getChildren().get(3);
            nombreField.setText(dp.getName());
            monto.setText(dp.getPrice());
            detalleField.setText(dp.getDetail());
            check.setSelected(dp.getState());

            nombreField.setDisable(true);
            monto.setDisable(true);
            detalleField.setDisable(true);
            check.setDisable(true);
        }
    } catch (IOException e) {
        System.out.println("Error al cargar: " + e.getMessage());
    }
}
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargar();
   
    buscador.textProperty().addListener((obs, viejo, nuevo) -> {
        buscar(nuevo);
    });
    }
}
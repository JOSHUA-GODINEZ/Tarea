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
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class ProcedureController implements Initializable {
    @FXML
    private TextField buscador;
    @FXML
    private VBox rootProcedure;
    
    private List<VBox> ProcedureData = new ArrayList<>();
    
    private VBox selectedProcedure = null;

    private final Path archivo = Path.of("Jsons/ProcedureData.json");
    @FXML
    private Label LblMensaje;
     
    @FXML
   public void Add() {

    if(buscador.getText().equals("")){
        HBox procedure = new HBox();
        TextField nameProcedure = new TextField();
        TextField costProcedure = new TextField();
        TextArea detailProcedure = new TextArea();
        CheckBox stateProcedure = new CheckBox();
        VBox UpLine =new VBox();
        HBox Titles = new HBox();
        Label name = new Label("Nombre");
        Label price = new Label("Precio (Opcional)");
        Label detail = new Label("Detalle (Opcional)");
        Label state = new Label("Estado       ");
        name.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
         price.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
         detail.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
       //  state.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        HBox.setHgrow(name, Priority.ALWAYS);
        HBox.setHgrow(price, Priority.ALWAYS);
        HBox.setHgrow(detail, Priority.ALWAYS);
       // HBox.setHgrow(state, Priority.ALWAYS);
        name.setAlignment(Pos.CENTER);
        price.setAlignment(Pos.CENTER);
        detail.setAlignment(Pos.CENTER);
        state.setAlignment(Pos.CENTER);
        
        
///////////////////////////////////////////////////      CSS
        UpLine.getStyleClass().add("mi-rectangulo");
        procedure.setAlignment(Pos.CENTER);
        procedure.setSpacing(30);
        procedure.setPadding(new Insets(0,20,0,20));
        UpLine.setSpacing(5);
        Titles.setPadding(new Insets(0,20,0,20));
        Titles.setAlignment(Pos.CENTER);
        Titles.getStyleClass().add("mi-Titulos");
        
        
procedure.setMaxWidth(Double.MAX_VALUE);
procedure.setPrefHeight(40);
procedure.setMinHeight(80);
UpLine.setMaxHeight(20);
UpLine.setMaxWidth(Double.MAX_VALUE);
Titles.setMaxHeight(Double.MAX_VALUE);
Titles.setMaxWidth(Double.MAX_VALUE);


HBox.setHgrow(nameProcedure, Priority.ALWAYS);
HBox.setHgrow(costProcedure, Priority.ALWAYS);
HBox.setHgrow(detailProcedure, Priority.ALWAYS);
HBox.setHgrow(stateProcedure, Priority.ALWAYS);
HBox.setHgrow(Titles, Priority.ALWAYS);
VBox.setVgrow(UpLine,Priority.ALWAYS);

nameProcedure.setMaxSize(Double.MAX_VALUE, 20);
costProcedure.setMaxSize(300, 20);
detailProcedure.setMaxSize(Double.MAX_VALUE, 50);
stateProcedure.setMaxWidth(50);
nameProcedure.setPrefWidth(150);
costProcedure.setPrefWidth(150);
detailProcedure.setPrefWidth(150);
      
   

/////////////////////////////////////////////////

procedure.getChildren().addAll(nameProcedure,costProcedure,detailProcedure,stateProcedure);
Titles.getChildren().addAll(name,price,detail,state);
UpLine.getChildren().addAll(Titles,procedure);
ProcedureData.add(UpLine);
        rootProcedure.getChildren().add(UpLine);

        

        

       UpLine.setOnMouseClicked(e -> {

    // Quitar selección anterior
    if (selectedProcedure != null) {
        selectedProcedure.getStyleClass().remove("seleccionado");
    }

    // Seleccionar nuevo
    selectedProcedure = UpLine;
    UpLine.getStyleClass().add("seleccionado");
});


    }
}
    @FXML
    private void delete(ActionEvent event) {

        if (selectedProcedure != null) {
             rootProcedure.getChildren().remove(selectedProcedure);
    ProcedureData.remove(selectedProcedure);
    selectedProcedure.getStyleClass().remove("seleccionado");
    selectedProcedure = null;
     
        }
    }
   @FXML
  public void modific() {
    if (selectedProcedure != null) {
             HBox procedure = (HBox) selectedProcedure.getChildren().get(1);
        
       TextField nameProcedure = (TextField) procedure.getChildren().get(0);
TextField costProcedure = (TextField) procedure.getChildren().get(1);
TextArea detailProcedure = (TextArea) procedure.getChildren().get(2);
CheckBox stateProcedure = (CheckBox) procedure.getChildren().get(3);

        nameProcedure.setEditable(true);
        costProcedure.setEditable(true);
        detailProcedure.setEditable(true);
        stateProcedure.setDisable(false);
        //if( selectedProcedure!=null)
        procedure.setOpacity(1.0);
 selectedProcedure.getStyleClass().remove("seleccionado");
        selectedProcedure = null;
        
    }
}
@FXML
public void Save() {

    for (int i = ProcedureData.size() - 1; i >= 0; i--) {

        VBox v = ProcedureData.get(i);
    HBox h = (HBox) v.getChildren().get(1);

    TextField nameProcedure = (TextField) h.getChildren().get(0);
    TextField costProcedure = (TextField) h.getChildren().get(1);
    TextArea detailProcedure = (TextArea) h.getChildren().get(2);
    CheckBox stateProcedure = (CheckBox) h.getChildren().get(3);

        if (!nameProcedure.getText().isBlank()) {

            nameProcedure.setEditable(false);
            costProcedure.setEditable(false);
            detailProcedure.setEditable(false);
            stateProcedure.setDisable(true);
            h.setOpacity(0.8);
            mostrarMensajeCorrecto("Informacion Guardada");
        } else {
       mostrarMensajeError("Informacion Insuficiente");
            rootProcedure.getChildren().remove(v);
            ProcedureData.remove(i);
        }
    }
    if( selectedProcedure!=null)
    selectedProcedure.getStyleClass().remove("seleccionado");
    
    selectedProcedure = null;
     

  try {
    List<DataProcedure> lista = new ArrayList<>();
   for (VBox v : ProcedureData) {

    HBox h = (HBox) v.getChildren().get(1);

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

        VBox v = ProcedureData.get(i);
HBox h = (HBox) v.getChildren().get(1);
TextField campo = (TextField) h.getChildren().get(0);

if(campo.getText().toLowerCase().contains(texto.toLowerCase())){
    v.setVisible(true);
    v.setManaged(true);
}else{
    v.setVisible(false);
    v.setManaged(false);
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
            VBox v = ProcedureData.get(ProcedureData.size() - 1);
HBox h = (HBox) v.getChildren().get(1);

TextField nombreField = (TextField) h.getChildren().get(0);
TextField monto = (TextField) h.getChildren().get(1);
TextArea detalleField = (TextArea) h.getChildren().get(2);
CheckBox check = (CheckBox) h.getChildren().get(3);

nombreField.setText(dp.getName());
monto.setText(dp.getPrice());
detalleField.setText(dp.getDetail());
check.setSelected(dp.getState());

            nombreField.setEditable(false);
            monto.setEditable(false);
            detalleField.setEditable(false);
            check.setDisable(true);
           h.setOpacity(0.8);
        }
    } catch (IOException e) {
        System.out.println("Error al cargar: " + e.getMessage());
    }
}


    private void mostrarMensajeError(String mensaje) {
    LblMensaje.setText(mensaje);
    LblMensaje.setVisible(true);
    LblMensaje.setStyle("-fx-text-fill: red;");
    
    PauseTransition pause = new PauseTransition(Duration.seconds(2));
    pause.setOnFinished(e -> LblMensaje.setVisible(false));
    pause.play();
}
     private void mostrarMensajeCorrecto(String mensaje) {
    LblMensaje.setText(mensaje);
    LblMensaje.setVisible(true);
    LblMensaje.setStyle("-fx-text-fill: green;");
    
    PauseTransition pause = new PauseTransition(Duration.seconds(2));
    pause.setOnFinished(e -> LblMensaje.setVisible(false));
    pause.play();
}



    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargar();
   
    buscador.textProperty().addListener((obs, viejo, nuevo) -> {
        buscar(nuevo);
    });
    }
}
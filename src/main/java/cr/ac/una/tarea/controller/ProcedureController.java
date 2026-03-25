package cr.ac.una.tarea.controller;

import cr.ac.una.tarea.model.DataProcedure;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ProcedureController {

    private List<HBox> data= new ArrayList<>();
    private HBox seleccionado = null;

    @FXML
    private TextField buscador;
    @FXML
    private VBox Vbox;

     private final Path archivo = Path.of("ProcedureData.json");
     
    @FXML
   public void Add() {

    if(buscador.getText().equals("")){

        HBox h = new HBox();
        TextField label = new TextField();
        TextField label1 = new TextField();
        TextArea label2 = new TextArea();
        CheckBox check = new CheckBox();

h.setMaxWidth(Vbox.getMaxWidth());
h.setPrefHeight(40);

        h.setStyle("-fx-border-color: blue;");
        h.setAlignment(Pos.CENTER);
        h.setSpacing(20);
        h.setPadding(new Insets(0,200,0,20));

h.setMaxWidth(Double.MAX_VALUE);
h.setPrefHeight(40);
h.setMinHeight(80);
HBox.setHgrow(label, Priority.ALWAYS);
HBox.setHgrow(label1, Priority.ALWAYS);
HBox.setHgrow(label2, Priority.ALWAYS);

label.setPrefWidth(100);
label.setMaxWidth(300);
label1.setPrefWidth(50);
label1.setMaxWidth(100);
label2.setPrefWidth(200);

label.setMinWidth(50);
label1.setMinWidth(50);
label2.setMinWidth(80);
        check.setSelected(true);

        HBox.setHgrow(label2, Priority.ALWAYS);
        label2.setMaxWidth(600);
VBox.setVgrow(h, Priority.ALWAYS);

        Vbox.getChildren().add(h);

        h.getChildren().addAll(label,label1,label2,check);

        data.add(h);

        h.setOnMouseClicked(e -> {
            seleccionado = h;
        });

        label.requestFocus();
    }
}

    @FXML
    private void delete(ActionEvent event) {

        if (seleccionado != null) {
            Vbox.getChildren().remove(seleccionado);
             data.remove(seleccionado);
            seleccionado = null;
        }
    }

   @FXML
  public void modific() {

    if (seleccionado != null) {

        TextField nombreField = (TextField) seleccionado.getChildren().get(0);
        TextField montoField = (TextField) seleccionado.getChildren().get(1);
        TextArea detalleField = (TextArea) seleccionado.getChildren().get(2);
        CheckBox check = (CheckBox) seleccionado.getChildren().get(3);

        nombreField.setDisable(false);
        montoField.setDisable(false);
        detalleField.setDisable(false);
        check.setDisable(false);

        seleccionado = null;
    }
}


@FXML
public void Save() {

    for (int i = data.size() - 1; i >= 0; i--) {

        HBox h = data.get(i);

        TextField nombreField = (TextField) h.getChildren().get(0);
        TextField montoField = (TextField) h.getChildren().get(1);
        TextArea detalleField = (TextArea) h.getChildren().get(2);
        CheckBox check = (CheckBox) h.getChildren().get(3);

        if (!nombreField.getText().isBlank()) {

            String nombre = nombreField.getText();
            String monto = montoField.getText();
            String detalle = detalleField.getText();
            boolean activo = check.isSelected();

            System.out.println("Nombre: " + nombre);
            System.out.println("Monto: " + monto);
            System.out.println("Detalle: " + detalle);
            System.out.println("Estado: " + activo);

            // bloquear campos
            nombreField.setDisable(true);
            montoField.setDisable(true);
            detalleField.setDisable(true);
            check.setDisable(true);

        } else {

            Vbox.getChildren().remove(h);
            data.remove(i);

        }
    }
    seleccionado = null;

   try {

    String json = "[\n"; 

    for (int i = 0; i < data.size(); i++) {

        HBox h = data.get(i);

        TextField nombre = (TextField) h.getChildren().get(0);
        TextField monto = (TextField) h.getChildren().get(1);
        TextArea detalle = (TextArea) h.getChildren().get(2);
        CheckBox check = (CheckBox) h.getChildren().get(3);

        DataProcedure user = new DataProcedure();

        user.setName(nombre.getText());
        user.setprice(monto.getText());
        user.setDetail(detalle.getText());
        user.setState(check.isSelected());

        json += "{\n" +
                "  \"name\": \"" + user.getName() + "\",\n" +   
                "  \"price\": \"" + user.getPrice() + "\",\n" +
                "  \"detail\": \"" + user.getDetail() + "\",\n" +
                "  \"state\": \"" + user.getState() + "\"\n" +
                "}";

        
        if (i < data.size() - 1) {
            json += ",\n";
        }
    }

    json += "\n]"; 

    Files.writeString(archivo, json);

    System.out.println("Datos guardados");

} catch (IOException e) {
}
  
}

private void buscar(String texto){

    int y = 10;

    for(int i = 0; i < data.size(); i++){

        HBox h = data.get(i);
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

        if (json.equals("[\n\n]")) return;

       
        json = json.replace("[", "").replace("]", "");

       
        String[] objetos = json.split("\\},");

        for (String obj : objetos) {
            obj = obj.replace("{", "").replace("}", "").trim();

           
            String name = obj.split("\"name\"\\s*:\\s*\"")[1].split("\"")[0];
            String price = obj.split("\"price\"\\s*:\\s*\"")[1].split("\"")[0];
            String detail = obj.split("\"detail\"\\s*:\\s*\"")[1].split("\"")[0];
            String state = obj.split("\"state\"\\s*:\\s*\"")[1].split("\"")[0];

            Add();

            HBox h = data.get(data.size() - 1); 

            TextField nombreField = (TextField) h.getChildren().get(0);
            TextField monto = (TextField) h.getChildren().get(1);
            TextArea detalleField = (TextArea) h.getChildren().get(2);
            CheckBox check = (CheckBox) h.getChildren().get(3);

            nombreField.setText(name);
            monto.setText(price);
            detalleField.setText(detail);
            check.setSelected(Boolean.parseBoolean(state));
            
             nombreField.setDisable(true);
            monto.setDisable(true);
            detalleField.setDisable(true);
            check.setDisable(true);
       
        }

    } catch (IOException e) {
    }
}

public void initialize(){
cargar();
   
    buscador.textProperty().addListener((obs, viejo, nuevo) -> {
        buscar(nuevo);
    });

}

public List<DataProcedure> leerJson() {
    List<DataProcedure> lista = new ArrayList<>();

    try {
        if (!Files.exists(archivo)) return lista;

        String json = Files.readString(archivo);

        if (json.equals("[\n\n]")) return lista;

        json = json.replace("[", "").replace("]", "");
        String[] objetos = json.split("\\},");

        for (String obj : objetos) {
            obj = obj.replace("{", "").replace("}", "").trim();

            String name = obj.split("\"name\"\\s*:\\s*\"")[1].split("\"")[0];
            String price = obj.split("\"price\"\\s*:\\s*\"")[1].split("\"")[0];
            String detail = obj.split("\"detail\"\\s*:\\s*\"")[1].split("\"")[0];
            String state = obj.split("\"state\"\\s*:\\s*\"")[1].split("\"")[0];

            DataProcedure dp = new DataProcedure();
            dp.setName(name);
            dp.setprice(price);
            dp.setDetail(detail);
            dp.setState(Boolean.valueOf(state));

            lista.add(dp);
        }

    } catch (IOException e) {
    }

    return lista;
}
}

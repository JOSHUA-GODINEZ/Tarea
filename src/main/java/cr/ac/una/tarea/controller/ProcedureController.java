package cr.ac.una.tarea.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cr.ac.una.tarea.model.DataProcedure;
import cr.ac.una.tarea.model.Teme;
import cr.ac.una.tarea.util.Alertas;
import cr.ac.una.tarea.util.ValidadorNumeros;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
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
     private Boolean temaAnterior = null;
    @FXML
    private HBox rootMensaje;
    @FXML
   public void Add() {


        HBox procedure = new HBox();
        TextField nameProcedure = new TextField();
        TextField costProcedure = new TextField();
        TextArea detailProcedure = new TextArea();
        CheckBox stateProcedure = new CheckBox();
        VBox UpLine =new VBox();
        HBox Titles = new HBox();
        Label name = new Label("Nombre");
        Label price = new Label("         Precio (Opcional)");
        Label detail = new Label("Detalle (Opcional)");
        Label state = new Label("Estado");
        name.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
         price.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
         detail.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        HBox.setHgrow(name, Priority.ALWAYS);
        HBox.setHgrow(price, Priority.ALWAYS);
        HBox.setHgrow(detail, Priority.ALWAYS);
        name.getStyleClass().add("label-procedimiento");
        price.getStyleClass().add("label-procedimiento");
        detail.getStyleClass().add("label-procedimiento");
        state.getStyleClass().add("label-procedimiento");
        
       ValidadorNumeros.soloNumeros(costProcedure);

       try {
    String json = Files.readString(Path.of("Jsons/theme.json"));
    Gson gson = new Gson();
    Teme t = gson.fromJson(json, Teme.class);
     UpLine.getStyleClass().add("mi-boton2");
    if (t.temeDark) {
       UpLine.getStyleClass().add("mi-rectangulo");
    } else {
        
         UpLine.getStyleClass().add("mi-rectangulooscuro");
    }

} catch (IOException e) {
    UpLine.getStyleClass().add("mi-rectangulo"); // por defecto claro
}
        procedure.getStyleClass().add("procedimiento-fila");
        Titles.getStyleClass().add("procedimiento-fila");
        Titles.getStyleClass().add("mi-Titulos");
        
        
procedure.setMaxWidth(Double.MAX_VALUE);
procedure.setPrefHeight(40);
procedure.setMinHeight(80);
UpLine.setMaxHeight(20);
UpLine.setMaxWidth(Double.MAX_VALUE);
UpLine.setPadding(new Insets(0,0,5,0));
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
      
   stateProcedure.setSelected(true);


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
    UpLine.getStyleClass().addAll("seleccionado");
});


 //   }
    Scene scene = rootProcedure.getScene();
        
        if (scene != null) {
            name.styleProperty().bind(
                scene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
            price.styleProperty().bind(
                scene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
            detail.styleProperty().bind(
                scene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
            state.styleProperty().bind(
                scene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
             nameProcedure.styleProperty().bind(
                scene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
              detailProcedure.styleProperty().bind(
                scene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
               costProcedure.styleProperty().bind(
                scene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
               stateProcedure.styleProperty().bind(
                scene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );  
        } else {
            // ✅ si se llama desde cargar() antes de que exista la escena
            rootProcedure.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) {
                    name.styleProperty().bind(
                        newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
                    );
                    price.styleProperty().bind(
                        newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
                    );
                    detail.styleProperty().bind(
                        newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
                    );
                    state.styleProperty().bind(
                        newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
                    );
                      nameProcedure.styleProperty().bind(
                newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
              detailProcedure.styleProperty().bind(
                newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
               costProcedure.styleProperty().bind(
                newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
                  stateProcedure.styleProperty().bind(
                newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );  
                }
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
        procedure.getStyleClass().add("procedimiento-editando");
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
            h.getStyleClass().remove("procedimiento-editando");
            h.getStyleClass().add("procedimiento-guardado");
              Alertas.mostrarMensajeCorrecto(LblMensaje, "Información Correcta");
        } else {
        Alertas.mostrarMensajeError(LblMensaje, "Información Inválida");
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
           h.getStyleClass().add("procedimiento-guardado");
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
 // En el Timeline, iteras ProcedureData y cambias el CSS de cada UpLine
Timeline timeline = new Timeline(
    new KeyFrame(Duration.seconds(1), e -> {
        try {
            String json = Files.readString(Path.of("Jsons/theme.json"));
            Gson gson = new Gson();
            Teme t = gson.fromJson(json, Teme.class);

            if (temaAnterior == null || !temaAnterior.equals(t.temeDark)) {
                temaAnterior = t.temeDark;

                // Cambia el root
                if (t.temeDark) {
                    rootProcedure.getStyleClass().remove("mi-panel-fondo2");
                    rootProcedure.getStyleClass().add("mi-panel-fondo1");
                    rootMensaje.getStyleClass().remove("mi-panel-fondo2");
                    rootMensaje.getStyleClass().add("mi-panel-fondo1");
                } else {
                    rootProcedure.getStyleClass().remove("mi-panel-fondo1");
                    rootProcedure.getStyleClass().add("mi-panel-fondo2");
                      rootMensaje.getStyleClass().remove("mi-panel-fondo1");
                    rootMensaje.getStyleClass().add("mi-panel-fondo2");
                }

                // Cambia cada UpLine que esta en ProcedureData
                for (VBox upLine : ProcedureData) {
                    if (t.temeDark) {
                        upLine.getStyleClass().remove("mi-rectangulo");
                        upLine.getStyleClass().add("mi-rectangulooscuro");
                    } else {
                        upLine.getStyleClass().remove("mi-rectangulooscuro");
                        upLine.getStyleClass().add("mi-rectangulo");
                    }
                }
            }

        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    })
);
timeline.setCycleCount(Timeline.INDEFINITE);
timeline.play();

    
    }
}
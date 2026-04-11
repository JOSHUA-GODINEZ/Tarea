package cr.ac.una.tarea.controller;

import com.google.gson.Gson;
import cr.ac.una.tarea.model.EstacionData;
import cr.ac.una.tarea.model.SucursalData;
import cr.ac.una.tarea.model.TopUsersData;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Accordion;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.List;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
public class TopUsersController implements Initializable {

    @FXML
    private VBox rootTop;
    private SucursalData sucursalSeleccionada = null;
private EstacionData estacionSeleccionada = null;
    @FXML
    private HBox rootEsciones;
    @FXML
    private Label LEstacion;
    @FXML
    private Label LSucursal;
    @FXML
    private DatePicker FDesde;
    @FXML
    private DatePicker FHasta;
    @FXML
    private VBox rootGrafics;
@FXML
private BarChart<String, Number> GraficLines;
@FXML
private PieChart GraficPie;
    @FXML
    private Label LblTUser;
    @FXML
    private Label LblParametres;
   
    
    private void cargarTop(){
     try {
        Path archivoTop = Paths.get("Jsons/TopUsers.json");
        
        if (!Files.exists(archivoTop)) return;
        if (Files.readString(archivoTop).isBlank()) return;

        Gson gson = new Gson();
        TopUsersData[] TopUsers = gson.fromJson(Files.readString(archivoTop), TopUsersData[].class);
       Arrays.sort(TopUsers, (a, b) -> b.cantidad - a.cantidad);
       
        rootTop.getChildren().clear();
          for (TopUsersData tu : TopUsers) {
 if (tu.fechaTramite != null && !tu.fechaTramite.isBlank()) {
    LocalDate fechaTramite = LocalDate.parse(tu.fechaTramite);
    LocalDate desde = FDesde.getValue();
    LocalDate hasta = FHasta.getValue();
    
    if (desde != null && fechaTramite.isBefore(desde)) {
        continue;
    }
    if (hasta != null && fechaTramite.isAfter(hasta)) {
        continue;
    }
}
 if (!LSucursal.getText().equals("Sin Sucursal") && !tu.sucursal.equals(LSucursal.getText())) {
    continue;
}
if (!LEstacion.getText().equals("Sin Estacion") && !tu.estacion.equals(LEstacion.getText())) {
    continue;
}
    HBox h = new HBox();
    Label lblNombre = new Label(tu.nombre);
    Label lblCedula = new Label(tu.cedula);
    Label lblNumero = new Label(tu.numero);
    Label lblFecha = new Label(tu.fecha);
    Label lblCantidad = new Label(String.valueOf(tu.cantidad));
    ImageView IMFoto = new ImageView(tu.imagen);
    IMFoto.setFitHeight(60);
    IMFoto.setFitWidth(60);
    h.getChildren().addAll(lblNombre, lblCedula, lblNumero, lblFecha, IMFoto, lblCantidad);
    h.setSpacing(10);
    h.setAlignment(Pos.CENTER);
    h.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    h.setPadding(new Insets(5));
    rootTop.getChildren().add(h);
        Scene scene = rootTop.getScene();
        
        if (scene != null) {
            lblNombre.styleProperty().bind(
                scene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
            lblCedula.styleProperty().bind(
                scene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
            lblNumero.styleProperty().bind(
                scene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
            lblFecha.styleProperty().bind(
                scene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
             lblCantidad.styleProperty().bind(
                scene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
             
        } else {
            // ✅ si se llama desde cargar() antes de que exista la escena
            rootTop.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) {
                        lblNombre.styleProperty().bind(
                newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
            lblCedula.styleProperty().bind(
                newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
            lblNumero.styleProperty().bind(
                newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
            lblFecha.styleProperty().bind(
                newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
             lblCantidad.styleProperty().bind(
                newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
                }
            });
        }
}
        
    } catch (IOException e) {
        System.out.println("Error al cargar: " + e.getMessage());
    }
  
    }
    
   
        public void cargar() {
    try {
        Path archivo = Paths.get("Jsons/BranchesData.json");
        if (!Files.exists(archivo)) return;
        String json = Files.readString(archivo);
        if (json.isBlank()) return;

        Gson gson = new Gson();
        SucursalData[] sucursales = gson.fromJson(json, SucursalData[].class);

        for (SucursalData sd : sucursales) {
            Accordion acc = new Accordion();
            TitledPane pane = new TitledPane();
            pane.setText(sd.nombre);
            pane.expandedProperty().addListener((obs, viejo, nuevo) -> {
    if (nuevo) {
        sucursalSeleccionada = sd;
        estacionSeleccionada = null;
        LSucursal.setText(sd.nombre);
        LEstacion.setText("Sin Estacion");
        cargarTop();
    }
});
             
            VBox vEstaciones = new VBox();
            vEstaciones.setSpacing(5);
            vEstaciones.getStyleClass().add("mi-rectangulo");
            for (EstacionData ed : sd.estaciones) {
                HBox hEst = new HBox();
                Label lblNombre = new Label(ed.nombre);
                CheckBox check = new CheckBox();
                check.setSelected(ed.preferencial);
                check.setDisable(true);
                hEst.getChildren().addAll(lblNombre, check);
                hEst.setSpacing(10);
                hEst.setAlignment(Pos.CENTER_LEFT);
               // hEst.setStyle("-fx-border-color: blue;");
                hEst.setPadding(new Insets(5));

                hEst.setOnMouseClicked(ev -> {
                    sucursalSeleccionada = sd;
                    estacionSeleccionada = ed;
                    LSucursal.setText(sd.nombre);
                    LEstacion.setText(ed.nombre);
                    System.out.println("Sucursal: " + sd.nombre + " Estacion: " + ed.nombre);
                    cargarTop();
                });

                vEstaciones.getChildren().add(hEst);
            }

            pane.setContent(vEstaciones);
            acc.getPanes().add(pane);
            
            
            rootEsciones.getChildren().add(acc);
          
        }
    } catch (IOException e) {
        System.out.println("Error al cargar: " + e.getMessage());
    }
}
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    cargarTop();
    cargar();
    cargarGraficos();
    
        LblTUser.sceneProperty().addListener((obs, oldScene, newScene) -> {
        if (newScene != null) {
            LblTUser.styleProperty().bind(
                newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
            LblParametres.styleProperty().bind(
                newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
                }
        });
        
    }    

    @FXML
    private void onActionSinParametros(ActionEvent event) {
        LSucursal.setText("Sin Sucursal");
        LEstacion.setText("Sin Estacion");
          FDesde.setValue(null);
          FHasta.setValue(null);
        cargarTop();
    }

    @FXML
    private void onActionBuscar(ActionEvent event) {
        cargarTop();
    }
    
    
    
    
private void cargarGraficos() {
    try {
        Path archivoTop = Paths.get("Jsons/TopUsers.json");
        if (!Files.exists(archivoTop)) return;
        String json = Files.readString(archivoTop);
        if (json.isBlank()) return;
        Gson gson = new Gson();
        TopUsersData[] topUsers = gson.fromJson(json, TopUsersData[].class);

        List<String> sucursalNames = new ArrayList<>();
        List<Integer> sucursalCounts = new ArrayList<>();
        List<String> estacionNames = new ArrayList<>();
        List<Integer> estacionCounts = new ArrayList<>();

        for (TopUsersData tu : topUsers) {
            int idxSuc = sucursalNames.indexOf(tu.sucursal);
            if (idxSuc == -1) {
                sucursalNames.add(tu.sucursal);
                sucursalCounts.add(tu.cantidad);
            } else {
                sucursalCounts.set(idxSuc, sucursalCounts.get(idxSuc) + tu.cantidad);
            }

            int idxEst = estacionNames.indexOf(tu.estacion);
            if (idxEst == -1) {
                estacionNames.add(tu.estacion);
                estacionCounts.add(tu.cantidad);
            } else {
                estacionCounts.set(idxEst, estacionCounts.get(idxEst) + tu.cantidad);
            }
        }

     GraficLines.setTitle("Atenciones por Sucursal");
     ((CategoryAxis) GraficLines.getXAxis()).setLabel("Sucursal");
     ((NumberAxis) GraficLines.getYAxis()).setLabel("Cantidad");
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (int i = 0; i < sucursalNames.size(); i++) {
    series.getData().add(new XYChart.Data<>(sucursalNames.get(i), sucursalCounts.get(i)));
     }
        GraficLines.getData().clear();
          GraficLines.getData().add(series);
         GraficLines.setLegendVisible(false);

        // Cargar PieChart
        GraficPie.setTitle("Atenciones por Estacion");
        GraficPie.getData().clear();
        for (int i = 0; i < estacionNames.size(); i++) {
        GraficPie.getData().add(new PieChart.Data(estacionNames.get(i), estacionCounts.get(i)));    
       }

    } catch (IOException e) {
        System.out.println("Error al cargar graficos: " + e.getMessage());
    }
}

}

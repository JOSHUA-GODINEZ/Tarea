package cr.ac.una.tarea.controller;

import com.google.gson.Gson;
import cr.ac.una.tarea.model.EstacionData;
import cr.ac.una.tarea.model.SucursalData;
import cr.ac.una.tarea.model.Teme;
import cr.ac.una.tarea.model.TopUsersData;
import cr.ac.una.tarea.util.DataEjecucion;
import cr.ac.una.tarea.util.Propiedades;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.util.Duration;
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
private BarChart<String, Number> GraficLines;
@FXML
private PieChart GraficPie;
    @FXML
    private Label LblTUser;
    @FXML
    private Label LblParametres;
private Boolean temaAnterior = null;
    @FXML
    private HBox LblTutulos;
    @FXML
    private VBox rootUsers;
    @FXML
    private VBox rootserch;
     Propiedades config = new Propiedades();
DataEjecucion data = new DataEjecucion(config);
    
   //Carga los usuarios del archivo
    private void cargarTop(){
     try {
            File archivo = data.getArchivo("TopUsers");
        if (!Files.exists(archivo.toPath())) return;
        String json = Files.readString(archivo.toPath());
        if (json.isBlank()) return;

        Gson gson = new Gson();
        TopUsersData[] TopUsers =  gson.fromJson(json, TopUsersData[].class);
       Arrays.sort(TopUsers, (a, b) -> b.cantidad - a.cantidad);// Los ordena segun la variable cantidad de mayor a menor
       
        rootTop.getChildren().clear();
        // Carga los usuarios segun la fecha del tramite
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
 
 //Carga los usuarios por sucursal y estacion
 if (!LSucursal.getText().equals("Sin Sucursal") && !tu.sucursal.equals(LSucursal.getText())) {
    continue;
}
if (!LEstacion.getText().equals("Sin Estacion") && !tu.estacion.equals(LEstacion.getText())) {
    continue;
}
// crea el usuarios
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

    h.getStyleClass().addAll("item-estacion");
    h.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
   
    rootTop.getChildren().add(h);
       //cambia el tamaño del texto
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
    }
    }
          //Carga las sucursales y estaciones 
    public void cargar() {
    try {
          File archivo = data.getArchivo("BranchesData");
        if (!Files.exists(archivo.toPath())) return;
        String json = Files.readString(archivo.toPath());
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
               hEst.getStyleClass().add("item-estacion");
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
    }
}
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    
    cargar();
    cargarGraficos();
          // Cambia el tamaño del texto
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
        // Carga los usuarios por segundo
        Timeline timeline = new Timeline(
    new KeyFrame(Duration.seconds(1), e -> {
   cargarTop();
      
    })
);
timeline.setCycleCount(Timeline.INDEFINITE);
timeline.play();

  // carga el tema por segundo
      Timeline timeline1 = new Timeline(
    new KeyFrame(Duration.seconds(1), e -> {
        try {
                 File archivo = data.getArchivo("theme");
     if (!archivo.exists()) return;
       String json = Files.readString(archivo.toPath());

            Gson gson = new Gson();
            Teme t = gson.fromJson(json, Teme.class);

            if (temaAnterior == null || !temaAnterior.equals(t.temeDark)) {
                temaAnterior = t.temeDark;

                if (t.temeDark) {
                      rootUsers.getStyleClass().clear();
                    rootUsers.getStyleClass().addAll("mi-rectangulooscuro","mi-Titulososcuros");
                    rootserch.getStyleClass().clear();
                    rootserch.getStyleClass().add("mi-rectangulooscuro");
                    LblTutulos.getStyleClass().clear();
                    LblTutulos.getStyleClass().addAll("mi-Titulososcuros","mi-panel-fondo1");
                   
                } else {
                   rootUsers.getStyleClass().clear();
                    rootUsers.getStyleClass().addAll("mi-rectangulo","mi-Titulos");
                    rootserch.getStyleClass().clear();
                    rootserch.getStyleClass().add("mi-rectangulo");
                    LblTutulos.getStyleClass().clear();
                    LblTutulos.getStyleClass().addAll("mi-Titulos","mi-panel-fondo2");
                }
            }

        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    })
);
timeline1.setCycleCount(Timeline.INDEFINITE);
timeline1.play();
    }    

    @FXML // Desactiva los parametros de busqueda
    private void onActionSinParametros(ActionEvent event) {
        LSucursal.setText("Sin Sucursal");
        LEstacion.setText("Sin Estacion");
          FDesde.setValue(null);
          FHasta.setValue(null);
        cargarTop();
    }

    @FXML  //Busca al poner las fechas
    private void onActionBuscar(ActionEvent event) {
        cargarTop();
    }

private void cargarGraficos() {
    // Carga usuarios
    try {
          File archivo = data.getArchivo("TopUsers");
        if (!Files.exists(archivo.toPath())) return;
        String json = Files.readString(archivo.toPath());
        if (json.isBlank()) return;
        Gson gson = new Gson();
        TopUsersData[] topUsers = gson.fromJson(json, TopUsersData[].class);

        List<String> sucursalNames = new ArrayList<>();
        List<Integer> sucursalCounts = new ArrayList<>();
        List<String> estacionNames = new ArrayList<>();
        List<Integer> estacionCounts = new ArrayList<>();
           // Asigna las sucursales y estaciones
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
//  Agrega datos al codigo de barras
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

        // Cargar datos al grafico de pie
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

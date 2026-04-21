package cr.ac.una.tarea.controller;

import com.google.gson.Gson;
import cr.ac.una.tarea.App;
import cr.ac.una.tarea.model.DataParametres;
import cr.ac.una.tarea.model.Teme;
import cr.ac.una.tarea.util.DataEjecucion;
import cr.ac.una.tarea.util.Propiedades;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class OfficialsController implements Initializable {

    @FXML
    private Tab tab1;
    @FXML
    private Tab tab2;
    @FXML
    private ImageView LogoOfficial;
    @FXML
    private Label LName;
    @FXML
    private HBox rootH;
    Propiedades config = new Propiedades();
DataEjecucion data = new DataEjecucion(config);
private Boolean temaAnterior = null;
    @FXML
    private TabPane roottab;
    //Carga los Parametros Generales 
        private void cargar() {
    try {
       File archivo = data.getArchivo("GeneralData");
        if (!Files.exists(archivo.toPath())) return;
        String json = Files.readString(archivo.toPath());
        if (json.isBlank()) return;
        Gson gson = new Gson();
        DataParametres user = gson.fromJson(json, DataParametres.class);

       LName.setText(user.getName());
       LogoOfficial.setImage(new Image(user.getImageUrl()));
       LogoOfficial.fitWidthProperty().bind(rootH.widthProperty());
         LogoOfficial.fitHeightProperty().bind(rootH.heightProperty());
    } catch (IOException e) {
        System.out.println("Error al cargar: " + e.getMessage());
    }
}
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
     cargar();
     //Cambia el tamaño del texto segun el tamaño de la ventana
          LName.sceneProperty().addListener((obs, oldScene, newScene) -> {
        if (newScene != null) {
            LName.styleProperty().bind(
                newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.12fpx;")
            );
             }
    }); 
          //Carga las vistas en los tabs
         try {
            FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/cr/ac/una/tarea/view/ManagementTokensView.fxml"));
            Parent root1 = loader1.load();
            tab1.setContent(root1);

            FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/cr/ac/una/tarea/view/LoginUsersView.fxml"));
            Parent root2 = loader2.load();
            tab2.setContent(root2);
            

        } catch (IOException e) {
   
        } 
         // cada segundo carga El CSS y los parametros generales
         Timeline timeline = new Timeline(
    new KeyFrame(Duration.seconds(1), e -> {
        try {
               cargar();
             File archivo = data.getArchivo("theme");
if (!archivo.exists()) return;
String json = Files.readString(archivo.toPath());

            Gson gson = new Gson();
            Teme t = gson.fromJson(json, Teme.class);

            if (temaAnterior == null || !temaAnterior.equals(t.temeDark)) {
                temaAnterior = t.temeDark;

                // Cambia los roots
                rootH.getStyleClass().clear();
                roottab.getStyleClass().clear();
               
 if (t.temeDark) {
     
    rootH.getStyleClass().addAll("mi-panel-fondo1","mi-Titulososcuros");
    roottab.getStyleClass().add("tab-pane-oscuro");
 //LName.getStyleClass().add("titulososcu");
} else {

   rootH.getStyleClass().addAll("mi-panel-fondo2","mi-Titulos");
    roottab.getStyleClass().add("tab-pane");
             // LName.getStyleClass().add("titulosclar");
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

    @FXML
    private void onACtionSalir(ActionEvent event) throws IOException {
         Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    
}

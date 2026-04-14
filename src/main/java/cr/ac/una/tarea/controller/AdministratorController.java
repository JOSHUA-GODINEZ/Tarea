package cr.ac.una.tarea.controller;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cr.ac.una.tarea.App;
import cr.ac.una.tarea.model.DataParametres;
import cr.ac.una.tarea.model.Teme;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class AdministratorController implements Initializable {

    @FXML
    private Tab tab1;

    @FXML
    private Tab tab2;
    
    @FXML
    private Tab tab3;
    @FXML
    private Tab tab4;
    @FXML
    private ImageView LogoAdim;
    @FXML
    private Label LName;
    @FXML
    private HBox rootH;
    @FXML
    private TabPane roottab;

    private void cargar() {
    try {
         Path archivo = Paths.get("Jsons/GenenarlData.json");
        if (!Files.exists(archivo)) return;
        String json = Files.readString(archivo);
        if (json.isBlank()) return;

        Gson gson = new Gson();
        DataParametres user = gson.fromJson(json, DataParametres.class);

       LName.setText(user.getName());
       LogoAdim.setImage(new Image(user.getImageUrl()));
               LogoAdim.fitWidthProperty().bind(rootH.widthProperty());
         LogoAdim.fitHeightProperty().bind(rootH.heightProperty());
    } catch (IOException e) {
        System.out.println("Error al cargar: " + e.getMessage());
    }
}
    
      @FXML
    private void onActionSalir(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargar();
        try {
            FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/cr/ac/una/tarea/view/ProcedureView.fxml"));
            Parent root1 = loader1.load();
            tab1.setContent(root1);
            
            FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/cr/ac/una/tarea/view/BranchesStationsView.fxml"));
            Parent root2 = loader2.load();
            tab2.setContent(root2);
            
            FXMLLoader loader3 = new FXMLLoader(getClass().getResource("/cr/ac/una/tarea/view/LoginUsersView.fxml"));
            Parent root3 = loader3.load();
            tab3.setContent(root3);
            
             FXMLLoader loader4 = new FXMLLoader(getClass().getResource("/cr/ac/una/tarea/view/TopUsersView.fxml"));
            Parent root4 = loader4.load();
            tab4.setContent(root4);

            
              LName.sceneProperty().addListener((obs, oldScene, newScene) -> {
        if (newScene != null) {
            LName.styleProperty().bind(
                newScene.widthProperty().multiply(0.03).asString("-fx-font-size: %.2fpx;")
            );
             tab1.styleProperty().bind(
                newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
              tab2.styleProperty().bind(
                newScene.widthProperty().multiply(0.013).asString("-fx-font-size: %.2fpx;")
            );
               tab3.styleProperty().bind(
                newScene.widthProperty().multiply(0.015).asString("-fx-font-size: %.2fpx;")
            );
                tab4.styleProperty().bind(
                newScene.widthProperty().multiply(0.013).asString("-fx-font-size: %.2fpx;")
            );
            
             }  
              });
                       String json = Files.readString(Path.of("Jsons/theme.json"));
            Gson gson = new Gson();
            Teme t = gson.fromJson(json, Teme.class);
    if (t.temeDark) {
                      rootH.getStyleClass().remove("mi-panel-fondo2");
            rootH.getStyleClass().add("mi-panel-fondo1");
            roottab.getStyleClass().add("tab-pane");
              LName.getStyleClass().add("titulososcu");
        } else {
          rootH.getStyleClass().remove("mi-panel-fondo1");
            rootH.getStyleClass().add("mi-panel-fondo2");

            roottab.getStyleClass().remove("tab-pane-oscuro");
            LName.getStyleClass().add("titulosclar");
        }
           
            
            
        } catch (IOException e) {
         
            
        }
    }

    @FXML
    private void onActionOption(ActionEvent event) throws IOException {
        App.setRoot("OfficialsView");
    }

    @FXML
    private void onActionAuxs(ActionEvent event) throws IOException {
        App.setRoot("LoginView");
    }

 @FXML
private void onActionClaro(ActionEvent event) {
   rootH.getStyleClass().clear();
    rootH.getStyleClass().add("mi-panel-fondo2");
   
    LName.getStyleClass().clear();
     LName.getStyleClass().add("titulosclar");
 roottab.getStyleClass().clear(); // siempre debe estar
roottab.getStyleClass().add("tab-pane");
    Teme t = new Teme();
    t.temeDark = false;
    try {
        Files.writeString(Path.of("Jsons/theme.json"), new Gson().toJson(t));
    } catch (IOException e) {
        System.out.println("Error: " + e.getMessage());
    }
}

@FXML
private void onActionOscuro(ActionEvent event) {
       rootH.getStyleClass().clear();
    rootH.getStyleClass().add("mi-panel-fondo1");
    LName.getStyleClass().clear();
    LName.getStyleClass().add("titulososcu");
 roottab.getStyleClass().clear();
roottab.getStyleClass().add("tab-pane-oscuro");
    Teme t = new Teme();
    t.temeDark = true;
    try {
        Files.writeString(Path.of("Jsons/theme.json"), new Gson().toJson(t));
    } catch (IOException e) {
        System.out.println("Error: " + e.getMessage());
    }
}
    
}

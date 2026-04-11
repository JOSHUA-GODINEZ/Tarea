package cr.ac.una.tarea.controller;

import com.google.gson.Gson;
import cr.ac.una.tarea.App;
import cr.ac.una.tarea.model.DataParametres;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
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

        private void cargar() {
    try {
         Path archivo = Paths.get("Jsons/GenenarlData.json");
        if (!Files.exists(archivo)) return;
        String json = Files.readString(archivo);
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
        
         try {
            FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/cr/ac/una/tarea/view/ManagementTokensView.fxml"));
            Parent root1 = loader1.load();
            tab1.setContent(root1);

            FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/cr/ac/una/tarea/view/LoginUsersView.fxml"));
            Parent root2 = loader2.load();
            tab2.setContent(root2);
            

        } catch (IOException e) {
   
        } 
  
    }    

    @FXML
    private void onActionSwitchP(ActionEvent event) throws IOException {
         App.setRoot("AdministratorView");
    }

    @FXML
    private void onACtionSalir(ActionEvent event) throws IOException {
        /* Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();*/
           App.setRoot("AuxiliarProyectionView");
    }
    
}

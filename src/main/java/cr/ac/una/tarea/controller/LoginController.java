package cr.ac.una.tarea.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cr.ac.una.tarea.App;
import cr.ac.una.tarea.model.DataParametres;
import cr.ac.una.tarea.util.Alertas;
import cr.ac.una.tarea.util.ValidadorNumeros;
import javafx.stage.FileChooser;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import java.io.File;
import java.io.IOException;
import javafx.scene.control.Button;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import java.nio.file.Files;
import java.nio.file.Path;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField Tname;
    @FXML
    private ImageView ImageLogo;
    @FXML
    private TextField TPin;
    @FXML
    private TextArea TInfo;
    @FXML
    private Button BNext;
    @FXML
    private Label Title;
    @FXML
    private Label Information;
    @FXML
    private Label Logo;
    @FXML
    private Label PIn;
    
    @FXML
    private VBox root;
    @FXML
    private Label LblMensaje;
    
 private final Path archivo = Path.of("Jsons/GenenarlData.json");
   
@FXML
     private void insetLogo(){
         FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Selecciona una imagen");
 FileChooser.ExtensionFilter filtro =
            new FileChooser.ExtensionFilter(
                    "Imágenes", "*.png", "*.jpg", "*.jpeg"
            );
    fileChooser.getExtensionFilters().add(filtro);

    // Ventana actual
    File archivo1 = fileChooser.showOpenDialog(ImageLogo.getScene().getWindow());

    if (archivo1 != null) {
        Image imagen = new Image(archivo1.toURI().toString());
        ImageLogo.setImage(imagen);
    }
     }
 
    @FXML
    private void onActionChangeView(ActionEvent event) throws IOException {
         App.setRoot("AdministratorView");
    }


    @FXML
    private void onActionSalir(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    @FXML
   private void Save() {
    TPin.getParent().requestFocus();
    Tname.getParent().requestFocus();
    TInfo.getParent().requestFocus();

   if (Tname.getText() != null && !Tname.getText().isBlank() && 
    TInfo.getText() != null && !TInfo.getText().isBlank() && 
    TPin.getText() != null && TPin.getText().length() == 4 && 
    ImageLogo.getImage() != null && !ImageLogo.getImage().getUrl().equals("../resources/cr/ac/una/tarea/resource/Base.png")) {
    BNext.setDisable(false);
    BNext.getStyleClass().add("BNext");
} 
    verifyData();

    try {
        DataParametres user = new DataParametres();
        user.setName(Tname.getText());
        if (TInfo.getText().isBlank()) {
    user.setInfo("");
       TInfo.setText("");
} else {
    user.setInfo(TInfo.getText());
}
        if (TPin.getText() != null && TPin.getText().length() == 4) {
        user.setPin(TPin.getText());
    } else {
        user.setPin("");
        TPin.setText("");
       Alertas.mostrarMensajeError(LblMensaje, "Información Inválida");
    }
        if (ImageLogo.getImage() != null &&
    ImageLogo.getImage().getUrl() != null &&
    !ImageLogo.getImage().getUrl().contains("Base.png")) {

    user.setImageUrl(ImageLogo.getImage().getUrl());
}
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Files.writeString(archivo, gson.toJson(user));
        if (Tname.getText() != null && !Tname.getText().isBlank() &&
        TInfo.getText() != null && !TInfo.getText().isBlank() &&
        TPin.getText() != null && TPin.getText().length() == 4 &&
        ImageLogo.getImage() != null &&
        ImageLogo.getImage().getUrl() != null &&
        !ImageLogo.getImage().getUrl().contains("Base.png")) {
       Alertas.mostrarMensajeCorrecto(LblMensaje, "Información Correcta");
    }else{Alertas.mostrarMensajeError(LblMensaje, "Información Inválida");}
    } catch (IOException e) {
        System.out.println("Error al guardar: " + e.getMessage());
    }
}
    private void cargar() {
    try {
        if (!Files.exists(archivo)) return;
        String json = Files.readString(archivo);
        if (json.isBlank()) return;

        Gson gson = new Gson();
        DataParametres user = gson.fromJson(json, DataParametres.class);

        Tname.setText(user.getName());
        TInfo.setText(user.getInfo());
        TPin.setText(user.getPin());

        if (user.getImageUrl() != null && !user.getImageUrl().isBlank()) {
            ImageLogo.setImage(new Image(user.getImageUrl()));
        }
    } catch (IOException e) {
        System.out.println("Error al cargar: " + e.getMessage());
    }
}
    private void verifyData() {
    if (Tname.getText() != null && !Tname.getText().isBlank() &&
        TInfo.getText() != null && !TInfo.getText().isBlank() &&
        TPin.getText() != null && TPin.getText().length() == 4 &&
        ImageLogo.getImage() != null &&
        ImageLogo.getImage().getUrl() != null &&
        !ImageLogo.getImage().getUrl().contains("Base.png")) {
        BNext.setDisable(false);
        BNext.getStyleClass().add("BNext");
       
    } else {
        BNext.setDisable(true);
        BNext.getStyleClass().add("BNext");
        
    }
}
    
     public String FullParameters() throws IOException {
    if (!Files.exists(archivo)) return "LoginView";
    String json = Files.readString(archivo);
    if (json.isBlank()) return "LoginView";

    Gson gson = new Gson();
    DataParametres user = gson.fromJson(json, DataParametres.class);

   if (user.getName() == null || user.getName().isBlank() || 
    user.getInfo() == null || user.getInfo().isBlank() || 
    user.getPin() == null || user.getPin().isBlank() ||
    user.getImageUrl() == null || 
    user.getImageUrl().contains("Base.png")) {
        return "LoginView";
   }else {
        return "AdministratorView";
    }
}
    
        public void initialize() throws IOException {
        cargar();
        verifyData();
      
     ValidadorNumeros.soloNumeros(TPin);
        
        
        
          Title.sceneProperty().addListener((obs, oldScene, newScene) -> {
        if (newScene != null) {
            Title.styleProperty().bind(
                newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
            Information.styleProperty().bind(
                newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
            Logo.styleProperty().bind(
                newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
            PIn.styleProperty().bind(
                newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
                 Tname.styleProperty().bind(
            newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
        );
        TInfo.styleProperty().bind(
            newScene.widthProperty().multiply(0.014).asString("-fx-font-size: %.2fpx;")
        );
         TPin.styleProperty().bind(
            newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
        );
        }
    });
}
}

package cr.ac.una.tarea.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cr.ac.una.tarea.App;
import cr.ac.una.tarea.model.DataParametres;
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
import javafx.animation.PauseTransition;
import javafx.util.Duration;
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

     private final Path archivo = Path.of("Jsons/GenenarlData.json");
    @FXML
    private VBox root;
    @FXML
    private Label LblMensaje;
    
@FXML
   private void Save() {
    TPin.getParent().requestFocus();
    Tname.getParent().requestFocus();
    TInfo.getParent().requestFocus();

   if (Tname.getText() != null && !Tname.getText().isBlank() && 
    TInfo.getText() != null && !TInfo.getText().isBlank() && 
    TPin.getText() != null && TPin.getText().length() == 4 && 
    ImageLogo.getImage() != null && !ImageLogo.getImage().getUrl().equals("../resources/cr/ac/una/tarea/resource/Inicial.png")) {
    BNext.setOpacity(1.0);
    BNext.setDisable(false);
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
        mostrarMensaje("Informacion Invalida");
    }
        if (ImageLogo.getImage() != null &&
    ImageLogo.getImage().getUrl() != null &&
    !ImageLogo.getImage().getUrl().contains("Inicial.png")) {

    user.setImageUrl(ImageLogo.getImage().getUrl());
}
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Files.writeString(archivo, gson.toJson(user));
    } catch (IOException e) {
        System.out.println("Error al guardar: " + e.getMessage());
    }
}
   
@FXML
     private void Insert1(){
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
    

     
    public void initialize() throws IOException {
        cargar();
        verifyData();
      
        TPin.textProperty().addListener((obs, oldVal, newVal) -> {
    if (!newVal.matches("\\d*")) {
        TPin.setText(newVal.replaceAll("[^\\d]", ""));
    }
});
        
        
        
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
   
  /* root.sceneProperty().addListener((obs, oldScene, newScene) -> {
        if (newScene != null) {
            newScene.widthProperty().addListener((o, oldVal, newVal) -> {
                double spacing = newVal.doubleValue() / 40;

                if (spacing < 20) spacing = 20;
                if (spacing > 50) spacing = 50;

                root.setSpacing(spacing);

            });
        }
    });
    */}
    
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
        !ImageLogo.getImage().getUrl().contains("Inicial.png")) {

        BNext.setOpacity(1.0);
        BNext.setDisable(false);
    } else {
        BNext.setOpacity(0.2);
        BNext.setDisable(true);
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
    user.getImageUrl().contains("Inicio.png")) {
        return "LoginView";
   }else {
        return "AdministratorView";
    }
}

    @FXML
    private void onActionChangeView(ActionEvent event) throws IOException {
         App.setRoot("AdministratorView");
    }
    
    
    private void mostrarMensaje(String mensaje) {
    LblMensaje.setText(mensaje);
    LblMensaje.setVisible(true);

    PauseTransition pause = new PauseTransition(Duration.seconds(2));
    pause.setOnFinished(e -> LblMensaje.setVisible(false));
    pause.play();
}
    
}

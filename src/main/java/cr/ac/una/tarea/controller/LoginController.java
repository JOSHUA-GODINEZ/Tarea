package cr.ac.una.tarea.controller;

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

public class LoginController {
private String Name;
private String Pin;
private String Info;
private Boolean Image=false;

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
    private VBox vbox;
    
     private final Path archivo = Path.of("GenenarlData.json");
    
@FXML
    private void Save(){
        TPin.getParent().requestFocus();
         Tname.getParent().requestFocus();
        TInfo.getParent().requestFocus();

     
      if(!Tname.getText().isEmpty() && !TInfo.getText().isEmpty()&& !TPin.getText().isEmpty()&& !ImageLogo.getImage().getUrl().equals("file:/Joshua/Tarea1-Logos/Inicial.png") ){
      BNext.setOpacity(1.0);
      BNext.setDisable(false);
      }
       verificarDatos();
     
     try {
        DataParametres user = new DataParametres();
        user.setName(Tname.getText());
        user.setInfo(TInfo.getText());
        user.setPin(TPin.getText());
        if(ImageLogo.getImage().getUrl().equals("file:/Joshua/Tarea1-Logos/Inicial.png")==false){
        user.setImageUrl(ImageLogo.getImage().getUrl());}

        String json = "{\n" +
                      "  \"name\": \"" + user.getName() + "\"\n" +
                "  \"Informacion\": \"" + user.getInfo() + "\"\n" +
                "  \"Pin\": \"" + user.getPin() + "\"\n" +
                 "  \"image\": \"" + user.getImageUrl() + "\"\n" +
                      "}";

        Files.writeString(archivo, json);

    } catch (IOException e) {

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
        //Image=true;
    }
     }
    

     
    public void initialize() throws IOException {
        cargar();
        verificarDatos();
      
          Title.sceneProperty().addListener((obs, oldScene, newScene) -> {
        if (newScene != null) {
            Title.styleProperty().bind(
                newScene.widthProperty().multiply(0.03).asString("-fx-font-size: %.2fpx;")
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
        }
    });
   
   vbox.sceneProperty().addListener((obs, oldScene, newScene) -> {
        if (newScene != null) {
            newScene.widthProperty().addListener((o, oldVal, newVal) -> {
                double spacing = newVal.doubleValue() / 40;

                if (spacing < 20) spacing = 20;
                if (spacing > 50) spacing = 50;

                vbox.setSpacing(spacing);

            });
        }
    });
    }
    
    private void cargar() {
        try {
            if (!Files.exists(archivo)) return;

            String json = Files.readString(archivo);

            String name = json.split("\"name\"\\s*:\\s*\"")[1].split("\"")[0];
             String info = json.split("\"Informacion\"\\s*:\\s*\"")[1].split("\"")[0];
              String pin = json.split("\"Pin\"\\s*:\\s*\"")[1].split("\"")[0];
               String imageUrl = json.split("\"image\"\\s*:\\s*\"")[1].split("\"")[0];
              
            Tname.setText(name);
            TInfo.setText(info);
            TPin.setText(pin);
            
             if (imageUrl != null && !imageUrl.isBlank()) {
            ImageLogo.setImage(new Image(imageUrl));
        }
          
        } catch (IOException e) {
    
        } 
    }

private void verificarDatos() {
    if (!Tname.getText().isEmpty() && !TInfo.getText().isEmpty() && !TPin.getText().isEmpty()&& !ImageLogo.getImage().getUrl().equals("file:/Joshua/Tarea1-Logos/Inicial.png")) {
        BNext.setOpacity(1.0);
        BNext.setDisable(false);
       
    } else {
        BNext.setOpacity(0.5);
        BNext.setDisable(true);
    }
}
       
       
       public String verificarDatos1() throws IOException{
            String json = Files.readString(archivo);

            String name = json.split("\"name\"\\s*:\\s*\"")[1].split("\"")[0];
             String info = json.split("\"Informacion\"\\s*:\\s*\"")[1].split("\"")[0];
              String pin = json.split("\"Pin\"\\s*:\\s*\"")[1].split("\"")[0];
               String imageUrl = json.split("\"image\"\\s*:\\s*\"")[1].split("\"")[0];
              
         if(!name.equals("")){return "AdministratorView";}
         else{return "LoginView";}
       
}

    @FXML
    private void onActionChangeView(ActionEvent event) throws IOException {
         App.setRoot("AdministratorView");
    }
}

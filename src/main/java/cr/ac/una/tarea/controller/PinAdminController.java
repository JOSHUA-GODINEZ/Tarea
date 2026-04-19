package cr.ac.una.tarea.controller;
import com.google.gson.Gson;
import cr.ac.una.tarea.model.DataParametres;
import cr.ac.una.tarea.model.KioscoData;
import cr.ac.una.tarea.util.Alertas;
import cr.ac.una.tarea.util.DataEjecucion;
import cr.ac.una.tarea.util.Propiedades;
import cr.ac.una.tarea.util.ValidadorNumeros;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class PinAdminController implements Initializable {
  KioscoData kiosco;
 
   Propiedades config = new Propiedades();
DataEjecucion data = new DataEjecucion(config);
    @FXML
    private TextField pin;
    @FXML
    private Label mensaje;
  private String PinAdmin;
    @FXML
    private Label LName;
    @FXML
    private ImageView Logo;
    @FXML
    private HBox root;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ValidadorNumeros.soloNumeros(pin); 
        cargar();
       
    }    

     @FXML
    private void onAction7(ActionEvent event) {
        pin.appendText("7");
    }

    @FXML
    private void onAction8(ActionEvent event) {
        pin.appendText("8");
    }

    @FXML
    private void onAction9(ActionEvent event) {
        pin.appendText("9");
    }

    @FXML
    private void onAction4(ActionEvent event) {
        pin.appendText("4");
    }

    @FXML
    private void onAction5(ActionEvent event) {
        pin.appendText("5");
    }

    @FXML
    private void onAction6(ActionEvent event) {
        pin.appendText("6");
    }

    @FXML
    private void onAction3(ActionEvent event) {
        pin.appendText("3");
    }

    @FXML
    private void onAction2(ActionEvent event) {
        pin.appendText("2");
    }

    @FXML
    private void onAction1(ActionEvent event) {
         pin.appendText("1");
    }

    @FXML
    private void onAction0(ActionEvent event) {
        pin.appendText("0");
    }

    @FXML
    private void onActionEliminar(ActionEvent event) {
            String txt = pin.getText();
    if (!txt.isEmpty()) {
        pin.setText(txt.substring(0, txt.length() - 1));
    }
    }
     private void cargar() {
    try {
       File archivo = data.getArchivo("GeneralData");
        if (!Files.exists(archivo.toPath())) return;
        String json = Files.readString(archivo.toPath());
        if (json.isBlank()) return;

        Gson gson = new Gson();
        DataParametres user = gson.fromJson(json, DataParametres.class);

       LName.setText(user.getName());
       PinAdmin = user.getPin();
       Logo.setImage(new javafx.scene.image.Image(user.getImageUrl()));
               Logo.fitWidthProperty().bind(root.widthProperty());
         Logo.fitHeightProperty().bind(root.heightProperty());
    } catch (IOException e) {
        System.out.println("Error al cargar: " + e.getMessage());
    }
}
    @FXML
    private void onActionAvanzar(ActionEvent event) {
        mensaje.getStyleClass().clear();
        if(pin.getText().isBlank()) Alertas.mostrarMensajeError(mensaje, "Pin Invalido");
         
        if(pin.getText().equals(PinAdmin)){
    kiosco.setPreference(true);
    Alertas.mostrarMensajeCorrecto(mensaje, "Pin correcto");
    pin.setText("");
    }else{ Alertas.mostrarMensajeError(mensaje, "Pin Invalido");}
    }

    @FXML
    private void onActionCancelar(ActionEvent event) {
          Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
     public void setKiosco(KioscoData kiosco) {
        this.kiosco = kiosco;
    }
        
}

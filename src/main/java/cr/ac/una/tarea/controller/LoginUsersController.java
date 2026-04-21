package cr.ac.una.tarea.controller;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cr.ac.una.tarea.model.Teme;
import cr.ac.una.tarea.model.UsuarioData;
import cr.ac.una.tarea.util.Alertas;
import cr.ac.una.tarea.util.CamaraUtil;
import cr.ac.una.tarea.util.DataEjecucion;
import cr.ac.una.tarea.util.Propiedades;
import cr.ac.una.tarea.util.ValidadorNumeros;
import java.io.IOException;
import java.nio.file.Files;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class LoginUsersController implements Initializable {
    @FXML
    private VBox rootUsers;

    @FXML
    private TextField searchField;
    
    private List<VBox> usersData= new ArrayList<>();
    private VBox selectedUser = null;
    @FXML
    private Label LblMensaje;
        private Boolean temaAnterior = null;
    @FXML
    private HBox rootMensaje;
        Propiedades config = new Propiedades();
DataEjecucion data = new DataEjecucion(config);

    @FXML
    private void onActionAdd(ActionEvent event) {
        // Crea los usuarios
HBox usersInfo = new HBox();       
TextField name = new TextField();
TextField id = new TextField();
TextField numberPhone = new TextField();
DatePicker dateOfBirth = new DatePicker();
ImageView userFoto = new ImageView("file:C:/Joshua/Tarea/src/main/resources/cr/ac/una/tarea/resource/Base.png");
VBox contain = new VBox();
Button foto = new Button();
Button image = new Button();
VBox upLine = new VBox();
HBox titles = new HBox();
Label lblName = new Label("Nombre ");
Label lblId = new Label("Cedula   ");
Label lblPhone = new Label("Telefono   ");
Label lblDate = new Label("Fecha   ");
Label lblPhoto = new Label("Foto");

    ValidadorNumeros.soloNumeros(id);
      ValidadorNumeros.soloNumeros(numberPhone);

///////////////// LABELS CSS
lblName.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
lblId.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
lblPhone.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
lblDate.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
lblPhoto.setMaxSize(150, Double.MAX_VALUE);
HBox.setHgrow(lblName, Priority.ALWAYS);
HBox.setHgrow(lblId, Priority.ALWAYS);
HBox.setHgrow(lblPhone, Priority.ALWAYS);
HBox.setHgrow(lblDate, Priority.ALWAYS);
HBox.setHgrow(lblPhoto, Priority.ALWAYS);

lblName.getStyleClass().add("label-procedimiento");
lblId.getStyleClass().add("label-procedimiento");
lblPhone.getStyleClass().add("label-procedimiento");
lblDate.getStyleClass().add("label-procedimiento");
lblPhoto.getStyleClass().add("label-procedimiento");

///////////////// CSS
userFoto.setFitHeight(70);
userFoto.setFitWidth(70);
upLine.getStyleClass().addAll("mi-rectangulo","mi-boton2");
upLine.setMaxSize(Double.MAX_VALUE, 70);
//titles.setPadding(new Insets(0, 20, 0, 20));
//titles.setAlignment(Pos.CENTER_LEFT);
titles.getStyleClass().addAll("mi-Titulos","procedimiento");
titles.setMaxWidth(Double.MAX_VALUE);
HBox.setHgrow(titles, Priority.ALWAYS);
VBox.setVgrow(upLine, Priority.ALWAYS);

//usersInfo.setAlignment(Pos.CENTER);
//usersInfo.setSpacing(40);
//usersInfo.setPadding(new Insets(0, 0, 0, 20));
usersInfo.getStyleClass().add("procedimiento-fila");
usersInfo.setMaxHeight(Double.MAX_VALUE);
usersInfo.setMinHeight(70);
usersInfo.setMaxWidth(Double.MAX_VALUE);

name.setMaxWidth(Double.MAX_VALUE);
id.setMaxWidth(Double.MAX_VALUE);
numberPhone.setMaxWidth(Double.MAX_VALUE);
dateOfBirth.setMaxWidth(Double.MAX_VALUE);
foto.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
image.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
image.getStyleClass().add("mi-botonImage");
foto.getStyleClass().add("mi-botonFoto");
HBox.setHgrow(name, Priority.ALWAYS);
HBox.setHgrow(id, Priority.ALWAYS);
HBox.setHgrow(numberPhone, Priority.ALWAYS);
HBox.setHgrow(dateOfBirth, Priority.ALWAYS);
HBox.setHgrow(userFoto, Priority.ALWAYS);
VBox.setVgrow(foto, Priority.ALWAYS);
VBox.setVgrow(image, Priority.ALWAYS);
///////////////// STRUCTURE
titles.getChildren().addAll(lblName, lblId, lblPhone, lblDate, lblPhoto);
usersInfo.getChildren().addAll(name, id, numberPhone, dateOfBirth, userFoto);

contain.getStyleClass().add("label-procedimiento");
contain.getChildren().addAll(foto, image);
contain.setPadding(new Insets(0, 0, 0, -40));
usersInfo.getChildren().add(contain);

upLine.getChildren().addAll(titles, usersInfo);

usersData.add(upLine);
rootUsers.getChildren().add(upLine);

upLine.setOnMouseClicked(e -> {
    if (selectedUser != null) {
        selectedUser.getStyleClass().remove("seleccionado");
    }
    selectedUser = upLine;
    upLine.getStyleClass().add("seleccionado");
});
// Cambia el tamaño del texto
    Scene scene = rootUsers.getScene();  
        if (scene != null) {
            name.styleProperty().bind(
                scene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
            id.styleProperty().bind(
                scene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
            numberPhone.styleProperty().bind(
                scene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
            dateOfBirth.styleProperty().bind(
                scene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
             lblName.styleProperty().bind(
                scene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
              lblId.styleProperty().bind(
                scene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
               lblPhone.styleProperty().bind(
                scene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
                lblDate.styleProperty().bind(
                scene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
                 lblPhoto.styleProperty().bind(
                scene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
        } else {
            rootUsers.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) {
                      name.styleProperty().bind(
                newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
            id.styleProperty().bind(
                newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
            numberPhone.styleProperty().bind(
                newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
            dateOfBirth.styleProperty().bind(
                newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );  
            
     lblName.styleProperty().bind(
                newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
              lblId.styleProperty().bind(
                newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
              lblPhone.styleProperty().bind(
                newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
                lblDate.styleProperty().bind(
                newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
                 lblPhoto.styleProperty().bind(
                newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
        }
            });
        }
// Agrega la imagen
    image.setOnMouseClicked(e -> {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Selecciona una imagen");
    FileChooser.ExtensionFilter filter =
            new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg");
    fileChooser.getExtensionFilters().add(filter);
    File selectedFile = fileChooser.showOpenDialog(userFoto.getScene().getWindow());
    if (selectedFile != null) {
        Image selectedImage = new Image(selectedFile.toURI().toString());
        userFoto.setImage(selectedImage);
    }
});
// Agrega la foto
foto.setOnMouseClicked(e -> CamaraUtil.tomarFoto(userFoto));
}




    @FXML
    private void onActionDelete(ActionEvent event) {
    if (selectedUser != null) {
        rootUsers.getChildren().remove(selectedUser);
        usersData.remove(selectedUser);
            selectedUser.getStyleClass().remove("seleccionado");
        selectedUser = null;
    }
}

    @FXML
    private void onActionEdit(ActionEvent event) {
    if (selectedUser != null) {
         HBox information = (HBox) selectedUser.getChildren().get(1);
        TextField name = (TextField) information.getChildren().get(0);
        TextField id = (TextField) information.getChildren().get(1);
        TextField numberPhone = (TextField) information.getChildren().get(2);
        DatePicker dateOfBirth = (DatePicker) information.getChildren().get(3);
        ImageView userPhoto = (ImageView) information.getChildren().get(4);
          VBox contain = (VBox) information.getChildren().get(5);
        // Obtener los botones - ajusta los índices según tu estructura
        Button btnImage = (Button) contain.getChildren().get(0); // índice del botón image
        Button btnFot = (Button) contain.getChildren().get(1);  
        
        name.setEditable(true);
        id.setEditable(true);
        numberPhone.setEditable(true);
        dateOfBirth.setEditable(true);
        userPhoto.setDisable(false);
          btnImage.setDisable(false);
            btnFot.setDisable(false);
        information.getStyleClass().add("procedimiento-editando");
        selectedUser.getStyleClass().remove("seleccionado");
        selectedUser = null;
    }
}

@FXML
private void onActionSave(ActionEvent event) {
    for (int i = usersData.size() - 1; i >= 0; i--) {
        VBox v = usersData.get(i);
        HBox row = (HBox) v.getChildren().get(1);
        TextField name = (TextField) row.getChildren().get(0);
        TextField id = (TextField) row.getChildren().get(1);
        TextField numberPhone = (TextField) row.getChildren().get(2);
        DatePicker dateOfBirth = (DatePicker) row.getChildren().get(3);
        ImageView userPhoto = (ImageView) row.getChildren().get(4);
           VBox contain = (VBox) row.getChildren().get(5);
        // Obtener los botones - ajusta los índices según tu estructura
        Button btnImage = (Button) contain.getChildren().get(0); // índice del botón image
        Button btnFot = (Button) contain.getChildren().get(1);   // índice del botón fot

        boolean hasImage = userPhoto.getImage() != null &&
                userPhoto.getImage().getUrl() != null &&
                userPhoto.getImage().getUrl().contains("Base.png");

        if (!name.getText().isBlank() && !id.getText().isBlank()
                && dateOfBirth.getValue() != null && !hasImage) {
            name.setEditable(false);
            id.setEditable(false);
            numberPhone.setEditable(false);
            dateOfBirth.setEditable(false);
            userPhoto.setDisable(true);

            // Deshabilita los botones igual que los demas campos
            btnImage.setDisable(true);
            btnFot.setDisable(true);

            row.getStyleClass().remove("procedimiento-editando");
            row.getStyleClass().add("procedimiento-guardado");
            Alertas.mostrarMensajeCorrecto(LblMensaje, "Información Correcta");
        } else {
            Alertas.mostrarMensajeError(LblMensaje, "Información Inválida");
            rootUsers.getChildren().remove(v);
            usersData.remove(i);
        }
    }

    if( selectedUser!=null){
    selectedUser.getStyleClass().remove("seleccionado");
    selectedUser = null;}
// guarda en el json
    try {
        List<UsuarioData> userList = new ArrayList<>();

        for (VBox v : usersData) {
            HBox row = (HBox) v.getChildren().get(1);
            TextField name = (TextField) row.getChildren().get(0);
            TextField id = (TextField) row.getChildren().get(1);
            TextField numberPhone = (TextField) row.getChildren().get(2);
            DatePicker dateOfBirth = (DatePicker) row.getChildren().get(3);
            ImageView userPhoto = (ImageView) row.getChildren().get(4);

            UsuarioData ud = new UsuarioData();
            ud.nombre = name.getText();
            ud.cedula = id.getText();
            ud.numero = numberPhone.getText();
            ud.fecha = dateOfBirth.getValue() != null ? dateOfBirth.getValue().toString() : "";

            String imageUrl = "";
            if (userPhoto.getUserData() != null) {
                imageUrl = userPhoto.getUserData().toString();
            } else if (userPhoto.getImage() != null && userPhoto.getImage().getUrl() != null) {
                imageUrl = userPhoto.getImage().getUrl();
            }
            ud.imagen = imageUrl;
            userList.add(ud);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
         Files.writeString(data.getArchivo("usuarios").toPath(),gson.toJson(userList));
      
       
        System.out.println("Datos guardados");

    } catch (IOException ex) {
        System.out.println("Error al guardar: " + ex.getMessage());
    }
}

private void search(String text) {
    for (int i = 0; i < usersData.size(); i++) {
        VBox v = usersData.get(i);
        HBox row = (HBox) v.getChildren().get(1);
        TextField name = (TextField) row.getChildren().get(0);
        TextField id = (TextField) row.getChildren().get(1);

        if (name.getText().toLowerCase().contains(text.toLowerCase()) ||
            id.getText().toLowerCase().contains(text.toLowerCase())) {
            row.setVisible(true);
            row.setManaged(true);
        } else {
            row.setVisible(false);
            row.setManaged(false);
        }
    }
}
//Carga usuarios del archivo 
private void loadUsers() {
    rootUsers.getChildren().clear();
    try {
         File archivo = data.getArchivo("usuarios");
        if (!Files.exists(archivo.toPath())) return;
        String json = Files.readString(archivo.toPath());
        if (json.isBlank()) return;
        Gson gson = new Gson();
        UsuarioData[] users = gson.fromJson(json, UsuarioData[].class);

        for (UsuarioData ud : users) {
            onActionAdd(null);
            VBox v = usersData.get(usersData.size() - 1);
            HBox row = (HBox)  v.getChildren().get(1);

            TextField name = (TextField) row.getChildren().get(0);
            TextField id = (TextField) row.getChildren().get(1);
            TextField numberPhone = (TextField) row.getChildren().get(2);
            DatePicker dateOfBirth = (DatePicker) row.getChildren().get(3);
            ImageView userPhoto = (ImageView) row.getChildren().get(4);
         VBox contain = (VBox) row.getChildren().get(5);
        
        Button btnImage = (Button) contain.getChildren().get(0); 
        Button btnFot = (Button) contain.getChildren().get(1);    
            
            name.setText(ud.nombre);
            id.setText(ud.cedula);
            numberPhone.setText(ud.numero);
            dateOfBirth.setValue(ud.fecha != null && !ud.fecha.isBlank()
                    ? java.time.LocalDate.parse(ud.fecha)
                    : null);

            if (ud.imagen != null && !ud.imagen.isBlank()) {
                //Guarda la imagen con un file
                try {
                    String imagePath = ud.imagen;
                    if (imagePath.startsWith("file:/") && !imagePath.startsWith("file:///")) {
                        imagePath = imagePath.replace("file:/", "file:///");
                    }
                    Image loadedImage = new Image(imagePath);
                    userPhoto.setImage(loadedImage);
                    userPhoto.setUserData(imagePath);
                } catch (Exception ex) {
                    System.out.println("Error al cargar imagen: " + ex.getMessage());
                }
            }
            name.setEditable(false);
            id.setEditable(false);
            numberPhone.setEditable(false);
            dateOfBirth.setEditable(false);
            userPhoto.setDisable(true);
             btnImage.setDisable(true);
            btnFot.setDisable(true);
             row.getStyleClass().remove("procedimiento-editando");
            row.getStyleClass().add("procedimiento-guardado");
        }
    } catch (IOException e) {

    }
}
@Override
public void initialize(URL url, ResourceBundle rb) {
    loadUsers();
    searchField.textProperty().addListener((obs, oldVal, newVal) -> {
        search(newVal);
    });
 
    // Carga el tema cada segundo
Timeline timeline = new Timeline(
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
                    rootUsers.getStyleClass().remove("mi-panel-fondo2");
                    rootUsers.getStyleClass().add("mi-panel-fondo1");
                    rootMensaje.getStyleClass().remove("mi-panel-fondo2");
                    rootMensaje.getStyleClass().add("mi-panel-fondo1");
                } else {
                    rootUsers.getStyleClass().remove("mi-panel-fondo1");
                    rootUsers.getStyleClass().add("mi-panel-fondo2");
                      rootMensaje.getStyleClass().remove("mi-panel-fondo1");
                    rootMensaje.getStyleClass().add("mi-panel-fondo2");
                }

                for (VBox upLine : usersData) {
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
           
        }
    })
);
timeline.setCycleCount(Timeline.INDEFINITE);
timeline.play();



         // Cambia el tamaño del texto
    LblMensaje.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) {
                    LblMensaje.styleProperty().bind(
                        newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
                    );
                }
     });
}
}
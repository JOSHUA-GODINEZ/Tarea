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
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import com.github.sarxos.webcam.Webcam;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cr.ac.una.tarea.model.UsuarioData;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
public class LoginUsersController implements Initializable {
    @FXML
    private VBox rootUsers;

    @FXML
    private TextField searchField;
    
    private List<HBox> usersData= new ArrayList<>();
    private HBox selectedUser = null;
    @FXML
    private void onActionAdd(ActionEvent event) {
  HBox usersInfo = new HBox();       
TextField name = new TextField();
TextField id = new TextField();
TextField numberPhone = new TextField();
DatePicker dateOfBirth = new DatePicker();
ImageView userFoto = new ImageView("file:/C:/Joshua/Tarea1-Logos/Inicial.png");
VBox contain = new VBox();
Button foto = new Button("Foto");
Button image = new Button("Imagen");

userFoto.setFitHeight(70);
userFoto.setFitWidth(70);

usersInfo.setAlignment(Pos.CENTER_LEFT);

name.setMaxWidth(Double.MAX_VALUE);
id.setMaxWidth(Double.MAX_VALUE);
numberPhone.setMaxWidth(Double.MAX_VALUE);
dateOfBirth.setMaxWidth(Double.MAX_VALUE);
foto.setMaxWidth(Double.MAX_VALUE);
image.setMaxWidth(Double.MAX_VALUE);

HBox.setHgrow(name, Priority.ALWAYS);
HBox.setHgrow(id, Priority.ALWAYS);
HBox.setHgrow(numberPhone, Priority.ALWAYS);
HBox.setHgrow(dateOfBirth, Priority.ALWAYS);
HBox.setHgrow(userFoto, Priority.ALWAYS);
VBox.setVgrow(usersInfo, Priority.ALWAYS);

usersInfo.setPadding(new Insets(0, 0, 0, 20));
usersInfo.setSpacing(40);
usersInfo.setMaxHeight(Double.MAX_VALUE);
usersInfo.setMinHeight(70);

usersInfo.getChildren().add(name);
usersInfo.getChildren().add(id);
usersInfo.getChildren().add(numberPhone);
usersInfo.getChildren().add(dateOfBirth);
usersInfo.getChildren().add(userFoto);

contain.setAlignment(Pos.CENTER);
contain.getChildren().add(foto);
contain.getChildren().add(image);
contain.setPadding(new Insets(0, 0, 0, -40));
usersInfo.getChildren().add(contain);

usersInfo.setStyle("-fx-border-color: blue;");
usersInfo.setAlignment(Pos.CENTER);

usersData.add(usersInfo);
rootUsers.setSpacing(20);
rootUsers.getChildren().add(usersInfo);

usersInfo.setOnMouseClicked(e -> {
    selectedUser = usersInfo;
}); 

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

foto.setOnMouseClicked(e -> {
    Webcam webcam = Webcam.getDefault();
    if (webcam == null) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
            javafx.scene.control.Alert.AlertType.ERROR
        );
        alert.setContentText("No se encontró cámara.");
        alert.showAndWait();
        return;
    }
    webcam.open();

    ImageView preview = new ImageView();
    preview.setFitWidth(480);
    preview.setFitHeight(360);

    Button captureBtn = new Button("Capturar");
    Button cancelBtn = new Button("Cancelar");

    HBox buttonsBox = new HBox(12, captureBtn, cancelBtn);
    buttonsBox.setAlignment(Pos.CENTER);
    buttonsBox.setPadding(new Insets(10));

    BorderPane cameraRoot = new BorderPane(preview);
    cameraRoot.setBottom(buttonsBox);

    Stage cameraStage = new Stage();
    cameraStage.initModality(Modality.APPLICATION_MODAL);
    cameraStage.setTitle("Tomar foto");
    cameraStage.setScene(new Scene(cameraRoot, 500, 430));

    Thread streamThread = new Thread(() -> {
        while (cameraStage.isShowing()) {
            try {
                java.awt.image.BufferedImage frame = webcam.getImage();
                if (frame != null) {
                    javafx.scene.image.Image fxImage = SwingFXUtils.toFXImage(frame, null);
                    javafx.application.Platform.runLater(() -> preview.setImage(fxImage));
                }
                Thread.sleep(66);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    });
    streamThread.setDaemon(true);

    captureBtn.setOnAction(ev -> {
        java.awt.image.BufferedImage capturedFrame = webcam.getImage();
        if (capturedFrame != null) {
            javafx.scene.image.Image fxImage = SwingFXUtils.toFXImage(capturedFrame, null);
            String photoPath = saveImageFromCamera(fxImage);
            userFoto.setImage(fxImage);
            userFoto.setUserData(photoPath);
        }
        webcam.close();
        streamThread.interrupt();
        cameraStage.close();
    });

    cancelBtn.setOnAction(ev -> {
        webcam.close();
        streamThread.interrupt();
        cameraStage.close();
    });

    cameraStage.setOnCloseRequest(ev -> {
        webcam.close();
        streamThread.interrupt();
    });

    cameraStage.show();
    streamThread.start();
});
    }



private String saveImageFromCamera(javafx.scene.image.Image image) {
    try {
        String folderPath = System.getProperty("user.dir") + "/fotos/";
        java.io.File dir = new java.io.File(folderPath);
        if (!dir.exists()) dir.mkdirs();

        String fileName = folderPath + "foto_" +
            java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))
            + ".png";

        java.io.File outputFile = new java.io.File(fileName);
        javax.imageio.ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", outputFile);
        return outputFile.toURI().toString();

    } catch (IOException ex) {
        return "";
    }
}




    @FXML
    private void onActionDelete(ActionEvent event) {
    if (selectedUser != null) {
        rootUsers.getChildren().remove(selectedUser);
        usersData.remove(selectedUser);
        selectedUser = null;
    }
}

    @FXML
    private void onActionEdit(ActionEvent event) {
    if (selectedUser != null) {
        TextField name = (TextField) selectedUser.getChildren().get(0);
        TextField id = (TextField) selectedUser.getChildren().get(1);
        TextField numberPhone = (TextField) selectedUser.getChildren().get(2);
        DatePicker dateOfBirth = (DatePicker) selectedUser.getChildren().get(3);
        ImageView userPhoto = (ImageView) selectedUser.getChildren().get(4);

        name.setDisable(false);
        id.setDisable(false);
        numberPhone.setDisable(false);
        dateOfBirth.setDisable(false);
        userPhoto.setDisable(false);

        selectedUser = null;
    }
}

@FXML
private void onActionSave(ActionEvent event) {
    for (int i = usersData.size() - 1; i >= 0; i--) {
        HBox row = usersData.get(i);
        TextField name = (TextField) row.getChildren().get(0);
        TextField id = (TextField) row.getChildren().get(1);
        TextField numberPhone = (TextField) row.getChildren().get(2);
        DatePicker dateOfBirth = (DatePicker) row.getChildren().get(3);
        ImageView userPhoto = (ImageView) row.getChildren().get(4);

        boolean hasImage = userPhoto.getImage() != null &&
            (userPhoto.getImage().getUrl() != null || userPhoto.getUserData() != null);

        if (!name.getText().isBlank() && !id.getText().isBlank()
                && dateOfBirth.getValue() != null && hasImage) {
            name.setDisable(true);
            id.setDisable(true);
            numberPhone.setDisable(true);
            dateOfBirth.setDisable(true);
            userPhoto.setDisable(true);
        } else {
            rootUsers.getChildren().remove(row);
            usersData.remove(i);
        }
    }
    selectedUser = null;

    try {
        List<UsuarioData> userList = new ArrayList<>();

        for (HBox row : usersData) {
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
        Files.writeString(Paths.get("Jsons/usuarios.json"), gson.toJson(userList));
        System.out.println("Datos guardados");

    } catch (IOException ex) {
        System.out.println("Error al guardar: " + ex.getMessage());
    }
}

private void search(String text) {
    for (int i = 0; i < usersData.size(); i++) {
        HBox row = usersData.get(i);
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

private void loadUsers() {
    try {
        Path usersFile = Paths.get("Jsons/usuarios.json");
        if (!Files.exists(usersFile)) return;
        String json = Files.readString(usersFile);
        if (json.isBlank()) return;

        Gson gson = new Gson();
        UsuarioData[] users = gson.fromJson(json, UsuarioData[].class);

        for (UsuarioData ud : users) {
            onActionAdd(null);
            HBox row = usersData.get(usersData.size() - 1);

            TextField name = (TextField) row.getChildren().get(0);
            TextField id = (TextField) row.getChildren().get(1);
            TextField numberPhone = (TextField) row.getChildren().get(2);
            DatePicker dateOfBirth = (DatePicker) row.getChildren().get(3);
            ImageView userPhoto = (ImageView) row.getChildren().get(4);

            name.setText(ud.nombre);
            id.setText(ud.cedula);
            numberPhone.setText(ud.numero);
            dateOfBirth.setValue(ud.fecha != null && !ud.fecha.isBlank()
                    ? java.time.LocalDate.parse(ud.fecha)
                    : null);

            if (ud.imagen != null && !ud.imagen.isBlank()) {
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

            name.setDisable(true);
            id.setDisable(true);
            numberPhone.setDisable(true);
            dateOfBirth.setDisable(true);
            userPhoto.setDisable(true);
        }

    } catch (IOException e) {
        System.out.println("Error al cargar: " + e.getMessage());
    }
}

@Override
public void initialize(URL url, ResourceBundle rb) {
    loadUsers();
    searchField.textProperty().addListener((obs, oldVal, newVal) -> {
        search(newVal);
    });
}

}
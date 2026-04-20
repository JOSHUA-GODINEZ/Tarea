package cr.ac.una.tarea.util;

import com.github.sarxos.webcam.Webcam;
import io.github.palexdev.materialfx.utils.SwingFXUtils;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

public class CamaraUtil {
private static Propiedades config = new Propiedades();

// crea la ventana y sus botnes para tomar la foto
    public static void tomarFoto(ImageView userFoto) {
        Webcam webcam = Webcam.getDefault();
        if (webcam == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
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
        BorderPane cameraRoot = new BorderPane(preview);
        cameraRoot.setBottom(buttonsBox);

        Stage cameraStage = new Stage();
        cameraStage.initModality(Modality.APPLICATION_MODAL);
        cameraStage.setTitle("Tomar foto");
        cameraStage.setScene(new Scene(cameraRoot, 500, 430));

        
        // Captura la imagen de la webcam
        Thread streamThread = new Thread(() -> {
            while (cameraStage.isShowing()) {
                try {
                    BufferedImage frame = webcam.getImage();
                    if (frame != null) {
                        Image fxImage = SwingFXUtils.toFXImage(frame, null);
                        Platform.runLater(() -> preview.setImage(fxImage));
                    }
                    Thread.sleep(33);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        streamThread.setDaemon(true);

        captureBtn.setOnAction(ev -> {
            BufferedImage capturedFrame = webcam.getImage();
            if (capturedFrame != null) {
                Image fxImage = SwingFXUtils.toFXImage(capturedFrame, null);
                String photoPath = guardarFoto(fxImage);
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
    }

    //Guarda la foto con un nombre diferente(fecha y hora actual) en la carpeta de del .ini
private static String guardarFoto(Image image) {
    try {
        String basePath = config.getRutaJson();

        File base = new File(basePath);
        if (base.isFile()) {
            base = base.getParentFile();
        }

        File carpetaFotos = new File(base, "Fotos");
        if (!carpetaFotos.exists()) {
            carpetaFotos.mkdirs();
        }

        String nombre = "foto_" +
                LocalDateTime.now().format(
                        DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
                ) + ".png";

        File outputFile = new File(carpetaFotos, nombre);

        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", outputFile);

        return outputFile.toURI().toString();

    } catch (IOException ex) {
        ex.printStackTrace();
        return "";
    }
}
}

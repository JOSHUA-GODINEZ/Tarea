package cr.ac.una.tarea.controller;

import com.google.gson.Gson;
import cr.ac.una.tarea.App;
import cr.ac.una.tarea.model.DataHolder;
import cr.ac.una.tarea.model.DataParametres;
import cr.ac.una.tarea.model.EstacionData;
import cr.ac.una.tarea.model.FichasProyection;
import cr.ac.una.tarea.model.SucursalData;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.Interpolator;
import javafx.fxml.Initializable;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.util.Duration;

public class ProjectionController implements Initializable {

    @FXML
    private Label lblFechaHora;
    @FXML
    private Label movingText;
    @FXML
    private HBox container;
    @FXML
    private Label LName;
    @FXML
    private ImageView Logo;
    @FXML
    private HBox root;

    private TranslateTransition animation;
    @FXML
    private VBox rootFichas;
    @FXML
    private Label estacion1;
    @FXML
    private Label estacion2;
    @FXML
    private Label estacion3;
    @FXML
    private Label estacion4;
    @FXML
    private Label ficha1;
    @FXML
    private Label ficha2;
    @FXML
    private Label ficha3;
    @FXML
    private Label ficha4;
    @FXML
    private ImageView IMagePrefer1=new ImageView();
    @FXML
    private VBox rootFicha1;
    private long ultimaModificacion = 0;
    @FXML
    private VBox rootFicha2;
    @FXML
    private ImageView IMagePrefer2;
    @FXML
    private VBox rootFicha3;
    @FXML
    private ImageView IMagePrefer3;
    @FXML
    private VBox rootFicha4;
    @FXML
    private ImageView IMagePrefer4;
    List<String> audios = new ArrayList<>();
    private long ultimaSenal = 0;
private Timeline timelineFichas;
private Timeline reloj;
private boolean reproduciendo = false;
private static ProjectionController instance;
private Timeline timeoutAudio;
private javafx.scene.media.MediaPlayer playerActual = null;
      private void cargar() {
    try {
         Path archivo = Paths.get("Jsons/GenenarlData.json");
        if (!Files.exists(archivo)) return;
        String json = Files.readString(archivo);
        if (json.isBlank()) return;

        Gson gson = new Gson();
        DataParametres user = gson.fromJson(json, DataParametres.class);

       LName.setText(user.getName());
       Logo.setImage(new Image(user.getImageUrl()));
               Logo.fitWidthProperty().bind(root.widthProperty());
         Logo.fitHeightProperty().bind(root.heightProperty());
    } catch (IOException e) {
        System.out.println("Error al cargar: " + e.getMessage());
    }
}
      
      
    private void cargarInfo() {
      
    try {
         Path archivo = Paths.get("Jsons/BranchesData.json");
        if (!Files.exists(archivo)) return;
        String json = Files.readString(archivo);
        if (json.isBlank()) return;

        Gson gson = new Gson();
       SucursalData[] sucursales = gson.fromJson(json, SucursalData[].class);
        String seleccionado = DataHolder.selectedLabel;

for (SucursalData sd : sucursales) {

    if (sd.nombre.equals(seleccionado)) {

        movingText.setText(sd.BranchInfo);
        break;
    }
}
    } catch (IOException e) {
        System.out.println("Error al cargar: " + e.getMessage());
    }
    
    
    }
    
    
    
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    instance = this;
    reproduciendo = true; // ✅ bloquea audio al entrar

  //cargarAudios();

    cargar();
    cargarInfo();
    cargarFicha();

    // ✅ inicializa timestamps para ignorar archivos existentes
    File archivoSenal = new File("Jsons/signal.json");
    if (archivoSenal.exists()) {
        ultimaSenal = archivoSenal.lastModified();
    }
    File archivoFichas = new File("Jsons/Fichas.json");
    if (archivoFichas.exists()) {
        ultimaModificacion = archivoFichas.lastModified();
    }

    reproduciendo = false; // ✅ listo para recibir señales nuevas

    // reloj — solo uno
    if (reloj != null) reloj.stop();
    reloj = new Timeline(
        new KeyFrame(Duration.seconds(1), e -> {
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            lblFechaHora.setText(LocalDateTime.now().format(formato));
        })
    );
    reloj.setCycleCount(Timeline.INDEFINITE);
    reloj.play();

    // monitoreo de fichas y señal
    if (timelineFichas != null) timelineFichas.stop();
timelineFichas = new Timeline(
    new KeyFrame(Duration.seconds(1), e -> {
        File fichas = new File("Jsons/Fichas.json");
        if (fichas.lastModified() != ultimaModificacion) {
            ultimaModificacion = fichas.lastModified();
            Platform.runLater(() -> {
                cargarFicha();
                cargarAudios();
                 System.out.println("Sucursal: "+ DataHolder.selectedLabel);// ✅ aquí, después de cargar ficha
            });
        }
File senal = new File("Jsons/signal.json");
if (senal.exists() && senal.lastModified() != ultimaSenal) {
    ultimaSenal = senal.lastModified();
    Platform.runLater(() -> {
        detenerAudio(); // ✅ cancela lo que esté sonando
        reproducirAudiosEnSecuencia(new ArrayList<>(audios), 0); // ✅ empieza de nuevo
    });
}
    })
);
    timelineFichas.setCycleCount(Timeline.INDEFINITE);
    timelineFichas.play();

    // animación texto
    movingText.layoutBoundsProperty().addListener((obs, oldVal, newVal) -> {
        double textWidth = newVal.getWidth();
        double containerWidth = container.getWidth();
        if (animation != null) animation.stop();
        animation = new TranslateTransition();
        animation.setNode(movingText);
        animation.setDuration(Duration.seconds(13));
        animation.setFromX(containerWidth);
        animation.setToX(-textWidth);
        animation.setCycleCount(TranslateTransition.INDEFINITE);
        animation.play();
    });
    
    
     
    
         movingText.sceneProperty().addListener((obs, oldScene, newScene) -> {
        if (newScene != null) {
            movingText.styleProperty().bind(
                newScene.widthProperty().multiply(0.04).asString("-fx-font-size: %.2fpx;")
            );
            estacion1.styleProperty().bind(
                newScene.widthProperty().multiply(0.03).asString("-fx-font-size: %.2fpx;")
            );
            ficha1.styleProperty().bind(
                newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
             estacion2.styleProperty().bind(
                newScene.widthProperty().multiply(0.03).asString("-fx-font-size: %.2fpx;")
            );
            ficha2.styleProperty().bind(
                newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
               estacion3.styleProperty().bind(
                newScene.widthProperty().multiply(0.03).asString("-fx-font-size: %.2fpx;")
            );
            ficha3.styleProperty().bind(
                newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
               estacion4.styleProperty().bind(
                newScene.widthProperty().multiply(0.03).asString("-fx-font-size: %.2fpx;")
            );
            ficha4.styleProperty().bind(
                newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
             LName.styleProperty().bind(
                newScene.widthProperty().multiply(0.03).asString("-fx-font-size: %.2fpx;")
            );
        }
        
    });
  
}
    
    public void cargarFicha() {
   try {
    Gson gson = new Gson();

    Path archivo = Paths.get("Jsons/Fichas.json");

    if (!Files.exists(archivo)) return;

    String json = Files.readString(archivo);
    if (json.isBlank()) return;

    FichasProyection[] lista = gson.fromJson(json, FichasProyection[].class);

    for (FichasProyection fichaActual : lista) {

        if (fichaActual.Sucursal != null &&
            fichaActual.Sucursal.equals(DataHolder.selectedLabel)) {

            estacion1.setText(fichaActual.estacion[0] != null ? fichaActual.estacion[0] : "");
            estacion2.setText(fichaActual.estacion[1] != null ? fichaActual.estacion[1] : "");
            estacion3.setText(fichaActual.estacion[2] != null ? fichaActual.estacion[2] : "");
            estacion4.setText(fichaActual.estacion[3] != null ? fichaActual.estacion[3] : "");

            ficha1.setText(fichaActual.ficha[0] != null ? fichaActual.ficha[0] : "");
            ficha2.setText(fichaActual.ficha[1] != null ? fichaActual.ficha[1] : "");
            ficha3.setText(fichaActual.ficha[2] != null ? fichaActual.ficha[2] : "");
            ficha4.setText(fichaActual.ficha[3] != null ? fichaActual.ficha[3] : "");
            rootFicha1.getStyleClass().add("mi-panel-fondo1");
             rootFicha1.getStyleClass().add("seleccionado");
            rootFicha2.getStyleClass().add("mi-panel-fondo1");
             rootFicha2.getStyleClass().remove("seleccionado");
            rootFicha3.getStyleClass().add("mi-panel-fondo1");
            rootFicha4.getStyleClass().add("mi-panel-fondo1");
            IMagePrefer1.getStyleClass().remove("mi-IMagepreferencial");
            IMagePrefer1.getStyleClass().remove("seleccionado"); 
            IMagePrefer2.getStyleClass().remove("mi-IMagepreferencial");
            IMagePrefer3.getStyleClass().remove("mi-IMagepreferencial");
            IMagePrefer4.getStyleClass().remove("mi-IMagepreferencial");
            
            if (fichaActual.Preferencial[0] != null && fichaActual.Preferencial[0]) {
                IMagePrefer1.getStyleClass().add("mi-IMagepreferencial"); 
                 
            }
 if (fichaActual.Preferencial[1] != null && fichaActual.Preferencial[1]) {
                IMagePrefer2.getStyleClass().add("mi-IMagepreferencial");
            }
  if (fichaActual.Preferencial[2] != null && fichaActual.Preferencial[2]) {
                IMagePrefer3.getStyleClass().add("mi-IMagepreferencial");
            }
   if (fichaActual.Preferencial[3] != null && fichaActual.Preferencial[3]) {
                IMagePrefer4.getStyleClass().add("mi-IMagepreferencial");
            }
            break; 
        }
    }
//cargarAudios();
} catch (IOException e) {
    System.out.println("Error: " + e.getMessage());
}
}

    @FXML
    private void BtnSalir(ActionEvent event) throws IOException {
        /* Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();*/
          App.setRoot("OfficialsView");
    }

    @FXML
    private void onActionActualizar(ActionEvent event) {
        initialize(null, null);
    }
private void reproducirAudiosEnSecuencia(List<String> rutas, int index) {
    if (index >= rutas.size()) {
        playerActual = null;
        return;
    }

    try {
        String ruta = new File(rutas.get(index)).toURI().toString();
        javafx.scene.media.Media media = new javafx.scene.media.Media(ruta);
        javafx.scene.media.MediaPlayer player = new javafx.scene.media.MediaPlayer(media);
        playerActual = player; // ✅ guarda referencia

        player.setOnReady(() -> {
            System.out.println("reproduciendo " + index + ": " + rutas.get(index));
            player.play();
        });

        player.setOnEndOfMedia(() -> {
            System.out.println("termino " + index);
            player.dispose();
            Timeline pausa = new Timeline(
                new KeyFrame(Duration.millis(300), e ->
                    reproducirAudiosEnSecuencia(rutas, index + 1)
                )
            );
            pausa.play();
        });

        player.setOnError(() -> {
            System.out.println("Error " + index + ": " + player.getError().getMessage());
            player.dispose();
            reproducirAudiosEnSecuencia(rutas, index + 1);
        });

    } catch (Exception e) {
        System.out.println("Error: " + e.getMessage());
        playerActual = null;
    }
}

private void detenerAudio() {
    if (playerActual != null) {
        try {
            playerActual.stop();
            playerActual.dispose();
            playerActual = null;
        } catch (Exception e) {
            playerActual = null;
        }
    }
}

public void recargarDatos() {
    Platform.runLater(() -> {  // ✅ asegura que corre en el hilo de JavaFX
        cargarFicha();
        reproducirAudiosEnSecuencia(audios, 0);
        System.out.println("Actualizando...");
    });
}
public static ProjectionController getInstance() {
    return instance;
}

private void cargarAudios() {
   System.out.println("estacion1 texto: " + estacion1.getText());
List<String> nuevosAudios = new ArrayList<>();
String estacionActual = estacion1.getText();
boolean encontrado = false; // ✅ flag

try {
    Path archivo = Paths.get("Jsons/BranchesData.json");
    if (!Files.exists(archivo)) return;
    String json = Files.readString(archivo);
    if (json.isBlank()) return;
    Gson gson = new Gson();
    SucursalData[] sucursales = gson.fromJson(json, SucursalData[].class);

    for (SucursalData sd : sucursales) {
        if (!sd.nombre.equals(DataHolder.selectedLabel)) continue;

        for (EstacionData ed : sd.estaciones) {
            if (ed.nombre.equals(estacionActual)) {
                encontrado = true; // ✅ encontró
                if (ed.rutaAudio != null && !ed.rutaAudio.isBlank()) {
                    // ✅ solo agrega todos si encontró la estación correcta
                    nuevosAudios.add("C:/Joshua/Tarea/sonidos/Ficha.wav");
                    nuevosAudios.add("C:/Joshua/Tarea/sonidos/A1.wav");
                    nuevosAudios.add("C:/Joshua/Tarea/sonidos/Estacion.wav");
                    nuevosAudios.add(ed.rutaAudio);
                }
                break;
            }
        }
    }
} catch (IOException e) {
    System.out.println("Error cargando audio: " + e.getMessage());
}

if (encontrado) {
    audios.clear();
    audios.addAll(nuevosAudios);
} else {
    audios.clear(); // ✅ si no encontró, limpia para que no suene nada
    System.out.println("estacion no encontrada, no se reproducirá audio");
}
}
}


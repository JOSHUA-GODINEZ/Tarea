package cr.ac.una.tarea.controller;

import com.google.gson.Gson;
import cr.ac.una.tarea.App;

import cr.ac.una.tarea.model.DataParametres;
import cr.ac.una.tarea.model.EstacionData;
import cr.ac.una.tarea.model.FichasProyection;
import cr.ac.una.tarea.model.SignalData;
import cr.ac.una.tarea.model.SucursalData;
import cr.ac.una.tarea.model.Teme;
import cr.ac.una.tarea.util.DataEjecucion;
import cr.ac.una.tarea.util.Propiedades;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javafx.util.Duration;

public class ProjectionController implements Initializable {

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
private boolean reproduciendoAudio = false;
private Boolean temaAnterior = null;
Propiedades config = new Propiedades();
DataEjecucion data = new DataEjecucion(config);
private javafx.scene.media.MediaPlayer playerActual = null;
          // Carga los parametros generales
      private void cargar() {
    try {
       File archivo = data.getArchivo("GeneralData");
        if (!Files.exists(archivo.toPath())) return;
        String json = Files.readString(archivo.toPath());
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
      
      // Carga la informacion de las sucursales
    private void cargarInfo() {
    try {
               File archivo = data.getArchivo("BranchesData");
        if (!Files.exists(archivo.toPath())) return;
        String json = Files.readString(archivo.toPath());
        if (json.isBlank()) return;

        Gson gson = new Gson();
       SucursalData[] sucursales = gson.fromJson(json, SucursalData[].class);
        String seleccionado = config.getSucursal();

       for (SucursalData sd : sucursales) {

    if (sd.nombre.equals(seleccionado)) {

        movingText.setText(sd.BranchInfo);
        break;
    }
         }
    } catch (IOException e) {
    }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    reproduciendo = true; //  bloquea audio al entrar

    cargar();
    cargarInfo();
    cargarFicha();
    
 File archivoSenal = data.getArchivo("signal");
if (archivoSenal.exists()) {
    // El archivo señal que se actualiza con el boton funcionarios se guarda como ultima señal
    try {
        String json = Files.readString(archivoSenal.toPath());
        if (!json.isBlank()) {
            SignalData signal = new Gson().fromJson(json, SignalData.class);
            ultimaSenal = signal.timestamp;
        }
    } catch (IOException e) {
      
    }
}
    File archivoFichas = data.getArchivo("Fichas");
    if (archivoFichas.exists()) {
        ultimaModificacion = archivoFichas.lastModified();
    }

    reproduciendo = false; // puede recibir señales nuevas

     // relog para cargar la hora cada egundo y cargar demas informacion
    if (reloj != null) reloj.stop();
    reloj = new Timeline(
        new KeyFrame(Duration.seconds(1), e -> {
            config.cargar();
            cargarInfo();
            cargarFicha();
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            lblFechaHora.setText(LocalDateTime.now().format(formato));
        })
    );
    reloj.setCycleCount(Timeline.INDEFINITE);
    reloj.play();

    // monitoreo de fichas y señal
    if (timelineFichas != null) timelineFichas.stop();
timelineFichas = new Timeline(
    new KeyFrame(Duration.seconds(2), e -> {
File senal = data.getArchivo("signal");
if (senal.exists()) {
    try {

        String json = Files.readString(senal.toPath());
        if (json.isBlank()) return;
        Gson gson = new Gson();
        SignalData signal = gson.fromJson(json, SignalData.class);
        if (signal.timestamp == ultimaSenal) return;
        ultimaSenal = signal.timestamp;
        Platform.runLater(() -> procesarSignal(signal));// Si en cuentra algun cambio, esto ejecuta todo

    } catch (IOException ex) {
        System.out.println("Error leyendo signal: " + ex.getMessage());
    }
}
    })
);
    timelineFichas.setCycleCount(Timeline.INDEFINITE);
    timelineFichas.play();

    // animación texto en movimiento
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

    //Cambia el tamaño del texto
         movingText.sceneProperty().addListener((obs, oldScene, newScene) -> {
        if (newScene != null) {
            movingText.styleProperty().bind(
                newScene.widthProperty().multiply(0.04).asString("-fx-font-size: %.2fpx;")
            );
            estacion1.styleProperty().bind(
                newScene.widthProperty().multiply(0.03).asString("-fx-font-size: %.2fpx;")
            );
            ficha1.styleProperty().bind(
                newScene.widthProperty().multiply(0.03).asString("-fx-font-size: %.2fpx;")
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
         //Carga el tema
  Timeline timeline = new Timeline(
    new KeyFrame(Duration.seconds(1), e -> {
        try {
            cargar();
             File archivoTheme = data.getArchivo("theme");
if (!archivoTheme.exists()) return;
String json = Files.readString(archivoTheme.toPath());

            Gson gson = new Gson();
            Teme t = gson.fromJson(json, Teme.class);

            if (temaAnterior == null || !temaAnterior.equals(t.temeDark)) {
                temaAnterior = t.temeDark;
                root.getStyleClass().clear();
                container.getStyleClass().clear();
                rootFichas.getStyleClass().clear();
               
 if (t.temeDark) {
     root.getStyleClass().addAll("mi-rectangulooscuro","mi-Titulososcuros");
    container.getStyleClass().addAll("mi-rectangulooscuro","mi-Titulososcuros");
    rootFichas.getStyleClass().addAll("mi-rectangulooscuro");
} else {
   root.getStyleClass().addAll("mi-rectangulo","mi-Titulos");
    rootFichas.getStyleClass().addAll("mi-rectangulo");
    container.getStyleClass().addAll("mi-rectangulo","mi-Titulos");
}
            }
        } catch (IOException ex) {
        }
    })
);
timeline.setCycleCount(Timeline.INDEFINITE);
timeline.play();
}
    
    public void cargarFicha() {
        //Carga la ficha del archivo
   try {
    Gson gson = new Gson();
 File archivo = data.getArchivo("Fichas");
        if (!Files.exists(archivo.toPath())) return;
        String json = Files.readString(archivo.toPath());
        if (json.isBlank()) return;
   
    FichasProyection[] lista = gson.fromJson(json, FichasProyection[].class);

    for (FichasProyection fichaActual : lista) {
//Asigna las ficha por sucursal, la asigna de arriva para abajo
        if (fichaActual.Sucursal != null &&
            fichaActual.Sucursal.equals(config.getSucursal())) {

        if (fichaActual.estacion[0] != null) estacion1.setText(fichaActual.estacion[0]); else estacion1.setText("");
        if (fichaActual.estacion[1] != null) estacion2.setText(fichaActual.estacion[1]); else estacion2.setText("");
        if (fichaActual.estacion[2] != null) estacion3.setText(fichaActual.estacion[2]); else estacion3.setText("");
        if (fichaActual.estacion[3] != null) estacion4.setText(fichaActual.estacion[3]); else estacion4.setText("");

        if (fichaActual.ficha[0] != null) ficha1.setText(fichaActual.ficha[0]); else ficha1.setText("");
        if (fichaActual.ficha[1] != null) ficha2.setText(fichaActual.ficha[1]); else ficha2.setText("");
        if (fichaActual.ficha[2] != null) ficha3.setText(fichaActual.ficha[2]); else ficha3.setText("");
        if (fichaActual.ficha[3] != null) ficha4.setText(fichaActual.ficha[3]); else ficha4.setText("");
            //Cambia el CSS
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
            //CSS si es preferencial
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
} catch (IOException e) {
    System.out.println("Error: " + e.getMessage());
}
}

private void reproducirAudiosEnSecuencia(List<String> rutas, int index) {
    if (index == 0) {
        reproduciendoAudio = true; // empieza
    }
    if (index >= rutas.size()) {
        playerActual = null;
        reproduciendoAudio = false; //termina
        return;
    }

    try {
         // Reproduce audios en secuencia con manejo de errores y pequeña pausa entre cada uno
        Media media = crearMedia(rutas.get(index));

        if (media == null) {
            reproducirAudiosEnSecuencia(rutas, index + 1);
            return;
        }
        MediaPlayer player = new MediaPlayer(media);
        playerActual = player;

        player.play();

        player.setOnEndOfMedia(() -> {
            player.dispose();

            Timeline pausa = new Timeline(
                new KeyFrame(Duration.millis(300), e ->
                    reproducirAudiosEnSecuencia(rutas, index + 1)
                )
            );
            pausa.play();
        });

        player.setOnError(() -> {
            System.out.println("Error: " + player.getError());
            player.dispose();
            reproducirAudiosEnSecuencia(rutas, index + 1);
        });

    } catch (Exception e) {
        System.out.println("Error general: " + e.getMessage());
        reproducirAudiosEnSecuencia(rutas, index + 1);
    }
}


public void recargarDatos() {
    Platform.runLater(() -> {  // ✅ asegura que corre en el hilo de JavaFX
        cargarFicha();
        reproducirAudiosEnSecuencia(audios, 0);
        System.out.println("Actualizando...");
    });
}

private void cargarAudios() {
   audios.clear(); 

  

   

    List<String> nuevosAudios = new ArrayList<>();
    String estacionActual = estacion1.getText();
    boolean encontrado = false;

    try {
        Gson gson = new Gson();

        // leer sucursales
        File archivoBranches = data.getArchivo("BranchesData");
        if (!archivoBranches.exists()) return;

        String json = Files.readString(archivoBranches.toPath());
        if (json.isBlank()) return;

        SucursalData[] sucursales = gson.fromJson(json, SucursalData[].class);
        if (sucursales == null) sucursales = new SucursalData[0];

        // leer signal
        File archivoSignal = data.getArchivo("signal");
        String audioFicha = null;

        if (archivoSignal.exists()) {
            String jsonSignal = Files.readString(archivoSignal.toPath());

            if (!jsonSignal.isBlank()) {
                SignalData signal = gson.fromJson(jsonSignal, SignalData.class);
                if (signal != null) {
                    audioFicha = signal.audioFicha;
                }
            }
        }

       for (SucursalData sd : sucursales) {

           for (EstacionData ed : sd.estaciones) {
//Agrega los 4 audios a la lista
        if (ed.nombre.equals(estacionActual)) {

            encontrado = true;

            agregarAudioSeguro(nuevosAudios, "Ficha.wav");

            if (audioFicha != null && !audioFicha.isBlank()) {
                agregarAudioSeguro(nuevosAudios, audioFicha);
            }

            agregarAudioSeguro(nuevosAudios, "Estacion.wav");

            if (ed.rutaAudio != null && !ed.rutaAudio.isBlank()) {
                agregarAudioSeguro(nuevosAudios, ed.rutaAudio);
            }

            break;
        }
    }
    if (encontrado) break;
}
    } catch (IOException e) {
    }
    if (encontrado) {
        audios.clear();
        audios.addAll(nuevosAudios);
    } else {
        audios.clear();
    }

}
private Media crearMedia(String ruta) {

    try {

        // Caso 1 ruta dentro del JAR
        if (ruta.startsWith("jar:")) {
            return new Media(ruta);
        }

        // Caso 2 URI tipo file:/
        if (ruta.startsWith("file:/")) {
            return new Media(ruta);
        }

        // Caso 3 ruta normal del sistema
        File file = new File(ruta);

        if (!file.exists()) {
            return null;
        }
        return new Media(file.toURI().toString());
    } catch (Exception e) {
        return null;
    }
}
private void procesarSignal(SignalData signal) {
    if (!signal.sucursal.equalsIgnoreCase(config.getSucursal())) return;

    // evita repetición
    if (reproduciendoAudio) {
        return;
    }
    //Carga todo
    cargarFicha();
    cargarAudios();
    reproducirAudiosEnSecuencia(new ArrayList<>(audios), 0);
}
private String getAudioPath(String nombre) {
    URL url = getClass().getResource("/cr/ac/una/tarea/resource/sonidos/" + nombre);
    if (url == null) {
        return null;
    }
    String ruta = url.toExternalForm();
    return ruta;
}
private void agregarAudioSeguro(List<String> lista, String nombre) {
    if (nombre == null || nombre.isBlank()) return;

    // Caso 1 ruta absoluta
    if (nombre.contains(":/") || nombre.startsWith("file:/")) {
        lista.add(nombre);
        return;
    }

    // Caso 2 archivo local 
    File posible = new File(nombre);
    if (posible.exists()) {
        lista.add(posible.toURI().toString());
        return;
    }

    // Caso 3 recurso dentro del JAR
    String ruta = getAudioPath(nombre);

    if (ruta != null) {
        lista.add(ruta);
    }
}
}


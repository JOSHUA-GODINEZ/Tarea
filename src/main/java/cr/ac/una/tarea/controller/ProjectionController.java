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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

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
private boolean reproduciendoAudio = false;
private static ProjectionController instance;
private Timeline timeoutAudio;
private Boolean temaAnterior = null;
            Propiedades config = new Propiedades();
DataEjecucion data = new DataEjecucion(config);
private javafx.scene.media.MediaPlayer playerActual = null;

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
        System.out.println("Error al cargar: " + e.getMessage());
    }
    
    
    }
    
    
    
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
File archivo = new File("propiedades.ini");
System.out.println("Ruta absoluta: " + archivo.getAbsolutePath());
System.out.println("Existe: " + archivo.exists());
  System.out.println("Sucursal: "+ config.getSucursal());// ✅ aquí, después de cargar ficha
   System.out.println("Estacion: "+ config.getEstacion());// ✅ aquí, después de cargar ficha
    instance = this;
    reproduciendo = true; // ✅ bloquea audio al entrar

  //cargarAudios();

    cargar();
    cargarInfo();
    cargarFicha();

    // ✅ inicializa timestamps para ignorar archivos existentes
 File archivoSenal = data.getArchivo("signal");
if (archivoSenal.exists()) {
    try {
        
        String json = Files.readString(archivoSenal.toPath());
        if (!json.isBlank()) {
            SignalData signal = new Gson().fromJson(json, SignalData.class);
            ultimaSenal = signal.timestamp; // 🔥 CORRECTO
        }
    } catch (IOException e) {
        System.out.println("Error inicializando señal: " + e.getMessage());
    }
}
    File archivoFichas = data.getArchivo("Fichas");
    if (archivoFichas.exists()) {
        ultimaModificacion = archivoFichas.lastModified();
    }

    reproduciendo = false; // ✅ listo para recibir señales nuevas

    // reloj — solo uno
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

        Platform.runLater(() -> procesarSignal(signal));

    } catch (IOException ex) {
        System.out.println("Error leyendo signal: " + ex.getMessage());
    }
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

                // Cambia los roots
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
            System.out.println("Error: " + ex.getMessage());
        }
    })
);
timeline.setCycleCount(Timeline.INDEFINITE);
timeline.play();
}
    
    public void cargarFicha() {
   try {
    Gson gson = new Gson();
 File archivo = data.getArchivo("Fichas");
        if (!Files.exists(archivo.toPath())) return;
        String json = Files.readString(archivo.toPath());
        if (json.isBlank()) return;
   
 System.out.println("Cargando la dicha:");
    FichasProyection[] lista = gson.fromJson(json, FichasProyection[].class);

    for (FichasProyection fichaActual : lista) {

        if (fichaActual.Sucursal != null &&
            fichaActual.Sucursal.equals(config.getSucursal())) {

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
     
          App.setRoot("OfficialsView");
    }

    @FXML
    private void onActionActualizar(ActionEvent event) {
        initialize(null, null);
    }
private void reproducirAudiosEnSecuencia(List<String> rutas, int index) {

    if (index == 0) {
        reproduciendoAudio = true; // 🔥 empieza
    }

    if (index >= rutas.size()) {
        playerActual = null;
        reproduciendoAudio = false; // 🔥 termina
        return;
    }

    try {

        Media media = crearMedia(rutas.get(index));

        if (media == null) {
            reproducirAudiosEnSecuencia(rutas, index + 1);
            return;
        }

        MediaPlayer player = new MediaPlayer(media);
        playerActual = player;

        System.out.println("Reproduciendo: " + rutas.get(index));

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

private void detenerAudio() {
    if (playerActual != null) {
        try {
            playerActual.stop();
            playerActual.dispose();
        } catch (Exception e) {
            // ignorar
        }
        playerActual = null;
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

   audios.clear(); // 🔥 evita acumulación

    System.out.println("estacion1 texto: " + estacion1.getText());

   

    List<String> nuevosAudios = new ArrayList<>();
    String estacionActual = estacion1.getText();
    boolean encontrado = false;

    try {
        Gson gson = new Gson();

        // 🔹 leer sucursales
        File archivoBranches = data.getArchivo("BranchesData");
        if (!archivoBranches.exists()) return;

        String json = Files.readString(archivoBranches.toPath());
        if (json.isBlank()) return;

        SucursalData[] sucursales = gson.fromJson(json, SucursalData[].class);
        if (sucursales == null) sucursales = new SucursalData[0];

        // 🔥 leer SIGNAL (aquí viene audioFicha)
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

        System.out.println("audioFicha desde signal: " + audioFicha);

for (SucursalData sd : sucursales) {

    for (EstacionData ed : sd.estaciones) {

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

            break; // rompe inner
        }
    }

    if (encontrado) break; // 🔥 rompe outer
}

    } catch (IOException e) {
        System.out.println("Error cargando audio: " + e.getMessage());
    }

    if (encontrado) {
        audios.clear();
        audios.addAll(nuevosAudios);

        System.out.println("Audios cargados:");
        for (String a : audios) {
            System.out.println(a);
        }

      
    } else {
        audios.clear();
        System.out.println("estacion no encontrada");
    }
    System.out.println("LISTA FINAL:");
for (String a : audios) {
    System.out.println(a);
}
}
private Media crearMedia(String ruta) {

    try {

        // 🔥 CASO 1: recurso dentro del JAR
        if (ruta.startsWith("jar:")) {
            return new Media(ruta);
        }

        // 🔥 CASO 2: URI tipo file:/ ya lista
        if (ruta.startsWith("file:/")) {
            return new Media(ruta);
        }

        // 🔥 CASO 3: ruta normal del sistema (C:\...)
        File file = new File(ruta);

        if (!file.exists()) {
            System.out.println("No existe: " + ruta);
            return null;
        }

        return new Media(file.toURI().toString());

    } catch (Exception e) {
        System.out.println("Error creando media: " + e.getMessage());
        return null;
    }
}
private void procesarSignal(SignalData signal) {

    System.out.println("Procesando señal...");

    if (!signal.sucursal.equalsIgnoreCase(config.getSucursal())) return;

    // 🔥 CLAVE: evitar repetición
    if (reproduciendoAudio) {
        System.out.println("🔁 Ignorado (ya reproduciendo)");
        return;
    }

    cargarFicha();
    cargarAudios();

    reproducirAudiosEnSecuencia(new ArrayList<>(audios), 0);
}
private String getAudioPath(String nombre) {

    URL url = getClass().getResource("/cr/ac/una/tarea/resource/sonidos/" + nombre);

    if (url == null) {
        System.out.println("❌ Audio no encontrado: " + nombre);
        return null;
    }

    String ruta = url.toExternalForm();
    System.out.println("✔ Audio cargado: " + ruta);

    return ruta;
}
private void agregarAudioSeguro(List<String> lista, String nombre) {

    if (nombre == null || nombre.isBlank()) return;

    // 🔥 CASO 1: ruta absoluta (C:/..., file:/...)
    if (nombre.contains(":/") || nombre.startsWith("file:/")) {
        lista.add(nombre);
        return;
    }

    // 🔥 CASO 2: archivo local (ej: Caja1.wav en disco)
    File posible = new File(nombre);
    if (posible.exists()) {
        lista.add(posible.toURI().toString());
        return;
    }

    // 🔥 CASO 3: recurso dentro del JAR
    String ruta = getAudioPath(nombre);

    if (ruta != null) {
        lista.add(ruta);
    } else {
        System.out.println("❌ Audio no encontrado: " + nombre);
    }
}
}


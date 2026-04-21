package cr.ac.una.tarea.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cr.ac.una.tarea.model.DataProcedure;
import cr.ac.una.tarea.model.EstacionData;
import cr.ac.una.tarea.model.SucursalData;
import cr.ac.una.tarea.model.Teme;
import cr.ac.una.tarea.util.Alertas;
import cr.ac.una.tarea.util.DataEjecucion;
import cr.ac.una.tarea.util.Propiedades;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class BranchesStationsController implements Initializable {
    @FXML
    private VBox rootBranches;
    @FXML
    private VBox rootProcedures;
    @FXML
    private TextField searchField;
    @FXML
    private VBox rootStation;
private String rutaAudio = null;

    private VBox selectedBranch = null;
    private HBox selectedStation = null;
    private VBox currentStationContent = null;
    private List<VBox> branches = new ArrayList<>();
    private List<HBox> stations = new ArrayList<>();
    private List<VBox> stationContents = new ArrayList<>();

    @FXML
    private Label LblMensaje;

    @FXML
    private Label LblSyE;
    @FXML
    private Label LblTramite;
    @FXML
    private Label LblStation;
private Boolean temaAnterior = null;
    @FXML
    private HBox rootMensaje;
        @FXML
    private HBox rootTitulos;
      Propiedades config = new Propiedades();
       DataEjecucion data = new DataEjecucion(config);
       private Button btnAudio;

@FXML
private void onActionAddBranch(ActionEvent event) {
     // Crea las Sucursales
    TextField branchName = new TextField();
    TextArea branchInformation = new TextArea();
    Accordion accordion = new Accordion();
    TitledPane pane = new TitledPane();
    VBox buttonContainer = new VBox();
    VBox branch = new VBox();
    Button addStationBtn = new Button();
    int[] stationCounter = {0};

    branchName.setPromptText("Nombre Sucursal");
    branchInformation.setPromptText("Informacion Sucursal");
    branchInformation.setMaxSize(Double.MAX_VALUE, 50);
    addStationBtn.setMaxSize(200, Double.MAX_VALUE);
    VBox.setVgrow(addStationBtn, Priority.ALWAYS);
    pane.setText("Estaciones");
    addStationBtn.getStyleClass().add("mi-botonAdd");
    buttonContainer.getChildren().add(addStationBtn);
    buttonContainer.setSpacing(10);
    buttonContainer.getStyleClass().addAll("mi-rectangulo","label-procedimiento");
    buttonContainer.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    pane.setContent(buttonContainer);
    accordion.getPanes().add(pane);
    branch.getChildren().add(branchName);
    branch.getChildren().add(branchInformation);
    branch.getChildren().add(accordion);
    branch.setPadding(new Insets(30, 20, 30, 20));
    branch.getStyleClass().add("mi-rectangulo");
    rootStation.getChildren().add(new Label(""));
    rootBranches.getChildren().add(branch);
    branches.add(branch);

    // Grega las estaciones
    addStationBtn.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            TextField stationName = new TextField();
            HBox stationRow = new HBox();
            CheckBox preferentialCheck = new CheckBox();
            VBox stationContent = new VBox();
            ImageView PreferIMag = new ImageView();
            String[] ruta = {""};
            stations.add(stationRow);
            stationContents.add(stationContent);

           // Agrega los adudios predeterminados por estacion(son 5)
            stationCounter[0]++;
            int audioIndex = ((stationCounter[0] - 1) % 5) + 1; 
String audioFileName = "caja" + audioIndex + ".wav";
java.net.URL audioUrl = getClass().getResource("/cr/ac/una/tarea/resource/sonidos/" + audioFileName);

if (audioUrl != null) {
    stationRow.setUserData(audioUrl.toExternalForm());
   
}
            PreferIMag.setFitHeight(40);
            PreferIMag.setFitWidth(40);
            stationName.setPromptText("Nombre Estación");
            PreferIMag.getStyleClass().add("mi-IMagepreferencial");
            stationRow.getChildren().addAll(stationName, preferentialCheck, PreferIMag);
            stationRow.setSpacing(10);
            stationRow.getStyleClass().addAll("mi-rectangulo", "mi-boton2", "label-procedimiento");
            stationRow.setMinHeight(70);

            // Se mustra los tramites y audios que tiene la estacion
            stationRow.setOnMouseClicked(station -> {
                if (selectedStation != null) {
                    selectedStation.getStyleClass().remove("seleccionado");
                }
                stationRow.getStyleClass().add("seleccionado");
                selectedStation = stationRow;
                currentStationContent = stationContent;
                loadProcedures();


                VBox preview = new VBox();
                Button audio = new Button();
                Button playAudio = new Button();          
                Label selecAudio = new Label("Seleccione un audio.wav (Opcional)");
                HBox audios = new HBox();
                Label nameLabel = new Label(stationName.getText());
               nameLabel.getStyleClass().addAll("mi-rectanguloborde");
             
                nameLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                HBox.setHgrow(nameLabel, Priority.ALWAYS);
                nameLabel.getStyleClass().addAll("label-procedimiento");
                selecAudio.getStyleClass().add("label-procedimiento");
                Scene scene = rootStation.getScene();
                if (scene != null) {
                    nameLabel.styleProperty().bind(
                        scene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
                    );
                }

                stationContent.setSpacing(10);
                stationContent.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                VBox.setVgrow(stationContent, Priority.ALWAYS);
                stationContent.setAlignment(Pos.TOP_CENTER);
                preview.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                VBox.setVgrow(preview, Priority.ALWAYS);
                preview.setSpacing(10);
                preview.setMinHeight(70);
                preview.getStyleClass().add("label-procedimiento");

                audios.getStyleClass().add("label-procedimiento");
                audio.getStyleClass().add("mi-botonAdd");
               
                playAudio.getStyleClass().add("mi-BtnReproducir");
                audio.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                HBox.setHgrow(audio, Priority.ALWAYS);
              
                playAudio.setMaxSize(40, Double.MAX_VALUE);
                HBox.setHgrow(playAudio, Priority.ALWAYS);

                Label rutaLabel = new Label("Sin audio");
                rutaLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                HBox.setHgrow(rutaLabel, Priority.ALWAYS);
                rutaLabel.getStyleClass().add("label-procedimiento");

                // ✅ Mostrar el nombre del audio asignado automáticamente
                if (stationRow.getUserData() != null) {
                    rutaLabel.setText(new File(stationRow.getUserData().toString()).getName());
                } else {
                    rutaLabel.setText("Sin audio");
                }
               // Agrega audio wav
                audio.setOnMouseClicked(clickAudio -> {
                    FileChooser fileChooser = new FileChooser();
   fileChooser.setTitle("Seleccionar audio");fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Audio", "*.mp3", "*.wav", "*.aac"));
                    Stage stage = (Stage) stationRow.getScene().getWindow();
                    File archivo = fileChooser.showOpenDialog(stage);
                    if (archivo != null) {
                        stationRow.setUserData(archivo.getAbsolutePath());
                        rutaLabel.setText(archivo.getName());
                    }
                });

                
              playAudio.setOnMouseClicked(clickPlay -> {
                   String rutaAudio;

              if (stationRow.getUserData() != null) {
                 rutaAudio = stationRow.getUserData().toString();
                 } else {
                    rutaAudio = "";
                      }

    if (rutaAudio.isEmpty()) {   
        return;
    }

    Media media;
    // Lo reproduce si es predeterminado o seleccionado
    if (rutaAudio.startsWith("file:/") || rutaAudio.startsWith("jar:")) {
        media = new Media(rutaAudio);
    } else {
        File file = new File(rutaAudio);
media = new Media(file.toURI().toString());
    }
    MediaPlayer player = new MediaPlayer(media);
    player.setOnReady(() -> player.play());
});

            // Agrega los objetos a los roots
                audios.getChildren().addAll(audio, rutaLabel, playAudio);
                preview.getChildren().addAll(selecAudio, audios, nameLabel, stationContent);
                rootStation.getChildren().clear();
                rootStation.getChildren().add(preview);
                Scene scene1 = rootStation.getScene();
                if (scene1 != null) {
                    selecAudio.styleProperty().bind(
                        scene1.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
                    );
                }
            });
            buttonContainer.getChildren().add(stationRow);
            stationRow.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) {
                    stationName.styleProperty().bind(
                        newScene.widthProperty().multiply(0.013).asString("-fx-font-size: %.2fpx;")
                    );
                }
            });
        }
    });
         // Seleciona Sucursal
        branch.setOnMouseClicked(e -> {
              if (selectedBranch != null) {
                    selectedBranch.getStyleClass().remove("seleccionado");
                }
                branch.getStyleClass().add("seleccionado");
            selectedBranch = branch;
        });
        
        
               // Cambia el tamaño del texto segun el tamaño de la ventana
          Scene scene = rootBranches.getScene();
              if (scene != null) {
          branchName.styleProperty().bind(
            scene.widthProperty().multiply(0.015).asString("-fx-font-size: %.2fpx;")
             );
             branchInformation.styleProperty().bind(
              scene.widthProperty().multiply(0.015).asString("-fx-font-size: %.2fpx;")
           );
             } else {
             rootBranches.sceneProperty().addListener((obs, oldScene, newScene) -> {
        if (newScene != null) {
            branchName.styleProperty().bind(
                newScene.widthProperty().multiply(0.015).asString("-fx-font-size: %.2fpx;")
            );
            branchInformation.styleProperty().bind(
                newScene.widthProperty().multiply(0.015).asString("-fx-font-size: %.2fpx;")
            );
        }
    });
}
      
}   

    @FXML
    private void onActionDelete(ActionEvent event) {
        // Elimina toda la sucursal selecionada
        if (selectedBranch != null) {
        
            rootBranches.getChildren().remove(selectedBranch);
            branches.remove(selectedBranch);
        }
          // elimina la station selecionada 
        if (selectedStation != null) {
            int index = stations.indexOf(selectedStation);
            if (index != -1) {
                stations.remove(index);
                stationContents.remove(index);
            }
            ((Pane) selectedStation.getParent()).getChildren().remove(selectedStation);
        }
 selectedBranch.getStyleClass().remove("seleccionado");
        selectedBranch = null;
        selectedStation = null;
    }

    @FXML
    private void onActionEdit(ActionEvent event) {
     //Permite editar la sucursal selecionada
        if (selectedBranch != null) {
    TextField branchName = (TextField) selectedBranch.getChildren().get(0);
    branchName.setEditable(true);
    branchName.getStyleClass().add("procedimiento-editando");
    
        TextArea branchInformation = (TextArea) selectedBranch.getChildren().get(1);
    branchInformation.setEditable(true);
    branchInformation.getStyleClass().add("procedimiento-editando");
    

    Accordion accordion = (Accordion) selectedBranch.getChildren().get(2);
    TitledPane pane = accordion.getPanes().get(0);
    VBox buttonContainer = (VBox) pane.getContent();

// Recorre todos los nodos hijos dentro del contenedor principal
for (Node n : buttonContainer.getChildren()) {
    // instanceof sirve para comprobar el tipo de objeto en tiempo de ejecución
    if (n instanceof HBox) {
       HBox hb = (HBox) n;
        for (Node child : hb.getChildren()) {

            if (child instanceof TextField) {
                // Convierte el Node a TextField para poder usar sus métodos
                ((TextField) child).setEditable(true); // permite editar el texto

                child.getStyleClass().add("procedimiento-editando");
            }

            if (child instanceof CheckBox) {
                // Convierte el Node a CheckBox
                ((CheckBox) child).setDisable(false); // lo habilita (false = activo)

                child.getStyleClass().add("procedimiento-editando");
            }
        }
    }
}
  selectedBranch.getStyleClass().remove("seleccionado");
}
       // Permite editar la Estacion selecionada
        if (selectedStation != null) {
        TextField station = (TextField) selectedStation.getChildren().get(0);
        CheckBox pref = (CheckBox) selectedStation.getChildren().get(1);
        station.setEditable(true);
        pref.setDisable(false);
        station.getStyleClass().add("procedimiento-editando");
          pref.getStyleClass().add("procedimiento-editando");
}

        selectedBranch = null;
        selectedStation = null;
       
    }

    @FXML
    private void onActionSave(ActionEvent event) throws IOException {
        // Primero desactiva todos los objetos para no poder ser midificados
      for (int i = branches.size() - 1; i >= 0; i--) {
    VBox branch = branches.get(i);
    TextField branchName = (TextField) branch.getChildren().get(0);
     TextArea branchInformation = (TextArea) branch.getChildren().get(1);
    Accordion accordion = (Accordion) branch.getChildren().get(2);
    TitledPane pane = accordion.getPanes().get(0);
    VBox buttonContainer = (VBox) pane.getContent();

    if (!branchName.getText().isBlank() && !branchInformation.getText().isBlank() ) {

        branchName.setEditable(false);
          branchName.getStyleClass().remove("procedimiento-editando");
        branchName.getStyleClass().add("procedimiento-guardado");
        
        branchInformation.setEditable(false);
        branchInformation.getStyleClass().remove("procedimiento-editando");
        branchInformation.getStyleClass().add("procedimiento-guardado");

        buttonContainer.getChildren().removeIf(n ->
              
         // El instanceof verifica sean hbox y texfield 
      (n instanceof HBox hb) && (hb.getChildren().get(0) instanceof TextField ta) && ta.getText().isBlank());

  //Igual que en modificar solo que desabilita
        // Recorre todos los nodos hijos dentro del contenedor principal
        for (Node n : buttonContainer.getChildren()) {
            // instanceof sirve para comprobar el tipo de objeto en tiempo de ejecución
          if (n instanceof HBox) {
        HBox hb = (HBox) n;

        for (Node child : hb.getChildren()) {

            if (child instanceof TextField) {
                 // Convierte el Node a TextField para poder usar sus métodos
                TextField tf = (TextField) child;
                tf.setEditable(false); // Desactiva editar el texto
                tf.getStyleClass().remove("procedimiento-editando");
                tf.getStyleClass().add("procedimiento-guardado");
            }

            if (child instanceof CheckBox) {
                CheckBox cb = (CheckBox) child;
                cb.setDisable(true);
                cb.getStyleClass().remove("procedimiento-editando");
                cb.getStyleClass().add("procedimiento-guardado");
            }
        }
    }
}
        // Alertas de informacion
        Alertas.mostrarMensajeCorrecto(LblMensaje, "Información Correcta");
        if(selectedBranch != null)
 selectedBranch.getStyleClass().remove("seleccionado");
         if(selectedStation != null)
             selectedStation.getStyleClass().remove("seleccionado");
    } else {
        Alertas.mostrarMensajeError(LblMensaje, "Información Inválida");
        
        rootBranches.getChildren().remove(branch);
        branches.remove(i);
    }
}

        // Guarda en el json 
        List<SucursalData> branchList = new ArrayList<>();
        for (VBox branch : branches) {
            TextField branchName = (TextField) branch.getChildren().get(0);
             TextArea branchInformation = (TextArea) branch.getChildren().get(1);
            Accordion accordion = (Accordion) branch.getChildren().get(2);
            TitledPane pane = accordion.getPanes().get(0);
            VBox buttonContainer = (VBox) pane.getContent();

            SucursalData sd = new SucursalData();
            sd.nombre = branchName.getText();
            sd.BranchInfo = branchInformation.getText();
            sd.estaciones = new ArrayList<>();

            for (Node n : buttonContainer.getChildren()) {
              // verifica que sea hbox
                if (n instanceof HBox h) {
                    TextField stationName = (TextField) h.getChildren().get(0);
                    CheckBox preferentialCheck = (CheckBox) h.getChildren().get(1);
                    int index = stations.indexOf(h);
                    VBox stationContent = stationContents.get(index);

                    EstacionData ed = new EstacionData();
                    ed.nombre = stationName.getText();
                    ed.preferencial = preferentialCheck.isSelected();
                    ed.tramites = new ArrayList<>();
                 
                   String rutaAudio = h.getUserData() != null ? h.getUserData().toString() : "";
                if (rutaAudio.startsWith("file:/")) {
                            rutaAudio = rutaAudio.replace("file:/", "");
                          }
                          ed.rutaAudio = rutaAudio;
                    for (Node lbl : stationContent.getChildren()) {
                        if (lbl instanceof Label l) {
                            ed.tramites.add(l.getText());
                        }
                    }
                    sd.estaciones.add(ed);
                }
            }
            branchList.add(sd);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
          Files.writeString(data.getArchivo("BranchesData").toPath(),gson.toJson(branchList));
        System.out.println("Datos guardados");
        selectedBranch = null;
    }


    // Busca por nombre de la sucursal
    private void search(String text) {
        for (VBox branch : branches) {
            TextField branchName = (TextField) branch.getChildren().get(0);
            boolean matches = branchName.getText().toLowerCase().contains(text.toLowerCase());
            branch.setVisible(matches);
            branch.setManaged(matches);
        }
    }

    // Carga del json las sucursales y estaciones 
    public void loadBranches() {
        try {
              File archivo = data.getArchivo("BranchesData");
        if (!Files.exists(archivo.toPath())) return;
        String json = Files.readString(archivo.toPath());
        if (json.isBlank()) return;
            
          
            Gson gson = new Gson();
            SucursalData[] branchArray = gson.fromJson(json, SucursalData[].class);

            for (SucursalData sd : branchArray) {
                onActionAddBranch(null);
                VBox branch = branches.get(branches.size() - 1);
                TextField branchName = (TextField) branch.getChildren().get(0);
                branchName.setText(sd.nombre);
                 TextArea branchInfo = (TextArea) branch.getChildren().get(1);
                branchInfo.setText(sd.BranchInfo);

                Accordion accordion = (Accordion) branch.getChildren().get(2);
                TitledPane pane = accordion.getPanes().get(0);
                VBox buttonContainer = (VBox) pane.getContent();
                Button addStationBtn = (Button) buttonContainer.getChildren().get(0);

           for (EstacionData ed : sd.estaciones) {
                  addStationBtn.fire();
                  HBox stationRow = (HBox) buttonContainer.getChildren().get(buttonContainer.getChildren().size() - 1);
                  TextField stationName = (TextField) stationRow.getChildren().get(0);
                  CheckBox preferentialCheck = (CheckBox) stationRow.getChildren().get(1);
                   stationName.setText(ed.nombre);
                   preferentialCheck.setSelected(ed.preferencial);
    
                    //Guarda la ruta en su respectiva estacion
             if (ed.rutaAudio != null && !ed.rutaAudio.isEmpty()) {
        stationRow.setUserData(ed.rutaAudio);         
             }  
    int index = stations.indexOf(stationRow);
    VBox stationContent = stationContents.get(index);
    for (String procedure : ed.tramites) {
        stationContent.getChildren().add(createLabel(procedure));
    }
}
                  // Desactiva los objetos
               branchName.setEditable(false);
                branchName .getStyleClass().add("procedimiento-guardado");
                branchInfo.setEditable(false);
                branchInfo .getStyleClass().add("procedimiento-guardado");
 // Igual que al modificar el instanceof se asegura de que sean del tipo
         for (Node n : buttonContainer.getChildren()) {
            if (n instanceof HBox) {
                 HBox hb = (HBox) n;

            for (Node child : hb.getChildren()) {

              if (child instanceof TextField) {
                ((TextField) child).setEditable(false);
                child .getStyleClass().add("procedimiento-guardado");
            }

               if (child instanceof CheckBox) {
                ((CheckBox) child).setDisable(true);
                child.getStyleClass().add("procedimiento-guardado");
            }
        }
    }
}
            }
        } catch (IOException e) {
        }
    }

    // Carga los tramites 
public void loadProcedures() {
    rootProcedures.getChildren().clear();
    try {
          File archivo = data.getArchivo("ProcedureData");
String json = Files.readString(archivo.toPath());
        if (!archivo.exists()) return;
        
        Gson gson = new Gson();
        DataProcedure[] procedureList = gson.fromJson(json, DataProcedure[].class);

        for (DataProcedure dp : procedureList) {
            if ((currentStationContent != null && existsInVBox(currentStationContent, dp.getName())) || !dp.getState()) {
                continue;
            }

            rootProcedures.getChildren().add(createLabel(dp.getName()));
        }

    } catch (IOException e) {   
    }
}

    // Crea el Label con el nombre del tramite 
    private Label createLabel(String text) {
        Label label = new Label(text);
         label.getStyleClass().addAll("mi-Label","label-procedimiento","mi-rectanguloProcedure");
         label.setPrefSize(Double.MAX_VALUE, 40);
         // Tamaño de texto segun tamaño de la ventana
         Scene scene = rootProcedures.getScene();
          if (scene != null) {
           label.styleProperty().bind(scene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;"));
            }
          
          // Detecta cuando lo agarran
      label.setOnDragDetected(event -> {
    Dragboard db = label.startDragAndDrop(TransferMode.MOVE);

    ClipboardContent content = new ClipboardContent();
    content.putString(label.getText());
    db.setContent(content);

    // Muestra el label mientras se arrastra
    db.setDragView(label.snapshot(null, null));

    event.consume();
});
         
      //Detecta con termina el agarrar
     label.setOnDragDone(event -> {
    if (event.getTransferMode() == TransferMode.MOVE) {

        if (label.getParent() instanceof VBox parent) {
            parent.getChildren().remove(label);
        }

    }
    event.consume();
});
        return label;
    }
 
    // Comprueba que en los tramites no exita tambien en las estacion(Evita duplicados)
    private boolean existsInVBox(VBox vbox, String text) {
        for (Node node : vbox.getChildren()) {
            if (node instanceof Label lbl && lbl.getText().equals(text)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadBranches();

        // Drag and drop que permite devolver los labels
        rootStation.setOnDragOver(event -> {
            if (event.getGestureSource() != rootStation && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        rootStation.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString() && currentStationContent != null) {
                if (!existsInVBox(currentStationContent, db.getString())) {
                    currentStationContent.getChildren().add(createLabel(db.getString()));
                }
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });

        rootProcedures.setOnDragOver(event -> {
            if (event.getGestureSource() != rootProcedures && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        rootProcedures.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                rootProcedures.getChildren().add(createLabel(db.getString()));
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            search(newVal);
        });
        
        
        // Cambia el tamaño del texto segun el tamaño de la ventana
         LblStation.sceneProperty().addListener((obs, oldScene, newScene) -> {
        if (newScene != null) {
            LblMensaje.styleProperty().bind(
                newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
            LblStation.styleProperty().bind(
                newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
            LblSyE.styleProperty().bind(
                newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
            LblTramite.styleProperty().bind(
                newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
               
        }
           });
        

// Cambia el tema segun el archivo(verifica cada segundo)
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

                // Cambia los roots
                rootBranches.getStyleClass().clear();
                rootProcedures.getStyleClass().clear();
                rootStation.getStyleClass().clear();
                rootMensaje.getStyleClass().clear();
                rootTitulos.getStyleClass().clear();
 if (t.temeDark) {
     rootBranches.getStyleClass().addAll("mi-rectangulooscuro","mi-Titulososcuros");
    rootProcedures.getStyleClass().addAll("mi-rectangulooscuro","mi-Titulososcuros");
    rootStation.getStyleClass().addAll("mi-rectangulooscuro","mi-Titulososcuros");
rootMensaje.getStyleClass().add("mi-panel-fondo1");
rootTitulos.getStyleClass().addAll("mi-rectangulooscuro","mi-Titulososcuros");


} else {

   rootBranches.getStyleClass().addAll("mi-rectangulo","mi-Titulos");
    rootProcedures.getStyleClass().addAll("mi-rectangulo","mi-Titulos");
    rootStation.getStyleClass().addAll("mi-rectangulo","mi-Titulos");
 rootMensaje.getStyleClass().add("mi-panel-fondo2");
 rootTitulos.getStyleClass().addAll("mi-rectangulo","mi-Titulos");

}
                for (VBox branch : branches) {
                    branch.getStyleClass().clear();
                    if (t.temeDark) {
                         branch.getStyleClass().add("mi-rectangulooscuro");
                    } else {
                      
                         branch.getStyleClass().add("mi-rectangulo");
                    }
                }
                for (HBox station : stations) {
                    station.getStyleClass().clear();
                    if (t.temeDark) {
                     station.getStyleClass().addAll("mi-rectangulooscuro", "mi-boton2", "label-procedimiento");
                    } else {
                     station.getStyleClass().addAll("mi-rectangulo", "mi-boton2", "label-procedimiento");
                    }
                }
            }
           //CArga los tramites cada segundo 
           loadProcedures();
        } catch (IOException ex) {
        }
    })
);
timeline.setCycleCount(Timeline.INDEFINITE);
timeline.play();
        
    }

}
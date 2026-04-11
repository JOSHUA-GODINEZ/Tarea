package cr.ac.una.tarea.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cr.ac.una.tarea.model.DataProcedure;
import cr.ac.una.tarea.model.EstacionData;
import cr.ac.una.tarea.model.SucursalData;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class BranchesStationsController implements Initializable {

    ///////////////// FXML
    @FXML
    private VBox rootBranches;
    @FXML
    private VBox rootProcedures;
    @FXML
    private TextField searchField;
    @FXML
    private VBox rootStation;
private String rutaAudio = null;

    ///////////////// VARIABLES
    private VBox selectedBranch = null;
    private HBox selectedStation = null;
    private VBox currentStationContent = null;
    private List<VBox> branches = new ArrayList<>();
    private List<HBox> stations = new ArrayList<>();
    private List<VBox> stationContents = new ArrayList<>();
    private final Path branchesFile = Path.of("Jsons/BranchesData.json");
    @FXML
    private Label LblMensaje;
    private Button btnAudio;
    @FXML
    private Label LblSyE;
    @FXML
    private Label LblTramite;
    @FXML
    private Label LblStation;


    ///////////////// ACTIONS
    @FXML
      private void onActionAddBranch(ActionEvent event) {
        TextField branchName = new TextField();
        TextArea branchInformation = new TextArea();
        Accordion accordion = new Accordion();
        TitledPane pane = new TitledPane();
        VBox buttonContainer = new VBox();
        VBox branch = new VBox();
        Button addStationBtn = new Button();
        
       branchInformation.setMaxSize(Double.MAX_VALUE, 50);
        addStationBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        VBox.setVgrow(addStationBtn, Priority.ALWAYS);
       // branch.setStyle("-fx-border-color: blue;");
        pane.setText("Estaciones");
        addStationBtn.getStyleClass().add("mi-botonAdd");
        buttonContainer.getChildren().add(addStationBtn);
        buttonContainer.setSpacing(10);
        buttonContainer.getStyleClass().add("mi-rectangulo");
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        pane.setContent(buttonContainer);     
        accordion.getPanes().add(pane);
        //accordion.getStyleClass().add("mi-rectangulo");
        branch.getChildren().add(branchName);
        branch.getChildren().add(branchInformation);
        branch.getChildren().add(accordion);
        branch.setPadding(new Insets(30, 20, 30, 20));
        branch.getStyleClass().add("mi-rectangulo");
        rootStation.getChildren().add(new Label(""));
        rootBranches.getChildren().add(branch);
        branches.add(branch);

        ///////////////// ADD STATION
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
            PreferIMag.setFitHeight(40);
            PreferIMag.setFitWidth(40);
           
            PreferIMag.getStyleClass().add("mi-IMagepreferencial");
                stationRow.getChildren().addAll(stationName, preferentialCheck, PreferIMag);
                stationRow.setSpacing(10);
                stationRow.setAlignment(Pos.CENTER);
                
                stationRow.getStyleClass().addAll("mi-rectangulo","mi-boton2");
                stationRow.setMinHeight(70);
               
                stationRow.setOnMouseClicked(station -> {
                    if (selectedStation != null) {
         selectedStation.getStyleClass().remove("seleccionado");
    }
                    stationRow.getStyleClass().add("seleccionado");
                    selectedStation = stationRow;
                    currentStationContent = stationContent;
                    loadProcedures();
                     
                  
                 
                    ///////////////// PREVIEW
                    VBox preview = new VBox();
                    Button audio = new Button();
                    Button playAudio = new Button();
                    Button deleteAudio = new Button();
                    HBox audios = new HBox();
                   Label nameLabel = new Label(stationName.getText());
 Scene scene = rootStation.getScene();
    if (scene != null) {
        nameLabel.styleProperty().bind(
            scene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
        );
      
    }
                         
                   // stationContent.setStyle("-fx-border-color: blue;");
                   stationContent.setSpacing(10);
                    stationContent.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                    VBox.setVgrow(stationContent, Priority.ALWAYS);
                    stationContent.setAlignment(Pos.TOP_CENTER);
                    preview.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                    VBox.setVgrow(preview, Priority.ALWAYS);

                   
                    preview.setSpacing(10);
                    preview.setAlignment(Pos.CENTER);
                 //   preview.setStyle("-fx-border-color: blue;");
                    preview.setMinHeight(70);
                   // preview.getStyleClass().add("mi-panel-fondo");
                   
                   
                   
                   
                   
                   
                  Label rutaLabel = new Label("Sin audio");
                      if (stationRow.getUserData() != null) {
        rutaLabel.setText(new File(stationRow.getUserData().toString()).getName());
    } else {
        rutaLabel.setText("Sin audio");
    }
                 audio.setOnMouseClicked(clickAudio -> {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Seleccionar audio");
    fileChooser.getExtensionFilters().add(
        new FileChooser.ExtensionFilter("Audio", "*.mp3", "*.wav", "*.aac")
    );
    Stage stage = (Stage) stationRow.getScene().getWindow();
    File archivo = fileChooser.showOpenDialog(stage);
    if (archivo != null) {
       stationRow.setUserData(archivo.getAbsolutePath());
         rutaLabel.setText(archivo.getName());
    }
   
});

              playAudio.setOnMouseClicked(clickPlay -> {
    String rutaAudio = stationRow.getUserData() != null ? stationRow.getUserData().toString() : "";
    
    if (rutaAudio.isEmpty()) {
        System.out.println("No has seleccionado un audio");
        return;
    }
    
    String ruta1 = new File(rutaAudio).toURI().toString();
    javafx.scene.media.Media media = new javafx.scene.media.Media(ruta1);
    javafx.scene.media.MediaPlayer player = new javafx.scene.media.MediaPlayer(media);
    player.setOnReady(() -> player.play());
});

              deleteAudio.setOnMouseClicked(clickDelete -> {
    stationRow.setUserData(null);
    rutaLabel.setText("Sin audio");// ✅ elimina la ruta
    System.out.println("Audio eliminado");
});
 
                
                
         audios.getChildren().addAll(audio,rutaLabel,playAudio ,deleteAudio); // ✅ ambos botones, sintaxis correcta
       preview.getChildren().addAll( audios ,nameLabel, stationContent); // ✅ audios incluido
    rootStation.getChildren().clear();
          rootStation.getChildren().add(preview);
                });

                buttonContainer.getChildren().add(stationRow);
                stationRow.sceneProperty().addListener((obs, oldScene, newScene) -> {
    if (newScene != null) {
        stationName.styleProperty().bind(
            newScene.widthProperty().multiply(0.013).asString("-fx-font-size: %.2fpx;")
        );
     /*   nameLabel.styleProperty().bind(
            newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
        );
        rutaLabel.styleProperty().bind(
            newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
        );*/
    }
});
            }
        });

        branch.setOnMouseClicked(e -> {
            selectedBranch = branch;
        });
        
        
        // ✅ después de crear branchName y branchInformation
Scene scene = rootBranches.getScene();

if (scene != null) {
    branchName.styleProperty().bind(
        scene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
    );
    branchInformation.styleProperty().bind(
        scene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
    );
} else {
    rootBranches.sceneProperty().addListener((obs, oldScene, newScene) -> {
        if (newScene != null) {
            branchName.styleProperty().bind(
                newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
            branchInformation.styleProperty().bind(
                newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
        }
    });
}
       
            
        
    }

    @FXML
    private void onActionDelete(ActionEvent event) {
        if (selectedBranch != null) {
          /*  Accordion accordion = (Accordion) selectedBranch.getChildren().get(1);
            TitledPane pane = accordion.getPanes().get(0);
            VBox buttonContainer = (VBox) pane.getContent();

            for (Node n : buttonContainer.getChildren()) {
                if (n instanceof HBox hb) {
                    int index = stations.indexOf(hb);
                    if (index != -1) {
                        stations.remove(index);
                        stationContents.remove(index);
                    }
                }
            }*/
            rootBranches.getChildren().remove(selectedBranch);
            branches.remove(selectedBranch);
        }

        if (selectedStation != null) {
            int index = stations.indexOf(selectedStation);
            if (index != -1) {
                stations.remove(index);
                stationContents.remove(index);
            }
            ((Pane) selectedStation.getParent()).getChildren().remove(selectedStation);
        }

        selectedBranch = null;
        selectedStation = null;
    }

    @FXML
    private void onActionEdit(ActionEvent event) {
       if (selectedBranch != null) {

    TextField branchName = (TextField) selectedBranch.getChildren().get(0);
    branchName.setEditable(true);
    branchName.setOpacity(1);
    
        TextArea branchInformation = (TextArea) selectedBranch.getChildren().get(1);
    branchInformation.setEditable(true);
    branchInformation.setOpacity(1);
    

    Accordion accordion = (Accordion) selectedBranch.getChildren().get(2);
    TitledPane pane = accordion.getPanes().get(0);
    VBox buttonContainer = (VBox) pane.getContent();

    for (Node n : buttonContainer.getChildren()) {
        if (n instanceof HBox) {
            HBox hb = (HBox) n;

            for (Node child : hb.getChildren()) {

                if (child instanceof TextField) {
                    ((TextField) child).setEditable(true);
                    child.setOpacity(1);
                }

                if (child instanceof CheckBox) {
                    ((CheckBox) child).setDisable(false);
                    child.setOpacity(1);
                }
            }
        }
    }

    selectedBranch = null;
}
    }

    @FXML
    private void onActionSave(ActionEvent event) throws IOException {
        ///////////////// VALIDATE AND DISABLE
      for (int i = branches.size() - 1; i >= 0; i--) {
    VBox branch = branches.get(i);
    TextField branchName = (TextField) branch.getChildren().get(0);
     TextArea branchInformation = (TextArea) branch.getChildren().get(1);
    Accordion accordion = (Accordion) branch.getChildren().get(2);
    TitledPane pane = accordion.getPanes().get(0);
    VBox buttonContainer = (VBox) pane.getContent();

    if (!branchName.getText().isBlank() && !branchInformation.getText().isBlank() ) {

        branchName.setEditable(false);
        branchName.setOpacity(0.8);
       
        branchInformation.setEditable(false);
        branchInformation.setOpacity(0.8);
        
        buttonContainer.getChildren().removeIf(n ->
            (n instanceof HBox hb) &&
            (hb.getChildren().get(0) instanceof TextField ta) &&
            ta.getText().isBlank()
        );

        // 🔥 Aquí reemplazas el disable
        for (Node n : buttonContainer.getChildren()) {
    if (n instanceof HBox) {
        HBox hb = (HBox) n;

        for (Node child : hb.getChildren()) {

            if (child instanceof TextField) {
                TextField tf = (TextField) child;
                tf.setEditable(false);
                tf.setOpacity(0.8); // opcional
            }

            if (child instanceof CheckBox) {
                CheckBox cb = (CheckBox) child;
                cb.setDisable(true);
                cb.setOpacity(0.8); // opcional visual
            }
        }
    }
}
        mostrarMensajeCorrecto("Informacion Guardada");

    } else {
        mostrarMensajeError("Informacion Insuficiente");
        rootBranches.getChildren().remove(branch);
        branches.remove(i);
    }
}

        ///////////////// SAVE TO JSON
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
                if (n instanceof HBox h) {
                    TextField stationName = (TextField) h.getChildren().get(0);
                    CheckBox preferentialCheck = (CheckBox) h.getChildren().get(1);
                    int index = stations.indexOf(h);
                    VBox stationContent = stationContents.get(index);

                    EstacionData ed = new EstacionData();
                    ed.nombre = stationName.getText();
                    ed.preferencial = preferentialCheck.isSelected();
                    ed.tramites = new ArrayList<>();
                     ed.rutaAudio = h.getUserData() != null ? h.getUserData().toString() : "";
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
        Files.writeString(branchesFile, gson.toJson(branchList));
        System.out.println("Datos guardados");
        selectedBranch = null;
    }

    @FXML
    private void onActionRefresh(ActionEvent event) {
        loadProcedures();
    }

    ///////////////// SEARCH
    private void search(String text) {
        for (VBox branch : branches) {
            TextField branchName = (TextField) branch.getChildren().get(0);
            boolean matches = branchName.getText().toLowerCase().contains(text.toLowerCase());
            branch.setVisible(matches);
            branch.setManaged(matches);
        }
    }

    ///////////////// LOAD
    public void loadBranches() {
        try {
            if (!Files.exists(branchesFile)) return;
            String json = Files.readString(branchesFile);
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
    
    // ✅ cargar ruta de audio
    if (ed.rutaAudio != null && !ed.rutaAudio.isEmpty()) {
        stationRow.setUserData(ed.rutaAudio);           // ✅ guarda la ruta en el HBox
        // actualizar el label — buscar rutaLabel en el preview no es fácil,
        // el label se actualizará cuando el usuario haga click en la estación
    }
    
    int index = stations.indexOf(stationRow);
    VBox stationContent = stationContents.get(index);
    for (String procedure : ed.tramites) {
        stationContent.getChildren().add(createLabel(procedure));
    }
}

               branchName.setEditable(false);
                branchName.setOpacity(0.8);
                branchInfo.setEditable(false);
                branchInfo.setOpacity(0.8);

// recorrer estaciones
for (Node n : buttonContainer.getChildren()) {
    if (n instanceof HBox) {
        HBox hb = (HBox) n;

        for (Node child : hb.getChildren()) {

            if (child instanceof TextField) {
                ((TextField) child).setEditable(false);
                child.setOpacity(0.8);
            }

            if (child instanceof CheckBox) {
                ((CheckBox) child).setDisable(true);
                child.setOpacity(0.8);
            }
        }
    }
}
            }
        } catch (IOException e) {
            System.out.println("Error al cargar: " + e.getMessage());
        }
    }

    public void loadProcedures() {
        rootProcedures.getChildren().clear();
        try {
            Path proceduresFile = Paths.get("Jsons/ProcedureData.json");
            if (!Files.exists(proceduresFile)) return;
            String json = Files.readString(proceduresFile);
            if (json.isBlank()) return;

            Gson gson = new Gson();
            DataProcedure[] procedureList = gson.fromJson(json, DataProcedure[].class);

            for (DataProcedure dp : procedureList) {
                if (currentStationContent != null && existsInVBox(currentStationContent, dp.getName()) || dp.getState().equals(false) ) {
                    continue;
                }
                
                rootProcedures.getChildren().add(createLabel(dp.getName()));
            }
        } catch (IOException e) {
            System.out.println("Error al cargar procedimientos: " + e.getMessage());
        }
    }

    ///////////////// HELPERS
    private Label createLabel(String text) {
        Label label = new Label(text);
         label.getStyleClass().add("mi-Label");
         label.setPrefSize(Double.MAX_VALUE, 40);
         label.setAlignment(Pos.CENTER);
          Scene scene = rootProcedures.getScene();
          if (scene != null) {
                label.styleProperty().bind(
                    scene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
                );
            }
      label.setOnDragDetected(event -> {
    Dragboard db = label.startDragAndDrop(TransferMode.MOVE);

    ClipboardContent content = new ClipboardContent();
    content.putString(label.getText());
    db.setContent(content);

    // 🔥 Esto muestra el label mientras lo arrastras
    db.setDragView(label.snapshot(null, null));

    event.consume();
});

        label.setOnDragDone(event -> {
            if (event.getTransferMode() == TransferMode.MOVE) {
                ((VBox) label.getParent()).getChildren().remove(label);
            }
            event.consume();
        });
        return label;
    }

    private boolean existsInVBox(VBox vbox, String text) {
        for (Node node : vbox.getChildren()) {
            if (node instanceof Label lbl && lbl.getText().equals(text)) {
                return true;
            }
        }
        return false;
    }

    
        private void mostrarMensajeError(String mensaje) {
    LblMensaje.setText(mensaje);
    LblMensaje.setVisible(true);
 //   LblMensaje.setStyle("-fx-text-fill: red;");
    
    PauseTransition pause = new PauseTransition(Duration.seconds(2));
    pause.setOnFinished(e -> LblMensaje.setVisible(false));
    pause.play();
}
     private void mostrarMensajeCorrecto(String mensaje) {
    LblMensaje.setText(mensaje);
    LblMensaje.setVisible(true);
    //LblMensaje.setStyle("-fx-text-fill: green;");
    
    PauseTransition pause = new PauseTransition(Duration.seconds(2));
    pause.setOnFinished(e -> LblMensaje.setVisible(false));
    pause.play();
}
    
    
    
    
    
    
    ///////////////// INITIALIZE
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadProcedures();
        loadBranches();

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
        
        
        
    }

    private void onActionAudio(ActionEvent event) {
          FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Seleccionar audio");
    fileChooser.getExtensionFilters().add(
        new FileChooser.ExtensionFilter("Audio", "*.mp3", "*.wav", "*.aac")
    );
    
    Stage stage = (Stage) btnAudio.getScene().getWindow();
    File archivo = fileChooser.showOpenDialog(stage);
    
    if (archivo != null) {
        rutaAudio = archivo.getAbsolutePath(); 
        System.out.println("Seleccionaste: " + rutaAudio);
    }
    }

    private void onActionReproducir(ActionEvent event) {
        if (rutaAudio == null) {
        System.out.println("No has seleccionado un audio");
        return;
    }
    
    String ruta = new File(rutaAudio).toURI().toString();
    javafx.scene.media.Media media = new javafx.scene.media.Media(ruta);
    javafx.scene.media.MediaPlayer player = new javafx.scene.media.MediaPlayer(media);
    player.setOnReady(() -> player.play());
    }
}
package cr.ac.una.tarea.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cr.ac.una.tarea.model.DataProcedure;
import cr.ac.una.tarea.model.EstacionData;
import cr.ac.una.tarea.model.SucursalData;
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
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
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


    ///////////////// ACTIONS
    @FXML
      private void onActionAddBranch(ActionEvent event) {
        TextField branchName = new TextField();
        Accordion accordion = new Accordion();
        TitledPane pane = new TitledPane();
        VBox buttonContainer = new VBox();
        VBox branch = new VBox();
        Button addStationBtn = new Button();
        
       
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
        branch.getChildren().add(accordion);
        branch.setPadding(new Insets(30, 20, 30, 20));
        branch.getStyleClass().addAll("mi-rectangulo");
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

                stations.add(stationRow);
                stationContents.add(stationContent);

                stationRow.getChildren().addAll(stationName, preferentialCheck);
                stationRow.setSpacing(30);
                stationRow.setAlignment(Pos.CENTER);
                stationRow.getStyleClass().add("mi-rectangulo");
                stationRow.setMinHeight(70);
               
                stationRow.setOnMouseClicked(ev -> {
                    selectedStation = stationRow;
                    currentStationContent = stationContent;
                    loadProcedures();

                    ///////////////// PREVIEW
                    VBox preview = new VBox();
                   Label nameLabel = new Label(stationName.getText());

                         
                   // stationContent.setStyle("-fx-border-color: blue;");
                   stationContent.setSpacing(10);
                    stationContent.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                    VBox.setVgrow(stationContent, Priority.ALWAYS);
                    stationContent.setAlignment(Pos.TOP_CENTER);
                    preview.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                    VBox.setVgrow(preview, Priority.ALWAYS);

                    preview.getChildren().addAll(nameLabel, stationContent);
                    preview.setSpacing(10);
                    preview.setAlignment(Pos.CENTER);
                 //   preview.setStyle("-fx-border-color: blue;");
                    preview.setMinHeight(70);
                   // preview.getStyleClass().add("mi-panel-fondo");
                    rootStation.getChildren().clear();
                    rootStation.getChildren().add(preview);
                });

                buttonContainer.getChildren().add(stationRow);
            }
        });

        branch.setOnMouseClicked(e -> {
            selectedBranch = branch;
        });
    }

    @FXML
    private void onActionDelete(ActionEvent event) {
        if (selectedBranch != null) {
            Accordion accordion = (Accordion) selectedBranch.getChildren().get(1);
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
            }
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

    Accordion accordion = (Accordion) selectedBranch.getChildren().get(1);
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
    Accordion accordion = (Accordion) branch.getChildren().get(1);
    TitledPane pane = accordion.getPanes().get(0);
    VBox buttonContainer = (VBox) pane.getContent();

    if (!branchName.getText().isBlank()) {

        branchName.setEditable(false);
        branchName.setOpacity(0.8);

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
            Accordion accordion = (Accordion) branch.getChildren().get(1);
            TitledPane pane = accordion.getPanes().get(0);
            VBox buttonContainer = (VBox) pane.getContent();

            SucursalData sd = new SucursalData();
            sd.nombre = branchName.getText();
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

                Accordion accordion = (Accordion) branch.getChildren().get(1);
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

                    int index = stations.indexOf(stationRow);
                    VBox stationContent = stationContents.get(index);
                    for (String procedure : ed.tramites) {
                        stationContent.getChildren().add(createLabel(procedure));
                    }
                }

               branchName.setEditable(false);
branchName.setOpacity(0.8);

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
        label.setOnDragDetected(event -> {
            Dragboard db = label.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(label.getText());
            db.setContent(content);
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
    LblMensaje.setStyle("-fx-text-fill: red;");
    
    PauseTransition pause = new PauseTransition(Duration.seconds(2));
    pause.setOnFinished(e -> LblMensaje.setVisible(false));
    pause.play();
}
     private void mostrarMensajeCorrecto(String mensaje) {
    LblMensaje.setText(mensaje);
    LblMensaje.setVisible(true);
    LblMensaje.setStyle("-fx-text-fill: green;");
    
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
    }
}
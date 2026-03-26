package cr.ac.una.tarea.controller;

import cr.ac.una.tarea.model.DataProcedure;
import cr.ac.una.tarea.util.JsonUtil;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
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

public class BranchesStationsController implements Initializable {
    @FXML
    private VBox rootBranches;
     private VBox selec = null;
     private HBox selec1 = null;
    
     private List<VBox> data= new ArrayList<>();
    @FXML
    private VBox rootPresures;

    @FXML
    private TextField Buscador1;

    @FXML
    private VBox rootEstacion;
    private List<VBox> vHijos = new ArrayList<>();

private VBox vHijoActual = null;
 @FXML
private void onActionSucursal(ActionEvent event) {
    TextField t = new TextField();
    Accordion box = new Accordion();
    TitledPane p1 = new TitledPane();

    VBox v = new VBox();
    VBox vox = new VBox();
    vox.setStyle("-fx-border-color: blue;");
    Button b = new Button("Agregar Estacion");
   
    p1.setText("Estaciones");
      v.getChildren().add(b);
     v.setSpacing(10);
    p1.setContent(v);
    box.getPanes().add(p1);
    vox.getChildren().add(t);
    vox.getChildren().add(box);
    vox.setPadding(new Insets(30,20,30,20));
      Label le = new Label("");
    rootEstacion.getChildren().add(le);
    rootBranches.getChildren().add(vox);
   data.add(vox);

    b.setOnAction(new EventHandler<ActionEvent>() {
    @Override
    public void handle(ActionEvent e) {
        TextField ta = new TextField();
        HBox h = new HBox();
        CheckBox check = new CheckBox();
        VBox vHijo = new VBox(); // <-- se crea una sola vez aqui

        h.getChildren().add(ta);
        h.getChildren().add(check);
        h.setSpacing(30);
        h.setAlignment(Pos.CENTER);
        h.setStyle("-fx-border-color: blue;");
        h.setMinHeight(70);

        h.setOnMouseClicked(ev -> {
            selec1 = h;
            vHijoActual = vHijo;
                cargarDatos();
           vHijos.add(vHijo);
            VBox hCopia = new VBox();
            Label taCopia = new Label();

            taCopia.setText(ta.getText());

            vHijo.setStyle("-fx-border-color: blue;");
            vHijo.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            VBox.setVgrow(vHijo, Priority.ALWAYS);

            hCopia.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            VBox.setVgrow(hCopia, Priority.ALWAYS);

            hCopia.getChildren().add(taCopia);
            hCopia.getChildren().add(vHijo); // <-- siempre el mismo vHijo con sus labels
            hCopia.setSpacing(30);
            hCopia.setAlignment(Pos.CENTER);
            hCopia.setStyle("-fx-border-color: blue;");
            hCopia.setMinHeight(70);

            rootEstacion.getChildren().clear();
            rootEstacion.getChildren().add(hCopia);

            System.out.println("Seleccionado");
        });
        v.getChildren().add(h);
    }
});
    vox.setOnMouseClicked(e->{
         selec=vox;
      });   
}
     @FXML
    private void onActionEliminate(ActionEvent event) {  
        if(selec != null){    
      rootBranches.getChildren().remove(selec); 
        }
       if (selec1 != null) {
    ((Pane) selec1.getParent()).getChildren().remove(selec1);
          
} 
        selec = null;
        selec1=null;
    }

     @FXML
   private void onActionModific(ActionEvent event) {
    if (selec != null) {
        selec.setDisable(false);
  
        for (Node n : selec.getChildren()) {
            n.setDisable(false);
        }
        Accordion acc = (Accordion) selec.getChildren().get(1);
        TitledPane pane = acc.getPanes().get(0);
        VBox v = (VBox) pane.getContent();

        v.setDisable(false);

        for (Node n : v.getChildren()) {
            n.setDisable(false);
        }
        selec = null;
    }
}
    
    
    
    
    
    
@FXML
private void onActionSave(ActionEvent event) {
        
    for (int i = data.size() - 1; i >= 0; i--) {
        VBox vox = data.get(i);
        TextField nombreField = (TextField) vox.getChildren().get(0);
        Accordion acc = (Accordion) vox.getChildren().get(1);
        TitledPane pane = acc.getPanes().get(0);
        VBox v = (VBox) pane.getContent();

        if (!nombreField.getText().isBlank()) {
            nombreField.setDisable(true);
            v.getChildren().removeIf(n ->
                (n instanceof HBox hb) &&
                (hb.getChildren().get(0) instanceof TextField ta) &&
                ta.getText().isBlank()
            );
            v.setDisable(true);
        } else {
            rootBranches.getChildren().remove(vox);
            data.remove(i);
        }
    }
    selec = null;
}
 
private void buscar(String texto){
    int y = 10;
    for(int i = 0; i < data.size(); i++){
        VBox h = data.get(i);
        TextField campo = (TextField) h.getChildren().get(0);

        if(campo.getText().toLowerCase().contains(texto.toLowerCase())){
            h.setVisible(true);
            h.setManaged(true);
            h.setLayoutY(y);
            y += 40;

        }else{
            h.setVisible(false);
            h.setManaged(false);
        }
    }
}

public void cargarDatos() {
    rootPresures.getChildren().clear(); 
    List<DataProcedure> lista = JsonUtil.leerJson(Paths.get("ProcedureData.json"));
    for (DataProcedure dp : lista) {
        if (vHijoActual != null && existeEnVBox(vHijoActual, dp.getName())) {
            continue;
        }
        Label lbl = crearLabel(dp.getName());
        lbl.setOnDragDetected(event -> {
            Dragboard db = lbl.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(lbl.getText()); 
            db.setContent(content);
            event.consume();
        });
        lbl.setOnDragDone(event -> {
            if (event.getTransferMode() == TransferMode.MOVE) {
                ((VBox) lbl.getParent()).getChildren().remove(lbl);
            }
            event.consume();
        });   
        rootPresures.getChildren().add(lbl);
    }
}
@Override
    public void initialize(URL url, ResourceBundle rb) {
    cargarDatos();
        rootEstacion.setOnDragOver(event -> {
    if (event.getGestureSource() != rootEstacion && event.getDragboard().hasString()) {
        event.acceptTransferModes(TransferMode.MOVE);
    }
    event.consume();
});

rootEstacion.setOnDragDropped(event -> {
    Dragboard db = event.getDragboard();
    boolean success = false;

    if (db.hasString() && vHijoActual != null) {
        if (!existeEnVBox(vHijoActual, db.getString())) {
            Label nuevo = crearLabel(db.getString());
            vHijoActual.getChildren().add(nuevo);
        }
        success = true;
    }
    event.setDropCompleted(success);
    event.consume();
});
  rootPresures.setOnDragOver(event -> {
    if (event.getGestureSource() != rootPresures && event.getDragboard().hasString()) {
        event.acceptTransferModes(TransferMode.MOVE);
    }
    event.consume();
});

rootPresures.setOnDragDropped(event -> {
    Dragboard db = event.getDragboard();
    boolean success = false;

    if (db.hasString()) {
        Label nuevo = crearLabel(db.getString());
        rootPresures.getChildren().add(nuevo);
        success = true;
    }
    event.setDropCompleted(success);
    event.consume();
});

      Buscador1.textProperty().addListener((obs, viejo, nuevo) -> {
        buscar(nuevo);
    });
    }    

    @FXML
    private void onActionActualiza(ActionEvent event) {
        cargarDatos();
    }
 private Label crearLabel(String texto) {
    Label lbl = new Label(texto);

    lbl.setOnDragDetected(event -> {
        Dragboard db = lbl.startDragAndDrop(TransferMode.MOVE);

        ClipboardContent content = new ClipboardContent();
        content.putString(lbl.getText());

        db.setContent(content);

        event.consume();
    });

    lbl.setOnDragDone(event -> {
        if (event.getTransferMode() == TransferMode.MOVE) {
            ((VBox) lbl.getParent()).getChildren().remove(lbl);
        }
        event.consume();
    });
    return lbl;
}

 private boolean existeEnVBox(VBox vbox, String texto) {
    for (Node node : vbox.getChildren()) {
        if (node instanceof Label lbl) {
            if (lbl.getText().equals(texto)) {
                return true;
            }
        }
    }
    return false;
}
 private boolean existeEnAlgunVHijo(String texto) {
    for (VBox vh : vHijos) {
        if (existeEnVBox(vh, texto)) {
            return true;
        }
    }
    return false;
}
 
}

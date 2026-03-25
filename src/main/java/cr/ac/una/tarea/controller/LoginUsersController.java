package cr.ac.una.tarea.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class LoginUsersController implements Initializable {
    @FXML
    private VBox rootUsers;
   private List<HBox> data= new ArrayList<>();
     private HBox seleccionado = null;
    @FXML
    private TextField buscar;
    
    @FXML
    private void onActionAdd(ActionEvent event) {
       HBox h = new HBox();       
        TextField t = new TextField();
        TextField t1 = new TextField();
        TextField t2 = new TextField();
        DatePicker date = new DatePicker();
        ImageView image =new ImageView("file:/C:/Joshua/Tarea1-Logos/Inicial.png");
        image.setFitHeight(50);
        image.setFitWidth(50);
        
    t.setAlignment(Pos.CENTER_LEFT);
      
       t.setMaxWidth(Double.MAX_VALUE);
      t1.setMaxWidth(Double.MAX_VALUE);
      t2.setMaxWidth(Double.MAX_VALUE);
      date.setMaxWidth(Double.MAX_VALUE);

        HBox.setHgrow(t, Priority.ALWAYS);
         HBox.setHgrow(t1, Priority.ALWAYS);
          HBox.setHgrow(t2, Priority.ALWAYS);
           HBox.setHgrow(date, Priority.ALWAYS);
            HBox.setHgrow(image, Priority.ALWAYS);

        h.setPadding(new Insets(0,40,0,20));
        h.setSpacing(60);
        h.setMaxHeight(70);
        h.getChildren().add(t);
        h.getChildren().add(t1);
        h.getChildren().add(t2);
        h.getChildren().add(date);
         h.getChildren().add(image);
         h.setStyle("-fx-border-color: blue;");
         h.setAlignment(Pos.CENTER);
        data.add(h);
         rootUsers.setSpacing(20);
        rootUsers.getChildren().add(h);
 
        h.setOnMouseClicked(e->{
        seleccionado=h;
        });   
    }

    @FXML
    private void onActiondelete(ActionEvent event) {
        if(seleccionado!=null){
         rootUsers.getChildren().remove(seleccionado);
            data.remove(seleccionado);
            seleccionado=null;
        }
    }

    @FXML
    private void onActionmofic(ActionEvent event) {
       if(seleccionado!=null){ 
               TextField nombre = (TextField) seleccionado.getChildren().get(0);
       TextField cedula = (TextField) seleccionado.getChildren().get(1);
       TextField numero = (TextField) seleccionado.getChildren().get(2);
       DatePicker d = (DatePicker) seleccionado.getChildren().get(3);
       ImageView ima = (ImageView) seleccionado.getChildren().get(4);
       
           nombre.setDisable(false);
          cedula.setDisable(false);
          numero.setDisable(false);
          numero.setDisable(false);
          d.setDisable(false);
          ima.setDisable(false); 
          
       seleccionado=null;
        } 
    }

    @FXML
    private void onActionSave(ActionEvent event) {
         for (int i = data.size() - 1; i >= 0; i--) {
        HBox h = data.get(i);   
       TextField nombre = (TextField) h.getChildren().get(0);
       TextField cedula = (TextField) h.getChildren().get(1);
       TextField numero = (TextField) h.getChildren().get(2);
       DatePicker d = (DatePicker) h.getChildren().get(3);
       ImageView ima = (ImageView) h.getChildren().get(4);

        if (!nombre.getText().isBlank() &&!cedula.getText().isBlank() && d.getValue() != null &&  !ima.getImage().getUrl().equals("file:/Joshua/Tarea1-Logos/Inicial.png")) {
          nombre.setDisable(true);
          cedula.setDisable(true);
          numero.setDisable(true);
          numero.setDisable(true);
          d.setDisable(true);
          ima.setDisable(true);        
        }
        else{
         rootUsers.getChildren().remove(h);
            data.remove(i);
        }
         }
         seleccionado=null;
    }

    private void buscar(String texto){
    int y = 10;
    for(int i = 0; i < data.size(); i++){

        HBox h = data.get(i);
        TextField campo = (TextField) h.getChildren().get(0);
        TextField campo1 = (TextField) h.getChildren().get(1);
        if(campo.getText().toLowerCase().contains(texto.toLowerCase())||campo1.getText().toLowerCase().contains(texto.toLowerCase())){
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
        @Override
    public void initialize(URL url, ResourceBundle rb) {
       buscar.textProperty().addListener((obs, viejo, nuevo) -> {
        buscar(nuevo);
    });
    } 
    
}

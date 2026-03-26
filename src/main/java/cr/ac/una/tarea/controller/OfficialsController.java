package cr.ac.una.tarea.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Tab;

public class OfficialsController implements Initializable {

    @FXML
    private Tab tab1;
    @FXML
    private Tab tab2;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
     
        
         try {
            FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/cr/ac/una/tarea/view/ManagementTokensView.fxml"));
            Parent root1 = loader1.load();
            tab1.setContent(root1);

            FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/cr/ac/una/tarea/view/LoginUsersView.fxml"));
            Parent root2 = loader2.load();
            tab2.setContent(root2);
            

        } catch (IOException e) {
         
            
        }
        
    }    
    
}

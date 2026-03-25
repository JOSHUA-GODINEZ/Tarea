package cr.ac.una.tarea.controller;
import cr.ac.una.tarea.App;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Tab;

public class AdministratorController implements Initializable {

    @FXML
    private Tab tab1;

    @FXML
    private Tab tab2;
    
    @FXML
    private Tab tab3;
    @FXML
    private Tab tab4;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/cr/ac/una/tarea/view/ProcedureView.fxml"));
            Parent root1 = loader1.load();
            tab1.setContent(root1);

            FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/cr/ac/una/tarea/view/BranchesStationsView.fxml"));
            Parent root2 = loader2.load();
            tab2.setContent(root2);
            
            FXMLLoader loader3 = new FXMLLoader(getClass().getResource("/cr/ac/una/tarea/view/LoginUsersView.fxml"));
            Parent root3 = loader3.load();
            tab3.setContent(root3);
            
             FXMLLoader loader4 = new FXMLLoader(getClass().getResource("/cr/ac/una/tarea/view/TopUsersView.fxml"));
            Parent root4 = loader4.load();
            tab4.setContent(root4);

        } catch (IOException e) {
         
            
        }
    }

    @FXML
    private void onActionOption(ActionEvent event) throws IOException {
        App.setRoot("LoginView");
    }
}

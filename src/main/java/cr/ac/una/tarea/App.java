package cr.ac.una.tarea;
import cr.ac.una.tarea.controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List; 

public class App extends Application {
    private static Scene scene;
    LoginController l = new LoginController();

    @Override
    public void start(Stage stage) throws IOException {
        List<String> params = getParameters().getRaw();
        String param = params.isEmpty() ? "login" : params.get(0);

        String vista = switch (param) {
            case "officials" -> "OfficialsView";
            case "administrador"  -> "AdministratorView";
            default          -> l.FullParameters();
        };

        scene = new Scene(loadFXML(vista), 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
            App.class.getResource("/cr/ac/una/tarea/view/" + fxml + ".fxml")
        );
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
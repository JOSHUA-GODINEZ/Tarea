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

    @Override
    public void start(Stage stage) throws IOException {

        List<String> params = getParameters().getRaw();
        String param = params.isEmpty() ? "LoginView" : params.get(0);

        LoginController l = new LoginController(); // lo dejamos

        String vista;

        switch (param.toLowerCase()) {
            case "officials":
                vista = "OfficialsView";
                break;
            case "administrador":
               // vista = "LoginView";
                vista = l.FullParameters();
                break;
                   case "kiosko":
                vista = "";
                break;
                   case "proyeccion":
                vista = "ProjectionView";
                break;
            default:
                vista = l.FullParameters(); // 👈 importante
                break;
        }

        System.out.println("Vista seleccionada: " + vista);

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
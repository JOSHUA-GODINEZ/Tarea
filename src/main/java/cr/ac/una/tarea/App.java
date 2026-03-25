package cr.ac.una.tarea;
import cr.ac.una.tarea.controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List; // <-- agregar este import

public class App extends Application {
    private static Scene scene;
    LoginController l= new LoginController();

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("LoginView"), 800, 600);

        // <-- NUEVO: leer el parametro
        List<String> params = getParameters().getRaw();
        String param = params.isEmpty() ? "l" : params.get(0);

        if (param.equals("u")) {
            scene.setRoot(loadFXML("LoginUsersView")); // abre usuarios
        } else {
            String vista= l.verificarDatos1(); // tu codigo original
            scene.setRoot(loadFXML(vista));
        }

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
        launch(args); // <-- solo se agrega args aquí
    }
}
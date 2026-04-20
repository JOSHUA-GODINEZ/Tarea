package cr.ac.una.tarea.util;

import javafx.animation.PauseTransition;
import javafx.scene.control.Label;
import javafx.util.Duration;

//  Muestra Los mensajes de avisos por 2 segundos en pantalla
public class Alertas {

    public static void mostrarMensajeError(Label label, String mensaje) {
        label.setText(mensaje);
        label.setVisible(true);
        label.getStyleClass().add("mensaje-error");

        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> label.setVisible(false));
        pause.play();
    }

    public static void mostrarMensajeCorrecto(Label label, String mensaje) {
        label.setText(mensaje);
        label.setVisible(true);
        label.getStyleClass().add("mensaje-correcto");

        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> label.setVisible(false));
        pause.play();
    }
}

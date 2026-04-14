package cr.ac.una.tarea.util;

import javafx.scene.control.TextField;

public class ValidadorNumeros {

    public static void soloNumeros(TextField campo) {
        campo.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                campo.setText(newVal.replaceAll("[^\\d]", ""));
            }
        });
    }
}

package cr.ac.una.tarea.util;

import javafx.scene.control.TextField;

public class ValidadorNumeros {

   public static void soloNumeros4Digitos(TextField campo) {
    campo.textProperty().addListener((obs, oldVal, newVal) -> {

        String texto = newVal.replaceAll("[^\\d]", "");
        
        if (texto.length() > 4) {
            texto = texto.substring(0, 4);
        }
        if (!texto.equals(newVal)) {
            campo.setText(texto);
        }
    });
}
   
   public static void soloNumeros(TextField campo) {
    campo.textProperty().addListener((obs, oldVal, newVal) -> {

        String texto = newVal.replaceAll("[^\\d]", "");

        if (!texto.equals(newVal)) {
            campo.setText(texto);
        }
    });
}
}

package cr.ac.una.tarea.util;

import javafx.scene.control.TextField;

public class ValidadorNumeros {

   public static void soloNumeros(TextField campo) {
    campo.textProperty().addListener((obs, oldVal, newVal) -> {

        // 🔥 eliminar todo lo que no sea número
        String texto = newVal.replaceAll("[^\\d]", "");

        // 🔥 limitar a 4 caracteres
        if (texto.length() > 4) {
            texto = texto.substring(0, 4);
        }

        // 🔥 solo actualizar si cambió (evita loops innecesarios)
        if (!texto.equals(newVal)) {
            campo.setText(texto);
        }
    });
}
}

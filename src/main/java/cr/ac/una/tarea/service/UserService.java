/*package cr.ac.una.tarea.service;

import cr.ac.una.tarea.model.DataUsers;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
/**
 *
 * @author joshu
 *//*
public class UserService {
    private final Path archivo = Path.of("dataUsers.json");

    public void guardarUsuario(DataUsers user) throws IOException {

        String json = "{\n" +
                      "  \"name\": \"" + user.getName() + "\"\n" +
                "  \"Informacion\": \"" + user.getInfo() + "\"\n" +
                "  \"Pin\": \"" + user.getPin() + "\"\n" +
                 "  \"image\": \"" + user.getImageUrl() + "\"\n" +
                      "}";


        Files.writeString(archivo, json);
    }

    public DataUsers cargarUsuario() throws IOException {

        if (!Files.exists(archivo)) return null;

        String json = Files.readString(archivo);

        String name = json.split("\"name\"\\s*:\\s*\"")[1].split("\"")[0];
        String info = json.split("\"Informacion\"\\s*:\\s*\"")[1].split("\"")[0];
        String pin = json.split("\"Pin\"\\s*:\\s*\"")[1].split("\"")[0];
        String imageUrl = json.split("\"image\"\\s*:\\s*\"")[1].split("\"")[0];

        return new DataUsers(name, info, pin, imageUrl);
    }

    public boolean validarPin(String pin) {
        return pin.matches("\\d{4}");
}
}*/
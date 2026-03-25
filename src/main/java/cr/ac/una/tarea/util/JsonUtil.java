package cr.ac.una.tarea.util;

import java.nio.file.*;
import java.io.IOException;
import java.util.*;
import cr.ac.una.tarea.model.DataProcedure;

public class JsonUtil {

    public static List<DataProcedure> leerJson(Path archivo) {
        List<DataProcedure> lista = new ArrayList<>();

        try {
            if (!Files.exists(archivo)) return lista;

            String json = Files.readString(archivo);

            if (json.equals("[\n\n]")) return lista;

            json = json.replace("[", "").replace("]", "");
            String[] objetos = json.split("\\},");

            for (String obj : objetos) {
                obj = obj.replace("{", "").replace("}", "").trim();

                String name = obj.split("\"name\"\\s*:\\s*\"")[1].split("\"")[0];
                String price = obj.split("\"price\"\\s*:\\s*\"")[1].split("\"")[0];
                String detail = obj.split("\"detail\"\\s*:\\s*\"")[1].split("\"")[0];
                String state = obj.split("\"state\"\\s*:\\s*\"")[1].split("\"")[0];

                DataProcedure dp = new DataProcedure();
                dp.setName(name);
                dp.setprice(price);
                dp.setDetail(detail);
                dp.setState(Boolean.valueOf(state));

                lista.add(dp);
            }

        } catch (IOException e) {
        }

        return lista;
    }
}
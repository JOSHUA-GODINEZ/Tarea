
package cr.ac.una.tarea.util;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class DataEjecucion {

    private File carpeta;

    public DataEjecucion(Propiedades config) {

        File ruta = new File(config.getRutaJson());

        if (ruta.isAbsolute()) {
            carpeta = ruta;
        } else {
            carpeta = new File(System.getProperty("user.dir"), config.getRutaJson());
        }

        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        System.out.println("Carpeta JSON: " + carpeta.getAbsolutePath());
    }

    public File getArchivo(String nombre) {
        return new File(carpeta, nombre + ".json");
    }
}

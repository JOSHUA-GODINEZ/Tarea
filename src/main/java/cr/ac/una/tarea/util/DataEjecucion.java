package cr.ac.una.tarea.util;

import java.io.File;

public class DataEjecucion {

    private File carpeta;
//Crear (si no existe) y guardar la ruta donde se guardan los archivos Json
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
    }

    public File getArchivo(String nombre) {
        return new File(carpeta, nombre + ".json");
    }
}


package cr.ac.una.tarea.util;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.io.*;

import java.io.*;

public class Propiedades {

    private String sucursal = " ";
    private String estacion = " ";
    private String rutaJson = "Jsons";
    public Propiedades() {
        cargar();
    }

    public void cargar() {

        BufferedReader br = null;

        try {
            // Carpeta donde se ejecuta el .jar
            File archivo = new File(System.getProperty("user.dir"), "propiedades.ini");

            System.out.println("INI: " + archivo.getAbsolutePath());
            System.out.println("Existe: " + archivo.exists());

            if (!archivo.exists()) {
                return;
            }

            br = new BufferedReader(new FileReader(archivo));
            String linea;

            while ((linea = br.readLine()) != null) {

                linea = linea.replace("\uFEFF", "").trim();

                String[] partes = linea.split("=", 2);

                if (partes.length != 2) continue;

                String clave = partes[0].trim();
                String valor = partes[1].trim();

                if (clave.equals("Sucursal")) {
                    sucursal = valor;
                } 
                else if (clave.equals("Estacion")) {
                    estacion = valor;
                }
                if (clave.equals("RutaJson")) {
                 rutaJson = valor;
               }
            }

        } catch (Exception e) {
            System.out.println("Error leyendo ini: " + e.getMessage());

        } finally {
            try {
                if (br != null) br.close();
            } catch (Exception ignored) {}
        }
    }

    public String getSucursal() {
        return sucursal;
    }

    public String getEstacion() {
        return estacion;
    }
    public String getRutaJson() {
    return rutaJson;
}
}
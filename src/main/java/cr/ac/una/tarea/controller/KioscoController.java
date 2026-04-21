package cr.ac.una.tarea.controller;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import cr.ac.una.tarea.App;
import cr.ac.una.tarea.model.DataParametres;
import cr.ac.una.tarea.model.DataProcedure;
import cr.ac.una.tarea.model.KioscoData;
import cr.ac.una.tarea.model.Teme;
import cr.ac.una.tarea.model.UsuarioData;
import cr.ac.una.tarea.util.Alertas;
import cr.ac.una.tarea.util.DataEjecucion;
import cr.ac.una.tarea.util.Propiedades;
import java.awt.Desktop;
import java.io.File;
import javafx.event.ActionEvent;
import java.time.LocalDate;
import java.time.Period;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class KioscoController implements Initializable {
    @FXML private VBox blockID;
    @FXML private TextField txtId;


    @FXML
    private HBox root;
     KioscoData kiosco = new KioscoData();
    @FXML
    private ImageView Logo;

 Propiedades config = new Propiedades();
DataEjecucion data = new DataEjecucion(config);
    @FXML
    private Label LName;
    @FXML
    private HBox rootProcedures;
    @FXML
    private Label LblSelec;
    @FXML
    private Button BtnGenerarFicha;
    @FXML
    private Label Lnombre;
    @FXML
    private Label Ltramite;
    private Boolean temaAnterior = null;
    @FXML
    private HBox rootGenerar;
    @FXML
    private Label LblMensaje;
    @FXML
    private VBox root1;
    private Label selectedProcedure = null;
    String PinAdmin;
    @Override
    public void initialize(URL url, ResourceBundle rb) {

                      // Cambiar el tamaño del texto segune el tamaño de la ventana
               root1.sceneProperty().addListener((obs, oldScene, newScene) -> {
        if (newScene != null) {
              LName.styleProperty().bind(
                newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
            Lnombre.styleProperty().bind(
                newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
            txtId.styleProperty().bind(
                newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
            Ltramite.styleProperty().bind(
                newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
                
            LblSelec.styleProperty().bind(
                newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
                BtnGenerarFicha.styleProperty().bind(
                newScene.widthProperty().multiply(0.03).asString("-fx-font-size: %.2fpx;")
            );
                  
        
        }
    }); 
               // Carga cada segundo los tramites, parametros y el tema
       Timeline timelineTheme = new Timeline(
    new KeyFrame(Duration.seconds(1), e -> {
        try {
            cargar();
            loadProcedures();
             File archivoTheme = data.getArchivo("theme");
if (!archivoTheme.exists()) return;
String json = Files.readString(archivoTheme.toPath());

            Gson gson = new Gson();
            Teme t = gson.fromJson(json, Teme.class);

            if (temaAnterior == null || !temaAnterior.equals(t.temeDark)) {
                temaAnterior = t.temeDark;

                // Cambia los roots
                root.getStyleClass().clear();
                blockID.getStyleClass().clear();
                rootProcedures.getStyleClass().clear();
                rootGenerar.getStyleClass().clear();

               
 if (t.temeDark) {
     root.getStyleClass().addAll("mi-panel-fondo1","mi-Titulososcuros");
 
    rootGenerar.getStyleClass().addAll("mi-panel-fondo1","mi-Titulososcuros");
    blockID.getStyleClass().addAll("mi-rectangulooscuro","mi-Titulososcuros");
    rootProcedures.getStyleClass().addAll("mi-panel-fondo1","mi-Titulososcuros");
    
} else {

      root.getStyleClass().addAll("mi-panel-fondo2","mi-Titulos");

    rootGenerar.getStyleClass().addAll("mi-panel-fondo2","mi-Titulos");
    blockID.getStyleClass().addAll("mi-rectangulo","mi-Titulos");
    rootProcedures.getStyleClass().addAll("mi-panel-fondo2","mi-Titulos");
}     

            }

        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    })
);
timelineTheme.setCycleCount(Timeline.INDEFINITE);
timelineTheme.play();
}
        
    

@FXML
private void onActionGenerateTicket(ActionEvent event) {
 if( selectedProcedure!=null){
    selectedProcedure.getStyleClass().remove("seleccionado");
    selectedProcedure = null;}
    String ficha = generateTicketNumber();
    kiosco.setTicketNumber(ficha);
    guardarKiosco(kiosco);
   generarPDF(ficha);
      BtnGenerarFicha.setDisable(true);
      txtId.setText("");
      LblSelec.setText("Sin tramite selecionado");
    kiosco = new KioscoData();
}

private void generarPDF(String numero) {
    try {
        //En la ruta del .ini crea y guarda el pdf 
        String basePath = config.getRutaJson();
        File carpeta = new File(basePath, "FichasPdfs");
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
        File archivoPDF = new File(carpeta, "ficha_" + numero + ".pdf");

        Document doc = new Document();
        PdfWriter.getInstance(doc, new FileOutputStream(archivoPDF));
        doc.open();
           // Crea el documentro y le da posiciones a las imagenes y la ficha
        String url = Logo.getImage().getUrl();
        com.itextpdf.text.Image logo = com.itextpdf.text.Image.getInstance(url);

        logo.scaleToFit(120, 120);
        logo.setAbsolutePosition(40, 750);
        doc.add(logo);

        if (kiosco.Preferencial) {
            String urlPref = getClass().getResource("/cr/ac/una/tarea/resource/PrefetencialImage.png").toExternalForm();

            com.itextpdf.text.Image logoPref = com.itextpdf.text.Image.getInstance(urlPref);

            logoPref.scaleToFit(100, 100);
            logoPref.setAbsolutePosition(450, 750);
            doc.add(logoPref);
        }

        Paragraph numeroTexto = new Paragraph(numero);
        numeroTexto.setAlignment(Element.ALIGN_CENTER);
        numeroTexto.setSpacingBefore(300);
        numeroTexto.getFont().setSize(40);

        doc.add(numeroTexto);

        doc.close();

        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(archivoPDF);
        }

        System.out.println("PDF generado correctamente");

    } catch (Exception e) {
        e.printStackTrace();
    }
}
  
   
 private String generateTicketNumber() {
//Guarda en un archivo la sucursal y su ultima ficha
    try {
        File archivo = data.getArchivo("TicketCounter");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        JsonObject json;
  
        if (archivo.exists()) {
          String contenido = Files.readString(archivo.toPath());

            if (!contenido.isBlank()) {
                json = gson.fromJson(contenido, JsonObject.class);
            } else {
                json = new JsonObject();
            }
        } else {
            json = new JsonObject();
        }
        String sucursal = config.getSucursal();
        // Al ultimo le genera el siguiente
       String ultimo;
         if (json.has(sucursal)) {
         ultimo = json.get(sucursal).getAsString();
         } else {
            ultimo = "A-1";
              }

        char letra = ultimo.charAt(0);
        int numero = Integer.parseInt(ultimo.substring(1));

        // 
        numero++;
// Genera un letra entre A y B, tambien un mero entre 0 y 9
        if (numero > 9) {
            numero = 0;
            letra++;

            if (letra > 'B') {
                letra = 'A';
            }
        }

        String nuevo = letra + String.valueOf(numero);

  
        json.addProperty(sucursal, nuevo);

        Files.writeString(archivo.toPath(), gson.toJson(json));

      //Le accicna su respectivo audio a la ficha
        kiosco.setAudio(nuevo + ".wav");

        return nuevo;

    } catch (Exception e) {
       // Si no existe imprime A0
        e.printStackTrace();
        return "A0";
    }
}
    @FXML  // Botones del GranePane
    private void onAction7(ActionEvent event) {
        txtId.appendText("7");
    }

    @FXML
    private void onAction8(ActionEvent event) {
        txtId.appendText("8");
    }

    @FXML
    private void onAction9(ActionEvent event) {
        txtId.appendText("9");
    }

    @FXML
    private void onAction4(ActionEvent event) {
        txtId.appendText("4");
    }

    @FXML
    private void onAction5(ActionEvent event) {
        txtId.appendText("5");
    }

    @FXML
    private void onAction6(ActionEvent event) {
        txtId.appendText("6");
    }

    @FXML
    private void onAction3(ActionEvent event) {
        txtId.appendText("3");
    }

    @FXML
    private void onAction2(ActionEvent event) {
        txtId.appendText("2");
    }

    @FXML
    private void onAction1(ActionEvent event) {
         txtId.appendText("1");
    }

    @FXML
    private void onAction0(ActionEvent event) {
        txtId.appendText("0");
    }

    @FXML
    private void onActionEliminar(ActionEvent event) {
            String txt = txtId.getText();
    if (!txt.isEmpty()) {
        txtId.setText(txt.substring(0, txt.length() - 1));
    }
    }
    
    // Carga los parametros generales a la vista
     private void cargar() {
    try {
       File archivo = data.getArchivo("GeneralData");
        if (!Files.exists(archivo.toPath())) return;
        String json = Files.readString(archivo.toPath());
        if (json.isBlank()) return;

        Gson gson = new Gson();
        DataParametres user = gson.fromJson(json, DataParametres.class);

       LName.setText(user.getName());
       PinAdmin = user.getPin();
       Logo.setImage(new javafx.scene.image.Image(user.getImageUrl()));
               Logo.fitWidthProperty().bind(root.widthProperty());
         Logo.fitHeightProperty().bind(root.heightProperty());
    } catch (IOException e) {
        System.out.println("Error al cargar: " + e.getMessage());
    }
}
    
    //Carga los tramites disponibles
    public void loadProcedures() {
    rootProcedures.getChildren().clear();
    try {
          File archivo = data.getArchivo("ProcedureData");
String json = Files.readString(archivo.toPath());
        if (!archivo.exists()) return;
        
        Gson gson = new Gson();
        DataProcedure[] procedureList = gson.fromJson(json, DataProcedure[].class);

     for (DataProcedure dp : procedureList) {
 if (!dp.getState()) continue;
    Label label = new Label(dp.getName());
   label.getStyleClass().addAll("mi-Label","label-procedimiento","mi-boton2");

   // Cambia el CSS del tema
if (temaAnterior != null && temaAnterior) {
    label.getStyleClass().addAll("mi-rectangulooscuro","mi-Titulososcuros");
} else {
    label.getStyleClass().addAll("mi-rectangulo","mi-Titulos");
}
    label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    label.setMinSize(200, 0);

    // Cambia de Tamaño el texto sugun el tamaño de la ventana
    Scene scene = rootProcedures.getScene();
    if (scene != null) {
        label.styleProperty().bind(scene.widthProperty().multiply(0.02).asString("-fx-font-size: %.12fpx;"));
    }

    label.setOnMouseClicked(e -> {
         if (selectedProcedure != null) {
        selectedProcedure.getStyleClass().remove("seleccionado");
    }
    selectedProcedure = label;
    label.getStyleClass().addAll("seleccionado");
        String nombre = dp.getName();
        LblSelec.setText(nombre);
        // guardar en kiosco
        kiosco.setTramite(nombre);
          BtnGenerarFicha.setDisable(false);
        System.out.println("Trámite seleccionado: " + nombre);
    });

    rootProcedures.getChildren().add(label);
}

    } catch (IOException e) {
    }
}


@FXML
private void onActionAvanzar(ActionEvent event) {

    if (txtId.getText().isBlank()) {  Alertas.mostrarMensajeError(LblMensaje, "Cedula Invalida");       return;}

    try {
        File archivo = data.getArchivo("Usuarios");
        if (!archivo.exists()) return;

        String json = Files.readString(archivo.toPath());
        if (json.isBlank()) return;

        Gson gson = new Gson();
        UsuarioData[] usuarios = gson.fromJson(json, UsuarioData[].class);
        if (usuarios == null) return;

        String cedula = txtId.getText();
        UsuarioData encontrado = null;

        // Buscar el usuario
        for (UsuarioData u : usuarios) {
            if (cedula.equals(u.cedula)) {
                encontrado = u;
                Alertas.mostrarMensajeCorrecto(LblMensaje, "Bienvenid@ "+u.nombre);
                break;
            }
        }

        if (encontrado == null) {
            Alertas.mostrarMensajeError(LblMensaje, "No se encuentra en el sistema");
            return;
        }

        // Calcular edad(preferencial)
        LocalDate fechaNacimiento = LocalDate.parse(encontrado.fecha);
        int edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();
        boolean esPreferencial = edad >= 65;

        kiosco.setTxtId(cedula);
        kiosco.setPreference(esPreferencial);


    } catch (Exception e) {
        e.printStackTrace();
    }
}

 // Guarda los datos del kiosco en e archivo
private void guardarKiosco(KioscoData kiosco) {
    try {
        File archivo = data.getArchivo("KioscoData");

         Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<KioscoData> lista = new ArrayList<>();

        if (archivo.exists()) {
            String json = Files.readString(archivo.toPath());

            if (!json.isBlank()) {
                KioscoData[] arr = gson.fromJson(json, KioscoData[].class);
                if (arr != null) {
                    lista.addAll(Arrays.asList(arr));
                }
            }
        }
      kiosco.setFecha(LocalDate.now().toString());
      kiosco.Sucursal= config.getSucursal();   
       if(kiosco.Preferencial==null){kiosco.Preferencial=false;}
      
        lista.add(kiosco);

        String nuevoJson = gson.toJson(lista);
        Files.writeString(archivo.toPath(), nuevoJson);
    } catch (Exception e) {
        e.printStackTrace();
    }
}

@FXML          // muestra y bloquea la vista del pin
private void onActionIndication(ActionEvent event) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/cr/ac/una/tarea/view/PinAdminView.fxml"));
        Parent root = loader.load();
        PinAdminController controller = loader.getController();

     controller.setKiosco(kiosco);
        Stage owner = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.initOwner(owner); 
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle("Pin Administrativo");
        stage.showAndWait();

    } catch (IOException e) {
        e.printStackTrace();
    }
}
}

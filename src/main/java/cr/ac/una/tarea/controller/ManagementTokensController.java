
package cr.ac.una.tarea.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cr.ac.una.tarea.model.EstacionData;
import cr.ac.una.tarea.model.FichasProyection;
import cr.ac.una.tarea.model.KioscoData;
import cr.ac.una.tarea.model.SignalData;
import cr.ac.una.tarea.model.SucursalData;
import cr.ac.una.tarea.model.Teme;
import cr.ac.una.tarea.model.TopUsersData;
import cr.ac.una.tarea.model.TramiteData;
import cr.ac.una.tarea.model.UsuarioData;
import cr.ac.una.tarea.util.DataEjecucion;
import cr.ac.una.tarea.util.Propiedades;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class ManagementTokensController implements Initializable {

    private SucursalData sucursalSeleccionada = null;
private EstacionData estacionSeleccionada = null;
  FichasProyection ficha =new FichasProyection();
private FichasProyection fichaActual = new FichasProyection();
    @FXML
    private Label LSucursal;
    @FXML
    private Label LEstacion;
    @FXML
    private VBox rootTramits;

    @FXML
    private HBox rootAtencion;
    private  Label lblFicha;
    private HBox selectedTramit = null;
      private HBox selectedStation = null;
      //DataHolder sucursal = new DataHolder();
      
      Boolean pref = null;
    @FXML
    private Label LblScursal;
    @FXML
    private Label LblEstacion;
    @FXML
    private Label usuariosPendientes;
    private Boolean temaAnterior = null;
    Propiedades config = new Propiedades();
DataEjecucion data = new DataEjecucion(config);
    @FXML
    private HBox rootTop;
    @FXML
    private HBox rootBotones;
    @FXML
    private HBox rootAtendiendo;
    @FXML
    private HBox rootEspera;
    @FXML
    private Label LbLLlamar;
private boolean modoEspecifico = false;
    @FXML
    private Button btnEspecifico;
    @FXML
    private Label LAtendiendo;
    @FXML
    private Label LClientes;
public void cargar() {

    String sucursal = config.getSucursal();
    String estacion = config.getEstacion();

    // Mostrar directamente en pantalla
    LSucursal.setText(sucursal);
    LEstacion.setText(estacion);

    // Si quieres crear algo visual simple
    HBox hEst = new HBox();
    Label lblNombre = new Label(estacion);

    hEst.getChildren().add(lblNombre);
    hEst.setSpacing(10);
    hEst.setAlignment(Pos.CENTER_LEFT);
    hEst.setPadding(new Insets(5));

    hEst.getStyleClass().addAll("mi-rectangulo","mi-Titulos","mi-boton","seleccionado");


    System.out.println("Sucursal: " + sucursal + " Estacion: " + estacion);

    // cargar trámites directamente
  // cargarTramites();
}

    
    
    
public void cargarTramites() {
    try {

            
     File archivoBranches = data.getArchivo("BranchesData");
        File archivoTramites = data.getArchivo("KioscoData");

          if (!archivoBranches.exists() || !archivoTramites.exists()) return;

       String nombreSucursal = LSucursal.getText();
          String nombreEstacion = LEstacion.getText();

           Gson gson = new Gson();

          SucursalData[] sucursales = gson.fromJson(Files.readString(archivoBranches.toPath()),SucursalData[].class);

     KioscoData[] kiosco = gson.fromJson(
    Files.readString(archivoTramites.toPath()),
    KioscoData[].class
);

// 🔥 SOLUCIÓN
if (kiosco == null) {
    kiosco = new KioscoData[0];
}

        // Buscar la estacion que coincida
        List<String> tramitesEstacion = new ArrayList<>();
        for (SucursalData sd : sucursales) {
            if (sd.nombre.equals(nombreSucursal)) {
                for (EstacionData ed : sd.estaciones) {
                    if (ed.nombre.equals(nombreEstacion)) {
                        tramitesEstacion = ed.tramites;
                        break;
                    }
                }
                break;
            }
        }

        // Agregar al rootTramits si coincide con TramitesData
        rootTramits.getChildren().clear();
         int contador = 0; // 👈 contador de trámites cargados
        for (String tramiteEstacion : tramitesEstacion) {
            for (KioscoData td : kiosco) {
                if (td.tramite.equals(tramiteEstacion) && td.Sucursal.equals(LSucursal.getText())) {
                    HBox h = new HBox();
                    Label lblTramite = new Label(td.tramite);
                    lblFicha = new Label(td.ficha);
              lblTramite.sceneProperty().addListener((obs, oldScene, newScene) -> {
    if (newScene != null) {
        lblTramite.styleProperty().bind(
            newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
        );
    }
});

lblFicha.sceneProperty().addListener((obs, oldScene, newScene) -> {
    if (newScene != null) {
        lblFicha.styleProperty().bind(
            newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
        );
    }
});
            
          
              ImageView PreferIMag = new ImageView();
                  PreferIMag.setFitHeight(30);
            PreferIMag.setFitWidth(30);
            PreferIMag.getStyleClass().add("mi-IMagepreferencial");
            if( td.Preferencial){
                    h.getChildren().addAll(lblTramite, lblFicha, PreferIMag);}
            else{ h.getChildren().addAll(lblTramite, lblFicha);}
                    h.setSpacing(10);
                    h.setAlignment(Pos.CENTER);
                   // h.setPadding(new Insets(5));
                  //  h.setStyle("-fx-border-color: blue;");
                  h.getStyleClass().addAll("mi-rectangulo","mi-Titulos","mi-boton");
h.setOnMouseClicked(ev -> {

    if (selectedTramit != null) {
        selectedTramit.getStyleClass().remove("seleccionado");
    }

    h.getStyleClass().add("seleccionado");
    selectedTramit = h;

    if (modoEspecifico) {
        modoEspecifico = false;
        btnEspecifico.getStyleClass().remove("boton-activo");

        HBox tramiteSeleccionado = selectedTramit; // 🔥 copia segura

        procesarTramiteSeleccionado(tramiteSeleccionado);
    }
});



rootTramits.getChildren().add(h);
                  contador++;   
                }
            }
        }
     usuariosPendientes.setText(String.valueOf(contador));

usuariosPendientes.getStyleClass().clear();
if (contador < 3)
    usuariosPendientes.getStyleClass().add("mi-Scantidad");
else if (contador < 6)
    usuariosPendientes.getStyleClass().add("mi-Mcantidad");
else
    usuariosPendientes.getStyleClass().add("mi-Bcantidad");
    } catch (IOException e) {
        System.out.println("Error al cargar: " + e.getMessage());
    }
}
    

   
  @FXML

private void onActionNext(ActionEvent event) {

    rootAtencion.getChildren().clear();

    if (!rootTramits.getChildren().isEmpty()) {

        HBox hTramite = (HBox) rootTramits.getChildren().get(0);
        Label lblTramite = (Label) hTramite.getChildren().get(0);
        Label lblFicha = (Label) hTramite.getChildren().get(1);

        int actual = Integer.parseInt(usuariosPendientes.getText());
        if (actual > 0) {
            usuariosPendientes.setText(String.valueOf(actual - 1));
        }

        try {
            File archivoTramites = data.getArchivo("KioscoData");
            File archivoUsuarios = data.getArchivo("usuarios");

            if (!archivoTramites.exists() || !archivoUsuarios.exists()) return;

            String jsonTramites = Files.readString(archivoTramites.toPath());
            String jsonUsuarios = Files.readString(archivoUsuarios.toPath());

            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            KioscoData[] kiosco = gson.fromJson(jsonTramites, KioscoData[].class);
            UsuarioData[] usuarios = gson.fromJson(jsonUsuarios, UsuarioData[].class);

            if (kiosco == null) kiosco = new KioscoData[0];
            if (usuarios == null) usuarios = new UsuarioData[0];

            String idTramite = null;
            KioscoData tdEncontrado = null;
            boolean pref = false;

            // 🔍 BUSCAR
            for (KioscoData td : kiosco) {
                if (lblFicha.getText().equals(td.ficha)) { // 🔥 solo ficha
                    idTramite = td.id;
                    tdEncontrado = td;
                    pref = td.Preferencial;
                    break;
                }
            }

            // 🔥 CAPTURAR AUDIO ANTES DE BORRAR
            String audioFicha = null;
            if (tdEncontrado != null) {
                audioFicha = tdEncontrado.audio; // 🔥 aquí guardas el audio
            }

            // 🔥 ELIMINAR DEL JSON
            List<KioscoData> lista = new ArrayList<>(Arrays.asList(kiosco));

            lista.removeIf(td ->
                lblFicha.getText().equals(td.ficha)
            );

            Files.writeString(
                archivoTramites.toPath(),
                gson.toJson(lista)
            );

            // 🔹 UI USUARIO
            HBox hUsuario = new HBox();

            if (idTramite != null) {

                boolean encontrado = false;

                for (UsuarioData ud : usuarios) {

                    if (ud.cedula.equals(idTramite)) {

                        Label lblNombre = new Label(ud.nombre);
                        Label lblCedula = new Label(ud.cedula);
                        Label lblNumero = new Label(ud.numero);
                        Label lblFecha = new Label(ud.fecha);

                        ImageView IMFoto = new ImageView(ud.imagen);
                        IMFoto.setFitHeight(100);
                        IMFoto.setFitWidth(100);

                        hUsuario.getChildren().addAll(
                            lblNombre, lblCedula, lblNumero, lblFecha, IMFoto
                        );

                        encontrado = true;

                        // 🔹 TOP USERS
                        List<TopUsersData> topUsers = new ArrayList<>();
                        File archivoTop = data.getArchivo("TopUsers");

                        if (archivoTop.exists()) {
                            String json = Files.readString(archivoTop.toPath());
                            TopUsersData[] existentes = gson.fromJson(json, TopUsersData[].class);

                            if (existentes != null) {
                                topUsers.addAll(Arrays.asList(existentes));
                            }
                        }

                        boolean yaExiste = false;

                        for (TopUsersData tu : topUsers) {
                            if (tu.cedula.equals(ud.cedula)) {
                                tu.cantidad++;
                                yaExiste = true;
                                break;
                            }
                        }

                        if (!yaExiste) {
                            TopUsersData nuevo = new TopUsersData();
                            nuevo.nombre = ud.nombre;
                            nuevo.cedula = ud.cedula;
                            nuevo.numero = ud.numero;
                            nuevo.fecha = ud.fecha;
                            nuevo.imagen = ud.imagen;
                            nuevo.sucursal = LSucursal.getText();
                            nuevo.estacion = LEstacion.getText();
                            nuevo.fechaTramite = tdEncontrado != null ? tdEncontrado.fecha : "";
                            nuevo.cantidad = 1;
                            topUsers.add(nuevo);
                        }

                        Files.writeString(archivoTop.toPath(), gson.toJson(topUsers));

                        break;
                    }
                }

                if (!encontrado) {
                    hUsuario.getChildren().add(new Label("Persona desconocida"));
                }

            } else {
                hUsuario.getChildren().add(new Label("Persona desconocida"));
            }

            // 🔥 PROYECCIÓN
            guardarFicha(lblFicha.getText(), pref);

            // 🔥 SIGNAL (AQUÍ VA EL AUDIO)
            SignalData signal = new SignalData();
            signal.sucursal = LSucursal.getText();
            signal.estacion = LEstacion.getText();
            signal.audio = LEstacion.getText() + ".wav";
            signal.audioFicha = audioFicha; // 🔥 NUEVO
            signal.timestamp = System.currentTimeMillis();

            Gson gsonSignal = new Gson();

            Files.writeString(
                data.getArchivo("signal").toPath(),
                gsonSignal.toJson(signal)
            );

            // 🔹 UI FINAL
            hUsuario.setSpacing(10);
            hUsuario.setAlignment(Pos.CENTER_LEFT);
            hUsuario.setPadding(new Insets(5));
            hUsuario.getStyleClass().addAll("mi-rectangulo","mi-Titulos");

            rootTramits.getChildren().remove(hTramite);
            rootAtencion.getChildren().add(hUsuario);
            rootAtencion.getChildren().add(hTramite);

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
   
    
    

    
    
     @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargar();
        
        
        Timeline timeline = new Timeline(
    new KeyFrame(Duration.seconds(1), e -> {
        Platform.runLater(() -> {
            cargarTramites();
            config.cargar();
          LSucursal.setText(config.getSucursal());
    LEstacion.setText(config.getEstacion());
        });
    })
);
timeline.setCycleCount(Timeline.INDEFINITE); // 🔥 esto falta
timeline.play();
               
  fichaActual.estacion = new String[4];
   fichaActual.ficha = new String[4];
  fichaActual.Preferencial = new Boolean[4];
                        
               LblScursal.sceneProperty().addListener((obs, oldScene, newScene) -> {
        if (newScene != null) {
            LblScursal.styleProperty().bind(
                newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.12fpx;")
            );
            LblEstacion.styleProperty().bind(
                newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
            LSucursal.styleProperty().bind(
                newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
            LEstacion.styleProperty().bind(
                newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
              LAtendiendo.styleProperty().bind(
                newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
                  LClientes.styleProperty().bind(
                newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
                      LbLLlamar.styleProperty().bind(
                newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
            usuariosPendientes.styleProperty().bind(
                newScene.widthProperty().multiply(0.02).asString("-fx-font-size: %.2fpx;")
            );
        }
    }); 
       Timeline timelineTheme = new Timeline(
    new KeyFrame(Duration.seconds(1), e -> {
        try {
             File archivoTheme = data.getArchivo("theme");
if (!archivoTheme.exists()) return;
String json = Files.readString(archivoTheme.toPath());

            Gson gson = new Gson();
            Teme t = gson.fromJson(json, Teme.class);

            if (temaAnterior == null || !temaAnterior.equals(t.temeDark)) {
                temaAnterior = t.temeDark;

                // Cambia los roots
                rootTop.getStyleClass().clear();
                rootBotones.getStyleClass().clear();
                rootAtendiendo.getStyleClass().clear();
                rootAtencion.getStyleClass().clear();
                rootEspera.getStyleClass().clear();
                rootTramits.getStyleClass().clear();
                LbLLlamar.getStyleClass().clear();
               
 if (t.temeDark) {
     rootTop.getStyleClass().addAll("mi-rectangulooscuro","mi-Titulososcuros");
  //  rootBotones.getStyleClass().addAll("mi-panel-fondo1","mi-Titulososcuros");
    LbLLlamar.getStyleClass().addAll("mi-panel-fondo1","titulososcu");
    rootAtendiendo.getStyleClass().addAll("mi-rectangulooscuro","mi-Titulososcuros");
    rootAtencion.getStyleClass().addAll("mi-panel-fondo1","mi-Titulososcuros","mi-rootclaro");
    rootEspera.getStyleClass().addAll("mi-rectangulooscuro","mi-Titulososcuros");
    rootTramits.getStyleClass().addAll("mi-Titulososcuros","mi-panel-fondo1","mi-rootclaro");
} else {

   rootTop.getStyleClass().addAll("mi-rectangulo","mi-Titulos");
    //rootBotones.getStyleClass().addAll("mi-panel-fondo2","mi-Titulos");
    LbLLlamar.getStyleClass().addAll("mi-panel-fondo2","titulosclar");
    rootAtendiendo.getStyleClass().addAll("mi-rectangulo","mi-Titulos");
    rootAtencion.getStyleClass().addAll("mi-panel-fondo2","mi-Titulos","mi-root");
     rootEspera.getStyleClass().addAll("mi-rectangulo","mi-Titulos");
      rootTramits.getStyleClass().addAll("mi-panel-fondo2","mi-Titulos","mi-root");
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
private void onActionRepit(ActionEvent event) throws IOException {

    Path archivo = data.getArchivo("signal").toPath();

    if (!Files.exists(archivo)) return;

    Gson gson = new Gson();

    String json = Files.readString(archivo);

    if (json.isBlank()) return;

    SignalData signal = gson.fromJson(json, SignalData.class);

    // 🔥 solo actualizas timestamp para que se dispare otra vez
    signal.timestamp = System.currentTimeMillis();

    Files.writeString(archivo, gson.toJson(signal));
}







@FXML 
    private void onActionEspecific(ActionEvent event) {
        btnEspecifico.getStyleClass().add("boton-activo");
    modoEspecifico = true;
}

@FXML
private void onActionPreferential(ActionEvent event) {

    for (Node n : rootTramits.getChildren()) {
        if (n instanceof HBox h) {

            if (h.getChildren().size() > 2 &&
                h.getChildren().get(2) instanceof ImageView) {

                // 🔥 procesar directamente
                procesarTramiteSeleccionado(h);
                break;
            }
        }
    }
}
public void guardarFicha(String ficha, boolean pref) {

    try {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        
        Path archivo = data.getArchivo("Fichas").toPath();

      

        // 🔹 mover en memoria (fichaActual)
        for (int i = fichaActual.estacion.length - 1; i > 0; i--) {
            fichaActual.estacion[i] = fichaActual.estacion[i - 1];
        }
        fichaActual.estacion[0] = LEstacion.getText();

        for (int i = fichaActual.ficha.length - 1; i > 0; i--) {
            fichaActual.ficha[i] = fichaActual.ficha[i - 1];
        }
       fichaActual.ficha[0] = ficha;

        for (int i = fichaActual.Preferencial.length - 1; i > 0; i--) {
            fichaActual.Preferencial[i] = fichaActual.Preferencial[i - 1];
        }
      fichaActual.Preferencial[0] = pref;

        fichaActual.Sucursal = LSucursal.getText();

        // 🔥 guardar en memoria global
       // DataHolder.fichaActual = fichaActual;

        List<FichasProyection> lista = new ArrayList<>();

        // 🔹 leer archivo
        if (Files.exists(archivo)) {
            String json = Files.readString(archivo);

            if (!json.isBlank()) {
                FichasProyection[] existentes = gson.fromJson(json, FichasProyection[].class);
                lista.addAll(Arrays.asList(existentes));
            }
        }

        boolean encontrada = false;

        // 🔥 buscar si la sucursal ya existe
        for (FichasProyection f : lista) {

            if (f.Sucursal != null && f.Sucursal.equals(fichaActual.Sucursal)) {

                // 🔥 mover datos dentro de ESA sucursal
                for (int i = f.estacion.length - 1; i > 0; i--) {
                    f.estacion[i] = f.estacion[i - 1];
                    f.ficha[i] = f.ficha[i - 1];
                    f.Preferencial[i] = f.Preferencial[i - 1];
                }

                // 🔥 insertar nuevo en posición 0
                f.estacion[0] = fichaActual.estacion[0];
                f.ficha[0] = fichaActual.ficha[0];
                f.Preferencial[0] = fichaActual.Preferencial[0];

                encontrada = true;
                break;
            }
        }

        // 🔥 si NO existe la sucursal
        if (!encontrada) {

            FichasProyection nueva = new FichasProyection();
            nueva.Sucursal = fichaActual.Sucursal;
            nueva.estacion = fichaActual.estacion.clone();
            nueva.ficha = fichaActual.ficha.clone();
            nueva.Preferencial = fichaActual.Preferencial.clone();

            lista.add(nueva);
        }

        // 🔥 guardar JSON
        Files.writeString(archivo, gson.toJson(lista));

        System.out.println("Guardado correctamente");

    } catch (IOException e) {
        System.out.println("Error: " + e.getMessage());
    }
}
private void procesarTramiteSeleccionado(HBox tramite) {

    if (tramite == null) return;

    Label lblTramite = (Label) tramite.getChildren().get(0);
    Label lblFicha = (Label) tramite.getChildren().get(1);
   int actual = Integer.parseInt(usuariosPendientes.getText());
        if (actual > 0) {
            usuariosPendientes.setText(String.valueOf(actual - 1));
        }
    try {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
 File archivoTramites = data.getArchivo("KioscoData");
   String jsonTramites = Files.readString(archivoTramites.toPath());
          KioscoData[] kiosco = gson.fromJson(jsonTramites, KioscoData[].class);

        if (kiosco == null) kiosco = new KioscoData[0];

        UsuarioData[] usuarios = gson.fromJson(
            Files.readString(data.getArchivo("usuarios").toPath()),
            UsuarioData[].class
        );

        if (usuarios == null) usuarios = new UsuarioData[0];

        String idTramite = null;
        KioscoData tdEncontrado = null;

        // 🔥 BUSCAR TRÁMITE Y ASIGNAR PREF
        for (KioscoData td : kiosco) {
            if (td.tramite.equals(lblTramite.getText()) &&
                td.ficha.equals(lblFicha.getText())) {

                idTramite = td.id;
                tdEncontrado = td;
                pref = td.Preferencial; // 🔥 IMPORTANTE
                break;
            }
        }
  // 🔥 CAPTURAR AUDIO ANTES DE BORRAR
            String audioFicha = null;
            if (tdEncontrado != null) {
                audioFicha = tdEncontrado.audio; // 🔥 aquí guardas el audio
            }

            // 🔥 ELIMINAR DEL JSON
            List<KioscoData> lista = new ArrayList<>(Arrays.asList(kiosco));

            lista.removeIf(td ->
                lblFicha.getText().equals(td.ficha)
            );

            Files.writeString(
                archivoTramites.toPath(),
                gson.toJson(lista)
            );

            // 🔹 UI USUARIO
            HBox hUsuario = new HBox();
    

        if (idTramite != null) {
            boolean encontrado = false;

            for (UsuarioData ud : usuarios) {
                if (ud.cedula.equals(idTramite)) {

                    Label lblNombre = new Label(ud.nombre);
                    Label lblCedula = new Label(ud.cedula);
                    Label lblNumero = new Label(ud.numero);
                    Label lblFecha = new Label(ud.fecha);

                    ImageView IMFoto = new ImageView(ud.imagen);
                    IMFoto.setFitHeight(100);
                    IMFoto.setFitWidth(100);

                    hUsuario.getChildren().addAll(
                        lblNombre, lblCedula, lblNumero, lblFecha, IMFoto
                    );

                    encontrado = true;
                    break;
                }
            }

            if (!encontrado) {
                hUsuario.getChildren().add(new Label("Persona desconocida"));
            }

        } else {
            hUsuario.getChildren().add(new Label("Persona desconocida"));
        }

        // 🔥 ESTILOS
        hUsuario.setSpacing(10);
        hUsuario.setAlignment(Pos.CENTER_LEFT);
        hUsuario.setPadding(new Insets(5));
        hUsuario.getStyleClass().addAll("mi-rectangulo", "mi-Titulos");

        // 🔥 MOVER EN UI
        rootTramits.getChildren().remove(tramite);
        rootAtencion.getChildren().clear();
        rootAtencion.getChildren().addAll(hUsuario, tramite);

        tramite.getStyleClass().remove("seleccionado");
        selectedTramit = null; // opcional

        // 🔥 GUARDAR FICHA (YA FUNCIONA)
        guardarFicha(lblFicha.getText(), pref);

        // 🔥 SEÑAL (audio)
        SignalData signal = new SignalData();
        signal.sucursal = LSucursal.getText();
        signal.estacion = LEstacion.getText();
        signal.audio = LEstacion.getText() + ".wav";
        signal.audioFicha = audioFicha; // 🔥 NUEVO
        signal.timestamp = System.currentTimeMillis();

        Files.writeString(
            data.getArchivo("signal").toPath(),
            new Gson().toJson(signal)
        );

    } catch (IOException e) {
        System.out.println("Error: " + e.getMessage());
    }
}


}

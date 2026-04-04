package cr.ac.una.tarea.controller;

import com.google.gson.Gson;
import cr.ac.una.tarea.model.EstacionData;
import cr.ac.una.tarea.model.SucursalData;
import cr.ac.una.tarea.model.TopUsersData;
import cr.ac.una.tarea.model.TramiteData;
import cr.ac.una.tarea.model.UsuarioData;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ManagementTokensController implements Initializable {

    @FXML
    private HBox rootSucursal;
    private SucursalData sucursalSeleccionada = null;
private EstacionData estacionSeleccionada = null;
//private final Path archivo = Path.of("Jsons/TopUsers.json");
    @FXML
    private Label LSucursal;
    @FXML
    private Label LEstacion;
    @FXML
    private VBox rootTramits;

    @FXML
    private HBox rootAtencion;
    
    private HBox selectedTramit = null;
    public void cargar() {
    try {
        Path archivo = Paths.get("Jsons/BranchesData.json");
        if (!Files.exists(archivo)) return;
        String json = Files.readString(archivo);
        if (json.isBlank()) return;

        Gson gson = new Gson();
        SucursalData[] sucursales = gson.fromJson(json, SucursalData[].class);

        for (SucursalData sd : sucursales) {
            Accordion acc = new Accordion();
         //  acc.getStyleClass().add("mi-rectangulo");
            TitledPane pane = new TitledPane();
            pane.setText(sd.nombre);
         //   pane.getStyleClass().add("mi-rectangulo");
            VBox vEstaciones = new VBox();
            vEstaciones.setSpacing(5);
            vEstaciones.getStyleClass().add("mi-rectangulo");
            
            for (EstacionData ed : sd.estaciones) {
                HBox hEst = new HBox();
                Label lblNombre = new Label(ed.nombre);
                CheckBox check = new CheckBox();
                check.setSelected(ed.preferencial);
                check.setDisable(true);
                hEst.getChildren().addAll(lblNombre, check);
                hEst.setSpacing(10);
                hEst.setAlignment(Pos.CENTER_LEFT);
               // hEst.setStyle("-fx-border-color: blue;");
                hEst.setPadding(new Insets(5));
    
                hEst.setOnMouseClicked(ev -> {
                    sucursalSeleccionada = sd;
                    estacionSeleccionada = ed;
                    LSucursal.setText(sd.nombre);
                    LEstacion.setText(ed.nombre);
                    System.out.println("Sucursal: " + sd.nombre + " Estacion: " + ed.nombre);
                     cargarTramites();
                });
                 hEst.getStyleClass().addAll("mi-rectangulo","mi-Titulos");
                vEstaciones.getChildren().add(hEst);
            }

            pane.setContent(vEstaciones);
            acc.getPanes().add(pane);
            rootSucursal.getChildren().add(acc);
        }
    } catch (IOException e) {
        System.out.println("Error al cargar: " + e.getMessage());
    }
}

    
    
    
public void cargarTramites() {
    try {
        Path archivoBranches = Paths.get("Jsons/BranchesData.json");
        Path archivoTramites = Paths.get("Jsons/prueba.json");
        if (!Files.exists(archivoBranches) || !Files.exists(archivoTramites)) return;

        String nombreSucursal = LSucursal.getText();
        String nombreEstacion = LEstacion.getText();

        Gson gson = new Gson();
        SucursalData[] sucursales = gson.fromJson(Files.readString(archivoBranches), SucursalData[].class);
        TramiteData[] tramites = gson.fromJson(Files.readString(archivoTramites), TramiteData[].class);

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
        for (String tramiteEstacion : tramitesEstacion) {
            for (TramiteData td : tramites) {
                if (td.tramite.equals(tramiteEstacion)) {
                    HBox h = new HBox();
                    Label lblTramite = new Label(td.tramite);
                    Label lblFicha = new Label(td.ficha);
              CheckBox pref = new CheckBox();
              pref.setSelected(td.Preferencial);
            pref.setDisable(true);
                    h.getChildren().addAll(lblTramite, lblFicha,pref);
                    h.setSpacing(10);
                    h.setAlignment(Pos.CENTER);
                    h.setPadding(new Insets(5));
                  //  h.setStyle("-fx-border-color: blue;");
                  h.getStyleClass().addAll("mi-rectangulo","mi-Titulos");
            h.setOnMouseClicked(ev -> {
    selectedTramit = h;
});
rootTramits.getChildren().add(h);
                    
                }
            }
        }
        
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
        
        try {
            Gson gson = new Gson();
            TramiteData[] tramites = gson.fromJson(Files.readString(Paths.get("Jsons/prueba.json")), TramiteData[].class);
            UsuarioData[] usuarios = gson.fromJson(Files.readString(Paths.get("Jsons/usuarios.json")), UsuarioData[].class);
            
            String idTramite = null;
            TramiteData tdEncontrado = null;
            for (TramiteData td : tramites) {
                if (td.tramite.equals(lblTramite.getText()) && td.ficha.equals(lblFicha.getText())) {
                    idTramite = td.id;
                    tdEncontrado = td;
                    break;
                }
            }
            
            
            
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
                        hUsuario.getChildren().addAll(lblNombre, lblCedula, lblNumero, lblFecha, IMFoto);
                     //   hUsuario.setStyle("-fx-border-color: blue;");
                    
                        encontrado = true;
                        
                        // Guardar en TopUsers.json
                        List<TopUsersData> topUsers = new ArrayList<>();
                        Path archivoTop = Paths.get("Jsons/TopUsers.json");
                        
                        if (Files.exists(archivoTop)) {
                            TopUsersData[] existentes = gson.fromJson(Files.readString(archivoTop), TopUsersData[].class);
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
                            nuevo.fechaTramite = tdEncontrado.fecha;
                            nuevo.cantidad = 1;
                            topUsers.add(nuevo);
                        }
                        
                        Files.writeString(archivoTop, gson.toJson(topUsers));
                        break;
                    }
                }
                if (!encontrado) {
                    hUsuario.getChildren().add(new Label("Persona desconocida"));
                }
            } else {
                hUsuario.getChildren().add(new Label("Persona desconocida"));
            }
            
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
        cargarTramites();
    }    

    @FXML
    private void onActionRepit(ActionEvent event) {
         
    }

   @FXML
private void onActionEspecific(ActionEvent event) {
    if (selectedTramit != null) {
        Label lblTramite = (Label) selectedTramit.getChildren().get(0);
        Label lblFicha = (Label) selectedTramit.getChildren().get(1);

        try {
            Gson gson = new Gson();
            TramiteData[] tramites = gson.fromJson(Files.readString(Paths.get("Jsons/prueba.json")), TramiteData[].class);
            UsuarioData[] usuarios = gson.fromJson(Files.readString(Paths.get("Jsons/usuarios.json")), UsuarioData[].class);

            String idTramite = null;
            TramiteData tdEncontrado = null;
            for (TramiteData td : tramites) {
                if (td.tramite.equals(lblTramite.getText()) && td.ficha.equals(lblFicha.getText())) {
                    idTramite = td.id;
                    tdEncontrado = td;
                    break;
                }
            }

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
                        hUsuario.getChildren().addAll(lblNombre, lblCedula, lblNumero, lblFecha, IMFoto);
                      
                        encontrado = true;

                        List<TopUsersData> topUsers = new ArrayList<>();
                        Path archivoTop = Paths.get("Jsons/TopUsers.json");
                        if (Files.exists(archivoTop)) {
                            TopUsersData[] existentes = gson.fromJson(Files.readString(archivoTop), TopUsersData[].class);
                            if (existentes != null) topUsers.addAll(Arrays.asList(existentes));
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
                            nuevo.fechaTramite = tdEncontrado.fecha;
                            nuevo.cantidad = 1;
                            topUsers.add(nuevo);
                        }

                        Files.writeString(archivoTop, gson.toJson(topUsers));
                        break;
                    }
                }
                if (!encontrado) {
                    hUsuario.getChildren().add(new Label("Persona desconocida"));
                }
            } else {
                hUsuario.getChildren().add(new Label("Persona desconocida"));
            }

            hUsuario.setSpacing(10);
            hUsuario.setAlignment(Pos.CENTER_LEFT);
            hUsuario.setPadding(new Insets(5));
            hUsuario.getStyleClass().addAll("mi-rectangulo","mi-Titulos"); 
            rootTramits.getChildren().remove(selectedTramit);
            rootAtencion.getChildren().clear();
            rootAtencion.getChildren().add(hUsuario);
            rootAtencion.getChildren().add(selectedTramit);
            selectedTramit = null;

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

@FXML
private void onActionPreferential(ActionEvent event) {
    for (Node n : rootTramits.getChildren()) {
        if (n instanceof HBox h) {
            CheckBox pref = (CheckBox) h.getChildren().get(2);
            if (pref.isSelected()) {
                rootTramits.getChildren().remove(h);
                // reusar logica de especific
                selectedTramit = h;
                 h.getStyleClass().addAll("mi-rectangulo","mi-Titulos");
                onActionEspecific(null);
                break;
            }
        }
    }
}

}

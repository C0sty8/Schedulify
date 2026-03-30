package schedulify.ui;

import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Clasa pentru functionalitatea ferestrei de vizualizare a unei locatii
 */
public class LocationViewerController implements Initializable {
    public static double lat = 0;
    public static double lon = 0;

    @FXML
    private WebView webView;

    /**
     * Metoda pentru setarea coordonatelor unei locatii
     * @param location Coordonatele unei locatii din baza de date
     */
    public static void setLocation(String location) {
        String[] coordinates = location.split(",");
        lat = Double.parseDouble(coordinates[1]);
        lon = Double.parseDouble(coordinates[0]);
    }

    /**
     * Metoda pentru inchiderea ferestrei
     */
    @FXML
    private void onClose() {
        Stage stage = (Stage) webView.getScene().getWindow();
        stage.close();
        lat = 0;
        lon = 0;
    }

    /**
     * Metoda pentru initializarea web view-ului si afisarea hartii la coordonatele setate
     * @param url Sursa din care au fost luate elementele grafice
     * @param resourceBundle Contine trauceri pentru textele din interfata
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        WebEngine webEngine = webView.getEngine();
        webEngine.load(getClass().getResource("/map_viewer/map.html").toExternalForm());
        webEngine.getLoadWorker().stateProperty().addListener((
                observable, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                webEngine.executeScript("showLocation(" + lat + ", " + lon + ")");
            }
        });
    }
}
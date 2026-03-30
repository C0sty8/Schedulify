package schedulify.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Clasa pentru functionalitatea ferestrei de selectare a unei locatii
 */
public class MapPickerController implements Initializable {
    @FXML
    private WebView webView;
    @FXML
    private Label coordLabel;

    private double selectedLat = 0.0;
    private double selectedLng = 0.0;

    private TextField latField;
    private TextField lngField;

    /**
     * Metoda pentru setarea campurilor de destinatie pentru coordonate
     * @param latField Campul pentru latitudinea
     * @param lngField Campul pentru longitudinea
     */
    public void setDestinationFields(TextField latField, TextField lngField) {
        this.latField = latField;
        this.lngField = lngField;
    }

    /**
     * Metoda pentru actualizarea coordonatelor selectate
     * @param lat Coordonata de latitudine
     * @param lng Coordonata de longitudine
     */
    public void updateSelectedCoordinates(double lat, double lng) {
        this.selectedLat = lat;
        this.selectedLng = lng;

        coordLabel.setText(String.format("Coordinates: Lat %.6f, Lng %.6f", lat, lng));
    }

    /**
     * Metoda pentru salvarea coordonatelor selectate si inchidere a ferestrei
     */
    @FXML
    private void handleSaveLocation() {
        latField.setText(String.format("%.6f", selectedLat));
        lngField.setText(String.format("%.6f", selectedLng));

        Stage stage = (Stage) webView.getScene().getWindow();
        stage.close();
    }

    /**
     * Metoda pentru initializarea ferestrei
     * @param url Sursa din care au fost luate elementele grafice
     * @param resourceBundle Contine trauceri pentru textele din interfata
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        WebEngine webEngine = webView.getEngine();

        webEngine.load(getClass().getResource("/map/map.html").toExternalForm());
        webEngine.titleProperty().addListener((obs, oldTitle, newTitle) -> {
            if (newTitle != null) {
                String[] parts = newTitle.split("[|]");
                if (parts.length == 2) {
                    double lat = Double.parseDouble(parts[0]);
                    double lng = Double.parseDouble(parts[1]);
                    updateSelectedCoordinates(lat, lng);
                }
            }
        });
    }
}
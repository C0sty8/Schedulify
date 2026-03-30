package schedulify.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import schedulify.ui.MapPickerController;

import java.util.List;

/**
 * Clasa utilitara pentru diferite operatii
 */
public class CalendarUtils {
    /**
     * Metoda pentru crearea unui obiect de tip Text cu un font-size specificata
     * @param text continutul obiectului Text
     * @param size marimea fontului
     * @return Obiectul Text
     */
    public static Text makeText(String text, int size) {
        Text t = new Text(text);
        t.setFont(Font.font("Microsoft Tai Le Bold",size));

        return t;
    }

    /**
     * Metoda pentru adaugarea elementelor unui comboBox
     * @param list Lista cu elementele comboBox-ului
     * @param limit Numarul de elemente care trebuie adaugate
     */
    public static void comboBoxElements(List<Integer> list, int limit) {
        for(int i = 0; i < limit; i++) {
            list.add(i);
        }
    }

    /**
     * Metoda pentru dezactivarea unor butoane
     * @param buttons Butoanele care vor fi dezactivate
     */
    public static void makeInactive(Button... buttons) {
        for(Button button : buttons) {
            button.setDisable(true);
            button.setVisible(false);
        }
    }

    /**
     * Metoda pentru crearea ferestrei pentru selectarea unei locatii
     * @param latField Locul unde se va afisa latitudinea
     * @param longField Locul unde se va afisa longitudinea
     * @param selectLocationButton Butonul de selectare
     */
    public static void handleSelectLocation(TextField latField, TextField longField, Button selectLocationButton) {
        try {
            FXMLLoader loader = new FXMLLoader(CalendarUtils.class.getResource("/schedulify/ui/map_picker.fxml"));
            Parent root = loader.load();
            MapPickerController mapController = loader.getController();

            mapController.setDestinationFields(latField, longField);

            Stage mapStage = new Stage();
            mapStage.setTitle("Select location");
            mapStage.setScene(new Scene(root));

            mapStage.initModality(Modality.APPLICATION_MODAL);
            mapStage.initOwner(selectLocationButton.getScene().getWindow());
            mapStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

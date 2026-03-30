package schedulify.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import schedulify.models.Date;
import schedulify.service.CalendarService;
import schedulify.service.UserService;
import schedulify.utils.CalendarUtils;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

/**
 * Clasa pentru functionalitatea din fereastra de creare a calendarii
 */
public class CreateCalendarController implements Initializable {
    @FXML
    private TextField calendarName;
    @FXML
    private DatePicker startDate;
    @FXML
    private DatePicker endDate;
    @FXML
    private TextField editorInvitation;
    @FXML
    private TextField normalInvitation;
    @FXML
    private Text errorField;
    @FXML
    private TextField latField;
    @FXML
    private TextField longField;
    @FXML
    private Button selectLocationButton;

    /**
     * Metoda pentru crearea ferestrei pentru selectarea unei locatii
     */
    @FXML
    private void handleSelectLocation() {
        CalendarUtils.handleSelectLocation(latField, longField, selectLocationButton);
    }

    /**
     * Metoda pentru validarea datelor introduse in fereastra de creare a calendarii
     * @param event Reprezinta evenimentul declansat
     */
    @FXML
    public void validateCalendar(ActionEvent event) {
        if (calendarName.getText().isEmpty() || startDate.getValue() == null || endDate.getValue() == null) {
            errorField.setText("All fields must be filled");
            return;
        }

        Date start = new Date(startDate.getValue().toString());
        Date end = new Date(endDate.getValue().toString());

        if (start.compareTo(end) > 0) {
            errorField.setText("Start date must be before end date");
            return;
        }

        UserService.addCalendarToDB(calendarName.getText(), start.toString(), end.toString(), editorInvitation.getText(), normalInvitation.getText(), latField.getText() + "," + longField.getText());
        UserService.addCalendarUser(MainAppController.user.getId(), editorInvitation.getText(), 3);
        MainAppController.user.addCalendar(CalendarService.getCalendar(editorInvitation.getText()));
        backToMainScene(event);
    }

    /**
     * Metoda pentru generarea unui cod de invitatie aleatoriu
     * @return Codul generat
     */
    public static String generateInvitation() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder result = new StringBuilder();
        Random rnd = new Random();
        while (result.length() < 8) {
            int index = (int) (rnd.nextFloat() * characters.length());
            result.append(characters.charAt(index));
        }
        return result.toString();
    }

    /**
     * Metoda pentru redirectionarea catre fereastra principala
     * @param event Reprezinta evenimentul declansat
     */
    @FXML
    public void backToMainScene(ActionEvent event) {
        MainAppController.setScene(event, "/schedulify/ui/main_scene.fxml", "Schedulify");
    }

    /**
     * Suprascrierea metodei initialize pentru a initializa campurile de invitatie aleatorii
     * @param url Sursa din care au fost luate elementele grafice
     * @param resourceBundle Contine trauceri pentru textele din interfata
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        latField.setText("0.000000");
        longField.setText("0.000000");

        String edInv = generateInvitation();
        while (!UserService.isUniqueInvitation(edInv)) {
            edInv = generateInvitation();
        }

        String normInv = generateInvitation();
        while (normInv.equals(edInv) || !UserService.isUniqueInvitation(normInv)) {
            normInv = generateInvitation();
        }

        editorInvitation.setText(edInv);
        normalInvitation.setText(normInv);
    }
}

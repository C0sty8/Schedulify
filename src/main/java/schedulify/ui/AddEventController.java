package schedulify.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import schedulify.models.Date;
import schedulify.models.Event;
import schedulify.models.HourMinute;
import schedulify.service.CalendarService;
import schedulify.utils.CalendarUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Clasa pentru functionalitatea din fereastra de adaugare evenimentului
 */
public class AddEventController implements Initializable {
    @FXML
    private Text errorField;
    @FXML
    private TextField latField;
    @FXML
    private TextField longField;
    @FXML
    private TextField eventName;
    @FXML
    private DatePicker startDate;
    @FXML
    private ComboBox<Integer> startHour;
    @FXML
    private ComboBox<Integer> startMinute;
    @FXML
    private ComboBox<Integer> endHour;
    @FXML
    private ComboBox<Integer> endMinute;
    @FXML
    private Button selectLocationButton;

    /**
     * Metoda pentru resetarea campurilor
     */
    private void resetFields() {
        errorField.setText("");
        latField.setText("0.000000");
        longField.setText("0.000000");
        eventName.setText("");
        startDate.setValue(null);
        startHour.setValue(null);
        startMinute.setValue(null);
        endHour.setValue(null);
        endMinute.setValue(null);
    }

    /**
     * Metoda pentru crearea ferestrei pentru selectarea unei locatii
     */
    @FXML
    private void handleSelectLocation() {
        CalendarUtils.handleSelectLocation(latField, longField, selectLocationButton);
    }

    /**
     * Metoda pentru verificarea datelor introduse in fereastra de adaugare evenimentului in baza de date
     * daca datele introduse sunt corecte
     */
    @FXML
    private void addEventVerifications() {
        if(startDate.getValue() == null || eventName.getText().isEmpty() ||
                startHour.getValue() == null || startMinute.getValue() == null ||
                endHour.getValue() == null || endMinute.getValue() == null) {
            errorField.setText("Please enter an event name and set the intervals");
            return;
        }

        HourMinute startTime = new HourMinute(startHour.getValue(), startMinute.getValue());
        HourMinute endTime = new HourMinute(endHour.getValue(), endMinute.getValue());

        if(startTime.compareTo(endTime) > 0) {
            errorField.setText("Start time must be before end time");
            return;
        }

        String location = latField.getText() + "," + longField.getText();
        String startDateString = startDate.getValue().toString();

        CalendarService.addEventToDB(CalendarViewerController.getCalendar().getId(), eventName.getText(),
                startDateString, startTime, endTime, location);
        int eventId = CalendarService.getEventId(CalendarViewerController.getCalendar().getId(), eventName.getText(),
                startDateString, startTime, endTime, location);
        CalendarViewerController.getCalendar().addEvent(new Event(eventId, new Date(startDateString),
                eventName.getText(), startTime, endTime, location));

        resetFields();
    }

    /**
     * Metoda pentru initializarea campurilor
     * @param url Sursa din care au fost luate elementele grafice
     * @param resourceBundle Contine traduceri pentru textele din interfata
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Integer> list = new ArrayList<>();
        CalendarUtils.comboBoxElements(list, 60);
        startMinute.getItems().addAll(list);
        endMinute.getItems().addAll(list);
        list.clear();
        CalendarUtils.comboBoxElements(list, 24);
        startHour.getItems().addAll(list);
        endHour.getItems().addAll(list);
    }
}

package schedulify.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import schedulify.models.Activity;
import schedulify.models.HourMinute;
import schedulify.service.CalendarService;
import schedulify.utils.CalendarUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Clasa pentru functionalitatea din fereastra de adaugare a activitatii
 */
public class AddActivityController implements Initializable {
    @FXML
    private Text errorField;
    @FXML
    private TextField activityName;
    @FXML
    private ComboBox<String> dayPicker;
    @FXML
    private ComboBox<Integer> startHour;
    @FXML
    private ComboBox<Integer> startMinute;
    @FXML
    private ComboBox<Integer> endHour;
    @FXML
    private ComboBox<Integer> endMinute;

    /**
     * Metoda pentru resetarea campurilor
     */
    private void resetFields() {
        activityName.setText("");
        errorField.setText("");
        startHour.setValue(null);
        startMinute.setValue(null);
        endHour.setValue(null);
        endMinute.setValue(null);
        dayPicker.setValue(null);
    }

    /**
     * Metoda pentru verificarea datelor introduse de utilizator si adaugarea activitatii
     * in baza de date daca toate datele sunt corecte
     */
    @FXML
    private void addActivityVerifications() {
        if(activityName.getText().isEmpty() || dayPicker.getValue() == null ||
                startHour.getValue() == null || startMinute.getValue() == null ||
                endHour.getValue() == null || endMinute.getValue() == null) {
            errorField.setText("Please enter an activity name and set the intervals");
            return;
        }

        HourMinute startTime = new HourMinute(startHour.getValue(), startMinute.getValue());
        HourMinute endTime = new HourMinute(endHour.getValue(), endMinute.getValue());

        if(startTime.compareTo(endTime) > 0) {
            errorField.setText("Start time must be before end time");
            return;
        }

        String day = dayPicker.getValue().toString();

        CalendarService.addActivityToDB(CalendarViewerController.getCalendar().getId(),
                activityName.getText(), day, startTime, endTime);

        int activityId = CalendarService.getActivityID(CalendarViewerController.getCalendar().getId(), activityName.getText(), day, startTime, endTime);

        CalendarViewerController.getCalendar().addActivityInSchedule(day,
                new Activity(activityId, activityName.getText(), startTime, endTime));

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

        dayPicker.getItems().addAll("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");
    }
}

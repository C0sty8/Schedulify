package schedulify.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
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
 * Clasa pentru functionalitatea din fereastra de editare a orarului
 */
public class EditScheduleController implements Initializable {
    private String day;
    private Activity activity;
    private ActionEvent activityEvent;

    @FXML
    private Text currentEventName;
    @FXML
    private Text errorField;
    @FXML
    private TextField nameField;
    @FXML
    private ComboBox<Integer> startHour;
    @FXML
    private ComboBox<Integer> startMinute;
    @FXML
    private ComboBox<Integer> endHour;
    @FXML
    private ComboBox<Integer> endMinute;
    @FXML
    private ComboBox<String> dayPicker;
    @FXML
    private VBox activitiesViewer;

    /**
     * Metoda pentru resetarea variabilelor
     */
    private void resetCurrentData() {
        day = "";
        activity = null;
        activityEvent = null;
    }

    /**
     * Metoda pentru resetarea campurilor
     */
    private void resetFields() {
        nameField.setText("");
        startHour.setValue(null);
        startMinute.setValue(null);
        endHour.setValue(null);
        endMinute.setValue(null);
        dayPicker.setValue(null);
        currentEventName.setText("");
        errorField.setText("");
    }

    /**
     * Metoda pentru crearea unui buton
     * @param buttonActivity Activitatea pe care o va avea butonul
     * @param day Ziua in care se va desfasura activitatea
     * @return Butonul creat
     */
    private Button makeButton(Activity buttonActivity, String day) {
        Button button = new Button(String.format("%.3s | %s", day, buttonActivity.toString()));

        button.setPrefWidth(250);
        button.setPrefHeight(30);
        button.setOnAction(event -> activitySelected(event, buttonActivity, day));
        button.setStyle("-fx-background-color: #ECECEC;");

        return button;
    }

    /**
     * Metoda pentru determinarea noului timp
     * @param hour Noua ora
     * @param min Noul minut
     * @param interval Timpul curent
     * @return Timpul nou
     */
    public static HourMinute newInterval(ComboBox<Integer> hour, ComboBox<Integer> min, String interval) {
        if(hour.getValue() != null && min.getValue() != null)
            return new HourMinute(hour.getValue(), min.getValue());
        String[] split = interval.split(":");
        if(hour.getValue() != null)
            return new HourMinute(hour.getValue(), Integer.parseInt(split[1]));
        else if(min.getValue() != null)
            return new HourMinute(Integer.parseInt(split[0]), min.getValue());
        return new HourMinute(interval);
    }

    /**
     * Metoda pentru selectarea unei activitati
     * @param event ActionEvent-ul butonului specific activitatii selectate
     * @param activity Activitatea selectata
     * @param day Ziua in care se va desfasura activitatea
     */
    @FXML
    private void activitySelected(ActionEvent event, Activity activity, String day) {
        this.day = day;
        this.activity = activity;
        this.activityEvent = event;
        currentEventName.setText(activity.getActivityName());
    }

    /**
     * Metoda pentru stergerea unei activitati
     */
    @FXML
    public void deleteActivity() {
        if(activity == null) return;
        CalendarService.deleteActivity(activity.getID());
        CalendarViewerController.getCalendar().deleteActivity(day, activity.getID());
        ((Button) activityEvent.getSource()).setDisable(true);
        currentEventName.setText("");
        resetCurrentData();
    }

    /**
     * Metoda pentru salvarea modificarii
     */
    @FXML
    public void saveChanges() {
        if(activity == null) return;
        if(!nameField.getText().isEmpty()) activity.setActivityName(nameField.getText());
        if(dayPicker.getValue() != null) {
            CalendarViewerController.getCalendar().deleteActivity(day, activity.getID());
            CalendarViewerController.getCalendar().addActivityInSchedule(dayPicker.getValue(), activity);
        }
        HourMinute newStart = newInterval(startHour, startMinute, activity.getStart().toString());
        HourMinute newEnd = newInterval(endHour, endMinute, activity.getEnd().toString());
        if(newStart.compareTo(newEnd) > 0) {
            errorField.setText("Start time must be before end time");
            return;
        }
        activity.setStart(newStart);
        activity.setEnd(newEnd);
        CalendarService.setActivity(dayPicker.getValue() == null ? day : dayPicker.getValue(), activity);
        resetCurrentData();
        resetFields();
    }

    /**
     * Metoda pentru initializarea componentelor grafice
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

        List.of("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday").
                stream().filter(day -> CalendarViewerController.getCalendar().getSchedule().get(day) != null)
                .forEach(day -> CalendarViewerController.getCalendar().getSchedule().get(day).forEach(
                        a -> activitiesViewer.getChildren().add(makeButton(a, day)) ));
    }
}

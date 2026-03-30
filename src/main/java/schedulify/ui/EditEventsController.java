package schedulify.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
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
 * Clasa pentru functionalitatea din fereastra de editare a evenimentelor
 */
public class EditEventsController implements Initializable {
    private Event currentEvent;
    private ActionEvent actionEvent;

    @FXML
    private Text errorField;
    @FXML
    private Text currentEventName;
    @FXML
    private TextField latField;
    @FXML
    private TextField longField;
    @FXML
    private TextField nameField;
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
    @FXML
    private VBox eventsViewer;

    /**
     * Metoda pentru resetarea campurilor
     */
    private void resetFields() {
        nameField.setText("");
        startDate.setValue(null);
        startHour.setValue(null);
        startMinute.setValue(null);
        endHour.setValue(null);
        endMinute.setValue(null);
        errorField.setText("");
        currentEventName.setText("");
        latField.setText("");
        longField.setText("");
    }

    /**
     * Metoda pentru crearea unui buton
     * @param event Evenimentul pentru care se va crea butonul
     * @return Butonul creat
     */
    private Button makeButton(Event event) {
        Button button = new Button(event.toString());
        button.setPrefWidth(250);
        button.setPrefHeight(30);
        button.setStyle("-fx-background-color: #ECECEC;");
        button.setOnAction(e -> setEvent(event, e));
        return button;
    }

    /**
     * Metoda pentru setarea evenimentului curent si a action eventului corect
     * @param event Evenimentul curent
     * @param actionEvent Action eventul corect
     */
    @FXML
    private void setEvent(Event event, ActionEvent actionEvent) {
        this.currentEvent = event;
        this.actionEvent = actionEvent;
        if(event != null) currentEventName.setText(event.getActivityName());
    }

    /**
     * Metoda pentru crearea ferestrei pentru selectarea unei locatii
     */
    @FXML
    private void handleSelectLocation() {
        CalendarUtils.handleSelectLocation(latField, longField, selectLocationButton);
    }

    /**
     * Metoda pentru stergerea evenimentului curent
     */
    @FXML
    private void deleteEvent() {
        if(currentEvent == null) return;
        CalendarService.deleteEvent(currentEvent.getID());
        CalendarViewerController.getCalendar().deleteEvent(currentEvent);
        ((Button) actionEvent.getSource()).setDisable(true);
        setEvent(null, null);
        resetFields();
    }

    /**
     * Metoda Pentru salvarea modificarii
     */
    @FXML
    private void saveChanges() {
        if(currentEvent == null) return;
        HourMinute newStart = EditScheduleController.newInterval(startHour, startMinute, currentEvent.getStart().toString());
        HourMinute newEnd = EditScheduleController.newInterval(endHour, endMinute, currentEvent.getEnd().toString());
        if(newStart.compareTo(newEnd) > 0) {
            errorField.setText("Start time must be before end time");
            return;
        }
        currentEvent.setStart(newStart);
        currentEvent.setEnd(newEnd);
        if (!latField.getText().isEmpty()) currentEvent.setLocation(latField.getText() + "," + longField.getText());
        if (!nameField.getText().isEmpty()) currentEvent.setActivityName(nameField.getText());
        if (startDate.getValue() != null) currentEvent.setEventDate(new Date(startDate.getValue().toString()));
        CalendarService.setEvent(currentEvent);
        setEvent(null, null);
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

        CalendarViewerController.getCalendar().getEvents().forEach(e -> eventsViewer.getChildren().add(makeButton(e)));
    }
}

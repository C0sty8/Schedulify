package schedulify.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import schedulify.ai.GeminiService;
import schedulify.models.Event;
import schedulify.service.CalendarService;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Clasa pentru functionalitatea din fereastra de evenimente sugerate
 */
public class SuggestedEventsController implements Initializable {
    private Event selectedEvent;
    private ActionEvent selectedButtonAE;

    @FXML
    private VBox scrollArea;
    @FXML
    private Text selectedEventData;

    /**
     * Metoda pentru crearea unui buton
     * @param event Evenimentul pentru care se va crea butonul
     * @return Un Buton
     */
    private Button makeButton(Event event) {
        Button b = new Button(event.toString());
        b.setPrefWidth(260);
        b.setPrefHeight(26);
        b.setOnAction(a -> selectEvent(a, event));
        b.setStyle("-fx-background-color: #ECECEC;");
        return b;
    }

    /**
     * Metoda pentru selectarea unui eveniment
     * @param actionEvent Action eventul butonului selectat
     * @param event Evenimentul selectat
     */
    @FXML
    private void selectEvent(ActionEvent actionEvent, Event event) {
        selectedEvent = event;
        selectedButtonAE = actionEvent;
        selectedEventData.setText(event.toString() + "\n" + event.getLocation());
    }

    /**
     * Metoda pentru adaugarea evenimentului selectat in calendar si in baza de date
     */
    @FXML
    private void addEvent() {
        ((Button) selectedButtonAE.getSource()).setDisable(true);
        CalendarService.addEventToDB(CalendarViewerController.getCalendar().getId(),
                selectedEvent.getActivityName(), selectedEvent.getEventDate().toString(),
                selectedEvent.getStart(), selectedEvent.getEnd(),
                selectedEvent.getLocation());
        int eventID = CalendarService.getEventId(CalendarViewerController.getCalendar().getId(),
                selectedEvent.getActivityName(), selectedEvent.getEventDate().toString(),
                selectedEvent.getStart(), selectedEvent.getEnd(),
                selectedEvent.getLocation());
        CalendarViewerController.getCalendar().addEvent(new Event(eventID, selectedEvent.getEventDate(),
                selectedEvent.getActivityName(),
                selectedEvent.getStart(), selectedEvent.getEnd(),
                selectedEvent.getLocation()));
        selectedEventData.setText("");
    }

    /**
     * Metoda pentru initializarea controller-ului
     * @param url Sursa din care au fost luate elementele grafice
     * @param resourceBundle Contine traduceri pentru textele din interfata
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GeminiService.writeInJSON(GeminiService.getResponse(GeminiService.makePrompt(CalendarViewerController.getCalendar())));
        GeminiService.getEvents().forEach(e -> scrollArea.getChildren().add(makeButton(e)));
    }
}

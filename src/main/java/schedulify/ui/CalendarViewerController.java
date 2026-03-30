package schedulify.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import schedulify.models.Calendar;
import schedulify.utils.CalendarUtils;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Clasa pentru functionalitatea ferestrei de vizualizare a orarului
 */
public class CalendarViewerController implements Initializable {

    private static Calendar calendar;
    private static int userPermissions;

    @FXML
    private SubScene viewer;
    @FXML
    private Button editEventsButton;
    @FXML
    private Button editScheduleButton;
    @FXML
    private Button addEventsButton;
    @FXML
    private Button addActivitiesButton;
    @FXML
    private Button suggestedEventsButton;

    /**
     * Metoda pentru setarea calendarului curent
     * @param calendar Calendarul curent
     */
    public static void setCalendar(Calendar calendar) {
        CalendarViewerController.calendar = calendar;
    }

    /**
     * Metoda pentru setarea permisiunilor utilizatorului in calendarul curent
     * @param userPermissions Permisiunile utilizatorului
     */
    public static void setUserPermissions(int userPermissions) {
        CalendarViewerController.userPermissions = userPermissions;
    }

    /**
     * Metoda pentru vizualizarea unei ferestre
     * @param fxmlFile Informatiile despre fereastra
     */
    private void setViewer(String fxmlFile) {
        try {
            Parent newNode = FXMLLoader.load(getClass().getResource(fxmlFile));

            viewer.setRoot(newNode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda pentru obtinerea permisiunilor utilizatorului
     * @return Permisiunile utilizatorului
     */
    public static int getUserPermissions() {
        return userPermissions;
    }

    /**
     * Metoda pentru obtinerea calendarului curent
     * @return Calendarul curent
     */
    public static Calendar getCalendar() {
        return calendar;
    }

    /**
     * Metoda pentru redirectionarea la fereastra principala a aplicatiei
     * @param event Reprezinta evenimentul declansat
     */
    @FXML
    public void backToMainScene(ActionEvent event) {
        calendar = null;
        userPermissions = 0;
        MainAppController.setScene(event, "/schedulify/ui/main_scene.fxml", "Schedulify");
    }

    /**
     * Metoda pentru setarea view-ului la fereastra de adaugare eveniment
     */
    @FXML
    public void onAddEvent() {
        setViewer("/schedulify/ui/add_event.fxml");
    }

    /**
     * Metoda pentru setarea view-ului la fereastra de adaugare activitate
     */
    @FXML
    public void onAddActivity() {
        setViewer("/schedulify/ui/add_activity.fxml");
    }

    /**
     * Metoda pentru setarea view-ului la fereastra de vizualizare orar
     */
    @FXML
    public void onViewSchedule() {
        setViewer("/schedulify/ui/view_schedule.fxml");
    }

    /**
     * Metoda pentru setarea view-ului la fereastra de editare orar
     */
    @FXML
    public void onEditSchedule() {
        setViewer("/schedulify/ui/edit_schedule.fxml");
    }

    /**
     * Metoda pentru setarea view-ului la fereastra de editare evenimente
     */
    @FXML
    public void onEditEvents() {
        setViewer("/schedulify/ui/edit_events.fxml");
    }

    /**
     * Metoda pentru setarea view-ului la fereastra de setari calendarului
     * @param event ActionEvent-ul ferestrei principale
     */
    @FXML
    public void onSettings(ActionEvent event) {
        CalendarSettingsController.setMainEvent(event);
        setViewer("/schedulify/ui/calendar_settings.fxml");
    }

    /**
     * Metoda pentru setarea view-ului la fereastra de evenimente sugerate
     */
    @FXML
    public void onSuggestedEvents() {
        setViewer("/schedulify/ui/suggested_events.fxml");
    }

    /**
     * Metoda pentru a face butoanele inactive in functie de permisiunile utilizatorului
     * @param url Sursa din care au fost luate elementele grafice
     * @param resourceBundle Contine traduceri pentru textele din interfata
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(userPermissions == 1) {
            CalendarUtils.makeInactive(editEventsButton, editScheduleButton, addEventsButton, addActivitiesButton, suggestedEventsButton);
        }
        setViewer("/schedulify/ui/view_schedule.fxml");
    }
}

package schedulify.ui;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import org.checkerframework.checker.units.qual.A;
import schedulify.ai.GeminiService;
import schedulify.models.Calendar;
import schedulify.models.Event;
import schedulify.models.User;
import schedulify.service.CalendarService;
import schedulify.service.DBUtils;
import schedulify.service.UserService;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.SequencedSet;

/**
 * Clasa pentru functionalitatea ferestrei principale
 */
public class MainAppController extends Application implements Initializable {
    /**
     * Utilizatorul curent
     */
    public static User user;
    @FXML
    private Text userName;
    @FXML
    private Text errorField;
    @FXML
    private TextField invitationField;
    @FXML
    private AnchorPane schedulesArea;

    static void main(String[] args) {
        launch(args);
    }

    /**
     * Metoda pentru modificarea utilizatorului
     * @param userData Noul utilizator
     */
    public static void setUserData(User userData) {
        user = userData;

        UserService.getUserCalendars(user.getId());
        user.getCalendars().forEach(c ->
                c.setSchedule(CalendarService.getActivities(c.getId())));
        user.getCalendars().forEach(c ->
                c.setEvents(CalendarService.getEvents(c.getId())));
    }

    /**
     * Metoda pentru modificarea scena curenta a aplicatiei
     * @param event Reprezinta evenimentul declansat, din care putem obtine stage-ul anterior si sa-i schimbam scena
     * @param fxmlFile Informatiile pentru noua scena
     * @param title Titlul noii scene
     */
    public static void setScene(ActionEvent event, String fxmlFile, String title) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(MainAppController.class.getResource(fxmlFile));
            Parent root = loader.load();
            Scene newScene = new Scene(root);
            stage.setTitle(title);
            stage.setScene(newScene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda pentru adaugarea utilizatorului la un calendar sau schimbarea permisiunii la un calendar.
     * Daca utilizatorul intra in calendarul in care e admin prin intermediul unui cod de invitatie, atunci
     * Permisiunile acestuia vor fi modificate si un nou admin va fi setat. Daca un utilizator este deja in calendar,
     * si intra printr-un cod de invitatie in acelasi calendar, permisiunile acestuia vor fi modificate corespunzator.
     */
    @FXML
    public void joinSchedule() {
        String invitation = invitationField.getText();
        if(invitation.isEmpty())
            errorField.setText("Please enter an invitation code");
        else {
            int permisionType = CalendarService.getInvitationType(invitation);
            int userID = user.getId();
            int calendarID = CalendarService.getScheduleID(invitation);

            if(permisionType == 0) errorField.setText("Invalid invitation code");
            else if(UserService.isUserAdmin(userID,calendarID)) {
                UserService.setCalendarUserPermission(userID, calendarID, permisionType, true);
                UserService.setNewCalendarAdmin(calendarID);
            }
            else if(UserService.isUserInCalendar(userID,calendarID)){
                UserService.setCalendarUserPermission(userID, calendarID, permisionType, false);
            }
            else {
                UserService.addCalendarUser(user.getId(), invitation, permisionType);
                user.addCalendar(CalendarService.getCalendar(invitation));
                Calendar lastCalendar = user.getCalendars().getLast();
                lastCalendar.setSchedule(CalendarService.getActivities(lastCalendar.getId()));
                lastCalendar.setEvents(CalendarService.getEvents(lastCalendar.getId()));
                schedulesArea.getChildren().add(createButton(schedulesArea.getChildren().size() + 1, lastCalendar.getName()));
            }
        }
        invitationField.clear();
    }

    /**
     * Metoda pentru schibarea scenei in cea pentru crearea unui nou calendar
     * @param event Reprezinta evenimentul declansat, din care putem obtine stage-ul anterior si sa-i schimbam scena
     */
    @FXML
    public void calendarMakerScene(ActionEvent event) {
        setScene(event, "/schedulify/ui/create_calendar.fxml", "Create Calendar");
    }

    /**
     * Metoda pentru deconectarea utilizatorului
     * @param event Reprezinta evenimentul declansat
     */
    @FXML
    public void logOut(ActionEvent event) {
        user = null;
        setScene(event, "/schedulify/ui/authentication/login.fxml", "Login");
    }

    /**
     * Metoda pentru seta scena si calendarul curent
     * @param event ActionEvent-ul declansat
     * @param fxmlFile Informatiile pentru noua scena
     * @param title Titlul noii scene
     */
    @FXML
    public void setSceneAndSetCalendarData(ActionEvent event, String fxmlFile, String title) {
        int calendarIndex = Integer.parseInt(Arrays.stream(((Button) event.getSource()).getText().split("[.]")).
                toList().getFirst()) - 1;
        CalendarViewerController.setCalendar(user.getCalendars().get(calendarIndex));
        CalendarViewerController.setUserPermissions(UserService.getUserPermissionType(
                user.getId(), CalendarViewerController.getCalendar().getId()));
        setScene(event, fxmlFile, title);
    }

    /**
     * Metoda pentru crearea unui buton
     * @param index Index pentru calcularea pozitiei
     * @param text Textul de pe buton
     * @return Un buton cu o pozitie si marime specifica
     */
    private Button createButton(int index, String text) {
        Button b = new Button(index + ". " + text);
        b.setPrefWidth(250);
        b.setPrefHeight(30);
        b.setLayoutY(35 * (index-1));
        b.setLayoutX(10);
        b.setStyle("-fx-background-color: #ECECEC;");
        b.setOnAction(a -> setSceneAndSetCalendarData(a, "/schedulify/ui/calendar_viewer.fxml", text));
        return b;
    }

    /**
     * Suprascrierea metodei initialize pentru a afisa butoane pentru calendarele utilizatorului
     * @param url Sursa din care au fost luate elementele grafice
     * @param resourceBundle Contine trauceri pentru textele din interfata
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userName.setText("Hello, " + user.getUsername());

        int i = 1;
        schedulesArea.getChildren().clear();
        for (Calendar c : user.getCalendars()) {
            schedulesArea.getChildren().add(createButton(i, c.getName()));
            i++;
        }
    }

    /**
     * Metoda pentru inceputul aplicatiei
     * @param stage Stage-ul principal al aplicatiei
     */
    @Override
    public void start(Stage stage){
        try {
            DBUtils.ensureDBExists();
            DBUtils.ensureTablesExists();
            Parent root = FXMLLoader.load(getClass().getResource("/schedulify/ui/authentication/login.fxml"));
            Scene scene = new Scene(root);
            Image icon = new Image("img.png");

            stage.getIcons().add(icon);
            stage.setTitle("Login");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

package schedulify.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import schedulify.models.User;
import schedulify.service.CalendarService;
import schedulify.service.UserService;
import schedulify.utils.CalendarUtils;

import java.net.URL;
import java.util.*;

/**
 * Clasa pentru functionalitatea din fereastra de setari a calendarului
 */
public class CalendarSettingsController implements Initializable {
    private static ActionEvent mainEvent;
    private Map<Integer, List<User>> participants = new HashMap<>();
    private User selectedUser;
    private int selectedUserPerm;

    @FXML
    private VBox participantsViewer;
    @FXML
    private Text editorCodeText;
    @FXML
    private Text normalCodeText;
    @FXML
    private TextField editorCodeField;
    @FXML
    private TextField normalCodeField;
    @FXML
    private Button deleteCalendarButton;
    @FXML
    private Button removeUserButton;
    @FXML
    private Button promoteButton;
    @FXML
    private Button demoteButton;
    @FXML
    private Button viewLocationButton;

    /**
     * Metoda pentru setarea action eventului principal
     * @param event Obiectul ActionEvent
     */
    public static void setMainEvent(ActionEvent event) {
        mainEvent = event;
    }

    /**
     * Metoda pentru obtinerea numelui participantului alaturi de titlul acestuia
     * @param perm Tipul de permisiune
     * @param participantName Numele participantului
     * @return Textul cu titlul si numele participantului
     */
    private String getText(int perm, String participantName) {
        String prefix = perm == 3 ? "ADMIN" : perm == 2 ? "EDITOR" : "";
        return prefix + " | " + participantName;
    }

    /**
     * Metoda pentru crearea unui buton
     * @param perm Tipul de permisiune
     * @param participant Participanatul
     * @return Obiectul de tip buton
     */
    private Button makeButton(int perm, User participant) {
        Button button = new Button(getText(perm, participant.getUsername()));
        button.setOnAction(e -> onUserSelect(perm, participant));
        button.setPrefWidth(200);
        button.setPrefHeight(26);
        button.setStyle("-fx-background-color: #ECECEC;");
        return button;
    }

    /**
     * Metoda pentru redirectionarea catre fereastra principala
     * @param calendarID ID-ul calendarului
     */
    private void backToMain(int calendarID) {
        MainAppController.user.getCalendars().removeIf(c -> c.getId() == calendarID);
        CalendarViewerController.setCalendar(null);
        CalendarViewerController.setUserPermissions(0);
        MainAppController.setScene(mainEvent, "/schedulify/ui/main_scene.fxml", "Schedulify");
    }

    /**
     * Metoda pentru selectarea unui participant
     * @param perm Tipul de permisiune al participantului selectat
     * @param participant Participanatul selectat
     */
    @FXML
    public void onUserSelect(int perm, User participant) {
        this.selectedUserPerm = perm;
        this.selectedUser = participant;
    }

    /**
     * Metoda pentru stergerea unui calendar
     */
    @FXML
    public void onDeleteCalendar() {
        int currentCalendarID = CalendarViewerController.getCalendar().getId();
        CalendarService.deleteCalendar(currentCalendarID);
        backToMain(currentCalendarID);
    }

    /**
     * Metoda pentru iesirea dintr-un calendar
     */
    @FXML
    public void onLeave() {
        int currentCalendarID = CalendarViewerController.getCalendar().getId();
        int userID = MainAppController.user.getId();
        if(UserService.isUserAdmin(userID,currentCalendarID)) {
            UserService.setCalendarUserPermission(userID, currentCalendarID, 1, true);
            UserService.setNewCalendarAdmin(currentCalendarID);
        }
        CalendarService.removeParticipant(MainAppController.user.getId(), CalendarViewerController.getCalendar().getId());
        backToMain(currentCalendarID);
    }

    /**
     * Metoda pentru stergerea unui participant din calendar
     */
    @FXML
    public void onRemoveParticipant() {
        if ( selectedUser == null || selectedUser.getId() == MainAppController.user.getId()) return;
        CalendarService.removeParticipant(selectedUser.getId(), CalendarViewerController.getCalendar().getId());
    }

    /**
     * Metoda pentru promovarea unui participant la admin sau editor
     */
    @FXML
    public void onPromote() {
        if ( selectedUser == null || selectedUserPerm == 3 || MainAppController.user.getId() == selectedUser.getId()) return;
        UserService.setCalendarUserPermission(selectedUser.getId(), CalendarViewerController.getCalendar().getId(),
                selectedUserPerm + 1, false);
    }

    /**
     * Metoda pentru scaderea permisiunii unui participant la editor sau normal
     */
    @FXML
    public void onDemote() {
        if ( selectedUser == null || selectedUserPerm == 1 || MainAppController.user.getId() == selectedUser.getId()) return;
        UserService.setCalendarUserPermission(selectedUser.getId(), CalendarViewerController.getCalendar().getId(),
                selectedUserPerm - 1, false);
    }

    /**
     * Metoda pentru vizualizarea unei locatii
     */
    @FXML
    private void handleLocationViewer() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("location_viewer.fxml"));
            Parent root = loader.load();

            LocationViewerController.setLocation(CalendarViewerController.getCalendar().getLocation());

            Stage mapStage = new Stage();
            mapStage.setTitle("Location");
            mapStage.setScene(new Scene(root));

            mapStage.initModality(Modality.APPLICATION_MODAL);
            mapStage.initOwner(viewLocationButton.getScene().getWindow());
            mapStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda pentru a face butoanele si datele calendarului invalide in functie de permisiunea utilizatorului
     * @param url Sursa din care au fost luate elementele grafice
     * @param resourceBundle Contine traduceri pentru textele din interfata
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        participants = UserService.getCalendarParticipants(CalendarViewerController.getCalendar().getId());

        if (participants == null) return;
        List<Integer> keys = new ArrayList<>(participants.keySet().stream().toList());
        Collections.sort(keys, Collections.reverseOrder());

        if (CalendarViewerController.getUserPermissions() == 3) {
            String[] codes = CalendarService.getCodes(CalendarViewerController.getCalendar().getId());

            editorCodeField.setText(codes[0]);
            normalCodeField.setText(codes[1]);

            keys.forEach(p -> participants.get(p).forEach(
                    u -> participantsViewer.getChildren().add(makeButton(p, u))));
        }
        else {
            keys.forEach(p -> participants.get(p).forEach(
                    u -> participantsViewer.getChildren().add(CalendarUtils.makeText(getText(p, u.getUsername()),14))));
            CalendarUtils.makeInactive(deleteCalendarButton, removeUserButton, promoteButton, demoteButton);
            editorCodeText.setVisible(false);
            normalCodeText.setVisible(false);
            editorCodeField.setVisible(false);
            normalCodeField.setVisible(false);
        }
    }
}

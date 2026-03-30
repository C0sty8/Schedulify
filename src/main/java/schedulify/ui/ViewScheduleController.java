package schedulify.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.stage.Modality;
import javafx.stage.Stage;
import schedulify.models.Event;
import schedulify.utils.CalendarUtils;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Clasa pentru functionalitatea din fereastra de vizualizare a programului de programare
 */
public class ViewScheduleController implements Initializable
{
    @FXML
    private VBox eventViewer;
    @FXML
    private HBox scheduleViewer;

    /**
     * Metoda pentru crearea unui buton
     * @param event Evenimentul pentru care se va crea butonul
     * @return Butonul creat
     */
    private Button makeButton(Event event) {
        Button b = new Button(event.toString());
        b.setOnAction(e -> handleLocationViewer(event.getLocation()));
        b.setStyle("-fx-background-color: #ECECEC;");
        b.setPrefWidth(200);
        b.setPrefHeight(26);
        return b;
    }

    /**
     * Metoda pentru crearea unei ferestre pentru vizualizarea unei locatii
     * @param location Coordonatele unei locatii
     */
    @FXML
    private void handleLocationViewer(String location) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("location_viewer.fxml"));
            Parent root = loader.load();

            LocationViewerController.setLocation(location);

            Stage mapStage = new Stage();
            mapStage.setTitle("Event Location");
            mapStage.setScene(new Scene(root));

            mapStage.initModality(Modality.APPLICATION_MODAL);
            mapStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda pentru initializarea componentelor grafice
     * @param url Sursa din care au fost luate elementele grafice
     * @param resourceBundle Contine traduceri pentru textele din interfata
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<String> days = List.of("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");

        CalendarViewerController.getCalendar().getEvents().forEach(
                e -> eventViewer.getChildren().add(makeButton(e)));

        for(String day : days) {
            if (CalendarViewerController.getCalendar().getSchedule().containsKey(day)) {
                VBox box = new VBox();
                box.setStyle("-fx-border-color: #F1EFEC;");
                box.getChildren().add(CalendarUtils.makeText(day,16));
                box.getChildren().add(new Line());
                CalendarViewerController.getCalendar().getSchedule().get(day).forEach(a ->
                        box.getChildren().add(CalendarUtils.makeText(a.toString(), 14)));
                scheduleViewer.getChildren().add(box);
            }
        }
    }
}

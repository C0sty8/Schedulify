package schedulify.authentication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import schedulify.service.DBUtils;
import schedulify.service.UserService;
import schedulify.ui.MainAppController;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Clasa pentru functionalitatea din fereastra de logare
 */
public class LoginController {
    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField emailField;

    @FXML
    private Text errorField;

    /**
     * Metoda pentru verificarea datelor de logare
     * @param event Reprezinta evenimentul declansat
     */
    @FXML
    public void validateLogin(ActionEvent event) {
        String verifyLogin = "select count(1) from users_data where email = '" + emailField.getText() + "'";

        try {
            DBUtils conection = new DBUtils();
            Connection conectDB = conection.getConnection();
            Statement statement = conectDB.createStatement();
            ResultSet rs = statement.executeQuery(verifyLogin);
            rs.next();

            if (rs.getInt(1) == 1 && UserService.passwordIsValid(emailField.getText(), passwordField.getText())) {
                MainAppController.setUserData(UserService.getUser(emailField.getText()));
                MainAppController.setScene(event, "/schedulify/ui/main_scene.fxml", "Schedulify");
            } else {
                errorField.setText("Invalid email or password");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda pentru redirectionarea la fereastra de creare a unui nou cont
     * @param event Reprezinta evenimentul declansat
     */
    @FXML
    public void switchToSignUp(ActionEvent event) {
        MainAppController.setScene(event, "/schedulify/ui/authentication/sign_up.fxml", "Sign Up");
    }
}

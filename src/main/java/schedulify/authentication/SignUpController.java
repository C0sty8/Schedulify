package schedulify.authentication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import schedulify.service.DBUtils;
import schedulify.service.UserService;
import schedulify.ui.MainAppController;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Clasa pentru functionalitatea din fereastra de inregistrare
 */
public class SignUpController {
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField confirmPasswordField;
    @FXML
    private TextField nameField;
    @FXML
    private Text errorField;

    /**
     * Verificarea daca emailul introdus este unic
     * @param email Emailul introdus
     * @return true daca emailul este unic, false altfel
     */
    private static boolean isUniqueEmail(String email) {
        String verifyLogin = "select count(1) from users_data where email = '" + email + "'";

        try {

            DBUtils conection = new DBUtils();
            Connection conectDB = conection.getConnection();

            Statement statement = conectDB.createStatement();
            ResultSet rs = statement.executeQuery(verifyLogin);

            rs.next();
            return rs.getInt(1) == 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Verificarea daca emailul introdus respecte formatul necesar
     * @param email Emailul introdus
     * @return true daca emailul respecte formatul necesar, false altfel
     */
    private static boolean isValidEmailFormat(String email) {
        Pattern pattern = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+[.][A-Za-z0-9.-]+");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * Metoda pentru validarea datelor introduse de utilizator
     * @param event Reprezinta evenimentul declansat
     */
    @FXML
    public void validateSignUp(ActionEvent event) {
        if (emailField.getText().isEmpty() || passwordField.getText().isEmpty() || confirmPasswordField.getText().isEmpty() || nameField.getText().isEmpty()) {
            errorField.setText("All fields must be completed");
        }
        else if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            errorField.setText("Passwords do not match");
        }
        else if (!isValidEmailFormat(emailField.getText())) {
            errorField.setText("Invalid email format");
        }
        else if (!isUniqueEmail(emailField.getText())) {
            errorField.setText("Email is already in use");
        }
        else {
            UserService.addUser(emailField.getText(), nameField.getText(), passwordField.getText());
            MainAppController.setUserData(UserService.getUser(emailField.getText()));
            MainAppController.setScene(event,"/schedulify/ui/main_scene.fxml", "Schedulify");
        }
    }

    /**
     * Metoda pentru redirectionarea catre fereastra de login
     * @param event Reprezinta evenimentul declansat
     */
    @FXML
    public void switchToLogin(ActionEvent event) {
        MainAppController.setScene(event, "/schedulify/ui/authentication/login.fxml", "Login");
    }
}

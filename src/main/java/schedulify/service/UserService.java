package schedulify.service;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import schedulify.models.Calendar;
import schedulify.models.Date;
import schedulify.models.User;
import schedulify.ui.MainAppController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Clasa pentru gestionarea logici dintre utilizator si baza de date
 */
public class UserService {
    private static final Argon2 argon2 = Argon2Factory.create();

    /**
     * Metoda pentru verificarea daca o parola este corecta
     * @param email Email-ul utilizatorului
     * @param password Parola care se va verifica
     * @return true daca parola este corecta, false altfel
     */
    public static boolean passwordIsValid(String email, String password) {
        try {

            Connection conectDB = new DBUtils().getConnection();

            Statement statement = conectDB.createStatement();
            ResultSet rs = statement.executeQuery(
                    "select password from users_data where email = '" + email + "'"
            );
            if(! rs.next()) return false;
            return argon2.verify(rs.getString(1), password.toCharArray());

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Metoda pentru verificarea daca un cod de invitatie este unic
     * @param invitationCode Codul de invitatie
     * @return true daca codul este unic, false altfel
     */
    public static boolean isUniqueInvitation(String invitationCode) {
        try {
            
            Connection conectDB = new DBUtils().getConnection();

            Statement statement = conectDB.createStatement();
            ResultSet rs = statement.executeQuery(
                    "SELECT COUNT(1) FROM calendars_data" +
                    " WHERE edinv = '" + invitationCode + "' OR norminv = '" + invitationCode + "'"
            );
            rs.next();
            return rs.getInt(1) == 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Metoda verifica daca un utilizator participa la un calendar
     * @param userID ID-ul utilizatorului
     * @param calendarID ID-ul calendarului in care se face verificarea
     * @return true daca utilizatorul participa, false altfel
     */
    public static boolean isUserInCalendar(int userID, int calendarID) {
        try {
            
            Connection conectDB = new DBUtils().getConnection();
            Statement statement = conectDB.createStatement();
            ResultSet rs = statement.executeQuery(
                    "SELECT COUNT(1) FROM calendar_user WHERE user_id = '" + userID + "' AND calendar_id = '" + calendarID + "';"
            );
            if (!rs.next()) return false;
            return rs.getInt(1) == 1;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Verifica daca un utilizator este administrator unui calendar
     * @param userID ID-ul utilizatorului
     * @param calendarID ID-ul calendarului in care se face verificarea
     * @return true daca utilizatorul este administrator, false altfel
     */
    public static boolean isUserAdmin(int userID, int calendarID) {
        try {
            
            Connection conectDB = new DBUtils().getConnection();
            Statement statement = conectDB.createStatement();
            ResultSet rs = statement.executeQuery(
                    "SELECT COUNT(1) FROM calendar_user WHERE user_id = '" + userID + "' AND calendar_id = '" + calendarID + "' AND perm = 3;"
            );
            if (!rs.next()) return false;
            return rs.getInt(1) == 1;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Metoda pentru adaugarea unui nou calendar in baza de date
     * @param name Numele calendarului
     * @param start Data de inceputa
     * @param end Data de sfarsit
     * @param edInv Codul de invitatie pentru editori
     * @param normInv Codul de invitatie normal
     * @param location Locul unde o sa se desfasoare activitatile
     */
    public static void addCalendarToDB(String name, String start, String end , String edInv, String normInv, String location) {

        try {
            
            Connection conectDB = new DBUtils().getConnection();

            Statement statement = conectDB.createStatement();
            PreparedStatement ps = conectDB.prepareStatement(
                    "INSERT INTO calendars_data (cal_name, start_date, end_date, edinv, norminv, location) " +
                    "VALUES (?,?,?,?,?,?);"
            );

            ps.setString(1, name);
            java.time.LocalDate startLocal = java.time.LocalDate.parse(start);
            java.time.LocalDate endLocal = java.time.LocalDate.parse(end);
            ps.setDate(2, java.sql.Date.valueOf(startLocal));
            ps.setDate(3, java.sql.Date.valueOf(endLocal));
            ps.setString(4, edInv);
            ps.setString(5, normInv);
            ps.setString(6, location);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda pentru adaugarea unui utilizator in calendarul selectat
     * @param userID ID-ul utilizatorului
     * @param invitation Unul dintre codurile de invitatie ale calendarului
     * @param permission Permisiunea utilizatorului (1 - doar vizualizare, 2 - editare, 3 - administrator)
     */
    public static void addCalendarUser(int userID, String invitation, int permission) {
        try {
            
            Connection conectDB = new DBUtils().getConnection();

            Statement statement = conectDB.createStatement();
            ResultSet rs = statement.executeQuery("SELECT calendar_id FROM calendars_data WHERE " +
                    "edinv = '" + invitation + "' OR norminv = '" + invitation + "';");
            if (rs.next()) {
                PreparedStatement ps = conectDB.prepareStatement(
                        "INSERT INTO calendar_user (user_id, calendar_id, perm, join_date) VALUES (?,?,?,NOW());"
                );
                ps.setInt(1, userID);
                ps.setInt(2, rs.getInt(1));
                ps.setInt(3, permission);
                ps.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda pentru adaugarea unui utilizator in baza de date
     * @param email Email-ul utilizatorului
     * @param userName Numele utilizatorului
     * @param password Parola utilizatorului careia i se va salva doar hash-ul
     */
    public static void addUser(String email, String userName, String password) {
        try {
            
            Connection conectDB = new DBUtils().getConnection();
            PreparedStatement ps = conectDB.prepareStatement(
                    "INSERT INTO users_data (email, user_name, password) VALUES (?,?,?);"
            );

            ps.setString(1, email);
            ps.setString(2, userName);
            ps.setString(3, argon2.hash(4, 1024, 2, password.toCharArray()));
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda pentru obtinerea informatiilor despre un utilizator din baza de date
     * @param email emailul utilizatorului
     * @return Un obiect User
     */
    public static User getUser(String email) {
        try {
            
            Connection conectDB = new DBUtils().getConnection();
            Statement data = conectDB.createStatement();
            ResultSet dataRs = data.executeQuery(
                    "SELECT user_id, user_name FROM users_data WHERE email = '" + email + "'"
            );
            dataRs.next();
            return new User(dataRs.getInt(1), dataRs.getString(2));
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Metoda pentru obtinerea calendarelor unui utilizator
     * @param userID ID-ul utilizatorului
     */
    public static void getUserCalendars(int userID) {
        try {
            Connection conectDB = new DBUtils().getConnection();
            Statement data = conectDB.createStatement();
            ResultSet dataRs = data.executeQuery(
                    "SELECT calendar_id, cal_name, start_date, end_date, location " +
                            "FROM calendar_user NATURAL JOIN calendars_data " +
                            "WHERE user_id = " + userID + ";"
            );
            while (dataRs.next()) {
                MainAppController.user.addCalendar(new Calendar(
                        dataRs.getInt(1),
                        dataRs.getString(2),
                        new Date(dataRs.getString(3)),
                        new Date(dataRs.getString(4)),
                        dataRs.getString(5)
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda pentru obtinerea permisiunii unui utilizator intr-un calendar
     * @param userID ID-ul utilizatorului caruia vrem sa ii obtinem tipul de permisiune
     * @param calendarID ID-ul calendarului in care se afla utilizatorul
     * @return Permisiunea utilizatorului
     */
    public static int getUserPermissionType(int userID, int calendarID) {
        try {
            Connection conectDB = new DBUtils().getConnection();
            Statement data = conectDB.createStatement();
            ResultSet dataRs = data.executeQuery(
                    "SELECT perm " +
                            "FROM calendar_user " +
                            "WHERE user_id = " + userID + " AND calendar_id = " + calendarID + ";"
            );
            if (dataRs.next()) return dataRs.getInt(1);

            } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Metoda pentru obtinerea lista de utilizatori care participa la un calendar, grupat dupa permisiunea acestora
     * @param calendarID ID-ul calendarului in care se afla utilizatorii
     * @return Paticipantii unui calendar, grupat dupa permisiunea acestora
     */
    public static Map<Integer,List<User>> getCalendarParticipants(int calendarID) {
        Map<Integer,List<User>> participants = new HashMap<>();
        try {
            Connection conectDB = new DBUtils().getConnection();
            Statement data = conectDB.createStatement();
            ResultSet dataRs = data.executeQuery(
                    "SELECT u.user_id, u.user_name, m.perm FROM users_data u " +
                            "JOIN calendar_user m ON u.user_id = m.user_id " +
                            "WHERE m.calendar_id = " + calendarID + ";"
            );
            while (dataRs.next()) {
                int permType = dataRs.getInt(3);
                if(!participants.containsKey(permType))
                    participants.put(permType, new ArrayList<>());
                participants.get(permType).add(new User(dataRs.getInt(1), dataRs.getString(2)));
            }

            return participants;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Metoda pentru modificarea permisiunii unui utilizator intr-un calendar
     * @param userID ID-ul utilizatorului
     * @param calendarID ID-ul calendarului in care se face modificarea
     * @param permission Noul nivel de permisiune
     * @param newDate true pentru actualizarea datei de intrare in calendar, false altfel
     */
    public static void setCalendarUserPermission(int userID, int calendarID, int permission, boolean newDate) {
        try {
            Connection conn = new DBUtils().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(
                        "UPDATE calendar_user SET perm = ?" + (newDate ? ", join_date = NOW()" : "") +
                            " WHERE user_id = ? AND calendar_id = ?"
            );
            pstmt.setInt(1, permission);
            pstmt.setInt(2, userID);
            pstmt.setInt(3, calendarID);
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda pentru setarea utilizatorului cu cea mai mare permisiune si vechime ca administrator
     * @param calendarID ID-ul calendarului in care se face modificarea
     */
    public static void setNewCalendarAdmin(int calendarID) {
        try {
            Connection conn = new DBUtils().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(
                    "UPDATE calendar_user SET perm = 3 " +
                            "WHERE calendar_id = ? AND user_id IN " +
                            "( SELECT user_id FROM( SELECT user_id " +
                            "FROM calendar_user WHERE calendar_id = ? " +
                            "ORDER BY perm DESC, join_date, user_id " +
                            "LIMIT 1) AS temp);"
            );
            pstmt.setInt(1, calendarID);
            pstmt.setInt(2, calendarID);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

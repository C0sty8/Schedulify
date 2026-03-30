package schedulify.service;

import schedulify.models.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Clasa pentru gestionarea logici dintre calendare si baza de date
 */
public class CalendarService {

    /**
     * Metoda pentru obtinerea ID-ului unui orar din baza de date
     * prin intermediul unui cod de invitatie
     * @param invitation Codul de invitatie
     * @return ID-ul unui orar din baza de date
     */
    public static int getScheduleID(String invitation) {
        try {
            Connection conection = new DBUtils().getConnection();
            Statement statement = conection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "SELECT calendar_id FROM calendars_data WHERE edinv = '"
                            + invitation + "' or norminv = '" + invitation + "';"
            );
            if(!resultSet.next()) return -1;
            return resultSet.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Metoda pentru obtinerea datelor unui orar prin intermediul unui cod de invitatie
     * @param invitation Codul de invitatie
     * @return Datele unui orar
     */
    public static Calendar getCalendar(String invitation) {
        try {
            Connection conection = new DBUtils().getConnection();
            Statement statement = conection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "SELECT calendar_id, cal_name, start_date, end_date, location " +
                            "FROM calendars_data " +
                            "WHERE edinv = '" + invitation + "' OR norminv = '" + invitation + "';"
            );
            if(!resultSet.next()) return null;
            return new Calendar(
                    resultSet.getInt(1),
                    resultSet.getString(2),
                    new Date(resultSet.getString(3)),
                    new Date(resultSet.getString(4)),
                    resultSet.getString(5)
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Metoda pentru obtinerea tipului de invitatie (normal sau editor) prin intermediul unui cod de invitatie
     * @param invitation Codul de invitatie
     * @return 0 daca invitatia este invalida, 1 daca este normal, 2 daca este editor
     */
    public static int getInvitationType(String invitation){
        try {
            Connection conection = new DBUtils().getConnection();
            Statement statement = conection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "SELECT norminv FROM calendars_data WHERE edinv = '" + invitation + "' or norminv = '" + invitation + "';"
            );
            if(!resultSet.next()) return 0;

            if(resultSet.getString(1).equals(invitation)) return 1;
            return 2;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Metoda pentru obtinerea ID-ului unui eveniment din baza de date
     * @param calendarID ID-ul calendarului in care se afla evenimentul
     * @param eventName Numele evenimentului
     * @param eventDate Data evenimentului
     * @param start Timul la care incepe
     * @param end Timul la care se termina 
     * @param location Locul unde se va desfasoara evenimentul
     * @return ID-ul evenimentului
     */
    public static int getEventId(int calendarID, String eventName, String eventDate, HourMinute start, HourMinute end, String location) {
        try {
            Connection conection = new DBUtils().getConnection();
            Statement statement = conection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "SELECT event_id FROM schedules_events WHERE calendar_id = " + calendarID + " AND " +
                            "ename = '" + eventName + "' AND edate = '" + eventDate + "' AND start_hour = '" + start +
                            ":00' AND end_hour = '" + end + ":00' AND location = '" + location + "';"
            );
            if(!resultSet.next()) return -1;
            return resultSet.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Metoda pentru obtinerea ID-ului unei activitati din baza de date
     * @param calendarID ID-ul calendarului in care se afla activitatea
     * @param activityName Numele activitatii
     * @param day Ziua in care se va desfasura activitatea
     * @param start Timpul la care incepe
     * @param end Timpul la care se termina
     * @return ID-ul activitatii
     */
    public static int getActivityID(int calendarID, String activityName, String day, HourMinute start, HourMinute end) {
        try {
            Connection conection = new DBUtils().getConnection();
            Statement statement = conection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "SELECT activity_id FROM def_schedules WHERE calendar_id = " + calendarID + " AND " +
                            "aname = '" + activityName + "' AND astart = '" + start +
                            ":00' AND aend = '" + end + ":00' AND aday = '" + day + "';"
            );
            if(!resultSet.next()) return -1;
            return resultSet.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Metoda pentru obtinerea activitatilor unui calendar
     * @param calendarID ID-ul calendarului din care vom lua activitatiile
     * @return Activitatiile calendarului
     */
    public static Map<String, Set<Activity>> getActivities(int calendarID) {
        Map<String, Set<Activity>> activities = new HashMap<>();

        try {
            Connection conection = new DBUtils().getConnection();
            Statement statement = conection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "SELECT activity_id,aname,TIME_FORMAT(astart, '%H:%i')," +
                            "TIME_FORMAT(aend, '%H:%i'),aday FROM " +
                            "def_schedules WHERE calendar_id = " + calendarID + ";"
            );
            while(resultSet.next()) {
                String key = resultSet.getString(5);
                if(activities.get(key) == null)
                    activities.put(key, new TreeSet<>());
                activities.get(key).add(new Activity(resultSet.getInt(1),
                        resultSet.getString(2),
                        new HourMinute(resultSet.getString(3)),
                        new HourMinute(resultSet.getString(4))));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return activities;
    }

    /**
     * Metoda pentru obtinerea evenimentelor unui calendar
     * @param calendarID Calendarul din care vom lua evenimentele
     * @return Evenimentele calendarului
     */
    public static Set<Event> getEvents(int calendarID) {
        Set<Event> events = new TreeSet<>();

        try {
            Connection conection = new DBUtils().getConnection();
            Statement statement = conection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "SELECT event_id, ename, edate, TIME_FORMAT(start_hour, '%H:%i'), " +
                            "TIME_FORMAT(end_hour, '%H:%i'), location FROM " +
                            "schedules_events WHERE calendar_id = " + calendarID + ";"
            );
            while(resultSet.next()) {
                events.add(new Event(resultSet.getInt(1),
                        new Date(resultSet.getString(3)),
                        resultSet.getString(2),
                        new HourMinute(resultSet.getString(4)),
                        new HourMinute(resultSet.getString(5)),
                        resultSet.getString(6)));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return events;
    }

    /**
     * Metoda pentru obtinerea codurilor de invitatie ale unui calendar
     * @param calendarID ID-ul calendarului din care vom lua codurile
     * @return Codurile de invitatie ale calendarului
     */
    public static String[] getCodes(int calendarID) {
        try {
            Connection conection = new DBUtils().getConnection();
            Statement statement = conection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "SELECT edinv, norminv FROM calendars_data WHERE calendar_id = " + calendarID + ";"
            );
            if(resultSet.next()) return new String[]{resultSet.getString(1), resultSet.getString(2)};
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String[0];
    }

    /**
     * Metoda pentru adaugarea unui eveniment in baza de date
     * @param calendarID ID-ul calendarului in care se va adauga evenimentul
     * @param eventName Numele evenimentului
     * @param eventDate Data de desfasurare a evenimentului
     * @param start Timul la care incepe
     * @param end Timul la care se termina
     * @param location Locul unde se va desfasoara evenimentul
     */
    public static void addEventToDB(int calendarID, String eventName, String eventDate, HourMinute start, HourMinute end, String location) {
        try {

            Connection conectDB = new DBUtils().getConnection();
            PreparedStatement ps = conectDB.prepareStatement(
                    "INSERT INTO schedules_events (calendar_id, ename, edate, start_hour, end_hour, location) " +
                            "VALUES (?,?,?,?,?,?);"
            );
            java.time.LocalDate dateLocal = java.time.LocalDate.parse(eventDate);

            ps.setInt(1, calendarID);
            ps.setString(2, eventName);
            ps.setDate(3, java.sql.Date.valueOf(dateLocal));
            ps.setTime(4, java.sql.Time.valueOf(start + ":00"));
            ps.setTime(5, java.sql.Time.valueOf(end + ":00"));
            ps.setString(6, location);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda pentru adaugarea unei activitati in baza de date
     * @param calendarID ID-ul calendarului in care se va adauga activitatea
     * @param activityName Numele activitatii
     * @param day Ziua in care se va desfasura activitatea
     * @param start Timpul la care incepe
     * @param end Timpul la care se termina
     */
    public static void addActivityToDB(int calendarID, String activityName, String day, HourMinute start, HourMinute end) {
        try {

            Connection conectDB = new DBUtils().getConnection();
            PreparedStatement ps = conectDB.prepareStatement(
                    "INSERT INTO def_schedules (calendar_id,aname,astart,aend,aday) VALUES (?,?,?,?,?);"
            );

            ps.setInt(1, calendarID);
            ps.setString(2, activityName);
            ps.setTime(3, java.sql.Time.valueOf(start + ":00"));
            ps.setTime(4, java.sql.Time.valueOf(end + ":00"));
            ps.setString(5, day);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda pentru stergerea unei activitati din baza de date
     * @param activityId ID-ul activitatii care va fi sters
     */
    public static void deleteActivity(int activityId) {
        try {
            Connection conectDB = new DBUtils().getConnection();
            PreparedStatement ps = conectDB.prepareStatement(
                    "DELETE FROM def_schedules WHERE activity_id = ?;"
            );

            ps.setInt(1, activityId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda pentru stergerea unui eveniment din baza de date
     * @param eventID ID-ul evenimentului care va fi sters
     */
    public static void deleteEvent(int eventID) {
        try {
            Connection conectDB = new DBUtils().getConnection();
            PreparedStatement ps = conectDB.prepareStatement(
                    "DELETE FROM schedules_events WHERE event_id = ?;"
            );

            ps.setInt(1, eventID);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda pentru stergerea unui calendar din baza de date
     * @param calendarID ID-ul calendarului care va fi sters
     */
    public static void deleteCalendar(int calendarID) {
        try {
            Connection conectDB = new DBUtils().getConnection();
            PreparedStatement ps = conectDB.prepareStatement(
                    "DELETE FROM calendars_data WHERE calendar_id = ?;"
            );

            ps.setInt(1,calendarID);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda pentru stergerea unui participant dintr-un calendar
     * @param participantID ID-ul participantului care va fi sters
     * @param calendarID ID-ul calendarului din care se va sterge participantul
     */
    public static void removeParticipant (int participantID, int calendarID) {
        try {
            Connection conectDB = new DBUtils().getConnection();
            PreparedStatement ps = conectDB.prepareStatement(
                    "DELETE FROM calendar_user WHERE calendar_id = ? AND user_id = ?;"
            );

            ps.setInt(1,calendarID);
            ps.setInt(2,participantID);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda pentru actualizarea datelor unei activitati
     * @param day Ziua in care se va desfasura activitatea
     * @param activity Activitatea cu datele actualizate
     */
    public static void setActivity(String day, Activity activity) {
        try {
            Connection conectDB = new DBUtils().getConnection();
            PreparedStatement ps = conectDB.prepareStatement(
                    "UPDATE def_schedules SET aname = ?, " +
                            "astart = ?, aend = ?, aday = ? " +
                            "WHERE activity_id = ?;"
            );

            ps.setString(1,activity.getActivityName());
            ps.setTime(2, java.sql.Time.valueOf(activity.getStart() + ":00"));
            ps.setTime(3, java.sql.Time.valueOf(activity.getEnd() + ":00"));
            ps.setString(4, day);
            ps.setInt(5,activity.getID());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda pentru actualizarea datelor unui eveniment
     * @param event Evenimentul cu datele actualizate
     */
    public static void setEvent(Event event) {
        try {
            Connection conectDB = new DBUtils().getConnection();
            PreparedStatement ps = conectDB.prepareStatement(
                    "UPDATE schedules_events SET ename = ?, " +
                            "start_hour = ?, end_hour = ?, edate = ?, location = ? " +
                            "WHERE event_id = ?;"
            );
            java.time.LocalDate dateLocal = java.time.LocalDate.parse(event.getEventDate().toString());

            ps.setString(1,event.getActivityName());
            ps.setTime(2, java.sql.Time.valueOf(event.getStart() + ":00"));
            ps.setTime(3, java.sql.Time.valueOf(event.getEnd() + ":00"));
            ps.setDate(4, java.sql.Date.valueOf(dateLocal));
            ps.setString(5, event.getLocation());
            ps.setInt(6,event.getID());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

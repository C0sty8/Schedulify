package schedulify.models;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * Clasa pentru retinerea datelor despre un utilizator
 */
public class User {
    private int id;
    private String userName;
    List<Calendar> calendars;

    /**
     * Constructor cu parametri pentru clasa User
     * @param id ID-ul utilizatorului
     * @param userName Numele utilizatorului
     */
    public User(int id, String userName) {
        this.userName = userName;
        this.id = id;
        calendars = new ArrayList<>();
    }

    /**
     * Metoda pentru obtinerea numelui utilizatorului
     * @return Numele utilizatorului
     */
    public String getUsername() {
        return userName;
    }

    /**
     * Metoda pentru obtinerea ID-ului utilizatorului
     * @return ID-ul utilizatorului
     */
    public int getId() {
        return id;
    }

    /**
     * Metoda pentru obtinerea calendarelor unui utilizator
     * @return Calendarele utilizatorului
     */
    public List<Calendar> getCalendars() {
        return calendars;
    }

    /**
     * Metoda pentru modificarea numelui utilizatorului
     * @param userName Noul nume
     */
    public void setUsername(String userName) {
        this.userName = userName;
    }

    /**
     * Metoda pentru adaugarea unui calendar nou in lista de calendare ale utilizatorului
     * @param calendar Noul calendar
     */
    public void addCalendar(Calendar calendar) {
        calendars.add(calendar);
    }

    /**
     * Suprascrierea metodei toString pentru a afisa datele utilizatorului
     * @return Stringul cu datele utilizatorului
     */
    @Override
    public String toString() {
        return id + ". " + userName;
    }
}

package schedulify.models;

import java.util.*;

/**
 * Clasa pentru retinerea datelor despre un calendar
 */
public class Calendar {
    private int id;
    private String name;
    private String location;
    private Date start;
    private Date end;
    private Set<Event> events;
    private Map<String, Set<Activity>> schedule;

    /**
     * Constructor cu parametri pentru clasa Calendar
     * @param id id-ul calendarului
     * @param name numele calendarului
     * @param start data de inceputa
     * @param end data de sfarsit
     * @param location locul unde se desfasoara activitatile
     */
    public Calendar(int id, String name, Date start, Date end, String location) {
        this.location = location;
        this.name = name;
        this.start = start;
        this.end = end;
        this.id = id;

        events = new TreeSet<>();
        schedule = new HashMap<>();
    }

    /**
     * Metoda pentru obtinerea numelui calendarului
     * @return Numele calendarului
     */
    public String getName() {
        return name;
    }

    /**
     * Metoda pentru obtinerea id-ului calendarului
     * @return ID-ul calendarului
     */
    public int getId() {
        return id;
    }

    /**
     * Metoda pentru obtinerea locului unde se desfasoara activitatile
     * @return Locul unde se desfasoara activitatile
     */
    public String getLocation() {
        return location;
    }

    /**
     * Metoda pentru obtinerea data de inceputa
     * @return Data de inceputa
     */
    public Date getStart() {
        return start;
    }

    /**
     * Metoda pentru obtinerea data de sfarsit
     * @return Data de sfarsit
     */
    public Date getEnd() {
        return end;
    }

    /**
     * Metoda pentru obtinerea evenimentelor
     * @return Evenimentele calendarului
     */
    public Set<Event> getEvents() {
        return events;
    }

    /**
     * Metoda pentru obtinerea programului de activitati
     * @return Programul de activitati al calendarului
     */
    public Map<String, Set<Activity>> getSchedule() {
        return schedule;
    }

    /**
     * Metoda pentru adaugarea unui eveniment in calendar
     * @param event Noul eveniment
     */
    public void addEvent(Event event) {
        events.add(event);
    }

    /**
     * Metoda pentru adaugarea unei activitati in programul de activitati
     * @param day Ziua in care se va desfasura activitatea
     * @param activity Activitatea
     */
    public void addActivityInSchedule(String day, Activity activity) {
        if (!schedule.containsKey(day))
            schedule.put(day, new TreeSet<>());
        schedule.get(day).add(activity);
    }

    /**
     * Metoda pentru stergerea unei activitati din orar
     * @param day Ziua in care se desfasoara activitatea
     * @param activityID ID-ul activitatii
     */
    public void deleteActivity(String day, int activityID) {
        schedule.get(day).removeIf(a -> a.getID() == activityID);
    }

    /**
     * Metoda pentru stergerea unui eveniment din calendar
     * @param event Evenimentul ce trebuie sters
     */
    public void deleteEvent(Event event) {
        events.remove(event);
    }

    /**
     * Metoda pentru modificarea programului de activitati
     * @param schedule Programul de activitati
     */
    public void setSchedule(Map<String, Set<Activity>> schedule) {
        this.schedule = schedule;
    }

    /**
     * Metoda pentru modificarea evenimentelor
     * @param events Evenimentele calendarului
     */
    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    /**
     * Metoda pentru modificarea numelui calendarului
     * @param name Noul nume
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Metoda pentru modificarea locului unde se desfasoara activitatile
     * @param location Noul loc
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Metoda pentru modificarea datei de inceputa
     * @param start Noua data de inceput
     */
    public void setStart(Date start) {
        this.start = start;
    }

    /**
     * Metoda pentru modificarea datei de sfarsit
     * @param end Noua data de sfarsit
     */
    public void setEnd(Date end) {
        this.end = end;
    }

    /**
     * Suprascrierea metodei toString pentru a afisa datele calendarului
     * @return Stringul cu datele calendarului
     */
    @Override
    public String toString() {
        return id + " " + name + " " + start + " " + end + " " + location;
    }
}

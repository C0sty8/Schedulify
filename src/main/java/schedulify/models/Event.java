package schedulify.models;

/**
 * Clasa pentru retinerea datelor despre un eveniment
 */
public class Event extends Activity {
    private String location;
    private Date eventDate;

    /**
     * Constructor cu parametri pentru clasa Event
     * @param id ID-ul evenimentului
     * @param eventDate Data desfasurarii evenimentului
     * @param eventName Numele evenimentului
     * @param start Ora si minutul la care incepe evenimentul
     * @param end Ora si minutul la care se termina evenimentul
     * @param location Locul unde se va desfasoara evenimentul
     */
    public Event(int id, Date eventDate, String eventName,HourMinute start, HourMinute end, String location) {
        super(id,eventName,start,end);
        this.eventDate = eventDate;
        this.location = location;
    }

    /**
     * Metoda pentru obtinerea locului unde se va desfasoara evenimentul
     * @return Locul unde se va desfasoara evenimentul
     */
    public String getLocation() {
        return location;
    }

    /**
     * Metoda pentru obtinerea data in care se va desfasura evenimentul
     * @return Data desfasurarii evenimentului
     */
    public Date getEventDate() {
        return eventDate;
    }

    /**
     * Metoda pentru modificarea data in care se va desfasura evenimentul
     * @param eventDate Noua data
     */
    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    /**
     * Metoda pentru modificarea locului unde se va desfasoara evenimentul
     * @param location Noua locatie
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Suprascrierea metodei compareTo pentru a compara doua obiecte de tip Event
     * @param a Obiectul cu care se va efectua compararea
     * @return 0 daca sunt egale, un numar negativ daca este mai mic,
     * Un numar pozitiv daca este mai mare
     */
    @Override
    public int compareTo(Activity a) {
        if (a instanceof Event e) {
            int result = eventDate.compareTo(e.eventDate);
            return result != 0 ? result : super.compareTo(a);
        }

        return super.compareTo(a);
    }

    /**
     * Suprascrierea metodei toString pentru a afisa datele evenimentului
     * @return Stringul cu datele evenimentului
     */
    @Override
    public String toString() {
        return eventDate + " " + super.toString();
    }
}

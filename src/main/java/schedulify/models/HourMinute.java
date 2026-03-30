package schedulify.models;

/**
 * Clasa pentru retinerea unei ore si a unui minut
 */
public class HourMinute implements Comparable<HourMinute>{
    private int hour;
    private int minute;

    /**
     * Constructor cu parametri pentru clasa HourMinute
     * @param hour Ora
     * @param minute Minutul
     */
    public HourMinute(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    /**
     * Constructor pentru parsarea unei date de tip String
     * @param time Stringul cu formatul HH:MM
     */
    public HourMinute(String time) {
        String[] timeParts = time.split(":");
        this.hour = Integer.parseInt(timeParts[0]);
        this.minute = Integer.parseInt(timeParts[1]);
    }

    /**
     * Metoda pentru obtinerea orei
     * @return Ora
     */
    public int getHour() {
        return hour;
    }

    /**
     * Metoda pentru obtinerea minutului
     * @return Minutul
     */
    public int getMinute() {
        return minute;
    }

    /**
     * Metoda pentru modificarea orei
     * @param hour Ora noua
     */
    public void setHour(int hour) {
        this.hour = hour;
    }

    /**
     * Metoda pentru modificarea minutului
     * @param minute Minutul nou
     */
    public void setMinute(int minute) {
        this.minute = minute;
    }

    /**
     * Suprascrierea metodei compareTo pentru a compara doua obiecte de tip HourMinute
     * @param h Obiectul cu care se va efectua compararea
     * @return 0 daca sunt egale, un numar negativ daca este mai mic,
     * un numar pozitiv daca este mai mare
     */
    @Override
    public int compareTo(HourMinute h) {
        int result = hour - h.hour;
        return result != 0 ? result : minute - h.minute;
    }

    /**
     * Suprascrierea metodei toString pentru a afisa ora si minutul
     * @return Stringul cu ora si minutul
     */
    @Override
    public String toString() {
        return String.format("%02d:%02d", hour, minute);
    }
}

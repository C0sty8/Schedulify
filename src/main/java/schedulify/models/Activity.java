package schedulify.models;

/**
 * Clasa pentru retinerea datelor despre o activitate
 */
public class Activity implements Comparable<Activity>{
    private int id;
    private String activityName;
    private HourMinute start;
    private HourMinute end;

    /**
     * Constructor cu argumente pentru clasa Activity
     * @param id ID-ul activitatii
     * @param activityName Numele activitatii
     * @param start Ora si minutul la care incepe activitatea
     * @param end Ora si minutul la care se termina activitatea
     */
    public Activity(int id, String activityName, HourMinute start, HourMinute end) {
        this.id = id;
        this.activityName = activityName;
        this.start = start;
        this.end = end;
    }

    /**
     * Metoda pentru obtinerea numelui activitatii
     * @return Numele activitatii
     */
    public String getActivityName() {
        return activityName;
    }

    /**
     * Metoda pentru obtinerea orei si minutului la care incepe activitatea
     * @return Timpul la care incepe activitatea
     */
    public HourMinute getStart() {
        return start;
    }

    /**
     * Metoda pentru obtinerea oriei si minutului la care se termina activitatea
     * @return Timpul la care se termina activitatea
     */
    public HourMinute getEnd() {
        return end;
    }

    /**
     * Metoda pentru obtinerea ID-ului activitatii
     * @return ID-ul activitatii
     */
    public int getID() {
        return id;
    }

    /**
     * Metoda pentru modificarea numelui activitatii
     * @param activityName Noul nume
     */
    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    /**
     * Metoda pentru modificarea orei si minutului la care incepe activitatea
     * @param start Noul timp la care incepe activitatea
     */
    public void setStart(HourMinute start) {
        this.start = start;
    }

    /**
     * Metoda pentru modificarea orei si minutului la care se termina activitatea
     * @param end Noul timp la care se termina activitatea
     */
    public void setEnd(HourMinute end) {
        this.end = end;
    }

    /**
     * Suprascrierea metodei compareTo pentru a compara doua obiecte de tip Activity
     * @param a Obiectul cu care se va efectua compararea
     * @return 0 daca sunt egale, un numar negativ daca este mai mic,
     * un numar pozitiv daca este mai mare
     */
    @Override
    public int compareTo(Activity a) {
        int result = start.compareTo(a.start);
        return result != 0 ? result : activityName.compareTo(a.activityName);
    }

    /**
     * Suprascrierea metodei toString pentru a afisa datele activitatii
     * @return Un string cu datele activitatii
     */
    @Override
    public String toString() {
        return start + "-" + end + ", " + activityName;
    }
}

package schedulify.models;

/**
 * Clasa pentru retinerea datelor calendaristice
 */
public class Date implements Comparable<Date> {
    private int month;
    private int day;
    private int year;

    /**
     * Constructor cu parametri pentru clasa Date
     * @param month Luna
     * @param day Ziua
     * @param year Anul
     */
    public Date(int month, int day, int year) {
        this.month = month;
        this.day = day;
        this.year = year;
    }

    /**
     * Constructor pentru parsarea datelor dintr-un string
     * @param date Stringul cu formatul YYYY-MM-DD
     */
    public Date(String date) {
        String[] dateParts = date.split("-");
        this.year = Short.parseShort(dateParts[0]);
        this.month = Short.parseShort(dateParts[1]);
        this.day = Short.parseShort(dateParts[2]);
    }

    /**
     * Metoda pentru obtinerea lunii
     * @return Luna
     */
    public int getMonth() {
        return month;
    }

    /**
     * Metoda pentru obtinerea zilei
     * @return Ziua
     */
    public int getDay() {
        return day;
    }

    /**
     * Metoda pentru obtinerea anului
     * @return Anul
     */
    public int getYear() {
        return year;
    }

    /**
     * Metoda pentru modificarea lunii
     * @param month noua luna
     */
    public void setMonth(int month) {
        this.month = month;
    }

    /**
     * Metoda pentru modificarea zilei
     * @param day noua zi
     */
    public void setDay(int day) {
        this.day = day;
    }

    /**
     * Metoda pentru modificarea anului
     * @param year noul an
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Suprascrierea metodei compareTo pentru a compara doua obiecte de tip Date
     * @param d Obiectul cu care se va efectua compararea
     * @return 0 daca sunt egale, un numar negativ daca este mai mic,
     * un numar pozitiv daca este mai mare
     */
    @Override
    public int compareTo(Date d) {
        if (year == d.year)
            if (month == d.month)
                return day - d.day;
            else return month - d.month;
        else return year - d.year;
    }

    /**
     * Suprascrierea metodei toString pentru a obtine un string cu formatul YYYY-MM-DD
     * @return String cu formatul YYYY-MM-DD
     */
    @Override
    public String toString() {
        return String.format("%d-%02d-%02d", year, month, day);
    }
}

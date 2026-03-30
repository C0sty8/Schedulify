package schedulify.models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;

/**
 * Clasa pentru testarea metodelor din clasa Date
 */
public class DateTest {
    List<Date> dates;

    @BeforeEach
    void setup() {
        dates = new ArrayList<>();
        dates.add(new Date("2005-04-04"));
        dates.add(new Date("2025-12-25"));
        dates.add(new Date(12,8,2005));
    }

    @Test
    void printDate() {
        Assertions.assertEquals("2005-04-04", dates.get(0).toString());
    }

    @Test
    void printDate2() {
        Assertions.assertEquals("2025-12-25", dates.get(1).toString());
    }

    @Test
    void printDate3() {
        Assertions.assertEquals("2005-12-08", dates.get(2).toString());
    }

    @Test
    void compareDates() {
        Assertions.assertEquals(0, dates.get(1).compareTo(dates.get(1)));
    }

    @Test
    void compareDates2() {
        Assertions.assertEquals(false, dates.get(0).compareTo(dates.get(1)) > 0);
    }

    @Test
    void compareDates3() {
        Assertions.assertEquals(true, dates.get(1).compareTo(dates.get(0)) > 0);
    }

    @Test
    void getAndSetDay() {
        dates.get(0).setDay(15);
        Assertions.assertEquals(15, dates.get(0).getDay());
    }

    @Test
    void getAndSetDay2() {
        dates.get(1).setDay(5);
        Assertions.assertEquals(5, dates.get(1).getDay());
    }

    @Test
    void getAndSetDay3() {
        dates.get(2).setDay(20);
        Assertions.assertEquals(20, dates.get(2).getDay());
    }

    @Test
    void getAndSetMonth() {
        dates.get(0).setMonth(12);
        Assertions.assertEquals(12, dates.get(0).getMonth());
    }

    @Test
    void getAndSetMonth2() {
        dates.get(1).setMonth(1);
        Assertions.assertEquals(1, dates.get(1).getMonth());
    }

    @Test
    void getAndSetMonth3() {
        dates.get(2).setMonth(10);
        Assertions.assertEquals(10, dates.get(2).getMonth());
    }

    @Test
    void getAndSetYear() {
        dates.get(0).setYear(2020);
        Assertions.assertEquals(2020, dates.get(0).getYear());
    }

    @Test
    void getAndSetYear2() {
        dates.get(1).setYear(2027);
        Assertions.assertEquals(2027, dates.get(1).getYear());
    }

    @Test
    void getAndSetYear3() {
        dates.get(2).setYear(1997);
        Assertions.assertEquals(1997, dates.get(2).getYear());
    }
}

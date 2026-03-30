package schedulify.models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;

/**
 * Clasa pentru testarea metodelor din clasa HourMinute
 */
public class HourMinuteTest {
    private List<HourMinute> hourMinutes;

    @BeforeEach
    void setup() {
        hourMinutes = new ArrayList<>();
        hourMinutes.add(new HourMinute(10, 0));
        hourMinutes.add(new HourMinute(8, 5));
        hourMinutes.add(new HourMinute(14, 50));
    }

    @Test
    void printHourMinutes() {
        Assertions.assertEquals("10:00", hourMinutes.get(0).toString());
    }

    @Test
    void printHourMinutes2() {
        Assertions.assertEquals("08:05", hourMinutes.get(1).toString());
    }

    @Test
    void printHourMinutes3() {
        Assertions.assertEquals("14:50", hourMinutes.get(2).toString());
    }

    @Test
    void compareHourMinutes() {
        Assertions.assertEquals(0, hourMinutes.get(1).compareTo(hourMinutes.get(1)));
    }

    @Test
    void compareHourMinutes2() {
        Assertions.assertEquals(true, hourMinutes.get(0).compareTo(hourMinutes.get(1)) > 0);
    }

    @Test
    void compareHourMinutes3() {
        Assertions.assertEquals(false, hourMinutes.get(1).compareTo(hourMinutes.get(0)) > 0);
    }
    
    @Test
    void getAndSetHour() {
        hourMinutes.get(0).setHour(12);
        Assertions.assertEquals(12, hourMinutes.get(0).getHour());
    }

    @Test
    void getAndSetHour2() {
        hourMinutes.get(1).setHour(20);
        Assertions.assertEquals(20, hourMinutes.get(1).getHour());
    }

    @Test
    void getAndSetHour3() {
        hourMinutes.get(2).setHour(5);
        Assertions.assertEquals(5, hourMinutes.get(2).getHour());
    }

    @Test
    void getAndSetMinute() {
        hourMinutes.get(0).setMinute(30);
        Assertions.assertEquals(30, hourMinutes.get(0).getMinute());
    }

    @Test
    void getAndSetMinute2() {
        hourMinutes.get(1).setMinute(3);
        Assertions.assertEquals(3, hourMinutes.get(1).getMinute());
    }

    @Test
    void getAndSetMinute3() {
        hourMinutes.get(2).setMinute(0);
        Assertions.assertEquals(0, hourMinutes.get(2).getMinute());
    }
}

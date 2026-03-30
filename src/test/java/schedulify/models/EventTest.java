package schedulify.models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;

/**
 * Clasa pentru testarea metodelor din clasa Event
 */
public class EventTest {
    private List<Event> events;

    @BeforeEach
    void setup() {
        events = new ArrayList<>();
        events.add(new Event(1,new Date("2026-08-08"),"Nume", 
                new HourMinute(12,20), new HourMinute(14,50), "45.747286,21.231431"));
        events.add(new Event(2,new Date("2025-12-01"),"Nume2",
                new HourMinute(8,0), new HourMinute(20,30), "35.749986,11.231431"));
        events.add(new Event(3,new Date("2027-05-15"),"Nume3",
                new HourMinute(9,20), new HourMinute(10,50), "15.749986,31.231431"));
    }

    @Test
    void getId() {
        Assertions.assertEquals(1, events.getFirst().getID());
        Assertions.assertEquals(2, events.get(1).getID());
        Assertions.assertEquals(3, events.get(2).getID());
    }

    @Test
    void printEvent() {
        Assertions.assertEquals("2026-08-08 12:20-14:50, Nume",
                events.get(0).toString());
    }

    @Test
    void printEvent2() {
        Assertions.assertEquals("2025-12-01 08:00-20:30, Nume2",
                events.get(1).toString());
    }

    @Test
    void printEvent3() {
        Assertions.assertEquals("2027-05-15 09:20-10:50, Nume3",
                events.get(2).toString());
    }

    @Test
    void compareEvents() {
        Assertions.assertEquals(0, events.get(0).compareTo(events.get(0)));
    }

    @Test
    void compareEvents2() {
        Assertions.assertEquals(true, events.get(0).compareTo(events.get(1)) > 0);
    }

    @Test
    void compareEvents3() {
        Assertions.assertEquals(false, events.get(1).compareTo(events.get(2)) > 0);
    }

    @Test
    void getAndSetLocation() {
        events.get(0).setLocation("45.747286,21.231431");
        Assertions.assertEquals("45.747286,21.231431", events.get(0).getLocation());
    }

    @Test
    void getAndSetLocation2() {
        events.get(1).setLocation("0.000000,0.000000");
        Assertions.assertEquals("0.000000,0.000000", events.get(1).getLocation());
    }

    @Test
    void getAndSetLocation3() {
        events.get(2).setLocation("0.000000,0.000000");
        Assertions.assertEquals("0.000000,0.000000", events.get(2).getLocation());
    }

    @Test
    void getAndSetDate() {
        events.get(0).setEventDate(new Date("2026-08-08"));
        Assertions.assertEquals("2026-08-08", events.get(0).getEventDate().toString());
    }

    @Test
    void getAndSetDate2() {
        events.get(1).setEventDate(new Date("1900-01-01"));
        Assertions.assertEquals("1900-01-01", events.get(1).getEventDate().toString());
    }

    @Test
    void getAndSetDate3() {
        events.get(2).setEventDate(new Date("2000-12-06"));
        Assertions.assertEquals("2000-12-06", events.get(2).getEventDate().toString());
    }
}

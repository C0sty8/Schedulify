package schedulify.models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;

import java.util.*;

/**
 * Clasa pentru testarea metodelor din clasa Calendar
 */
public class CalendarTest {
    List<Calendar> calendars;

    @BeforeEach
    void setup() {
        calendars = new ArrayList<>();
        calendars.add(new Calendar(101,"Nume",
                new Date("2025-10-14"), new Date("2026-02-06"),
                "0.000000,0.000000"));
        calendars.add(new Calendar(28,"Nume2",
                new Date("2020-01-24"), new Date("2030-01-06"),
                "45.717507,21.242346"));
        calendars.add(new Calendar(180,"Nume3",
                new Date("2025-01-24"), new Date("2025-01-24"),
                "45.765379,21.228325"));
    }

    @Test
    void printCalendar() {
        Assertions.assertEquals("101 Nume 2025-10-14 2026-02-06 0.000000,0.000000", calendars.get(0).toString());
    }

    @Test
    void printCalendar2() {
        Assertions.assertEquals("28 Nume2 2020-01-24 2030-01-06 45.717507,21.242346", calendars.get(1).toString());
    }

    @Test
    void printCalendar3() {
        Assertions.assertEquals("180 Nume3 2025-01-24 2025-01-24 45.765379,21.228325", calendars.get(2).toString());
    }

    @Test
    void addAndDeleteEventTest() {
        calendars.get(0).addEvent(new Event(1,new Date("2026-08-08"),"Nume",
                new HourMinute(12,20), new HourMinute(14,50), "45.747286,21.231431"));
        Assertions.assertEquals("[2026-08-08 12:20-14:50, Nume]", calendars.get(0).getEvents().toString());
        calendars.get(0).deleteEvent(calendars.get(0).getEvents().stream().toList().get(0));
        Assertions.assertEquals("[]", calendars.get(0).getEvents().toString());
    }

    @Test
    void addAndDeleteEventTest2() {
        calendars.get(1).addEvent(new Event(3,new Date("2026-08-08"),"Nume", 
                new HourMinute(12,20), new HourMinute(14,50), "45.747286,21.231431"));
        calendars.get(1).addEvent(new Event(4,new Date("2026-08-08"),"Nume2", 
                new HourMinute(10,0), new HourMinute(12,0), "45.007286,21.231431"));
        Assertions.assertEquals("[2026-08-08 10:00-12:00, Nume2, 2026-08-08 12:20-14:50, Nume]",
                calendars.get(1).getEvents().toString());
        calendars.get(1).deleteEvent(calendars.get(1).getEvents().stream().toList().get(1));
        Assertions.assertEquals("[2026-08-08 10:00-12:00, Nume2]",
                calendars.get(1).getEvents().toString());
    }

    @Test
    void addAndDeleteEventTest3() {
        calendars.get(2).addEvent(new Event(5,new Date("2026-08-08"),"Nume", 
                new HourMinute(12,20), new HourMinute(14,50), "45.747286,21.231431"));
        calendars.get(2).addEvent(new Event(6,new Date("2025-08-08"),"Nume2", 
                new HourMinute(10,0), new HourMinute(12,0), "45.007286,21.231431"));
        calendars.get(2).addEvent(new Event(7,new Date("2027-08-08"),"Nume3", 
                new HourMinute(10,0), new HourMinute(12,0), "45.007286,21.231431"));
        Assertions.assertEquals("[2025-08-08 10:00-12:00, Nume2, 2026-08-08 12:20-14:50, Nume, 2027-08-08 10:00-12:00, Nume3]",
                calendars.get(2).getEvents().toString());
        calendars.get(2).deleteEvent(calendars.get(2).getEvents().stream().toList().get(2));
        Assertions.assertEquals("[2025-08-08 10:00-12:00, Nume2, 2026-08-08 12:20-14:50, Nume]",
                calendars.get(2).getEvents().toString());
    }

    String calendarData(Calendar calendar) {
        StringBuffer result = new StringBuffer();
        for (String key : calendar.getSchedule().keySet()) {
            result.append(key).append(" ").append(calendar.getSchedule().get(key).toString()).append("\n");
        }

        return result.toString();
    }

    @Test
    void addAndDeleteActivityInScheduleTest() {
        calendars.get(0).addActivityInSchedule("Monday", new Activity(8,"Test", new HourMinute(10,0), new HourMinute(12,0)));
        StringBuffer result = new StringBuffer();
        for (String key : calendars.get(0).getSchedule().keySet()) {
            result.append(key).append(" ").append(calendars.get(0).getSchedule().get(key).toString()).append("\n");
        }
        Assertions.assertEquals("Monday [10:00-12:00, Test]\n", result.toString());
        calendars.get(0).deleteActivity("Monday", 8);
        Assertions.assertEquals("Monday []\n", calendarData(calendars.get(0)));
    }

    @Test
    void addAndDeleteActivityInScheduleTest2() {
        calendars.get(1).addActivityInSchedule("Monday", new Activity(1,"Test", new HourMinute(10,0), new HourMinute(12,0)));
        calendars.get(1).addActivityInSchedule("Monday", new Activity(2,"Test2", new HourMinute(9,30), new HourMinute(11,0)));
        calendars.get(1).addActivityInSchedule("Monday", new Activity(3,"Test3", new HourMinute(20,0), new HourMinute(1,0)));
        Assertions.assertEquals("Monday [09:30-11:00, Test2, 10:00-12:00, Test, 20:00-01:00, Test3]\n", calendarData(calendars.get(1)));
        calendars.get(1).deleteActivity("Monday", 1);
        calendars.get(1).deleteActivity("Monday", 2);
        Assertions.assertEquals("Monday [20:00-01:00, Test3]\n", calendarData(calendars.get(1)));
    }

    @Test
    void addAndDeleteActivityInScheduleTest3() {
        calendars.get(2).addActivityInSchedule("Monday", new Activity(1,"Test", new HourMinute(10,0), new HourMinute(12,0)));
        calendars.get(2).addActivityInSchedule("Monday", new Activity(2,"Test2", new HourMinute(9,30), new HourMinute(11,0)));
        calendars.get(2).addActivityInSchedule("Monday", new Activity(3,"Test3", new HourMinute(20,0), new HourMinute(1,0)));
        calendars.get(2).addActivityInSchedule("Tuesday", new Activity(4,"Test4", new HourMinute(10,0), new HourMinute(12,0)));
        calendars.get(2).addActivityInSchedule("Wednesday", new Activity(5,"Test5", new HourMinute(20,30), new HourMinute(12,0)));
        Assertions.assertEquals("Monday [09:30-11:00, Test2, 10:00-12:00, Test, 20:00-01:00, Test3]\n" +
                "Wednesday [20:30-12:00, Test5]\n" +
                "Tuesday [10:00-12:00, Test4]\n", calendarData(calendars.get(2)));
        calendars.get(2).deleteActivity("Monday", 1);
        Assertions.assertEquals("Monday [09:30-11:00, Test2, 20:00-01:00, Test3]\n" +
                "Wednesday [20:30-12:00, Test5]\n" +
                "Tuesday [10:00-12:00, Test4]\n", calendarData(calendars.get(2)));
    }

    @Test
    void getAndSetName() {
        calendars.get(0).setName("NumeNou");
        Assertions.assertEquals("NumeNou", calendars.get(0).getName());
    }

    @Test
    void getAndSetName2() {
        calendars.get(1).setName("NumeNou2");
        Assertions.assertEquals("NumeNou2", calendars.get(1).getName());
    }

    @Test
    void getAndSetName3() {
        calendars.get(2).setName("NumeNou3");
        Assertions.assertEquals("NumeNou3", calendars.get(2).getName());
    }

    @Test
    void getAndSetLocation() {
        calendars.get(2).setLocation("0.000000,0.000000");
        Assertions.assertEquals("0.000000,0.000000", calendars.get(2).getLocation());
    }

    @Test
    void getAndSetLocation2() {
        calendars.get(0).setLocation("45.747286,21.231431");
        Assertions.assertEquals("45.747286,21.231431", calendars.get(0).getLocation());
    }

    @Test
    void getAndSetLocation3() {
        calendars.get(1).setLocation("45.747286,21.231431");
        Assertions.assertEquals("45.747286,21.231431", calendars.get(1).getLocation());
    }

    @Test
    void getAndSetStart() {
        calendars.get(0).setStart(new Date("1991-12-10"));
        Assertions.assertEquals("1991-12-10", calendars.get(0).getStart().toString());
    }

    @Test
    void getAndSetStart2() {
        calendars.get(1).setStart(new Date("2000-02-01"));
        Assertions.assertEquals("2000-02-01", calendars.get(1).getStart().toString());
    }

    @Test
    void getAndSetStart3() {
        calendars.get(2).setStart(new Date("2001-12-11"));
        Assertions.assertEquals("2001-12-11", calendars.get(2).getStart().toString());
    }

    @Test
    void getAndSetEnd() {
        calendars.get(0).setEnd(new Date("2026-08-08"));
        Assertions.assertEquals("2026-08-08", calendars.get(0).getEnd().toString());
    }

    @Test
    void getAndSetEnd2() {
        calendars.get(1).setEnd(new Date("2030-12-09"));
        Assertions.assertEquals("2030-12-09", calendars.get(1).getEnd().toString());
    }

    @Test
    void getAndSetEnd3() {
        calendars.get(2).setEnd(new Date("2070-08-08"));
        Assertions.assertEquals("2070-08-08", calendars.get(2).getEnd().toString());
    }

    @Test
    void getIdTest() {
        Assertions.assertEquals(101, calendars.get(0).getId());
    }

    @Test
    void getIdTest2() {
        Assertions.assertEquals(28, calendars.get(1).getId());
    }

    @Test
    void getIdTest3() {
        Assertions.assertEquals(180, calendars.get(2).getId());
    }

    @Test
    void setEventsTests() {
        Set<Event> events = new TreeSet<>();
        calendars.getFirst().setEvents(events);
        Assertions.assertEquals("[]", calendars.getFirst().getEvents().toString());

        events.add(new Event(1,new Date("2026-08-08"),"Nume",
                new HourMinute(12,20), new HourMinute(14,50), "45.747286,21.231431"));
        calendars.get(1).setEvents(events);
        Assertions.assertEquals("[2026-08-08 12:20-14:50, Nume]", calendars.get(1).getEvents().toString());

        events.add(new Event(2,new Date("2025-12-01"),"Nume2",
                new HourMinute(8,0), new HourMinute(20,30), "35.749986,11.231431"));
        events.add(new Event(3,new Date("2027-05-15"),"Nume3",
                new HourMinute(9,20), new HourMinute(10,50), "15.749986,31.231431"));
        calendars.get(2).setEvents(events);
        Assertions.assertEquals("[2025-12-01 08:00-20:30, Nume2, 2026-08-08 12:20-14:50, Nume, 2027-05-15 09:20-10:50, Nume3]", calendars.get(2).getEvents().toString());
    }

    @Test
    void setScheduleTests() {
        Map<String, Set<Activity>> schedule = new HashMap<>();
        calendars.getFirst().setSchedule(schedule);
        Assertions.assertEquals("{}", calendars.getFirst().getSchedule().toString());

        schedule.put("Monday", new TreeSet<>());
        schedule.get("Monday").add(new Activity(1,"Test Mate", new HourMinute(10,0), new HourMinute(12,0)));
        calendars.get(1).setSchedule(schedule);
        Assertions.assertEquals("{Monday=[10:00-12:00, Test Mate]}", calendars.get(1).getSchedule().toString());

        schedule.put("Tuesday", new TreeSet<>());
        schedule.get("Tuesday").add(new Activity(1,"Test", new HourMinute(10,0), new HourMinute(12,0)));
        calendars.get(2).setSchedule(schedule);
        Assertions.assertEquals("{Monday=[10:00-12:00, Test Mate], Tuesday=[10:00-12:00, Test]}", calendars.get(2).getSchedule().toString());
    }
}

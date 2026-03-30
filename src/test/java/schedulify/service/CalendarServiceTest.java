package schedulify.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import schedulify.models.Activity;
import schedulify.models.Date;
import schedulify.models.Event;
import schedulify.models.HourMinute;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Clasa pentru testarea metodelor din clasa CalendarService
 */
public class CalendarServiceTest {
    @BeforeEach
    void setup() {
        DBUtils.ensureDBExists();
        DBUtils.ensureTablesExists();
    }

    @Test
    void getScheduleIDTests() {
        Assertions.assertEquals(1, CalendarService.getScheduleID("2Q03X9W6"));
        Assertions.assertEquals(1, CalendarService.getScheduleID("SRNYWXHY"));
        Assertions.assertEquals(-1, CalendarService.getScheduleID("cod_de_invitatie_gresit"));
    }

    @Test
    void getCalendarTests() {
        Assertions.assertEquals("1 Facultate 2026-01-09 2026-02-07 45.733086,21.239256",
                CalendarService.getCalendar("2Q03X9W6").toString());
        Assertions.assertEquals("1 Facultate 2026-01-09 2026-02-07 45.733086,21.239256",
                CalendarService.getCalendar("SRNYWXHY").toString());
        Assertions.assertEquals(null, CalendarService.getCalendar("cod_de_invitatie_gresit"));
    }

    @Test
    void getInvitationTypeTests() {
        Assertions.assertEquals(2, CalendarService.getInvitationType("2Q03X9W6"));
        Assertions.assertEquals(1, CalendarService.getInvitationType("SRNYWXHY"));
        Assertions.assertEquals(0, CalendarService.getInvitationType("cod_de_invitatie_gresit"));
    }

    @Test
    void getActivityId() {
        Assertions.assertEquals(10, CalendarService.getActivityID(1, "Curs",
                "Monday",new HourMinute("08:00:00"), new HourMinute("9:30:00")));
        Assertions.assertEquals(1, CalendarService.getActivityID(1, "SO 1",
                "Tuesday",new HourMinute("09:40:00"), new HourMinute("11:10:00")));
        Assertions.assertEquals(-1, CalendarService.getActivityID(1, "Curs",
                "Monday",new HourMinute("10:00:00"), new HourMinute("11:30:00")));
    }

    @Test
    void getEventId() {
        Assertions.assertEquals(9, CalendarService.getEventId(1, "nume nou",
                "2026-01-30",new HourMinute("00:00:00"), new HourMinute("20:00:00"), "45.831358,20.458426"));
        Assertions.assertEquals(3, CalendarService.getEventId(3, "b",
                "2025-12-31",new HourMinute("00:00:00"), new HourMinute("02:00:00"), "45.738837,21.226896"));
        Assertions.assertEquals(-1, CalendarService.getEventId(0, "Excursier",
                "2026-01-30",new HourMinute("10:00:00"), new HourMinute("11:30:00"), "45.831358,20.458426"));
    }

    @Test
    void getActivitiesTests() {
        Assertions.assertEquals("{Monday=[08:00-09:30, Curs], Tuesday=[09:40-11:10, SO 1]}",
                CalendarService.getActivities(1).toString());
        Assertions.assertEquals("{Monday=[01:01-01:01, 1]}", CalendarService.getActivities(3).toString());
        Assertions.assertEquals("{}", CalendarService.getActivities(0).toString());
    }

    @Test
    void getEventsTests() {
        Assertions.assertEquals("[2026-01-01 08:00-10:00, T1, 2026-01-30 00:00-20:00, nume nou, 2026-02-02 10:00-12:00, T2, 2026-02-02 10:00-12:00, nume nou 2, 2026-03-03 03:00-15:00, T3, 2026-06-07 08:00-10:00, T1]", CalendarService.getEvents(1).toString());
        Assertions.assertEquals("[2025-12-31 00:00-02:00, b, 2026-01-06 02:04-15:01,  fgh, 2026-01-30 12:00-12:00, Poligon]"
                , CalendarService.getEvents(3).toString());
        Assertions.assertEquals("[]", CalendarService.getEvents(0).toString());
    }

    @Test
    void getCodesTests() {
        String[] codes = CalendarService.getCodes(1);
        Assertions.assertEquals("2Q03X9W6,SRNYWXHY", codes[0] + "," + codes[1]);

        codes = CalendarService.getCodes(3);
        Assertions.assertEquals("UUAUXU86,Q4RHSMX7", codes[0] + "," + codes[1]);

        codes = CalendarService.getCodes(0);
        Assertions.assertEquals(0, codes.length);
    }

    @Test
    void addModifyAndDeleteEventTests() {
        CalendarService.addEventToDB(7, "T1", "2026-01-01",
                new HourMinute("08:00:00"), new HourMinute("10:00:00"), "45.733086,21.239256");
        CalendarService.addEventToDB(7, "T2", "2026-02-02",
                new HourMinute("10:00:00"), new HourMinute("12:00:00"), "45.733086,21.239256");
        CalendarService.addEventToDB(7, "T3", "2026-03-03",
                new HourMinute("03:00:00"), new HourMinute("15:00:00"), "45.733086,21.239256");

        List<Event> events = CalendarService.getEvents(7).stream().toList();
        Assertions.assertEquals("[2026-01-01 08:00-10:00, T1, 2026-02-02 10:00-12:00, T2, 2026-03-03 03:00-15:00, T3]"
                , events.toString());

        events.getFirst().setEventDate(new Date("2026-06-07"));
        events.get(1).setActivityName("nume nou");
        events.get(1).setStart(new HourMinute("00:00:00"));
        events.get(2).setActivityName("nume nou 2");

        for (Event event : events) {
            CalendarService.setEvent(event);
        }
        events = CalendarService.getEvents(7).stream().toList();
        Assertions.assertEquals("[2026-02-02 00:00-12:00, nume nou, 2026-03-03 03:00-15:00, nume nou 2, 2026-06-07 08:00-10:00, T1]"
                , events.toString());

        for (Event event : events) {
            CalendarService.deleteEvent(event.getID());
        }
        events = CalendarService.getEvents(7).stream().toList();
        Assertions.assertEquals("[]", events.toString());
    }

    @Test
    void addModifyAndDeleteActivityTests() {
        CalendarService.addActivityToDB(7, "Curs", "Monday", new HourMinute("08:00:00"), new HourMinute("9:30:00"));
        CalendarService.addActivityToDB(7, "SO 1", "Tuesday", new HourMinute("09:40:00"), new HourMinute("11:10:00"));
        CalendarService.addActivityToDB(7, "Curs", "Wednesday", new HourMinute("10:00:00"), new HourMinute("11:30:00"));

        Map<String, Set<Activity>> activities = CalendarService.getActivities(7);
        Assertions.assertEquals("{Monday=[08:00-09:30, Curs], Wednesday=[10:00-11:30, Curs], Tuesday=[09:40-11:10, SO 1]}",
                activities.toString());

        List<Activity> temp = activities.get("Monday").stream().toList();
        temp.getFirst().setActivityName("nume nou");
        temp = activities.get("Wednesday").stream().toList();
        temp.getFirst().setEnd(new HourMinute("12:00:00"));
        temp = activities.get("Tuesday").stream().toList();
        temp.getFirst().setActivityName("nume nou 2");

        for(String key : activities.keySet()) {
            activities.get(key).forEach(
                    a -> CalendarService.setActivity(key, a)
            );
        }

        activities = CalendarService.getActivities(7);
        Assertions.assertEquals("{Monday=[08:00-09:30, nume nou], Wednesday=[10:00-12:00, Curs], Tuesday=[09:40-11:10, nume nou 2]}",
                activities.toString());

        for(String key : activities.keySet()) {
            activities.get(key).forEach(
                    a -> CalendarService.deleteActivity(a.getID())
            );
        }

        activities = CalendarService.getActivities(7);
        Assertions.assertEquals("{}", activities.toString());
    }

    @Test
    void removeParticipantTests() {
        CalendarService.removeParticipant(5, 8);
        CalendarService.removeParticipant(5, 9);
        CalendarService.removeParticipant(5, 11);

        Assertions.assertEquals(false, UserService.isUserInCalendar(5, 8));
        Assertions.assertEquals(false, UserService.isUserInCalendar(5, 9));
        Assertions.assertEquals(false, UserService.isUserInCalendar(5, 11));
    }

    @Test
    void deleteCalendarTests() {
        UserService.addCalendarToDB("1","2020-01-01","2020-02-02",
                "a","b","0.000000,0.000000");
        UserService.addCalendarToDB("2","2020-01-01","2020-02-02",
                "c","d","0.000000,0.000000");
        UserService.addCalendarToDB("3","2020-01-01","2020-02-02",
                "e","f","0.000000,0.000000");
        int calendarID1 = CalendarService.getScheduleID("a");
        int calendarID2 = CalendarService.getScheduleID("c");
        int calendarID3 = CalendarService.getScheduleID("e");

        CalendarService.deleteCalendar(calendarID1);
        CalendarService.deleteCalendar(calendarID2);
        CalendarService.deleteCalendar(calendarID3);

        Assertions.assertEquals(null, CalendarService.getCalendar("a"));
        Assertions.assertEquals(null, CalendarService.getCalendar("c"));
        Assertions.assertEquals(null, CalendarService.getCalendar("e"));
    }
}

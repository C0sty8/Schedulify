package schedulify.models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;

/**
 * Clasa pentru testarea metodelor din clasa Activity
 */
public class ActivityTest {
    private List<Activity> activities;

    @BeforeEach
    void setup() {
        activities = new ArrayList<>();
        activities.add(new Activity(0,"Test", new HourMinute(10, 0), new HourMinute(11, 0)));
        activities.add(new Activity(1,"Test2", new HourMinute(12, 0), new HourMinute(20, 40)));
        activities.add(new Activity(2,"Test3", new HourMinute(14, 50), new HourMinute(1, 40)));
    }

    @Test
    void printActivities() {
        Assertions.assertEquals("10:00-11:00, Test", activities.getFirst().toString());
    }

    @Test
    void printActivities2() {
        Assertions.assertEquals("12:00-20:40, Test2", activities.get(1).toString());
    }

    @Test
    void printActivities3() {
        Assertions.assertEquals("14:50-01:40, Test3", activities.get(2).toString());
    }

    @Test
    void getIDTest() {
        Assertions.assertEquals(0, activities.getFirst().getID());
        Assertions.assertEquals(1, activities.get(1).getID());
        Assertions.assertEquals(2, activities.getLast().getID());
    }

    @Test
    void getAndSetActivityNameTest() {
        activities.getFirst().setActivityName("Nume");
        Assertions.assertEquals("Nume", activities.getFirst().getActivityName());
    }

    @Test
    void getAndSetActivityNameTest2() {
        activities.get(1).setActivityName("");
        Assertions.assertEquals("", activities.get(1).getActivityName());
    }

    @Test
    void getAndSetActivityNameTest3() {
        activities.getLast().setActivityName("Nume2");
        Assertions.assertEquals("Nume2", activities.getLast().getActivityName());
    }

    @Test
    void getAndSetStartHourTest() {
        activities.getFirst().setStart(new HourMinute(20, 0));
        Assertions.assertEquals("20:00", activities.getFirst().getStart().toString());
    }

    @Test
    void getAndSetStartHourTest2() {
        activities.get(1).setStart(new HourMinute(12, 30));
        Assertions.assertEquals("12:30", activities.get(1).getStart().toString());
    }

    @Test
    void getAndSetStartHourTest3() {
        activities.getLast().setStart(new HourMinute(2, 50));
        Assertions.assertEquals("02:50", activities.getLast().getStart().toString());
    }

    @Test
    void getAndSetEndTest() {
        activities.getFirst().setEnd(new HourMinute(20, 0));
        Assertions.assertEquals("20:00", activities.getFirst().getEnd().toString());
    }

    @Test
    void getAndSetEndTest2() {
        activities.get(1).setEnd(new HourMinute(12, 30));
        Assertions.assertEquals("12:30", activities.get(1).getEnd().toString());
    }

    @Test
    void getAndSetEndTest3() {
        activities.getLast().setEnd(new HourMinute(2, 50));
        Assertions.assertEquals("02:50", activities.getLast().getEnd().toString());
    }

    @Test
    void compareActivities() {
        Assertions.assertEquals(0, activities.getFirst().compareTo(activities.getFirst()));
    }

    @Test
    void compareActivities2() {
        Assertions.assertEquals(false, activities.getFirst().compareTo(activities.get(1)) > 0);
    }

    @Test
    void compareActivities3() {
        Assertions.assertEquals(true, activities.get(1).compareTo(activities.getFirst()) > 0);
    }
}


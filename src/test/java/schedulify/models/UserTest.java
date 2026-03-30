package schedulify.models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;

/**
 * Clasa pentru testarea metodelor din clasa User
 */
public class UserTest {
    List<User> users;
    @BeforeEach
    void setup() {
        users = new ArrayList<>();
        users.add(new User(1,"Nume"));
        users.add(new User(2,"Nume2"));
        users.add(new User(3,"Nume3"));
    }

    @Test
    void getIdTest() {
        Assertions.assertEquals(1, users.get(0).getId());
    }

    @Test
    void getIdTest2() {
        Assertions.assertEquals(2, users.get(1).getId());
    }

    @Test
    void getIdTest3() {
        Assertions.assertEquals(3, users.get(2).getId());
    }

    @Test
    void printUser() {
        Assertions.assertEquals("1. Nume", users.get(0).toString());
    }

    @Test
    void printUser2() {
        Assertions.assertEquals("2. Nume2", users.get(1).toString());
    }

    @Test
    void printUser3() {
        Assertions.assertEquals("3. Nume3", users.get(2).toString());
    }

    @Test
    void getAndSetUserName() {
        users.get(0).setUsername("NumeNou");
        Assertions.assertEquals("NumeNou", users.get(0).getUsername());
    }

    @Test
    void getAndSetUserName2() {
        users.get(1).setUsername("NumeNou2");
        Assertions.assertEquals("NumeNou2", users.get(1).getUsername());
    }

    @Test
    void getAndSetUserName3() {
        users.get(2).setUsername("NumeNou3");
        Assertions.assertEquals("NumeNou3", users.get(2).getUsername());
    }

    @Test
    void getAndAddInList() {
        users.get(0).addCalendar(new Calendar(101,"Nume",
                new Date("2025-10-14"), new Date("2026-02-06"),
                "0.000000,0.000000"));
        Assertions.assertEquals("[101 Nume 2025-10-14 2026-02-06 0.000000,0.000000]",
                users.get(0).getCalendars().toString());
    }

    @Test
    void getAndAddInList2() {
        users.get(1).addCalendar(new Calendar(101,"Nume",
                new Date("2025-10-14"), new Date("2026-02-06"),
                "0.000000,0.000000"));
        users.get(1).addCalendar(new Calendar(102,"Nume2",
                new Date("2015-11-14"), new Date("2020-01-06"),
                "0.000000,0.000000"));
        Assertions.assertEquals("[101 Nume 2025-10-14 2026-02-06 0.000000,0.000000, " +
                        "102 Nume2 2015-11-14 2020-01-06 0.000000,0.000000]",
                users.get(1).getCalendars().toString());
    }

    @Test
    void getAndAddInList3() {
        users.get(2).addCalendar(new Calendar(101,"Nume",
                new Date("2025-10-14"), new Date("2026-02-06"),
                "0.000000,0.000000"));
        users.get(2).addCalendar(new Calendar(102,"Nume2",
                new Date("2015-11-14"), new Date("2020-01-06"),
                "0.000000,0.000000"));
        users.get(2).addCalendar(new Calendar(103,"Nume3",new Date("2025-10-14"), new Date("2026-02-06"),
                "0.000000,0.000000"));
        Assertions.assertEquals("[101 Nume 2025-10-14 2026-02-06 0.000000,0.000000, " +
                        "102 Nume2 2015-11-14 2020-01-06 0.000000,0.000000, " +
                        "103 Nume3 2025-10-14 2026-02-06 0.000000,0.000000]",
                users.get(2).getCalendars().toString());
    }
}

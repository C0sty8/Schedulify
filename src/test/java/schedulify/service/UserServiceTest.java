package schedulify.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Clasa pentru testarea metodelor din clasa UserService
 */
public class UserServiceTest {

    String randomStringGenerator() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 10) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }

    @BeforeEach
    void setup() {
        DBUtils.ensureDBExists();
        DBUtils.ensureTablesExists();
    }

    @Test
    void passwordIsValidTests() {
        Assertions.assertEquals(true,
                UserService.passwordIsValid("q@a.z", "123"));
        Assertions.assertEquals(false,
                UserService.passwordIsValid("q@a.z", "1234"));
        Assertions.assertEquals(false,
                UserService.passwordIsValid("test@a.z", "12345"));
    }

    @Test
    void isUniqueInvitationTests() {
        Assertions.assertEquals(true, UserService.isUniqueInvitation("QBCDEFGH"));
        Assertions.assertEquals(false, UserService.isUniqueInvitation("SRNYWXHY"));
        Assertions.assertEquals(false, UserService.isUniqueInvitation("2Q03X9W6"));
    }

    @Test
    void isUserInCalendarTests() {
        Assertions.assertEquals(true, UserService.isUserInCalendar(1, 1));
        Assertions.assertEquals(false, UserService.isUserInCalendar(1, 2));
        Assertions.assertEquals(false, UserService.isUserInCalendar(0, 0));
    }

    @Test
    void isUserAdminTests() {
        Assertions.assertEquals(true, UserService.isUserAdmin(1, 1));
        Assertions.assertEquals(false, UserService.isUserAdmin(1, 2));
        Assertions.assertEquals(false, UserService.isUserAdmin(0, 0));
    }

    @Test
    @DisplayName("Teste pentru addCalendarToDB, addUser, addCalendarUser, getUser si getUserPermissionType")
    void addAndGetTests() {
        UserService.addCalendarToDB("1","2020-01-01","2020-02-02",
                "a","b","0.000000,0.000000");
        UserService.addCalendarToDB("2","2020-01-01","2020-02-02",
                "c","d","0.000000,0.000000");
        UserService.addCalendarToDB("3","2020-01-01","2020-02-02",
                "e","f","0.000000,0.000000");

        List<Integer> calendarIDs = List.of(CalendarService.getScheduleID("a"),
                CalendarService.getScheduleID("c"), CalendarService.getScheduleID("e"));

        List<Integer> userIDs = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            String email = randomStringGenerator() + "@a.z";
            UserService.addUser(email, "nume", "123");
            userIDs.add(UserService.getUser(email).getId());
        }

        UserService.addCalendarUser(userIDs.get(0), "a",2);
        UserService.addCalendarUser(userIDs.get(1), "d",1);
        UserService.addCalendarUser(userIDs.get(2), "e",3);

        Assertions.assertEquals(2, UserService.getUserPermissionType(userIDs.get(0), calendarIDs.get(0)));
        Assertions.assertEquals(1, UserService.getUserPermissionType(userIDs.get(1), calendarIDs.get(1)));
        Assertions.assertEquals(3, UserService.getUserPermissionType(userIDs.get(2), calendarIDs.get(2)));

        CalendarService.deleteCalendar(calendarIDs.get(0));
        CalendarService.deleteCalendar(calendarIDs.get(1));
        CalendarService.deleteCalendar(calendarIDs.get(2));
    }

    @Test
    void getCalendarParticipantsTests() {
        Assertions.assertEquals("{1=[2. Cont de test 1], 3=[1. Qwerty]}", UserService.getCalendarParticipants(1).toString());
        Assertions.assertEquals("{3=[1. Qwerty]}", UserService.getCalendarParticipants(3).toString());
        Assertions.assertEquals("{}", UserService.getCalendarParticipants(0).toString());
    }

    @Test
    void setCalendarUserPermissionTests() {
        UserService.setCalendarUserPermission(2,1,3,false);
        Assertions.assertEquals(3, UserService.getUserPermissionType(2,1));

        UserService.setCalendarUserPermission(2,1,1,true);
        Assertions.assertEquals(1, UserService.getUserPermissionType(2,1));

        UserService.setCalendarUserPermission(0,1,2,false);
        Assertions.assertEquals(0, UserService.getUserPermissionType(0,1));
    }

    @Test
    void setNewCalendarAdminTests() {
        UserService.setCalendarUserPermission(1,1,1,true);
        UserService.setNewCalendarAdmin(1);
        Assertions.assertEquals(3, UserService.getUserPermissionType(2,1));
        Assertions.assertEquals(1, UserService.getUserPermissionType(1,1));

        UserService.setCalendarUserPermission(2,1,1,true);
        UserService.setNewCalendarAdmin(1);
        Assertions.assertEquals(1, UserService.getUserPermissionType(2,1));
        Assertions.assertEquals(3, UserService.getUserPermissionType(1,1));

        UserService.setCalendarUserPermission(1,3,2,true);
        UserService.setNewCalendarAdmin(3);
        Assertions.assertEquals(3, UserService.getUserPermissionType(1,3));
    }
}

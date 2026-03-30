package schedulify.utils;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Clasa pentru testarea metodelor din clasa CalendarUtils
 */
public class CalendarUtilsTest {
    @BeforeAll
    public static void setup() {
        Platform.startup(() -> {});
    }

    @Test
    void makeTextTests() {
        Text temp = CalendarUtils.makeText("test",14);
        Assertions.assertEquals("Font[name=System Regular, family=System, style=Regular, size=14.0]",
                temp.getFont().toString());
        Assertions.assertEquals("test", temp.getText());

        temp = CalendarUtils.makeText("test 2",5);
        Assertions.assertEquals("Font[name=System Regular, family=System, style=Regular, size=5.0]",
                temp.getFont().toString());
        Assertions.assertEquals("test 2", temp.getText());

        temp = CalendarUtils.makeText("", 20);
        Assertions.assertEquals("Font[name=System Regular, family=System, style=Regular, size=20.0]",
                temp.getFont().toString());
        Assertions.assertEquals("", temp.getText());
    }

    @Test
    void makeButtonTests() {
        Button b = new Button("test");

        CalendarUtils.makeInactive(b);
        Assertions.assertTrue(b.isDisable());
        Assertions.assertFalse(b.isVisible());

        Button[] buttons = new Button[3];
        buttons[0] = (new Button("test1"));
        buttons[1] = (new Button("test2"));
        buttons[2] = (new Button("test3"));

        CalendarUtils.makeInactive(buttons);
        for (Button button : buttons) {
            Assertions.assertTrue(button.isDisable());
            Assertions.assertFalse(button.isVisible());
        }

        Button b1 = new Button("Button 5");
        Button b2 = new Button("Button 6");

        CalendarUtils.makeInactive(b1, b2);
        Assertions.assertTrue(b1.isDisable());
        Assertions.assertTrue(b2.isDisable());
        Assertions.assertFalse(b1.isVisible());
        Assertions.assertFalse(b2.isVisible());
    }

    @Test
    void ComboBoxElementsTests() {
        List<Integer> elements = new ArrayList<>();
        CalendarUtils.comboBoxElements(elements, 5);

        Assertions.assertEquals("[0, 1, 2, 3, 4]", elements.toString());

        elements.clear();
        CalendarUtils.comboBoxElements(elements, 10);
        Assertions.assertEquals("[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]", elements.toString());

        elements.clear();
        CalendarUtils.comboBoxElements(elements, 0);
        Assertions.assertEquals("[]", elements.toString());
    }
}

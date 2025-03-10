import main.Epic;
import main.Status;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    void equals() {
        Epic task = new Epic(1,"Задача 1", "Описание 1", Status.NEW);
        Epic task1 = new Epic(1, "Задача 2", "Описание 2", Status.DONE);

        assertEquals(task, task1);
    }

    @Test
    void notEqual() {
        Epic task = new Epic(1,"Задача 1", "Описание 1", Status.NEW);
        Epic task1 = new Epic(2, "Задача 1", "Описание 1", Status.NEW);

        assertNotEquals(task, task1);
    }
}
import main.Status;
import main.Subtask;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {
    @Test
    void equals() {
        Subtask task = new Subtask(1,"Задача 1", "Описание 1", Status.NEW, 3);
        Subtask task1 = new Subtask(1, "Задача 2", "Описание 2", Status.DONE, 3);

        assertEquals(task, task1);
    }

    @Test
    void notEqual() {
        Subtask task = new Subtask(1,"Задача 1", "Описание 1", Status.NEW,3);
        Subtask task1 = new Subtask(2, "Задача 1", "Описание 1", Status.NEW, 4);

        assertNotEquals(task, task1);
    }


}
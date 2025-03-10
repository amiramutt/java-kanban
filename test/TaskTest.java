import main.Status;
import main.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void equals() {
        Task task = new Task(1,"Задача 1", "Описание 1", Status.NEW);
        Task task1 = new Task(1, "Задача 2", "Описание 2", Status.DONE);

        assertEquals(task, task1);
    }

    @Test
    void notEqual() {
        Task task = new Task(1,"Задача 1", "Описание 1", Status.NEW);
        Task task1 = new Task(2, "Задача 1", "Описание 1", Status.NEW);

        assertNotEquals(task, task1);
    }
}
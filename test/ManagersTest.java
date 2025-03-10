import main.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ManagersTest {

    @Test
    void shouldCreateTaskManagerAndAddTask() {
        Managers managers = new Managers();
        TaskManager taskManager = managers.getDefault();
        Task task = new Task(1,"Задача 1", "Описание 1", Status.NEW);
        taskManager.addNewTask(task);
        assertEquals(taskManager.getTaskById(task.getId()), task);
    }

    @Test
    void shouldCreateHistoryManagerAndAddTask() {
        Managers managers = new Managers();
        HistoryManager historyManager = managers.getDefaultHistory();

        Task task = new Task(1,"Задача 1", "Описание 1", Status.NEW);
        historyManager.add(task);
        assertEquals(historyManager.getHistory().get(0),task);
    }
}

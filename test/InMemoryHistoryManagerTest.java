import main.InMemoryHistoryManager;
import main.Status;
import main.Task;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private static InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
    private static Task task = new Task("Задача 1", "Описание 1");

    /*
    @AfterEach
    public void afterEach() {
        history.clear();
    }
    */

    @Test
    void add() {
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История пустая.");
        assertEquals(1, history.size(), "История пустая.");
        assertEquals(task, history.get(0), "История не идентична.");
    }

    @Test
    void doesntSavePreviousVersionsOfTasksWhenAddsToHistory() {
        historyManager.add(task);
        task.setStatus(Status.DONE);
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();

        assertEquals(1, history.size(), "История пустая.");
        assertEquals(task, history.get(0), "История не идентична.");
    }

    @Test
    void remove() {
        historyManager.add(task);
        Task task2 = new Task("Задача 2", "Описание 2",Status.DONE);
        task2.setId(2);
        historyManager.add(task2);

        historyManager.remove(0);
        historyManager.remove(2);

        final List<Task> history = historyManager.getHistory();

        assertEquals(0, history.size(), "История не пустая.");
    }
}
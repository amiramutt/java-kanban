package test;

import main.InMemoryHistoryManager;
import main.InMemoryTaskManager;
import main.Status;
import main.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private static InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
    private static Task task = new Task("Задача 1", "Описание 1");
    private static List<Task> history = historyManager.getHistory();


    @AfterEach
    public void afterEach() {
        history.clear();
    }

    @Test
    void add() {
        historyManager.add(task);
        assertNotNull(history, "История пустая.");
        assertEquals(1, history.size(), "История пустая.");
        assertEquals(task, history.get(0), "История не идентична.");
    }

    @Test
    void savesPreviousVersionsOfTasksWhenAddsToHistory() {
        historyManager.add(task);
        task.setStatus(Status.DONE);
        historyManager.add(task);

        Task task2 = new Task("Задача 1", "Описание 1",Status.DONE);

        assertNotNull(history, "История не пустая.");
        assertEquals(2, history.size(), "История не пустая.");
        assertEquals(task, history.get(0), "История не идентична.");
        assertEquals(task2, history.get(1), "История не идентична.");
    }
}
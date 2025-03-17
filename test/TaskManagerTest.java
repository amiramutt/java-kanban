import main.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

    T taskManager;

    @BeforeEach
    void beforeEach() {
        init();
        taskManager.deleteAllEpics();
        taskManager.deleteAllTasks();
        taskManager.deleteAllSubtasks();
    }

    abstract void init();

    @Test
    void shouldCalculateEpicStatusWhenAllSubtasksNew() {
        Epic epic = new Epic("Epic 1", "Test Epic");
        taskManager.addNewEpic(epic);
        Epic newEpic = taskManager.getEpics().get(0);
        Subtask subtask1 = new Subtask("Subtask 1", "Test Subtask", newEpic.getId());
        Subtask subtask2 = new Subtask("Subtask 2", "Test Subtask", newEpic.getId());
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        assertEquals(Status.NEW, newEpic.getStatus());
    }

    @Test
    void shouldCalculateEpicStatusWhenAllSubtasksDone() {
        Epic epic = new Epic("Epic 2", "Test Epic");
        taskManager.addNewEpic(epic);
        Epic newEpic = taskManager.getEpics().get(0);
        Subtask subtask1 = new Subtask("Subtask 1", "Test Subtask", Status.DONE, newEpic.getId());
        Subtask subtask2 = new Subtask("Subtask 2", "Test Subtask", Status.DONE, newEpic.getId());
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        assertEquals(Status.DONE, newEpic.getStatus());
    }

    @Test
    void shouldCalculateEpicStatusWhenSubtasksNewAndDone() {
        Epic epic = new Epic("Epic 3", "Test Epic");
        taskManager.addNewEpic(epic);
        Epic newEpic = taskManager.getEpics().get(0);
        Subtask subtask1 = new Subtask("Subtask 1", "Test Subtask", Status.NEW, newEpic.getId());
        Subtask subtask2 = new Subtask("Subtask 2", "Test Subtask", Status.DONE, newEpic.getId());
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        assertEquals(Status.IN_PROGRESS, newEpic.getStatus());
    }

    @Test
    void shouldCalculateEpicStatusWhenSubtasksInProgress() {
        Epic epic = new Epic("Epic 4", "Test Epic");
        taskManager.addNewEpic(epic);
        Epic newEpic = taskManager.getEpics().get(0);
        Subtask subtask1 = new Subtask("Subtask 1", "Test Subtask", Status.IN_PROGRESS, newEpic.getId());
        Subtask subtask2 = new Subtask("Subtask 2", "Test Subtask", Status.IN_PROGRESS, newEpic.getId());
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        assertEquals(Status.IN_PROGRESS, newEpic.getStatus());
    }

    @Test
    void shouldNotAllowOverlappingTasks() {
        Task task1 = new Task("Task 1", "Description", Duration.ofMinutes(60), LocalDateTime.of(2024, 3, 20, 10, 0));
        Task task2 = new Task("Task 2", "Description", Duration.ofMinutes(60), LocalDateTime.of(2024, 3, 20, 10, 30));
        taskManager.addNewTask(task1);

        Exception exception = assertThrows(ManagerIntersectionException.class, () -> {
            taskManager.addNewTask(task2);
        });

        assertEquals("Задача пересекается с текущими задачами", exception.getMessage());
    }
}

package test;

import main.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    private static InMemoryTaskManager taskManager = new InMemoryTaskManager();
    
    @AfterEach
    void afterEach() {
        taskManager.deleteAllEpics();
        taskManager.deleteAllTasks();
        taskManager.deleteAllSubtasks();
    }

    @Test
    void subtaskShouldNotBeItsOwnEpic() {
        Epic epic = new Epic(1,"Эпик 1","Описание 1");
        taskManager.addNewEpic(epic);
        Epic finalEpic = taskManager.getEpics().get(0);

        Subtask subtask1 = new Subtask("Задача 1", "Описание 1", Status.NEW,finalEpic.getId());
        Subtask subtask2 = new Subtask(finalEpic.getId(),"Задача 2", "Описание 2", Status.NEW,finalEpic.getId());

        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);

        ArrayList<Subtask> epicSubtasks = finalEpic.getSubtasks();
        for (Subtask subtask: epicSubtasks) {
            assertNotEquals(subtask.getId(),finalEpic.getId(),"Эпик добавляет себя в кач-ве подзадачи.");
        }
    }

    @Test
    void epicShouldNotBeItsOwnSubtask() {
        Subtask task = new Subtask(1,"Задача 1", "Описание 1", Status.NEW,1);
        taskManager.addNewSubtask(task);
        assertEquals(taskManager.getSubtasks().size(),0,"Подзадача добавляет себя в кач-ве эпика.");
    }

    @Test
    void addNewTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        taskManager.addNewTask(task);
        final int taskId = task.getId();
        final Task savedTask = taskManager.getTaskById(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void addNewEpic() {
        Epic epic = new Epic("Эпик 1","Описание 1");
        taskManager.addNewEpic(epic);
        final int taskId = epic.getId();
        final Task savedTask = taskManager.getEpicById(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(epic, savedTask, "Задачи не совпадают.");

        final List<Epic> epics = taskManager.getEpics();

        assertNotNull(epics, "Задачи не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic, epics.get(0), "Задачи не совпадают.");
    }

    @Test
    void addNewSubtask() {
        Epic epic = new Epic("Эпик 1","Описание 1");
        taskManager.addNewEpic(epic);
        final int epicId = epic.getId();
        Subtask subtask = new Subtask("Подзадача 1","Описание 1",epicId);

        taskManager.addNewSubtask(subtask);
        final int taskId = subtask.getId();
        final Task savedTask = taskManager.getSubtaskById(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(subtask, savedTask, "Задачи не совпадают.");

        final List<Subtask> tasks = taskManager.getSubtasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(subtask, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void shouldNotConflictWhenAddingTasksByIdAndWithout() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        Task task2 = new Task(2,"Test addNewTask", "Test addNewTask description", Status.NEW);
        taskManager.addNewTask(task);
        taskManager.addNewTask(task2);
        final int taskId = task.getId();
        final Task savedTask = taskManager.getTaskById(taskId);
        final Task savedTask2 = taskManager.getTaskById(task2.getId());

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");
        assertNotNull(savedTask2, "Задача не найдена.");
        assertEquals(task2, savedTask2, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(2, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
        assertEquals(task2, tasks.get(1), "Задачи не совпадают.");
    }

    @Test
    void shouldNotChangeTaskPropertiesWhenAddsTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        taskManager.addNewTask(task);
        final int taskId = task.getId();
        final Task savedTask = taskManager.getTaskById(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");
        assertEquals(task.getName(), savedTask.getName(), "Задачи не совпадают.");
        assertEquals(task.getDescription(), savedTask.getDescription(), "Задачи не совпадают.");
        assertEquals(task.getStatus(), savedTask.getStatus(), "Задачи не совпадают.");
    }


}
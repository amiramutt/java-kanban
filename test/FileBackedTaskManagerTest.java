import main.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager>{
    private File file;

    @BeforeEach
    void beforeEach() {
        init();
    }

    @Override
    protected void init() {
        file = new File("test", ".csv");
        taskManager = new FileBackedTaskManager(file);
    }

    @Test
    void shouldSaveAndLoadTasksToFromFile() {
        Task task1 = new Task("Task 1", "Description 1", Duration.ofMinutes(50), LocalDateTime.of(2025, 03, 17, 05, 51));
        Epic epic = new Epic("Epic 1", "Description 1", LocalDateTime.of(2025, 03, 15, 05, 51), Duration.ofMinutes(1440));
        Subtask subtask1 = new Subtask("Subtask 1","Description 1",3, Duration.ofMinutes(30), LocalDateTime.of(2025, 03, 17, 01, 53));

        taskManager.addNewTask(task1);
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(subtask1);
        FileBackedTaskManager newManager = FileBackedTaskManager.loadFromFile(file);

        assertDoesNotThrow(() -> {
            assertTrue(file.length() > 0, "Файл не сохранил задачи");
            assertEquals(taskManager.getTasks().size(), newManager.getTasks().size());
            assertEquals(taskManager.getSubtasks().size(), newManager.getSubtasks().size());
            assertEquals(taskManager.getEpics().size(), newManager.getEpics().size());
        }, "Ошибки при работе с файлом");
    }
}
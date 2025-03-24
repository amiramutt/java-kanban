package main;

import java.util.ArrayList;
import java.util.Set;
/*
Код можно сделать ещё лаконичнее, пробрасывая NotFoundException в TaskManager.
    Тогда в обработчиках не нужно проверять экземпляр Task на null — можно обрабатывать сразу исключение.
    Реализуйте такой подход.
    Также добавьте try — catch — он будет обрабатывать все исключения, которые возникают во время работы
    программы.
 */

public interface TaskManager {
    ArrayList<Task> getTasks();

    ArrayList<Subtask> getSubtasks();

    ArrayList<Epic> getEpics();

    ArrayList<Subtask> getEpicSubtasks(int id);

    void deleteAllTasks();

    void deleteAllSubtasks();

    void deleteAllEpics();

    Task getTaskById(int id);

    Subtask getSubtaskById(int id);

    Epic getEpicById(int id);

    void addNewTask(Task task);

    void addNewSubtask(Subtask subtask);

    void addNewEpic(Epic epic);

    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void deleteTaskById(int id);

    void deleteSubtaskById(int id);

    void deleteEpicById(int id);

    ArrayList<Task> getHistory();

    Set<Task> getPrioritizedTasks();
}

package main;

import java.util.ArrayList;

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
}

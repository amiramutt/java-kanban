import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");

        TaskManager taskManager = new TaskManager();

        Task task1 = new Task("Задача 1", "Описание 1");

        Subtask subtask1 = new Subtask("Подзадача 1","Описание 1",2);
        Subtask subtask2 = new Subtask("Подзадача 2","Описание 2",2);
        Subtask subtask3 = new Subtask("Подзадача 3","Описание 3",3);
        ArrayList<Subtask> subtasks1 = new ArrayList<>();
        subtasks1.add(subtask1);
        subtasks1.add(subtask2);
        ArrayList<Subtask> subtasks2 = new ArrayList<>();
        subtasks2.add(subtask3);
        Epic epic1 = new Epic("Эпик 1","Описание 1",subtasks1);
        Epic epic2 = new Epic("Эпик 2", "Описание 1",subtasks2);

        System.out.println(epic1);
        System.out.println(epic2);
        System.out.println(task1);
        System.out.println(subtask1);
        System.out.println(subtask2);
        System.out.println(subtask3);

        taskManager.addNewTask(task1); // 1
        taskManager.addNewEpic(epic1); // 2
        taskManager.addNewEpic(epic2); // 3
        taskManager.addNewSubtask(subtask1); // 4
        taskManager.addNewSubtask(subtask2); // 5
        taskManager.addNewSubtask(subtask3); // 6

        HashMap<Integer, Task> tasks = taskManager.tasks;
        HashMap<Integer, Subtask> subtasks = taskManager.subtasks;
        HashMap<Integer, Epic> epics = taskManager.epics;

        System.out.println("all tasks " + tasks);
        System.out.println("all subtasks " + subtasks);
        System.out.println("all epics " + epics);

        Task task2 = new Task("Задача 1", "Описание 1", Status.DONE);
        taskManager.updateTask(task2, 1);
        System.out.println("update task 1 " + tasks);

        Subtask subtask4 = new Subtask("Подзадача 1","Описание 1", Status.DONE,2);
        taskManager.updateSubtask(subtask4,4);
        System.out.println("update subtask 3, subtasks: " + subtasks);
        System.out.println("update subtask 3, epics: " + epics);

        Subtask subtask5 = new Subtask("Подзадача 3","Описание 3", Status.DONE,3);
        taskManager.updateSubtask(subtask5,6);
        System.out.println("update subtask 4, subtasks: " + subtasks);
        System.out.println("update subtask 4, epics: " + epics);

        taskManager.deleteTaskById(1);
        System.out.println("delete by task id 1 tasks: " + tasks);

        taskManager.deleteSubtaskById(6);
        System.out.println("delete by subtask id 6 tasks: " + tasks);
        System.out.println("delete by subtask id 6 subtasks: " + subtasks);
        System.out.println("delete by subtask id 6 epics: " + epics);

        taskManager.deleteEpicById(2);
        System.out.println("delete by epic id 2 tasks: " + tasks);
        System.out.println("delete by epic id 2 subtasks: " + subtasks);
        System.out.println("delete by epic id 2 epics: " + epics);

        taskManager.deleteAllTasks();
        System.out.println("delete all tasks " + tasks);
        taskManager.deleteAllSubtasks();
        System.out.println("delete all subtasks " + subtasks);
        taskManager.deleteAllEpics();
        System.out.println("delete all epics " + epics);


    }
}

package main;

import java.io.File;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");

        Managers managers = new Managers();
        TaskManager taskManager = managers.getDefault();

        Task task1 = new Task("Задача 1", "Описание 1");

        Subtask subtask1 = new Subtask("Подзадача 1","Описание 1",2);
        Subtask subtask2 = new Subtask("Подзадача 2","Описание 2",2);
        Subtask subtask3 = new Subtask("Подзадача 3","Описание 3",3);
        ArrayList<Subtask> subtasks1 = new ArrayList<>();
        subtasks1.add(subtask1);
        subtasks1.add(subtask2);
        ArrayList<Subtask> subtasks2 = new ArrayList<>();
        subtasks2.add(subtask3);
        Epic epic1 = new Epic("Эпик 1","Описание 1");
        Epic epic2 = new Epic("Эпик 2", "Описание 1");

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


        System.out.println("all tasks " + taskManager.getTasks());
        System.out.println("all subtasks " + taskManager.getSubtasks());
        System.out.println("all epics " + taskManager.getEpics());

        taskManager.getTaskById(1);
        taskManager.getEpicById(2);
        taskManager.getEpicById(3);
        taskManager.getEpicById(2);
        taskManager.getSubtaskById(5);
        taskManager.getTaskById(1);
        taskManager.getEpicById(2);
        taskManager.getEpicById(3);
        taskManager.getEpicById(2);
        taskManager.getSubtaskById(5);

        printAllTasks(taskManager);

        Task task2 = new Task(1,"Задача 1", "Описание 1", Status.DONE);
        taskManager.updateTask(task2);
        System.out.println("update task 1 " + taskManager.getTasks());

        Subtask subtask4 = new Subtask(4,"Подзадача 1","Описание 1", Status.DONE,2);
        taskManager.updateSubtask(subtask4);
        System.out.println("update subtask 3, subtasks: " + taskManager.getSubtasks());
        System.out.println("update subtask 3, epics: " + taskManager.getEpics());

        taskManager.getEpicById(2);

        Subtask subtask5 = new Subtask(6,"Подзадача 3","Описание 3", Status.DONE,3);
        taskManager.updateSubtask(subtask5);
        System.out.println("update subtask 4, subtasks: " + taskManager.getSubtasks());
        System.out.println("update subtask 4, epics: " + taskManager.getEpics());

        taskManager.deleteTaskById(1);
        System.out.println("delete by task id 1 tasks: " + taskManager.getTasks());

        taskManager.getSubtaskById(6);

        taskManager.deleteSubtaskById(6);
        System.out.println("delete by subtask id 6 tasks: " + taskManager.getTasks());
        System.out.println("delete by subtask id 6 subtasks: " + taskManager.getSubtasks());
        System.out.println("delete by subtask id 6 epics: " + taskManager.getEpics());

        taskManager.deleteEpicById(2);
        System.out.println("delete by epic id 2 tasks: " + taskManager.getTasks());
        System.out.println("delete by epic id 2 subtasks: " + taskManager.getSubtasks());
        System.out.println("delete by epic id 2 epics: " + taskManager.getEpics());

        taskManager.deleteAllTasks();
        System.out.println("delete all tasks " + taskManager.getTasks());
        taskManager.deleteAllSubtasks();
        System.out.println("delete all subtasks " + taskManager.getSubtasks());
        taskManager.deleteAllEpics();
        System.out.println("delete all epics " + taskManager.getEpics());

        printAllTasks(taskManager);

    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getEpics()) {
            System.out.println(epic);

            for (Task task : manager.getEpicSubtasks(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getSubtasks()) {
            System.out.println(subtask);
        }


        System.out.println("История:");

        if (((InMemoryTaskManager) manager).inMemoryHistoryManager.getHistory().size() > 0) {
            for (Task task : ((InMemoryTaskManager) manager).inMemoryHistoryManager.getHistory()) {
                System.out.println(task);
            }
        }
    }
}


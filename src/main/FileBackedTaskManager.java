package main;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager (File file) {
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        final FileBackedTaskManager taskManager = new FileBackedTaskManager(file);
        int generatorId = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (generatorId == 0) {
                    generatorId = 1;
                    continue;
                }
                Task task = CSVTaskFormat.taskFromString(line);
                //System.out.println(line);
                //System.out.println(task);

                if (task.getId() > generatorId) {
                    generatorId = task.getId();
                }

                if (task instanceof Epic) {
                    taskManager.epics.put(task.getId(), (Epic) task);
                } else if (task instanceof Subtask) {
                    taskManager.subtasks.put(task.getId(), (Subtask) task);
                    Epic epic = taskManager.getEpicById(((Subtask) task).getEpicId());
                    ArrayList<Subtask> subtasks = new ArrayList<>();

                    if (epic.getSubtasks() != null) {
                        subtasks = epic.getSubtasks();
                    }
                    subtasks.add((Subtask) task);
                    epic.setSubtasks(subtasks);
                } else {
                    taskManager.tasks.put(task.getId(), task);
                }
            }

            br.close();
            taskManager.id = generatorId + 1;
        } catch (IOException e) {
            System.out.println("Произошла ошибка во время чтения файла.");
        }

        List<Integer> history = Collections.emptyList();

        return taskManager;
    }

    private void save(){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,type,name,status,description,epic\n");

            for (Task task : getTasks()) {
                writer.write(CSVTaskFormat.toString(task));
            }
            for (Epic epic : getEpics()) {
                writer.write(CSVTaskFormat.toString(epic));
            }
            for (Subtask subtask : getSubtasks()) {
                writer.write(CSVTaskFormat.toString(subtask));
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка во время записи файла.");
        }
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void addNewTask(Task task) {
        super.addNewTask(task);
        save();
    }

    @Override
    public void addNewSubtask(Subtask subtask) {
        super.addNewSubtask(subtask);
        save();
    }

    @Override
    public void addNewEpic(Epic epic) {
        super.addNewEpic(epic);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public ArrayList<Task> getTasks() {
        final ArrayList<Task> tasks = super.getTasks();
        //save();
        return tasks;
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        final ArrayList<Subtask> tasks = super.getSubtasks();
        //save();
        return tasks;
    }

    @Override
    public ArrayList<Epic> getEpics() {
        final ArrayList<Epic> tasks = super.getEpics();
        //save();
        return tasks;
    }

    @Override
    public ArrayList<Subtask> getEpicSubtasks(int id) {
        final ArrayList<Subtask> tasks = super.getEpicSubtasks(id);
        //save();
        return tasks;
    }

    @Override
    public Task getTaskById(int id) {
        final Task task = super.getTaskById(id);
        //save();
        return task;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        final Subtask task = super.getSubtaskById(id);
        //save();
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        final Epic task = super.getEpicById(id);
        //save();
        return task;
    }

    public static void main(String[] args) {
        File file = new File("./src/files/file.csv");
        //FileBackedTaskManager taskManager = new FileBackedTaskManager(file);
        FileBackedTaskManager fileBackedTaskManager1 = loadFromFile(file);
        System.out.println(fileBackedTaskManager1.getTasks());
        System.out.println(fileBackedTaskManager1.getSubtasks());
        System.out.println(fileBackedTaskManager1.getEpics());

        //add tasks
        Task task1 = new Task("Task 1", "Description 1");
        Subtask subtask1 = new Subtask("Subtask 1","Description 1",5);
        Subtask subtask2 = new Subtask("Subtask 2","Description 2",5);
        Subtask subtask3 = new Subtask("Subtask 3","Description 3",6);
        ArrayList<Subtask> subtasks1 = new ArrayList<>();
        subtasks1.add(subtask1);
        subtasks1.add(subtask2);
        ArrayList<Subtask> subtasks2 = new ArrayList<>();
        subtasks2.add(subtask3);
        Epic epic1 = new Epic("Epic 1","Description 1",subtasks1);
        Epic epic2 = new Epic("Epic 2", "Description 1",subtasks2);

        System.out.println(epic1);
        System.out.println(epic2);
        System.out.println(task1);
        System.out.println(subtask1);
        System.out.println(subtask2);
        System.out.println(subtask3);

        fileBackedTaskManager1.addNewTask(task1); // 1
        fileBackedTaskManager1.addNewEpic(epic1); // 2
        fileBackedTaskManager1.addNewEpic(epic2); // 3
        fileBackedTaskManager1.addNewSubtask(subtask1); // 4
        fileBackedTaskManager1.addNewSubtask(subtask2); // 5
        fileBackedTaskManager1.addNewSubtask(subtask3); // 6
        
        System.out.println("all tasks " + fileBackedTaskManager1.getTasks());
        System.out.println("all subtasks " + fileBackedTaskManager1.getSubtasks());
        System.out.println("all epics " + fileBackedTaskManager1.getEpics());
/*
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
        */
        /*
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

        printAllTasks(taskManager); */

        //FileBackedTaskManager fileBackedTaskManager1 = FileBackedTaskManager.loadFromFile(file);
        //test

        System.out.println(fileBackedTaskManager1.getTasks());
        System.out.println(fileBackedTaskManager1.getSubtasks());
        System.out.println(fileBackedTaskManager1.getEpics());
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

package main;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
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
                Task task = taskFromString(line);

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
        return tasks;
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        final ArrayList<Subtask> tasks = super.getSubtasks();
        return tasks;
    }

    @Override
    public ArrayList<Epic> getEpics() {
        final ArrayList<Epic> tasks = super.getEpics();
        return tasks;
    }

    @Override
    public ArrayList<Subtask> getEpicSubtasks(int id) {
        final ArrayList<Subtask> tasks = super.getEpicSubtasks(id);
        return tasks;
    }

    @Override
    public Task getTaskById(int id) {
        final Task task = super.getTaskById(id);
        return task;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        final Subtask task = super.getSubtaskById(id);
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        final Epic task = super.getEpicById(id);
        return task;
    }

    public static void main(String[] args) {
        File file = new File("./src/files/file.csv");
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

        fileBackedTaskManager1.addNewTask(task1); // 1
        fileBackedTaskManager1.addNewEpic(epic1); // 2
        fileBackedTaskManager1.addNewEpic(epic2); // 3
        fileBackedTaskManager1.addNewSubtask(subtask1); // 4
        fileBackedTaskManager1.addNewSubtask(subtask2); // 5
        fileBackedTaskManager1.addNewSubtask(subtask3); // 6

        System.out.println(fileBackedTaskManager1.getTasks());
        System.out.println(fileBackedTaskManager1.getSubtasks());
        System.out.println(fileBackedTaskManager1.getEpics());
    }

    private static String toString(Task task) {
        if (task instanceof Epic) {
            return String.format("%d,%s,%s,%s,%s,\n", task.getId(), TaskType.EPIC, task.getName(), task.getStatus(),
                    task.getDescription());
        } else if (task instanceof Subtask) {
            return String.format("%d,%s,%s,%s,%s,%s,\n", task.getId(), TaskType.SUBTASK, task.getName(), task.getStatus(),
                    task.getDescription(), ((Subtask) task).getEpicId());
        } else {
            return String.format("%d,%s,%s,%s,%s,\n", task.getId(), TaskType.TASK, task.getName(), task.getStatus(),
                    task.getDescription());
        }
    }

    private static Task taskFromString(String value) {
        String[] tasks = value.split(",");
        Status status = Status.IN_PROGRESS;

        for (int i = 0; i < tasks.length; i++) {
            tasks[i] = tasks[i].replaceAll("\"", "").trim();
        }
        if (tasks[3].equals("NEW")) {
            status = Status.NEW;
        } else if (tasks[3].equals("DONE")) {
            status = Status.DONE;
        }

        if (tasks[1].equals("EPIC")) {
            return new Epic(Integer.parseInt(tasks[0]), tasks[2], tasks[4], status);
        } else if (tasks[1].equals("SUBTASK")) {
            return new Subtask(Integer.parseInt(tasks[0]), tasks[2], tasks[4], status, Integer.parseInt(tasks[5]));
        } else {
            return new Task(Integer.parseInt(tasks[0]), tasks[2], tasks[4], status);
        }
    }

    private void save(){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,type,name,status,description,epic\n");

            for (Task task : getTasks()) {
                writer.write(toString(task));
            }
            for (Epic epic : getEpics()) {
                writer.write(toString(epic));
            }
            for (Subtask subtask : getSubtasks()) {
                writer.write(toString(subtask));
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка во время записи файла.");
        }
    }
}

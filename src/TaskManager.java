import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map.Entry;
public class TaskManager {
    private static int id = 1;
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Subtask> subtasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();

    public void getTasks() {
        System.out.println(tasks.toString());
    }

    public void getSubtasks() {
        System.out.println(subtasks.toString());
    }

    public void getEpics() {
        System.out.println(epics.toString());
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllSubtasks() {
        subtasks.clear();
    }

    public void deleteAllEpics() {
        epics.clear();
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public void addNewTask(Task task) {
        tasks.put(id, task);
        id = id + 1;
    }

    public void addNewSubtask(Subtask subtask) {
        subtasks.put(id, subtask);
        id = id + 1;
    }

    public void addNewEpic(Epic epic) {
        epics.put(id, epic);
        id = id + 1;
    }

    public void updateTask(Task task, int oldId) {

        tasks.remove(oldId);
        tasks.put(oldId, task);

    }

    public void updateSubtask(Subtask subtask, int oldId) {
        Subtask oldSubtask = subtasks.get(oldId);
        Epic epic = epics.get(oldSubtask.getEpicId());
        ArrayList<Subtask> oldSubtasks = epic.getSubtasks();

        Subtask epicSubtaskOld = new Subtask();

        for (Subtask epicSubtask : oldSubtasks) {
            if (epicSubtask.equals(subtask)) {
                epicSubtaskOld = epicSubtask;
            }
        }

        if (epicSubtaskOld != null) {
            oldSubtasks.remove(epicSubtaskOld);
        }

        oldSubtasks.add(subtask);
        subtasks.remove(oldId);
        subtasks.put(oldId, subtask);
        updateEpic(epic,oldSubtask.getEpicId());

    }

    public void updateEpic(Epic epic, int oldId) {
        int newSubtasksCnt = 0; // кол-во сабтасков со стасусом нью
        int doneSubtasksCnt = 0; // кол-во сабтасков со стасусом дан
        ArrayList<Subtask> oldEpicSubtasks = epics.get(oldId).getSubtasks();

        for (Subtask subtask : oldEpicSubtasks) {
            if (subtask.getStatus() == Status.NEW) {
                newSubtasksCnt++;
            } else if (subtask.getStatus() == Status.DONE) {
                doneSubtasksCnt++;
            }

        }

        if (oldEpicSubtasks == null) {
            epic.setStatus(Status.NEW);
        } else if (newSubtasksCnt == oldEpicSubtasks.size()){
            epic.setStatus(Status.NEW);
        } else if (oldEpicSubtasks.isEmpty()) {
            epic.setStatus(Status.NEW);
        } else if (doneSubtasksCnt == oldEpicSubtasks.size()) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
        epics.remove(oldId);
        epics.put(oldId, epic);


    }


    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    public void deleteSubtaskById(int id) {

        Epic epic = epics.get(subtasks.get(id).getEpicId());
        ArrayList<Subtask> oldSubtasks = epic.getSubtasks();
        Subtask oldSubtask = subtasks.get(id);

        Subtask subtaskToBeDeleted = new Subtask();

        for (Subtask epicSubtask : oldSubtasks) {
            if (epicSubtask.equals(oldSubtask)) {
                subtaskToBeDeleted = epicSubtask;
            }
        }

        if (subtaskToBeDeleted != null) {
            oldSubtasks.remove(subtaskToBeDeleted);
        }

        subtasks.remove(id);

    }

    public void deleteEpicById(int id) {

        deleteSubtasksByEpicId(id);
        epics.remove(id);

    }

    public void deleteSubtasksByEpicId(int id) {

        Epic epic = epics.get(id);
        ArrayList<Subtask> oldSubtasks = epic.getSubtasks();

        for (Subtask epicSubtask : oldSubtasks) {
            subtasks.remove(getSubtaskId(epicSubtask));
        }

        oldSubtasks.clear();

    }

    public int getTaskId(Task task) {
        int taskId = -1;

        if (tasks.containsValue(task)) {
            for (Entry<Integer, Task> entry: tasks.entrySet()) {
                if (entry.getValue().equals(task)) {
                    taskId = entry.getKey();
                }
            }
        }

        return taskId;
    }

    public int getSubtaskId(Subtask subtask) {
        int subtaskId = -1;
        if (subtasks.containsValue(subtask)) {
            for (Entry<Integer, Subtask> entry: subtasks.entrySet()) {
                if (entry.getValue().equals(subtask)) {
                    subtaskId = entry.getKey();
                }
            }
        }

        return subtaskId;
    }

    public int getEpicId(Epic epic) {
        int epicId = -1;

        if (epics.containsValue(epic)) {
            for (Entry<Integer, Epic> entry: epics.entrySet()) {
                if (entry.getValue().equals(epic)) {
                    epicId = entry.getKey();
                }
            }
        }

        return epicId;
    }


}

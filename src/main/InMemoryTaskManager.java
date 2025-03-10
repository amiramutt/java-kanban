package main;

import java.util.HashMap;
import java.util.ArrayList;

public class InMemoryTaskManager implements TaskManager {
    protected static int id = 1;
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Subtask> subtasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();

    public ArrayList<Task> getTasks() {
        ArrayList<Task> allTasks = new ArrayList<>();

        for (Task task : tasks.values()) {
            allTasks.add(task);
        }

        return allTasks;
    }

    public ArrayList<Subtask> getSubtasks() {
        ArrayList<Subtask> allSubtasks = new ArrayList<>();

        if (subtasks.isEmpty()) {
            return allSubtasks;
        } else {
            for (Subtask subtask : subtasks.values()) {
                allSubtasks.add(subtask);
            }
            return allSubtasks;
        }
    }

    public ArrayList<Epic> getEpics() {
        ArrayList<Epic> allEpics = new ArrayList<>();

        for (Epic epic : epics.values()) {
            allEpics.add(epic);
        }

        return allEpics;
    }

    public ArrayList<Subtask> getEpicSubtasks(int id) {
        ArrayList<Subtask> epicSubtasks = new ArrayList<>();

        for (Subtask subtask : subtasks.values()) {
            if (subtask.getEpicId() == id) {
                epicSubtasks.add(subtask);
            }
        }

        return epicSubtasks;
    }

    public void deleteAllTasks() {
        for (Task task : tasks.values()) {
            inMemoryHistoryManager.remove(task.getId());
        }
        tasks.clear();
    }

    public void deleteAllSubtasks() {
        for (Subtask subtask : subtasks.values()) {
            inMemoryHistoryManager.remove(subtask.getId());
        }

        for (Epic epic : epics.values()) {
            epic.setSubtasks(new ArrayList<>());
            updateEpic(epic);
        }
        subtasks.clear();
    }

    public void deleteAllEpics() {
        for (Subtask subtask : subtasks.values()) {
            inMemoryHistoryManager.remove(subtask.getId());
        }
        for (Epic epic : epics.values()) {
            inMemoryHistoryManager.remove(epic.getId());
        }
        epics.clear();
        subtasks.clear();
    }

    public Task getTaskById(int id) {
        inMemoryHistoryManager.add(tasks.get(id));
        return tasks.get(id);
    }

    public Subtask getSubtaskById(int id) {
        inMemoryHistoryManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    public Epic getEpicById(int id) {
        inMemoryHistoryManager.add(epics.get(id));
        return epics.get(id);
    }

    public void addNewTask(Task task) {
        task.setId(id);
        tasks.put(id, task);
        id = id + 1;
    }

    public void addNewSubtask(Subtask subtask) {
        if (id != subtask.getEpicId()) {
            subtask.setId(id);
            subtasks.put(id, subtask);
            id = id + 1;
            ArrayList<Subtask> epicSubtasks = epics.get(subtask.getEpicId()).getSubtasks();
            epicSubtasks.add(subtask);
            updateEpic(epics.get(subtask.getEpicId()));
        }
    }

    public void addNewEpic(Epic epic) {
        epic.setId(id);
        epics.put(id, epic);
        id = id + 1;
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateSubtask(Subtask subtask) {

        subtasks.put(subtask.getId(), subtask);

        Epic epic = epics.get(subtask.getEpicId()); // получение эпика сабтаска
        ArrayList<Subtask> oldSubtasks = epic.getSubtasks(); // получение всех сабтасков эпика
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
        updateEpic(epic);
    }

    public void deleteTaskById(int id) {
        inMemoryHistoryManager.remove(id);
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
        updateEpic(epic);
        inMemoryHistoryManager.remove(id);
    }

    public void deleteEpicById(int id) {
        deleteSubtasksByEpicId(id);
        epics.remove(id);
        inMemoryHistoryManager.remove(id);
    }

    private void deleteSubtasksByEpicId(int id) {
        Epic epic = epics.get(id);
        ArrayList<Subtask> oldSubtasks = epic.getSubtasks();

        for (Subtask epicSubtask : oldSubtasks) {
            subtasks.remove(epicSubtask.getId());
            inMemoryHistoryManager.remove(epicSubtask.getId());
        }
        oldSubtasks.clear();
    }

    private void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);

        int newSubtasksCnt = 0; // кол-во сабтасков со стасусом нью

        int doneSubtasksCnt = 0; // кол-во сабтасков со стасусом дан

        ArrayList<Subtask> oldEpicSubtasks = epics.get(epic.getId()).getSubtasks();

        for (Subtask subtask : oldEpicSubtasks) {
            if (subtask.getStatus() == Status.NEW) {
                newSubtasksCnt++;
            } else if (subtask.getStatus() == Status.DONE) {
                doneSubtasksCnt++;
            }
        }

        if (newSubtasksCnt == oldEpicSubtasks.size()) {
            epic.setStatus(Status.NEW);
        } else if (oldEpicSubtasks.isEmpty()) {
            epic.setStatus(Status.NEW);
        } else if (doneSubtasksCnt == oldEpicSubtasks.size()) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

}

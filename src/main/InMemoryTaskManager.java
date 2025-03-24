package main;

import exceptions.ManagerIntersectionException;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    protected static int id = 1;
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Subtask> subtasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();
    Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime, Comparator.nullsLast(Comparator.naturalOrder())));

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public Set<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    public ArrayList<Subtask> getEpicSubtasks(int id) {
        return (ArrayList<Subtask>) subtasks.values().stream()
                .filter(subtask -> subtask.getEpicId() == id)
                .collect(Collectors.toList());
    }

    public void deleteAllTasks() {
        tasks.values().stream()
                        .forEach(task -> {
                            inMemoryHistoryManager.remove(task.getId());
                            prioritizedTasks.remove(task);
                        });
        tasks.clear();
    }

    public void deleteAllSubtasks() {
        subtasks.values().stream()
                .forEach(task -> {
                    inMemoryHistoryManager.remove(task.getId());
                    prioritizedTasks.remove(task);
                });
        epics.values().forEach(epic -> {
            epic.setSubtasks(new ArrayList<>());
            updateEpic(epic);
        });
        subtasks.clear();
    }

    public void deleteAllEpics() {
        epics.values().forEach(epic -> inMemoryHistoryManager.remove(epic.getId()));
        epics.clear();
        deleteAllSubtasks();
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
        if (isTaskOverlapping(task)) {
            throw new ManagerIntersectionException("Задача пересекается с текущими задачами");
        } else {
            task.setId(id);
            task.setStatus(Status.NEW);
            tasks.put(id, task);
            id = id + 1;
            addPrioritizedTask(task);
        }

    }

    public void addNewSubtask(Subtask subtask) {
        if (id != subtask.getEpicId()) {
            if (isTaskOverlapping(subtask)) {
                throw new ManagerIntersectionException("Задача пересекается с текущими задачами");
            } else {
                subtask.setId(id);
                if (subtask.getStatus() == null) {
                    subtask.setStatus(Status.NEW);
                }
                subtasks.put(id, subtask);
                id = id + 1;
                ArrayList<Subtask> epicSubtasks = epics.get(subtask.getEpicId()).getSubtasks();
                epicSubtasks.add(subtask);
                updateEpic(epics.get(subtask.getEpicId()));
                addPrioritizedTask(subtask);
            }
        }
    }

    public void addNewEpic(Epic epic) {
        epic.setId(id);
        if (epic.getStatus() == null) {
            epic.setStatus(Status.NEW);
        }
        epics.put(id, epic);
        id = id + 1;
    }

    public void updateTask(Task task) {
        if (isTaskOverlapping(task)) {
            throw new ManagerIntersectionException("Задача пересекается с текущими задачами");
        } else {
            tasks.put(task.getId(), task);
            addPrioritizedTask(task);
        }
    }

    public void updateSubtask(Subtask subtask) {
        if (isTaskOverlapping(subtask)) {
            throw new ManagerIntersectionException("Задача пересекается с текущими задачами");
        } else {
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
            addPrioritizedTask(subtask);
        }
    }

    public void deleteTaskById(int id) {
        Task task = tasks.remove(id);
        if (task != null) {
            prioritizedTasks.remove(task); // Удаляем из списка приоритетных задач
            inMemoryHistoryManager.remove(id); // Удаляем из истории
        }
    }

    public void deleteSubtaskById(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            prioritizedTasks.remove(subtask); // Удаляем из списка приоритетных задач
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.setSubtasks(epic.getSubtasks().stream()
                        .filter(s -> s.getId() != id)
                        .collect(Collectors.toCollection(ArrayList::new)));
            }
            inMemoryHistoryManager.remove(id); // Удаляем из истории
        }
    }

    public void deleteEpicById(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            deleteSubtasksByEpicId(id); // Удаляем все подзадачи эпика
            prioritizedTasks.remove(epic); // Удаляем эпик из приоритетных задач
            inMemoryHistoryManager.remove(id); // Удаляем эпик из истории
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return inMemoryHistoryManager.getHistory();
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

        epic.setEndTime(epic.getEndTime());
        epic.setStartTime(epic.getStartTime());
        epic.setDuration(epic.getDuration());
    }

    private void deleteSubtasksByEpicId(int id) {
        List<Integer> subtaskIds = subtasks.values().stream()
                .filter(subtask -> subtask.getEpicId() == id)
                .map(Subtask::getId)
                .toList(); // Собираем список id подзадач

        subtaskIds.forEach(epicId -> {
            Subtask subtask = subtasks.remove(epicId);
            if (subtask != null) {
                prioritizedTasks.remove(subtask); // Удаляем из приоритетных задач
                inMemoryHistoryManager.remove(epicId); // Удаляем из истории
            }
        });
    }

    private void addPrioritizedTask(Task task) {
        if (task.getDuration() != null && task.getStartTime() != null && !isTaskOverlapping(task)) {
            prioritizedTasks.add(task);
        }
    }

    private boolean isTaskOverlapping(Task task) {
        if (task.getStartTime() == null) {
            return false;
        } else {
            return getPrioritizedTasks().stream()
                    .anyMatch(priorTask ->
                        !(priorTask.getStartTime().isAfter(task.getEndTime()) ||
                        priorTask.getEndTime().isBefore(task.getStartTime()))
                    );
        }
    }

}

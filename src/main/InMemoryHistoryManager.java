package main;

import java.util.ArrayList;


public class InMemoryHistoryManager implements HistoryManager {
    ArrayList<Task> history = new ArrayList<>();

    public void add(Task task) {
        Task taskToAdd = new Task(task.getId(), task.getName(), task.getDescription(), task.getStatus());

        history.add(taskToAdd);

        if (history.size()>10) {
            history.remove(0);
        }
    }

    public ArrayList<Task> getHistory() {
        return history;
    }

}

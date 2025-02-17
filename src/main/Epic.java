package main;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Subtask> subtasks = new ArrayList<>();

    public Epic(int id, String name, String description) {
        super(id, name, description);
        this.subtasks = subtasks;
    }

    public Epic(int id, String name, String description, Status status) {
        super(id, name, description, status);
    }

    public Epic(String name, String description) {
        super(name, description);
        ArrayList<Subtask> epicSubtasks = new ArrayList<>();
        subtasks = epicSubtasks;
    }

    public Epic(String name, String description, ArrayList<Subtask> subtasks) {
        super(name, description);
        this.subtasks = subtasks;
    }

    public Epic(int id, String name, String description, Status status, ArrayList<Subtask> subtasks) {
        super(id, name, description, status);
        this.subtasks = subtasks;
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(ArrayList<Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    @Override
    public String toString() {
        String result =  "main.Epic{" +
                "id='" + getId() + '\'' +
                "name='" + getName() + '\'';

        if (getDescription() != null) {
            result = result + ", description.length=" + getDescription().length() + '\'' +
                    ", status=" + getStatus() +
                    ", subtasks=" + subtasks +
                    '}';
        } else {
            result = result + ", description=null" + '\'' +
                    ", status=" + getStatus() +
                    ", subtasks=" + subtasks +
                    '}';
        }
        return result;
    }
}

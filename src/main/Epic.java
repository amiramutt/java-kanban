package main;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Subtask> subtasks = new ArrayList<>();

    private static LocalDateTime endTime;

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

    public Epic(int id, String name, String description, Status status, LocalDateTime startTime, Duration duration) {
        super(id, name, description, status, duration, startTime);
    }

    public Epic(String name, String description, LocalDateTime startTime, Duration duration) {
        super(name, description, duration, startTime);
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(ArrayList<Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    @Override
    public LocalDateTime getStartTime() {
        if (subtasks.isEmpty()) {
            return null;
        }

        LocalDateTime earliest = null;

        for (Subtask subtask : subtasks) {
            if (subtask.getStartTime() != null) {
                if (earliest == null || subtask.getStartTime().isBefore(earliest)) {
                    earliest = subtask.getStartTime();
                }
            }
        }
        return earliest;
    }

    @Override
    public Duration getDuration() {
        Duration totalDuration = Duration.ZERO;
        if (!subtasks.isEmpty()) {
            for (Subtask subtask : subtasks) {
                if (subtask.getDuration() != null) {
                    totalDuration = totalDuration.plus(subtask.getDuration());
                }
            }
        }
        return totalDuration;
    }

    @Override
    public LocalDateTime getEndTime() {
        if (subtasks.isEmpty()) {
            return null;
        }

        LocalDateTime latest = null;

        for (Subtask subtask : subtasks) {
            if (subtask.getStartTime() != null && subtask.getDuration() != null) {
                LocalDateTime endTime = subtask.getStartTime().plus(subtask.getDuration());
                if (latest == null || endTime.isAfter(latest)) {
                    latest = endTime;
                }
            }
        }
        return latest;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        String result =  "main.Epic{" +
                "id='" + getId() + '\'' +
                "name='" + getName() + '\'';

        if (getDescription() != null) {
            result = result + ", description.length=" + getDescription().length() + '\'';
        } else {
            result = result + ", description=null" + '\'';
        }

        result = result  +
                ", status=" + getStatus() +
                ", subtasks=" + subtasks +
                ", startTime=" + getStartTime() + '\'' +
                ", duration=" + getDuration() +
                '}';

        return result;
    }
}

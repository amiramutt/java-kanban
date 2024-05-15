import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task{

    private ArrayList<Subtask> subtasks;
    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(String name, String description, ArrayList<Subtask> subtasks) {
        super(name, description);
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

        String result =  "Epic{" +
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

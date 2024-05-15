import java.util.ArrayList;
import java.util.Objects;

public class Subtask extends Task {

    private int epicId;

    public Subtask(String name, String description, Status status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
        setStatus(Status.NEW);
    }

    public Subtask() {
        super();
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {

        String result =  "Subtask{" +
                "name='" + getName() + '\'';

        if (getDescription() != null) {
            result = result + ", description.length=" + getDescription().length() + '\'' +
                    ", status=" + getStatus() +
                    ", epicId=" + epicId +
                    '}';
        } else {
            result = result + ", description=null" + '\'' +
                    ", status=" + getStatus() +
                    ", epicId=" + epicId +
                    '}';
        }

        return result;
    }
}

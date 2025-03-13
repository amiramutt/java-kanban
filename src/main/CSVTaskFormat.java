package main;

public class CSVTaskFormat {
    /* public static String toString(Task task) {
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

    public static Task taskFromString(String value) {
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
            //System.out.println("EPIC: " + Integer.parseInt(tasks[0]) + tasks[2] + tasks[3] + status);
            return new Epic(Integer.parseInt(tasks[0]), tasks[2], tasks[4], status);
        } else if (tasks[1].equals("SUBTASK")) {
            //System.out.println("SUBTASK: " + Integer.parseInt(tasks[0]) + tasks[2] + tasks[3] + status + Integer.parseInt(tasks[5]));
            return new Subtask(Integer.parseInt(tasks[0]), tasks[2], tasks[4], status, Integer.parseInt(tasks[5]));
        } else {
            //System.out.println("Task: " + Integer.parseInt(tasks[0]) + tasks[2] + tasks[3] + status);
            return new Task(Integer.parseInt(tasks[0]), tasks[2], tasks[4], status);
        }
    }

   public static String toString(HistoryManager historyManager) {

    }
*/
/*    public static List<Integer> historyFromString(String value) {

    }
*/
}

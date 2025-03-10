package main;

<<<<<<< HEAD

import java.io.File;

=======
>>>>>>> sprint_6-solution
public class Managers {
    public TaskManager getDefault() {
        return new FileBackedTaskManager(new File("./src/files/file.csv"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}


package main;


import java.io.File;

public class Managers {

    public TaskManager getDefault() {
        return new FileBackedTaskManager(new File("./src/files/file.csv"));
    }

    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }

}


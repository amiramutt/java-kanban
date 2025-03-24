package main;

import adapters.DurationAdapter;
import adapters.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import handlers.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class HttpTaskServer {

    public static final int PORT = 8080;
    private final HttpServer server;
    private final TaskManager manager;

    public HttpTaskServer(TaskManager manager) throws IOException {
        this.manager = manager;

        Task task1 = new Task("Задача 1", "Описание 1", Duration.ofMinutes(30), LocalDateTime.of(2024, 3, 20, 9, 0));

        Subtask subtask1 = new Subtask("Подзадача 1","Описание 1",2, Duration.ofMinutes(45), LocalDateTime.of(2024, 3, 20, 10, 0));
        Subtask subtask2 = new Subtask("Подзадача 2","Описание 2",2, Duration.ofMinutes(60), LocalDateTime.of(2024, 3, 20, 11, 0));
        Subtask subtask3 = new Subtask("Подзадача 3","Описание 3",3, Duration.ofMinutes(90), LocalDateTime.of(2024, 3, 20, 12, 30));
        ArrayList<Subtask> subtasks1 = new ArrayList<>();
        subtasks1.add(subtask1);
        subtasks1.add(subtask2);
        ArrayList<Subtask> subtasks2 = new ArrayList<>();
        subtasks2.add(subtask3);
        Epic epic1 = new Epic("Эпик 1","Описание 1", subtasks1);
        Epic epic2 = new Epic("Эпик 2", "Описание 1", subtasks2);

        manager.addNewTask(task1); // 1
        manager.addNewEpic(epic1); // 2
        manager.addNewEpic(epic2); // 3

        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);

        server.createContext("/tasks", new TasksHandler(manager));
        server.createContext("/subtasks", new SubtasksHandler(manager));
        server.createContext("/epics", new EpicsHandler(manager));
        server.createContext("/history", new HistoryHandler(manager));
        server.createContext("/prioritized", new PrioritizedHandler(manager));

    }

    public void start() {
        System.out.println("Starting TaskServer " + PORT);
        server.start();
    }

    public void stop() {
        System.out.println("Stopping TaskSerer " + PORT);
        server.stop(0);
    }

    public static void main(String[] args) throws IOException {
        TaskManager manager = Managers.getDefault();

        HttpTaskServer taskServer = new HttpTaskServer(manager);
        taskServer.start();
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .setPrettyPrinting();

        return gsonBuilder.create();
    }
}

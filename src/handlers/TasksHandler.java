package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.ManagerIntersectionException;
import main.HttpTaskServer;
import main.Task;
import main.TaskManager;
import java.io.IOException;
import java.util.List;

public class TasksHandler extends BaseHttpHandler implements HttpHandler {

    private final TaskManager taskManager;
    private final Gson gson;

    public TasksHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.gson = HttpTaskServer.getGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        final Integer idFromRequest = getIdFromPath(exchange.getRequestURI().getPath());

        switch (exchange.getRequestMethod()) {
            case "GET": {
                if (idFromRequest == null) {
                    final List<Task> tasks = taskManager.getTasks();
                    final String response = gson.toJson(tasks);
                    System.out.println("Получили все задачи");
                    sendText(exchange, response, 200);
                    return;
                }

                final Task task = taskManager.getTaskById(idFromRequest);

                if (task != null) {
                    final String response = gson.toJson(task);
                    System.out.println("Получили задачу id = " + idFromRequest);
                    sendText(exchange, response, 200);
                } else {
                    System.out.println("Задачи с id = " + idFromRequest + " не найдено.");
                    sendNotFound(exchange);
                }

                break;
            }
            case "DELETE": {
                taskManager.deleteTaskById(idFromRequest);
                System.out.println("Удалили задачу с id = " + idFromRequest);
                exchange.sendResponseHeaders(200, 0);
                exchange.close();
                break;
            }
            case "POST": {
                String json = readText(exchange);
                Task task = gson.fromJson(json, Task.class);
                Integer id = task.getId();

                if (id > 0) {
                    taskManager.updateTask(task);
                    System.out.println("Обновили задачу с id = " + idFromRequest);
                    exchange.sendResponseHeaders(200, 0);
                } else {
                    try {
                        taskManager.addNewTask(task);
                        int idAdded = taskManager.getTasks().get(taskManager.getTasks().size()-1).getId();
                        System.out.println("Создали задачу с id = " + idAdded);
                        final String response = gson.toJson(task);
                        sendText(exchange, response, 201);
                    } catch (ManagerIntersectionException e) {
                        System.out.println("Задача пересекается с существующей");
                        sendHasInteractions(exchange);
                    }
                }
            }
        }
    }

}

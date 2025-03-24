package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.ManagerIntersectionException;
import main.Epic;
import main.HttpTaskServer;
import main.Subtask;
import main.TaskManager;
import java.io.IOException;
import java.util.List;

public class EpicsHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public EpicsHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.gson = HttpTaskServer.getGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        final Integer idFromRequest = getIdFromPath(exchange.getRequestURI().getPath());
        final boolean returnEpicSubtasks = getEpicSubtasksFromPath(exchange.getRequestURI().getPath());

        switch (exchange.getRequestMethod()) {
            case "GET": {
                if (idFromRequest == null) {
                    final List<Epic> tasks = taskManager.getEpics();
                    final String response = gson.toJson(tasks);
                    System.out.println("Получили все задачи");
                    sendText(exchange, response, 200);
                    return;
                }

                final Epic task = taskManager.getEpicById(idFromRequest);

                if (task != null && returnEpicSubtasks) {
                    final List<Subtask> tasks = taskManager.getEpicById(idFromRequest).getSubtasks();
                    final String response = gson.toJson(tasks);
                    System.out.println("Получили все подзадачи эпика");
                    sendText(exchange, response, 200);
                    return;
                } else if (task != null) {
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
                taskManager.deleteEpicById(idFromRequest);
                System.out.println("Удалили задачу с id = " + idFromRequest);
                exchange.sendResponseHeaders(200, 0);
                exchange.close();
                break;
            }
            case "POST": {
                String json = readText(exchange);
                final Epic task = gson.fromJson(json, Epic.class);

                try {
                    taskManager.addNewEpic(task);
                    int idAdded = taskManager.getEpics().get(taskManager.getEpics().size()-1).getId();
                    System.out.println("Создали задачу с id = " + idAdded);
                    final String response = gson.toJson(task);
                    sendText(exchange, response, 201);
                } catch (ManagerIntersectionException e) {
                     System.out.println("Задача пересекается с существующей");
                     sendHasInteractions(exchange);
                }

            }
            default: {
                System.out.println("Такого метода не существует.");
                sendNotFound(exchange);
            }
        }
    }

    protected boolean getEpicSubtasksFromPath(String path) {
        final String[] uriSplitted = path.split("/");
        Integer id = null;
        if (uriSplitted.length >= 4) {
            id = Integer.parseInt(uriSplitted[3]);
        }

        if (id != null) {
            return true;
        } else {
            return false;
        }
    }

}

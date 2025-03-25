package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.HttpTaskServer;
import main.Task;
import main.TaskManager;
import java.io.IOException;
import java.util.Set;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public PrioritizedHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.gson = HttpTaskServer.getGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        final Integer idFromRequest = getIdFromPath(exchange.getRequestURI().getPath());

        switch (exchange.getRequestMethod()) {
            case "GET": {
                if (idFromRequest == null) {
                    final Set<Task> tasks = taskManager.getPrioritizedTasks();
                    final String response = gson.toJson(tasks);
                    System.out.println("Получили все задачи");
                    sendText(exchange, response, 200);
                    return;
                } else {
                    System.out.println("Такого метода не существует.");
                    sendNotFound(exchange);
                }

                break;
            }
            default: {
                System.out.println("Такого метода не существует.");
                sendNotFound(exchange);
            }
        }
    }

}
package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.*;

import java.io.IOException;
import java.util.ArrayList;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public HistoryHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.gson = HttpTaskServer.getGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        final Integer idFromRequest = getIdFromPath(exchange.getRequestURI().getPath());

        switch (exchange.getRequestMethod()) {
            case "GET": {
                if (idFromRequest == null) {
                    final ArrayList<Task> tasks = taskManager.getHistory();
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
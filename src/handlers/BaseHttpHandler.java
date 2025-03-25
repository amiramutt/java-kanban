package handlers;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler {
    private static final int NUM_PARTS_IN_PATH_WITH_ID = 3;

    protected String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }

    protected void sendText(HttpExchange h, String text, int status) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(status, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendNotFound(HttpExchange h) throws IOException {
        h.sendResponseHeaders(404, 0);
        h.close();
    }

    protected void sendHasInteractions(HttpExchange h) throws IOException {
        h.sendResponseHeaders(406, 0);
        h.getResponseBody().write("Есть пересечения с существующими задачами".getBytes(StandardCharsets.UTF_8));
        h.close();
    }

    protected Integer getIdFromPath(String path) {
        final String[] uriSplitted = path.split("/");
        Integer id = null;

        if (uriSplitted.length >= NUM_PARTS_IN_PATH_WITH_ID) {
            id = Integer.parseInt(uriSplitted[NUM_PARTS_IN_PATH_WITH_ID - 1]);
        }

        return id;
    }

}

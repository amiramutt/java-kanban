import com.google.gson.Gson;
import main.HttpTaskServer;
import main.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.time.LocalDateTime;

public class SerializationTest {
    Gson gson = HttpTaskServer.getGson();

    @Test
    public void serializationTest() {
        Task task = new Task("Test 1", "Testing task 1", Duration.ofMinutes(5), LocalDateTime.of(2020, 12, 12, 12, 12));
        String jsonStr = gson.toJson(task);
        System.out.println(jsonStr);
        Task task1 = gson.fromJson(jsonStr, Task.class);
        Assertions.assertEquals(task1, task);
    }
}

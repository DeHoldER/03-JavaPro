import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class HistoryService {

    String historyPath;
    List<String> HISTORY = new ArrayList<>();
    int HISTORY_MAX_LINES = 10; // Количество сообщений, выводимых из истории при запуске клиентской части

    public void setLogin(String login) {
        historyPath = (".\\history\\history_" + login + ".txt");
    }

    public void createHistoryFile() {
        if (Files.notExists(Paths.get("history"))) {
            try {
                Files.createDirectory(Paths.get("history"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!Files.exists(Paths.get(historyPath))) {
            try {
                Files.createFile(Paths.get(historyPath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<String> readHistory() {
        try {
            createHistoryFile();
            HISTORY = Files.readAllLines(Paths.get(historyPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> out = new ArrayList<>();
        int startingIndex = 0;
        if (HISTORY_MAX_LINES < HISTORY.size()) {
            startingIndex = HISTORY.size() - HISTORY_MAX_LINES;
        }
        for (int i = startingIndex; i < HISTORY.size(); i++) {
            out.add(HISTORY.get(i));
        }
        return out;
    }

    public void addMessageToHistory(String message) {
        try {
            createHistoryFile();
            if (Files.isWritable(Paths.get(historyPath))) {
                Files.writeString(Paths.get(historyPath), (message + System.lineSeparator()), StandardOpenOption.APPEND);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

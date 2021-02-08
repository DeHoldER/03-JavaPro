import java.io.IOException;
import java.util.logging.*;

public class LogService {

    private Handler handler;
    public Logger logger;

    public LogService(String className) {
        String filename = ("log_" + className + "%g.txt");
        try {
            handler = new FileHandler(filename, 10 * 1024, 20, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger = Logger.getLogger(className);
        logger.setUseParentHandlers(false);
        logger.addHandler(handler);
        logger.setLevel(Level.ALL);
        handler.setLevel(Level.ALL);
        // Хотел сделать симпл форматтер, но не пойму, как передать в него название класса из аргументов конструктора
//        handler.setFormatter(new SimpleFormatter());
    }


    public void logConfig(String msg) {
        logger.log(Level.CONFIG, msg);
    }

    public void logInfo(String msg) {
        logger.log(Level.INFO, msg);
    }
}

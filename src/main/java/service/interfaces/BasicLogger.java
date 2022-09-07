package service.interfaces;

import java.util.List;

public interface BasicLogger {

    void addMessageToLog(String message);
    void clearLogFile();
    List<String> getLoggedMessages();
}

package service.interfaces;

import models.Task;
import java.util.List;

public interface HistoryManager {

    void add(Task task);
    void remove(int id);
    void clearHistory();
    List<Task> getHistory();
}

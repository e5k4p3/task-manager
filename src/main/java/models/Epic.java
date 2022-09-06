package models;

import models.enums.TaskType;

import java.time.LocalDateTime;
import java.util.HashMap;

import static models.enums.TaskStatus.*;

public class Epic extends Task {
    private final HashMap<Integer, Subtask> listOfSubtasks;

    public Epic(int id,
                TaskType type,
                String name,
                String description) {
        super(id, type, name, description, NEW, LocalDateTime.of(9999, 1, 1, 0, 0), 0L);
        this.listOfSubtasks = new HashMap<>();
    }

    public void addSubtask(Subtask subtask) {
        listOfSubtasks.put(subtask.getId(), subtask);
        updateStatus();
    }

    public void removeSubtaskById(int id) {
        listOfSubtasks.remove(id);
        updateStatus();
    }

    public HashMap<Integer, Subtask> getListOfSubtasks() {
        return listOfSubtasks;
    }

    public void updateSubtask(Subtask subtask) {

    }

    private void updateStatus() {
        int doneStatus = 0;

        if (!listOfSubtasks.isEmpty()) {
            for (Subtask subtask : listOfSubtasks.values()) {
                if (subtask.getStatus() == IN_PROGRESS) {
                    status = IN_PROGRESS;
                    return;
                } else if (subtask.getStatus() == DONE) {
                    doneStatus++;
                }
            }
            if (doneStatus == listOfSubtasks.size()) {
                status = DONE;
            } else if (doneStatus == 0){
                status = NEW;
            } else {
                status = IN_PROGRESS;
            }
        } else {
            status = NEW;
        }
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status +
                ", startTime'" + startTime +
                ", duration'" + duration +
                ", listOfSubtasks=" + listOfSubtasks +
                '}';
    }
}

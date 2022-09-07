package models;

import models.enums.TaskType;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.TreeSet;

import static models.enums.TaskStatus.*;

public class Epic extends Task {
    private final TreeSet<Subtask> listOfSubtasks;

    public Epic(int id,
                TaskType type,
                String name,
                String description) {
        super(id, type, name, description, NEW, LocalDateTime.of(9999, 1, 1, 0, 0), 0L);
        this.listOfSubtasks = new TreeSet<>(Comparator.comparing(Subtask::getStartTime));
    }

    public TreeSet<Subtask> getListOfSubtasks() {
        return listOfSubtasks;
    }

    public void clearListOfSubtasks() {
        listOfSubtasks.clear();
        updateStatus();
    }

    public void addSubtask(Subtask subtask) {
        listOfSubtasks.add(subtask);
        updateStatus();
    }

    public void removeSubtask(Subtask subtask) {
        listOfSubtasks.remove(subtask);
        updateStatus();
    }

    public void updateSubtask(Subtask subtask) {

    }

    private void updateStatus() { //TODO добавить обновление startTime и duration!
        int doneStatus = 0;

        if (!listOfSubtasks.isEmpty()) {
            for (Subtask subtask : listOfSubtasks) {
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

package models;

import models.enums.*;

import java.time.LocalDateTime;
import java.util.Objects;

public class NormalTask extends Task {
    public NormalTask(int id,
                      TaskType type,
                      String name,
                      String description,
                      TaskStatus status,
                      LocalDateTime startTime,
                      Long duration) {
        super(id, type, name, description, status, startTime, duration);
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NormalTask normalTask = (NormalTask) o;
        return id == normalTask.id
                && type == normalTask.type
                && Objects.equals(name, normalTask.name)
                && Objects.equals(description, normalTask.description)
                && status == normalTask.status
                && Objects.equals(startTime, normalTask.startTime)
                && Objects.equals(duration, normalTask.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, name, description, status, startTime, duration);
    }

    @Override
    public String toString() {
        return "NormalTask{" +
                "id=" + id +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", startTime=" + startTime +
                ", duration=" + duration +
                '}';
    }
}

package models;

import models.enums.*;

import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {
    private final int epicId;

    public Subtask(int id,
                   TaskType type,
                   String name,
                   String description,
                   TaskStatus status,
                   LocalDateTime startTime,
                   Long duration,
                   int epicId) {
        super(id, type, name, description, status, startTime, duration);
        this.epicId = epicId;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subtask subtask = (Subtask) o;
        return id == subtask.id
                && type == subtask.type
                && Objects.equals(name, subtask.name)
                && Objects.equals(description, subtask.description)
                && status == subtask.status
                && Objects.equals(startTime, subtask.startTime)
                && Objects.equals(duration, subtask.duration)
                && epicId == subtask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, name, description, status, startTime, duration, epicId);
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + id +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", startTime=" + startTime +
                ", duration=" + duration +
                ", epicId=" + epicId +
                '}';
    }
}

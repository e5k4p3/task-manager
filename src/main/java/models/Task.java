package models;

import models.enums.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public abstract class Task {
    protected final int id;
    protected final TaskType type;
    protected String name;
    protected String description;
    protected TaskStatus status;
    protected final LocalDateTime startTime;
    protected final Duration duration;

    protected Task(int id,
                   TaskType type,
                   String name,
                   String description,
                   TaskStatus status,
                   LocalDateTime startTime,
                   Long duration) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = Duration.ofMinutes(duration);
    }

    public int getId() {
        return id;
    }

    public TaskType getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }
}

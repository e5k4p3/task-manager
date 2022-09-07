package service;

import models.Task;
import service.exceptions.TaskIsNullException;
import service.exceptions.TaskValidationException;

import java.util.Collection;

public class TaskValidator {
    private TaskValidator(){
    }

    public static<T extends Task> boolean validateTask(T task, Collection<T> tasksInList) throws TaskValidationException, TaskIsNullException {
        if (task != null && tasksInList != null) {
            for (T taskInList : tasksInList) {
                final boolean firstCondition = taskInList.getStartTime().isBefore(task.getStartTime());
                final boolean secondCondition = taskInList.getEndTime().isAfter(task.getStartTime());
                final boolean thirdCondition = taskInList.getStartTime().isAfter(task.getStartTime());
                final boolean fourthCondition = taskInList.getStartTime().isBefore(task.getEndTime());
                if (firstCondition && secondCondition) {
                   throw new TaskValidationException(task.getName()
                           + " заканчивается после начала " + taskInList.getName());
                } else if (thirdCondition && fourthCondition) {
                    throw new TaskValidationException(task.getName()
                            + " начинается до завершения " + taskInList.getName());
                }
            } return true;
        } else {
            throw new TaskIsNullException("Данная Task-а равна null");
        }
    }
}

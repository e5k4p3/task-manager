import models.Epic;
import models.NormalTask;
import models.Subtask;
import models.Task;
import service.TaskManagerWithResources;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static models.enums.TaskStatus.*;
import static models.enums.TaskType.*;

public class Main {
    public static void main(String[] args) {
        TaskManagerWithResources taskManager = new TaskManagerWithResources();
        DateTimeFormatter LOCAL_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");

        NormalTask task1 = new NormalTask(TaskManagerWithResources.getNewId(), NORMAL_TASK, "Первая таска", "Описание первой таски", NEW,
                LocalDateTime.parse("08:10 11.07.1995", LOCAL_DATE_TIME_FORMATTER), 30L);
        NormalTask task2 = new NormalTask(TaskManagerWithResources.getNewId(), NORMAL_TASK, "Вторая таска", "Описание второй таски", NEW,
                LocalDateTime.parse("09:20 11.07.1995", LOCAL_DATE_TIME_FORMATTER), 40L);
        taskManager.addNormalTask(task1);
        taskManager.addNormalTask(task2);
        Epic epic1 = new Epic(TaskManagerWithResources.getNewId(), EPIC, "Первый эпик", "Описание первого эпика");
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask(TaskManagerWithResources.getNewId(), SUBTASK, "Первая сабтаска", "Описание первой сабтаски",
                NEW, LocalDateTime.parse("18:20 11.07.1995", LOCAL_DATE_TIME_FORMATTER), 30L, epic1.getId());
        Subtask subtask2 = new Subtask(TaskManagerWithResources.getNewId(), SUBTASK, "Вторая сабтаска", "Описание второй сабтаски",
                IN_PROGRESS, LocalDateTime.parse("19:20 11.07.1995", LOCAL_DATE_TIME_FORMATTER), 10L, epic1.getId());
        Subtask subtask3 = new Subtask(TaskManagerWithResources.getNewId(), SUBTASK, "Третья сабтаска", "Описание третьей сабтаски",
                DONE, LocalDateTime.parse("20:20 11.07.1995", LOCAL_DATE_TIME_FORMATTER), 15L, epic1.getId());
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);
        Epic epic2 = new Epic(TaskManagerWithResources.getNewId(), EPIC, "Второй эпик", "Описание второго эпика");
        taskManager.addEpic(epic2);
        for (Task task : taskManager.getAllNormalTasks()) {
            System.out.println(task);
        }
        for (Task task : taskManager.getAllSubtasks()) {
            System.out.println(task);
        }
        for (Task task : taskManager.getAllEpics()) {
            System.out.println(task);
        }
    }
}

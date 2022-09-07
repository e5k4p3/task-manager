package service.interfaces;

import models.*;
import service.exceptions.EpicNotFoundException;

import java.util.List;

public interface TaskManager {

    void addNormalTask(NormalTask normalTask);
    void addSubtask(Subtask subtask);
    void addEpic(Epic epic);
    void removeNormalTaskById(int id);
    void removeSubtaskById(int id);
    void removeEpicById(int id);
    void updateNormalTask(NormalTask normalTask);
    void updateSubtask(Subtask subtask);
    void updateEpic(Epic epic);
    NormalTask getNormalTaskById(int id);
    Subtask getSubtaskById(int id);
    Epic getEpicById(int id) throws EpicNotFoundException;
    void removeAllNormalTasks();
    void removeAllSubtasks();
    void removeAllEpics();
    List<NormalTask> getAllNormalTasks();
    List<Subtask> getAllSubtasks();
    List<Epic> getAllEpics();
}

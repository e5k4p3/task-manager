package service;

import models.*;
import service.exceptions.NormalTaskIsNullException;
import service.exceptions.TaskValidationException;
import service.interfaces.TaskManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TaskManagerWithResources implements TaskManager {
    private static int id = 1;
    final private List<NormalTask> allNormalTasks;
    final private List<Epic> allEpics;
    private File file = null;

    public TaskManagerWithResources() {
        allNormalTasks = new ArrayList<>();
        allEpics = new ArrayList<>();
    }

    public TaskManagerWithResources(File file) {
        allNormalTasks = new ArrayList<>();
        allEpics = new ArrayList<>();
        this.file = file;
    }

    public int getNewId() {
        return id++;
    }

    @Override
    public void addNormalTask(NormalTask normalTask) {
        try {
            if (validateNormalTask(normalTask)) {
                allNormalTasks.add(normalTask);
            }
        } catch (NormalTaskIsNullException | TaskValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void addSubtask(Subtask subtask) {

    }

    @Override
    public void addEpic(Epic epic) {

    }

    @Override
    public void removeNormalTaskById(int id) {

    }

    @Override
    public void removeSubtaskById(int id) {

    }

    @Override
    public void removeEpicById() {

    }

    @Override
    public void updateNormalTask(NormalTask normalTask) {

    }

    @Override
    public void updateSubtask(Subtask subtask) {

    }

    @Override
    public void updateEpic(Epic epic) {

    }

    @Override
    public NormalTask getNormalTaskById(int id) {
        return null;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        return null;
    }

    @Override
    public Epic getEpicById(int id) {
        return null;
    }

    @Override
    public void removeAllNormalTasks() {

    }

    @Override
    public void removeAllSubtasks() {

    }

    @Override
    public void removeAllEpics() {

    }

    @Override
    public List<NormalTask> getAllNormalTasks() {
        return null;
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return null;
    }

    @Override
    public List<Epic> getAllEpics() {
        return null;
    }

    private boolean validateNormalTask(NormalTask normalTask) throws TaskValidationException, NormalTaskIsNullException {
        if (normalTask != null) {
            for (NormalTask normalTaskInList : allNormalTasks) {
                final boolean firstCondition = normalTaskInList.getStartTime().isBefore(normalTask.getStartTime());
                final boolean secondCondition = normalTaskInList.getEndTime().isAfter(normalTask.getStartTime());
                final boolean thirdCondition = normalTaskInList.getStartTime().isAfter(normalTask.getStartTime());
                final boolean fourthCondition = normalTaskInList.getStartTime().isBefore(normalTask.getEndTime());
                if (firstCondition && secondCondition) {
                    throw new TaskValidationException(normalTask.getName()
                            + " заканчивается после начала " + normalTaskInList.getName());
                } else if (thirdCondition && fourthCondition) {
                    throw new TaskValidationException(normalTask.getName()
                            + " начинается до завершения " + normalTaskInList.getName());
                }
            } return true;
        } else {
            throw new NormalTaskIsNullException("Добавляемая задача равна null");
        }
    }
}

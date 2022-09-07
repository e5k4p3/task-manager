package service;

import models.*;
import service.exceptions.*;
import service.interfaces.BasicLogger;
import service.interfaces.TaskManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskManagerWithResources implements TaskManager {
    private static int id = 1;
    final private List<NormalTask> allNormalTasks = new ArrayList<>();
    final private List<Epic> allEpics = new ArrayList<>();
    final private BasicLogger logger = new TaskManagerLogger(new File("src/resources/log.txt"));
    private File file = null;

    public TaskManagerWithResources() {
    }

    public TaskManagerWithResources(File file) {
        this.file = file;
    }

    public int getNewId() {
        return id++;
    }

    @Override
    public void addNormalTask(NormalTask normalTask) {
        try {
            if (TaskValidator.taskValidator(normalTask, allNormalTasks)) {
                allNormalTasks.add(normalTask);
            }
        } catch (TaskIsNullException e) {
            logger.addMessageToLog(NormalTaskIsNullException.class + " " + e.getMessage());
        } catch (TaskValidationException e) {
            logger.addMessageToLog(TaskValidationException.class + " " + e.getMessage());
        }
    }

    @Override
    public void addSubtask(Subtask subtask) {
        try {
            if (TaskValidator.taskValidator(subtask, getEpicById(subtask.getEpicId()).getListOfSubtasks())) {
                getEpicById(subtask.getEpicId()).addSubtask(subtask);
            }
        } catch (EpicNotFoundException e) {
            logger.addMessageToLog(EpicNotFoundException.class + " " + e.getMessage());
        } catch (TaskIsNullException e) {
            logger.addMessageToLog(SubtaskIsNullException.class + " " + e.getMessage());
        } catch (TaskValidationException e) {
            logger.addMessageToLog(TaskValidationException.class + " " + e.getMessage());
        }
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
    public Epic getEpicById(final int id) throws EpicNotFoundException {
        Optional<Epic> epic = allEpics.stream().filter(e -> e.getId() == id).findFirst();
        if (epic.isPresent()) {
            return epic.get();
        } else {
            throw new EpicNotFoundException("Epic-а с id " + id + " не существует");
        }
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
}

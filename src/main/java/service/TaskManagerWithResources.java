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
    private final BasicLogger logger = new TaskManagerLogger(new File("src/main/java/resources/log.txt"));
    private File saveFile = null;

    public TaskManagerWithResources() {
    }

    public TaskManagerWithResources(File saveFile) {
        this.saveFile = saveFile;
    }

    public static int getNewId() {
        return id++;
    }

    public void setNewSaveFile(File saveFile) {
        this.saveFile = saveFile;
    }

    @Override
    public void addNormalTask(NormalTask normalTask) {
        try {
            if (TaskValidator.validateTask(normalTask, allNormalTasks)) {
                allNormalTasks.add(normalTask);
            }
        } catch (TaskIsNullException e) {
            logger.addMessageToLog(NormalTaskIsNullException.class + " " + e.getMessage());
        } catch (TaskValidationException e) {
            logger.addMessageToLog(TaskValidationException.class + " " + e.getMessage());
        }
    }

    @Override
    public void addSubtask(Subtask subtask) { //TODO убрать костыли из этого метода, если получится
        try {
            if (subtask != null) {
                if (TaskValidator.validateTask(subtask, getEpicById(subtask.getEpicId()).getListOfSubtasks())) {
                    getEpicById(subtask.getEpicId()).addSubtask(subtask);
                }
            } else {
                throw new SubtaskIsNullException("Данная Task-а равна null.");
            }
        } catch (SubtaskIsNullException | TaskIsNullException e) {
            logger.addMessageToLog(SubtaskIsNullException.class + " " + e.getMessage());
        } catch (TaskValidationException e) {
            logger.addMessageToLog(TaskValidationException.class + " " + e.getMessage());
        }
    }

    @Override
    public void addEpic(Epic epic) {
        try {
            if (TaskValidator.validateTask(epic, allEpics)) {
                allEpics.add(epic);
            }
        } catch (TaskIsNullException e) {
            logger.addMessageToLog(EpicIsNullException.class + " " + e.getMessage());
        } catch (TaskValidationException e) {
            logger.addMessageToLog(TaskValidationException.class + " " + e.getMessage());
        }
    }

    @Override
    public void removeNormalTaskById(int id) {
        Optional<NormalTask> normalTaskToRemove = allNormalTasks.stream().filter(n -> n.getId() == id).findFirst();
        try {
            if (normalTaskToRemove.isPresent()) {
                allNormalTasks.remove(normalTaskToRemove.get());
            } else {
                throw new NormalTaskNotFoundException("NormalTask с id " + id + " не найден.");
            }
        } catch (NormalTaskNotFoundException e) {
            logger.addMessageToLog(NormalTaskNotFoundException.class + " " + e.getMessage());
        }
    }

    @Override
    public void removeSubtaskById(int id) {
        try {
            Optional<Subtask> subtaskToRemove = getAllSubtasks().stream().filter(s -> s.getId() == id).findFirst();
            if (subtaskToRemove.isPresent()) {
                getEpicById(subtaskToRemove.get().getEpicId()).removeSubtask(subtaskToRemove.get());
            } else {
                throw new SubtaskNotFoundException("Subtask с id " + id + " не найден.");
            }
        } catch (SubtaskNotFoundException e) {
            logger.addMessageToLog(SubtaskNotFoundException.class + " " + e.getMessage());
        }
    }

    @Override
    public void removeEpicById(int id) {
        Optional<Epic> epicToRemove= allEpics.stream().filter(e -> e.getId() == id).findFirst();
        try {
            if (epicToRemove.isPresent()) {
                allEpics.remove(epicToRemove.get());
            } else {
                throw new EpicNotFoundException("Epic с id " + id + " не найден.");
            }
        } catch (EpicNotFoundException e) {
            logger.addMessageToLog(EpicNotFoundException.class + " " + e.getMessage());
        }
    }

    @Override
    public void updateNormalTask(NormalTask normalTask) {
        try {
            if (normalTask != null) {
                if (getNormalTaskById(normalTask.getId()) != null) {
                    NormalTask taskToUpdate = getNormalTaskById(normalTask.getId());
                    taskToUpdate.setName(normalTask.getName());
                    taskToUpdate.setDescription(normalTask.getDescription());
                    taskToUpdate.setStatus(normalTask.getStatus());
                } else {
                    throw new NormalTaskNotFoundException("NormalTask с id " + normalTask.getId() + " не найден.");
                }
            } else {
                throw new NormalTaskIsNullException("Данная Task-а равна null.");
            }
        } catch (NormalTaskIsNullException e) {
            logger.addMessageToLog(NormalTaskIsNullException.class + " " + e.getMessage());
        } catch (NormalTaskNotFoundException e) {
            logger.addMessageToLog(NormalTaskNotFoundException.class + " " + e.getMessage());
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        try {
            if (subtask != null) {
                if (getSubtaskById(subtask.getId()) != null) {
                    Subtask subtaskToUpdate = getSubtaskById(subtask.getId());
                    subtaskToUpdate.setName(subtask.getName());
                    subtaskToUpdate.setDescription(subtask.getDescription());
                    subtaskToUpdate.setStatus(subtask.getStatus());
                } else {
                    throw new SubtaskNotFoundException("Subtask с id " + subtask.getId() + " не найден.");
                }
            } else {
                throw new SubtaskIsNullException("Данная Task-а равна null.");
            }
        } catch (SubtaskIsNullException e) {
            logger.addMessageToLog(SubtaskIsNullException.class + " " + e.getMessage());
        } catch (SubtaskNotFoundException e) {
            logger.addMessageToLog(SubtaskNotFoundException.class + " " + e.getMessage());
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        try {
            if (epic != null) {
                if (getEpicById(epic.getId()) != null) {
                    Epic epicToUpdate = getEpicById(epic.getId());
                    epicToUpdate.setName(epic.getName());
                    epicToUpdate.setDescription(epic.getDescription());
                } else {
                    throw new EpicNotFoundException("Epic с id " + epic.getId() +" не найден.");
                }
            } else {
                throw new EpicIsNullException("Данная Task-a равна null.");
            }
        } catch (EpicIsNullException e) {
            logger.addMessageToLog(EpicIsNullException.class + " " + e.getMessage());
        } catch (EpicNotFoundException e) {
            logger.addMessageToLog(EpicNotFoundException.class + " " + e.getMessage());
        }
    }

    @Override
    public NormalTask getNormalTaskById(int id) {
        Optional<NormalTask> normalTaskToGet = allNormalTasks.stream().filter(n -> n.getId() == id).findFirst();
        try {
            if (normalTaskToGet.isPresent()) {
                return normalTaskToGet.get();
            } else {
                throw new NormalTaskNotFoundException("NormalTask с id " + id + " не найден.");
            }
        } catch (NormalTaskNotFoundException e) {
            logger.addMessageToLog(NormalTaskNotFoundException.class + " " + e.getMessage());
        }
        return null;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        try {
            Optional<Subtask> subtaskToGet = getAllSubtasks().stream().filter(s -> s.getId() == id).findFirst();
            if (subtaskToGet.isPresent()) {
                return subtaskToGet.get();
            } else {
                throw new SubtaskNotFoundException("Subtask с id " + id + " не найден.");
            }
        } catch (SubtaskNotFoundException e) {
            logger.addMessageToLog(SubtaskNotFoundException.class + " " + e.getMessage());
        }
        return null;
    }

    @Override
    public Epic getEpicById(final int id) {
        try {
            Optional<Epic> epic = allEpics.stream().filter(e -> e.getId() == id).findFirst();
            if (epic.isPresent()) {
                return epic.get();
            } else {
                throw new EpicNotFoundException("Epic с id " + id + " не найден.");
            }
        } catch (EpicNotFoundException e) {
            logger.addMessageToLog(EpicNotFoundException.class + " " + e.getMessage());
        }
        return null;
    }

    @Override
    public void removeAllNormalTasks() {
        allNormalTasks.clear();
    }

    @Override
    public void removeAllSubtasks() {
        for (Epic epic : allEpics) {
            epic.clearListOfSubtasks();
        }
    }

    @Override
    public void removeAllEpics() {
        allEpics.clear();
    }

    @Override
    public List<NormalTask> getAllNormalTasks() {
        if (allNormalTasks.isEmpty()) {
            return new ArrayList<>();
        } else {
            return new ArrayList<>(allNormalTasks);
        }
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        if (allEpics.isEmpty()) {
            return new ArrayList<>();
        } else {
            ArrayList<Subtask> allSubtasks = new ArrayList<>();
            for (Epic epic : allEpics) {
                allSubtasks.addAll(epic.getListOfSubtasks());
            }
            return allSubtasks;
        }
    }

    @Override
    public List<Epic> getAllEpics() {
        if (allEpics.isEmpty()) {
            return new ArrayList<>();
        } else {
            return new ArrayList<>(allEpics);
        }
    }

    public List<Task> getHistory() {
        return null;
    }

    public void save() {

    }

    public List<String> getLogLines() {
        return logger.getLoggedMessages();
    }

    public void clearLogFile() {
        logger.clearLogFile();
    }
}

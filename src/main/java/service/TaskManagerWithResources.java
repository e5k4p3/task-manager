package service;

import models.*;
import service.exceptions.*;
import service.interfaces.BasicLogger;
import service.interfaces.HistoryManager;
import service.interfaces.TaskManager;

import java.io.File;
import java.util.*;

public class TaskManagerWithResources implements TaskManager {
    private static int id = 1;
    private final List<NormalTask> allNormalTasks = new ArrayList<>(); //TODO возможно стоит хранить таски сразу в Set, надо обдумать
    private final List<Epic> allEpics = new ArrayList<>();
    private final HistoryManager historyManager = new TaskHistoryManager();
    private final BasicLogger logger;

    public TaskManagerWithResources(File logFile) {
        this.logger = new TaskManagerLogger(logFile);
    }

    public static int getNewId() {
        return id++;
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
                historyManager.remove(normalTaskToRemove.get().getId());
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
                historyManager.remove(subtaskToRemove.get().getId());
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
                for (Subtask subtask : epicToRemove.get().getListOfSubtasks()) {
                    historyManager.remove(subtask.getId());
                }
                historyManager.remove(epicToRemove.get().getId());
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
                historyManager.add(normalTaskToGet.get());
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
                historyManager.add(subtaskToGet.get());
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
            Optional<Epic> epicToGet = allEpics.stream().filter(e -> e.getId() == id).findFirst();
            if (epicToGet.isPresent()) {
                historyManager.add(epicToGet.get());
                return epicToGet.get();
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

    public Set<Task> getSortedAllTasks() {
        TreeSet<Task> sortedTasks = new TreeSet<>(Comparator.comparing(Task::getId));
        sortedTasks.addAll(getAllNormalTasks());
        sortedTasks.addAll(getAllEpics());
        sortedTasks.addAll(getAllSubtasks());
        return sortedTasks;
    }

    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public void clearHistory() {
        historyManager.clearHistory();
    }

    public void saveToFile(File saveFile) {
        try {
            if (saveFile == null) {
                throw new ManagerSaveException("Путь до файла сохранения равен null");
            } else {
                TaskManagerSaveAndLoadService.save(saveFile, getSortedAllTasks(), historyManager.getHistory());
            }
        } catch (ManagerSaveException e) {
            logger.addMessageToLog(ManagerSaveException.class + " " + e.getMessage());
        }
    }

    public void loadFromFile(File saveFile) {
        try {
            if (saveFile == null) {
                throw new ManagerLoadException("Путь до файла сохранения равен null");
            }
        } catch (ManagerLoadException e) {
            logger.addMessageToLog(ManagerLoadException.class + " " + e.getMessage());
        }
    }

    public List<String> getLogLines() {
        return logger.getLoggedMessages();
    }

    public void clearLogFile() {
        logger.clearLogFile();
    }
}

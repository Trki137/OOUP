package hr.fer.zemris.ooup.texteditor.observer;

import hr.fer.zemris.ooup.texteditor.manager.ManagerInfo;

import java.util.function.Consumer;

public interface UndoManagerObserver {
    void undoStackInfo(ManagerInfo managerInfoConsumer);
    void redoStackInfo(ManagerInfo managerInfoConsumer);
}

package hr.fer.zemris.ooup.texteditor.manager;

import hr.fer.zemris.ooup.texteditor.command.EditAction;
import hr.fer.zemris.ooup.texteditor.observer.UndoManagerObserver;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class UndoManager{
    private final Stack<EditAction> undoStack;
    private final Stack<EditAction> redoStack;
    private static UndoManager undoManager =null;

    private final List<UndoManagerObserver> undoManagerObserverList = new LinkedList<>();
    private final List<UndoManagerObserver> redoManagerObserverList = new LinkedList<>();

    private UndoManager(){
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
    }

    public void undo(){
        if(undoStack.isEmpty()) return;

        EditAction action = undoStack.pop();
        action.execute_undo();
        redoStack.push(action);

        redoNotifyAll(ManagerInfo.NOT_EMPTY);
        if(undoStack.isEmpty())
            undoNotifyAll(ManagerInfo.EMPTY);
    }

    public void redo(){
        if(redoStack.isEmpty()) return;

        EditAction action = redoStack.pop();
        action.execute_do();
        undoStack.push(action);

        undoNotifyAll(ManagerInfo.NOT_EMPTY);
        if(redoStack.isEmpty())
            redoNotifyAll(ManagerInfo.EMPTY);
    }

    public void push(EditAction c){
        redoStack.clear();
        undoStack.push(c);
        undoNotifyAll(ManagerInfo.NOT_EMPTY);
        redoNotifyAll(ManagerInfo.EMPTY);
    }

    public static UndoManager getInstance(){
        if(undoManager == null)
            undoManager = new UndoManager();

        return undoManager;
    }

    public void addUndoListener(UndoManagerObserver observer){
        undoManagerObserverList.add(observer);
    }
    public void removeUndoListener(UndoManagerObserver observer){
        undoManagerObserverList.remove(observer);
    }
    public void undoNotifyAll(ManagerInfo managerInfoConsumer){
        undoManagerObserverList.forEach(observer -> observer.undoStackInfo(managerInfoConsumer));
    }

    public void addRedoListener(UndoManagerObserver observer){
        redoManagerObserverList.add(observer);
    }
    public void removeRedoListener(UndoManagerObserver observer){
        redoManagerObserverList.remove(observer);
    }
    public void redoNotifyAll(ManagerInfo managerInfoConsumer){
        redoManagerObserverList.forEach(observer -> observer.redoStackInfo(managerInfoConsumer));
    }



}

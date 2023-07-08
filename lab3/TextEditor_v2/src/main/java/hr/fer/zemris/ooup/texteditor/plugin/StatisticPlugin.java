package hr.fer.zemris.ooup.texteditor.plugin;

import hr.fer.zemris.ooup.texteditor.command.EditAction;
import hr.fer.zemris.ooup.texteditor.command.StatisticAction;
import hr.fer.zemris.ooup.texteditor.component.ClipboardStack;
import hr.fer.zemris.ooup.texteditor.manager.UndoManager;
import hr.fer.zemris.ooup.texteditor.model.TextEditorModel;

import javax.swing.*;

public class StatisticPlugin implements Plugin{


    @Override
    public String getName() {
        return "Statistic";
    }

    @Override
    public String getDescription() {
        return "Shows number of rows, letters and words in text document";
    }

    @Override
    public void execute(TextEditorModel model, UndoManager undoManager, ClipboardStack clipboardStack) {
        EditAction statisticAction = new StatisticAction(model,model.getFrame());
        statisticAction.execute_do();
        undoManager.push(statisticAction);
    }
}

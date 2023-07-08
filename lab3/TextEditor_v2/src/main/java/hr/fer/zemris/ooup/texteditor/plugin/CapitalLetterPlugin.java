package hr.fer.zemris.ooup.texteditor.plugin;

import hr.fer.zemris.ooup.texteditor.command.CapitalLetterAction;
import hr.fer.zemris.ooup.texteditor.command.EditAction;
import hr.fer.zemris.ooup.texteditor.component.ClipboardStack;
import hr.fer.zemris.ooup.texteditor.manager.UndoManager;
import hr.fer.zemris.ooup.texteditor.model.TextEditorModel;

public class CapitalLetterPlugin implements Plugin{
    @Override
    public String getName() {
        return "Capital letter";
    }

    @Override
    public String getDescription() {
        return "For each word in document first letter is changed to uppercase";
    }

    @Override
    public void execute(TextEditorModel model, UndoManager undoManager, ClipboardStack clipboardStack) {
        EditAction capitalLetterAction = new CapitalLetterAction(model);
        capitalLetterAction.execute_do();
        undoManager.push(capitalLetterAction);
    }
}

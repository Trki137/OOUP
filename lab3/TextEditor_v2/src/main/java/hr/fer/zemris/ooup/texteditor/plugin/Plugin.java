package hr.fer.zemris.ooup.texteditor.plugin;

import hr.fer.zemris.ooup.texteditor.component.ClipboardStack;
import hr.fer.zemris.ooup.texteditor.manager.UndoManager;
import hr.fer.zemris.ooup.texteditor.model.TextEditorModel;

public interface Plugin {
    String getName();
    String getDescription();
    void execute(TextEditorModel model, UndoManager undoManager, ClipboardStack clipboardStack);
}

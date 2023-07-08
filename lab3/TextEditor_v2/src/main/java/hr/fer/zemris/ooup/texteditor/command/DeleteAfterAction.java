package hr.fer.zemris.ooup.texteditor.command;

import hr.fer.zemris.ooup.texteditor.model.Location;
import hr.fer.zemris.ooup.texteditor.model.LocationRange;
import hr.fer.zemris.ooup.texteditor.model.TextEditorModel;

import java.util.ArrayList;
import java.util.List;

public class DeleteAfterAction implements EditAction{
    private final TextEditorModel textEditorModel;
    private List<String> prevLines;
    private Location prevCursorLocation;
    private LocationRange prevSelectionRange;

    public DeleteAfterAction(TextEditorModel textEditorModel){
        this.textEditorModel = textEditorModel;
    }
    @Override
    public void execute_do() {
        prevCursorLocation = textEditorModel.getCursorLocation();
        prevLines = new ArrayList<>(textEditorModel.getLines());
        prevSelectionRange = textEditorModel.getSelectionRange();
        textEditorModel.deleteAfter();
    }

    @Override
    public void execute_undo() {
        textEditorModel.setSelectionRange(prevSelectionRange);
        textEditorModel.setLines(prevLines);
        textEditorModel.setCursorLocation(prevCursorLocation);
    }
}

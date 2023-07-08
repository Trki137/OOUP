package hr.fer.zemris.ooup.texteditor.command;

import hr.fer.zemris.ooup.texteditor.model.Location;
import hr.fer.zemris.ooup.texteditor.model.LocationRange;
import hr.fer.zemris.ooup.texteditor.model.TextEditorModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InsertAction implements EditAction {
    private final TextEditorModel textEditorModel;
    private final String text;
    private List<String> prevLines;
    private Location prevCursorLocation;
    private LocationRange prevSelectionRange;

    public InsertAction(TextEditorModel textEditorModel,String text){
        this.textEditorModel = textEditorModel;
        this.text = text;
    }

    @Override
    public void execute_do() {
        prevCursorLocation = textEditorModel.getCursorLocation();
        prevLines = new ArrayList<>(textEditorModel.getLines());
        prevSelectionRange = textEditorModel.getSelectionRange();
        textEditorModel.insert(text);
    }

    @Override
    public void execute_undo() {
        textEditorModel.setSelectionRange(prevSelectionRange);
        textEditorModel.setLines(prevLines);
        textEditorModel.setCursorLocation(prevCursorLocation);
    }
}

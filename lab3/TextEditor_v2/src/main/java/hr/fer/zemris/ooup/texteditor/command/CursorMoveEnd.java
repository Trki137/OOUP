package hr.fer.zemris.ooup.texteditor.command;

import hr.fer.zemris.ooup.texteditor.model.Location;
import hr.fer.zemris.ooup.texteditor.model.LocationRange;
import hr.fer.zemris.ooup.texteditor.model.TextEditorModel;

import java.util.ArrayList;
import java.util.List;

public class CursorMoveEnd implements EditAction{
    private final TextEditorModel textEditorModel;
    private Location prevCursorLocation;

    public CursorMoveEnd(TextEditorModel textEditorModel) {
        this.textEditorModel = textEditorModel;
    }

    @Override
    public void execute_do() {
        prevCursorLocation = textEditorModel.getCursorLocation();
        int lastRow = textEditorModel.getLines().size() - 1;
        int lastColumn = textEditorModel.getLines().get(lastRow).length() - 1;
        textEditorModel.setCursorLocation(new Location(lastColumn,lastRow));
    }

    @Override
    public void execute_undo() {
        textEditorModel.setCursorLocation(prevCursorLocation);
    }
}

package hr.fer.zemris.ooup.texteditor.command;

import hr.fer.zemris.ooup.texteditor.model.Location;
import hr.fer.zemris.ooup.texteditor.model.LocationRange;
import hr.fer.zemris.ooup.texteditor.model.TextEditorModel;

import java.util.ArrayList;
import java.util.List;

public class CursorMoveStart implements EditAction {
    private final TextEditorModel textEditorModel;
    private Location prevCursorLocation;

    public CursorMoveStart(TextEditorModel textEditorModel) {
        this.textEditorModel = textEditorModel;
    }

    @Override
    public void execute_do() {
        prevCursorLocation = textEditorModel.getCursorLocation();
        textEditorModel.setCursorLocation(new Location(-1,0));
    }

    @Override
    public void execute_undo() {
        textEditorModel.setCursorLocation(prevCursorLocation);
    }
}

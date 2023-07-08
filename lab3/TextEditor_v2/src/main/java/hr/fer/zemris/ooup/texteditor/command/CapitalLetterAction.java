package hr.fer.zemris.ooup.texteditor.command;

import hr.fer.zemris.ooup.texteditor.model.TextEditorModel;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class CapitalLetterAction implements EditAction{
    private final TextEditorModel textEditorModel;
    private List<String> prevLines;

    public CapitalLetterAction(TextEditorModel textEditorModel){
        this.textEditorModel = textEditorModel;
    }
    @Override
    public void execute_do() {
        prevLines = new ArrayList<>(textEditorModel.getLines());

        List<String> newLines = new ArrayList<>();

        for(String line : textEditorModel.getLines()){
            StringBuilder sb = new StringBuilder();
            for(String word: line.split(" ")){
                String save = Pattern.compile("^.").matcher(word).replaceFirst(m -> m.group().toUpperCase());
                sb.append(save).append(" ");
            }
            newLines.add(sb.toString());
        }

        textEditorModel.setLines(newLines);
    }

    @Override
    public void execute_undo() {
        textEditorModel.setLines(prevLines);
    }
}

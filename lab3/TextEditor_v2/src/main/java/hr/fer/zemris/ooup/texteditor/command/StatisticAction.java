package hr.fer.zemris.ooup.texteditor.command;

import hr.fer.zemris.ooup.texteditor.demo.Demo;
import hr.fer.zemris.ooup.texteditor.model.TextEditorModel;

import javax.swing.*;
import java.util.List;

public class StatisticAction implements EditAction {
    private final TextEditorModel textEditorModel;
    private final JFrame frame;

    public StatisticAction(TextEditorModel textEditorModel, JFrame frame) {
        this.textEditorModel = textEditorModel;
        this.frame = frame;
    }

    @Override
    public void execute_do() {
        List<String> lines = textEditorModel.getLines();
        int numberOfRows = lines.size();

        int numOfWords = 0;
        int numOfLetters = 0;

        for (String line : lines) {
            for (String word : line.split(" ")) {
                boolean hasLetter = false;

                for (char c : word.toCharArray()) {
                    if (Character.isLetter(c)) {
                        numOfLetters++;
                        hasLetter = true;
                    }

                }

                if (hasLetter) numOfWords++;
            }
        }


        JOptionPane.showMessageDialog(
                frame,
                String.format("Number of words : %d\nNumber of letters: %d\nNumber of rows: %d", numOfWords, numOfLetters, numberOfRows),
                "Statistic",
                JOptionPane.INFORMATION_MESSAGE
        );

    }

    @Override
    public void execute_undo() {

    }
}

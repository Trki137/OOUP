package hr.fer.zemris.ooup.texteditor.component;

import hr.fer.zemris.ooup.texteditor.observer.ClipboardObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ClipboardStack {
    private final Stack<String> texts = new Stack<>();

    private final List<ClipboardObserver> clipboardObserverList = new ArrayList<>();

    public void pushText(String text) {
        texts.push(text);
        notifyAllListeners();
    }

    public String popText() {
        String text = texts.pop();
        notifyAllListeners();
        return text;
    }

    public String peekText() {
        return texts.peek();
    }

    public void clearClipboard() {
        texts.clear();
        notifyAllListeners();
    }

    public boolean hasText() {
        return !texts.isEmpty();
    }

    public void addClipboardListener(ClipboardObserver observer) {
        clipboardObserverList.add(observer);
    }

    public void removeClipboardListener(ClipboardObserver observer) {
        clipboardObserverList.remove(observer);
    }

    public void notifyAllListeners() {
        clipboardObserverList.forEach(ClipboardObserver::updateClipboard);
    }
}

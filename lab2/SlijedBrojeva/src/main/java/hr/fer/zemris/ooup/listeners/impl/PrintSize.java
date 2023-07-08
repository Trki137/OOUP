package hr.fer.zemris.ooup.listeners.impl;

import hr.fer.zemris.ooup.SlijedBrojeva;
import hr.fer.zemris.ooup.listeners.NumberSequenceListener;

import java.util.Objects;

public class PrintSize implements NumberSequenceListener {
    private final SlijedBrojeva slijedBrojeva;

    public PrintSize(SlijedBrojeva slijedBrojeva){
        Objects.requireNonNull(slijedBrojeva);

        this.slijedBrojeva = slijedBrojeva;
    }
    @Override
    public void update() {
        System.out.println("Size: "+slijedBrojeva.getIntegerList().size());
    }
}

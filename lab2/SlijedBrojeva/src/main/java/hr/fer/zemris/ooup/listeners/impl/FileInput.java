package hr.fer.zemris.ooup.listeners.impl;

import hr.fer.zemris.ooup.SlijedBrojeva;
import hr.fer.zemris.ooup.listeners.NumberSequenceListener;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class FileInput implements NumberSequenceListener {

    private final Path path = Path.of("./output.txt");

    private final SlijedBrojeva slijedBrojeva;

    public FileInput(SlijedBrojeva slijedBrojeva){
        Objects.requireNonNull(slijedBrojeva);

        this.slijedBrojeva = slijedBrojeva;
    }

    @Override
    public void update() {
        try {
            if (!Files.exists(path))
                Files.createFile(path);

            LocalDateTime now =  LocalDateTime.now();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

            String dateAndTimeFormatted = String.format("[%s]\n", now.format(formatter));

            Files.writeString(path,dateAndTimeFormatted,StandardOpenOption.TRUNCATE_EXISTING);

            List<Integer> integers = slijedBrojeva.getIntegerList();

            for(Integer num : integers){
                Files.writeString(
                        path,
                        String.format("%d\n",num),
                        StandardOpenOption.APPEND
                        );
            }


        }catch (IOException e){
            System.out.println("Couldn't write to a file");
        }
    }
}

package app.audio.Files;

import app.audio.LibraryEntry;
import lombok.Getter;

@Getter
public abstract class AudioFile extends LibraryEntry {
    private final Integer duration;

    public AudioFile(String name, Integer duration) {
        super(name);
        this.duration = duration;
    }
}

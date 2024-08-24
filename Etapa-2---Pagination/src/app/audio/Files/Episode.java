package app.audio.Files;

import lombok.Getter;

@Getter
public final class Episode extends AudioFile {
    private final String description;

    public Episode(final String name, final Integer duration, final String description) {
        super(name, duration);
        this.description = description;
    }
}

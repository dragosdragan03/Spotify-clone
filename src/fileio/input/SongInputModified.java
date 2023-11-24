package fileio.input;

public class SongInputModified {

    private SongInput song;
    private int repeat; // vreau sa vad daca melodia este pusa pe repeat sau nu

    public SongInputModified(final SongInput song, final int status) {
        this.song = song;
        this.repeat = status;
    }

    public final SongInput getSong() {
        return song;
    }

    public final int getRepeat() {
        return repeat;
    }

    public final void setRepeat(final int repeat) {
        this.repeat = repeat;
    }
}

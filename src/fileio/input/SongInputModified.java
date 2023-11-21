package fileio.input;

public class SongInputModified {

    private SongInput song;
    private int repeat; // vreau sa vad daca melodia este pusa pe repeat sau nu

    public SongInputModified(SongInput song, int status) {
        this.song = song;
        this.repeat = status;
    }

    public SongInput getSong() {
        return song;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }
}

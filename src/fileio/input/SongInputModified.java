package fileio.input;

public class SongInputModified {

    private SongInput song;
    private int status;

    public SongInputModified(SongInput song, int status) {
        this.song = song;
        this.status = status;
    }

    public SongInput getSong() {
        return song;
    }

    public int getStatus() {
        return status;
    }
}

package fileio.input;


import java.util.ArrayList;

public class PodcastInputModified {
    private String name;
    private String owner;
    private ArrayList<EpisodeInput> episodes;
    private int listeningTimePodcast; // retin sa vad cat timp a ascultat din podcast
    private int repeat;

    public PodcastInputModified(final String name, final String owner,
           final ArrayList<EpisodeInput> episodes, final int listeningTimePodcast) {
        this.name = name;
        this.owner = owner;
        this.episodes = episodes;
        this.listeningTimePodcast = listeningTimePodcast;
    }

    public final String getName() {
        return name;
    }

    public final void setName(final String name) {
        this.name = name;
    }

    public final String getOwner() {
        return owner;
    }

    public final void setOwner(final String owner) {
        this.owner = owner;
    }

    public final int getListeningTimePodcast() {
        return listeningTimePodcast;
    }

    public final int getRepeat() {
        return repeat;
    }

    public final void setRepeat(final int repeat) {
        this.repeat = repeat;
    }
}

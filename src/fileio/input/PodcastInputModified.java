package fileio.input;


import java.util.ArrayList;

public class PodcastInputModified {
    private String name;
    private String owner;
    private ArrayList<EpisodeInput> episodes;
    private int listeningTimePodcast; // retin sa vad cat timp a ascultat din podcast
    private int repeat;

    public PodcastInputModified(String name, String owner, ArrayList<EpisodeInput> episodes, int listeningTimePodcast) {
        this.name = name;
        this.owner = owner;
        this.episodes = episodes;
        this.listeningTimePodcast = listeningTimePodcast;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(final String owner) {
        this.owner = owner;
    }

    public int getListeningTimePodcast() {
        return listeningTimePodcast;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }
}

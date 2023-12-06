package fileio.input;

import app.audio.Files.Episode;

import java.util.ArrayList;

public final class PodcastInput {
    private String name;
    private String owner;
    private ArrayList<EpisodeInput> episodes;

    public PodcastInput() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public ArrayList<EpisodeInput> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(ArrayList<EpisodeInput> episodes) {
        this.episodes = episodes;
    }

    @Override
    public String toString() {
        return "PodcastInput{" +
                "name='" + name + '\'' +
                ", owner='" + owner + '\'' +
                ", episodes=" + episodes +
                '}';
    }
}

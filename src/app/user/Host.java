package app.user;

import app.Admin;
import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import fileio.input.EpisodeInput;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Host extends User {

    private List<Podcast> podcasts;
    private List<Announcement> announcements;

    public Host(final String username, final int age, final String city) {
        super(username, age, city);
        this.podcasts = new ArrayList<>();
        this.announcements = new ArrayList<>();
    }

    public final class Announcement {
        @Getter
        private String name;
        @Getter
        private String description;

        public Announcement(String name, String description) {
            this.name = name;
            this.description = description;
        }
    }

    @Override
    public List<Podcast> getPodcastsHost() {
        return podcasts;
    }

    @Override
    public List<Announcement> getAnnouncementsHost() {
        return announcements;
    }

    public boolean isHost() {
        return true;
    }

    private boolean isDuplicated(final List<EpisodeInput> episodes) {
        Set<String> uniqueItems = new HashSet<>();
        for (EpisodeInput iterEpisode : episodes) {
            if (!uniqueItems.add(iterEpisode.getName())) {
                return true; // inseamna ca este duplicat
            }
        }
        return false; // inseamna ca nu este duplicat
    }

    @Override
    public String addPodcast(final String name, final List<EpisodeInput> episodes) {
        for (Podcast iterPodcast : podcasts) {
            if (iterPodcast.getName().equals(name)) {
                return getUsername() + " has another podcast with the same name.";
            }
        }
        // insemana ca nu a mai gasit u n podcast cu acelasi nume
        if (isDuplicated(episodes)) {
            return getUsername() + " has the same episode in this podcast.";
        }

        List<Episode> newListEpisode = new ArrayList<>();
        for (EpisodeInput iterEpisode : episodes) {
            Episode episode = new Episode(iterEpisode.getName(),
                    iterEpisode.getDuration(), iterEpisode.getDescription());
            newListEpisode.add(episode);
        }
        Podcast newPodcast = new Podcast(name, getUsername(), newListEpisode);
        podcasts.add(newPodcast);
        // verific daca mai exista deja podcastul in lista mare de podcasturi
        if (!Admin.getPodcasts().contains(newPodcast)) {
            Admin.getPodcasts().add(newPodcast);
        }

        return getUsername() + " has added new podcast successfully.";
    }

    @Override
    public String addAnnouncement(final String name, final String description) {
        for (Announcement iterAnnouncement : announcements) {
            if (iterAnnouncement.getName().equals(name)) {
                return getUsername() + " has already added an announcement with this name.";
            }
        }
        // inseamna ca nu mai exista acelasi anunt
        Announcement newAnnouncement = new Announcement(name, description);
        announcements.add(newAnnouncement);
        return getUsername() + " has successfully added new announcement.";
    }

    @Override
    public String removeAnnouncement(final String name) {
        for (Announcement iterAnnouncement : announcements) {
            if (iterAnnouncement.getName().equals(name)) {
                announcements.remove(iterAnnouncement); // scot anuntul din lista
                return getUsername() + " has successfully deleted the announcement.";
            }
        }
        return getUsername() + " has no announcement with the given name.";
    }

    @Override
    public String removePodcast(final String name) {
        for (User iterUser : Admin.getUsers()) { // parcurg toata lista de useri sa vad daca cineva a incarcat albumul respectiv
            if (!(iterUser.equals(getUsername()))) { // sa fie diferit de artistul respectiv
                if (iterUser.isListening(getUsername())) {
                    return getUsername() + " can't delete this podcast.";
                }
            }
        }

        for (Podcast iterPodcast : podcasts) {
            if (iterPodcast.getName().equals(name)) {
                podcasts.remove(iterPodcast);
                Admin.getPodcasts().remove(iterPodcast);
                return getUsername() + " deleted the podcast successfully.";
            }
        }
        return getUsername() + " doesn't have a podcast with the given name.";
    }
}

package app.pageSystem;

import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.user.Host;
import app.user.User;

import java.util.ArrayList;
import java.util.List;

public class HostPage implements Page {

    public String printCurrentPage(User user) {
        List<String> podcastsName = new ArrayList<>();
        List<String> announcements = new ArrayList<>();
        List<String> episodes = new ArrayList<>();
        for (Podcast iterPodcast : user.getPodcastsHost()) {
            for (Episode iterEpisode : iterPodcast.getEpisodes()) {
                episodes.add(iterEpisode.getName() + " - " + iterEpisode.getDescription());
            }
            podcastsName.add(iterPodcast.getName() + ":\n\t" + episodes + "\n");
            episodes = new ArrayList<>();
        }

        for (Host.Announcement iterAnnouncement : user.getAnnouncementsHost()) {
            announcements.add(iterAnnouncement.getName() + ":\n\t"
                    + iterAnnouncement.getDescription() + "\n");
        }

        return "Podcasts:\n\t" + podcastsName
                + "\n\nAnnouncements:\n\t" + announcements;
    }
}
